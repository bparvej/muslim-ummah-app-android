# Muslim Ummah (মুসলিম উম্মাহ) - Native Android App

**Muslim Ummah** is a feature-packed, native Android application built using Kotlin and Jetpack Compose. It serves as an everyday Islamic companion for Bengali and English-speaking Muslims, providing real-time prayer timings, Makruh prohibited time alerts, nearby mosque locator with Jama'at schedules, exact Azan alarms with authentic mosque tone profiles, Masnoon Duas, and a Halal directory.

---

## 🌟 Key Features

- 🕌 **Real-Time Prayer Timings & Countdown**:
  - Live 5 daily prayer timings (Fajr, Dhuhr, Asr, Maghrib, Isha) + Sunrise and Imsak times.
  - Live countdown timer to the next prayer.
- ⚠️ **Makruh (Forbidden) Time Red Alert**:
  - Automatic detection of the 3 prohibited prayer windows (Sunrise, Zawwal/Midday, Sunset).
  - Highlighting prayer card in red with real-time countdown until the window ends.
- 🗺️ **Nearby Mosque Finder**:
  - OpenStreetMap (Nominatim API) location search within configurable radius (default 1.0 km).
  - Distance calculation, street addresses, direction links (Google Maps), and Jama'at schedules.
- 🔔 **Azan Audio Alarms & Sound Profiles**:
  - Precision scheduling via Android `AlarmManager`.
  - 4 distinct Azan tone profiles stored in `res/raw/`:
    1. **Baitul Mukarrom National Mosque** (`baitul-mukarrom`)
    2. **Masjid al-Haram, Makkah** (`makkah`)
    3. **Al-Masjid an-Nabawi, Madinah** (`madinah`)
    4. **Al-Aqsa Mosque** (`al-aqsa`)
- 📖 **Masnoon Dua Library**:
  - Full Arabic text, Bengali pronunciation, and Bengali translation with authentic references.
- ⚙️ **Jetpack Compose Settings**:
  - View and customize Base Coordinates (`BASE_LAT`, `BASE_LNG`), Search Radius, Calculation Method, and Azan Tone.
  - Changes persist immediately via `SharedPreferences`.

---

## 📡 Sources of Truth & APIs

| Component | Source / API | Endpoint | Method / Config |
| :--- | :--- | :--- | :--- |
| **Prayer Timings** | Aladhan API | `https://api.aladhan.com/v1/timings` | HTTP GET (`latitude`, `longitude`, `method`) |
| **Mosque Search** | OpenStreetMap Nominatim | `https://nominatim.openstreetmap.org/search` | HTTP GET (`q=mosque`, `format=json`, `viewbox`) |
| **Base Coordinates** | Default Setup | `23.693639` Lat, `90.450333` Lng | Jatrabari / Dania / Nurpur, Dhaka |
| **Default Method** | Karachi / BD | Method `1` | University of Islamic Sciences, Karachi / Islamic Foundation BD |

### Calculation Methods
- `1` = University of Islamic Sciences, Karachi (Islamic Foundation Bangladesh compatible)
- `2` = Islamic Society of North America (ISNA)
- `3` = Muslim World League (MWL)
- `4` = Umm Al-Qura University, Makkah
- `5` = Egyptian General Authority of Survey

---

## 📐 Prohibited (Makruh) Window Logic

1. **Sunrise Period**: `Sunrise` $\rightarrow$ `Sunrise + 15 minutes`
2. **Zawwal (Midday) Period**: `Dhuhr - 12 minutes` $\rightarrow$ `Dhuhr`
3. **Sunset Period**: `Maghrib - 15 minutes` $\rightarrow$ `Maghrib`

---

## 📂 Project Structure

```
muslim-ummah-app-android/
├── app/
│   ├── src/main/
│   │   ├── assets/              # index.html, script.js, styles.css, .env, config.js
│   │   ├── java/com/example/muslimummah/
│   │   │   ├── alarm/           # AzanScheduler, AzanAlarmReceiver, AzanAudioService
│   │   │   ├── bridge/          # WebAppInterface (@JavascriptInterface bridge)
│   │   │   ├── data/            # AppPreferences, PrayerRepository, MosqueRepository, MakruhDetector
│   │   │   ├── MainActivity.kt  # Main activity hosting WebView
│   │   │   └── SettingsActivity.kt # Jetpack Compose Settings Screen
│   │   └── res/
│   │       ├── raw/             # baitul_mukarrom.mp3, makkah.mp3, madinah.mp3, al_aqsa.mp3
│   │       └── values/          # colors.xml, strings.xml, themes.xml
│   └── build.gradle.kts         # Module build file with release signingConfig
├── build.gradle.kts             # Root build file
├── publisher.md                 # Publishing credentials (Excluded from Git)
├── muslimummah.jks              # Release Keystore (Excluded from Git)
├── TECHNICAL_DOCUMENTATION.md   # Developer & AI architecture guide
└── README.md                    # Project documentation
```

---

## 🛠️ Build & Release Instructions

1. **Keystore & Credentials Setup**:
   Ensure `publisher.md` exists in the root directory:
   ```properties
   storePassword=muslimummah123
   keyAlias=muslimummah
   keyPassword=muslimummah123
   ```

2. **Generate Signed Release APK**:
   Execute the Gradle build with JDK 17:
   ```powershell
   $env:JAVA_HOME="C:\Users\parvej\.jdks\jbr-17.0.14"
   $env:ANDROID_PREFS_ROOT=$null
   .\gradlew assembleRelease
   ```

3. **Output Artifact**:
   - `app/build/outputs/apk/release/app-release.apk`

---

## 📄 License
Dedicated to the Muslim Ummah. Free to use and distribute for beneficial Islamic causes.
