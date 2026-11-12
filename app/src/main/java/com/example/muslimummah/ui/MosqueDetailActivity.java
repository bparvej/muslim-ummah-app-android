package com.example.muslimummah.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.muslimummah.R;

public class MosqueDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mosque_detail);

        String name = getIntent().getStringExtra("mosque_name");
        String address = getIntent().getStringExtra("mosque_address");
        double lat = getIntent().getDoubleExtra("mosque_lat", 23.693639);
        double lon = getIntent().getDoubleExtra("mosque_lon", 90.450333);

        TextView tvName = findViewById(R.id.tv_detail_name);
        TextView tvAddress = findViewById(R.id.tv_detail_address);
        Button btnDirections = findViewById(R.id.btn_directions);

        if (name != null) tvName.setText(name);
        if (address != null) tvAddress.setText("📍 " + address);

        btnDirections.setOnClickListener(v -> {
            String uriStr = "https://www.google.com/maps/dir/?api=1&destination=" + lat + "," + lon;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uriStr));
            startActivity(intent);
        });
    }
}
