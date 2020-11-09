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
import com.example.nutrobud.DashActivity;
import com.example.nutrobud.GoalsActivity;
import com.example.nutrobud.R;
import com.example.nutrobud.StatisticsActivity;
import com.example.nutrobud.ui.objectPassEx.Act1;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
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
    Button secretBtn;

    boolean caloriesScanStatus = false;
    boolean sodiumScanStatus = false;
    boolean proteinScanStatus = false;
    boolean carbsScanStatus = false;
    boolean fatScanStatus = false;

    private DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    private DocumentReference dr = FirebaseFirestore.getInstance().document("users/10001");

    private List<User> userList = new ArrayList<>();

    //Firebase Cloud Firestore Gang
    private Map<String, Object> user = new HashMap<String, Object>();
    private Map<String, Stats> statsMapObj = new HashMap<String, Stats>();
    private Map<String, Integer> nutrients = new HashMap<String, Integer>();
    private Stats statsObj = new Stats();
    private User userDBData;
    private FirebaseFirestore userDB = FirebaseFirestore.getInstance();

    private List<User> userData = new ArrayList<>();

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
    private String todayDate = formatter.format(new Date());
    boolean[] scanStatus = new boolean[]{false, false, false, false, false};
    String[] allergens = {"citric acid", "folic acid"};
    String[] nutrientsArr = {"calories","sodium","protein","carbs","fat"};

    private int countClicks = 0;

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

        //Loading all data from Firestore
        //getFirestoreData
        userDB.collection("users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshot) {
                if(!queryDocumentSnapshot.isEmpty()){
                    List<DocumentSnapshot> userDBDataList = queryDocumentSnapshot.getDocuments();
                    for(DocumentSnapshot d: userDBDataList){
                        userDBData = d.toObject(User.class);
                        userData.add(userDBData);
                    }
                }
            }
        });
        //END: getFirestoreData
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        todayBtn = (Button) getView().findViewById(R.id.todayBtn);
        scanBtn = (Button) getView().findViewById(R.id.scanBtn);
        calendarBtn = (Button) getView().findViewById(R.id.calendarBtn);
        goalsBtn = (Button) getView().findViewById(R.id.goalsBtn);
        statisticsBtn = (Button) getView().findViewById(R.id.statisticsBtn);
        secretBtn = (Button) getView().findViewById(R.id.secretBtn);

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

        secretBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                countClicks++;
                if(countClicks==5){
                    startActivity(new Intent(getApplicationContext(), Act1.class));
                }
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

        //Setting data in the Firestore DB
        //setFirestoreData
        user.put("stats", statsMapObj);
        dr.set(user, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                startActivity(new Intent(getApplicationContext(), DashActivity.class));
            }
        });
        //END: setFirestoreData
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

    public void searchNutrients(String textBlock, String nextBlock, final String entityToSearch, boolean scanStatus){
        String entityQty;

        if( caloriesScanStatus && sodiumScanStatus && proteinScanStatus && carbsScanStatus && fatScanStatus){
            return;
        }

        if(textBlock.toLowerCase().contains((entityToSearch))){
            entityQty = extractNumerals(textBlock.toLowerCase().substring(textBlock.toLowerCase().indexOf(entityToSearch)));

            if(entityQty.equals("")){
                entityQty="0";
            }
            final int entityQtyNum = Integer.parseInt(entityQty);

            int trackedCalories = 0;
            int trackedCarbohydrate = 0;
            int trackedProtein = 0;
            int trackedSodium =0;
            int trackedFat = 0;

            if(userData.get(0).getStats().containsKey(todayDate)){
                //the following are values fetched from the database so that new data can be added to them
                trackedCalories = userData.get(0).getStats().get(todayDate).getCaloriesTrackedQty();
                trackedCarbohydrate = userData.get(0).getStats().get(todayDate).getNutrients().get("carbohydrate");
                trackedProtein = userData.get(0).getStats().get(todayDate).getNutrients().get("protein");
                trackedSodium = userData.get(0).getStats().get(todayDate).getNutrients().get("sodium");
                trackedFat = userData.get(0).getStats().get(todayDate).getNutrients().get("fat");
            }

            if(entityToSearch.equalsIgnoreCase("calories") && !caloriesScanStatus){
                statsObj.setCaloriesTrackedQty(trackedCalories + entityQtyNum);
                caloriesScanStatus = true;
            } else if(entityToSearch.equalsIgnoreCase("sodium") && !sodiumScanStatus){
                if(!nutrients.containsKey(entityToSearch.toLowerCase())){
                    nutrients.put(entityToSearch.toLowerCase(), trackedSodium + entityQtyNum);
                }
                sodiumScanStatus = true;
            } else if(entityToSearch.equalsIgnoreCase("protein") && !proteinScanStatus){
                if(!nutrients.containsKey(entityToSearch.toLowerCase())){
                    nutrients.put(entityToSearch.toLowerCase(), trackedProtein + entityQtyNum);
                }
                proteinScanStatus = true;
            } else if(entityToSearch.equalsIgnoreCase("carbohydrate") && !carbsScanStatus){
                if(!nutrients.containsKey(entityToSearch.toLowerCase())){
                    nutrients.put(entityToSearch.toLowerCase(), trackedCarbohydrate + entityQtyNum);
                }
                carbsScanStatus = true;
            } else if(entityToSearch.equalsIgnoreCase("fat") && !fatScanStatus){
                if(!nutrients.containsKey(entityToSearch.toLowerCase())){
                    nutrients.put(entityToSearch.toLowerCase(), trackedFat + entityQtyNum);
                }
                fatScanStatus = true;
            }
            statsObj.setNutrients(nutrients);
            statsMapObj.put(todayDate, statsObj);
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
