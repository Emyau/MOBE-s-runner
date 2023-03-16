package com.ut3.moberunner.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ut3.moberunner.activities.views.ChickenView;

public class ChickActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new ChickenView(this));
    }
}