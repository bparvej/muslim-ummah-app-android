package com.example.muslimummah.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.muslimummah.R;

public class IbadatFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ibadat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.btn_ibadat_dua).setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), DuaLibraryActivity.class));
        });

        view.findViewById(R.id.btn_ibadat_settings).setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), SettingsActivity.class));
        });

        view.findViewById(R.id.btn_ibadat_restricted).setOnClickListener(v -> {
            Toast.makeText(requireContext(), "সূর্যোদয়, দ্বিপ্রহর (জাওয়াল) ও সূর্যাস্তের সময় নামাজ আদায় করা নিষেধ।", Toast.LENGTH_LONG).show();
        });

        view.findViewById(R.id.btn_ibadat_fasting).setOnClickListener(v -> {
            Toast.makeText(requireContext(), "আজকের সেহরির শেষ সময় ইমসাক এবং ইফতারের সময় মাগরিব।", Toast.LENGTH_SHORT).show();
        });
    }
}
