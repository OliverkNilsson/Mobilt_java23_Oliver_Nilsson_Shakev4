package com.example.mobilt_java23_oliver_nilsson_shakev4;

import android.os.Bundle;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private ProgressBar progressBarX, progressBarY, progressBarZ;

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor gyroscope;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Hämta referenser till ProgressBar komponenterna
        progressBarX = findViewById(R.id.progressBarX);
        progressBarY = findViewById(R.id.progressBarY);
        progressBarZ = findViewById(R.id.progressBarZ);

        // Fixar sensorerna
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        // Kollar så att där finns värde sensorerna
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }

        if (gyroscope != null) {
            sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            // Uppdaterar värdet till progressbar
            progressBarX.setProgress((int) Math.abs(x));
            progressBarY.setProgress((int) Math.abs(y));
            progressBarZ.setProgress((int) Math.abs(z));

            if (Math.abs(x) > 10 || Math.abs(y) > 10 || Math.abs(z) > 10) {
                Toast.makeText(this, "Device shaking!", Toast.LENGTH_SHORT).show();
            }
        }

        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            double rotation = Math.sqrt(Math.pow(event.values[0], 2) +
                    Math.pow(event.values[1], 2) +
                    Math.pow(event.values[2], 2));

            if (rotation > 2) {
                Toast.makeText(this, "SPINNING!!!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Tom för nu, men kan användas för att hantera förändringar i sensors noggrannhet.
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
    }
}