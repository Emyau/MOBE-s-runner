package com.ut3.moberunner;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class TitleScreenActivity extends AppCompatActivity {

    private boolean isMuted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageView volumeImage = findViewById(R.id.volumeIcon);
        volumeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleMute(v);
            }
        });
        setContentView(R.layout.activity_title_screen);
    }

    private void toggleMute(View v) {
        ImageView iv = (ImageView) v;
        if(isMuted) {
            unmute();
            isMuted = false;
            iv.setImageResource(R.drawable.volume_on);
        } else {
            mute();
            isMuted = true;
            iv.setImageResource(R.drawable.volume_off);

        }
    }

    private void mute() {
        //mute audio
        AudioManager amanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        amanager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);
    }

    public void unmute() {
        //unmute audio
        AudioManager amanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        amanager.setStreamMute(AudioManager.STREAM_NOTIFICATION, false);
    }

}