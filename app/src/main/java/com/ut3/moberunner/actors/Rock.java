package com.ut3.moberunner.actors;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Rock extends Actor {

    public enum RockState {
        UP,
        MID,
         DOWN
    }

    private RockState state;
    private Paint paint;

    private float speed;
    private float spawnX;
    private float groundLevel;

    public Rock(float speed, float spawnX, float groundLevel) {
        this.speed = speed;
        // default postion is right of the screen
        this.spawnX = spawnX;
        this.groundLevel = groundLevel;
        this.state = RockState.UP;

        height = 100;
        width = 20;
        x = spawnX;
        y = groundLevel-height;

        paint = new Paint();
        paint.setColor(Color.GRAY);
    }

    private void draw(Canvas canvas) {
        //gestion des états ici
        if (state == RockState.MID) {
            y = groundLevel - height/2;
        }
        if (state == RockState.DOWN) {
            x = 0;
            y = groundLevel;
        }
        canvas.drawOval(x, y, x + width, groundLevel, paint);
        //Paint p = new Paint();
        //p.setColor(Color.WHITE);
        //canvas.drawText("Y = " + y + " X = " + x, x, y, p);
    }

    @Override
    public void nextFrame(Canvas canvas) {
        if (x < 0) return;
        x -= speed;
        draw(canvas);
    }

    public RockState getState() {
        return this.state;
    }

    public void setState(float accelXValue, float accelYValue, float accelZValue) {
        if ((accelXValue > 10 && accelXValue < 15) || (accelYValue > 10 && accelYValue < 15) || (accelZValue > 10 && accelZValue < 15) ) {
            state = RockState.MID;
        }
        if (accelXValue > 15 || accelYValue > 15 || accelZValue > 15) {
            state = RockState.DOWN;
        }
    }
}
