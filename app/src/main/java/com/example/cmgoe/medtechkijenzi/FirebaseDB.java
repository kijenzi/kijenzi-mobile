package com.example.cmgoe.medtechkijenzi;

/**
 * Created by cmgoe on 11/24/2017.
 */


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import javax.security.auth.callback.Callback;

/**
 * Created by cmgoe on 11/14/2017.
 */

public final class FirebaseDB {
    private static ArrayList<Design> files;
    private static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    public FirebaseDB(){
        read();
    }

    public static ArrayList<Design> read(){
        DatabaseReference companyRef = mDatabase.child("PrintFiles");
//        companyRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot){
//                ArrayList<Design> readFile = new ArrayList<>();
//                for(DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()){
//                    Design file = noteDataSnapshot.getValue(Design.class);
//                    System.out.println(file.url + " file output");
//                    readFile.add(file);
//                }
//                System.out.println(readFile.toString() + "readFile toString");
//                setFiles(readFile);
//            }
//            @Override
//            public void onCancelled(DatabaseError error){
//                System.out.println("there was a db error");
//            }
//        });
        companyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                ArrayList<Design> readFiles = new ArrayList<>();
                for(DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()){
                    Design file = noteDataSnapshot.getValue(Design.class);
                    if(file.url.contains(".gcode") || file.url.contains(".stl")){
                        System.out.println("Found printable file");
                        readFiles.add(file);
                    }

                }
                setFiles(readFiles);
            }
            @Override
            public void onCancelled(DatabaseError error){
                System.out.println("there was a db error");
            }
        });

        return getFiles();
    }

    public static ArrayList<Design> getFiles() {
        return files;
    }

    public static void setFiles(ArrayList<Design> files) {
        FirebaseDB.files = files;
    }
}
