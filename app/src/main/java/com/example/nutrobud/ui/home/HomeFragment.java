package com.example.nutrobud.ui.home;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.nutrobud.DashActivity;
import com.example.nutrobud.Dashboard;
import com.example.nutrobud.R;
import com.example.nutrobud.ScanResult;
import com.example.nutrobud.ui.gallery.GalleryViewModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment {

    Button scanBtn;
    ImageView imageView;
    String pathToFile;
    private Uri pictureURI;

    //firebase gang
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private DatabaseReference imgPostStatus;
    private HomeViewModel homeViewModel;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        //final TextView textView = root.findViewById(R.id.text_gallery);

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });
        super.onCreate(savedInstanceState);
        //getActivity().setContentView(R.layout.fragment_home);

        //Firebase Storage Initializations
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        //Firebase Realtime Database Initializations
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        imgPostStatus = database.getReference().child("imgPostStatus");


        return root;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        scanBtn = (Button) getView().findViewById(R.id.scanBtn);
        imageView = (ImageView) getView().findViewById(R.id.imageView);
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

    public void onClick(View v){
        dispatchCamera();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
        Ref.putFile(pictureURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                updateDatabase();
                openScanResult();
            }
        });
    }

    public void updateDatabase() {
        HashMap updatedValue = new HashMap();
        updatedValue.put("isImageToScan", true);
        imgPostStatus.updateChildren(updatedValue);

    }

    public void openScanResult(){
        Intent intent = new Intent(getActivity(), ScanResult.class);
        startActivity(intent);
    }

    private void dispatchCamera() {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePicture.resolveActivity(getActivity().getPackageManager())!=null){
            File pictureFile = null;
            pictureFile = createPictureFile();

            if(pictureFile!=null){
                pathToFile = pictureFile.getAbsolutePath();
                pictureURI = FileProvider.getUriForFile(getActivity(), "com.example.nutrobud.fileprovider", pictureFile);
                takePicture.putExtra(MediaStore.EXTRA_OUTPUT, pictureURI);
                startActivityForResult(takePicture, 1);
            }
        }
    }

    private File createPictureFile(){
        String name = "image";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
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