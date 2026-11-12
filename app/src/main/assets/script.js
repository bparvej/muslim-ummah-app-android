// Muslim Ummah - Core Application Logic
// Handles real-time prayer calculations, Makruh detection, Aladhan API, OpenStreetMap Mosques Finder, Audio Alarms (Baitul Mukarrom/Makkah/Madinah/Al-Aqsa), and Android Native Bridge integration.

const translations = {
    bn: {
        tagline: 'আপনার দৈনন্দিন মুসলিম সঙ্গী',
        welcome: 'স্বাগতম',
        selectLanguage: 'ভাষা নির্বাচন করুন / Choose your language',
        locationAccess: 'লোকেশন অ্যাকসেস',
        locationHelp: 'নামাজের সময় ও কিবলার জন্য প্রয়োজন',
        continueMusafir: 'মুসাফির হিসেবে চালিয়ে যান',
        registerLogin: 'নিবন্ধন / লগইন',
        assalamuAlaikum: 'আসসালামু আলাইকুম, মুসাফির',
        nextPrayerLabel: 'পরবর্তী নামায',
        nextPrayerTime: 'ওয়াক্তের সময় লোড হচ্ছে...',
        naflPrayer: 'নফল নামায',
        adhkar: 'আমল',
        dua: 'দোয়া',
        qibla: 'কিবলা',
        todayReminder: 'আজকের বিশেষ অনুপ্রেরণা',
        upcomingFasting: 'আসন্ন সুন্নত রোজা',
        nearbyMosque: 'কাছের মসজিদ',
        sehriIftar: 'সেহরি / ইফতার',
        ibadat: 'ইবাদত',
        duaLibrary: 'মাসনুন দোয়া',
        adhanAlarm: 'নামাজের অ্যালার্ম',
        restrictedPrayer: 'নিষিদ্ধ সময়',
        ramadan: 'রমজান ও রোজা',
        sunnahFasting: 'সুন্নত রোজা',
        more: 'আরও',
        prayerTimes: 'নামাযের সময়সূচী',
        weeklySchedule: 'সাপ্তাহিক সময়সূচি',
        calcMethod: 'গণনা পদ্ধতি: ইসলামিক ফাউন্ডেশন বাংলাদেশ / করাচি বিশ্ববিদ্যালয়',
        mosqueFinder: 'মসজিদ খুঁজুন',
        islamicCalendar: 'ইসলামিক ক্যালেন্ডার',
        qiblaDirection: 'কিবলা দিক',
        profile: 'প্রোফাইল',
        language: 'ভাষা',
        location: 'বেস লোকেশন',
        notifications: 'নোটিশ ও অ্যালার্ম',
        prayerMethod: 'নামাজের ক্যালকুলেশন',
        navHome: 'হোম',
        navIbadat: 'ইবাদত',
        navMosque: 'মসজিদ',
        navCalendar: 'ক্যালেন্ডার',
        navProfile: 'প্রোফাইল',
        prayerAlarmTitle: 'নামাজের অ্যালার্ম',
        configureAlarm: 'সেট করুন'
    },
    en: {
        tagline: 'Your everyday Muslim companion',
        welcome: 'Welcome',
        selectLanguage: 'Choose your language / ভাষা নির্বাচন করুন',
        locationAccess: 'Location Access',
        locationHelp: 'Required for prayer times and qibla',
        continueMusafir: 'Continue as Musafir',
        registerLogin: 'Register / Login',
        assalamuAlaikum: 'Assalamu Alaikum, Musafir',
        nextPrayerLabel: 'Next Prayer',
        nextPrayerTime: 'Loading prayer time...',
        naflPrayer: 'Nafl Prayer',
        adhkar: 'Adhkar',
        dua: 'Dua',
        qibla: 'Qibla',
        todayReminder: "Today's Special Reminder",
        upcomingFasting: 'Upcoming Sunnah Fasting',
        nearbyMosque: 'Nearby Mosque',
        sehriIftar: 'Sehri / Iftar',
        ibadat: 'Ibadat',
        duaLibrary: 'Masnoon Dua',
        adhanAlarm: 'Adhan & Prayer Alarm',
        restrictedPrayer: 'Restricted Times',
        ramadan: 'Ramadan & Fasting',
        sunnahFasting: 'Sunnah Fasting',
        more: 'More',
        prayerTimes: 'Prayer Times',
        weeklySchedule: 'Weekly Schedule',
        calcMethod: 'Calc Method: Islamic Foundation Bangladesh / Karachi',
        mosqueFinder: 'Mosque Finder',
        islamicCalendar: 'Islamic Calendar',
        qiblaDirection: 'Qibla Direction',
        profile: 'Profile',
        language: 'Language',
        location: 'Base Location',
        notifications: 'Notifications & Alarm',
        prayerMethod: 'Prayer Calculation',
        navHome: 'Home',
        navIbadat: 'Ibadat',
        navMosque: 'Mosque',
        navCalendar: 'Calendar',
        navProfile: 'Profile',
        prayerAlarmTitle: 'Prayer Alarm',
        configureAlarm: 'Configure'
    }
};

const PRAYER_NAMES = {
    Fajr: { bn: 'ফজর', en: 'Fajr', zamatOffsetMin: 35 },
    Dhuhr: { bn: 'যোহর', en: 'Dhuhr', zamatOffsetMin: 45 },
    Asr: { bn: 'আসর', en: 'Asr', zamatOffsetMin: 30 },
    Maghrib: { bn: 'মাগরিব', en: 'Maghrib', zamatOffsetMin: 10 },
    Isha: { bn: 'ইশা', en: 'Isha', zamatOffsetMin: 45 }
};

// Azan Tones definitions with distinctive harmonic profiles
const AZAN_TONES = {
    'baitul-mukarrom': {
        name: 'বায়তুল মোকাররম জাতীয় মসজিদ',
        nameEn: 'Baitul Mukarrom National Mosque',
        frequencies: [330, 392, 440, 523.25, 440, 392],
        tempo: 0.38,
        wave: 'sine'
    },
    'makkah': {
        name: 'মসজিদুল হারাম, মক্কার আজান',
        nameEn: 'Masjid al-Haram, Makkah',
        frequencies: [392, 493.88, 587.33, 659.25, 587.33],
        tempo: 0.42,
        wave: 'triangle'
    },
    'madinah': {
        name: 'মসজিদে নববী, মদিনার আজান',
        nameEn: 'Al-Masjid an-Nabawi, Madinah',
        frequencies: [293.66, 349.23, 440, 523.25, 440, 349.23],
        tempo: 0.45,
        wave: 'sine'
    },
    'al-aqsa': {
        name: 'বায়তুল মুকাদ্দাস / আল-আকসার আজান',
        nameEn: 'Al-Aqsa Mosque',
        frequencies: [349.23, 415.3, 523.25, 622.25, 523.25],
        tempo: 0.40,
        wave: 'triangle'
    }
};

// Application State
const state = {
    lang: 'bn',
    timings: {
        Fajr: '04:52',
        Sunrise: '06:09',
        Dhuhr: '11:42',
        Asr: '14:53',
        Maghrib: '17:15',
        Isha: '18:32',
        Imsak: '04:42'
    },
    hijriDate: '৩০ জুমাদিউল আউয়াল ১৪ ৪৮',
    gregorianDate: '',
    currentStatus: {
        type: 'waqt',
        name: 'যোহর',
        timeStart: '11:42',
        nextName: 'আসর',
        nextTime: '14:53',
        isMakruh: false,
        makruhReason: ''
    },
    mosques: [],
    selectedMosque: null,
    selectedTone: 'baitul-mukarrom',
    alarms: {
        Fajr: { enabled: true, customTime: '' },
        Dhuhr: { enabled: true, customTime: '' },
        Asr: { enabled: true, customTime: '' },
        Maghrib: { enabled: true, customTime: '' },
        Isha: { enabled: true, customTime: '' }
    },
    lastTriggeredMinute: ''
};

// Web Audio Synthesizer & Android Native Audio Bridge
let audioCtx = null;
function playAlarmTone(toneKey = null) {
    const selectedKey = toneKey || state.selectedTone || 'baitul-mukarrom';

    // If native Android bridge is available, use native MediaPlayer
    if (window.AndroidBridge && typeof window.AndroidBridge.playAzanPreview === 'function') {
        try {
            window.AndroidBridge.playAzanPreview(selectedKey);
            return;
        } catch (e) {
            console.warn('Native audio play failed, falling back to WebAudio:', e);
        }
    }

    try {
        const AudioContext = window.AudioContext || window.webkitAudioContext;
        if (!AudioContext) return;
        audioCtx = audioCtx || new AudioContext();
        if (audioCtx.state === 'suspended') {
            audioCtx.resume();
        }

        const toneProfile = AZAN_TONES[selectedKey] || AZAN_TONES['baitul-mukarrom'];
        const notes = toneProfile.frequencies;
        const noteDuration = toneProfile.tempo;

        notes.forEach((freq, idx) => {
            const osc = audioCtx.createOscillator();
            const gain = audioCtx.createGain();
            osc.type = toneProfile.wave || 'sine';
            const startTime = audioCtx.currentTime + idx * noteDuration;
            const stopTime = startTime + noteDuration * 1.05;

            osc.frequency.setValueAtTime(freq, startTime);
            gain.gain.setValueAtTime(0.001, startTime);
            gain.gain.exponentialRampToValueAtTime(0.35, startTime + 0.05);
            gain.gain.exponentialRampToValueAtTime(0.0001, stopTime);

            osc.connect(gain);
            gain.connect(audioCtx.destination);
            osc.start(startTime);
            osc.stop(stopTime);
        });
    } catch (e) {
        console.warn('Audio tone could not play:', e);
    }
}

// Convert "HH:MM" (24h) to "h:MM AM/PM"
function format12Hour(timeStr) {
    if (!timeStr) return '--:--';
    const cleanTime = timeStr.split(' ')[0];
    const [hStr, mStr] = cleanTime.split(':');
    let hours = parseInt(hStr, 10);
    const minutes = mStr || '00';
    const ampm = hours >= 12 ? 'PM' : 'AM';
    hours = hours % 12;
    hours = hours ? hours : 12;
    return `${hours}:${minutes} ${ampm}`;
}

// Add minutes to "HH:MM" 24h
function addMinutesToTime(timeStr, minsToAdd) {
    if (!timeStr) return '--:--';
    const cleanTime = timeStr.split(' ')[0];
    const [h, m] = cleanTime.split(':').map(Number);
    let totalMins = (h * 60 + m + minsToAdd + 1440) % 1440;
    const newH = Math.floor(totalMins / 60);
    const newM = totalMins % 60;
    const hh = String(newH).padStart(2, '0');
    const mm = String(newM).padStart(2, '0');
    return `${hh}:${mm}`;
}

