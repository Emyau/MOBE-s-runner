package com.ut3.moberunner.sensorhandlers;

import android.content.Context;
import android.media.MediaRecorder;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class MicroHandler {

    private final String AUDIO_FILE;

    private MediaRecorder recorder;
    private AtomicBoolean isRecording;

    private double audioLevel = -11;


    public MicroHandler(Context context) {
        recorder = new MediaRecorder();
        this.AUDIO_FILE = context.getCacheDir().getAbsolutePath() + "/audio.3gp";
        isRecording = new AtomicBoolean(false);
    }

    public void startRecording() {
        if (isRecording.get()) return;

        Thread recordingThread = new Thread(() -> {
            while (isRecording.get()) {
                int amplitude = recorder.getMaxAmplitude();
                if (amplitude > 0) {
                    audioLevel = 20 * Math.log10(amplitude / 32767.0);
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(AUDIO_FILE);

        try {
            recorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        recorder.start();
        isRecording.set(true);
        recordingThread.start();
    }

    public void stopRecording() {
        if (!isRecording.get()) return;
        isRecording.set(false);
        recorder.stop();
        recorder.reset();
    }

    public boolean isRecording() {
        return isRecording.get();
    }

    public double getAudioLevel() {
        return audioLevel;
    }
}
