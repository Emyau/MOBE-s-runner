package com.ut3.moberunner.sensorhandlers;

import android.content.Context;
import android.media.MediaRecorder;

import java.io.IOException;

public class MicroHandler {

    private final String AUDIO_FILE;

    private MediaRecorder recorder;
    private boolean isRecording = false;

    private double audioLevel = -11;

    private final Thread recordingThread = new Thread(() -> {
        while (true) {
            int amplitude = recorder.getMaxAmplitude();
            if (amplitude > 0) {
                audioLevel = 20 * Math.log10(amplitude / 32767.0);
            }
        }
    });

    public MicroHandler(Context context) {
        recorder = new MediaRecorder();
        this.AUDIO_FILE = context.getCacheDir().getAbsolutePath() + "/audio.3gp";
    }

    public void startRecording() {
        if (isRecording) return;
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
        isRecording = true;
        recordingThread.start();
    }

    public void stopRecording() {
        if (!isRecording) return;
        recorder.stop();
        recorder.reset();
        isRecording = false;
        recordingThread.interrupt();
    }

    public boolean isRecording() {
        return isRecording;
    }

    public double getAudioLevel() {
        return audioLevel;
    }
}
