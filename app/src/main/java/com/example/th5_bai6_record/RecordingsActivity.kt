package com.example.th5_bai6_record

import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RecordingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val listView = ListView(this)
        setContentView(listView)

        val audioList = getAudioFiles()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, audioList)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val filePath = audioList[position]
            Toast.makeText(this, "Chọn file: $filePath", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getAudioFiles(): List<String> {
        val audioList = mutableListOf<String>()
        val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Audio.Media.DATA)
        val cursor: Cursor? = contentResolver.query(uri, projection, null, null, null)

        cursor?.use {
            val index = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            while (it.moveToNext()) {
                audioList.add(it.getString(index))
            }
        }
        return audioList
    }
}
