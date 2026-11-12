package com.example.muslimummah

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.muslimummah.alarm.AzanScheduler
import com.example.muslimummah.bridge.WebAppInterface
import com.example.muslimummah.data.AppPreferences
import com.example.muslimummah.data.MosqueRepository
import com.example.muslimummah.data.PrayerRepository

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var preferences: AppPreferences
    private lateinit var prayerRepository: PrayerRepository
    private lateinit var mosqueRepository: MosqueRepository
    private lateinit var azanScheduler: AzanScheduler
    private lateinit var webAppInterface: WebAppInterface

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        preferences = AppPreferences(this)
        prayerRepository = PrayerRepository(preferences)
        mosqueRepository = MosqueRepository(preferences)
        azanScheduler = AzanScheduler(this)

        webAppInterface = WebAppInterface(
            context = this,
            preferences = preferences,
            prayerRepository = prayerRepository,
            mosqueRepository = mosqueRepository,
            azanScheduler = azanScheduler
        )

        webView = findViewById(R.id.webView)

        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            allowFileAccess = true
            allowContentAccess = true
            databaseEnabled = true
            useWideViewPort = true
            loadWithOverviewMode = true
            cacheMode = WebSettings.LOAD_DEFAULT
        }

        webView.webChromeClient = WebChromeClient()
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                // Evaluate initial config sync into window.APP_CONFIG
                val configJson = preferences.getConfigAsJson()
                webView.evaluateJavascript(
                    """
                    if (window.APP_CONFIG) {
                        try {
                            const cfg = $configJson;
                            window.APP_CONFIG.BASE_LAT = cfg.BASE_LAT;
                            window.APP_CONFIG.BASE_LNG = cfg.BASE_LNG;
                            window.APP_CONFIG.MOSQUE_SEARCH_RADIUS_KM = cfg.MOSQUE_SEARCH_RADIUS_KM;
                            window.APP_CONFIG.BASE_LOCATION_NAME = cfg.BASE_LOCATION_NAME;
                            window.APP_CONFIG.PRAYER_CALC_METHOD = cfg.PRAYER_CALC_METHOD;
                            window.dispatchEvent(new CustomEvent('env-loaded', { detail: window.APP_CONFIG }));
                        } catch(e) {}
                    }
                    """.trimIndent(), null
                )
            }
        }

        webView.addJavascriptInterface(webAppInterface, "AndroidBridge")
        webView.loadUrl("file:///android_asset/index.html")

        checkAndRequestPermissions()
    }

    override fun onResume() {
        super.onResume()
        if (::webView.isInitialized) {
            val configJson = preferences.getConfigAsJson()
            webView.evaluateJavascript(
                """
                if (window.APP_CONFIG) {
                    try {
                        const cfg = $configJson;
                        window.APP_CONFIG.BASE_LAT = cfg.BASE_LAT;
                        window.APP_CONFIG.BASE_LNG = cfg.BASE_LNG;
                        window.APP_CONFIG.MOSQUE_SEARCH_RADIUS_KM = cfg.MOSQUE_SEARCH_RADIUS_KM;
                        window.APP_CONFIG.BASE_LOCATION_NAME = cfg.BASE_LOCATION_NAME;
                        window.APP_CONFIG.PRAYER_CALC_METHOD = cfg.PRAYER_CALC_METHOD;
                        if (typeof fetchRealPrayerTimes === 'function') fetchRealPrayerTimes();
                        if (typeof fetchNearbyMosques === 'function') fetchNearbyMosques();
                    } catch(e) {}
                }
                """.trimIndent(), null
            )
        }
    }

    private fun checkAndRequestPermissions() {
        val permissionsToRequest = mutableListOf<String>()

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toTypedArray(), PERMISSION_REQUEST_CODE)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (::webView.isInitialized && webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 101
    }
}
