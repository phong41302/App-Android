package com.example.th5_bai4_downshowimage

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.widget.ImageView
import android.widget.ProgressBar
import java.net.HttpURLConnection
import java.net.URL

class DownloadImageTask(
    private val imageView: ImageView,
    private val progressBar: ProgressBar
) : AsyncTask<String, Int, Bitmap?>() {

    override fun onPreExecute() {
        super.onPreExecute()
        progressBar.visibility = ProgressBar.VISIBLE // Hiển thị ProgressBar
    }

    override fun doInBackground(vararg urls: String?): Bitmap? {
        val urlString = urls[0] ?: return null
        try {
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()

            val inputStream = connection.inputStream
            return BitmapFactory.decodeStream(inputStream) // Chuyển đổi thành Bitmap
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override fun onPostExecute(result: Bitmap?) {
        progressBar.visibility = ProgressBar.GONE // Ẩn ProgressBar
        if (result != null) {
            imageView.setImageBitmap(result) // Hiển thị ảnh
        }
    }
}
