package com.example.muslimummah.data

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONObject

class AppPreferences(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var baseLat: Double
        get() = prefs.getFloat(KEY_BASE_LAT, 23.693639f).toDouble()
        set(value) = prefs.edit().putFloat(KEY_BASE_LAT, value.toFloat()).apply()

    var baseLng: Double
        get() = prefs.getFloat(KEY_BASE_LNG, 90.450333f).toDouble()
        set(value) = prefs.edit().putFloat(KEY_BASE_LNG, value.toFloat()).apply()

    var baseLocationName: String
        get() = prefs.getString(KEY_LOCATION_NAME, "যাত্রাবাড়ী/দনিয়া, ঢাকা") ?: "যাত্রাবাড়ী/দনিয়া, ঢাকা"
        set(value) = prefs.edit().putString(KEY_LOCATION_NAME, value).apply()

    var searchRadiusKm: Double
        get() = prefs.getFloat(KEY_RADIUS_KM, 1.0f).toDouble()
        set(value) = prefs.edit().putFloat(KEY_RADIUS_KM, value.toFloat()).apply()

    var calcMethod: Int
        get() = prefs.getInt(KEY_CALC_METHOD, 1)
        set(value) = prefs.edit().putInt(KEY_CALC_METHOD, value).apply()

    var selectedAzanTone: String
        get() = prefs.getString(KEY_AZAN_TONE, "baitul-mukarrom") ?: "baitul-mukarrom"
        set(value) = prefs.edit().putString(KEY_AZAN_TONE, value).apply()

    fun getConfigAsJson(): String {
        val json = JSONObject()
        json.put("BASE_LAT", baseLat)
        json.put("BASE_LNG", baseLng)
        json.put("BASE_LOCATION_NAME", baseLocationName)
        json.put("MOSQUE_SEARCH_RADIUS_KM", searchRadiusKm)
        json.put("PRAYER_CALC_METHOD", calcMethod)
        json.put("SELECTED_TONE", selectedAzanTone)
        return json.toString()
    }

    companion object {
        private const val PREFS_NAME = "muslim_ummah_prefs"
        private const val KEY_BASE_LAT = "base_lat"
        private const val KEY_BASE_LNG = "base_lng"
        private const val KEY_LOCATION_NAME = "location_name"
        private const val KEY_RADIUS_KM = "radius_km"
        private const val KEY_CALC_METHOD = "calc_method"
        private const val KEY_AZAN_TONE = "azan_tone"
    }
}
