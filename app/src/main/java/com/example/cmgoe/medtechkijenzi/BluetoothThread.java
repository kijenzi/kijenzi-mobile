package com.example.cmgoe.medtechkijenzi;

/**
 * Created by cmgoe on 11/24/2017.
 */

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;

/**
 * Created by cmgoe on 10/22/2017.
 */

public class BluetoothThread extends Thread implements Serializable {
    private final BluetoothDevice mmDevice;
    private final BluetoothSocket mmSocket;
    private String command;
    private boolean isDev = false;
    private InputStream in = null;
    private File localFile;
    private Context mContext;
    private OutputStream out = null;
    private final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    //private final UUID MY_UUID = UUID.fromString("8c372ea7-7a0f-4380-b133-17995eebff4b");

    public BluetoothThread(BluetoothDevice device, File localFile, boolean theIsDev, Context context) {
        mContext = context;
        mmDevice = device;
        isDev = theIsDev;
        this.localFile = localFile;
        BluetoothSocket tmp = null;
        try {
            tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("caught something");
        }
        mmSocket = tmp;
    }


    public void run() {
        //mBluetoothAdapter.cancelDiscovery();
        try {
            mmSocket.connect();
            System.out.println(mmDevice.getName());
        } catch (IOException connectException) {
            try {
                mmSocket.close();
                connectException.printStackTrace();
                System.out.println("closed socket?");
            } catch (IOException closeException) {
                System.out.println("caught something");
            }
            return;
        }

        //mConnectedThread = new MainActivity.ConnectedThread(mmSocket);
        InputStream tempIn = null;
        OutputStream tempOut = null;
        try {
            tempIn = mmSocket.getInputStream();
            tempOut = mmSocket.getOutputStream();
            System.out.println("it worked?");
        } catch (IOException e){
            e.printStackTrace();
            System.out.println("caught something");
        }

        in = tempIn;
        out = tempOut;

        if(isDev){
            sendGCode();
        } else {
            sendFile(localFile);
        }
    }

    public void setCommand (String command) {
        this.command = command;
    }

    public void sendGCode () {
        try {
            command = command + "\n";
            System.out.println(command.getBytes(StandardCharsets.US_ASCII));
            out.write(command.getBytes(StandardCharsets.US_ASCII));
            System.out.println(in.read() + " || response");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void cancel() {
        try {
            mmSocket.close();
            System.out.println("file send worked?");
        } catch (IOException e) {
            System.out.println("caught something");
        }
    }

    public void sendFile(File fileToSend){
        System.out.println("made it to sendFile");
        ArrayList<String> lines = FileToByteConverter.convertToLines(fileToSend);
        byte[] command = {};
        int i = 0;

        //inital print commands here
        //G0 Z0
//        command = "G28\n".getBytes(StandardCharsets.US_ASCII);
//        try{
//            out.write(command);
//            System.out.println(in.read());
//        } catch (Exception e){
//
//            e.printStackTrace();
//        }

        for(String line : lines){
            System.out.println(line + " here is a line");

            try{
                line = line + "\n";
                out.write(line.getBytes(StandardCharsets.US_ASCII));
                String read = Integer.toString(in.read());
                int letter = Integer.parseInt(line.replaceAll("\\D+",""));
                //responses.add(letter);
                String currChar = Character.toString((char) letter);

                Intent statusMessage = new Intent("StatusIntent");
                statusMessage.putExtra("theCurrChar", currChar);
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(statusMessage);

                System.out.print(currChar);
                out.flush();
            } catch (Exception e){
                e.printStackTrace();
                System.out.println("failed a line");
            }
            i++;
        }


//        try {
//            command = "G28\n".getBytes(StandardCharsets.US_ASCII);
//            //out.write(command);
//        } catch (Exception e){
//
//        }
//        //byte[] convertedFile = FileToByteConverter.convertFile(fileToSend);
//        byte[] command = {};
//        try{
//            command = "G28\n".getBytes(StandardCharsets.US_ASCII);
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//
//        System.out.println("Sending...");
//
//        try{
//
//            Scanner scanner = null;
//            ArrayList<String> response = null;
//
//            try{
//                scanner = new Scanner( new File(fileToSend.getPath()));
//                response = new ArrayList<String>();
//                String text = scanner.useDelimiter("\\A").next();
//                //System.out.println(text.getBytes(StandardCharsets.US_ASCII));
//                //out.write(text.getBytes(StandardCharsets.US_ASCII));
//                response.add(text + "cheese");
//
//
//            } catch (Exception e){
//
//            }scanner.close();
//
//            for(int i = 0; i < response.size(); i++){
//
//                System.out.println(response.get(i) + "heres line "+i);
//                //System.out.println(i);
//            }
//            //out.write(command);
//            //out.write(convertedFile, 0, convertedFile.length);
//            out.flush();
//            System.out.println("sent file");
//        } catch (IOException e){
//            e.printStackTrace();
//            System.out.println("something failed");
//        }
    }

}
