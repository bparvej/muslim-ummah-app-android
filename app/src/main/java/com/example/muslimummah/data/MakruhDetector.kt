package com.example.muslimummah.data

import org.json.JSONObject
import java.util.Calendar

data class MakruhStatus(
    val isMakruh: Boolean,
    val reason: String,
    val remainingMinutes: Int
)

class MakruhDetector {

    fun checkMakruhStatus(timings: PrayerTimings, now: Calendar = Calendar.getInstance()): MakruhStatus {
        val curMins = now.get(Calendar.HOUR_OF_DAY) * 60 + now.get(Calendar.MINUTE)

        fun toMins(tStr: String): Int {
            val parts = tStr.split(":")
            if (parts.size < 2) return 0
            val h = parts[0].toIntOrNull() ?: 0
            val m = parts[1].toIntOrNull() ?: 0
            return h * 60 + m
        }

        val sunriseM = toMins(timings.sunrise)
        val dhuhrM = toMins(timings.dhuhr)
        val maghribM = toMins(timings.maghrib)

        // 1. Sunrise period: sunrise -> sunrise + 15m
        val isSunriseMakruh = curMins in sunriseM until (sunriseM + 15)
        // 2. Zawwal/Midday period: Dhuhr - 12m -> Dhuhr
        val isMiddayMakruh = curMins in (dhuhrM - 12) until dhuhrM
        // 3. Sunset period: Maghrib - 15m -> Maghrib
        val isSunsetMakruh = curMins in (maghribM - 15) until maghribM

        return when {
            isSunriseMakruh -> MakruhStatus(
                isMakruh = true,
                reason = "সূর্যোদয়কালীন নিষিদ্ধ সময়",
                remainingMinutes = (sunriseM + 15) - curMins
            )
            isMiddayMakruh -> MakruhStatus(
                isMakruh = true,
                reason = "দ্বিপ্রহর (জাওয়াল) নিষিদ্ধ সময়",
                remainingMinutes = dhuhrM - curMins
            )
            isSunsetMakruh -> MakruhStatus(
                isMakruh = true,
                reason = "সূর্যাস্তকালীন নিষিদ্ধ সময়",
                remainingMinutes = maghribM - curMins
            )
            else -> MakruhStatus(
                isMakruh = false,
                reason = "",
                remainingMinutes = 0
            )
        }
    }
}
