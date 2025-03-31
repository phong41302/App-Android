package com.example.th5_bai6_record

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.io.File

class MainActivity : AppCompatActivity() {
    private var mediaRecorder: MediaRecorder? = null
    private var mediaPlayer: MediaPlayer? = null
    private var audioFilePath: String? = null
    private lateinit var btnRecord: Button
    private lateinit var btnStop: Button
    private lateinit var btnPlay: Button
    private lateinit var btnList: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnRecord = findViewById(R.id.btnRecord)
        btnStop = findViewById(R.id.btnStop)
        btnPlay = findViewById(R.id.btnPlay)
        btnList = findViewById(R.id.btnList)

        btnRecord.setOnClickListener { startRecording() }
        btnStop.setOnClickListener { stopRecording() }
        btnPlay.setOnClickListener { playRecording() }
        btnList.setOnClickListener { showRecordings() }
    }

    private fun startRecording() {
        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), 1001)
            return
        }

        val fileName = "record_${System.currentTimeMillis()}.mp3"
        val file = File(externalCacheDir, fileName)
        audioFilePath = file.absolutePath

        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setOutputFile(audioFilePath)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)

            try {
                prepare()
                start()
                btnRecord.isEnabled = false
                btnStop.isEnabled = true
                Toast.makeText(this@MainActivity, "Bắt đầu ghi âm...", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun stopRecording() {
        mediaRecorder?.apply {
            stop()
            release()
            mediaRecorder = null
        }

        btnRecord.isEnabled = true
        btnStop.isEnabled = false
        saveRecordingToMediaStore()
    }

    private fun saveRecordingToMediaStore() {
        val values = ContentValues().apply {
            put(MediaStore.Audio.Media.DISPLAY_NAME, "recording_${System.currentTimeMillis()}.mp3")
            put(MediaStore.Audio.Media.MIME_TYPE, "audio/mpeg")
            put(MediaStore.Audio.Media.RELATIVE_PATH, "Music/Recordings/")
        }

        val uri: Uri? = contentResolver.insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values)
        uri?.let {
            Toast.makeText(this, "Ghi âm đã lưu!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun playRecording() {
        if (audioFilePath == null) {
            Toast.makeText(this, "Không có file để phát", Toast.LENGTH_SHORT).show()
            return
        }

        mediaPlayer = MediaPlayer().apply {
            setDataSource(audioFilePath)
            prepare()
            start()
            setOnCompletionListener {
                Toast.makeText(this@MainActivity, "Phát xong!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showRecordings() {
        val intent = android.content.Intent(this, RecordingsActivity::class.java)
        startActivity(intent)
    }
}
