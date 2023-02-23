package com.ut3.moberunner.actors;

public class Actor {
    // Coordonate
    private float x, y;

    // Dimmension
    private int height, width;

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

    public void addY(int z) {
        this.y += z;
    }

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
