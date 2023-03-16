package com.ut3.moberunner.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ut3.moberunner.R;

public class TitleScreenActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private Long highestScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_screen);
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        highestScore = prefs.getLong("highestScore", 0);

        Button runButton = (Button) findViewById(R.id.runButton);
        runButton.setOnClickListener(v -> startRun());

        TextView highScoreText = (TextView) findViewById(R.id.highScoreText);
        highScoreText.setText(highestScore.toString());
    }

    private void startRun() {
        setContentView(new ChickenView(this));
    }

}