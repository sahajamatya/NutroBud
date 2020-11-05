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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class SignUpReview extends AppCompatActivity {

    Button okaybtn;
    ListView reviewview;
    FirebaseAuth fAuth;
    ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_review);

        okaybtn = findViewById(R.id.OkayBtn);
        reviewview = findViewById(R.id.ReviewView);

        fAuth = FirebaseAuth.getInstance();
        progressbar = findViewById(R.id.progressBar);

        //These will be coming from User. Hard coding for now to show functionality
        String firstName = "John";
        String lastName = "Doe";
        final String email = "someone@aplace.com";
        final String password = "123456";
        int age = 32;
        char gender = 'm';
        double weight = 200.6;
        int cal_goal = 2000;

        final ArrayList<String> ingredient_yes = new ArrayList<>();
        ingredient_yes.add("A");
        ingredient_yes.add("C");
        ingredient_yes.add("Iron");
        ingredient_yes.add("B");
        ingredient_yes.add("Protien");

        final ArrayList<Integer> ing_yes_goal = new ArrayList<>();
        ing_yes_goal.add(10);
        ing_yes_goal.add(100);
        ing_yes_goal.add(70);
        ing_yes_goal.add(-1);
        ing_yes_goal.add(200);

        final ArrayList<String> ingredient_no = new ArrayList<>();
        ingredient_no.add("Soy");
        ingredient_no.add("Milk");
        ingredient_no.add("Eggs");

        //start activity variables
        final ArrayList<String> output = new ArrayList<>();
        String temp = "";

        output.add("Email: " + email);

        for(int i = 0; i < password.length(); i++)
        {
            temp = temp + "*";
        }
        output.add("Password: " + temp);
        temp = "\0";

        output.add("First Name: " + firstName);
        output.add("Last Name: " + lastName);

        output.add("Age: " + age);

        if(gender == 'm'){
            temp = "Male";
        }
        else if(gender == 'f'){
            temp = "Female";
        }
        else{
            temp = "Other";
        }
        output.add("Gender: " + temp);
        temp = "\0";

        //Weight will be -1 if there is none recorded
        if(weight != -1) {
            output.add("Current Weight: " + weight + " lbs");
        }
        else{
            output.add("No weight recorded");
        }

        //Calories will be -1 if there is none recorded
        if(cal_goal != -1){
            output.add("Calorie goal per-day: " + cal_goal);
        }
        else{
            output.add("No calorie goal set");
        }

        if(ingredient_no.size() != 0) {
            for (int i = 0; i < ingredient_no.size()-1; i++) {
                temp = temp + ingredient_no.get(i) + ", ";
            }
            output.add("Ingredients to avoid: " + temp + ingredient_no.get(ingredient_no.size()-1));
            temp = "\0";
        }
        else{
            output.add("No ingredients listed to avoid");
        }

        if(ingredient_yes.size() != 0) {
            for (int i = 0; i < ingredient_yes.size()-1; i++) {
                //Value will be -1 if no goal is recorded
                if(ing_yes_goal.get(i) != -1) {
                    temp = temp + ing_yes_goal.get(i) + " mg goal per day of " + ingredient_yes.get(i) + "\n";
                }
                else{
                    temp = temp + "No goal set for " + ingredient_yes.get(i) + "\n";
                }
            }
            output.add("Vitamins or nutrients to track: \n" + temp + ing_yes_goal.get(ing_yes_goal.size()-1) + "mg goal per day of" + ingredient_yes.get(ingredient_yes.size()-1));
            temp = "\0";
        }
        else{
            output.add("No Vitamins or nutrients listed to track");
        }

        final ArrayAdapter outputAdapted = new ArrayAdapter(this, android.R.layout.simple_list_item_1, output);

        reviewview.setAdapter(outputAdapted);

        reviewview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String clicked = output.get(i);

                if(clicked.contains("Name") || clicked.contains("Gender") || clicked.contains("Age")){
                    startActivity(new Intent(getApplicationContext(), SignUp1.class));
                }
                else if(clicked.contains("Email") || clicked.contains("Password")){
                    startActivity(new Intent(getApplicationContext(), SignUpLoginInfo.class));
                }
                else if(clicked.contains("Ingredient") || clicked.contains("Vitamin") || clicked.contains("Weight")){
                    //This will take the user to where they entered the items
                    //If there is an issue with the goals, they will have to progress
                    //though the next page that will allow this to be altered
                    startActivity(new Intent(getApplicationContext(), SignUp2.class));
                }
                else if (clicked.contains("Calorie"))
                {
                    startActivity(new Intent(getApplicationContext(), SignUpGoals.class));
                }
            }
        });

        okaybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(SignUpReview.this, "Account Created!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), DashActivity.class));
                        }else{
                            Toast.makeText(SignUpReview.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }
}