package com.example.muslimummah.bridge

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.webkit.JavascriptInterface
import com.example.muslimummah.MainActivity
import com.example.muslimummah.R
import com.example.muslimummah.SettingsActivity
import com.example.muslimummah.alarm.AzanScheduler
import com.example.muslimummah.data.AppPreferences
import com.example.muslimummah.data.MakruhDetector
import com.example.muslimummah.data.MosqueRepository
import com.example.muslimummah.data.PrayerRepository
import com.example.muslimummah.data.PrayerTimings
import kotlinx.coroutines.runBlocking
import org.json.JSONObject

class WebAppInterface(
    private val context: Context,
    private val preferences: AppPreferences,
    private val prayerRepository: PrayerRepository,
    private val mosqueRepository: MosqueRepository,
    private val azanScheduler: AzanScheduler
) {

    private var previewPlayer: MediaPlayer? = null
    private var cachedTimings: PrayerTimings? = null

    @JavascriptInterface
    fun getAppConfig(): String {
        return preferences.getConfigAsJson()
    }

    @JavascriptInterface
    fun saveAppConfig(lat: Double, lng: Double, radius: Double, method: Int) {
        preferences.baseLat = lat
        preferences.baseLng = lng
        preferences.searchRadiusKm = radius
        preferences.calcMethod = method
    }

    @JavascriptInterface
    fun getPrayerTimes(): String {
        return runBlocking {
            val timings = prayerRepository.fetchPrayerTimings()
            cachedTimings = timings
            timings.toJsonString()
        }
    }

    @JavascriptInterface
    fun getNearbyMosques(): String {
        return runBlocking {
            mosqueRepository.getNearbyMosquesJson()
        }
    }

    @JavascriptInterface
    fun setAlarm(prayerName: String, timeHHMM: String, toneKey: String) {
        azanScheduler.scheduleAlarm(prayerName, timeHHMM, toneKey)
    }

    @JavascriptInterface
    fun cancelAlarm(prayerName: String) {
        azanScheduler.cancelAlarm(prayerName)
    }

    @JavascriptInterface
    fun checkMakruhStatus(): String {
        val timings = cachedTimings ?: runBlocking { prayerRepository.fetchPrayerTimings() }
        val detector = MakruhDetector()
        val status = detector.checkMakruhStatus(timings)

        val json = JSONObject()
        json.put("isMakruh", status.isMakruh)
        json.put("reason", status.reason)
        json.put("remainingMinutes", status.remainingMinutes)
        return json.toString()
    }

    @JavascriptInterface
    fun openSettings() {
        val intent = Intent(context, SettingsActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    @JavascriptInterface
    fun playAzanPreview(toneKey: String) {
        try {
            previewPlayer?.stop()
            previewPlayer?.release()
            previewPlayer = null

            val resId = when (toneKey.lowercase()) {
                "makkah" -> R.raw.makkah
                "madinah" -> R.raw.madinah
                "al-aqsa", "alaqsa" -> R.raw.al_aqsa
                else -> R.raw.baitul_mukarrom
            }

            previewPlayer = MediaPlayer.create(context, resId)?.apply {
                setOnCompletionListener {
                    it.release()
                    previewPlayer = null
                }
                start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
