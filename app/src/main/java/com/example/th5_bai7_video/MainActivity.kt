package com.example.th5_bai7_video



import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.VideoView
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var videoView: VideoView
    private lateinit var btnPickVideo: Button
    private lateinit var edtVideoUrl: EditText
    private lateinit var btnPlayUrl: Button
    private val PICK_VIDEO_REQUEST = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Khởi tạo UI
        videoView = findViewById(R.id.videoView)
        btnPickVideo = findViewById(R.id.btnPickVideo)
        edtVideoUrl = findViewById(R.id.edtVideoUrl)
        btnPlayUrl = findViewById(R.id.btnPlayUrl)

        // Thêm MediaController
        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)

        // Sự kiện chọn video từ thiết bị
        btnPickVideo.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_VIDEO_REQUEST)
        }

        // Sự kiện phát video từ URL
        btnPlayUrl.setOnClickListener {
            val url = edtVideoUrl.text.toString().trim()
            if (url.isNotEmpty()) {
                playVideo(Uri.parse(url))
            } else {
                Toast.makeText(this, "Vui lòng nhập URL", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Xử lý kết quả chọn video từ thiết bị
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_VIDEO_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedVideoUri: Uri? = data.data
            if (selectedVideoUri != null) {
                playVideo(selectedVideoUri)
            } else {
                Toast.makeText(this, "Không thể lấy video", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Hàm phát video
    private fun playVideo(videoUri: Uri) {
        videoView.setVideoURI(videoUri)
        videoView.start()
    }
}
