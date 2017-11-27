package com.example.cmgoe.medtechkijenzi;

/**
 * Created by cmgoe on 11/24/2017.
 */

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

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
    private InputStream in = null;
    private File localFile;
    private OutputStream out = null;
    private final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    //private final UUID MY_UUID = UUID.fromString("8c372ea7-7a0f-4380-b133-17995eebff4b");

    public BluetoothThread(BluetoothDevice device, File localFile) {
        mmDevice = device;
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

        sendFile(localFile);

        byte[] buffer = new byte[1024];
        int begin = 0;
        int bytes = 0;
        while (true) {
            try {
                bytes += in.read(buffer, bytes, buffer.length - bytes);
                for(int i = begin; i < bytes; i++) {
                    if(buffer[i] == "#".getBytes()[0]) {
                        begin = i + 1;
                        if(i == bytes - 1) {
                            bytes = 0;
                            begin = 0;
                        }
                    }
                }
                System.out.println(bytes + " || response");
            } catch (IOException e) {
                break;
            }
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
        ArrayList<String> lines = FileToByteConverter.convertToLines(fileToSend);
        int i = 0;
        for(String line : lines){

            System.out.println(line + " here is a line");
            System.out.println(i);
            //line = line.replaceAll

            try{
                line = line + "\n";
                out.write(line.getBytes(StandardCharsets.US_ASCII));
                out.flush();
            } catch (Exception e){
                e.printStackTrace();
                System.out.println("failed a line");
            }
            i++;
        }

        byte[] command = {};
        try {
            command = "G28\n".getBytes(StandardCharsets.US_ASCII);
            //out.write(command);
        } catch (Exception e){

        }


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
