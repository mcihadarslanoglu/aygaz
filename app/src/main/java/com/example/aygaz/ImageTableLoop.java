package com.example.aygaz;


import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class ImageTableLoop extends Thread{
    private static final double MAX_UPS = 30.0;
    private static final double UPS_PERIOD = 1E+3/MAX_UPS;

    private ImageTable imageTable;
    private SurfaceHolder surfaceHolder;

    private boolean isRunning = false;
    private double averageUPS;
    private double averageFPS;
    private Context context;

    public ImageTableLoop(ImageTable imageTable, SurfaceHolder surfaceHolder, Context context) {
        this.imageTable = imageTable;
        this.surfaceHolder = surfaceHolder;
        this.context = context;
    }


    public double getAverageUPS() {
        return averageUPS;
    }

    public double getAverageFPS() {
        return averageFPS;
    }

    public void startLoop() {
        isRunning = true;
        start();
    }

    @Override
    public void run() {
        super.run();

        // Declare time and cycle count variables
        int updateCount = 0;
        int frameCount = 0;

        long startTime;
        long elapsedTime;
        long sleepTime;

        // Game loop
        Canvas canvas = null;
        startTime = System.currentTimeMillis();
        while(isRunning) {

            // Try to update and render game
            try {
                canvas = surfaceHolder.lockCanvas();

                //canvas.setBitmap(BitmapFactory.decodeResource(this.context.getResources(),R.drawable.photo_ico));
                synchronized (surfaceHolder) {
                    imageTable.update();
                    updateCount++;

                    imageTable.draw(canvas);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } finally {
                if(canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                        frameCount++;
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            // Pause game loop to not exceed target UPS
            elapsedTime = System.currentTimeMillis() - startTime;
            sleepTime = (long) (updateCount*UPS_PERIOD - elapsedTime);
            if(sleepTime > 0) {
                try {
                    sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Skip frames to keep up with target UPS
            while(sleepTime < 0 && updateCount < MAX_UPS-1) {
                imageTable.update();
                updateCount++;
                elapsedTime = System.currentTimeMillis() - startTime;
                sleepTime = (long) (updateCount*UPS_PERIOD - elapsedTime);
            }

            // Calculate average UPS and FPS
            elapsedTime = System.currentTimeMillis() - startTime;
            if(elapsedTime >= 1000) {
                averageUPS = updateCount / (1E-3 * elapsedTime);
                averageFPS = frameCount / (1E-3 * elapsedTime);
                updateCount = 0;
                frameCount = 0;
                startTime = System.currentTimeMillis();
            }
        }
    }

}
