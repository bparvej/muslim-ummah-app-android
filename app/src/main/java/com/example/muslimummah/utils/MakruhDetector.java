package com.example.muslimummah.utils;

import com.example.muslimummah.models.MakruhStatus;
import com.example.muslimummah.models.PrayerTimings;
import java.util.Calendar;

public class MakruhDetector {

    public static MakruhStatus checkMakruhStatus(PrayerTimings timings, Calendar now) {
        if (timings == null) {
            return new MakruhStatus(false, "", 0);
        }

        int curMins = now.get(Calendar.HOUR_OF_DAY) * 60 + now.get(Calendar.MINUTE);

        int sunriseM = parseMins(timings.getSunrise());
        int dhuhrM = parseMins(timings.getDhuhr());
        int maghribM = parseMins(timings.getMaghrib());

        // 1. Sunrise period: sunrise -> sunrise + 15m
        boolean isSunriseMakruh = curMins >= sunriseM && curMins < (sunriseM + 15);
        // 2. Zawwal/Midday period: Dhuhr - 12m -> Dhuhr
        boolean isMiddayMakruh = curMins >= (dhuhrM - 12) && curMins < dhuhrM;
        // 3. Sunset period: Maghrib - 15m -> Maghrib
        boolean isSunsetMakruh = curMins >= (maghribM - 15) && curMins < maghribM;

        if (isSunriseMakruh) {
            return new MakruhStatus(true, "সূর্যোদয়কালীন নিষিদ্ধ সময় (Makruh: Sunrise)", (sunriseM + 15) - curMins);
        } else if (isMiddayMakruh) {
            return new MakruhStatus(true, "দ্বিপ্রহর (জাওয়াল) নিষিদ্ধ সময় (Makruh: Zawwal)", dhuhrM - curMins);
        } else if (isSunsetMakruh) {
            return new MakruhStatus(true, "সূর্যাস্তকালীন নিষিদ্ধ সময় (Makruh: Sunset)", maghribM - curMins);
        }

        return new MakruhStatus(false, "", 0);
    }

    private static int parseMins(String timeStr) {
        if (timeStr == null || !timeStr.contains(":")) return 0;
        try {
            String clean = timeStr.split(" ")[0];
            String[] parts = clean.split(":");
            int h = Integer.parseInt(parts[0]);
            int m = Integer.parseInt(parts[1]);
            return h * 60 + m;
        } catch (Exception e) {
            return 0;
        }
    }
}
