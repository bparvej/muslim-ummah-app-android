# Muslim Ummah - Pure Native Java Architecture Guide

> **Target Audience**: Maintainers, Developers, and AI Assistant Agents extending or modifying the Muslim Ummah Android application.

---

## 1. Executive Summary & Architecture Overview

**Muslim Ummah** is a 100% native Android application built in **Java** using standard **Android XML Layouts**, **Material Components**, **SharedPreferences**, **HttpURLConnection**, **AlarmManager**, and **MediaPlayer**.

### Architecture Diagram
```
+-------------------------------------------------------------------------------+
|                            Muslim Ummah Native App                            |
|                                                                               |
|  +-------------------------------------------------------------------------+  |
|  |                           MainActivity.java                             |  |
|  |                      (BottomNavigationView Host)                        |  |
|  +--------+------------------+------------------+------------------+-------+  |
|           |                  |                  |                  |          |
|           v                  v                  v                  v          |
|    +--------------+   +--------------+   +--------------+   +--------------+  |
|    | HomeFragment |   |IbadatFragment|   |MosquesFrag.  |   |CalendarFrag. |  |
|    +------+-------+   +------+-------+   +------+-------+   +------+-------+  |
|           |                  |                  |                  |          |
|           +------------------+------------------+------------------+          |
|                              |                                                |
|                              v                                                |
|              +-------------------------------+                                |
|              |         AppConfig.java        |<---- config.xml                |
|              |      (SharedPreferences)      |                                |
|              +---------------+---------------+                                |
|                              |                                                |
|     +------------------------+------------------------+                       |
|     |                        |                        |                       |
|     v                        v                        v                       |
| +-------------------+  +-------------------+  +-----------------------------+ |
| | PrayerRepository  |  | MosqueRepository  |  | AzanScheduler & Service     | |
| | (Aladhan API)     |  | (Nominatim OSM)   |  | (AlarmManager/MediaPlayer)  | |
| +-------------------+  +-------------------+  +-----------------------------+ |
+-------------------------------------------------------------------------------+
```

---

## 2. Configuration Subsystem (`config.xml` & `AppConfig.java`)

All environment variables and default parameters are externalized in `res/values/config.xml`:

- `config_base_lat`: `23.693639`
- `config_base_lng`: `90.450333`
- `config_base_location_name`: `যাত্রাবাড়ী/দনিয়া, ঢাকা`
- `config_mosque_search_radius_km`: `1.0`
- `config_prayer_calc_method`: `1` (Karachi / Islamic Foundation BD)
- `config_prayer_api_base_url`: `https://api.aladhan.com/v1`
- `config_osm_nominatim_api_url`: `https://nominatim.openstreetmap.org/search`

`AppConfig.java` acts as a thread-safe singleton providing getter/setter methods that automatically override XML defaults with user-configured values from `SharedPreferences`.

---

## 3. Core Native Subsystems

### A. Prayer Calculation Engine (`PrayerRepository.java`)
- Asynchronous HttpURLConnection worker calling `https://api.aladhan.com/v1/timings`.
- Returns parsed `PrayerTimings` model containing Fajr, Sunrise, Dhuhr, Asr, Maghrib, Isha, and Imsak.

### B. Makruh Prohibited Time Window Engine (`MakruhDetector.java`)
Calculates three prohibited windows:
1. **Sunrise Period**: `Sunrise` $\rightarrow$ `Sunrise + 15 minutes`
2. **Zawwal (Midday) Period**: `Dhuhr - 12 minutes` $\rightarrow$ `Dhuhr`
3. **Sunset Period**: `Maghrib - 15 minutes` $\rightarrow$ `Maghrib`

When active, `HomeFragment` dynamically updates the card background to a warning red gradient.

### C. Azan Alarm & Sound Subsystem (`AzanScheduler.java` / `AzanAudioService.java`)
- **Precision Alarm**: Uses Android `AlarmManager` with exact wakeup alarms.
- **Audio Service**: `AzanAudioService` runs as a foreground service with notification channel `azan_alarm_channel`.
- **10-Second Playback Limit**: Automatically caps tone playback to **10 seconds** via `Handler.postDelayed()` to avoid lengthy intrusive sounds.
- **Audio Profiles**:
  - `R.raw.baitul_mukarrom` (`baitul-mukarrom`)
  - `R.raw.makkah` (`makkah`)
  - `R.raw.madinah` (`madinah`)
  - `R.raw.al_aqsa` (`al-aqsa`)

---

## 4. App Launcher Icon Assets

- **Vector Adaptive Icons**: `ic_launcher_mosque_foreground.xml` & `ic_launcher_mosque_background.xml`
- **Raster Density Icons**: Generated across `mipmap-mdpi` (48px), `mipmap-hdpi` (72px), `mipmap-xhdpi` (96px), `mipmap-xxhdpi` (144px), and `mipmap-xxxhdpi` (192px).

---

## 5. Extension Guidelines for AI Agents & Maintainers

1. **Adding New Native Screens**:
   - Create layout XML in `app/src/main/res/layout/activity_your_screen.xml`.
   - Implement Activity in `com.example.muslimummah.ui`.
   - Register Activity in `AndroidManifest.xml`.

2. **Build Execution**:
   ```powershell
   $env:JAVA_HOME="C:\Users\parvej\.jdks\jbr-17.0.14"
   $env:ANDROID_PREFS_ROOT=$null
   .\gradlew assembleRelease
   ```
