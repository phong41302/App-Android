package com.example.th5_bai4_downshowimage

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editTextUrl = findViewById<EditText>(R.id.editTextUrl)
        val buttonLoad = findViewById<Button>(R.id.buttonLoad)
        val imageView = findViewById<ImageView>(R.id.imageView)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        buttonLoad.setOnClickListener {
            val imageUrl = editTextUrl.text.toString().trim()
            if (imageUrl.isNotEmpty()) {
                progressBar.visibility = ProgressBar.VISIBLE
                coroutineScope.launch {
                    val bitmap = withContext(Dispatchers.IO) { loadImageFromUrl(imageUrl) }
                    progressBar.visibility = ProgressBar.GONE
                    bitmap?.let { imageView.setImageBitmap(it) }
                }
            }
        }
    }

    private fun loadImageFromUrl(urlString: String): Bitmap? {
        return try {
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val inputStream = connection.inputStream
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }
}
