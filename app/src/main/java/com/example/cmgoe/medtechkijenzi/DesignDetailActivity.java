package com.example.cmgoe.medtechkijenzi;

/**
 * Created by cmgoe on 11/24/2017.
 */

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cmgoe.medtechkijenzi.R;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;

/**
 * Created by cmgoe on 11/2/2017.
 */

public class DesignDetailActivity extends AppCompatActivity {
    private TextView fileType, titleText, descText, urlText;
    private Button connectButton;
    private Button printButton;
    private ImageView designImage;
    private FirebaseFiles fireb;
    private boolean selectionMade;
    private File localFile;
    BluetoothDevice selectedDevice;
    BluetoothThread connection;
    ArrayList<BluetoothDevice> deviceList;
    static final int REQUEST_BT_ENABLE = 1;
    BluetoothAdapter mBluetoothAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        deviceList = new ArrayList<BluetoothDevice>();
        fireb = new FirebaseFiles();
        selectionMade = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listitem);
        this.setUpBluetooth();

        Intent intent = getIntent();
        int position = intent.getIntExtra("position", 0);

        urlText = (TextView) findViewById(R.id.detail_filename);
        titleText = (TextView) findViewById(R.id.detail_title);
        descText = (TextView) findViewById(R.id.detail_description);
        fileType = (TextView) findViewById(R.id.detail_filetype);

        Design currDesign = (Design)getIntent().getSerializableExtra("SelectedDesign");
        urlText.setText(currDesign.url);
        titleText.setText(currDesign.title);
        descText.setText(currDesign.desc);
        try{
            fileType.setText(currDesign.url.split("\\.")[1]);
        } catch (Exception e){
            fileType.setText("unknown");
        }

        localFile = fireb.getFile("/"+currDesign.url, ".gcode");

        System.out.println(localFile.exists() + "does the file exist");


        connectButton = (Button) findViewById(R.id.connect_button);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDevice();
            }
        });

        printButton = (Button) findViewById(R.id.print_button);
        printButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                print();
            }
        });

        designImage = (ImageView) findViewById(R.id.logo_picture);

        StorageReference ref = fireb.getStorageRef(getImageUrl(currDesign.url));

        GlideApp.with(this)
                .load(ref)
                .placeholder(R.drawable.full_logo)
                .into(designImage);

    }


    private void setUpBluetooth(){
        //Enable bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null){
            Toast.makeText(this, "This device does not support Bluetooth", Toast.LENGTH_LONG).show();
        }else{
            //Checks if bluetooth is enabled and enables it if it's not enabled
            if(!mBluetoothAdapter.isEnabled()){
                Intent bluetoothStartIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(bluetoothStartIntent, REQUEST_BT_ENABLE);
            }
        }
    }


    private void selectDevice(){


        //Finds list of connected devices, adds devices to ArrayList
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if(pairedDevices.size()> 0){
            for(BluetoothDevice device : pairedDevices){
                deviceList.add(device);
                Log.i("Device Found: ", device.getName());

            }
        }

        final Dialog dlg = new Dialog(this);
        ListView listView = new ListView(this);
        final String [] deviceNames = new String[deviceList.size()];
        for (int i =0; i<deviceList.size(); i++){
            deviceNames[i] = deviceList.get(i).getName();
        }
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, deviceNames));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {public void onItemClick(AdapterView<?> parent, View view, int position, long id){
            Log.i("Device Selected: ", deviceNames[position]);
            selectedDevice = deviceList.get(position);
//            ConnectThread mConnectThread = new ConnectThread(selectedDevice);
//            mConnectThread.start();
            //System.out.println(localFile.exists() + "does it exist");

            dlg.hide();

        }});
        dlg.setContentView(listView);
        dlg.show();
        selectionMade = true;
    }

    private void print() {
        if(selectionMade){
            connection = new BluetoothThread(selectedDevice, localFile, false,getApplicationContext());
            connection.start();
            System.out.println("Printing...");
            Toast.makeText(getApplicationContext(), "Printing design...", Toast.LENGTH_LONG).show();
            //connection.sendFile(localFile); //eventually this will be used instead of immediately sending

            Intent intent = new Intent(getApplicationContext(), PrintStatusActivity.class);
            //intent.putExtra("btConnect", connection);
            startActivity(intent);

        } else {
            Toast.makeText(getApplicationContext(), "You must select a Bluetooth device before printing!", Toast.LENGTH_LONG).show();
        }

    }

    private String getImageUrl(String designUrl){
        return designUrl.split("\\.")[0] + ".jpg";
    }
}
