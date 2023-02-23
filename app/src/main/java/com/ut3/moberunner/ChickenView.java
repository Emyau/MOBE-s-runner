package com.ut3.moberunner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.ut3.moberunner.actors.Chick;

public class ChickenView extends View {

    private Chick chick;

    int groundLevel;
    Handler handler;
    Runnable runnable;

    // 60fps
    final long UPDATE_TIME = 17;

    public ChickenView(Context context) {
        super(context);
        setupGame();

        handler = new Handler();
        runnable = this::invalidate;

    }

    private void setupGame() {
        this.chick = new Chick();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        groundLevel = (int) (getHeight() * 0.8);
        chick.setGroundLevel(groundLevel);

        drawGround(canvas);
        drawChick(canvas);
        drawDebug(canvas);

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
        canvas.drawText("Groud level : " + groundLevel, 50, 50, p);
    }

    // Feature for blind people we won't use
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            Log.d("DEV", "onTouch: JUMP");
            chick.jump();
            return true;
        }
        return false;
    }
}
