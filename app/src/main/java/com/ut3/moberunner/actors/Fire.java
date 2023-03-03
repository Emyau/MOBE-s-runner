package com.ut3.moberunner.actors;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Fire extends Actor {

    public enum FireState {
        BURNING,
        EXTINGUISH
    }

    private float audioLevel = 0;
    private FireState state;


    private final float speed;
    private final float spawnX;
    private final float groundLevel;
    private final Paint paint;

    public Fire(float speed, float spawnX, float groundLevel) {
        this.speed = speed;
        // default postion is right of the screen
        this.spawnX = spawnX;
        this.groundLevel = groundLevel;
        this.state = FireState.BURNING;

        height = 200;
        width = 20;
        x = spawnX;
        y = groundLevel-height;

        paint = new Paint();
        paint.setColor(Color.RED);
    }

    private void draw(Canvas canvas) {
        if( state == FireState.EXTINGUISH ) {
                paint.setColor(Color.BLUE);
        }

        canvas.drawRect(x, y, x + width, groundLevel, paint);
        Paint p = new Paint();
        p.setColor(Color.WHITE);
        canvas.drawText("Y = " + y + " X = " + x, x, y, p);
    }

    public FireState getState() {
        return this.state;
    }

    public void setState(double level) {
        System.out.println("audioLevel"+level);
        if(level >= 0){
            state = FireState.EXTINGUISH;
        } else {
            float hue = (float) ( (level + 50) * 2);
            paint.setColor(Color.HSVToColor(new float[]{hue, 1f, 1f}));
        }
    }

    @Override
    public void nextFrame(Canvas canvas) {
        if (x < 0) return;
        x -= speed;
        draw(canvas);
    }
}
