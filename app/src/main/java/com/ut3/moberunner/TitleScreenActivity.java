package com.ut3.moberunner;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

public class TitleScreenActivity extends AppCompatActivity {

    private boolean isMuted;
    private Context mContext = this;
    private SharedPreferences prefs;
    private ImageView volumeImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_screen);
        prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        volumeImage = (ImageView) findViewById(R.id.volumeIcon);
        volumeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleMute();
            }
        });
        isMuted = prefs.getBoolean("isMuted", false);
        setVolumeIcon(isMuted);

        Button runButton = (Button) findViewById(R.id.runButton);
        runButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRun();
            }
        });
    }

    private void startRun() {
        setContentView(new ChickenView(this));
    }

    private void setVolumeIcon(boolean isMuted) {
        if(isMuted) {
            volumeImage.setImageResource(R.drawable.volume_on);
        } else {
            volumeImage.setImageResource(R.drawable.volume_off);
        }
    }

    private void toggleMute() {
        if(isMuted) {
            unmute();
            isMuted = false;
        } else {
            mute();
            isMuted = true;
        }
        setVolumeIcon(isMuted);
    }

    private void mute() {
        SharedPreferences.Editor editor = prefs.edit(); // get an Editor object
        editor.putBoolean("isMuted", true); // set the mute boolean to true
        // Audio code TBD
    }

    public void unmute() {
        SharedPreferences.Editor editor = prefs.edit(); // get an Editor object
        editor.putBoolean("isMuted", false); // set the mute boolean to true
        // Audio code TBD
    }

}