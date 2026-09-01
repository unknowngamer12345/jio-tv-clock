package com.tv.overlayclock

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.WindowManager
import android.widget.TextClock
import androidx.core.app.NotificationCompat

class OverlayClockService : Service() {

    private var windowManager: WindowManager? = null
    private var clockView: TextClock? = null

    override fun onCreate() {
        super.onCreate()
        startNotification()

        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        // Styling: Translucent dark badge
        val badgeBackground = GradientDrawable().apply {
            setColor(Color.parseColor("#80000000")) // 50% opacity black
            cornerRadius = 16f
        }

        clockView = TextClock(this).apply {
            format12Hour = "hh:mm a"
            format24Hour = "HH:mm"
            setTextColor(Color.WHITE)
            textSize = 15f
            background = badgeBackground
            setPadding(20, 8, 20, 8)
        }

        val layoutParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.END
            x = 48 // margin right (pixels)
            y = 36 // margin top (pixels)
        }

        windowManager?.addView(clockView, layoutParams)
    }

    private fun startNotification() {
        val channelId = "clock_overlay"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Jio TV Clock",
                NotificationManager.IMPORTANCE_LOW
            )
            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Jio TV Clock Running")
            .setSmallIcon(android.R.drawable.ic_menu_recent_history)
            .build()

        startForeground(1, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        clockView?.let { windowManager?.removeView(it) }
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
