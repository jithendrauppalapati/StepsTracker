package com.example.steptracker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private TextView steps, cal, kilometers, time, currentTime;
    private SensorManager sensorManager;
    private Sensor stepSensor, stepDetector;
    int stepCount = 0, min = 0, stepsWalked = 0, stepDetect = 0;
    float calories = 0f, kMeters = 0f, test;
    SharedPreferences prefs;
    private CardView stepsCard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        steps = findViewById(R.id.steps);
        cal = findViewById(R.id.cal);
        kilometers = findViewById(R.id.nokmText);
        time = findViewById(R.id.time);
        currentTime = findViewById(R.id.currentTime);
        stepsCard = findViewById(R.id.stepsCard);

        stepsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyActivity.class);
                startActivity(intent);
            }
        });


        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){
            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 0);
        }

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
//        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null){
//            stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
//        }
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR) != null){
            stepDetector = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        }

        SharedPreferences myPrefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        stepsWalked = myPrefs.getInt("Steps", 0);
        currentTime.setText("Prevoius Steps:" + stepsWalked);


    }

    @Override
    public void onSensorChanged(SensorEvent event) {

//        Sensor sensor = event.sensor;
//        float[] values = event.values;
//        int value = 0;
//
//        stepCount  = (int) System.currentTimeMillis();
//        stepCount =  ((stepCount / (1000*60*60)) % 24);
//        Log.d("Time", String.valueOf(stepCount));
//        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
//        Date resultTime = new Date(stepCount);
//        Log.d("Time", String.valueOf(resultTime));

//        if (values.length > 0) {
//            value = (int) values[0];
//            value = value - 63250;
//            Log.d("Value", String.valueOf(value));
//            value = 0;
//            if(stepCount == 24) {
//                value = 0;
//            }

//        }

//        if (sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
//            value++;
//            prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
//            SharedPreferences.Editor editor = prefs.edit();
//            editor.putInt("Steps", value);
//            editor.apply();

//            steps.setText(String.valueOf(value));
//            Log.d("Saved", String.valueOf(value));
//        }

        if( event.sensor == stepDetector){
            stepDetect = (int) (stepDetect+event.values[0]);
            steps.setText(String.valueOf(stepDetect));

            prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("Steps", stepDetect);
            editor.apply();
        }

        kMeters = ((float) stepDetect) / 1250 ;
        String kiloFormatted = String.format("%.2f", kMeters);
        kilometers.setText(kiloFormatted);

        calories = kMeters * 55 ;
        String calFormatted = String.format("%.0f", calories);
        cal.setText( calFormatted);

        min = (int) (kMeters * 11);
        time.setText(min + " min");

//        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
//        Date resultdate = new Date(stepCount);
//        System.out.println(sdf.format(resultdate));

//        if(stepCount == 14){
//            value = 0;
//            SharedPreferences myPrefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
//            stepsWalked = myPrefs.getInt("Steps", 0);
//            Log.d("Show", String.valueOf(stepsWalked));
//            steps.setText(""+ value);
//            steps.setText(value);
//        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
//        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null){
//            sensorManager.registerListener(this, stepSensor,SensorManager.SENSOR_DELAY_NORMAL);
//            Toast.makeText(this, "StepTracker has been Activated, you can start walking", Toast.LENGTH_LONG).show();
//        }

        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR) != null){
            sensorManager.registerListener(this, stepDetector, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null){
//            sensorManager.unregisterListener(this, stepSensor);
//            Toast.makeText(this, "StepTracker has been deactivated", Toast.LENGTH_LONG).show();
//        }
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)!=null){
            sensorManager.unregisterListener(this, stepDetector);
        }
    }

}