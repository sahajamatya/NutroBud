package com.example.nutrobud;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.nutrobud.ui.home.Stats;
import com.example.nutrobud.ui.home.User;

import java.util.ArrayList;
import java.util.List;

public class Settings_Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings__main);

        /*From Here to Line 56 is me trying to hardcode a user so I can manipulate the data!*/
        List<String> ingredientsNo = new ArrayList<String>();
        ingredientsNo.add("nuts");
        ingredientsNo.add("almonds");
        ingredientsNo.add("chicken");
        List<String> ingredientsYes = new ArrayList<String>();
        ingredientsYes.add("chocolate");
        ingredientsYes.add("icecream");
        ingredientsYes.add("vanilla");
        ArrayList<Integer> ingredientsYesGoalsQty = new ArrayList<Integer>();
        ingredientsYesGoalsQty.add(1);
        ingredientsYesGoalsQty.add(2);
        ingredientsYesGoalsQty.add(3);
        ArrayList<Integer> ingredientsYesTrackedQty = new ArrayList<Integer>();
        ingredientsYesTrackedQty.add(1);
        ingredientsYesTrackedQty.add(2);
        ingredientsYesTrackedQty.add(3);

        User User = new User();
        User.setId(10002);
        User.setEmail("anh.nguyen2@mav.uta.edu");
        User.setPassword("myPassWo@rd");
        User.setFirstName("Anh");
        User.setSecondName("Nguyen");
        User.setGender("m");
        User.setAge(69);
        User.setWeight(169);
        User.setIngredientsNo(ingredientsNo);
        User.setIngredientsYes(ingredientsYes);
        User.setIngredientsYesGoalsQty(ingredientsYesGoalsQty);
        User.setIngredientsYesTrackedQty(ingredientsYesTrackedQty);
        User.setCalorieGoalsQty(2000);
        User.setcalorieTrackedQty(2000);
        /*End of Creating User*/

        Button btn2EditProfile = (Button)findViewById(R.id.editProfileBtn);                                     //Button to go to EditProfile page
        btn2EditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings_Main.this, Settings_EditProfile.class));
            }
        });

        Button btn2EditAllergen = (Button)findViewById(R.id.editAllergenBtn);                                   //Button to go to EditAllergen page
        btn2EditAllergen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings_Main.this, Settings_EditAllergen.class));

            }
        });

        Button btn2Main = (Button)findViewById(R.id.cancel2MainBtn);                                            //Button to go back to MainDashboard
        btn2Main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings_Main.this, DashActivity.class));
            }
        });

        Button deleteBtn = (Button)findViewById(R.id.delete_acc);                                            //Button to go to delete account
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings_Main.this, DashActivity.class));
            }
        });

    }
}