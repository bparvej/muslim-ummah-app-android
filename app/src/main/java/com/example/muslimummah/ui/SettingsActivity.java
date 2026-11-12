package com.example.muslimummah.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.muslimummah.R;
import com.example.muslimummah.config.AppConfig;

public class SettingsActivity extends AppCompatActivity {

    private EditText etLocationName, etLat, etLng, etRadius;
    private RadioGroup rgCalcMethod, rgAzanTone;
    private Button btnSave;
    private AppConfig appConfig;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        appConfig = AppConfig.getInstance(this);

        etLocationName = findViewById(R.id.et_location_name);
        etLat = findViewById(R.id.et_lat);
        etLng = findViewById(R.id.et_lng);
        etRadius = findViewById(R.id.et_radius);
        rgCalcMethod = findViewById(R.id.rg_calc_method);
        rgAzanTone = findViewById(R.id.rg_azan_tone);
        btnSave = findViewById(R.id.btn_save_settings);

        loadCurrentSettings();

        btnSave.setOnClickListener(v -> saveSettings());
    }

    private void loadCurrentSettings() {
        etLocationName.setText(appConfig.getBaseLocationName());
        etLat.setText(String.valueOf(appConfig.getBaseLat()));
        etLng.setText(String.valueOf(appConfig.getBaseLng()));
        etRadius.setText(String.valueOf(appConfig.getSearchRadiusKm()));

        int method = appConfig.getCalcMethod();
        switch (method) {
            case 2: rgCalcMethod.check(R.id.rb_method_2); break;
            case 3: rgCalcMethod.check(R.id.rb_method_3); break;
            case 4: rgCalcMethod.check(R.id.rb_method_4); break;
            case 5: rgCalcMethod.check(R.id.rb_method_5); break;
            default: rgCalcMethod.check(R.id.rb_method_1); break;
        }

        String tone = appConfig.getSelectedAzanTone();
        switch (tone.toLowerCase()) {
            case "makkah": rgAzanTone.check(R.id.rb_tone_makkah); break;
            case "madinah": rgAzanTone.check(R.id.rb_tone_madinah); break;
            case "al-aqsa":
            case "alaqsa": rgAzanTone.check(R.id.rb_tone_alaqsa); break;
            default: rgAzanTone.check(R.id.rb_tone_baitul); break;
        }
    }

    private void saveSettings() {
        try {
            String name = etLocationName.getText().toString().trim();
            double lat = Double.parseDouble(etLat.getText().toString().trim());
            double lng = Double.parseDouble(etLng.getText().toString().trim());
            double radius = Double.parseDouble(etRadius.getText().toString().trim());

            if (!name.isEmpty()) appConfig.setBaseLocationName(name);
            appConfig.setBaseLat(lat);
            appConfig.setBaseLng(lng);
            appConfig.setSearchRadiusKm(radius);

            int selectedMethodId = rgCalcMethod.getCheckedRadioButtonId();
            int method = 1;
            if (selectedMethodId == R.id.rb_method_2) method = 2;
            else if (selectedMethodId == R.id.rb_method_3) method = 3;
            else if (selectedMethodId == R.id.rb_method_4) method = 4;
            else if (selectedMethodId == R.id.rb_method_5) method = 5;
            appConfig.setCalcMethod(method);

            int selectedToneId = rgAzanTone.getCheckedRadioButtonId();
            String tone = "baitul-mukarrom";
            if (selectedToneId == R.id.rb_tone_makkah) tone = "makkah";
            else if (selectedToneId == R.id.rb_tone_madinah) tone = "madinah";
            else if (selectedToneId == R.id.rb_tone_alaqsa) tone = "al-aqsa";
            appConfig.setSelectedAzanTone(tone);

            Toast.makeText(this, "সেটিংস সংরক্ষিত হয়েছে!", Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            Toast.makeText(this, "সঠিক তথ্য প্রদান করুন!", Toast.LENGTH_SHORT).show();
        }
    }
}
