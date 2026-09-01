package com.tv.overlayclock

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.content.ContextCompat

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(64, 64, 64, 64)
        }

        val startButton = Button(this).apply {
            text = "START OVERLAY CLOCK"
            textSize = 18f
            setOnClickListener {
                val serviceIntent = Intent(this@MainActivity, OverlayClockService::class.java)
                ContextCompat.startForegroundService(this@MainActivity, serviceIntent)
            }
        }

        val stopButton = Button(this).apply {
            text = "STOP OVERLAY CLOCK"
            textSize = 18f
            setOnClickListener {
                stopService(Intent(this@MainActivity, OverlayClockService::class.java))
            }
        }

        layout.addView(startButton)
        layout.addView(stopButton)
        setContentView(layout)
    }
}
