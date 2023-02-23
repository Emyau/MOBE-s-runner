package com.ut3.moberunner.actors;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Chick extends Actor {
    public enum ChickState {
        RUNNING,
        JUMPING
    }

    private ChickState state;
    private float groundLevel;

    // Vertical Velocity
    private Float vVelocity;
    private float gravity;
    // Lift size
    private float lift;

    public Chick() {
        super();
        this.state = ChickState.RUNNING;
        super.setY(100);
        super.setX(50);
        gravity = 1;
        lift = 20;

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

    public void nextFrame(Canvas canvas) {

        if (this.vVelocity != null) {
            this.vVelocity += this.gravity;
            setY(getY() + vVelocity);
        }

        // If on the ground stop falling
        if (getGroundHitbox() > groundLevel) {
            this.vVelocity = null;
            // 50 for the size
            this.setY(groundLevel - 50);
        }

        draw(canvas);
    }

    public void draw(Canvas canvas) {
        Paint p = new Paint();
        p.setColor(Color.RED);
        canvas.drawRect(getX(), getY() + 50, getX() + 50, getY(), p);
        p.setColor(Color.WHITE);
        canvas.drawText("Y = " + getY() + " V = " + vVelocity, getX(), getY(), p);
    }

    private float getGroundHitbox() {
        return getY() + 50;
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
