package com.example.muslimummah.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class AzanAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String prayerName = intent.getStringExtra(AzanScheduler.EXTRA_PRAYER_NAME);
        if (prayerName == null) prayerName = "Prayer";

        String toneKey = intent.getStringExtra(AzanScheduler.EXTRA_TONE_KEY);
        if (toneKey == null) toneKey = "baitul-mukarrom";

        Intent serviceIntent = new Intent(context, AzanAudioService.class);
        serviceIntent.putExtra(AzanScheduler.EXTRA_PRAYER_NAME, prayerName);
        serviceIntent.putExtra(AzanScheduler.EXTRA_TONE_KEY, toneKey);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent);
        } else {
            context.startService(serviceIntent);
        }
    }
}
