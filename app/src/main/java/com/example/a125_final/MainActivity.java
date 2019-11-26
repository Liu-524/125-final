package com.example.a125_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity  implements SensorEventListener {
    /**acclerationmeter with gravity. */
    private Sensor accm;
    /**acceleration sensor.*/
    private Sensor acc;
    /**rotation sensor.*/
    private  Sensor gra;
    /**sensor manager.*/
    private SensorManager sManager;
    /**start button.*/
    private Button start;
    /**the phone is in air.*/
    private boolean inAir;
    /**acceleration threshold.*/
    private final double accTh = 6.0;
    /**score: rotation count.*/
    private double score = 0;
    /**is reading the first orientation.*/
    private boolean isFirst;
    /**temp value storing the last rotational position.*/
    private float[] last = new float[3];

    private TextView test;
    /**create game scene.
     *
     * @param savedInstanceState unused :(
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gra = sManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        acc = sManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        accm = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        setContentView(R.layout.activity_main);
        test = findViewById(R.id.score);
        start = findViewById(R.id.start_button);
        start.setOnClickListener(v -> {
            score = 0;
            isFirst = true;
            start.setVisibility(View.GONE);
            test.setText("test");
            sManager.registerListener(this, acc, SensorManager.SENSOR_DELAY_GAME);
            sManager.registerListener(this, gra, SensorManager.SENSOR_DELAY_GAME);
            /*
            try {
                if (!Thread.interrupted()) {
                    Thread.sleep(20000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!inAir) {
                sManager.unregisterListener(this);
                start.setVisibility(View.VISIBLE);
            }
            */

        });

    }

    /**
     * unable sensors to save power.
     */
    @Override
    protected void onPause() {
        super.onPause();
        test.setText("pause");
        sManager.unregisterListener(this);
    }

    /**
     * same as previous.
     */
    @Override
    protected void onStop() {
        super.onStop();
        test.setText("stop");
        sManager.unregisterListener(this);
    }

    /**
     * make start button visible to re-enable sensors.
     */
    @Override
    protected void onResume() {
        super.onResume();
        test.setText("resume");
        start.setVisibility(View.VISIBLE);
    }

    /**
     * unused.
     * @param sensor :(
     * @param accuracy :(
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }

    /**
     * either detect in-air device or count the rotations.
     * @param event the sensor event with data
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        int type = event.sensor.getType();
        if (type == Sensor.TYPE_LINEAR_ACCELERATION) {
            float[] accVal = event.values;
            if (!inAir) {
                if (Math.sqrt(accVal[0] * accVal[0]
                        + accVal[1] * accVal[1]
                        + accVal[2] * accVal[2]) >= accTh) {
                    inAir = true;
                    sManager.unregisterListener(this, acc);
                    sManager.registerListener(this, accm, SensorManager.SENSOR_DELAY_GAME);
                    test.setText("inair");
                }
            } else {
                if (Math.sqrt(accVal[0] * accVal[0]
                        + accVal[1] * accVal[1]
                        + accVal[2] * accVal[2]) >= accTh) {
                    inAir = false;
                    test.setText("not-in-air");
                    test.setText(Long.toString(Math.round(score / 6)));
                    start.setVisibility(View.VISIBLE);
                    sManager.unregisterListener(this);
                }
            }
        } else if (type == Sensor.TYPE_GRAVITY) {
            if (!inAir) {
                return;
            }
            float[] graVal = denoise(event.values);
            if (isFirst) {
                for (float a : graVal) {
                    if (a == 0) {
                        return;
                    }
                }
                last = graVal.clone();
                isFirst = false;
                return;
            }
            for (int i = 0; i < graVal.length; i++) {
                if (last[i] * graVal[i] < 0) {
                    score++;
                    if (graVal[i] != 0) {
                        last[i] = graVal[i];
                    }
                }
            }


            /*
            intent.putExtra("rotation", rotation);
            startActivity(intent);
            */
        } else if (type == Sensor.TYPE_ACCELEROMETER) {
            float[] accVal = event.values;
            double acceleration = Math.sqrt(accVal[0] * accVal[0] + accVal[1] * accVal[1] + accVal[2] * accVal[2]);
            if (acceleration > 9.3 && acceleration < 10) {
                sManager.registerListener(this, acc, SensorManager.SENSOR_DELAY_GAME);
                sManager.unregisterListener(this, accm);
            }
        }
    }

    /**
     * de-noise  sensor data to prevent extra score count.
     * @param input float to de-noise
     * @return a array of same length where values should be
     */
    private float[] denoise(float[] input) {
        for (float x : input) {
            x = Math.round(x * 10) / 10;
        }
        return input;
    }
}
