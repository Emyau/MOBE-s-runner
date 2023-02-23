package com.ut3.moberunner.actors;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.Log;

public abstract class Actor {
    // Coordonate
    protected float x, y;
    protected int height, width;

    public Actor(float x, float y, int height, int width) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
    }

    public Actor() {
        this.x = 0;
        this.y = 0;
        this.height = 0;
        this.width = 0;
    }

    public boolean isCollidingWith(Actor actor) {
        RectF myRect = new RectF(x, y, x + width, y + height);
        RectF actorRect = new RectF(actor.x, actor.y, actor.x + actor.width, actor.y + actor.height);
        if (Math.abs(x - actor.x) < 50) {
            Log.d("DEV", "isCollidingWith: proche");
        }
        return myRect.intersect(actorRect);
    }

    public abstract void nextFrame(Canvas canvas);

    public float getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

}
