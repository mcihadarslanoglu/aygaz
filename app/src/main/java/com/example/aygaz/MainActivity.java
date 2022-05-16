package com.example.aygaz;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.canhub.cropper.CropImageView;
import com.google.android.material.internal.ParcelableSparseArray;

import org.jetbrains.annotations.Contract;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {
    ImageView image1;
    ImageView image2;
    CropImageView cropImageView;
    ImageView currentImage;
    final String TAG = "MainActivity";

    JSONObject croppedImages = new JSONObject();
    Button submit;
    Button setImage;
    final int CAMERA_REQUEST = 1;
    final int GALLERY_IMAGE_REQUEST = 2;
    public int  currentImageID = -1;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        cropImageView = findViewById(R.id.cropImageView);

        image1.setOnClickListener(loadImage);
        image2.setOnClickListener(loadImage);

        setImage = findViewById(R.id.setImage);
        submit = findViewById(R.id.submit);

        submit.setOnClickListener(submitFunc);


        try {
            croppedImages.put(String.valueOf(R.id.image1),null);
            croppedImages.put(String.valueOf(R.id.image2),null);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    View.OnClickListener loadImage = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Intent takePicture = new Intent(Intent.ACTION_GET_CONTENT);
            takePicture.setType("image/*");
            currentImageID = view.getId();
            setImage.setOnClickListener(setImageFunc);

            //takePicture.setAction(Intent.ACTION_GET_CONTENT);
            //takePicture.putExtra("imageID", 22);
            //takePicture.putExtra("test","testValue");
            Log.i("test","onClick imageID is "+view.getId());
            //loadImageResult.launch(takePicture);
            startActivityForResult(takePicture,GALLERY_IMAGE_REQUEST);
        }
    };

    View.OnClickListener submitFunc = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent SubmitActivity = new Intent(MainActivity.this, SubmitActivity.class);
            Log.i(TAG,"Intent is created");


            try {
                SubmitActivity.putExtra("image1",(Bitmap)croppedImages.get(String.valueOf(R.id.image1)));
                Log.i(TAG,"Image1 added");
                SubmitActivity.putExtra("image2",(Bitmap)croppedImages.get(String.valueOf(R.id.image2)));
                Log.i(TAG,"Image2 added");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i(TAG,"Intent started");
            startActivity(SubmitActivity);
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case CAMERA_REQUEST:
                currentImageID = data.getIntExtra("imageID",R.id.image1);
                Log.i("test","image id is "+currentImageID);
                if(currentImageID != 0){
                    ImageView tmp = findViewById((int)currentImageID);
                    Log.i("test","bitmap is " + data.getExtras().get("data").toString());
                    tmp.setImageBitmap((Bitmap) data.getExtras().get("data"));
                }
                break;

            case GALLERY_IMAGE_REQUEST:

                Bitmap image = BitmapFactory.decodeResource(context.getResources(),R.drawable.photo_ico);
                currentImage = findViewById(currentImageID);
                Log.i("test","image id is "+currentImageID);

                try {
                    InputStream imageTMP = context.getContentResolver().openInputStream(data.getData());
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(imageTMP);
                    image = BitmapFactory.decodeStream(bufferedInputStream);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


                currentImage.setImageBitmap(image);
                cropImageView.setImageBitmap(image);
                break;
        }
    }

    View.OnClickListener setImageFunc = new View.OnClickListener(){
            @Override
            public void onClick(View view){
                try {
                    croppedImages.put(String.valueOf(currentImageID),cropImageView.getCroppedImage());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i(TAG,croppedImages.toString());
                Log.i(TAG, "cropped height is "+String.valueOf(cropImageView.getCroppedImage().getHeight()));
                Log.i(TAG, "before crop height is "+currentImage.getHeight());

    }
    };
    /*ActivityResultLauncher<Intent> loadImageResult = registerForActivityResult(new ActivityResultContracts.TakePicture(),
            new ActivityResultCallback<ActivityResultContracts.TakePicture>() {
                @Override
                public void onActivityResult(ActivityResultContracts.TakePicture takePicture) {
                    Log.i("Test", "a");

                    ImageView tmp = findViewById(currentImage);
                    tmp.setImageURI();


                }


            });
*/

}