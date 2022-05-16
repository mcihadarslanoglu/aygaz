package com.example.aygaz;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class Bbox {
    private float x, y,width,height;
    private Paint paint;
    private boolean actionDown = false;
    private boolean delete = false;
    String TAG = "Bbox";

    public Bbox(int x,int y,int width,int height){


        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.paint = new Paint();
        this.paint.setColor(Color.rgb(0,255,0));
        this.paint.setStrokeWidth(10);
        this.paint.setStyle(Paint.Style.STROKE);
    }

    public void draw(Canvas canvas){
        canvas.drawRect(this.x,this.y,this.x + this.width,this.y + this.height,this.paint);
        //Log.i(TAG,"Bbox drawn");

    }
    public void setActionDown(boolean actionDown){
        this.actionDown = actionDown;
    }
    public boolean getActionDown(){
        return this.actionDown;
    }
    public void setPosition(float  x, float y){
        this.x = x-this.width;
        this.y = y-this.height;
    }

    public boolean isTouched(float x, float y){
        //x = x -80;
        //y = y-80;
        Log.i(TAG,"Object positionx is "+ this.x+ "Objext positiony is "+ this.y + " Cursor positionx is "+x + "Cursor positiony is " +y);
        return (x > this.x && x < this.x + width) && (y > this.y && y < this.y + height);
    }

    public void setDelete(boolean isDelete){
        if(isDelete){
            this.paint.setColor(Color.rgb(255,0,0));
            this.delete = true;
        }else{
            this.paint.setColor(Color.rgb(0,255,0));
            this.delete = false;
        }

    }

    public boolean getDelete(){
        return this.delete;
    }
}
