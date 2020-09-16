package com.example.nutrobud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class ScanResult extends AppCompatActivity {

    //firebase gang
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private StorageReference ref;
    private DatabaseReference imgScanStatus;

    private String url;
    private boolean isTextFileReady=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);

        //Firebase Storage Initializations
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        //Firebase Realtime Database Initializations
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        imgScanStatus = database.getReference().child("imgScanStatus");

        loadFile();
    }

    public void loadFile(){
        ref = storageReference.child("scanFiles/scan.txt");
        ref.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        url = uri.toString();

                        imgScanStatus.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Map<String, Object> scanStatus = (Map<String, Object>) dataSnapshot.getValue();
                                if(scanStatus.get("isFinishedScanning").equals(true)){
                                    downloadFile(ScanResult.this, "scan", ".txt", DIRECTORY_DOWNLOADS, url);
//                                    imgScanStatus.removeEventListener(this);
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });
    }

    public void downloadFile(Context context, String fileName, String fileExtension, String destinationDirectory, String url){
        DownloadManager downloadManager = (DownloadManager) context
                .getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName+fileExtension);
        downloadManager.enqueue(request);
    }

}