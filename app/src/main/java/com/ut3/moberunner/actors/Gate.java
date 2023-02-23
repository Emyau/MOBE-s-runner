package com.ut3.moberunner.actors;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Gate extends Actor {

    private Paint paint;
    private float speed;
    private float spawnX;
    private float groundLevel;
    private float x1, y1;

    public Gate(float speed, float spawnX, float groundLevel) {
        this.speed = speed;
        // default postion is right of the screen
        this.spawnX = spawnX;
        this.groundLevel = groundLevel;

        height = 100;
        width = 10;
        x1 = spawnX;
        x = spawnX;
        y1 = groundLevel;
        y = groundLevel-height;

        paint = new Paint();
        paint.setColor(Color.YELLOW);
        paint.setStrokeWidth(width);
    }

    private void draw(Canvas canvas) {
        canvas.drawLine(x, y, x1, y1, paint);
    }

    @Override
    public void nextFrame(Canvas canvas) {
        if (x1 < 0) return;
        x1 -= speed;
        x -= speed;
        draw(canvas);
    }

    public void setState(float rotaZ) {
        x = x1 + rotaZ*300;
        y = (float) -(Math.sqrt(100*100 - (x1-x)*(x1-x))) + y1;
        if (y <0 ) {
            y = groundLevel;
        }
    }
}
