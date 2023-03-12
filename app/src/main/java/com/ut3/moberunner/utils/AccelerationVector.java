package com.ut3.moberunner.utils;

public class AccelerationVector {
    private float accelXValue = 0;
    private float accelYValue = 0;
    private float accelZValue = 0;

    public AccelerationVector(float accelXValue, float accelYValue, float accelZValue) {
        this.accelXValue = accelXValue;
        this.accelYValue = accelYValue;
        this.accelZValue = accelZValue;
    }

    public float getAccelXValue() {
        return accelXValue;
    }

    public void setAccelXValue(float accelXValue) {
        this.accelXValue = accelXValue;
    }

    public float getAccelYValue() {
        return accelYValue;
    }

    public void setAccelYValue(float accelYValue) {
        this.accelYValue = accelYValue;
    }

    public float getAccelZValue() {
        return accelZValue;
    }

    public void setAccelZValue(float accelZValue) {
        this.accelZValue = accelZValue;
    }
}
