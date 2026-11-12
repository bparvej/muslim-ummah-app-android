# Muslim Ummah - Technical Documentation & Architecture Guide

> **Target Audience**: Maintainers, Developers, and AI Assistant Agents extending or modifying the Muslim Ummah Android application.

---

## 1. Executive Summary & Architecture Overview

**Muslim Ummah** is a hybrid Android application combining a high-performance native Kotlin core with Jetpack Compose and an embedded local WebView running the web prototype UI. 

### Architecture Diagram
```
+-------------------------------------------------------------------------+
|                              Android App                                |
|                                                                         |
|  +---------------------------+        +------------------------------+  |
|  |     MainActivity.kt       |        |     SettingsActivity.kt      |  |
|  |   (Hosts Android WebView) |        |    (Jetpack Compose UI)     |  |
|  +-------------+-------------+        +--------------+---------------+  |
|                |                                     |                  |
|                v                                     v                  |
|  +---------------------------+        +------------------------------+  |
|  |   WebAppInterface (JS)    |------->|     AppPreferences.kt        |  |
|  |  window.AndroidBridge     |<-------|    (SharedPreferences)       |  |
|  +-------------+-------------+        +------------------------------+  |
|                |                                                        |
|   +------------+------------+--------------------+                      |
|   |                         |                    |                      |
|   v                         v                    v                      |
| +-------------------+  +------------------+  +------------------------+ |
| | PrayerRepository  |  | MosqueRepository |  | AzanScheduler & Service| |
| | (Aladhan API)     |  | (Nominatim OSM)  |  | (AlarmManager/Media)   | |
| +-------------------+  +------------------+  +------------------------+ |
+-------------------------------------------------------------------------+
```

---

## 2. JavaScript Interface Specification (`window.AndroidBridge`)

The web UI communicates with native Kotlin code via the `@JavascriptInterface` bridge attached as `AndroidBridge`.

| Method Name | Parameters | Return Type | Description |
| :--- | :--- | :--- | :--- |
| `getAppConfig()` | None | `String` (JSON) | Returns current configuration (`BASE_LAT`, `BASE_LNG`, `MOSQUE_SEARCH_RADIUS_KM`, `PRAYER_CALC_METHOD`, `SELECTED_TONE`). |
| `saveAppConfig(lat, lng, radius, method)` | `Double, Double, Double, Int` | `Unit` | Persists configuration changes to `SharedPreferences`. |
| `getPrayerTimes()` | None | `String` (JSON) | Asynchronously fetches live timings from Aladhan API or returns cached values. |
| `getNearbyMosques()` | None | `String` (JSON Array) | Fetches nearby mosques within the search radius via Nominatim OSM. |
| `setAlarm(prayerName, timeHHMM, toneKey)` | `String, String, String` | `Unit` | Schedules an exact alarm using Android `AlarmManager`. |
| `cancelAlarm(prayerName)` | `String` | `Unit` | Cancels scheduled alarm for the given prayer name. |
| `checkMakruhStatus()` | None | `String` (JSON) | Evaluates current time against prohibited prayer windows. |
| `openSettings()` | None | `Unit` | Launches the native Jetpack Compose `SettingsActivity`. |
| `playAzanPreview(toneKey)` | `String` | `Unit` | Plays a short preview of the selected Azan tone using `MediaPlayer`. |

---

## 3. Core Native Subsystems

### A. Prayer Calculation Engine (`PrayerRepository.kt`)
- **API Endpoint**: `https://api.aladhan.com/v1/timings`
- **Parameters**: `latitude`, `longitude`, `method`
- **Fallback**: If offline or request fails, standard default timings for Karachi/Islamic Foundation Bangladesh are returned.

### B. Makruh Prohibited Time Window Engine (`MakruhDetector.kt`)
Calculates three prohibited windows:
1. **Sunrise Period**: `Sunrise` to `Sunrise + 15 minutes`
2. **Zawwal (Midday) Period**: `Dhuhr - 12 minutes` to `Dhuhr`
3. **Sunset Period**: `Maghrib - 15 minutes` to `Maghrib`

When active, the native app triggers JS evaluation to apply the `.makruh-restricted` CSS gradient style and warning badge to the main prayer card.

### C. Azan Alarm & Notification System (`AzanScheduler.kt` / `AzanAudioService.kt`)
- **Scheduler**: Uses `AlarmManager.setExactAndAllowWhileIdle()` for high-precision alarms.
- **Audio Service**: `AzanAudioService` runs as a foreground service with a high-priority notification channel (`azan_alarm_channel`).
- **Audio Resources**:
  - `R.raw.baitul_mukarrom` (`baitul-mukarrom`)
  - `R.raw.makkah` (`makkah`)
  - `R.raw.madinah` (`madinah`)
  - `R.raw.al_aqsa` (`al-aqsa`)

---

## 4. Extension Guidelines for AI Agents & Maintainers

When extending this codebase:

1. **Adding New Native Methods**:
   - Declare the function with `@JavascriptInterface` in `WebAppInterface.kt`.
   - Update `app/src/main/assets/script.js` to call `window.AndroidBridge.yourNewMethod()`.
   - Maintain a fallback path in JS for browser execution.

2. **UI Enhancements**:
   - Native Jetpack Compose screens should be created as separate activities or composable routes under `com.example.muslimummah`.
   - Use `Material3` themes with brand colors: `#075B45` (Emerald) and `#D6B66D` (Gold).

3. **Build & Release Pipelines**:
   - Ensure `publisher.md` exists with valid properties (`storePassword`, `keyAlias`, `keyPassword`).
   - Run Gradle assemble with JDK 17:
     ```powershell
     $env:JAVA_HOME="C:\Users\parvej\.jdks\jbr-17.0.14"
     $env:ANDROID_PREFS_ROOT=$null
     .\gradlew assembleRelease
     ```
