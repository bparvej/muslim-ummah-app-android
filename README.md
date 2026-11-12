# Muslim Ummah (মুসলিম উম্মাহ) - 100% Native Java Android App

**Muslim Ummah** is a 100% native Android application built using **Java** and **Android XML Layouts**. It mirrors all visual, animation, layout, and feature elements from the prototype.

---

## 🌟 Key Features

- 🕌 **Real-Time Prayer Timings & Countdown**:
  - Live 5 daily prayer timings + Sunrise and Imsak times.
  - Live 1-second ticker countdown.
- ⚠️ **Makruh (Forbidden) Time Red Alert**:
  - Automatic detection of Sunrise, Zawwal (Midday), and Sunset prohibited windows with dynamic red alert gradient card styling.
- 🔔 **Alarm Settings Bottom Sheet Drawer**:
  - Recreates the web modal drawer using `BottomSheetDialogFragment`.
  - Per-prayer toggles, custom time pickers, Azan tone selection, and 10-second test audio button.
- 🗺️ **Nearby Mosque Finder**:
  - OpenStreetMap (Nominatim API) location search within configurable radius.
  - RecyclerView list with Jama'at schedules and Google Maps direction buttons.
- 🧭 **Qibla Compass**:
  - Real-time compass using Android `SensorManager` pointing to Qibla ($278^\circ$ WNW for Dhaka).
- 📖 **Searchable Masnoon Dua Library**:
  - Complete Duas with Arabic text, Bengali pronunciation, translation, and reference.
- 📿 **Interactive Adhkar Counter**:
  - Daily Adhkar cards with interactive counter button (`0/10` to `10/10`).
- 🎨 **Mosque Launcher Icon**:
  - Vector & PNG density launcher icons (`mdpi`, `hdpi`, `xhdpi`, `xxhdpi`, `xxxhdpi`).

---

## 🛠️ Build & Release Instructions

```powershell
$env:JAVA_HOME="C:\Users\parvej\.jdks\jbr-17.0.14"
$env:ANDROID_PREFS_ROOT=$null
.\gradlew assembleRelease
```
- Output APK: `app/build/outputs/apk/release/app-release.apk`
