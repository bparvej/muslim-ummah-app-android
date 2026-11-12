package com.example.muslimummah.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.muslimummah.R;
import com.example.muslimummah.config.AppConfig;

public class ProfileFragment extends Fragment {

    private TextView tvLocationVal, tvRadiusVal;
    private AppConfig appConfig;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        appConfig = AppConfig.getInstance(requireContext());

        tvLocationVal = view.findViewById(R.id.tv_profile_location_val);
        tvRadiusVal = view.findViewById(R.id.tv_profile_radius_val);

        view.findViewById(R.id.btn_profile_location).setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), SettingsActivity.class));
        });

        view.findViewById(R.id.btn_profile_alarms).setOnClickListener(v -> {
            AlarmBottomSheetDialogFragment sheet = new AlarmBottomSheetDialogFragment();
            sheet.show(getChildFragmentManager(), "ProfileAlarmSheet");
        });

        view.findViewById(R.id.btn_profile_radius).setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), SettingsActivity.class));
        });

        updateConfigDisplay();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateConfigDisplay();
    }

    private void updateConfigDisplay() {
        if (tvLocationVal != null) {
            tvLocationVal.setText(appConfig.getBaseLocationName() + " >");
        }
        if (tvRadiusVal != null) {
            tvRadiusVal.setText(appConfig.getSearchRadiusKm() + " km >");
        }
    }
}