// Calculate Great Circle Distance in KM (Haversine formula)
function calculateDistanceKm(lat1, lon1, lat2, lon2) {
    const R = 6371; // km
    const dLat = (lat2 - lat1) * Math.PI / 180;
    const dLon = (lon2 - lon1) * Math.PI / 180;
    const a =
        Math.sin(dLat / 2) * Math.sin(dLat / 2) +
        Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
        Math.sin(dLon / 2) * Math.sin(dLon / 2);
    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return parseFloat((R * c).toFixed(2));
}

// Format Bangla numbers
function toBengaliNumerals(str) {
    if (str === undefined || str === null) return '';
    const bengaliDigits = ['০', '১', '২', '৩', '৪', '৫', '৬', '৭', '৮', '৯'];
    return String(str).replace(/[0-9]/g, (d) => bengaliDigits[d]);
}

// Load Saved Alarms and Selected Tone from LocalStorage or Android SharedPreferences
function loadSavedPreferences() {
    try {
        const savedAlarms = localStorage.getItem('muslim_ummah_alarms');
        if (savedAlarms) {
            state.alarms = Object.assign(state.alarms, JSON.parse(savedAlarms));
        }
        const savedTone = localStorage.getItem('muslim_ummah_azan_tone');
        if (savedTone && AZAN_TONES[savedTone]) {
            state.selectedTone = savedTone;
        }
    } catch (e) {
        console.warn('Error loading saved preferences:', e);
    }
}

function saveAlarmsToStorage() {
    try {
        localStorage.setItem('muslim_ummah_alarms', JSON.stringify(state.alarms));
    } catch (e) {
        console.warn('Error saving alarms:', e);
    }

    // Sync with Android Native AlarmManager via JS Bridge
    if (window.AndroidBridge && typeof window.AndroidBridge.setAlarm === 'function') {
        Object.keys(state.alarms).forEach((prayerKey) => {
            const conf = state.alarms[prayerKey];
            const targetTime = conf.customTime || (state.timings[prayerKey] ? state.timings[prayerKey].split(' ')[0] : '');
            if (conf && conf.enabled && targetTime) {
                try {
                    window.AndroidBridge.setAlarm(prayerKey, targetTime, state.selectedTone);
                } catch (err) {
                    console.warn('AndroidBridge.setAlarm error:', err);
                }
            } else if (conf && !conf.enabled && window.AndroidBridge.cancelAlarm) {
                try {
                    window.AndroidBridge.cancelAlarm(prayerKey);
                } catch (err) {}
            }
        });
    }

    updateAlarmUI();
}

function selectAlarmTone(toneKey) {
    if (!AZAN_TONES[toneKey]) return;
    state.selectedTone = toneKey;
    try {
        localStorage.setItem('muslim_ummah_azan_tone', toneKey);
    } catch (e) {}

    // Update chips in adhan screen & modal
    document.querySelectorAll('[data-sound]').forEach((chip) => {
        chip.classList.toggle('active', chip.dataset.sound === toneKey);
    });

    const label = document.getElementById('selected-tone-label');
    if (label) {
        label.textContent = state.lang === 'bn' ? AZAN_TONES[toneKey].name : AZAN_TONES[toneKey].nameEn;
    }

    // Play preview
    playAlarmTone(toneKey);
}

// Check Alarms Loop
function checkAlarmTrigger(now) {
    const curHours = String(now.getHours()).padStart(2, '0');
    const curMinutes = String(now.getMinutes()).padStart(2, '0');
    const curKey = `${curHours}:${curMinutes}`;

    if (state.lastTriggeredMinute === curKey) return;

    Object.keys(state.alarms).forEach((prayerKey) => {
        const conf = state.alarms[prayerKey];
        if (!conf || !conf.enabled) return;

        const targetTime = conf.customTime || (state.timings[prayerKey] ? state.timings[prayerKey].split(' ')[0] : '');
        if (targetTime === curKey) {
            state.lastTriggeredMinute = curKey;
            triggerAlarmAlert(prayerKey, targetTime);
        }
    });
}

function triggerAlarmAlert(prayerKey, targetTime) {
    playAlarmTone();
    const prayerName = state.lang === 'bn' ? PRAYER_NAMES[prayerKey]?.bn : PRAYER_NAMES[prayerKey]?.en;
    const banner = document.getElementById('alarm-banner');
    const titleEl = document.getElementById('alarm-banner-title');
    const subEl = document.getElementById('alarm-banner-sub');

    if (titleEl) titleEl.textContent = `${prayerName} ওয়াক্তের আজান ও অ্যালার্ম!`;
    if (subEl) subEl.textContent = `সুর: ${AZAN_TONES[state.selectedTone]?.name || 'আজান'} • ${format12Hour(targetTime)}`;
    if (banner) {
        banner.style.display = 'flex';
        setTimeout(() => {
            if (banner.style.display !== 'none') {
                banner.style.display = 'none';
            }
        }, 20000);
    }
}

function dismissAlarm() {
    const banner = document.getElementById('alarm-banner');
    if (banner) banner.style.display = 'none';
}

// Fetch Real Prayer Timings from Native Kotlin Bridge or Aladhan API
async function fetchRealPrayerTimes() {
    if (window.AndroidBridge && typeof window.AndroidBridge.getPrayerTimes === 'function') {
        try {
            const jsonStr = window.AndroidBridge.getPrayerTimes();
            if (jsonStr) {
                const parsed = JSON.parse(jsonStr);
                if (parsed && parsed.Fajr) {
                    state.timings = parsed;
                    updatePrayerTimingsUI();
                    updateRealTimePrayerCalculation();
                    return;
                }
            }
        } catch (e) {
            console.warn('Native getPrayerTimes error, falling back to HTTP:', e);
        }
    }

    const cfg = window.APP_CONFIG || {};
    const lat = cfg.BASE_LAT || 23.693639;
    const lng = cfg.BASE_LNG || 90.450333;
    const method = cfg.PRAYER_CALC_METHOD || 1;
    const baseUrl = cfg.PRAYER_API_BASE_URL || 'https://api.aladhan.com/v1';

    try {
        const url = `${baseUrl}/timings?latitude=${lat}&longitude=${lng}&method=${method}`;
        const response = await fetch(url);
        if (!response.ok) throw new Error(`HTTP ${response.status}`);
        const result = await response.json();

        if (result && result.data && result.data.timings) {
            const apiTimings = result.data.timings;
            state.timings = {
                Fajr: apiTimings.Fajr.split(' ')[0],
                Sunrise: apiTimings.Sunrise.split(' ')[0],
                Dhuhr: apiTimings.Dhuhr.split(' ')[0],
                Asr: apiTimings.Asr.split(' ')[0],
                Maghrib: apiTimings.Maghrib.split(' ')[0],
                Isha: apiTimings.Isha.split(' ')[0],
                Imsak: apiTimings.Imsak ? apiTimings.Imsak.split(' ')[0] : '04:42'
            };

            // Hijri Date
            if (result.data.date && result.data.date.hijri) {
                const h = result.data.date.hijri;
                const monthName = h.month?.en || 'Jumādá al-ūlá';
                state.hijriDate = `${toBengaliNumerals(h.day)} ${monthName} ${toBengaliNumerals(h.year)} হিজরি`;
            }

            // Gregorian Date
            if (result.data.date && result.data.date.gregorian) {
                const g = result.data.date.gregorian;
                state.gregorianDate = `${g.weekday?.en || ''}, ${toBengaliNumerals(g.day)} নভেম্বর ২০২৬`;
            }

            console.log('[Muslim Ummah] Prayer timings loaded from Aladhan API:', state.timings);
            updatePrayerTimingsUI();
            updateRealTimePrayerCalculation();
        }
    } catch (e) {
        console.warn('[Muslim Ummah] Failed to fetch live prayer timings (using standard Karachi defaults):', e);
        updatePrayerTimingsUI();
        updateRealTimePrayerCalculation();
    }
}

// Calculate Current Waqt and Makruh (Forbidden) Times
function evaluatePrayerAndMakruhStatus(now) {
    const curMins = now.getHours() * 60 + now.getMinutes() + now.getSeconds() / 60;

    const toM = (tStr) => {
        if (!tStr) return 0;
        const [h, m] = tStr.split(':').map(Number);
        return h * 60 + m;
    };

    const fajrM = toM(state.timings.Fajr);
    const sunriseM = toM(state.timings.Sunrise);
    const dhuhrM = toM(state.timings.Dhuhr);
    const asrM = toM(state.timings.Asr);
    const maghribM = toM(state.timings.Maghrib);
    const ishaM = toM(state.timings.Isha);

    // 3 Makruh / Forbidden Prayer Times:
    // 1. Sunrise period: from sunrise until ~15 minutes after sunrise
    const isSunriseMakruh = curMins >= sunriseM && curMins < (sunriseM + 15);
    // 2. Zawwal/Midday period: 12 minutes before Dhuhr starts until Dhuhr
    const isMiddayMakruh = curMins >= (dhuhrM - 12) && curMins < dhuhrM;
    // 3. Sunset period: 15 minutes before Maghrib until Maghrib (sunset)
    const isSunsetMakruh = curMins >= (maghribM - 15) && curMins < maghribM;

    let isMakruh = false;
    let makruhReason = '';
    let makruhRemainingMins = 0;

    if (isSunriseMakruh) {
        isMakruh = true;
        makruhReason = state.lang === 'bn' ? 'সূর্যোদয়কালীন নিষিদ্ধ সময়' : 'Forbidden Time: Sunrise';
        makruhRemainingMins = Math.ceil((sunriseM + 15) - curMins);
    } else if (isMiddayMakruh) {
        isMakruh = true;
        makruhReason = state.lang === 'bn' ? 'দ্বিপ্রহর (জাওয়াল) নিষিদ্ধ সময়' : 'Forbidden Time: Zawwal Midday';
        makruhRemainingMins = Math.ceil(dhuhrM - curMins);
    } else if (isSunsetMakruh) {
        isMakruh = true;
        makruhReason = state.lang === 'bn' ? 'সূর্যাস্তকালীন নিষিদ্ধ সময়' : 'Forbidden Time: Sunset';
        makruhRemainingMins = Math.ceil(maghribM - curMins);
    }

    // Determine currently active Waqt and next Waqt
    let currentWaqt = '';
    let nextWaqt = '';
    let nextWaqtTime = '';
    let nextWaqtDateObj = null;

    if (curMins >= fajrM && curMins < sunriseM) {
        currentWaqt = 'Fajr';
        nextWaqt = 'Dhuhr';
        nextWaqtTime = state.timings.Dhuhr;
        const [dhH, dhM] = state.timings.Dhuhr.split(':').map(Number);
        nextWaqtDateObj = new Date(now.getFullYear(), now.getMonth(), now.getDate(), dhH, dhM, 0);
    } else if (curMins >= sunriseM && curMins < dhuhrM) {
        currentWaqt = 'Ishraq'; // Duha / Morning
        nextWaqt = 'Dhuhr';
        nextWaqtTime = state.timings.Dhuhr;
        const [dhH, dhM] = state.timings.Dhuhr.split(':').map(Number);
        nextWaqtDateObj = new Date(now.getFullYear(), now.getMonth(), now.getDate(), dhH, dhM, 0);
    } else if (curMins >= dhuhrM && curMins < asrM) {
        currentWaqt = 'Dhuhr';
        nextWaqt = 'Asr';
        nextWaqtTime = state.timings.Asr;
        const [asH, asM] = state.timings.Asr.split(':').map(Number);
        nextWaqtDateObj = new Date(now.getFullYear(), now.getMonth(), now.getDate(), asH, asM, 0);
    } else if (curMins >= asrM && curMins < maghribM) {
        currentWaqt = 'Asr';
        nextWaqt = 'Maghrib';
        nextWaqtTime = state.timings.Maghrib;
        const [mgH, mgM] = state.timings.Maghrib.split(':').map(Number);
        nextWaqtDateObj = new Date(now.getFullYear(), now.getMonth(), now.getDate(), mgH, mgM, 0);
    } else if (curMins >= maghribM && curMins < ishaM) {
        currentWaqt = 'Maghrib';
        nextWaqt = 'Isha';
        nextWaqtTime = state.timings.Isha;
        const [isH, isM] = state.timings.Isha.split(':').map(Number);
        nextWaqtDateObj = new Date(now.getFullYear(), now.getMonth(), now.getDate(), isH, isM, 0);
    } else {
        // Isha until next Fajr
        currentWaqt = 'Isha';
        nextWaqt = 'Fajr';
        nextWaqtTime = state.timings.Fajr;
        const [fjH, fjM] = state.timings.Fajr.split(':').map(Number);
        const nextDay = curMins >= ishaM ? now.getDate() + 1 : now.getDate();
        nextWaqtDateObj = new Date(now.getFullYear(), now.getMonth(), nextDay, fjH, fjM, 0);
    }

    return {
        isMakruh,
        makruhReason,
        makruhRemainingMins,
        currentWaqt,
        nextWaqt,
        nextWaqtTime,
        nextWaqtDateObj
    };
}

