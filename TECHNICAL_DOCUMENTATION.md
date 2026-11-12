# Muslim Ummah - Complete Native Java Architecture Guide

> **Target Audience**: Maintainers, Developers, and AI Assistant Agents extending or modifying the Muslim Ummah Android application.

---

## 1. Executive Summary & Architecture Overview

**Muslim Ummah** is a 100% native Android application built in **Java** using **Android XML Layouts**, **Material Components**, **SharedPreferences**, **HttpURLConnection**, **AlarmManager**, **SensorManager**, and **MediaPlayer**.

### Architecture & Activity Map
```
SplashActivity (1.8s Animated Intro)
  └── OnboardingActivity (Language & Permission Setup)
        └── MainActivity (BottomNavigationView Host)
              ├── HomeFragment (Live Ticker, Next Prayer, Makruh Red Alert, 5 Daily Prayers)
              │     └── AlarmBottomSheetDialogFragment (Alarm Settings Modal Drawer)
              ├── IbadatFragment (Module Grid)
              │     ├── NaflPrayerActivity (Nafl Prayers & Niyyat)
              │     ├── AdhkarActivity (Adhkar & Interactive 0/10 Counter)
              │     ├── RestrictedTimesActivity (Prohibited Makruh Windows Guide)
              │     ├── FastingActivity (Sehri/Iftar & Iftar Dua)
              │     └── QiblaActivity (SensorManager Qibla Compass)
              ├── MosquesFragment (Osm Search & Nearby Mosque List)
              │     └── MosqueDetailActivity (Jama'at Schedule & Map Directions)
              ├── CalendarFragment (Hijri/Gregorian Date & Special Islamic Days)
              └── ProfileFragment (User Profile & App Settings Launcher)
```

---

## 2. Configuration Subsystem (`config.xml` & `AppConfig.java`)

Environment variables are externalized in `res/values/config.xml`:

- `config_base_lat`: `23.693639`
- `config_base_lng`: `90.450333`
- `config_base_location_name`: `যাত্রাবাড়ী/দনিয়া, ঢাকা`
- `config_mosque_search_radius_km`: `1.0`
- `config_prayer_calc_method`: `1` (Karachi / Islamic Foundation BD)
- `config_prayer_api_base_url`: `https://api.aladhan.com/v1`
- `config_osm_nominatim_api_url`: `https://nominatim.openstreetmap.org/search`

`AppConfig.java` manages `SharedPreferences` runtime overrides.

---

## 3. Core Native Subsystems

### A. Prayer Calculation & Makruh Engine
- **PrayerRepository.java**: Asynchronous HttpURLConnection calling `https://api.aladhan.com/v1/timings`.
- **MakruhDetector.java**: Calculates Sunrise (+15m), Zawwal (Midday, Dhuhr -12m), and Sunset (Maghrib -15m) prohibited windows, dynamically updating `HomeFragment`'s card to a red gradient (`#A72323` to `#D93838`).

### B. Azan Alarm & Sound Subsystem (`AzanScheduler.java` / `AzanAudioService.java`)
- **AlarmManager**: Schedules exact wakeup alarms.
- **AzanAudioService**: Foreground service with notification channel `azan_alarm_channel`.
- **10-Second Playback Limit**: Automatically stops playback after 10 seconds via `Handler.postDelayed()`.
- **Audio Profiles**:
  - `R.raw.baitul_mukarrom` (`baitul-mukarrom`)
  - `R.raw.makkah` (`makkah`)
  - `R.raw.madinah` (`madinah`)
  - `R.raw.al_aqsa` (`al-aqsa`)

### C. Sensor-Driven Qibla Compass (`QiblaActivity.java`)
- Uses Android `SensorManager` (TYPE_ACCELEROMETER & TYPE_MAGNETIC_FIELD) to compute live azimuth orientation and point towards Qibla ($278^\circ$ WNW for Dhaka).

---

## 4. App Launcher Icon Assets

- **Vector Adaptive Icons**: `ic_launcher_mosque_foreground.xml` & `ic_launcher_mosque_background.xml`
- **Raster Density Icons**: Generated across `mipmap-mdpi`, `mipmap-hdpi`, `mipmap-xhdpi`, `mipmap-xxhdpi`, and `mipmap-xxxhdpi`.

---

## 5. Build Instructions

```powershell
$env:JAVA_HOME="C:\Users\parvej\.jdks\jbr-17.0.14"
$env:ANDROID_PREFS_ROOT=$null
.\gradlew assembleRelease
```
