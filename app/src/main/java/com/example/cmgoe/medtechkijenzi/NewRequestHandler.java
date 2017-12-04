package com.example.cmgoe.medtechkijenzi;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.security.Permission;

//TODO: Make the ImageView look nicer
//TODO: Create a way to post a request to server

public class NewRequestHandler extends AppCompatActivity {

    ImageView objectImage;
    TextView objectTitle;
    TextView objectDescription;
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_request_handler);
        getSupportActionBar().setTitle("Create a request");

        //Handles protecting the URI (necessary in Nougat and beyond)
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA},
                    0);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    0);
        }

        //Find the elements in the view and assign them
        objectImage = (ImageView) findViewById(R.id.add_photo);
        objectTitle = (TextView) findViewById(R.id.object_name);
        objectDescription = (TextView) findViewById(R.id.object_description);
        submitButton = (Button) findViewById(R.id.submit_button);

        //Set up OnClickListener for ImageView
        objectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Launch camera activity, take photo, set new image in existing ImageView
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File imageFile = getImagePath();
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
                startActivityForResult(cameraIntent, 0);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), objectTitle.getText().toString()+" request successfully submitted.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }


    //This method creates a path for the image that is saved when user takes a photo of a component
    private File getImagePath(){
        File folder = new File("sdcard/medtech_kijenzi");

        if(!folder.exists()){
            folder.mkdir();
        }

        File imageFile = new File(folder, "object_image.png");
        if (imageFile.exists()){
            imageFile.delete();
        }
        return imageFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String filePath = "sdcard/medtech_kijenzi/object_image.png";
        objectImage.setImageDrawable(Drawable.createFromPath(filePath));
        objectImage.setRotation(90);
        objectImage.setAdjustViewBounds(false);
        objectImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ViewGroup vg = findViewById(R.id.request_view);
        vg.invalidate();
    }
}