// Real-Time Calculation & UI Renderer (every second)
function updateRealTimePrayerCalculation() {
    const now = new Date();
    checkAlarmTrigger(now);

    const status = evaluatePrayerAndMakruhStatus(now);
    const prayerCard = document.getElementById('home-prayer-card');
    const statusPill = document.getElementById('prayer-status-pill');
    const titleEl = document.getElementById('home-current-prayer-status');
    const countdownEl = document.getElementById('prayer-countdown');
    const nextPrayerTimeEl = document.getElementById('home-next-prayer-time');
    const prayerScreenStatus = document.getElementById('prayer-screen-status');

    // Remaining Countdown to Next Prayer
    const diffSecs = Math.max(0, Math.floor((status.nextWaqtDateObj.getTime() - now.getTime()) / 1000));
    const hours = String(Math.floor(diffSecs / 3600)).padStart(2, '0');
    const mins = String(Math.floor((diffSecs % 3600) / 60)).padStart(2, '0');
    const secs = String(diffSecs % 60).padStart(2, '0');
    const countdownFormatted = `${hours}:${mins}:${secs}`;

    if (countdownEl) countdownEl.textContent = countdownFormatted;

    const currName = PRAYER_NAMES[status.currentWaqt]?.bn || status.currentWaqt;
    const nextName = PRAYER_NAMES[status.nextWaqt]?.bn || status.nextWaqt;
    const nextFormatted = format12Hour(status.nextWaqtTime);

    if (status.isMakruh) {
        // Apply RED / Makruh alert style
        if (prayerCard) {
            prayerCard.classList.add('makruh-restricted');
        }
        if (statusPill) {
            statusPill.innerHTML = `<i class="fa-solid fa-triangle-exclamation"></i> ${status.makruhReason}`;
        }
        if (titleEl) {
            titleEl.textContent = state.lang === 'bn' ?
                `নিষিদ্ধ সময় • নামাজ পড়া নিষেধ` :
                `Forbidden Time (Makruh)`;
        }
        if (nextPrayerTimeEl) {
            nextPrayerTimeEl.textContent = state.lang === 'bn' ?
                `বাকি প্রায় ${toBengaliNumerals(status.makruhRemainingMins)} মিনিট • পরবর্তী ${nextName}: ${nextFormatted}` :
                `Ends in ~${status.makruhRemainingMins}m • Next: ${status.nextWaqt} at ${nextFormatted}`;
        }
        if (prayerScreenStatus) {
            prayerScreenStatus.innerHTML = `<i class="fa-solid fa-triangle-exclamation" style="color:#d93838;"></i> নিষিদ্ধ সময় চলমান`;
            prayerScreenStatus.style.color = '#d93838';
            prayerScreenStatus.style.background = 'rgba(217, 56, 56, 0.12)';
        }
    } else {
        // Normal green running waqt style
        if (prayerCard) {
            prayerCard.classList.remove('makruh-restricted');
        }
        if (statusPill) {
            statusPill.innerHTML = `<i class="fa-solid fa-circle-check"></i> ${currName} ওয়াক্ত চলমান`;
        }
        if (titleEl) {
            titleEl.textContent = state.lang === 'bn' ?
                `${currName} ওয়াক্ত শুরু হয়েছে` :
                `${status.currentWaqt} prayer started`;
        }
        if (nextPrayerTimeEl) {
            nextPrayerTimeEl.textContent = state.lang === 'bn' ?
                `পরবর্তী ${nextName} শুরু: ${nextFormatted}` :
                `Next ${status.nextWaqt} starts at ${nextFormatted}`;
        }
        if (prayerScreenStatus) {
            prayerScreenStatus.innerHTML = `<i class="fa-solid fa-circle-check" style="color:var(--emerald);"></i> নিয়মিত ওয়াক্ত চলছে`;
            prayerScreenStatus.style.color = 'var(--emerald)';
            prayerScreenStatus.style.background = 'rgba(7, 91, 69, 0.08)';
        }
    }

    // Highlight active in Home horizontal prayers row
    const prayerCols = document.querySelectorAll('#home-prayers-row .prayer-col');
    prayerCols.forEach((col) => {
        const p = col.dataset.prayer;
        col.classList.toggle('active', p === status.currentWaqt);
    });

    // Highlight in Prayer detail screen
    ['Fajr', 'Sunrise', 'Dhuhr', 'Asr', 'Maghrib', 'Isha'].forEach((p) => {
        const item = document.getElementById(`item-${p.toLowerCase()}`);
        if (item) {
            item.classList.toggle('active-list-item', p === status.currentWaqt);
        }
    });
}

// Update Prayer UI Display Elements
function updatePrayerTimingsUI() {
    const hijriEl = document.getElementById('hijri-date-text');
    if (hijriEl) hijriEl.textContent = state.hijriDate;
    const gregEl = document.getElementById('gregorian-date-text');
    if (gregEl && !state.gregorianDate) {
        const d = new Date();
        const options = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };
        state.gregorianDate = d.toLocaleDateString(state.lang === 'bn' ? 'bn-BD' : 'en-US', options);
    }
    if (gregEl) gregEl.textContent = state.gregorianDate;

    const calHijriEl = document.getElementById('cal-hijri-date');
    if (calHijriEl) calHijriEl.textContent = state.hijriDate;
    const calGregEl = document.getElementById('cal-greg-date');
    if (calGregEl) calGregEl.textContent = state.gregorianDate;

    const prayerCols = document.querySelectorAll('#home-prayers-row .prayer-col');
    prayerCols.forEach((col) => {
        const p = col.dataset.prayer;
        const timeEl = col.querySelector('.p-time');
        if (timeEl && state.timings[p]) {
            timeEl.textContent = format12Hour(state.timings[p]).replace(' AM', '').replace(' PM', '');
        }
    });

    ['Fajr', 'Sunrise', 'Dhuhr', 'Asr', 'Maghrib', 'Isha'].forEach((p) => {
        const el = document.getElementById(`detail-${p.toLowerCase()}`);
        if (el && state.timings[p]) {
            el.textContent = format12Hour(state.timings[p]);
        }
    });

    const imsakEl = document.getElementById('time-imsak');
    if (imsakEl) imsakEl.textContent = format12Hour(state.timings.Imsak || state.timings.Fajr);
    const iftarEl = document.getElementById('time-iftar');
    if (iftarEl) iftarEl.textContent = format12Hour(state.timings.Maghrib);
    const fastingSehriEl = document.getElementById('fasting-sehri-time');
    if (fastingSehriEl) fastingSehriEl.textContent = format12Hour(state.timings.Imsak || state.timings.Fajr);
    const fastingIftarEl = document.getElementById('fasting-iftar-time');
    if (fastingIftarEl) fastingIftarEl.textContent = format12Hour(state.timings.Maghrib);

    updateAlarmUI();
}

// Calculate Next Jamat/Zamat Time for a Mosque
function calculateNextZamat(mosque) {
    const now = new Date();
    const prayersOrder = ['Fajr', 'Dhuhr', 'Asr', 'Maghrib', 'Isha'];

    for (const key of prayersOrder) {
        const zamatTimeStr = mosque.zamatSchedule[key];
        const [h, m] = zamatTimeStr.split(':').map(Number);
        const zDate = new Date(now.getFullYear(), now.getMonth(), now.getDate(), h, m, 0);

        if (zDate.getTime() > now.getTime()) {
            const prayerName = state.lang === 'bn' ? PRAYER_NAMES[key].bn : PRAYER_NAMES[key].en;
            return {
                prayer: key,
                name: prayerName,
                time: format12Hour(zamatTimeStr),
                rawTime: zamatTimeStr
            };
        }
    }

    const fajrTime = mosque.zamatSchedule.Fajr;
    return {
        prayer: 'Fajr',
        name: state.lang === 'bn' ? PRAYER_NAMES.Fajr.bn : PRAYER_NAMES.Fajr.en,
        time: format12Hour(fajrTime),
        rawTime: fajrTime,
        tomorrow: true
    };
}

