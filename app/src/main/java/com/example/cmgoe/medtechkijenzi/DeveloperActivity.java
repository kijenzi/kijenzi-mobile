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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class DeveloperActivity extends AppCompatActivity {
    private Button sendButton;
    private EditText textInput;
    BluetoothDevice selectedDevice;
    BluetoothThread connection;
    ArrayList<BluetoothDevice> deviceList;
    static final int REQUEST_BT_ENABLE = 1;
    BluetoothAdapter mBluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer);

        deviceList = new ArrayList<BluetoothDevice>();
        this.setUpBluetooth();
        this.selectDevice();
        textInput = (EditText) findViewById(R.id.gcode_input);
        sendButton = (Button) findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCommand();
            }
        });

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

    private void sendCommand() {
        connection = new BluetoothThread(selectedDevice, null, true, getApplicationContext());
        connection.setCommand(textInput.getText().toString());
        connection.start();
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
    }
}
