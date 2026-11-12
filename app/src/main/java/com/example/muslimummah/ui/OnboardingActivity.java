package com.example.muslimummah.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.muslimummah.R;

public class OnboardingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        Button btnBn = findViewById(R.id.btn_lang_bn);
        Button btnEn = findViewById(R.id.btn_lang_en);
        Button btnContinue = findViewById(R.id.btn_continue_musafir);

        if (btnBn != null) {
            btnBn.setOnClickListener(v -> Toast.makeText(this, "বাংলা ভাষা নির্বাচিত", Toast.LENGTH_SHORT).show());
        }

        if (btnEn != null) {
            btnEn.setOnClickListener(v -> Toast.makeText(this, "English Selected", Toast.LENGTH_SHORT).show());
        }

        if (btnContinue != null) {
            btnContinue.setOnClickListener(v -> {
                Intent intent = new Intent(OnboardingActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            });
        }
    }
}
