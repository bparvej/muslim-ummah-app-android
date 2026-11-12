// Environment Configuration for Muslim Ummah (Browser & Android WebView)
window.APP_CONFIG = {
    APP_NAME: "Muslim Ummah",
    BASE_LAT: 23.693639,
    BASE_LNG: 90.450333,
    BASE_LOCATION_NAME: "যাত্রাবাড়ী / দনিয়া, ঢাকা",
    MOSQUE_SEARCH_RADIUS_KM: 1.0,
    PRAYER_CALC_METHOD: 1, // Karachi / Islamic Foundation BD compatible
    PRAYER_API_BASE_URL: "https://api.aladhan.com/v1",
    OSM_NOMINATIM_API_URL: "https://nominatim.openstreetmap.org/search",
    OSM_OVERPASS_API_URL: "https://overpass-api.de/api/interpreter"
};

// Check if Android Native Bridge is available to override configuration
(function loadAndroidNativeConfig() {
    if (window.AndroidBridge && typeof window.AndroidBridge.getAppConfig === 'function') {
        try {
            const rawConfig = window.AndroidBridge.getAppConfig();
            if (rawConfig) {
                const nativeCfg = JSON.parse(rawConfig);
                if (nativeCfg.BASE_LAT) window.APP_CONFIG.BASE_LAT = parseFloat(nativeCfg.BASE_LAT);
                if (nativeCfg.BASE_LNG) window.APP_CONFIG.BASE_LNG = parseFloat(nativeCfg.BASE_LNG);
                if (nativeCfg.MOSQUE_SEARCH_RADIUS_KM) window.APP_CONFIG.MOSQUE_SEARCH_RADIUS_KM = parseFloat(nativeCfg.MOSQUE_SEARCH_RADIUS_KM);
                if (nativeCfg.BASE_LOCATION_NAME) window.APP_CONFIG.BASE_LOCATION_NAME = nativeCfg.BASE_LOCATION_NAME;
                if (nativeCfg.PRAYER_CALC_METHOD) window.APP_CONFIG.PRAYER_CALC_METHOD = parseInt(nativeCfg.PRAYER_CALC_METHOD, 10);
                console.log('[Muslim Ummah] Native Android configuration attached:', window.APP_CONFIG);
            }
        } catch (e) {
            console.warn('[Muslim Ummah] Failed to parse AndroidBridge config:', e);
        }
    }
})();

// Attempt to fetch and parse .env file dynamically if served via HTTP/HTTPS
(async function loadDotEnv() {
    try {
        const response = await fetch('.env');
        if (response.ok) {
            const text = await response.text();
            const lines = text.split('\n');
            lines.forEach(line => {
                const trimmed = line.trim();
                if (trimmed && !trimmed.startsWith('#')) {
                    const eqIndex = trimmed.indexOf('=');
                    if (eqIndex !== -1) {
                        const key = trimmed.slice(0, eqIndex).trim();
                        const val = trimmed.slice(eqIndex + 1).trim();
                        if (key === 'BASE_LAT') window.APP_CONFIG.BASE_LAT = parseFloat(val);
                        else if (key === 'BASE_LNG') window.APP_CONFIG.BASE_LNG = parseFloat(val);
                        else if (key === 'MOSQUE_SEARCH_RADIUS_KM') window.APP_CONFIG.MOSQUE_SEARCH_RADIUS_KM = parseFloat(val);
                        else if (key === 'BASE_LOCATION_NAME') window.APP_CONFIG.BASE_LOCATION_NAME = val;
                        else if (key === 'PRAYER_CALC_METHOD') window.APP_CONFIG.PRAYER_CALC_METHOD = parseInt(val, 10);
                        else window.APP_CONFIG[key] = val;
                    }
                }
            });
            console.log('[Muslim Ummah] .env configuration loaded successfully:', window.APP_CONFIG);
            window.dispatchEvent(new CustomEvent('env-loaded', { detail: window.APP_CONFIG }));
        }
    } catch (e) {
        console.warn('[Muslim Ummah] Running without dynamic .env fetch:', e);
    }
})();