// Free OpenStreetMap / Native Kotlin Mosque Locator
async function fetchNearbyMosques() {
    if (window.AndroidBridge && typeof window.AndroidBridge.getNearbyMosques === 'function') {
        try {
            const jsonStr = window.AndroidBridge.getNearbyMosques();
            if (jsonStr) {
                const parsed = JSON.parse(jsonStr);
                if (Array.isArray(parsed) && parsed.length > 0) {
                    state.mosques = processMosquesList(parsed);
                    renderMosquesList();
                    return;
                }
            }
        } catch (e) {
            console.warn('Native getNearbyMosques error, falling back:', e);
        }
    }

    const cfg = window.APP_CONFIG || {};
    const baseLat = cfg.BASE_LAT || 23.693639;
    const baseLng = cfg.BASE_LNG || 90.450333;
    const radiusKm = cfg.MOSQUE_SEARCH_RADIUS_KM || 1.0;
    const nominatimUrl = cfg.OSM_NOMINATIM_API_URL || 'https://nominatim.openstreetmap.org/search';

    const degDelta = (radiusKm * 1.3) / 111.0;
    const left = (baseLng - degDelta).toFixed(5);
    const right = (baseLng + degDelta).toFixed(5);
    const top = (baseLat + degDelta).toFixed(5);
    const bottom = (baseLat - degDelta).toFixed(5);

    const localDefaults = [
        {
            name: 'নূরপুর সেন্ট্রাল জামে মসজিদ',
            nameEn: 'Nurpur Central Jame Mosque',
            address: 'সেন্ট্রাল রোড, নূরপুর, দনিয়া, যাত্রাবাড়ী, ঢাকা',
            lat: 23.6987,
            lon: 90.4498,
            distance: calculateDistanceKm(baseLat, baseLng, 23.6987, 90.4498),
            photoUrl: 'https://images.unsplash.com/photo-1591604129939-f1efa4d9f7fa?auto=format&fit=crop&q=80&w=700',
            mapsQuery: 'Nurpur+Central+Jame+Mosque+Dania+Dhaka',
            tags: ['মহিলাদের নামাজের ব্যবস্থা', 'শীতাতপ নিয়ন্ত্রিত', '৩ তলা কমপ্লেক্স']
        },
        {
            name: 'দনিয়া কেন্দ্রীয় জামে মসজিদ',
            nameEn: 'Dania Central Jame Mosque',
            address: 'দনিয়া বাজার রোড, যাত্রাবাড়ী, ঢাকা',
            lat: 23.6952,
            lon: 90.4532,
            distance: calculateDistanceKm(baseLat, baseLng, 23.6952, 90.4532),
            photoUrl: 'https://images.unsplash.com/photo-1542816417-0983c9c9ad53?auto=format&fit=crop&q=80&w=700',
            mapsQuery: 'Dania+Central+Jame+Mosque+Jatrabari+Dhaka',
            tags: ['প্রশস্ত জামাতখানা', 'এসি সুবিধা', 'সরাসরি প্রবেশদ্বার']
        },
        {
            name: 'আহলে হাদিস জামে মসজিদ',
            nameEn: 'Ahle Hadith Jame Mosque',
            address: 'আল-আকসা মসজিদ রোড, দাসপাড়া, দনিয়া',
            lat: 23.6975,
            lon: 90.4475,
            distance: calculateDistanceKm(baseLat, baseLng, 23.6975, 90.4475),
            photoUrl: 'https://images.unsplash.com/photo-1564769625905-50e93615e769?auto=format&fit=crop&q=80&w=700',
            mapsQuery: 'Ahle+Hadith+Jame+Mosque+Al+aksa+Road+Dania+Dhaka',
            tags: ['নিয়মিত জামাত', 'সুপরিসর ওজুখানা', 'সরাসরি সড়ক সংলগ্ন']
        },
        {
            name: 'যাত্রাবাড়ী চৌরাস্তা বায়তুল আমান জামে মসজিদ',
            nameEn: 'Baitul Aman Jame Mosque',
            address: 'চৌরাস্তা সংলগ্ন, যাত্রাবাড়ী, ঢাকা',
            lat: 23.7020,
            lon: 90.4410,
            distance: calculateDistanceKm(baseLat, baseLng, 23.7020, 90.4410),
            photoUrl: 'https://images.unsplash.com/photo-1584551246679-0daf3d275d0f?auto=format&fit=crop&q=80&w=700',
            mapsQuery: 'Baitul+Aman+Jame+Mosque+Jatrabari+Dhaka',
            tags: ['জুমার জামাত ১:৩০ PM', 'মারকাজ মসজিদ']
        },
        {
            name: 'বায়তুল মোকাররম জাতীয় মসজিদ',
            nameEn: 'Baitul Mukarrom National Mosque',
            address: 'পল্টন, ঢাকা',
            lat: 23.7297,
            lon: 90.4125,
            distance: 1.2,
            photoUrl: 'https://images.unsplash.com/photo-1519817650390-64a93db51149?auto=format&fit=crop&q=80&w=700',
            mapsQuery: 'Baitul+Mukarram+National+Mosque+Dhaka',
            tags: ['জাতীয় মসজিদ', 'মহিলাদের অংশ', 'এসি']
        }
    ];

    try {
        const queryUrl = `${nominatimUrl}?q=mosque&format=json&limit=15&viewbox=${left},${top},${right},${bottom}&bounded=1`;
        const res = await fetch(queryUrl);
        if (res.ok) {
            const data = await res.json();
            if (Array.isArray(data) && data.length > 0) {
                const fetchedMosques = data.map((item, idx) => {
                    const lat = parseFloat(item.lat);
                    const lon = parseFloat(item.lon);
                    const dist = calculateDistanceKm(baseLat, baseLng, lat, lon);
                    const matchedDefault = localDefaults.find(d => item.name && (item.name.includes(d.name) || d.name.includes(item.name)));
                    const defaultPhoto = matchedDefault ? matchedDefault.photoUrl : (localDefaults[idx % localDefaults.length].photoUrl);

                    return {
                        name: item.name || item.display_name.split(',')[0],
                        nameEn: item.name || 'Local Mosque',
                        address: item.display_name,
                        lat: lat,
                        lon: lon,
                        distance: dist,
                        photoUrl: defaultPhoto,
                        mapsQuery: encodeURIComponent(item.name || item.display_name.split(',')[0]),
                        tags: ['নিয়মিত জামাত', 'সুপরিসর']
                    };
                }).filter(m => m.distance <= (radiusKm * 1.5));

                if (fetchedMosques.length > 0) {
                    state.mosques = processMosquesList(fetchedMosques);
                    renderMosquesList();
                    return;
                }
            }
        }
    } catch (e) {
        console.warn('[Muslim Ummah] OpenStreetMap fetch error, using radius-filtered vetted local mosques:', e);
    }

    state.mosques = processMosquesList(localDefaults.filter(m => m.distance <= (radiusKm * 1.6)));
    renderMosquesList();
}

function processMosquesList(list) {
    return list.map((m) => {
        const fajrWaqt = state.timings.Fajr || '04:52';
        const asrWaqt = state.timings.Asr || '14:53';
        const maghribWaqt = state.timings.Maghrib || '17:15';

        const zamatSchedule = {
            Fajr: addMinutesToTime(fajrWaqt, 38),
            Dhuhr: '13:15',
            Asr: addMinutesToTime(asrWaqt, 25),
            Maghrib: addMinutesToTime(maghribWaqt, 10),
            Isha: '19:45',
            Juma: '13:30'
        };

        return {
            ...m,
            zamatSchedule: zamatSchedule
        };
    }).sort((a, b) => a.distance - b.distance);
}

function renderMosquesList() {
    const container = document.getElementById('mosques-list-container');
    const radiusDisplay = document.getElementById('mosque-radius-display');
    const baseCoordsEl = document.getElementById('mosque-base-coords');
    const homeRadiusText = document.getElementById('home-radius-text');
    const profileRadiusText = document.getElementById('profile-radius-text');

    const cfg = window.APP_CONFIG || {};
    const radiusKm = cfg.MOSQUE_SEARCH_RADIUS_KM || 1.0;

    const radiusLabel = state.lang === 'bn' ? `${toBengaliNumerals(radiusKm)} কিমি ব্যাসার্ধে অনুসন্ধান` : `Searching within ${radiusKm} km radius`;
    if (radiusDisplay) radiusDisplay.textContent = radiusLabel;
    if (homeRadiusText) homeRadiusText.textContent = state.lang === 'bn' ? `${toBengaliNumerals(radiusKm)} কিমি ব্যাসার্ধ` : `${radiusKm} km radius`;
    if (profileRadiusText) profileRadiusText.textContent = `${radiusKm} km (.env)`;
    if (baseCoordsEl) baseCoordsEl.textContent = `বেস কোঅর্ডিনেট: 23°41'37.1"N, 90°27'01.2"E (${cfg.BASE_LOCATION_NAME || 'ঢাকা'})`;

    if (!container) return;

    if (!state.mosques || state.mosques.length === 0) {
        container.innerHTML = `
            <div class="card mt-15 text-center">
                <i class="fa-solid fa-mosque" style="font-size:32px; color:var(--text-sec); opacity:0.5;"></i>
                <h4 class="mt-10">${state.lang === 'bn' ? 'কোনো মসজিদ পাওয়া যায়নি' : 'No Mosque Found'}</h4>
            </div>
        `;
        return;
    }

    const closest = state.mosques[0];
    if (closest) {
        const homeMosqueName = document.getElementById('home-mosque-name');
        const homeMosqueSub = document.getElementById('home-mosque-sub');
        const homeMosqueImg = document.getElementById('home-mosque-img');
        const nextZ = calculateNextZamat(closest);
        if (homeMosqueName) homeMosqueName.textContent = closest.name;
        if (homeMosqueImg) {
            homeMosqueImg.src = closest.photoUrl;
            homeMosqueImg.style.display = 'block';
        }
        if (homeMosqueSub) {
            const distStr = state.lang === 'bn' ? `${toBengaliNumerals(closest.distance)} কিমি` : `${closest.distance} km`;
            homeMosqueSub.textContent = `পরবর্তী জামাত: ${nextZ.name} ${nextZ.time} • ${distStr}`;
        }
    }

    let html = '';
    state.mosques.forEach((mosque, idx) => {
        const nextZ = calculateNextZamat(mosque);
        const distStr = state.lang === 'bn' ? `${toBengaliNumerals(mosque.distance)} কিমি` : `${mosque.distance} km`;
        html += `
            <div class="mosque-list-item-card" onclick="openMosqueDetail(${idx})">
                <img src="${mosque.photoUrl}" alt="${mosque.name}" class="mosque-thumbnail" loading="lazy">
                <div style="flex:1;">
                    <div style="display:flex; justify-content:space-between; align-items:flex-start;">
                        <h4 style="font-size:15px; font-weight:700;">${mosque.name}</h4>
                        <span class="zamat-pill"><i class="fa-solid fa-clock"></i> ${nextZ.name} ${nextZ.time}</span>
                    </div>
                    <p class="text-secondary small-text mt-5"><i class="fa-solid fa-location-dot" style="color:var(--emerald);"></i> ${mosque.address.split(',').slice(0, 3).join(', ')} • <strong>${distStr}</strong></p>
                    <div class="tags mt-10">
                        <span class="tag"><i class="fa-solid fa-person-walking"></i> হেঁটেই পৌঁছান (${distStr})</span>
                        <span class="tag"><i class="fa-solid fa-camera"></i> ছবি সংরক্ষিত</span>
                    </div>
                </div>
            </div>
        `;
    });
    container.innerHTML = html;
}

