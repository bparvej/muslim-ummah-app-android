package com.example.muslimummah.alarm;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.util.Calendar;

public class AzanScheduler {

    public static final String ACTION_AZAN_ALARM = "com.example.muslimummah.ACTION_AZAN_ALARM";
    public static final String EXTRA_PRAYER_NAME = "extra_prayer_name";
    public static final String EXTRA_TONE_KEY = "extra_tone_key";

    private final Context context;
    private final AlarmManager alarmManager;

    public AzanScheduler(Context context) {
        this.context = context.getApplicationContext();
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    @SuppressLint("ScheduleExactAlarm")
    public void scheduleAlarm(String prayerName, String timeHHMM, String toneKey) {
        if (timeHHMM == null || !timeHHMM.contains(":")) return;

        String[] parts = timeHHMM.split(":");
        try {
            int hour = Integer.parseInt(parts[0]);
            int minute = Integer.parseInt(parts[1]);

            Calendar now = Calendar.getInstance();
            Calendar target = Calendar.getInstance();
            target.set(Calendar.HOUR_OF_DAY, hour);
            target.set(Calendar.MINUTE, minute);
            target.set(Calendar.SECOND, 0);
            target.set(Calendar.MILLISECOND, 0);

            if (target.before(now)) {
                target.add(Calendar.DAY_OF_YEAR, 1);
            }

            Intent intent = new Intent(context, AzanAlarmReceiver.class);
            intent.setAction(ACTION_AZAN_ALARM);
            intent.putExtra(EXTRA_PRAYER_NAME, prayerName);
            intent.putExtra(EXTRA_TONE_KEY, toneKey);

            int requestCode = prayerName.hashCode();
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context,
                    requestCode,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, target.getTimeInMillis(), pendingIntent);
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, target.getTimeInMillis(), pendingIntent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cancelAlarm(String prayerName) {
        Intent intent = new Intent(context, AzanAlarmReceiver.class);
        intent.setAction(ACTION_AZAN_ALARM);
        int requestCode = prayerName.hashCode();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_NO_CREATE | PendingIntent.FLAG_IMMUTABLE
        );
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
    }
}
