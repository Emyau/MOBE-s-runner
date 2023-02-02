package com.ut3.moberunner;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Handler;
import android.view.Display;
import android.view.View;

public class ChickenView extends View {

    int screenWidth, screenHeight, newWidth, newHeight;
    int grassX = 0, frontX = 0;
    int chickenX, chickenY, chickenFrame = 0;
    Bitmap grass, front;
    Bitmap chicken[] = new Bitmap[6];

    Handler handler;
    Runnable runnable;
    final long UPDATE_TIME = 20;

    public ChickenView(Context context) {
        super(context);
        grass = BitmapFactory.decodeResource(getResources(), R.drawable.forest3);
        front = BitmapFactory.decodeResource(getResources(), R.drawable.frontground1);
        chicken[0] = BitmapFactory.decodeResource(getResources(), R.drawable.cat1);
        chicken[1] = BitmapFactory.decodeResource(getResources(), R.drawable.cat2);
        chicken[2] = BitmapFactory.decodeResource(getResources(), R.drawable.cat3);
        chicken[3] = BitmapFactory.decodeResource(getResources(), R.drawable.cat4);
        chicken[4] = BitmapFactory.decodeResource(getResources(), R.drawable.cat5);
        chicken[5] = BitmapFactory.decodeResource(getResources(), R.drawable.cat6);

        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
        float height = grass.getHeight();
        float width = grass.getWidth();
        //float ratio = width / height;
        newWidth = screenWidth;
        newHeight = screenHeight;
        grass = Bitmap.createScaledBitmap(grass, newWidth, newHeight, false);
        front = Bitmap.createScaledBitmap(front, newWidth, newHeight, false);
        chickenX = screenWidth / 2 - 200;
        chickenY = screenHeight - 250;
        handler = new Handler();

        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        grassX -= 10;
        if(grassX < -newWidth) {
            grassX = 0;
        }
        canvas.drawBitmap(grass, grassX, 0, null);
        if(grassX < screenWidth - newWidth) {
            canvas.drawBitmap(grass, grassX+newWidth, 0, null);
        }

        frontX -= 20;
        if(frontX < -newWidth) {
            frontX = 0;
        }
        canvas.drawBitmap(front, frontX, 0, null);
        if(frontX < screenWidth - newWidth) {
            canvas.drawBitmap(front, frontX+newWidth, 0, null);
        }

        chickenFrame++;
        if(chickenFrame == 6) {
            chickenFrame = 0;
        }
        canvas.drawBitmap(chicken[chickenFrame], chickenX, chickenY, null);

        handler.postDelayed(runnable, UPDATE_TIME);
    }
}
