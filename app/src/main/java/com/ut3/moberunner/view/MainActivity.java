package com.ut3.moberunner.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.Manifest;
import android.os.Bundle;

import com.ut3.moberunner.view.TitleScreenActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = new Intent(this, TitleScreenActivity.class);
        startActivity(i, savedInstanceState);
        //Pour demander l'autorisation manuelle au démarrage
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 200);
    }
}