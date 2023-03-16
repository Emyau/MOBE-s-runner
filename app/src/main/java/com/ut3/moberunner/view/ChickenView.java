package com.ut3.moberunner.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;

import com.ut3.moberunner.actorhandlers.ActorGenerator;
import com.ut3.moberunner.actorhandlers.ActorManager;
import com.ut3.moberunner.actors.Chick;
import com.ut3.moberunner.sensorhandlers.MicroHandler;
import com.ut3.moberunner.utils.AccelerationVector;

import java.util.Random;

public class ChickenView extends View {

    private final float CHICK_X = 50;
    private Chick chick; // the one and only
    private int score = 0;

    private ActorManager actorManager;
    private ActorGenerator actorGenerator;

    private MicroHandler microHandler;

    private Paint scorePaint;

    private int groundLevel;
    private Handler handler;
    private Runnable runnable;
    private Random random = new Random();

    private SensorManager sm;
    private AccelerationVector accelerationVector;

    private SensorEventListener listenerAccel = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (accelerationVector == null) {
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

    private SensorEventListener listenerGyro = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            // The acceleration may be negative, so take their absolute value
            rotaZ = event.values[2];
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            //not used
        }
    };

    // 60fps
    final long UPDATE_TIME = 1000 / 60;
    float speed = 10;
    private boolean isRecording;
    private double audioLevel = 0;
    float rotaZ = 0;

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

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == View.VISIBLE) {
            if (microHandler != null) {
                microHandler.startRecording();
            }
        } else {
            microHandler.stopRecording();
        }
    }

    private void setupGame() {
        chick = new Chick(CHICK_X);
        groundLevel = (int) (getHeight() * 0.8);
        chick.setGroundLevel(groundLevel);

        accelerationVector = new AccelerationVector(0, 0, 0);

        actorManager = new ActorManager(chick);
        startGenerator();

        scorePaint = new Paint();
        scorePaint.setTextAlign(Paint.Align.CENTER);
        scorePaint.setColor(Color.WHITE);
        scorePaint.setTextSize(40);

        sm = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        Sensor sensorAccel = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(listenerAccel, sensorAccel, SensorManager.SENSOR_DELAY_NORMAL);
        Sensor sensorGyro = sm.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
        sm.registerListener(listenerGyro, sensorGyro, SensorManager.SENSOR_DELAY_NORMAL);

        microHandler = new MicroHandler(getContext());
        microHandler.startRecording();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        long startTime = System.nanoTime();
        if (chick.getState() == Chick.ChickState.DEAD) {
            gameOver();
        }

        drawGround(canvas);
        drawChick(canvas);
        drawDebug(canvas);

        // TODO: appeler directemnt la méthode de l'actorManager ici
        actorManager.handleActors(canvas, accelerationVector, microHandler.getAudioLevel(), rotaZ);
        updateScore(canvas);

        long stopTime = System.nanoTime();
        long timeElapsed = (stopTime - startTime) / 1000000;
        if (timeElapsed > UPDATE_TIME) {
            Log.d("DEV", "Low framerate !");
            handler.post(runnable);
        } else {
            handler.postDelayed(runnable, UPDATE_TIME - timeElapsed);
        }
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
        saveScore();
    }

    private void restartGame() {
        score = 0;
        chick.setState(Chick.ChickState.RUNNING);
        actorGenerator.setGenerating(true);
    }

    private void saveScore() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        long highestScore = prefs.getLong("highestScore", 0);
        if (score > highestScore) {
            System.out.println("New High Score : " + score);
            editor.putLong("highestScore", score);
            editor.commit();
        }
    }
}
