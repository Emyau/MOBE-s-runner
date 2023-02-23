package com.ut3.moberunner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SensorManager mng;
    private ChickenView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = new Intent(this, TitleScreenActivity.class);
        view = findViewById(R.id.mainView);
        mng = (SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> sensors = mng.getSensorList(Sensor.TYPE_ALL);
        for(Sensor sensor : sensors){
            Log.i("DEBUG", sensor.getName() + " numéro " + sensor.getType() + " type " + sensor.getStringType());
        }
        mng.registerListener(view, mng.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        startActivity(i, savedInstanceState);
    }

    /*@Override
    protected void onResume() {
        super.onResume();
        mng.registerListener(view, mng.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mng.unregisterListener(view);
    }*/
}