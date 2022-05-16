package com.example.aygaz;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

public class CustomSurface extends SurfaceView implements SurfaceHolder.Callback {
    public CustomSurface(Context context,SurfaceView surface) {
        super(context);
        SurfaceHolder surfaceHolder = surface.getHolder();
        surfaceHolder.addCallback(this);

        setFocusable(true);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        Log.i("CustomSurface","CustomSurface created");

        Log.i("ImageTable","Surface created");

        Rect rect = new Rect(300,300,100,100);
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        Canvas canvas = surfaceHolder.lockCanvas();
        canvas.drawRect(rect,paint);
        //this.draw(canvas);
        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }
}
