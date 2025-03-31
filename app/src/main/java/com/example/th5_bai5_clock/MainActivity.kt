package com.example.th5_bai5_clock

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    private lateinit var tvTimer: TextView
    private lateinit var btnStart: Button
    private lateinit var btnStop: Button

    private var seconds = 0
    private var isRunning = false
    private var job: Job? = null // Coroutine Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvTimer = findViewById(R.id.tvTimer)
        btnStart = findViewById(R.id.btnStart)
        btnStop = findViewById(R.id.btnStop)

        btnStart.setOnClickListener { startTimer() }
        btnStop.setOnClickListener { stopTimer() }
    }

    private fun startTimer() {
        if (isRunning) return
        isRunning = true

        job = CoroutineScope(Dispatchers.Main).launch {
            while (isRunning) {
                delay(1000) // Dừng 1 giây
                seconds++
                tvTimer.text = seconds.toString() // Cập nhật UI trực tiếp
            }
        }
    }

    private fun stopTimer() {
        isRunning = false
        job?.cancel()
    }
}
