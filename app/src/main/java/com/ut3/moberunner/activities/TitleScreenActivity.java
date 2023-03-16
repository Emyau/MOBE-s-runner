package com.ut3.moberunner.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.ut3.moberunner.R;

public class TitleScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_screen);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 200);

        Button runButton = findViewById(R.id.runButton);
        runButton.setOnClickListener(v -> start());
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateHighScore();
    }

    private void updateHighScore() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        long highestScore = prefs.getInt("highestScore", 0);
        TextView highScoreTextView = findViewById(R.id.highScoreText);
        highScoreTextView.setText(Long.toString(highestScore));
    }

    private void start() {
        Intent intent = new Intent(this, ChickActivity.class);
        startActivity(intent);
    }

}