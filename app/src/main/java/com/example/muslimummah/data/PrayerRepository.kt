package com.example.muslimummah.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

data class PrayerTimings(
    val fajr: String,
    val sunrise: String,
    val dhuhr: String,
    val asr: String,
    val maghrib: String,
    val isha: String,
    val imsak: String
) {
    fun toJsonString(): String {
        val json = JSONObject()
        json.put("Fajr", fajr)
        json.put("Sunrise", sunrise)
        json.put("Dhuhr", dhuhr)
        json.put("Asr", asr)
        json.put("Maghrib", maghrib)
        json.put("Isha", isha)
        json.put("Imsak", imsak)
        return json.toString()
    }
}

class PrayerRepository(private val preferences: AppPreferences) {

    suspend fun fetchPrayerTimings(): PrayerTimings = withContext(Dispatchers.IO) {
        val lat = preferences.baseLat
        val lng = preferences.baseLng
        val method = preferences.calcMethod
        val urlString = "https://api.aladhan.com/v1/timings?latitude=$lat&longitude=$lng&method=$method"

        try {
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 8000
            connection.readTimeout = 8000

            if (connection.responseCode == 200) {
                val responseText = connection.inputStream.bufferedReader().use { it.readText() }
                val rootObj = JSONObject(responseText)
                val timingsObj = rootObj.getJSONObject("data").getJSONObject("timings")

                fun cleanTime(t: String): String = t.split(" ")[0]

                return@withContext PrayerTimings(
                    fajr = cleanTime(timingsObj.getString("Fajr")),
                    sunrise = cleanTime(timingsObj.getString("Sunrise")),
                    dhuhr = cleanTime(timingsObj.getString("Dhuhr")),
                    asr = cleanTime(timingsObj.getString("Asr")),
                    maghrib = cleanTime(timingsObj.getString("Maghrib")),
                    isha = cleanTime(timingsObj.getString("Isha")),
                    imsak = cleanTime(timingsObj.optString("Imsak", "04:42"))
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Fallback standard default timings
        return@withContext PrayerTimings(
            fajr = "04:52",
            sunrise = "06:09",
            dhuhr = "11:42",
            asr = "14:53",
            maghrib = "17:15",
            isha = "18:32",
            imsak = "04:42"
        )
    }
}
