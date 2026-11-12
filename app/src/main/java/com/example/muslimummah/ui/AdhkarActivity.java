package com.example.muslimummah.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.muslimummah.R;

public class AdhkarActivity extends AppCompatActivity {

    private int count1 = 0;
    private int count2 = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adhkar);

        Button btn1 = findViewById(R.id.btn_count_1);
        TextView tvStatus1 = findViewById(R.id.tv_status_1);

        Button btn2 = findViewById(R.id.btn_count_2);
        TextView tvStatus2 = findViewById(R.id.tv_status_2);

        if (btn1 != null) {
            btn1.setOnClickListener(v -> {
                count1 = (count1 < 10) ? count1 + 1 : 0;
                btn1.setText("অবস্থান: " + count1 + "/10 (জিকির করুন)");
                if (tvStatus1 != null) {
                    if (count1 >= 10) {
                        tvStatus1.setText("সমাপ্ত ✓");
                        tvStatus1.setBackgroundColor(0x33075B45);
                    } else {
                        tvStatus1.setText("অসমাপ্ত");
                        tvStatus1.setBackgroundColor(0x22D6B66D);
                    }
                }
            });
        }

        if (btn2 != null) {
            btn2.setOnClickListener(v -> {
                count2 = (count2 < 10) ? count2 + 1 : 0;
                btn2.setText("অবস্থান: " + count2 + "/10 (জিকির করুন)");
                if (tvStatus2 != null) {
                    if (count2 >= 10) {
                        tvStatus2.setText("সমাপ্ত ✓");
                        tvStatus2.setBackgroundColor(0x33075B45);
                    } else {
                        tvStatus2.setText("অসমাপ্ত");
                        tvStatus2.setBackgroundColor(0x22D6B66D);
                    }
                }
            });
        }
    }
}
