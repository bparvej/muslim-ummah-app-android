package com.example.muslimummah.repository;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.example.muslimummah.config.AppConfig;
import com.example.muslimummah.models.Mosque;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MosqueRepository {

    public interface MosqueCallback {
        void onSuccess(List<Mosque> mosques);
        void onError(Exception e);
    }

    private final AppConfig appConfig;
    private final ExecutorService executorService;
    private final Handler mainHandler;

    public MosqueRepository(Context context) {
        this.appConfig = AppConfig.getInstance(context);
        this.executorService = Executors.newSingleThreadExecutor();
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    public void fetchNearbyMosques(MosqueCallback callback) {
        executorService.execute(() -> {
            List<Mosque> mosqueList = new ArrayList<>();
            try {
                double baseLat = appConfig.getBaseLat();
                double baseLng = appConfig.getBaseLng();
                double radiusKm = appConfig.getSearchRadiusKm();

                double degDelta = (radiusKm * 1.3) / 111.0;
                String left = String.format("%.5f", baseLng - degDelta);
                String right = String.format("%.5f", baseLng + degDelta);
                String top = String.format("%.5f", baseLat + degDelta);
                String bottom = String.format("%.5f", baseLat - degDelta);

                String queryUrl = appConfig.getOsmNominatimApiUrl() +
                        "?q=mosque&format=json&limit=15&viewbox=" + left + "," + top + "," + right + "," + bottom + "&bounded=1";

                URL url = new URL(queryUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("User-Agent", "MuslimUmmahAndroid/2.0");
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

                    JSONArray rawArray = new JSONArray(sb.toString());
                    for (int i = 0; i < rawArray.length(); i++) {
                        JSONObject item = rawArray.getJSONObject(i);
                        double lat = item.getDouble("lat");
                        double lon = item.getDouble("lon");
                        double dist = calculateDistanceKm(baseLat, baseLng, lat, lon);

                        if (dist <= radiusKm * 1.6) {
                            String name = item.optString("name");
                            if (name.isEmpty()) {
                                name = item.optString("display_name").split(",")[0];
                            }
                            mosqueList.add(new Mosque(
                                    name,
                                    item.optString("display_name"),
                                    lat,
                                    lon,
                                    dist,
                                    "https://images.unsplash.com/photo-1591604129939-f1efa4d9f7fa?auto=format&fit=crop&q=80&w=700",
                                    getDefaultJamaatSchedule()
                            ));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (mosqueList.isEmpty()) {
                mosqueList = getFallbackMosques();
            }

            List<Mosque> finalResult = mosqueList;
            mainHandler.post(() -> callback.onSuccess(finalResult));
        });
    }

    private Map<String, String> getDefaultJamaatSchedule() {
        Map<String, String> map = new HashMap<>();
        map.put("Fajr", "05:30");
        map.put("Dhuhr", "13:15");
        map.put("Asr", "16:15");
        map.put("Maghrib", "17:25");
        map.put("Isha", "19:45");
        map.put("Juma", "13:30");
        return map;
    }

    private List<Mosque> getFallbackMosques() {
        List<Mosque> list = new ArrayList<>();
        double baseLat = appConfig.getBaseLat();
        double baseLng = appConfig.getBaseLng();

        list.add(new Mosque(
                "নূরপুর সেন্ট্রাল জামে মসজিদ",
                "সেন্ট্রাল রোড, নূরপুর, দনিয়া, যাত্রাবাড়ী, ঢাকা",
                23.6987, 90.4498,
                calculateDistanceKm(baseLat, baseLng, 23.6987, 90.4498),
                "https://images.unsplash.com/photo-1591604129939-f1efa4d9f7fa?auto=format&fit=crop&q=80&w=700",
                getDefaultJamaatSchedule()
        ));
        list.add(new Mosque(
                "দনিয়া কেন্দ্রীয় জামে মসজিদ",
                "দনিয়া বাজার রোড, যাত্রাবাড়ী, ঢাকা",
                23.6952, 90.4532,
                calculateDistanceKm(baseLat, baseLng, 23.6952, 90.4532),
                "https://images.unsplash.com/photo-1542816417-0983c9c9ad53?auto=format&fit=crop&q=80&w=700",
                getDefaultJamaatSchedule()
        ));
        list.add(new Mosque(
                "বায়তুল মোকাররম জাতীয় মসজিদ",
                "পল্টন, ঢাকা",
                23.7297, 90.4125,
                1.2,
                "https://images.unsplash.com/photo-1519817650390-64a93db51149?auto=format&fit=crop&q=80&w=700",
                getDefaultJamaatSchedule()
        ));
        return list;
    }

    private double calculateDistanceKm(double lat1, double lon1, double lat2, double lon2) {
        double r = 6371.0;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return Math.round(r * c * 100.0) / 100.0;
    }
}
