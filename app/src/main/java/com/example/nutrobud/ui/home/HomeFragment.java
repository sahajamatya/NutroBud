package com.example.nutrobud.ui.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.nutrobud.CalendarActivity;
import com.example.nutrobud.GoalsActivity;
import com.example.nutrobud.R;
import com.example.nutrobud.StatisticsActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class HomeFragment<i> extends Fragment {

    Button todayBtn;
    Button scanBtn;
    Button calendarBtn;
    Button statisticsBtn;
    Button goalsBtn;

    boolean caloriesScanStatus = false;
    boolean sodiumScanStatus = false;
    boolean proteinScanStatus = false;
    boolean carbsScanStatus = false;
    boolean fatScanStatus = false;

    private DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    private DocumentReference dr = FirebaseFirestore.getInstance().document("users/10001");


    private List<User> userList = new ArrayList<>();

    boolean[] scanStatus = new boolean[]{false, false, false, false, false};
    String[] allergens = {"citric acid", "folic acid"};
    String[] nutrients = {"calories","sodium","protein","carbs","fat"};

    String memo ="";
    enum status {
        CALORIES,
        SODIUM,
        PROTEIN,
        CARBS,
        FAT
    }

    ImageView imageView;
    String pathToFile;
    private Uri pictureURI;

    //firebase gang
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private DatabaseReference imgPostStatus;

    private HomeViewModel homeViewModel;

    @Override
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

        db.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    userList.add(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        todayBtn = (Button) getView().findViewById(R.id.todayBtn);
        scanBtn = (Button) getView().findViewById(R.id.scanBtn);
        calendarBtn = (Button) getView().findViewById(R.id.calendarBtn);
        goalsBtn = (Button) getView().findViewById(R.id.goalsBtn);
        statisticsBtn = (Button) getView().findViewById(R.id.statisticsBtn);

        imageView = (ImageView) getView().findViewById(R.id.imageView);
        if(Build.VERSION.SDK_INT >=23){
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},2);
        }
        todayBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), StatisticsActivity.class));
            }
        });
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchCamera();
            }
        });
        calendarBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CalendarActivity.class));
            }
        });

        goalsBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), GoalsActivity.class));
            }
        });

        statisticsBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), StatisticsActivity.class));
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        caloriesScanStatus = false;
        sodiumScanStatus = false;
        proteinScanStatus = false;
        carbsScanStatus = false;
        fatScanStatus = false;
        if(resultCode == RESULT_OK){
            if(requestCode == 1){
                Bitmap bitmap = BitmapFactory.decodeFile(pathToFile);
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                InputImage image = InputImage.fromBitmap(rotatedBitmap, 0);
                TextRecognizer recognizer = TextRecognition.getClient();
                recognizer.process(image)
                        .addOnSuccessListener(
                                new OnSuccessListener<Text>() {
                                    @Override
                                    public void onSuccess(Text texts) {
                                        processTextRecognitionResult(texts);
                                    }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Task failed with an exception
                                        e.printStackTrace();
                                    }
                                });

                //For debugging purposes: - sahajamatya - 11/01
                System.out.println("This is the bitmap reference: "+bitmap);
                System.out.println("This is the path to file: "+pathToFile);
            }
        }
    }

    @SuppressLint("RestrictedApi")
    private void processTextRecognitionResult(Text texts) {
        List<Text.TextBlock> blocks = texts.getTextBlocks();
        if (blocks.size() == 0) {
            showToast("No text found");
            System.out.println("No text found");
            return;
        }
        String calories;
        boolean caloriesFound = false;
        memo="";
        for (int i = 0; i < blocks.size(); i++) {
            String textBlock = blocks.get(i).getText();
            String nextBlock="";
            if(i<blocks.size()-1){
                nextBlock = blocks.get(i+1).getText();
            }
            searchHelper(textBlock, nextBlock, "calories", "sodium", "protein", "carbohydrate", "fat");
        }
    }

    public void searchHelper(String textBlock, String nextBlock, String calories, String sodium, String protein, String carbs, String fat){
        searchNutrients(textBlock, nextBlock, calories, caloriesScanStatus);
        searchNutrients(textBlock, nextBlock, sodium, sodiumScanStatus);
        searchNutrients(textBlock, nextBlock, protein, proteinScanStatus);
        searchNutrients(textBlock, nextBlock, carbs, carbsScanStatus);
        searchNutrients(textBlock, nextBlock, fat, fatScanStatus);

        searchAllergens(textBlock);
    }

    public void searchAllergens(String textBlock){
        //System.out.println("INSIDE FUNCTION");
        for(String s: allergens){
            if(textBlock.toLowerCase().contains(s) && !memo.contains(s)){
                System.out.println("ALLERGEN DETECTED: "+ s.toUpperCase());
                memo+=s+',';
            }
        }
    }

    public void searchNutrients(String textBlock, String nextBlock, String entityToSearch, boolean scanStatus){
        String entityQty;

        if( caloriesScanStatus && sodiumScanStatus && proteinScanStatus && carbsScanStatus && fatScanStatus){
            return;
        }

        if(textBlock.toLowerCase().contains((entityToSearch))){
            entityQty = extractNumerals(textBlock.toLowerCase().substring(textBlock.toLowerCase().indexOf(entityToSearch)));

            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            String todayDate = formatter.format(new Date());
            if(entityQty.equals("")){
                entityQty="0";
            }
            int entityQtyNum = Integer.parseInt(entityQty);

            //Firebase RTDB deployment
            User demoUser = userList.get(0);
            Map<String, Stats> demoUserStatsMap = demoUser.getStats();
            Stats demoUserStats = demoUserStatsMap.get(todayDate);
            Map<String, Integer> nutrientsMap = demoUserStats.getNutrients();

            //Firebase Cloud Firestore Database deployment
            Map<String, Object> user = new HashMap<String, Object>();
            Map<String, Stats> statsMapObj = new HashMap<String, Stats>();
            Stats statsObj = new Stats();
            if(entityToSearch.equalsIgnoreCase("calories") && !caloriesScanStatus){
                System.out.println("THE AMOUNT OF "+ entityToSearch +" IS: "+ entityQty);
                System.out.println("Previous amount from DB: "+demoUserStats.getCaloriesTrackedQty());
                db.child("users").child("demoUserID").child("stats").child(todayDate).child("caloriesTrackedQty").setValue(demoUserStats.getCaloriesTrackedQty()+entityQtyNum);
                user.put("age", 222);
                statsObj.setCaloriesTrackedQty(555);
                statsMapObj.put(todayDate, statsObj);
                user.put("stats", statsMapObj);
                dr.update(user);
                caloriesScanStatus = true;
            } else if(entityToSearch.equalsIgnoreCase("sodium") && !sodiumScanStatus){
                System.out.println("THE AMOUNT OF "+ entityToSearch +" IS: "+ entityQty);
                System.out.println("Previous amount from DB: "+nutrientsMap.get("sodium"));
                db.child("users").child("demoUserID").child("stats").child(todayDate).child("nutrients").child("sodium").setValue(nutrientsMap.get("sodium")+entityQtyNum);
                sodiumScanStatus = true;
            } else if(entityToSearch.equalsIgnoreCase("protein") && !proteinScanStatus){
                System.out.println("THE AMOUNT OF "+ entityToSearch +" IS: "+ entityQty);
                System.out.println("Previous amount from DB: "+nutrientsMap.get("protein"));
                db.child("users").child("demoUserID").child("stats").child(todayDate).child("nutrients").child("protein").setValue(nutrientsMap.get("protein")+entityQtyNum);
                sodiumScanStatus = true;
            } else if(entityToSearch.equalsIgnoreCase("carbohydrate") && !carbsScanStatus){
                System.out.println("THE AMOUNT OF "+ entityToSearch +" IS: "+ entityQty);
                System.out.println("Previous amount from DB: "+nutrientsMap.get("carbohydrates"));
                db.child("users").child("demoUserID").child("stats").child(todayDate).child("nutrients").child("carbohydrates").setValue(nutrientsMap.get("carbohydrates")+entityQtyNum);
                carbsScanStatus = true;
            } else if(entityToSearch.equalsIgnoreCase("fat") && !fatScanStatus){
                System.out.println("THE AMOUNT OF "+ entityToSearch +" IS: "+ entityQty);
                System.out.println("Previous amount from DB: "+nutrientsMap.get("fat"));
                db.child("users").child("demoUserID").child("stats").child(todayDate).child("nutrients").child("fat").setValue(nutrientsMap.get("fat")+entityQtyNum);
                fatScanStatus = true;
            }
        }
    }

    public String extractNumerals(String textBlock){
        String numeral="";
        int count=0;
        for(int i=0;i<textBlock.length();i++){
            if(isNumeric(String.valueOf(textBlock.charAt(i)))){
                numeral+=String.valueOf(textBlock.charAt(i));
                count++;
            } else {
                if(count>0){
                    return numeral;
                }
            }
        }
        return numeral;
    }

    public boolean isNumeric(String toTest){
        if(android.text.TextUtils.isDigitsOnly(toTest)){
            return true;
        }
        else {
            return false;
        }
    }

    @SuppressLint("RestrictedApi")
    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
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