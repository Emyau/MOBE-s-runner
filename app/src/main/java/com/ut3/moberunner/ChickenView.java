package com.ut3.moberunner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaRecorder;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.Manifest;

import com.ut3.moberunner.actors.Chick;
import com.ut3.moberunner.actors.Fire;
import com.ut3.moberunner.actors.Spike;
import com.ut3.moberunner.utils.AccelerationVector;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class ChickenView extends View {

    private final float CHICK_X = 50;
    private Chick chick; // the one and only
    private int score = 0;

    private ActorManager actorManager;
    private ActorGenerator actorGenerator;

    private Paint scorePaint;

    private int groundLevel;
    private Handler handler;
    private Runnable runnable;
    private Random random = new Random();

    private SensorManager sm;
    private AccelerationVector accelerationVector;

    private SensorEventListener listener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if(accelerationVector == null) {
                accelerationVector = new AccelerationVector(Math.abs(event.values[0]),
                        Math.abs(event.values[1]),
                        Math.abs(event.values[2]));
            } else {
                accelerationVector.setAccelXValue(Math.abs(event.values[0]));
                accelerationVector.setAccelYValue(Math.abs(event.values[1]));
                accelerationVector.setAccelZValue(Math.abs(event.values[2]));
            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            //not used
        }
    };

    // 60fps
    final long UPDATE_TIME = 1000 / 60;
    float speed = 10;
    private double audioLevel = 0;

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

        accelerationVector = new AccelerationVector(0,0,0);

        actorManager = new ActorManager(chick);
        startGenerator();

        scorePaint = new Paint();
        scorePaint.setTextAlign(Paint.Align.CENTER);
        scorePaint.setColor(Color.WHITE);
        scorePaint.setTextSize(40);

        sm = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL);

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
                    audioLevel = 20 * Math.log10(amplitude / 32767.0);
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
        if (chick.getState() != Chick.ChickState.DEAD && doSpawnRock ) {
            actors.add(new Rock(speed, getWidth(), groundLevel));
            actorList.add(new Spike(speed, getWidth(), groundLevel));
        }
        if (chick.getState() != Chick.ChickState.DEAD && doSpawnFire) {
            actors.add(new Fire(speed, getWidth(), groundLevel, getContext()));
        }

        if (chick.getState() == Chick.ChickState.DEAD) {
            gameOver();
        }

        drawGround(canvas);
        drawChick(canvas);
        drawDebug(canvas);
        handleActors(canvas, accelerationVector);
        updateScore(canvas);

        // This define the FPS of the game
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
        canvas.drawText("Ground level : " + groundLevel + " Chick State : " + chick.getState(), 50, 50, p);
    }

    private void handleActors(Canvas canvas) {
        actorList.forEach(actor -> handleActor(actor, canvas));
    }

    private void handleActor(Actor actor, Canvas canvas) {

        if(actor instanceof  Fire){
            Fire fire = (Fire) actor;
            fire.setState(audioLevel);
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
        actorManager.handleActor(actor, canvas, chick);
        actorManager.handleActors(canvas);
    private void handleActors(Canvas canvas, AccelerationVector accelerationVector) {
        actorManager.handleActors(canvas, accelerationVector);
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
                    restartGame();
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

    private void startGenerator() {
        actorGenerator = new ActorGenerator(actorManager, this);
        new Thread(actorGenerator).start();
    }

    private void gameOver() {
        actorGenerator.setGenerating(false);
        actorManager.clearActors();
    }

    private void restartGame() {
        score = 0;
        chick.setState(Chick.ChickState.RUNNING);
        actorGenerator.setGenerating(true);
    }
}
