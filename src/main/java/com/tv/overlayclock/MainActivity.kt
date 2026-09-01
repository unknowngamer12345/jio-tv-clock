package com.tv.overlayclock

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat

class MainActivity : Activity() {

    private val colorPresets = listOf(
        "White (Classic)" to "#FFFFFF",
        "Neon Cyan (HUD)" to "#00E5FF",
        "Cyber Green" to "#00E676",
        "Amber Orange" to "#FF9100",
        "Electric Yellow" to "#FFEA00"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("TVClockPrefs", Context.MODE_PRIVATE)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(64, 48, 64, 48)
        }

        val title = TextView(this).apply {
            text = "Jio TV Clock Settings"
            textSize = 24f
            setTextColor(Color.WHITE)
            setPadding(0, 0, 0, 24)
        }
        layout.addView(title)

        // Control Buttons
        val startButton = Button(this).apply {
            text = "START / RESTART CLOCK"
            textSize = 16f
            setOnClickListener {
                val serviceIntent = Intent(this@MainActivity, OverlayClockService::class.java)
                ContextCompat.startForegroundService(this@MainActivity, serviceIntent)
            }
        }

        val stopButton = Button(this).apply {
            text = "STOP CLOCK"
            textSize = 16f
            setOnClickListener {
                stopService(Intent(this@MainActivity, OverlayClockService::class.java))
            }
        }

        layout.addView(startButton)
        layout.addView(stopButton)

        val colorSectionLabel = TextView(this).apply {
            text = "Choose Clock Text Color:"
            textSize = 18f
            setTextColor(Color.LTGRAY)
            setPadding(0, 32, 0, 16)
        }
        layout.addView(colorSectionLabel)

        // Generate Preset Buttons
        for ((label, hexCode) in colorPresets) {
            val colorBtn = Button(this).apply {
                text = label
                setTextColor(Color.parseColor(hexCode))
                setOnClickListener {
                    // Save chosen color to SharedPreferences
                    prefs.edit().putString("clock_color", hexCode).apply()

                    // Restart service with new color
                    val serviceIntent = Intent(this@MainActivity, OverlayClockService::class.java)
                    ContextCompat.startForegroundService(this@MainActivity, serviceIntent)
                }
            }
            layout.addView(colorBtn)
        }

        setContentView(layout)
    }
}
