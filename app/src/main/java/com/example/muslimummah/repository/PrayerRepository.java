package com.example.muslimummah.repository;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.example.muslimummah.config.AppConfig;
import com.example.muslimummah.models.PrayerTimings;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PrayerRepository {

    public interface PrayerCallback {
        void onSuccess(PrayerTimings timings);
        void onError(Exception e);
    }

    private final AppConfig appConfig;
    private final ExecutorService executorService;
    private final Handler mainHandler;

    public PrayerRepository(Context context) {
        this.appConfig = AppConfig.getInstance(context);
        this.executorService = Executors.newSingleThreadExecutor();
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    public void fetchPrayerTimings(PrayerCallback callback) {
        executorService.execute(() -> {
            try {
                double lat = appConfig.getBaseLat();
                double lng = appConfig.getBaseLng();
                int method = appConfig.getCalcMethod();
                String baseUrl = appConfig.getPrayerApiBaseUrl();

                String urlString = baseUrl + "/timings?latitude=" + lat + "&longitude=" + lng + "&method=" + method;
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(8000);
                conn.setReadTimeout(8000);

                if (conn.getResponseCode() == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    reader.close();

                    JSONObject root = new JSONObject(sb.toString());
                    JSONObject timingsObj = root.getJSONObject("data").getJSONObject("timings");

                    PrayerTimings timings = new PrayerTimings(
                            cleanTime(timingsObj.getString("Fajr")),
                            cleanTime(timingsObj.getString("Sunrise")),
                            cleanTime(timingsObj.getString("Dhuhr")),
                            cleanTime(timingsObj.getString("Asr")),
                            cleanTime(timingsObj.getString("Maghrib")),
                            cleanTime(timingsObj.getString("Isha")),
                            cleanTime(timingsObj.optString("Imsak", "04:42"))
                    );

                    mainHandler.post(() -> callback.onSuccess(timings));
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Fallback default timings
            PrayerTimings fallback = new PrayerTimings(
                    "04:52", "06:09", "11:42", "14:53", "17:15", "18:32", "04:42"
            );
            mainHandler.post(() -> callback.onSuccess(fallback));
        });
    }

    private String cleanTime(String raw) {
        if (raw == null) return "--:--";
        return raw.split(" ")[0];
    }
}
