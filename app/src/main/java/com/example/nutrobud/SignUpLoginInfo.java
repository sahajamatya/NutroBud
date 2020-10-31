package com.example.nutrobud;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpLoginInfo extends AppCompatActivity {

    EditText emailtext, passwordtext;
    Button nextbtn, backbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_login_info);

        emailtext = findViewById(R.id.EmailText);
        passwordtext = findViewById(R.id.PasswordText);
        nextbtn = findViewById(R.id.NextBtn);
        backbtn = findViewById(R.id.BackBtn);

        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Email = emailtext.getText().toString().trim();
                String Password = passwordtext.getText().toString().trim();

                if (TextUtils.isEmpty(Email)) {
                    emailtext.setError("Email is required");
                    return;
                } else if (TextUtils.isEmpty(Password)) {
                    passwordtext.setError("Password is required");
                    return;
                }

                startActivity(new Intent(getApplicationContext(), SignUp1.class));
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });
    }
}