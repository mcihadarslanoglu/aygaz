package com.example.aygaz;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Game manages all objects in the game and is responsible for updating all states and render all
 * objects to the screen
 */
class ImageTable extends SurfaceView implements SurfaceHolder.Callback,View.OnTouchListener {
    //public OnTouchListener onTouchEvent;
    private ImageTableLoop imageTableLoop;
    private Context context;
    private Bitmap bgImage;
    private SurfaceHolder surfaceHolder;
    private Bbox bbox;
    private ArrayList<Bbox> BboxesList = new ArrayList<Bbox>();
    private boolean longPress;
    String TAG = "ImageTable";
    /*public final View.OnTouchListener onTouchEvent = new View.OnTouchListener(new View.OnTouchListener(){
        @Override
        public boolean onTouch(View view, MotionEvent event){

            Log.i(TAG,String.valueOf( event.getAction())+"asd");
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    for (Bbox bbox : BboxesList) {
                        if (bbox.isTouched(event.getX(), event.getY())) {
                            bbox.setActionDown(true);
                            Log.i(TAG,"An object is touched");
                        }


                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    for (Bbox bbox : BboxesList) {
                        if (bbox.getActionDown()) {
                            bbox.setPosition(event.getX(), event.getY());
                        }
                    }
                    break;

                case MotionEvent.ACTION_UP:
                    for (Bbox bbox : BboxesList) {
                        if (bbox.getActionDown()) {
                            bbox.setActionDown(false);

                        }
                    }
                    break;
            }


        }

    });*/
    public ImageTable(Context context, SurfaceView surfaceView, Bitmap bgImage) {
        super(context);

        // Get surface holder and add callback
       // surfaceView.setOnTouchListener(this.onTouchEvent);
        surfaceView.setOnTouchListener(this);

        surfaceHolder =  surfaceView.getHolder();//getHolder();//
        surfaceHolder.addCallback(this);

        this.bgImage = bgImage;

        this.context = context;

        imageTableLoop = new ImageTableLoop(this, surfaceHolder, context);





        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i("ImageTable","Surface created");
        imageTableLoop.startLoop();
        Rect rect = new Rect(300,300,100,100);
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        Canvas canvas = holder.lockCanvas();
        canvas.drawRect(rect,paint);

        holder.unlockCanvasAndPost(canvas);


    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        //canvas.drawBitmap(BitmapFactory.decodeResource(this.context.getResources(),R.drawable.photo_ico), null,new Rect(0,0,100,100),null);
        drawUPS(canvas);
        drawFPS(canvas);
        drawBG(canvas);
        drawBboxes(canvas);
    }

    public void drawUPS(Canvas canvas) {
        String averageUPS = Double.toString(imageTableLoop.getAverageUPS());
        Paint paint = new Paint();
        int color = Color.BLUE;
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("UPS: " + averageUPS, 100, 100, paint);
    }

    public void drawFPS(Canvas canvas) {
        String averageFPS = Double.toString(imageTableLoop.getAverageFPS());
        Paint paint = new Paint();
        int color = Color.BLUE;
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("FPS: " + averageFPS, 100, 200, paint);
    }

    public void drawBG(Canvas canvas){

        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                surfaceHolder.setFixedSize(bgImage.getWidth(),bgImage.getHeight());

            }
        });

        canvas.drawBitmap(this.bgImage, null,new Rect(0,0,this.bgImage.getWidth(),this.bgImage.getHeight()),null);
    }

    public void setBgImage(Bitmap bgImage){
        this.bgImage = bgImage;
    }
    public void setBboxes(Bbox[] bboxes) {
        Log.i(TAG,"Boxes set" + bboxes[0]);
        for (Bbox bbox: bboxes) {
            BboxesList.add(bbox);
            Log.i(TAG,"Box is "+ bbox);
        }
    }
    public void setBoxes(ArrayList bboxes){
        this.BboxesList = bboxes;
    }


    public void drawBboxes(Canvas canvas){

        for (Bbox bbox: this.BboxesList) {
            bbox.draw(canvas);

        }

    }
    public void update() {
        // Update game state
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                for (Bbox bbox : this.BboxesList) {
                    this.longPress = true;
                    Log.i(TAG,"left is " + view.getLeft() + " top is " + view.getTop());
                    Log.i(TAG,"Eventx is " + event.getX()*0.75 + " Eventy is "+event.getY()*0.75);
                    if (bbox.isTouched((float)(event.getX()*this.bgImage.getWidth()/view.getWidth()), (float)(event.getY()*this.bgImage.getHeight()/view.getHeight()))) {
                        bbox.setActionDown(true);
                        Log.i(TAG,"An object is touched");
                        break;
                    }


                }
                break;
            case MotionEvent.ACTION_MOVE:
                this.longPress = false;
                for (Bbox bbox : this.BboxesList) {
                    if (bbox.getActionDown()) {
                        bbox.setPosition((float)(event.getX()*this.bgImage.getWidth()/view.getWidth()), (float)(event.getY()*this.bgImage.getHeight()/view.getHeight()));
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                for (Bbox bbox : this.BboxesList) {
                    if (bbox.getActionDown() && !this.longPress) {
                        bbox.setActionDown(false);
                        break;

                    }
                    if (bbox.getActionDown() && this.longPress){
                        if (bbox.getDelete()){
                            bbox.setDelete(false);

                            bbox.setActionDown(false);
                        }else{
                            bbox.setDelete(true);

                            bbox.setActionDown(false);
                        }
                        this.longPress = false;
                        break;
                    }

                }
                break;
        }

        return true;
    }
}