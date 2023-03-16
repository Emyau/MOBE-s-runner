package com.ut3.moberunner.actors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.ut3.moberunner.R;

import com.ut3.moberunner.utils.AccelerationVector;

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

    private Bitmap rockUP, rockMID;
    private Bitmap toUse;

    public Rock(float speed, float spawnX, float groundLevel, Context context) {
        this.speed = speed;
        // default postion is right of the screen
        this.spawnX = spawnX;
        this.groundLevel = groundLevel;
        this.state = RockState.UP;

        height = 200;
        width = 120;
        x = spawnX;
        y = groundLevel - height;

        rockUP = BitmapFactory.decodeResource(context.getResources(), R.drawable.rock);
        rockMID = BitmapFactory.decodeResource(context.getResources(), R.drawable.little_rock);

        rockUP = Bitmap.createScaledBitmap(rockUP, 120, 120, false);
        rockMID = Bitmap.createScaledBitmap(rockMID, 120, 120, false);

        toUse = rockUP;
    }

    private void draw(Canvas canvas) {
        switch (state) {
            case MID:
                y = groundLevel - height / 2;
                toUse = rockMID;
                break;
            case DOWN:
                x = 0;
                y = groundLevel;
                break;
        }
        canvas.drawBitmap(toUse, x, groundLevel-100, null);
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

    public void setState(AccelerationVector vector) {
        // Small shake check
        if ((vector.getAccelXValue() > 10 && vector.getAccelXValue() < 15) ||
                (vector.getAccelYValue() > 10 && vector.getAccelYValue() < 15) ||
                (vector.getAccelZValue() > 10 && vector.getAccelZValue() < 15)) {
            state = RockState.MID;
        }
        // Big shake check
        if (vector.getAccelXValue() > 15 || vector.getAccelYValue() > 15 || vector.getAccelZValue() > 15) {
            state = RockState.DOWN;
        }
    }
}