function filterMosques(keyword) {
    const query = (keyword || '').toLowerCase().trim();
    const container = document.getElementById('mosques-list-container');
    if (!container) return;

    if (!query) {
        renderMosquesList();
        return;
    }

    const filtered = state.mosques.filter((m) =>
        m.name.toLowerCase().includes(query) ||
        (m.nameEn && m.nameEn.toLowerCase().includes(query)) ||
        m.address.toLowerCase().includes(query)
    );

    let html = '';
    filtered.forEach((mosque, idx) => {
        const nextZ = calculateNextZamat(mosque);
        const distStr = state.lang === 'bn' ? `${toBengaliNumerals(mosque.distance)} কিমি` : `${mosque.distance} km`;
        html += `
            <div class="mosque-list-item-card" onclick="openMosqueDetail(${idx})">
                <img src="${mosque.photoUrl}" alt="${mosque.name}" class="mosque-thumbnail" loading="lazy">
                <div style="flex:1;">
                    <div style="display:flex; justify-content:space-between; align-items:flex-start;">
                        <h4 style="font-size:15px; font-weight:700;">${mosque.name}</h4>
                        <span class="zamat-pill"><i class="fa-solid fa-clock"></i> ${nextZ.name} ${nextZ.time}</span>
                    </div>
                    <p class="text-secondary small-text mt-5"><i class="fa-solid fa-location-dot" style="color:var(--emerald);"></i> ${mosque.address.split(',').slice(0, 3).join(', ')} • <strong>${distStr}</strong></p>
                </div>
            </div>
        `;
    });
    container.innerHTML = html || `<p class="text-center text-secondary small-text mt-20">কোনো ফলাফল পাওয়া যায়নি</p>`;
}

function refreshMosques() {
    const container = document.getElementById('mosques-list-container');
    if (container) container.innerHTML = `<p class="text-center text-secondary small-text mt-20"><i class="fa-solid fa-spinner fa-spin"></i> রিফ্রেশ করা হচ্ছে...</p>`;
    fetchNearbyMosques();
}

function openMosqueDetail(idx) {
    const mosque = state.mosques[idx];
    if (!mosque) return;
    state.selectedMosque = mosque;

    const coverEl = document.getElementById('detail-mosque-cover');
    const nameEl = document.getElementById('detail-mosque-name');
    const addrEl = document.getElementById('detail-mosque-address');
    const zamatBadgeEl = document.getElementById('detail-next-zamat-badge');
    const zamatTextEl = document.getElementById('detail-next-zamat-text');
    const btnDirections = document.getElementById('btn-directions');

    if (coverEl && mosque.photoUrl) {
        coverEl.style.background = `linear-gradient(rgba(0,0,0,0.3), rgba(0,0,0,0.5)), url('${mosque.photoUrl}') center/cover no-repeat`;
    }

    const nextZ = calculateNextZamat(mosque);
    const distStr = state.lang === 'bn' ? `${toBengaliNumerals(mosque.distance)} কিমি` : `${mosque.distance} km`;

    if (nameEl) nameEl.textContent = mosque.name;
    if (addrEl) addrEl.textContent = `${mosque.address.split(',').slice(0, 3).join(', ')} • ${distStr} (হেঁটে প্রায় ${Math.ceil(mosque.distance * 12)} মিনিট)`;
    if (zamatBadgeEl) zamatBadgeEl.textContent = `${nextZ.name} ${nextZ.time}`;
    if (zamatTextEl) {
        zamatTextEl.textContent = state.lang === 'bn' ?
            `আসন্ন জামাত শুরু হবে ${nextZ.time} মিনিটে। ওযু করে প্রস্তুতি নিন।` :
            `Next Jama'at starts at ${nextZ.time}. Prepare for prayer.`;
    }

    const z = mosque.zamatSchedule;
    if (document.getElementById('zamat-fajr')) document.getElementById('zamat-fajr').textContent = format12Hour(z.Fajr);
    if (document.getElementById('zamat-dhuhr')) document.getElementById('zamat-dhuhr').textContent = format12Hour(z.Dhuhr);
    if (document.getElementById('zamat-asr')) document.getElementById('zamat-asr').textContent = format12Hour(z.Asr);
    if (document.getElementById('zamat-maghrib')) document.getElementById('zamat-maghrib').textContent = format12Hour(z.Maghrib);
    if (document.getElementById('zamat-isha')) document.getElementById('zamat-isha').textContent = format12Hour(z.Isha);
    if (document.getElementById('zamat-juma')) document.getElementById('zamat-juma').textContent = format12Hour(z.Juma);

    if (btnDirections) {
        btnDirections.onclick = () => {
            const mapsUrl = `https://www.google.com/maps/dir/?api=1&destination=${mosque.lat},${mosque.lon}`;
            window.open(mapsUrl, '_blank');
        };
    }

    navTo('screen-mosque-detail');
}

// Open Native Settings Activity from JS Interface
function openNativeSettings() {
    if (window.AndroidBridge && typeof window.AndroidBridge.openSettings === 'function') {
        try {
            window.AndroidBridge.openSettings();
        } catch (e) {
            console.warn('Failed to open native settings:', e);
        }
    }
}

// Alarm Configuration Modal & State Management
function openAlarmModal() {
    const backdrop = document.getElementById('alarm-modal-backdrop');
    const container = document.getElementById('alarm-pickers-container');
    if (!container || !backdrop) return;

    let html = '';
    ['Fajr', 'Dhuhr', 'Asr', 'Maghrib', 'Isha'].forEach((pKey) => {
        const conf = state.alarms[pKey] || { enabled: true, customTime: '' };
        const pName = state.lang === 'bn' ? PRAYER_NAMES[pKey].bn : PRAYER_NAMES[pKey].en;
        const defaultTime = state.timings[pKey] ? state.timings[pKey].split(' ')[0] : '05:00';
        const currentTimeVal = conf.customTime || defaultTime;

        html += `
            <div class="alarm-time-picker-row">
                <div style="display:flex; align-items:center; gap:10px;">
                    <span class="toggle ${conf.enabled ? 'active' : ''}" id="modal-toggle-${pKey}" onclick="this.classList.toggle('active')"></span>
                    <div>
                        <strong>${pName} সালাত</strong>
                        <p class="small-text text-secondary">ওয়াক্ত: ${format12Hour(defaultTime)}</p>
                    </div>
                </div>
                <div>
                    <input type="time" id="modal-time-${pKey}" value="${currentTimeVal}">
                </div>
            </div>
        `;
    });

    container.innerHTML = html;

    // Highlight current selected tone in modal
    document.querySelectorAll('#modal-sound-options [data-sound]').forEach((chip) => {
        chip.classList.toggle('active', chip.dataset.sound === state.selectedTone);
    });

    backdrop.classList.add('active');
}

function closeAlarmModal(e) {
    if (e && e.target && e.target.id !== 'alarm-modal-backdrop' && !e.target.classList.contains('modal-close-btn')) {
        return;
    }
    const backdrop = document.getElementById('alarm-modal-backdrop');
    if (backdrop) backdrop.classList.remove('active');
}

function saveAlarms() {
    ['Fajr', 'Dhuhr', 'Asr', 'Maghrib', 'Isha'].forEach((pKey) => {
        const toggle = document.getElementById(`modal-toggle-${pKey}`);
        const input = document.getElementById(`modal-time-${pKey}`);
        if (toggle && input) {
            state.alarms[pKey] = {
                enabled: toggle.classList.contains('active'),
                customTime: input.value
            };
        }
    });

    saveAlarmsToStorage();
    const backdrop = document.getElementById('alarm-modal-backdrop');
    if (backdrop) backdrop.classList.remove('active');

    alert(state.lang === 'bn' ? 'নামাজের অ্যালার্ম ও আজানের সুর সংরক্ষিত হয়েছে!' : 'Prayer alarms & azan tones saved successfully!');
}

function toggleSingleAlarm(pKey, e) {
    if (e) e.stopPropagation();
    if (!state.alarms[pKey]) state.alarms[pKey] = { enabled: true, customTime: '' };
    state.alarms[pKey].enabled = !state.alarms[pKey].enabled;
    saveAlarmsToStorage();
}

function updateAlarmUI() {
    ['Fajr', 'Dhuhr', 'Asr', 'Maghrib', 'Isha'].forEach((pKey) => {
        const item = document.getElementById(`item-${pKey.toLowerCase()}`);
        if (item) {
            const bell = item.querySelector('.alarm-toggle-btn');
            const isEnabled = state.alarms[pKey]?.enabled;
            if (bell) {
                bell.classList.toggle('inactive', !isEnabled);
                bell.title = isEnabled ? 'অ্যালার্ম চালু আছে' : 'অ্যালার্ম বন্ধ';
            }
        }
    });

    const summaryText = document.getElementById('alarm-summary-text');
    if (summaryText) {
        const activeCount = Object.values(state.alarms).filter(a => a.enabled).length;
        summaryText.textContent = state.lang === 'bn' ?
            `৫টির মধ্যে ${toBengaliNumerals(activeCount)}টি ওয়াক্তের অ্যালার্ম সক্রিয় (${AZAN_TONES[state.selectedTone]?.name || 'আজান'})` :
            `${activeCount} of 5 prayer alarms active (${AZAN_TONES[state.selectedTone]?.nameEn || 'Azan'})`;
    }
}

function testAlarmSound() {
    playAlarmTone();
}

// Language / Translation Management
function updateLanguageButtons() {
    document.querySelectorAll('[data-lang]').forEach((button) => {
        const isActive = button.dataset.lang === state.lang;
        button.classList.toggle('active', isActive);
    });
}

function applyTranslations() {
    document.documentElement.lang = state.lang;
    document.querySelectorAll('[data-i18n]').forEach((element) => {
        const key = element.dataset.i18n;
        const value = translations[state.lang] && translations[state.lang][key];
        if (value) {
            element.textContent = value;
        }
    });
    updateLanguageButtons();
    updatePrayerTimingsUI();
    renderMosquesList();
}

function setLanguage(lang) {
    state.lang = lang;
    applyTranslations();
}

