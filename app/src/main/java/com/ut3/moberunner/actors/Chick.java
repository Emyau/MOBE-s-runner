package com.ut3.moberunner.actors;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

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

    public Chick(float x) {
        super();
        this.state = ChickState.RUNNING;

        // Dimension
        this.x = x;
        y = 100;
        height = 50;
        width = 50;

        gravity = 3;
        lift = 35;

        paint = new Paint();
        paint.setColor(Color.RED);

        if (getY() < 560) {
            vVelocity = 5.0f;
        }
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

        draw(canvas);
    }

    public void draw(Canvas canvas) {
        canvas.drawRect(x, y, x + width, y + height, paint);

        Paint p = new Paint();
        p.setColor(Color.WHITE);
        canvas.drawText("Y = " + getY() + " V = " + vVelocity, getX(), getY(), p);
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
