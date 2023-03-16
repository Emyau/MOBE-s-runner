package com.ut3.moberunner.actors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.ut3.moberunner.R;

public class Spike extends Actor {

    private final Bitmap bitmap;
    private float speed;
    private float spawnX;
    private float groundLevel;

    private Paint paint;

    public Spike(float speed, float spawnX, float groundLevel, Context context) {
        this.speed = speed;
        // default postion is right of the screen
        this.spawnX = spawnX;
        this.groundLevel = groundLevel;

        height = 100;
        width = 75;
        x = spawnX;
        y = groundLevel - height;

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.spike_pixel);

        paint = new Paint();
        paint.setColor(Color.GREEN);
    }

    private void draw(Canvas canvas) {
        canvas.drawBitmap(Bitmap.createScaledBitmap(bitmap,width+5,height,false), x, y, paint);
    }

    @Override
    public void nextFrame(Canvas canvas) {
        if (x < 0) return;
        x -= speed;
        draw(canvas);
    }


}