// Weekly Prayer Times Generator & Modal
function openWeeklyModal() {
    const backdrop = document.getElementById('weekly-modal-backdrop');
    const container = document.getElementById('weekly-table-container');
    if (!container || !backdrop) return;

    const days = ['রবিবার', 'সোমবার', 'মঙ্গলবার', 'বুধবার', 'বৃহস্পতিবার', 'শুক্রবার', 'শনিবার'];
    const now = new Date();
    let html = `
        <div style="overflow-x:auto;">
            <table style="width:100%; border-collapse:collapse; font-size:12px; text-align:center;">
                <thead>
                    <tr style="background:#e8f4f0; color:var(--emerald);">
                        <th style="padding:6px; border:1px solid #ddd;">দিন</th>
                        <th style="padding:6px; border:1px solid #ddd;">ফজর</th>
                        <th style="padding:6px; border:1px solid #ddd;">যোহর</th>
                        <th style="padding:6px; border:1px solid #ddd;">আসর</th>
                        <th style="padding:6px; border:1px solid #ddd;">মাগরিব</th>
                        <th style="padding:6px; border:1px solid #ddd;">ইশা</th>
                    </tr>
                </thead>
                <tbody>
    `;

    for (let i = 0; i < 7; i++) {
        const d = new Date(now.getFullYear(), now.getMonth(), now.getDate() + i);
        const dayName = days[d.getDay()];
        const isToday = i === 0;
        const fajr = addMinutesToTime(state.timings.Fajr, i);
        const dhuhr = state.timings.Dhuhr;
        const asr = addMinutesToTime(state.timings.Asr, i);
        const maghrib = addMinutesToTime(state.timings.Maghrib, -i);
        const isha = addMinutesToTime(state.timings.Isha, -i);

        html += `
            <tr style="${isToday ? 'background:rgba(214,182,109,0.18); font-weight:700;' : ''}">
                <td style="padding:6px; border:1px solid #ddd;">${dayName}${isToday ? ' (আজ)' : ''}</td>
                <td style="padding:6px; border:1px solid #ddd;">${format12Hour(fajr).replace(' AM', '')}</td>
                <td style="padding:6px; border:1px solid #ddd;">${format12Hour(dhuhr).replace(' PM', '')}</td>
                <td style="padding:6px; border:1px solid #ddd;">${format12Hour(asr).replace(' PM', '')}</td>
                <td style="padding:6px; border:1px solid #ddd;">${format12Hour(maghrib).replace(' PM', '')}</td>
                <td style="padding:6px; border:1px solid #ddd;">${format12Hour(isha).replace(' PM', '')}</td>
            </tr>
        `;
    }

    html += `
                </tbody>
            </table>
        </div>
    `;

    container.innerHTML = html;
    backdrop.classList.add('active');
}

function closeWeeklyModal(e) {
    if (e && e.target && e.target.id !== 'weekly-modal-backdrop' && !e.target.classList.contains('modal-close-btn')) {
        return;
    }
    const backdrop = document.getElementById('weekly-modal-backdrop');
    if (backdrop) backdrop.classList.remove('active');
}

// Special Days & Nafl Prayer Details Database
const SPECIAL_DAYS_DATA = {
    'shab_e_barat': {
        title: 'শবে বরাত (লাইলাতুল বরাত)',
        date: '১৫ শাবান দিবাগত রাত',
        desc: 'আল্লাহ তাআলার রহমত, মাগফিরাত ও ক্ষমা প্রার্থনার বিশেষ রজনী। এই রাতে বান্দার গুনাহ মাফ ও হেদায়েত প্রার্থনা করা হয়।',
        info: [
            { label: 'আমল', text: 'অধিক পরিমাণে ইস্তিগফার, নফল নামাজ, কুরআন তিলাওয়াত ও দোয়া।' },
            { label: 'নফল রোজা', text: 'পরের দিন (১৫ শাবান) নফল রোজা রাখা মুস্তাহাব।' },
            { label: 'বর্জনীয়', text: 'অপ্রমাণিত কোনো আনুষ্ঠানিকতা বা আলোকসজ্জা পরিহার করে একাগ্রচিত্তে আল্লাহর ইবাদতে মশগুল থাকা।' }
        ]
    },
    'shab_e_qadr': {
        title: 'পবিত্র শবে কদর (লাইলাতুল কদর)',
        date: 'রমজানুল মুবারকের শেষ দশকের বিজোড় রাত (২১, ২৩, ২৫, ২৭, ২৯)',
        desc: 'হাজার মাসের চেয়েও উত্তম ও বরকতময় এক মহামহিম রাত। এই পবিত্র রজনীতে মহাগ্রন্থ পবিত্র কুরআনুল কারিম নাজিল হয়েছিল।',
        info: [
            { label: 'বিশেষ ফজিলত', text: 'কুরআনুল কারিমের সূরা আল-কদরে ঘোষণা করা হয়েছে—লাইলাতুল কদর এক হাজার মাসের চেয়েও শ্রেষ্ঠ।' },
            { label: 'রাসূলুল্লাহ (সা.) এর শেখানো দোয়া', text: '"আল্লাহুম্মা ইন্নাকা \'আফুউন তুহিব্বুল \'আফওয়া ফা\'ফু \'আন্নী" (হে আল্লাহ, আপনি পরম ক্ষমাশীল, ক্ষমা ভালোবাসেন, অতএব আমাকে ক্ষমা করুন)।' },
            { label: 'আমল', text: 'কিয়ামুল লাইল (তাহাজ্জুদ), দান-সদকা, দীর্ঘ সেজদায় রোনাজারি।' }
        ]
    },
    'juma': {
        title: 'জুমার দিন (ইয়াউমুল জুমু\'আ)',
        date: 'সাপ্তাহিক শ্রেষ্ঠ দিন (প্রতি শুক্রবার)',
        desc: 'সপ্তাহের শ্রেষ্ঠতম মুবারক দিন। মুসলমানদের জন্য এই দিনটি সাপ্তাহিক ঈদের সমতুল্য মর্যাদাপূর্ণ।',
        info: [
            { label: 'আমল ও সুন্নত', text: 'উত্তমরূপে গোসল করা, পরিষ্কার পোশাক পরিধান করা, সুগন্ধি ব্যবহার এবং সূরা কাহাফ তিলাওয়াত করা।' },
            { label: 'দরুদ শরীফ', text: 'জুমার দিনে রাসূলুল্লাহ (সা.)-এর প্রতি অধিক পরিমাণে দরুদ পাঠ করার বিশেষ তাগিদ রয়েছে।' },
            { label: 'দোয়া কবুলের সময়', text: 'আসরের পর থেকে সূর্যাস্ত পর্যন্ত বিশেষ মুহূর্তে দোয়া কবুল হয়।' }
        ]
    },
    'ashura': {
        title: 'পবিত্র আশুরা',
        date: '১০ মহররম',
        desc: 'ইসলামের ইতিহাসে বহু স্মরণীয় তাৎপর্যপূর্ণ ঘটনার ঐতিহাসিক দিন। এই দিনে মূসা (আ.) ও বনী ইসরাঈল ফেরাউনের কবল থেকে মুক্তি পেয়েছিলেন।',
        info: [
            { label: 'সুন্নত রোজা', text: '৯ ও ১০ মহররম অথবা ১০ ও ১১ মহররম দুইটি নফল রোজা রাখা সুন্নতে মুয়াক্কাদা।' },
            { label: 'ফজিলত', text: 'হাদিস শরিফে এসেছে, আশুরার রোজা পূর্ববর্তী এক বছরের সগিরা গুনাহের কাফফারা স্বরূপ।' }
        ]
    },
    'arafa': {
        title: 'আরাফার দিন (ইয়াউমে আরাফাহ)',
        date: '৯ জিলহজ',
        desc: 'হজের মূল দিন এবং বছরের শ্রেষ্ঠতম ফজিলতপূর্ণ দিনগুলোর অন্যতম।',
        info: [
            { label: 'হজব্যতীত ব্যক্তিদের রোজা', text: 'যারা হজে নেই তাদের জন্য ৯ জিলহজ আরাফার রোজা রাখা অত্যন্ত সওয়াবের কাজ।' },
            { label: 'ফজিলত', text: 'সহিহ মুসলিমের হাদিসে এসেছে, আরাফার দিনের রোজা পূর্ববর্তী এক বছর ও পরবর্তী এক বছরের গুনাহ মোচন করে।' },
            { label: 'তাকবিরে তাশরিক', text: '৯ জিলহজ ফজর থেকে ১৩ জিলহজ আসর পর্যন্ত প্রত্যেক ফরজ নামাজের পর তাকবিরে তাশরিক পাঠ ওয়াজিব।' }
        ]
    }
};

