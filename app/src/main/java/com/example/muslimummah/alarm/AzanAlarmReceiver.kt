package com.example.muslimummah.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

class AzanAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val prayerName = intent.getStringExtra(AzanScheduler.EXTRA_PRAYER_NAME) ?: "Prayer"
        val toneKey = intent.getStringExtra(AzanScheduler.EXTRA_TONE_KEY) ?: "baitul-mukarrom"

        val serviceIntent = Intent(context, AzanAudioService::class.java).apply {
            putExtra(AzanScheduler.EXTRA_PRAYER_NAME, prayerName)
            putExtra(AzanScheduler.EXTRA_TONE_KEY, toneKey)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent)
        } else {
            context.startService(serviceIntent)
        }
    }
}
