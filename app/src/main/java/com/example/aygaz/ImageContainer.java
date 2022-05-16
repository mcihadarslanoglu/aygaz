package com.example.aygaz;

import android.graphics.Bitmap;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ImageContainer {
    private Bitmap image;
    private ArrayList<Bbox> bboxArrayList;
    public ImageContainer(Bitmap image, ArrayList<Bbox> bboxArrayList){
        this.image = image;
        this.bboxArrayList = bboxArrayList;
    }

    public Bitmap getImage() {
        return this.image;
    }

    public ArrayList<Bbox> getBboxArrayList() {
        return this.bboxArrayList;
    }

    public void setBboxArrayList(ArrayList<Bbox> bboxArrayList) {
        this.bboxArrayList = bboxArrayList;
    }
}
