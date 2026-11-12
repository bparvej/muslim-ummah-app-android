package com.example.muslimummah.config;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.muslimummah.R;

public class AppConfig {

    private static final String PREF_NAME = "muslim_ummah_config_prefs";
    private static final String KEY_LAT = "key_base_lat";
    private static final String KEY_LNG = "key_base_lng";
    private static final String KEY_LOCATION_NAME = "key_location_name";
    private static final String KEY_RADIUS = "key_radius_km";
    private static final String KEY_METHOD = "key_calc_method";
    private static final String KEY_AZAN_TONE = "key_azan_tone";

    private static AppConfig instance;
    private final SharedPreferences prefs;
    private final Context context;

    private AppConfig(Context context) {
        this.context = context.getApplicationContext();
        this.prefs = this.context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized AppConfig getInstance(Context context) {
        if (instance == null) {
            instance = new AppConfig(context);
        }
        return instance;
    }

    public double getBaseLat() {
        double defaultLat = Double.parseDouble(context.getString(R.string.config_base_lat));
        return (double) prefs.getFloat(KEY_LAT, (float) defaultLat);
    }

    public void setBaseLat(double lat) {
        prefs.edit().putFloat(KEY_LAT, (float) lat).apply();
    }

    public double getBaseLng() {
        double defaultLng = Double.parseDouble(context.getString(R.string.config_base_lng));
        return (double) prefs.getFloat(KEY_LNG, (float) defaultLng);
    }

    public void setBaseLng(double lng) {
        prefs.edit().putFloat(KEY_LNG, (float) lng).apply();
    }

    public String getBaseLocationName() {
        String defaultName = context.getString(R.string.config_base_location_name);
        return prefs.getString(KEY_LOCATION_NAME, defaultName);
    }

    public void setBaseLocationName(String name) {
        prefs.edit().putString(KEY_LOCATION_NAME, name).apply();
    }

    public double getSearchRadiusKm() {
        double defaultRadius = Double.parseDouble(context.getString(R.string.config_mosque_search_radius_km));
        return (double) prefs.getFloat(KEY_RADIUS, (float) defaultRadius);
    }

    public void setSearchRadiusKm(double radius) {
        prefs.edit().putFloat(KEY_RADIUS, (float) radius).apply();
    }

    public int getCalcMethod() {
        int defaultMethod = context.getResources().getInteger(R.integer.config_prayer_calc_method);
        return prefs.getInt(KEY_METHOD, defaultMethod);
    }

    public void setCalcMethod(int method) {
        prefs.edit().putInt(KEY_METHOD, method).apply();
    }

    public String getSelectedAzanTone() {
        return prefs.getString(KEY_AZAN_TONE, "baitul-mukarrom");
    }

    public void setSelectedAzanTone(String tone) {
        prefs.edit().putString(KEY_AZAN_TONE, tone).apply();
    }

    public String getPrayerApiBaseUrl() {
        return context.getString(R.string.config_prayer_api_base_url);
    }

    public String getOsmNominatimApiUrl() {
        return context.getString(R.string.config_osm_nominatim_api_url);
    }
}
