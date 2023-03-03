package com.ut3.moberunner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaRecorder;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.Manifest;

import com.ut3.moberunner.actors.Actor;
import com.ut3.moberunner.actors.Chick;
import com.ut3.moberunner.actors.Fire;
import com.ut3.moberunner.actors.Spike;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

public class ChickenView extends View {

    private final float CHICK_X = 50;
    private Chick chick;
    private LinkedList<Actor> actors = new LinkedList<>();
    private int score = 0;

    private Paint scorePaint;

    private int groundLevel;
    private Handler handler;
    private Runnable runnable;
    private Random random = new Random();

    // 60fps
    final long UPDATE_TIME = 1000 / 60;
    float speed = 10;
    private double audiolevel = 0;

    public ChickenView(Context context) {
        super(context);
        handler = new Handler();
        runnable = this::invalidate;

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        setupGame();
    }

    private void setupGame() {
        chick = new Chick(CHICK_X);
        groundLevel = (int) (getHeight() * 0.8);
        chick.setGroundLevel(groundLevel);

        scorePaint = new Paint();
        scorePaint.setTextAlign(Paint.Align.CENTER);
        scorePaint.setColor(Color.WHITE);
        scorePaint.setTextSize(40);

        startRecording();

    }

    private void startRecording(){

        MediaRecorder recorder = new MediaRecorder();
        recorder.reset();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(this.getContext().getCacheDir().getAbsolutePath() + "/audio.3gp");

        Thread thread = new Thread(() -> {
            try {
                recorder.prepare();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            recorder.start();
            while (true) {
                int amplitude = recorder.getMaxAmplitude();
                if (amplitude > 0) {
                    audiolevel = 20 * Math.log10(amplitude / 32767.0);
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        boolean doSpawnSpike = random.nextInt(100) > 98;
        boolean doSpawnFire = random.nextInt(100) > 98;

        if (chick.getState() != Chick.ChickState.DEAD && doSpawnSpike) {
            //actors.add(new Spike(speed, getWidth(), groundLevel));
        }
        if (chick.getState() != Chick.ChickState.DEAD && doSpawnFire) {
            actors.add(new Fire(speed, getWidth(), groundLevel));
        }

        if (chick.getState() == Chick.ChickState.DEAD) {
            actors.clear();
        }

        drawGround(canvas);
        drawChick(canvas);
        drawDebug(canvas);
        handleActors(canvas);
        updateScore(canvas);

        // This define une FPS of the game
        handler.postDelayed(runnable, UPDATE_TIME);
    }

    private void drawGround(Canvas canvas) {
        Paint p = new Paint();
        p.setColor(Color.RED);
        p.setStrokeWidth(10);
        canvas.drawLine(0, groundLevel, canvas.getWidth(), groundLevel, p);
    }

    private void drawChick(Canvas canvas) {
        chick.nextFrame(canvas);
    }

    private void drawDebug(Canvas canvas) {
        Paint p = new Paint();
        p.setColor(Color.WHITE);
        p.setTextSize(20);
        canvas.drawText("Ground level : " + groundLevel, 50, 50, p);
    }

    private void handleActors(Canvas canvas) {
        actors.forEach(actor -> handleActor(actor, canvas));
    }

    private void handleActor(Actor actor, Canvas canvas) {

        if(actor instanceof  Fire){
            Fire fire = (Fire) actor;
            fire.setState(audiolevel);
            if(fire.getState() == Fire.FireState.EXTINGUISH){
                actor.nextFrame(canvas);
                return;
            }
        }

        actor.nextFrame(canvas);
        if(actor.isCollidingWith(chick)) {
            Log.d("DEV", "Collision");
            chick.paint.setARGB(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
            chick.setState(Chick.ChickState.DEAD);
        }
    }

    private void updateScore(Canvas canvas) {
        if (chick.getState() != Chick.ChickState.DEAD) {
            score += 1;
            canvas.drawText(Integer.toString(score), (float) (getWidth() / 2), (float) (getHeight() * 0.1), scorePaint);
        } else {
            canvas.drawText(Integer.toString(score), (float) (getWidth() / 2), (float) (getHeight() * 0.1), scorePaint);
            canvas.drawText("Game over", (float) (getWidth() / 2), (float) (getHeight() / 2), scorePaint);
        }
    }

    // Feature for blind people we won't use
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            switch (chick.getState()) {
                case DEAD:
                    score = 0;
                    chick.setState(Chick.ChickState.RUNNING);
                    return true;
                case RUNNING:
                    Log.d("DEV", "onTouch: JUMP");
                    chick.jump();
                    return true;
                case JUMPING:
                    return true;
            }
        }
        return false;
    }

}
