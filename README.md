# Muslim Ummah (মুসলিম উম্মাহ) - 100% Native Java Android App

**Muslim Ummah** is a 100% native Android application built using **Java** and **Android XML Layouts**. It provides an everyday Islamic companion experience with real-time prayer calculations, Makruh time red alerts, nearby mosque locator, 10-second Azan alarms, searchable Masnoon Duas, and externalized environment configuration.

---

## 🌟 Key Features

- 🕌 **Real-Time Prayer Timings & Countdown**:
  - Live 5 daily prayer timings + Sunrise and Imsak times.
  - Ticker countdown timer updating every second.
- ⚠️ **Makruh (Forbidden) Time Red Alert**:
  - Automatic detection of the 3 prohibited prayer windows (Sunrise, Zawwal/Midday, Sunset).
  - Dynamic red alert gradient card styling with countdown until the prohibited window ends.
- 🗺️ **Nearby Mosque Finder**:
  - OpenStreetMap (Nominatim API) location search within configurable radius.
  - RecyclerView list with distance calculations and Google Maps direction links.
- 🔔 **Azan Audio Alarms (10-Second Auto-Stop)**:
  - Exact alarms via Android `AlarmManager`.
  - Audio playback capped at **10 seconds maximum** for a concise, non-intrusive sound.
  - 4 Azan tone profiles in `res/raw/`:
    1. **Baitul Mukarrom National Mosque** (`baitul-mukarrom`)
    2. **Masjid al-Haram, Makkah** (`makkah`)
    3. **Al-Masjid an-Nabawi, Madinah** (`madinah`)
    4. **Al-Aqsa Mosque** (`al-aqsa`)
- 📖 **Searchable Masnoon Dua Library**:
  - Searchable list of Duas with Arabic text, Bengali pronunciation, translation, and reference.
- ⚙️ **Native Java Settings**:
  - Customize Base Coordinates (`base_lat`, `base_lng`), Search Radius, Calculation Method, and Azan Tone.
- 🎨 **Mosque Launcher Icon**:
  - Custom vector & PNG launcher icons featuring Mosque dome, minarets, and crescent moon across density folders (`mdpi`, `hdpi`, `xhdpi`, `xxhdpi`, `xxxhdpi`).

---

## 📡 Sources of Truth & APIs

| Component | Source / API | Endpoint | Method / Config |
| :--- | :--- | :--- | :--- |
| **Prayer Timings** | Aladhan API | `https://api.aladhan.com/v1/timings` | HTTP GET (`latitude`, `longitude`, `method`) |
| **Mosque Search** | OpenStreetMap Nominatim | `https://nominatim.openstreetmap.org/search` | HTTP GET (`q=mosque`, `format=json`, `viewbox`) |
| **Configuration** | `config.xml` / `AppConfig.java` | Externalized Res Config | `23.693639` Lat, `90.450333` Lng |

---

## 📂 Project Structure

```
muslim-ummah-app-android/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/muslimummah/
│   │   │   ├── alarm/          # AzanScheduler, AzanAlarmReceiver, AzanAudioService (10s max)
│   │   │   ├── config/         # AppConfig.java
│   │   │   ├── models/         # PrayerTimings, Mosque, MasnoonDua, MakruhStatus
│   │   │   ├── repository/     # PrayerRepository, MosqueRepository
│   │   │   ├── ui/             # MainActivity, HomeFragment, IbadatFragment, MosquesFragment, etc.
│   │   │   └── utils/          # MakruhDetector
│   │   └── res/
│   │       ├── drawable/       # Vector mosque launcher icons & graphics
│   │       ├── layout/         # Native Android XML Layouts
│   │       ├── mipmap-*/       # Mosque PNG launcher icons across densities
│   │       ├── raw/            # 10s Azan audio MP3s
│   │       └── values/         # config.xml, colors.xml, strings.xml, themes.xml
│   └── build.gradle.kts        # Module build file with release signingConfig
├── TECHNICAL_DOCUMENTATION.md  # Developer & AI architecture guide
└── README.md                   # Project documentation
```

---

## 🛠️ Build & Release Instructions

1. **Build Release APK**:
   ```powershell
   $env:JAVA_HOME="C:\Users\parvej\.jdks\jbr-17.0.14"
   $env:ANDROID_PREFS_ROOT=$null
   .\gradlew assembleRelease
   ```

2. **Output Location**:
   - `app/build/outputs/apk/release/app-release.apk`
