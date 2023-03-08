package com.ut3.moberunner.actors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;

import com.ut3.moberunner.R;

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
    private Bitmap frames;
    private final int nbFrames = 7;
    private int frameIndex = 0;

    public Fire(float speed, float spawnX, float groundLevel, Context context) {
        this.speed = speed;
        // default postion is right of the screen
        this.spawnX = spawnX;
        this.groundLevel = groundLevel;
        this.state = FireState.BURNING;

        height = 200;
        width = 20;
        x = spawnX;
        y = groundLevel-height;

        frames = BitmapFactory.decodeResource(context.getResources(), R.drawable.fire_sheet);

        paint = new Paint();
        paint.setColor(Color.RED);
    }

    private void draw(Canvas canvas) {
        if( state == FireState.EXTINGUISH ) {
            paint.setColorFilter(null);
            paint.setColor(Color.BLUE);
            canvas.drawRect(x, y, x + width, groundLevel, paint);
        } else {
            int frameWidth = frames.getHeight();
            int frameHeight = frames.getHeight();
            Bitmap fireFrame = Bitmap.createBitmap(frames, frameIndex * frameWidth, 0, frameWidth, frameHeight);
            frameIndex = (frameIndex + 1) % nbFrames;

            canvas.drawBitmap(fireFrame, x, groundLevel - frameHeight, paint);
        }
        Paint p = new Paint();
        p.setColor(Color.WHITE);
        canvas.drawText("Y = " + y + " X = " + x, x, y, p);
    }

    public FireState getState() {
        return this.state;
    }

    public void setState(double level) {
        if(level >= 0){
            state = FireState.EXTINGUISH;
        } else {
            float hue = (float) ( (level + 50) * 2);
            paint.setColorFilter(new LightingColorFilter(Color.HSVToColor(new float[]{hue, 1f, 1f}),1));
        }
    }

    @Override
    public void nextFrame(Canvas canvas) {
        if (x < 0) return;
        x -= speed;
        draw(canvas);
    }
}
