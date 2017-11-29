package com.example.cmgoe.medtechkijenzi;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.storage.StorageMetadata;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    String fileInfoString;
    StorageMetadata theMetadata = null;
    Button connectButton;
    Button printButton;
    private FirebaseFiles fireb;
    private ListView mListView;
    ArrayList<Design> designs;
    File localFile;
    DesignListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fireb = new FirebaseFiles();
        System.out.println("back in main");

        //localFile = fireb.getFile("/microscope knob.gcode");
        //localFile = fireb.getFile("/motortest.gcode");
        localFile = fireb.getFile("/UltimakerRobot_support.gcode");
        //localFile = fireb.getFile("/m119.gcode");
        //localFile = fireb.getFile("/spatula.gcode");
        //localFile = fireb.getFile("/g28.gcode");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        ArrayList<Design> designs = new ArrayList<Design>();
//        for(int i = 0; i < 50; i++){
//            designs.add(new Design("Item Number " + i,"This is a short description for item number "+i,Integer.toString(i)));
//        }
        mListView = (ListView) findViewById(R.id.designs_list_view);
        this.setList(FirebaseDB.getFiles());

    }

    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        Log.i("HelloListView", "You clicked Item: " + id + " at position:" + position);
        // Then you start a new Activity via Intent
        Intent intent = new Intent();
        intent.setClass(this, DesignDetailActivity.class);

        Bundle mBundle = new Bundle();
        Design selectedDesign = designs.get(position);
        System.out.println(selectedDesign.title);
        mBundle.putSerializable("SelectedDesign",selectedDesign);
        intent.putExtras(mBundle);
        startActivity(intent);
    }

    public void setList(ArrayList<Design> files){
        System.out.println(files.toString());
//        designs = new ArrayList<Design>();
//        for(int i = 0; i < files.size(); i++){
//            designs.add(new Design(files.get(i),"This is a short description for item "+i,Integer.toString(i)));
//            System.out.println(files.get(i) + "inside setList");
//        }
        designs = files;

        System.out.println("called setList");

        adapter = new DesignListAdapter(this, files);
        System.out.println(mListView);
        System.out.println(adapter);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);
    }
}