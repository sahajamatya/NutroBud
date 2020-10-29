package com.example.nutrobud;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class SignUpReview extends AppCompatActivity {

    Button okaybtn;
    ListView reviewview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_review);

        okaybtn = findViewById(R.id.OkayBtn);
        reviewview = findViewById(R.id.ReviewView);

        okaybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), DashActivity.class));
            }
        });
    }
}