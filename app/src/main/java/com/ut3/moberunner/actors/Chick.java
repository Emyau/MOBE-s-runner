package com.ut3.moberunner.actors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.ut3.moberunner.R;

public class Chick extends Actor {
    public enum ChickState {
        RUNNING,
        JUMPING,
        DEAD
    }

    private ChickState state;

    // Vertical Velocity
    private Float vVelocity;
    private float groundLevel;

    final private float gravity;
    final private float lift;

    public Paint paint;

    private final int NB_FRAMES_RUNNING = 10;

    private final Bitmap[] framesRunning = new Bitmap[NB_FRAMES_RUNNING];
    private int frameRunningIndex = 0;

    public Chick(float x, Context context) {
        super();
        this.state = ChickState.RUNNING;

        // Dimension
        this.x = x;
        y = 100;
        height = 100;
        width = 100;

        gravity = 3;
        lift = 35;

        paint = new Paint();
        paint.setColor(Color.RED);

        if (getY() < 560) {
            vVelocity = 5.0f;
        }

        framesRunning[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.chick_run_00);
        framesRunning[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.chick_run_01);
        framesRunning[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.chick_run_02);
        framesRunning[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.chick_run_03);
        framesRunning[4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.chick_run_04);
        framesRunning[5] = BitmapFactory.decodeResource(context.getResources(), R.drawable.chick_run_05);
        framesRunning[6] = BitmapFactory.decodeResource(context.getResources(), R.drawable.chick_run_06);
        framesRunning[7] = BitmapFactory.decodeResource(context.getResources(), R.drawable.chick_run_07);
        framesRunning[8] = BitmapFactory.decodeResource(context.getResources(), R.drawable.chick_run_08);
        framesRunning[9] = BitmapFactory.decodeResource(context.getResources(), R.drawable.chick_run_09);

    }


    public boolean jump() {
        if (getGroundHitbox() == groundLevel) {
            this.vVelocity = -this.lift;
            return true;
        }
        return false;
    }

    @Override
    public void nextFrame(Canvas canvas) {

        if (this.vVelocity != null) {
            this.vVelocity += this.gravity;
            setY(getY() + vVelocity);
        }

        // If on the ground stop falling
        if (getGroundHitbox() > groundLevel) {
            this.vVelocity = null;
            // 50 for the size
            y = groundLevel - height;
            setState(ChickState.RUNNING);
        }

        frameRunningIndex++;
        if (frameRunningIndex == 10) {
            frameRunningIndex = 0;
        }

        draw(canvas);
    }

    public void draw(Canvas canvas) {
        Bitmap b = Bitmap.createScaledBitmap(framesRunning[frameRunningIndex], getWidth(), getHeight(), false);
        canvas.drawBitmap(b, x, y, null);
    }

    private float getGroundHitbox() {
        return getY() + height;
    }

    public void setGroundLevel(int groundLevel) {
        this.groundLevel = groundLevel;
    }

    public ChickState getState() {
        return state;
    }

    public void setState(ChickState state) {
        this.state = state;
    }

    @Override
    public void setY(float y) {
        super.setY(y);
    }
}
