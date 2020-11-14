package com.example.nutrobud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.nutrobud.ui.home.Stats;
import com.example.nutrobud.ui.home.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class SignUpReview extends AppCompatActivity {

    Button okaybtn;
    ListView reviewview;
    FirebaseAuth fAuth;
    ProgressBar progressbar;
    Map<String, Object> userList = new HashMap<String, Object>(); //Will store the items from user to put into the db
    User user;
    private FirebaseFirestore userDB = FirebaseFirestore.getInstance();//Firestore ref to pull user data
    private DocumentReference dr;
    private List<User> userData = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_review);

        //Get user variable from previous activity
        user = getIntent().getParcelableExtra("User");

        okaybtn = findViewById(R.id.OkayBtn);
        reviewview = findViewById(R.id.ReviewView);

        fAuth = FirebaseAuth.getInstance();
        progressbar = findViewById(R.id.progressBar);

        progressbar.setVisibility(View.INVISIBLE);


        //Get user id's and get the next one for current new user
        userDB.collection("users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshot) {
                if(!queryDocumentSnapshot.isEmpty()){
                    List<DocumentSnapshot> userDBDataList = queryDocumentSnapshot.getDocuments();
                    for(DocumentSnapshot d: userDBDataList){
                        userData.add(d.toObject(User.class));
                    }
                }
            }
        });

        //These variables are for outputting and formatting data for a the ListView
        //of the information he user has entered
        final ArrayList<String> output = new ArrayList<>();
        String temp = "";

        output.add("Email: " + user.getEmail());

        for(int i = 0; i < user.getPassword().length(); i++)
        {
            temp = temp + "*";
        }
        output.add("Password: " + temp);
        temp = "\0";

        output.add("First Name: " + user.getFirstName());
        output.add("Last Name: " + user.getSecondName());

        output.add("Age: " + user.getAge());

        output.add("Gender: " + user.getGender());

        //Weight will be -1 if there is none recorded
        if(user.getWeight() != -1) {
            output.add("Current Weight: " + user.getWeight() + " lbs");
        }
        else{
            output.add("No weight recorded");
        }

        output.add("Calorie goal per-day: " + user.getCalorieGoalsQty());

        if(user.getIngredientsNo().size() != 0) {
            for (int i = 0; i < user.getIngredientsNo().size()-1; i++) {
                temp = temp + user.getIngredientsNo().get(i) + ", ";
            }
            output.add("Ingredients to avoid: " + temp + user.getIngredientsNo().get(user.getIngredientsNo().size()-1));
            temp = "\0";
        }
        else{
            output.add("No ingredients listed to avoid");
        }

        if(user.getIngredientsYes().size() != 0) {
            for (int i = 0; i < user.getIngredientsYes().size()-1; i++) {
                //Value will be -1 if no goal is recorded
                if(Integer.parseInt(user.getIngredientsYesGoalsQty().get(i)) != -1) {
                    temp = temp + user.getIngredientsYesGoalsQty().get(i) + " mg goal per day of " + user.getIngredientsYes().get(i) + "\n";
                }
                else{
                    temp = temp + "No goal set for " + user.getIngredientsYes().get(i) + "\n";
                }
            }
            //Add all of the above things stored in "temp" but also add the last two items without having the new line after the last item
            if(Integer.parseInt(user.getIngredientsYesGoalsQty().get(user.getIngredientsYesGoalsQty().size()-1)) == -1)
            {
                output.add("Vitamins or nutrients to track: \n" + temp + "No goal set for " + user.getIngredientsYes().get(user.getIngredientsYes().size()-1));
            }else{
                output.add("Vitamins or nutrients to track: \n" + temp + user.getIngredientsYesGoalsQty().get(user.getIngredientsYesGoalsQty().size()-1) + "mg goal per day of" + user.getIngredientsYes().get(user.getIngredientsYes().size()-1));
            }
            temp = "\0";
        }
        else{
            output.add("No Vitamins or nutrients listed to track");
        }

        final ArrayAdapter outputAdapted = new ArrayAdapter(this, android.R.layout.simple_list_item_1, output);

        reviewview.setAdapter(outputAdapted);
        //Will open whatever item it slicked
        reviewview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String clicked = output.get(i);

                if(clicked.contains("Name") || clicked.contains("Gender") || clicked.contains("Age")){
                    //Pass intent and user to next activity
                    Intent intent = new Intent(getApplicationContext(), SignUp1.class);
                    intent.putExtra("User", user);
                    startActivity(intent);
                }
                else if(clicked.contains("Email") || clicked.contains("Password")){
                    //Pass intent and user to next activity
                    Intent intent = new Intent(getApplicationContext(), SignUpLoginInfo.class);
                    intent.putExtra("User", user);
                    startActivity(intent);
                }
                else if(clicked.contains("Ingredient") || clicked.contains("Vitamin") || clicked.contains("Weight") || clicked.contains("weight") || clicked.contains("avoid")){
                    //This will take the user to where they entered the items
                    //If there is an issue with the goals, they will have to progress
                    //though the next page that will allow this to be altered
                    //Pass intent and user to next activity
                    Intent intent = new Intent(getApplicationContext(), SignUp2.class);
                    intent.putExtra("User", user);
                    startActivity(intent);
                }
                else if (clicked.contains("Calorie"))
                {
                    //Pass intent and user to next activity
                    Intent intent = new Intent(getApplicationContext(), SignUpGoals.class);
                    intent.putExtra("User", user);
                    startActivity(intent);
                }
            }
        });

        okaybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(SignUpReview.this, "Account Created!", Toast.LENGTH_SHORT).show();

                            user.setId(userData.size() + 10001);
                            dr = FirebaseFirestore.getInstance().document("users/" + user.getId());//Document ref to post data


                            //Store user in the database
                            updateDB(user);
                        }else{
                            Toast.makeText(SignUpReview.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }

    //Put items in the database
    public void updateDB(User user){
        //Empty map made to allow scans to be entered later
        Map<String, Stats> statsPlaceHolder = new HashMap<String, Stats>();

        //Put user into userList to load into db later
        userList.put("age", user.getAge());
        userList.put("calorieGoalsQty", user.getCalorieGoalsQty());
        userList.put("email", user.getEmail());
        userList.put("firstName", user.getFirstName());
        userList.put("gender", user.getGender());
        userList.put("id", user.getId());
        userList.put("weight", user.getWeight());
        userList.put("ingredientsNo", user.getIngredientsNo());
        userList.put("ingredientsYes", user.getIngredientsYes());
        userList.put("ingredientsYesGoalsQty", user.getIngredientsYesGoalsQty());
        userList.put("password", user.getPassword());
        userList.put("secondName", user.getSecondName());
        userList.put("stats", statsPlaceHolder);

        //Load userList into the db
        dr.set(userList, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                startActivity(new Intent(getApplicationContext(), DashActivity.class));
            }
        });
    }
}