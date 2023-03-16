package com.ut3.moberunner.activities.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
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

import com.ut3.moberunner.R;
import com.ut3.moberunner.actorhandlers.ActorGenerator;
import com.ut3.moberunner.actorhandlers.ActorManager;
import com.ut3.moberunner.actors.Chick;
import com.ut3.moberunner.sensorhandlers.MicroHandler;
import com.ut3.moberunner.utils.AccelerationVector;

import java.lang.reflect.Type;

public class ChickenView extends View {

    private Chick chick; // the one and only
    private int score = 0;

    private ActorManager actorManager;
    private ActorGenerator actorGenerator;

    private MicroHandler microHandler;

    private Paint scorePaint;
    private Bitmap background;
    private int backgroundX = 0;

    private int groundLevel;
    private final Handler handler;
    private final Runnable runnable;

    private AccelerationVector accelerationVector;

    private final SensorEventListener listenerAccel = new SensorEventListener() {
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

    private final SensorEventListener listenerGyro = new SensorEventListener() {
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
        float CHICK_X = 50;
        chick = new Chick(CHICK_X, getContext());
        groundLevel = (int) (getHeight() * 0.88);
        chick.setGroundLevel(groundLevel);

        accelerationVector = new AccelerationVector(0, 0, 0);

        actorManager = new ActorManager(chick);
        startGenerator();

        scorePaint = new Paint();
        scorePaint.setTextAlign(Paint.Align.CENTER);
        scorePaint.setColor(Color.WHITE);
        scorePaint.setTextSize(40);
        Typeface typeface = getResources().getFont(R.font.bowlby_one);
        scorePaint.setTypeface(typeface);

        SensorManager sm = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
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
        } else {
            drawChick(canvas);
            drawDebug(canvas);
            actorManager.handleActors(canvas, accelerationVector, microHandler.getAudioLevel(), rotaZ);
        }

        scrollBackground(canvas);
        drawChick(canvas);
        drawDebug(canvas);

        actorManager.handleActors(canvas, accelerationVector, microHandler.getAudioLevel());
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
        int highestScore = prefs.getInt("highestScore", 0);
        if (score > highestScore) {
            System.out.println("New High Score : " + score);
            editor.putInt("highestScore", score);
            editor.apply();
        }
    }
    private void displayBackground() {
        background = BitmapFactory.decodeResource(getResources(), R.drawable.chick_background);
        background = Bitmap.createScaledBitmap(background, getWidth(), getHeight(), false);
    }

    private void scrollBackground(Canvas canvas) {
        backgroundX -= 10;
        if(backgroundX < -getWidth()) {
            backgroundX = 0;
        }
        canvas.drawBitmap(background, backgroundX, 0, null);
        if(backgroundX < getWidth()) {
            canvas.drawBitmap(background, backgroundX + getWidth(), 0, null);
        }
    }

}
