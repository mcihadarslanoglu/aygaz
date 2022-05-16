package com.example.aygaz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SubmitActivity extends AppCompatActivity {
    Context context;

    Button submitButton;
    Button deleteCylinder;
    Button addCylinderButton;

    //ImageView tmp;
    ImageView image1;
    ImageView image2;
    ImageView cylindersImage;

    TextView cylinderCount;

    Intent intent;

    Bitmap image1Content;
    Bitmap image2Content;

    String TAG;

    SurfaceView test;

    SurfaceHolder surfaceHolder;
    ImageTable imageTable;
    Bbox[] bboxes;
    ArrayList<Bbox> bboxesArrayLists = new ArrayList<Bbox>();
    ArrayList<Bbox> image1BboxesArrayLists = new ArrayList<Bbox>();
    ArrayList<Bbox> image2BboxesArrayLists = new ArrayList<Bbox>();
    JSONArray rectCoordinates;

    JSONObject images;
    ImageContainer currentImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);
        test = findViewById(R.id.cylindersImage);
        context = this;
        //setContentView(new ImageTable(this, test, image1Content));
        TAG = "SubmitActivity";
        surfaceHolder = test.getHolder();

        rectCoordinates = new JSONArray();
        images = new JSONObject();
        /*JSONObject coord1 = new JSONObject();
        JSONObject coord2 = new JSONObject();
        JSONObject coord3 = new JSONObject();


        try {
            coord1.put("left",0);
            coord1.put("top",0);
            coord1.put("right",100);
            coord1.put("bottom",100);

            coord2.put("left",125);
            coord2.put("top",125);
            coord2.put("right",225);
            coord2.put("bottom",225);

            coord3.put("left",250);
            coord3.put("top",250);
            coord3.put("right",350);
            coord3.put("bottom",350);

            rectCoordinates.put(coord1);
            rectCoordinates.put(coord2);
            rectCoordinates.put(coord3);

        }catch (Exception exception){
            exception.printStackTrace();
        }*/




        image1 = findViewById(R.id.image1);
        image2  = findViewById(R.id.image2);


        image1Content = BitmapFactory.decodeResource(this.getResources(),R.drawable.cylinder);
        image1BboxesArrayLists.add(new Bbox(0,0,100,100));
        image1BboxesArrayLists.add(new Bbox(250,250,100,100));
        //image1BboxesArrayLists.add(new Bbox(350,350,100,100));
        image2BboxesArrayLists.add(new Bbox(125,125,100,100));
        image2BboxesArrayLists.add(new Bbox(450,450,100,100));
        //image2BboxesArrayLists.add(new Bbox(100,100,100,100));
        image2Content = BitmapFactory.decodeResource(this.getResources(),R.drawable.cylinder2);

        try {
            images.put(String.valueOf(R.id.image1),new ImageContainer(image1Content,image1BboxesArrayLists));
            images.put(String.valueOf(R.id.image2),new ImageContainer(image2Content,image2BboxesArrayLists));
        } catch (JSONException e) {
            e.printStackTrace();
        }



        imageTable = new ImageTable(this,test,image2Content);



        //bboxesArrayLists.add(new Bbox(0,0,100,100));
        //bboxesArrayLists.add(new Bbox(125,125,100,100));


        //intent = getIntent();
        Log.i(TAG,"Entered SubmitActivity");
        //image1Content = (Bitmap) intent.getParcelableExtra("image1");
        //image2Content = (Bitmap) intent.getParcelableExtra("image2");
        //Log.i(TAG,"image 1 is "+ image1Content);
        //image1Content = BitmapFactory.decodeResource(this.getResources(),R.drawable.photo_ico);
        //image2Content = BitmapFactory.decodeResource(this.getResources(),R.drawable.photo_ico);
        //cylindersImage = BitmapFactory.decodeResource(this.getResources(),R.drawable.photo_ico);


        deleteCylinder = findViewById(R.id.deleteCylinder);
        deleteCylinder.setOnClickListener(deleteCylinderFunc);

        cylinderCount = findViewById(R.id.cylinderCount);
        cylinderCount.setText(String.valueOf(bboxesArrayLists.size()));

        //cylindersImage = findViewById(R.id.cylindersImage);


        image1.setImageBitmap(image1Content);
        image2.setImageBitmap(image2Content);

        image1.setOnClickListener(loadImage);
        image2.setOnClickListener(loadImage);


    }

    View.OnClickListener loadImage = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            try {
                currentImage = (ImageContainer) images.get(String.valueOf(view.getId()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //ImageView tmp = (ImageView)findViewById(view.getId());
            //BitmapDrawable bgImageDrawable = (BitmapDrawable)tmp.getDrawable();
            //Bitmap bgImage  = bgImageDrawable.getBitmap();
            Log.i(TAG,"Background of the clicked item is " + images.toString());
            imageTable.setBgImage(currentImage.getImage());
            cylinderCount.setText(String.valueOf(currentImage.getBboxArrayList().size()));
            imageTable.setBoxes(currentImage.getBboxArrayList());
        }
    };

    View.OnClickListener deleteCylinderFunc = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            bboxesArrayLists = currentImage.getBboxArrayList();
            for (int i = 0; i<bboxesArrayLists.size();i++){
                if (bboxesArrayLists.get(i).getDelete()){
                    bboxesArrayLists.remove(i);
                    Log.i(TAG," element deleted " + String.valueOf(i));
                    Log.i(TAG," bboxesArrayList is  " + bboxesArrayLists.toString());

                }
            }
            currentImage.setBboxArrayList(bboxesArrayLists);
            cylinderCount.setText(String.valueOf(bboxesArrayLists.size()));
        }
    };

}