package com.example.nutrobud;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.nutrobud.ui.home.User;

import java.util.ArrayList;
import java.util.List;

public class Settings_Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings__main);

        /*From Here to Line 42 (Log.d function) is me trying to hardcode a user so I can manipulate the data!*/
        List<String> ingredientsNo = new ArrayList<String>();
        ingredientsNo.add("nuts");
        ingredientsNo.add("almonds");
        ingredientsNo.add("chicken");
        List<String> ingredientsYes = new ArrayList<String>();
        ingredientsYes.add("chocolate");
        ingredientsYes.add("icecream");
        ingredientsYes.add("vanilla");
        List<Integer> ingredientsYesGoalsQty = new ArrayList<Integer>();
        ingredientsYesGoalsQty.add(1);
        ingredientsYesGoalsQty.add(2);
        ingredientsYesGoalsQty.add(3);
        List<Integer> ingredientsYesTrackedQty = new ArrayList<Integer>();
        ingredientsYesTrackedQty.add(1);
        ingredientsYesTrackedQty.add(2);
        ingredientsYesTrackedQty.add(3);
        new User(10002, "anh.nguyen2@mav.uta.edu", "myPassWo@rd", "Anh", "Nguyen", 69, "m", 169, ingredientsNo, ingredientsYes, ingredientsYesGoalsQty, ingredientsYesTrackedQty, 2000, 2000);
        int id = User.getId();
        Log.d("Id",id+"");
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