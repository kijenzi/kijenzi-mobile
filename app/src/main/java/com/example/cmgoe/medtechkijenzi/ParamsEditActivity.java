package com.example.cmgoe.medtechkijenzi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ParamsEditActivity extends AppCompatActivity {
    private Button generateButton;
    private Button cancelButton;
    private EditText holeDepth, holeDiameter,rackWidth,rackLength,coneHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_params_edit);


        generateButton = (Button) findViewById(R.id.generate_button);
        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateFile();
            }
        });

        cancelButton = (Button) findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });

        holeDepth = (EditText) findViewById(R.id.hole_depth);
        holeDiameter = (EditText) findViewById(R.id.hole_diameter);
        rackWidth = (EditText) findViewById(R.id.rack_width);
        rackLength = (EditText) findViewById(R.id.rack_length);
        coneHeight = (EditText) findViewById(R.id.cone_height);
    }

    private void generateFile(){
        //here the gcode would be generated from the endpoint
        if(holeDepth.getText().toString().isEmpty() ||
                holeDiameter.getText().toString().isEmpty() ||
                rackWidth.getText().toString().isEmpty() ||
                rackLength.getText().toString().isEmpty() ||
                coneHeight.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "All fields must be filled in!", Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent();
            intent.setClass(this, MainActivity.class);
            startActivity(intent);
            Toast.makeText(getApplicationContext(), "Find your generated file in the list.", Toast.LENGTH_LONG).show();
        }


    }

    private void cancel() {
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
    }
}
