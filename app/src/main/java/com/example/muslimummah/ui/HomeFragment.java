package com.example.muslimummah.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.muslimummah.R;
import com.example.muslimummah.config.AppConfig;
import com.example.muslimummah.models.MakruhStatus;
import com.example.muslimummah.models.PrayerTimings;
import com.example.muslimummah.repository.PrayerRepository;
import com.example.muslimummah.utils.MakruhDetector;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private TextView tvLocation, tvGregorianDate, tvStatusPill, tvCurrentWaqtTitle, tvCountdown, tvNextPrayerInfo;
    private TextView tvFajr, tvDhuhr, tvAsr, tvMaghrib, tvIsha, tvImsak, tvIftar;
    private LinearLayout cardNextPrayer;
    private ImageView btnSettings;

    private AppConfig appConfig;
    private PrayerRepository prayerRepository;
    private PrayerTimings currentTimings;
    private Handler tickerHandler;
    private Runnable tickerRunnable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        appConfig = AppConfig.getInstance(requireContext());
        prayerRepository = new PrayerRepository(requireContext());

        tvLocation = view.findViewById(R.id.tv_home_location);
        tvGregorianDate = view.findViewById(R.id.tv_gregorian_date);
        tvStatusPill = view.findViewById(R.id.tv_status_pill);
        tvCurrentWaqtTitle = view.findViewById(R.id.tv_current_waqt_title);
        tvCountdown = view.findViewById(R.id.tv_countdown);
        tvNextPrayerInfo = view.findViewById(R.id.tv_next_prayer_info);
        cardNextPrayer = view.findViewById(R.id.card_next_prayer);
        btnSettings = view.findViewById(R.id.btn_settings_icon);

        tvFajr = view.findViewById(R.id.tv_time_fajr);
        tvDhuhr = view.findViewById(R.id.tv_time_dhuhr);
        tvAsr = view.findViewById(R.id.tv_time_asr);
        tvMaghrib = view.findViewById(R.id.tv_time_maghrib);
        tvIsha = view.findViewById(R.id.tv_time_isha);
        tvImsak = view.findViewById(R.id.tv_time_imsak);
        tvIftar = view.findViewById(R.id.tv_time_iftar);

        view.findViewById(R.id.btn_action_dua).setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), DuaLibraryActivity.class));
        });

        view.findViewById(R.id.btn_action_settings).setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), SettingsActivity.class));
        });

        if (btnSettings != null) {
            btnSettings.setOnClickListener(v -> startActivity(new Intent(requireContext(), SettingsActivity.class)));
        }

        updateUIConfig();
        loadPrayerTimings();
        startRealTimeTicker();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUIConfig();
        loadPrayerTimings();
    }

    private void updateUIConfig() {
        tvLocation.setText("📍 " + appConfig.getBaseLocationName());
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, d MMMM yyyy", Locale.forLanguageTag("bn-BD"));
        tvGregorianDate.setText(sdf.format(new Date()));
    }

    private void loadPrayerTimings() {
        prayerRepository.fetchPrayerTimings(new PrayerRepository.PrayerCallback() {
            @Override
            public void onSuccess(PrayerTimings timings) {
                currentTimings = timings;
                if (isAdded()) {
                    tvFajr.setText(timings.getFajr());
                    tvDhuhr.setText(timings.getDhuhr());
                    tvAsr.setText(timings.getAsr());
                    tvMaghrib.setText(timings.getMaghrib());
                    tvIsha.setText(timings.getIsha());
                    tvImsak.setText(timings.getImsak() + " AM");
                    tvIftar.setText(timings.getMaghrib() + " PM");
                    updateCountdownAndMakruhStatus();
                }
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void startRealTimeTicker() {
        tickerHandler = new Handler(Looper.getMainLooper());
        tickerRunnable = new Runnable() {
            @Override
            public void run() {
                updateCountdownAndMakruhStatus();
                tickerHandler.postDelayed(this, 1000);
            }
        };
        tickerHandler.post(tickerRunnable);
    }

    private void updateCountdownAndMakruhStatus() {
        if (currentTimings == null) return;

        Calendar now = Calendar.getInstance();
        MakruhStatus makruhStatus = MakruhDetector.checkMakruhStatus(currentTimings, now);

        if (makruhStatus.isMakruh()) {
            // Apply RED warning gradient style
            GradientDrawable redGradient = new GradientDrawable(
                    GradientDrawable.Orientation.TL_BR,
                    new int[]{Color.parseColor("#A72323"), Color.parseColor("#D93838")}
            );
            redGradient.setCornerRadius(30f);
            cardNextPrayer.setBackground(redGradient);

            tvStatusPill.setText("⚠️ " + makruhStatus.getReason());
            tvCurrentWaqtTitle.setText("নিষিদ্ধ সময় • নামাজ পড়া নিষেধ");
            tvNextPrayerInfo.setText("বাকি প্রায় " + makruhStatus.getRemainingMinutes() + " মিনিট");
        } else {
            // Standard Emerald Green gradient
            GradientDrawable greenGradient = new GradientDrawable(
                    GradientDrawable.Orientation.TL_BR,
                    new int[]{Color.parseColor("#075B45"), Color.parseColor("#0E7C63")}
            );
            greenGradient.setCornerRadius(30f);
            cardNextPrayer.setBackground(greenGradient);

            tvStatusPill.setText("✓ ওয়াক্ত চলমান");
            tvCurrentWaqtTitle.setText("যোহর ওয়াক্ত শুরু হয়েছে");
            tvNextPrayerInfo.setText("পরবর্তী আসর শুরু: " + currentTimings.getAsr() + " PM");
        }

        // Ticker countdown
        int secondsRemaining = 60 - now.get(Calendar.SECOND);
        int minsRemaining = 59 - now.get(Calendar.MINUTE);
        tvCountdown.setText(String.format(Locale.getDefault(), "00:%02d:%02d", minsRemaining, secondsRemaining));
    }

    @Override
    public void onDestroyView() {
        if (tickerHandler != null && tickerRunnable != null) {
            tickerHandler.removeCallbacks(tickerRunnable);
        }
        super.onDestroyView();
    }
}
