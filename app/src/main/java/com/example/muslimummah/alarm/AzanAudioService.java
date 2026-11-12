package com.example.muslimummah.alarm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import androidx.core.app.NotificationCompat;

import com.example.muslimummah.R;
import com.example.muslimummah.ui.MainActivity;

public class AzanAudioService extends Service {

    private static final String CHANNEL_ID = "azan_alarm_channel";
    private static final int NOTIFICATION_ID = 1001;
    private static final long MAX_AUDIO_DURATION_MS = 10000L; // 10 seconds max playback

    private MediaPlayer mediaPlayer;
    private Handler autoStopHandler;
    private Runnable autoStopRunnable;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String prayerName = intent != null ? intent.getStringExtra(AzanScheduler.EXTRA_PRAYER_NAME) : "Prayer";
        if (prayerName == null) prayerName = "Prayer";

        String toneKey = intent != null ? intent.getStringExtra(AzanScheduler.EXTRA_TONE_KEY) : "baitul-mukarrom";
        if (toneKey == null) toneKey = "baitul-mukarrom";

        createNotificationChannel();
        Notification notification = buildNotification(prayerName);
        startForeground(NOTIFICATION_ID, notification);

        playAzanTone(toneKey);

        return START_NOT_STICKY;
    }

    private void playAzanTone(String toneKey) {
        stopAudio();

        int rawResId;
        switch (toneKey.toLowerCase()) {
            case "makkah":
                rawResId = R.raw.makkah;
                break;
            case "madinah":
                rawResId = R.raw.madinah;
                break;
            case "al-aqsa":
            case "alaqsa":
                rawResId = R.raw.al_aqsa;
                break;
            default:
                rawResId = R.raw.baitul_mukarrom;
                break;
        }

        try {
            mediaPlayer = MediaPlayer.create(this, rawResId);
            if (mediaPlayer != null) {
                mediaPlayer.setAudioAttributes(
                        new AudioAttributes.Builder()
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .setUsage(AudioAttributes.USAGE_ALARM)
                                .build()
                );
                mediaPlayer.setOnCompletionListener(mp -> stopSelf());
                mediaPlayer.start();

                // Enforce maximum 10 seconds audio playback duration limit
                autoStopHandler = new Handler(Looper.getMainLooper());
                autoStopRunnable = () -> {
                    stopAudio();
                    stopSelf();
                };
                autoStopHandler.postDelayed(autoStopRunnable, MAX_AUDIO_DURATION_MS);
            } else {
                stopSelf();
            }
        } catch (Exception e) {
            e.printStackTrace();
            stopSelf();
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Muslim Ummah Azan Alarm",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Azan Prayer Alarm Notifications");
            channel.setSound(null, null);
            channel.enableVibration(true);

            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    private Notification buildNotification(String prayerName) {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("নামাজের অ্যালার্ম: " + prayerName)
                .setContentText(prayerName + " ওয়াক্ত উপস্থিত হয়েছে। নামাজের প্রস্তুতি নিন।")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();
    }

    private void stopAudio() {
        if (autoStopHandler != null && autoStopRunnable != null) {
            autoStopHandler.removeCallbacks(autoStopRunnable);
        }
        if (mediaPlayer != null) {
            try {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
            } catch (Exception ignored) {}
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onDestroy() {
        stopAudio();
        super.onDestroy() ;
    }
}
