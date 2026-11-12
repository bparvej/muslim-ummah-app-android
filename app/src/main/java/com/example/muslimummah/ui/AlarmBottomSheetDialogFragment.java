package com.example.muslimummah.ui;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;

import com.example.muslimummah.R;
import com.example.muslimummah.alarm.AzanScheduler;
import com.example.muslimummah.config.AppConfig;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AlarmBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private SwitchCompat swFajr, swDhuhr, swAsr, swMaghrib, swIsha;
    private RadioGroup rgAzanTone;
    private Button btnTestSound, btnSave;
    private AppConfig appConfig;
    private AzanScheduler azanScheduler;
    private MediaPlayer previewPlayer;
    private Handler autoStopHandler;
    private Runnable autoStopRunnable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_alarm_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        appConfig = AppConfig.getInstance(requireContext());
        azanScheduler = new AzanScheduler(requireContext());

        swFajr = view.findViewById(R.id.sw_fajr);
        swDhuhr = view.findViewById(R.id.sw_dhuhr);
        swAsr = view.findViewById(R.id.sw_asr);
        swMaghrib = view.findViewById(R.id.sw_maghrib);
        swIsha = view.findViewById(R.id.sw_isha);
        rgAzanTone = view.findViewById(R.id.rg_sheet_azan_tone);
        btnTestSound = view.findViewById(R.id.btn_test_azan_sound);
        btnSave = view.findViewById(R.id.btn_save_sheet_alarms);

        view.findViewById(R.id.btn_close_alarm_sheet).setOnClickListener(v -> dismiss());

        loadCurrentTone();

        btnTestSound.setOnClickListener(v -> playTestAudio());
        btnSave.setOnClickListener(v -> saveAlarms());
    }

    private void loadCurrentTone() {
        String tone = appConfig.getSelectedAzanTone();
        switch (tone.toLowerCase()) {
            case "makkah": rgAzanTone.check(R.id.rb_sheet_makkah); break;
            case "madinah": rgAzanTone.check(R.id.rb_sheet_madinah); break;
            case "al-aqsa":
            case "alaqsa": rgAzanTone.check(R.id.rb_sheet_alaqsa); break;
            default: rgAzanTone.check(R.id.rb_sheet_baitul); break;
        }
    }

    private String getSelectedToneKey() {
        int checkedId = rgAzanTone.getCheckedRadioButtonId();
        if (checkedId == R.id.rb_sheet_makkah) return "makkah";
        if (checkedId == R.id.rb_sheet_madinah) return "madinah";
        if (checkedId == R.id.rb_sheet_alaqsa) return "al-aqsa";
        return "baitul-mukarrom";
    }

    private void playTestAudio() {
        stopAudio();
        String toneKey = getSelectedToneKey();

        int resId;
        switch (toneKey.toLowerCase()) {
            case "makkah": resId = R.raw.makkah; break;
            case "madinah": resId = R.raw.madinah; break;
            case "al-aqsa":
            case "alaqsa": resId = R.raw.al_aqsa; break;
            default: resId = R.raw.baitul_mukarrom; break;
        }

        try {
            previewPlayer = MediaPlayer.create(requireContext(), resId);
            if (previewPlayer != null) {
                previewPlayer.setAudioAttributes(
                        new AudioAttributes.Builder()
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .setUsage(AudioAttributes.USAGE_ALARM)
                                .build()
                );
                previewPlayer.setOnCompletionListener(mp -> stopAudio());
                previewPlayer.start();

                // Limit test audio preview playback to 10 seconds max
                autoStopHandler = new Handler(Looper.getMainLooper());
                autoStopRunnable = this::stopAudio;
                autoStopHandler.postDelayed(autoStopRunnable, 10000L);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveAlarms() {
        String toneKey = getSelectedToneKey();
        appConfig.setSelectedAzanTone(toneKey);

        if (swFajr.isChecked()) azanScheduler.scheduleAlarm("Fajr", "04:52", toneKey);
        else azanScheduler.cancelAlarm("Fajr");

        if (swDhuhr.isChecked()) azanScheduler.scheduleAlarm("Dhuhr", "11:42", toneKey);
        else azanScheduler.cancelAlarm("Dhuhr");

        if (swAsr.isChecked()) azanScheduler.scheduleAlarm("Asr", "14:53", toneKey);
        else azanScheduler.cancelAlarm("Asr");

        if (swMaghrib.isChecked()) azanScheduler.scheduleAlarm("Maghrib", "17:15", toneKey);
        else azanScheduler.cancelAlarm("Maghrib");

        if (swIsha.isChecked()) azanScheduler.scheduleAlarm("Isha", "18:32", toneKey);
        else azanScheduler.cancelAlarm("Isha");

        Toast.makeText(requireContext(), "অ্যালার্ম ও আজানের সুর সংরক্ষিত হয়েছে!", Toast.LENGTH_SHORT).show();
        dismiss();
    }

    private void stopAudio() {
        if (autoStopHandler != null && autoStopRunnable != null) {
            autoStopHandler.removeCallbacks(autoStopRunnable);
        }
        if (previewPlayer != null) {
            try {
                if (previewPlayer.isPlaying()) previewPlayer.stop();
            } catch (Exception ignored) {}
            previewPlayer.release();
            previewPlayer = null;
        }
    }

    @Override
    public void onDestroyView() {
        stopAudio();
        super.onDestroyView();
    }
}
