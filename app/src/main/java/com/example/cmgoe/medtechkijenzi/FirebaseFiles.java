package com.example.cmgoe.medtechkijenzi;

/**
 * Created by cmgoe on 11/24/2017.
 */

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by cmgoe on 11/1/2017.
 */

public class FirebaseFiles {
    private com.google.firebase.storage.FirebaseStorage storage;
    private StorageReference ref;

    public FirebaseFiles() {
        storage = com.google.firebase.storage.FirebaseStorage.getInstance();
        ref = storage.getReference();

    }

    public File getFile(String path, String suffix){
        File file = null;
        StorageReference model = ref.child(path);
        try {
            file = File.createTempFile("design-", suffix);
            model.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    System.out.println(taskSnapshot.getTotalByteCount() + " HERE IS THE TOTAL BYTE COUNT");

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    System.out.println("FAILED");
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            file = null;
            System.out.println("FAILED");
        }

        return file;
    }

    public StorageReference getStorageRef(String path){
        StorageReference refFromPath = ref.child(path);
        return refFromPath;
    }

}
