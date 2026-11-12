package com.example.muslimummah.ui;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.muslimummah.R;

public class QiblaActivity extends AppCompatActivity implements SensorEventListener {

    private ImageView ivCompassDial;
    private TextView tvDegree;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor magnetometer;

    private float[] gravity;
    private float[] geomagnetic;
    private float currentDegree = 0f;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qibla);

        ivCompassDial = findViewById(R.id.iv_compass_dial);
        tvDegree = findViewById(R.id.tv_qibla_degree);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sensorManager != null) {
            if (accelerometer != null) sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
            if (magnetometer != null) sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_GAME);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            gravity = event.values.clone();
        }
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            geomagnetic = event.values.clone();
        }

        if (gravity != null && geomagnetic != null) {
            float[] R_mat = new float[9];
            float[] I_mat = new float[9];
            boolean success = SensorManager.getRotationMatrix(R_mat, I_mat, gravity, geomagnetic);
            if (success) {
                float[] orientation = new float[3];
                SensorManager.getOrientation(R_mat, orientation);
                float azimuth = (float) Math.toDegrees(orientation[0]);
                azimuth = (azimuth + 360) % 360;

                // Qibla direction from Dhaka is approx 278 degrees WNW
                float targetQibla = 278f;
                float degree = targetQibla - azimuth;

                RotateAnimation ra = new RotateAnimation(
                        currentDegree,
                        degree,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f
                );
                ra.setDuration(250);
                ra.setFillAfter(true);

                if (ivCompassDial != null) {
                    ivCompassDial.startAnimation(ra);
                }
                currentDegree = degree;

                if (tvDegree != null) {
                    tvDegree.setText("কম্পাস ডিগ্রি: " + Math.round(azimuth) + "° (কিবলা: 278°)");
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}