const NAFL_PRAYER_DATA = {
    'ishraq': {
        title: 'ইশরাক সালাত',
        time: 'সূর্যোদয়ের আনুমানিক ১৫-২০ মিনিট পর থেকে শুরু',
        rakat: '২ থেকে ৪ রাকাত',
        desc: 'সূর্য পুরোপুরি ওঠার পর আদায়কৃত নফল নামাজ। তিরমিজি শরিফের হাদিস অনুযায়ী পূর্ণ একটি গ্রহণযোগ্য হজ ও ওমরার সমতুল্য সওয়াব মেলে।',
        niyyat: 'দুই রাকাত ইশরাকের নফল নামাজ আদায় করছি।'
    },
    'chasht': {
        title: 'চাশত / সালাত উদ-দুহা',
        time: 'পূর্বাহ্নে যখন সূর্যের তাপ বৃদ্ধি পায় (সকাল ৯:৩০ থেকে জাওয়ালের পূর্ব পর্যন্ত)',
        rakat: '২, ৪, ৮ বা ১২ রাকাত (সাধারণত ৪ রাকাত উত্তম)',
        desc: 'শরীরের প্রতিটি জোড়ের সদকাস্বরূপ এই সালাত বান্দার রিজিক ও হেফাযতে বরকত বয়ে আনে।',
        niyyat: 'সালাতুদ দুহা / চাশতের নফল নামাজ আদায় করছি।'
    },
    'tahajjud': {
        title: 'তাহাজ্জুদ সালাত',
        time: 'রাতের শেষ তৃতীয়াংশে (ফজরের সুবহে সাদিকের পূর্ব পর্যন্ত)',
        rakat: '২ থেকে ১২ রাকাত (২ রাকাত করে আদায়)',
        desc: 'সকল নফল নামাজের মধ্যে শ্রেষ্ঠতম মর্যাদাপূর্ণ নামাজ। আল্লাহ তাআলা রাতের শেষ তৃতীয়াংশে প্রথম আসমানে অবতরণ করে বান্দার দোয়া কবুল করেন।',
        niyyat: 'তাহাজ্জুদের নফল নামাজ আল্লাহর সন্তুষ্টির উদ্দেশ্যে আদায় করছি।'
    },
    'wudu': {
        title: 'তাহিয়াতুল ওযু',
        time: 'ওযু করার পর অঙ্গ শুকানোর পূর্বে',
        rakat: '২ রাকাত',
        desc: 'মনোযোগ সহকারে ওযু সম্পন্ন করে দুই রাকাত নামাজ আদায়কারী বান্দার জন্য জান্নাত ওয়াজিব হয়ে যাওয়ার সুসংবাদ রয়েছে হাদিস শরিফে।',
        niyyat: 'তাহিয়াতুল ওযুর দুই রাকাত নফল নামাজ আদায় করছি।'
    },
    'masjid': {
        title: 'তাহিয়াতুল মসজিদ',
        time: 'মসজিদে প্রবেশ করার পর বসার পূর্বে',
        rakat: '২ রাকাত',
        desc: 'মসজিদের সম্মানার্থে বসার পূর্বেই এই দুই রাকাত নামাজ আদায় করা সুন্নত/মুস্তাহাব।',
        niyyat: 'তাহিয়াতুল মসজিদের দুই রাকাত নফল নামাজ আদায় করছি।'
    },
    'tasbih': {
        title: 'সালাতুত তাসবিহ',
        time: 'মাকরূহ সময় ব্যতীত দিন বা রাতের যেকোনো সময়',
        rakat: '৪ রাকাত (বিশেষ তাসবিহসহ)',
        desc: 'এই নামাজে ৩০০ বার "সুবহানাল্লাহি ওয়াল হামদুলিল্লাহি ওয়া লা ইলাহা ইল্লাল্লাহু ওয়াল্লাহু আকবার" পাঠ করা হয়। জীবনের পূর্বাপর সকল গুনাহ মাফের সুসংবাদ রয়েছে।',
        niyyat: 'সালাতুত তাসবিহের চার রাকাত নামাজ আদায় করছি।'
    }
};

function openSpecialDay(key, shouldNavigate = true) {
    const data = SPECIAL_DAYS_DATA[key] || SPECIAL_DAYS_DATA['shab_e_barat'];
    const header = document.getElementById('special-day-header');
    const content = document.getElementById('special-day-content');
    if (header) header.textContent = data.title;
    if (content) {
        let infoHtml = '';
        data.info.forEach(item => {
            infoHtml += `<div><strong>${item.label}:</strong> ${item.text}</div>`;
        });
        content.innerHTML = `
            <p class="small-text text-secondary">${data.date}</p>
            <h3 class="mt-5">${data.title}</h3>
            <p class="mt-10">${data.desc}</p>
            <div class="info-list mt-15">
                ${infoHtml}
            </div>
        `;
    }
    if (shouldNavigate) {
        navTo('screen-special-day');
    }
}

function openNaflDetail(key, shouldNavigate = true) {
    const data = NAFL_PRAYER_DATA[key] || NAFL_PRAYER_DATA['ishraq'];
    const header = document.getElementById('nafl-detail-header');
    const content = document.getElementById('nafl-detail-content');
    if (header) header.textContent = data.title;
    if (content) {
        content.innerHTML = `
            <p class="small-text text-secondary">নফল নামাজের বিস্তারিত বিবরণ</p>
            <h3 class="mt-5">${data.title}</h3>
            <p class="mt-10">${data.desc}</p>
            <div class="info-list mt-15">
                <div><strong>সময়:</strong> ${data.time}</div>
                <div><strong>রাকাত সংখ্যা:</strong> ${data.rakat}</div>
                <div><strong>নিয়তের মর্ম:</strong> "${data.niyyat}"</div>
            </div>
        `;
    }
    if (shouldNavigate) {
        navTo('screen-nafl-detail');
    }
}

// Masnoon Dua Library with Arabic, Bengali Pronunciation, and Bengali Translation
const MASNOON_DUAS = [
    {
        id: 'sleep',
        title: 'ঘুমানোর পূর্বের দোয়া',
        titleEn: 'Dua Before Sleeping',
        arabic: 'بِاسْمِكَ اللَّهُمَّ أَمُوتُ وَأَحْيَا',
        pronunciation: 'বিসমিকাল্লা-হুম্মা আমূতু ওয়া আহ্ইয়া',
        bangla: 'হে আল্লাহ! আপনারই নামে আমি মৃত্যুবরণ (ঘুমাই) করি এবং আপনারই নামে জীবিত (জাগ্রত) হই।',
        ref: 'সহিহ বুখারি ৬৩১২'
    },
    {
        id: 'wake',
        title: 'ঘুম থেকে ওঠার দোয়া',
        titleEn: 'Dua After Waking Up',
        arabic: 'الْحَمْدُ لِلَّهِ الَّذِي أَحْيَانَا بَعْدَ مَا أَمَاتَنَا وَإِلَيْهِ النُّشُورُ',
        pronunciation: 'আলহামদু লিল্লা-হিল্লাযী আহ্ইয়ানা বা\'দা মা আমাতানা ওয়া ইলাইহিন নুশূর',
        bangla: 'সকল প্রশংসা আল্লাহর জন্য, যিনি আমাদেরকে মৃত্যু (ঘুম) দেওয়ার পর পুনরায় জীবিত করলেন, আর তাঁর দিকেই সকলের পুনরুত্থান।',
        ref: 'সহিহ বুখারি ৬৩১২'
    },
    {
        id: 'travel',
        title: 'সফর বা যানবাহনে ওঠার দোয়া',
        titleEn: 'Dua For Travel & Vehicle',
        arabic: 'سُبْحَانَ الَّذِي سَخَّرَ لَنَا هَٰذَا وَمَا كُنَّا لَهُ مُقْرِنِينَ وَإِنَّا إِلَىٰ رَبِّنَا لَمُنقَلِبُونَ',
        pronunciation: 'সুবহা-নাল্লাযী সাখ্খারা লানা হা-যা ওয়ামা কুন্না লাহু মুক্বরিনীনা, ওয়া ইন্না ইলা রব্বিনা লামুনক্বালিবূন',
        bangla: 'পবিত্র ও মহান সেই সত্তা, যিনি এটিকে আমাদের বশীভূত করে দিয়েছেন, অথচ আমরা এটিকে বশীভূত করতে সক্ষম ছিলাম না। এবং আমরা আমাদের পালনকর্তার নিকটই প্রত্যাবর্তনকারী।',
        ref: 'সূরা আয-যুখরুফ ১৩-১৪, তিরমিজি ৩৪৪৬'
    },
    {
        id: 'eating_start',
        title: 'খাবার শুরুর দোয়া',
        titleEn: 'Dua Before Meal',
        arabic: 'بِسْمِ اللَّهِ وَعَلَى بَرَكَةِ اللَّهِ',
        pronunciation: 'বিসমিল্লা-হি ওয়া \'আলা বারাকাতিল্লা-হ',
        bangla: 'আল্লাহর নামে এবং আল্লাহর বরকতের উপর শুরু করছি।',
        ref: 'মুস্তাদরাকে হাকেম'
    },
    {
        id: 'eating_end',
        title: 'খাবার শেষের দোয়া',
        titleEn: 'Dua After Meal',
        arabic: 'الْحَمْدُ لِلَّهِ الَّذِي أَطْعَمَنَا وَسَقَانَا وَجَعَلَنَا مُسْلِمِينَ',
        pronunciation: 'আলহামদু লিল্লা-হিল্লাযী আত\'আমানা ওয়াসাক্বানা ওয়াজা\'আলানা মুসলিমীন',
        bangla: 'সমস্ত প্রশংসা সেই আল্লাহর জন্য যিনি আমাদেরকে খাওয়ালেন, পান করালেন এবং মুসলিমদের অন্তর্ভুক্ত করলেন।',
        ref: 'সুনানে আবু দাউদ ৩৮৫০'
    },
    {
        id: 'mosque_enter',
        title: 'মসজিদে প্রবেশের দোয়া',
        titleEn: 'Dua When Entering Mosque',
        arabic: 'اللَّهُمَّ افْتَحْ لِي أَبْوَابَ رَحْمَتِكَ',
        pronunciation: 'আল্লাহুম্মাফ তাহলী আবওয়াবা রহ্-মাতিক',
        bangla: 'হে আল্লাহ! আমার জন্য আপনার রহমতের দরজাসমূহ উন্মুক্ত করে দিন।',
        ref: 'সহিহ মুসলিম ৭১৩'
    },
    {
        id: 'mosque_leave',
        title: 'মসজিদ থেকে বের হওয়ার দোয়া',
        titleEn: 'Dua When Leaving Mosque',
        arabic: 'اللَّهُمَّ إِنِّي أَسْأَلُكَ مِنْ فَضْلِكَ',
        pronunciation: 'আল্লাহুম্মা ইন্নী আস-আলুকা মিন ফাদ্বলিক',
        bangla: 'হে আল্লাহ! আমি আপনার নিকট আপনার অনুগ্রহ প্রার্থনা করছি।',
        ref: 'সহিহ মুসলিম ৭১৩'
    }
];

function renderDuasList(filterText = '') {
    const container = document.getElementById('duas-list-container');
    if (!container) return;

    const query = filterText.toLowerCase().trim();
    const list = query ? MASNOON_DUAS.filter(d =>
        d.title.toLowerCase().includes(query) ||
        d.pronunciation.toLowerCase().includes(query) ||
        d.bangla.toLowerCase().includes(query)
    ) : MASNOON_DUAS;

    let html = '';
    list.forEach((dua) => {
        html += `
            <div class="list-item clickable" onclick="openDuaDetail('${dua.id}')">
                <div>
                    <strong>${dua.title}</strong>
                    <p class="small-text text-secondary mt-5">${dua.pronunciation.slice(0, 42)}...</p>
                </div>
                <span>দেখুন</span>
            </div>
        `;
    });
    container.innerHTML = html || `<p class="text-center text-secondary small-text mt-20">কোনো দোয়া পাওয়া যায়নি</p>`;
}

function filterDuas(text) {
    renderDuasList(text);
}

