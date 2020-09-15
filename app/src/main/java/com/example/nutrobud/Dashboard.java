package com.example.nutrobud;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class Dashboard extends AppCompatActivity {

    Button scanBtn;
    ImageView imageView;
    String pathToFile;
    private Uri pictureURI;

    //firebase gang
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private DatabaseReference imgPostStatus;
    private DatabaseReference imgScanStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Firebase Storage Initializations
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        //Firebase Realtime Database Initializations
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        imgPostStatus = database.getReference().child("imgPostStatus");
        imgScanStatus = database.getReference().child("imgScanStatus");


        scanBtn = findViewById(R.id.scanBtn);
        imageView = findViewById(R.id.imageView);
        if(Build.VERSION.SDK_INT >=23){
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},2);
        }
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchCamera();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == 1){
                Bitmap bitmap = BitmapFactory.decodeFile(pathToFile);
                imageView.setImageBitmap(bitmap);
                uploadPicture();
            }
        }
    }

    private void uploadPicture() {
        StorageReference Ref = storageReference.child("image.jpg");
        Ref.putFile(pictureURI);
        updateDatabase();
    }

    public void updateDatabase() {
        HashMap updatedValue = new HashMap();
        updatedValue.put("isImageToScan", true);
        imgPostStatus.updateChildren(updatedValue);
        imgScanStatus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object> scanStatus = (Map<String, Object>) dataSnapshot.getValue();
                HashMap desiredScanStatus= new HashMap();
                desiredScanStatus.put("isFinishedScanning", true);//make this more elegant - sahajamatya 09/14
                if(scanStatus.equals(desiredScanStatus)){
                    openScanResult();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void openScanResult(){
        Intent intent = new Intent(this, ScanResult.class);
        startActivity(intent);
    }

    private void dispatchCamera() {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePicture.resolveActivity(getPackageManager())!=null){
            File pictureFile = null;
            pictureFile = createPictureFile();

            if(pictureFile!=null){
                pathToFile = pictureFile.getAbsolutePath();
                pictureURI = FileProvider.getUriForFile(Dashboard.this, "com.example.nutrobud.fileprovider", pictureFile);
                takePicture.putExtra(MediaStore.EXTRA_OUTPUT, pictureURI);
                startActivityForResult(takePicture, 1);
            }
        }
    }

    private File createPictureFile(){
        String name = "image";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        Creating a file in this directory only creates a file that can be read by our app and our
//        app only. An alternative would be to use the getExternalStoragePublicDirectory() method
//        from android.os.Environment.getExternalStoragePublicDirectory but this method has been
//        deprecated since Android Q. - sahajamatya 09/14
        File image = null;
        try{
            image = File.createTempFile(name, ".jpg", storageDir);
        }
        catch (IOException e){
            Log.d("myLog", "Excep: " + e.toString());
        }
        return image;
    }


}