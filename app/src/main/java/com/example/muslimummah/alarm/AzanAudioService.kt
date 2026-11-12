package com.example.muslimummah.alarm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.muslimummah.MainActivity
import com.example.muslimummah.R

class AzanAudioService : Service() {

    private var mediaPlayer: MediaPlayer? = null

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val prayerName = intent?.getStringExtra(AzanScheduler.EXTRA_PRAYER_NAME) ?: "Prayer"
        val toneKey = intent?.getStringExtra(AzanScheduler.EXTRA_TONE_KEY) ?: "baitul-mukarrom"

        createNotificationChannel()
        val notification = buildNotification(prayerName)
        startForeground(NOTIFICATION_ID, notification)

        playAzanTone(toneKey)

        return START_NOT_STICKY
    }

    private fun playAzanTone(toneKey: String) {
        stopAudio()

        val rawResId = when (toneKey.lowercase()) {
            "makkah" -> R.raw.makkah
            "madinah" -> R.raw.madinah
            "al-aqsa", "alaqsa" -> R.raw.al_aqsa
            else -> R.raw.baitul_mukarrom
        }

        try {
            mediaPlayer = MediaPlayer.create(this, rawResId)?.apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .build()
                )
                setOnCompletionListener {
                    stopSelf()
                }
                start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            stopSelf()
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Muslim Ummah Azan Alarm",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Azan and Prayer Alarm Notifications"
                setSound(null, null)
                enableVibration(true)
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    private fun buildNotification(prayerName: String): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("নামাজের অ্যালার্ম: $prayerName")
            .setContentText("$prayerName ওয়াক্ত উপস্থিত হয়েছে। নামাজের প্রস্তুতি নিন।")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
    }

    private fun stopAudio() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
            }
            it.release()
        }
        mediaPlayer = null
    }

    override fun onDestroy() {
        stopAudio()
        super.onDestroy()
    }

    companion object {
        private const val CHANNEL_ID = "azan_alarm_channel"
        private const val NOTIFICATION_ID = 1001
    }
}