function openDuaDetail(duaId) {
    const dua = MASNOON_DUAS.find(d => d.id === duaId) || MASNOON_DUAS[0];
    const header = document.getElementById('detail-dua-header');
    const content = document.getElementById('detail-dua-content');
    if (header) header.textContent = dua.title;
    if (content) {
        content.innerHTML = `
            <h4>${dua.title}</h4>
            <p class="arabic mt-15">${dua.arabic}</p>
            <div class="pronunciation mt-10">
                <span class="pronunciation-label"><i class="fa-solid fa-language"></i> বাংলা উচ্চারণ:</span>
                "${dua.pronunciation}"
            </div>
            <p class="bangla mt-10"><strong>অর্থ:</strong> "${dua.bangla}"</p>
            <p class="ref mt-10"><strong>উৎস:</strong> ${dua.ref}</p>
            <div class="share-row mt-15">
                <i class="fa-regular fa-heart" onclick="this.classList.toggle('fa-solid'); this.style.color='var(--emerald)';"></i>
                <i class="fa-solid fa-copy" onclick="alert('দোয়া কপি করা হয়েছে!')"></i>
                <i class="fa-solid fa-share-nodes" onclick="alert('শেয়ার উইন্ডো')"></i>
            </div>
        `;
    }
    navTo('screen-dua-detail');
}

// Adhkar Counter
function toggleCounter(button) {
    const status = button.parentElement.querySelector('.status-pill');
    const current = Number(button.dataset.count || 0);
    const next = current < 10 ? current + 1 : 0;
    button.dataset.count = next;
    button.textContent = state.lang === 'bn' ? `অবস্থান: ${toBengaliNumerals(next)}/১০` : `Count: ${next}/10`;
    if (status) {
        status.classList.toggle('done', next >= 10);
        status.textContent = next >= 10 ? (state.lang === 'bn' ? 'সমাপ্ত' : 'Done') : (state.lang === 'bn' ? 'অসমাপ্ত' : 'Pending');
    }
}

// Screen Navigation
function navTo(screenId, navElement = null) {
    const screens = document.querySelectorAll('.screen');
    screens.forEach((screen) => screen.classList.remove('active'));
    const target = document.getElementById(screenId);
    if (target) {
        target.classList.add('active');
    }

    const navItems = document.querySelectorAll('.nav-item');
    navItems.forEach((item) => item.classList.remove('active'));

    if (navElement) {
        navElement.classList.add('active');
        return;
    }

    if (screenId === 'screen-home' || screenId === 'screen-prayer' || screenId === 'screen-qibla' || screenId === 'screen-splash') {
        document.querySelector('.nav-item:nth-child(1)')?.classList.add('active');
    } else if (screenId === 'screen-ibadat' || screenId === 'screen-nafl' || screenId === 'screen-adhkar' || screenId === 'screen-dua' || screenId === 'screen-adhan' || screenId === 'screen-restricted' || screenId === 'screen-fasting' || screenId === 'screen-sunnah-fasting' || screenId === 'screen-more') {
        document.querySelector('.nav-item:nth-child(2)')?.classList.add('active');
    } else if (screenId === 'screen-mosques' || screenId === 'screen-mosque-detail' || screenId === 'screen-bazaar' || screenId === 'screen-biz-detail') {
        document.querySelector('.nav-item:nth-child(3)')?.classList.add('active');
    } else if (screenId === 'screen-calendar' || screenId === 'screen-special-day') {
        document.querySelector('.nav-item:nth-child(4)')?.classList.add('active');
    } else if (screenId === 'screen-profile') {
        document.querySelector('.nav-item:nth-child(5)')?.classList.add('active');
    }
}

// Halal Bazaar Listings Database & Filter
const BAZAAR_LISTINGS = [
    {
        id: 'star_kabab',
        category: 'restaurant',
        name: 'স্টার কাবাব & রেস্টুরেন্ট',
        address: 'যাত্রাবাড়ী মোড়, ঢাকা',
        distance: '০.৭ কিমি',
        phone: '০১৭০০০০০০০০',
        photoUrl: 'https://images.unsplash.com/photo-1555939594-58d7cb561ad1?auto=format&fit=crop&q=80&w=700',
        tags: ['মুসলিম মালিকানাধীন', 'হালাল প্রত্যয়িত'],
        desc: 'উন্নত মানের হালাল প্রক্রিয়ায় প্রস্তুতকৃত তাজা কাবাব, বিরিয়ানি ও দেশীয় খাবার।'
    },
    {
        id: 'bengal_meat',
        category: 'meat',
        name: 'বেঙ্গল মিট আউটলেট',
        address: 'শনির আখড়া, যাত্রাবাড়ী, ঢাকা',
        distance: '১.১ কিমি',
        phone: '০৯৬০৬৬৬৬৬৬৬',
        photoUrl: 'https://images.unsplash.com/photo-1607623814075-e51df1bdc82f?auto=format&fit=crop&q=80&w=700',
        tags: ['১০০% জবেহকৃত হালাল গোশত', 'হাইজিন মানসম্মত'],
        desc: 'শরীয়তসম্মত উপায়ে জবাইকৃত গরু, খাসি ও মুরগির তাজা মাংস।'
    },
    {
        id: 'islamic_books',
        category: 'books',
        name: 'মাকতাবাতুল আশরাফ লাইব্রেরি',
        address: 'মাদ্রাসা রোড, যাত্রাবাড়ী, ঢাকা',
        distance: '০.৫ কিমি',
        phone: '০১৮১০০০০০০০',
        photoUrl: 'https://images.unsplash.com/photo-1512820790803-83ca734da794?auto=format&fit=crop&q=80&w=700',
        tags: ['সহিহ ইসলামিক কিতাব', 'তাফসির ও হাদিস'],
        desc: 'সহিহ বুখারি, মুসলিম, তাফসিরে ইবনে কাসির ও প্রামাণ্য ইসলামিক গ্রন্থ সম্ভার।'
    },
    {
        id: 'sunnah_clothing',
        category: 'clothing',
        name: 'আল-বারাকা জুব্বা ও আতর ঘর',
        address: 'দনিয়া বাজার, ঢাকা',
        distance: '০.৬ কিমি',
        phone: '০১৯১০০০০০০০',
        photoUrl: 'https://images.unsplash.com/photo-1583391733956-3750e0ff4e8b?auto=format&fit=crop&q=80&w=700',
        tags: ['সুন্নতি পোশাক', 'খাঁটি আতর ও টুপি'],
        desc: 'উচ্চমানের সুতি কাপড়ের জুব্বা, পায়জামা, জায়নামাজ এবং সৌদি আরবের খাঁটি নন-অ্যালকোহলিক আতর।'
    }
];

function renderBazaarList(filterText = '', category = null) {
    const container = document.getElementById('bazaar-list-container');
    if (!container) return;

    const query = filterText.toLowerCase().trim();
    const list = BAZAAR_LISTINGS.filter(item => {
        const matchesQuery = !query || item.name.toLowerCase().includes(query) || item.address.toLowerCase().includes(query) || item.desc.toLowerCase().includes(query);
        const matchesCategory = !category || item.category === category;
        return matchesQuery && matchesCategory;
    });

    let html = '';
    list.forEach(item => {
        html += `
            <div class="card bazaar-card mt-10" onclick="openBazaarDetail('${item.id}')">
                <img src="${item.photoUrl}" alt="${item.name}" class="mosque-thumbnail" style="width:65px; height:65px;" loading="lazy">
                <div class="bazaar-info" style="flex:1;">
                    <div style="display:flex; justify-content:space-between; align-items:flex-start;">
                        <h4>${item.name}</h4>
                        <span class="zamat-pill">${item.distance}</span>
                    </div>
                    <p class="text-secondary small-text mt-5"><i class="fa-solid fa-location-dot" style="color:var(--emerald);"></i> ${item.address}</p>
                    <span class="tag mt-5"><i class="fa-solid fa-check-circle" style="color:var(--emerald);"></i> ${item.tags[0]}</span>
                </div>
            </div>
        `;
    });

    container.innerHTML = html || `<p class="text-center text-secondary small-text mt-20">কোনো প্রতিষ্ঠান পাওয়া যায়নি</p>`;
}

function filterBazaar(text) {
    renderBazaarList(text);
}

function filterBazaarCategory(cat) {
    renderBazaarList('', cat);
}

function openBazaarDetail(bizId) {
    const item = BAZAAR_LISTINGS.find(b => b.id === bizId) || BAZAAR_LISTINGS[0];
    const cover = document.getElementById('detail-biz-cover');
    const card = document.getElementById('detail-biz-card');

    if (cover) {
        cover.style.background = `linear-gradient(rgba(0,0,0,0.3), rgba(0,0,0,0.5)), url('${item.photoUrl}') center/cover no-repeat`;
    }

    if (card) {
        card.innerHTML = `
            <h2>${item.name}</h2>
            <p class="text-secondary">${item.address} • ${item.distance}</p>
            <div class="halal-status mt-10">
                <i class="fa-solid fa-shield-halal" style="color:var(--emerald); font-size:24px;"></i>
                <div>
                    <strong>${item.tags[0]}</strong>
                    <p class="small-text">${item.desc}</p>
                </div>
            </div>
            <div class="action-buttons mt-15" style="display:flex; gap:10px;">
                <button class="btn btn-outline" style="flex:1;" onclick="alert('যোগাযোগ নম্বর: ${item.phone}')"><i class="fa-solid fa-phone"></i> কল করুন</button>
                <button class="btn btn-primary" style="flex:1;" onclick="window.open('https://www.google.com/maps/search/?api=1&query=${encodeURIComponent(item.name + ' ' + item.address)}', '_blank')"><i class="fa-solid fa-directions"></i> মানচিত্র</button>
            </div>
        `;
    }

    navTo('screen-biz-detail');
}

// App Initialization
document.addEventListener('DOMContentLoaded', () => {
    loadSavedPreferences();

    document.querySelectorAll('[data-lang]').forEach((button) => {
        button.addEventListener('click', () => setLanguage(button.dataset.lang));
    });

    document.querySelectorAll('.toggle').forEach((toggle) => {
        toggle.addEventListener('click', () => toggle.classList.toggle('active'));
    });

    // Wire native settings click on profile setting items
    const profileSettings = document.querySelectorAll('.settings-list .setting-item');
    profileSettings.forEach(item => {
        item.style.cursor = 'pointer';
        item.addEventListener('click', () => openNativeSettings());
    });

    setTimeout(() => {
        const splash = document.getElementById('screen-splash');
        const onboarding = document.getElementById('screen-onboarding');
        if (splash && onboarding && splash.classList.contains('active')) {
            splash.classList.remove('active');
            onboarding.classList.add('active');
        }
    }, 1800);

    // Initial calculation and 1-second ticker loop
    updateRealTimePrayerCalculation();
    setInterval(updateRealTimePrayerCalculation, 1000);

    fetchRealPrayerTimes();
    fetchNearbyMosques();

    window.addEventListener('env-loaded', () => {
        fetchRealPrayerTimes();
        fetchNearbyMosques();
    });

    renderDuasList();
    renderBazaarList();
    openSpecialDay('shab_e_barat', false);
    openNaflDetail('ishraq', false);

    applyTranslations();
});
