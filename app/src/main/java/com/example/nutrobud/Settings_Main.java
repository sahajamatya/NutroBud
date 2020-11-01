package com.example.nutrobud;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Settings_Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings__main);

        Button btn = (Button)findViewById(R.id.editProfileBtn); //Button to go to EditProfile page
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings_Main.this, Settings_EditProfile.class));
            }
        });

        Button btn2 = (Button)findViewById(R.id.editAllergenBtn); //Button to go to EditAllergen page
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings_Main.this, Settings_EditAllergen.class));

            }
        });

        Button btn3 = (Button)findViewById(R.id.cancel2MainBtn); //Button to go back to MainDashboard
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings_Main.this, DashActivity.class));
            }
        });
    }
}