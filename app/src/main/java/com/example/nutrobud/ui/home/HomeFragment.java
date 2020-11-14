/*
Author: Sahaj Amatya
UTA ID: 1001661825
github.com/sahajamatya
 */

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    //Button initializations
    Button todayBtn;
    Button scanBtn;
    Button calendarBtn;
    Button statisticsBtn;
    Button goalsBtn;
    Button secretBtn;

    //scanStatus booleans
    /*
    These values are set to true once the item in question has been recorded so it doesn't keep looking
    for it in the scanned text block.
     */
    boolean caloriesScanStatus = false;
    boolean sodiumScanStatus = false;
    boolean proteinScanStatus = false;
    boolean carbsScanStatus = false;
    boolean fatScanStatus = false;

    //Firebase Cloud Firestore Gang
    private Map<String, Object> user = new HashMap<String, Object>(); //User obj will be wrapped into this map to be posted
    private Map<String, Stats> statsMapObj = new HashMap<String, Stats>();//Stats obj will be wrapped into this map to be added into User
    private Map<String, Integer> nutrients = new HashMap<String, Integer>();//Stats.nutrients will be wrapped into this map to be added to Stats
    private Map<String, Integer> ingYesTrackedQty = new HashMap<String, Integer>();
    private Stats statsObj = new Stats();
    private User userDBData;
    private FirebaseFirestore userDB = FirebaseFirestore.getInstance();//Firestore ref to pull user data
    private DocumentReference dr;


    //Firebase Auth Gang
    private FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();
    private int currUserID;
    private int currUserIndex;


    //List where pulled data will be kept on a per index basis
    private List<User> userData = new ArrayList<>();

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
    private String todayDate = formatter.format(new Date());

    //hardcoded allergen array, for now. Will be dynamically pulled from Firestore in near future.
    String[] allergens = {"citric acid", "folic acid"};

    private int countClicks = 0;

    String memo ="";

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
                    int indexCounter=-1;
                    for(DocumentSnapshot d: userDBDataList){
                        indexCounter++;
                        userDBData = d.toObject(User.class);
                        userData.add(userDBData);
                        if(userDBData.getEmail().equalsIgnoreCase(currUser.getEmail())){
                            currUserID = userDBData.getId();
                            currUserIndex = indexCounter;
                        }
                    }
                    dr = FirebaseFirestore.getInstance().document("users/"+currUserID);//Document ref to post data
                }
            }
        });
        //END: getFirestoreData
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){

        //linking all buttons and UI components to their respective variables
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

    //Whenever the user is done taking a picture:
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
                matrix.postRotate(90);//rotate image 90 degrees clockwise
                Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                InputImage image = InputImage.fromBitmap(rotatedBitmap, 0);
                TextRecognizer recognizer = TextRecognition.getClient();
                recognizer.process(image)
                        .addOnSuccessListener(
                                new OnSuccessListener<Text>() {
                                    @Override
                                    public void onSuccess(Text texts) {
                                        processTextRecognitionResult(texts);//send recognized text to be processed
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
        List<Text.TextBlock> blocks = texts.getTextBlocks();//convert text to text blocks, each block is stored in a List
        if (blocks.size() == 0) {
            showToast("No text found");
            System.out.println("No text found");
            return;
        }
        String calories;
        boolean caloriesFound = false;
        memo="";
        //text search algorithm
        for (int i = 0; i < blocks.size(); i++) {
            String textBlock = blocks.get(i).getText();
            searchHelper(textBlock, "calories", "sodium", "protein", "carbohydrate", "fat");
            //searchHelper helps search for the param values inside textBlock
        }

        //Setting data in the Firestore DB after done searching
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

    public void searchHelper(String textBlock, String calories, String sodium, String protein, String carbs, String fat){
        searchNutrients(textBlock, calories, caloriesScanStatus);
        searchNutrients(textBlock, sodium, sodiumScanStatus);
        searchNutrients(textBlock, protein, proteinScanStatus);
        searchNutrients(textBlock, carbs, carbsScanStatus);
        searchNutrients(textBlock, fat, fatScanStatus);

        searchAllergens(textBlock);
    }

    public void searchAllergens(String textBlock){
        for(String s: allergens){
            if(textBlock.toLowerCase().contains(s) && !memo.contains(s)){
                System.out.println("ALLERGEN DETECTED: "+ s.toUpperCase());
                memo+=s+',';
            }
        }
    }

    public void searchNutrients(String textBlock, final String entityToSearch, boolean scanStatus){
        String entityQty;

        if( caloriesScanStatus && sodiumScanStatus && proteinScanStatus && carbsScanStatus && fatScanStatus){
            return;
        }

        if(textBlock.toLowerCase().contains((entityToSearch))){
            entityQty = extractNumerals(textBlock.toLowerCase().substring(textBlock.toLowerCase().indexOf(entityToSearch)));

            //if entityToSearch is not present, its quantity is zero
            if(entityQty.equals("")){
                entityQty="0";
            }

            //convert String to integer
            final int entityQtyNum = Integer.parseInt(entityQty);

            int trackedCalories = 0;
            int trackedCarbohydrate = 0;
            int trackedProtein = 0;
            int trackedSodium =0;
            int trackedFat = 0;

            //check if it's already recorded today
            if(userData.get(currUserIndex).getStats().containsKey(todayDate)){
                //the following are values fetched from the database so that new data can be added to them
                trackedCalories = userData.get(currUserIndex).getStats().get(todayDate).getCaloriesTrackedQty();
                trackedCarbohydrate = userData.get(currUserIndex).getStats().get(todayDate).getNutrients().get("carbohydrate");
                trackedProtein = userData.get(currUserIndex).getStats().get(todayDate).getNutrients().get("protein");
                trackedSodium = userData.get(currUserIndex).getStats().get(todayDate).getNutrients().get("sodium");
                trackedFat = userData.get(currUserIndex).getStats().get(todayDate).getNutrients().get("fat");
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

            if(userData.get(currUserIndex).getIngredientsYes().contains(entityToSearch)){
                ingYesTrackedQty.put(entityToSearch, trackedStatsQty(entityToSearch)+entityQtyNum);
            }
            statsObj.setIngredientsYesTrackedQty(ingYesTrackedQty);
            statsObj.setNutrients(nutrients);
            statsMapObj.put(todayDate, statsObj);
        }
    }

    public int trackedStatsQty(String entity){
        if(userData.get(currUserIndex).getStats().containsKey(todayDate)){
            return userData.get(currUserIndex).getStats().get(todayDate).getNutrients().get(entity);
        } else {
            return 0;
        }
    }

    //This function extracts numeric values from the textBlock
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

    //checks if a string is numeric
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

    //basic boiler plate function that dispatches camera
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