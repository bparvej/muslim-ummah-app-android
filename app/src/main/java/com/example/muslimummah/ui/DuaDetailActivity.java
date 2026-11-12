package com.example.muslimummah.ui;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.muslimummah.R;

public class DuaDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dua_detail);

        String title = getIntent().getStringExtra("dua_title");
        String arabic = getIntent().getStringExtra("dua_arabic");
        String pronunciation = getIntent().getStringExtra("dua_pronunciation");
        String translation = getIntent().getStringExtra("dua_translation");
        String ref = getIntent().getStringExtra("dua_ref");

        TextView tvTitle = findViewById(R.id.tv_detail_dua_title);
        TextView tvArabic = findViewById(R.id.tv_detail_dua_arabic);
        TextView tvPronunciation = findViewById(R.id.tv_detail_dua_pronunciation);
        TextView tvTranslation = findViewById(R.id.tv_detail_dua_translation);
        TextView tvRef = findViewById(R.id.tv_detail_dua_ref);

        if (title != null) tvTitle.setText(title);
        if (arabic != null) tvArabic.setText(arabic);
        if (pronunciation != null) tvPronunciation.setText("বাংলা উচ্চারণ: " + pronunciation);
        if (translation != null) tvTranslation.setText("অর্থ: " + translation);
        if (ref != null) tvRef.setText("উৎস: " + ref);
    }
}
