package com.example.nutrobud;


import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nutrobud.ui.home.User;


public class SignUpLoginInfo extends AppCompatActivity {

    EditText emailtext, passwordtext, passwordtext2;
    Button nextbtn, backbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_login_info);

        //Get user variable from previous activity
        final User user = getIntent().getParcelableExtra("User");

        emailtext = findViewById(R.id.EmailText);
        passwordtext = findViewById(R.id.PasswordText);
        passwordtext2 = findViewById(R.id.PasswordText2);
        nextbtn = findViewById(R.id.NextBtn);
        backbtn = findViewById(R.id.BackBtn);

        //Check if a user has already entered in an email
        if(user.getEmail() != null)
        {
            //Set text to previously entered email
            emailtext.setText(user.getEmail(), TextView.BufferType.EDITABLE);
        }

        //Check if user has already entered in a password
        if(user.getPassword() != null) {
            //Set text to previously entered password
            passwordtext.setText(user.getPassword(), TextView.BufferType.EDITABLE);
            passwordtext2.setText(user.getPassword(), TextView.BufferType.EDITABLE);
        }

        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email = emailtext.getText().toString().trim();
                String Password = passwordtext.getText().toString().trim();
                String Password2 = passwordtext2.getText().toString().trim();

                //Set error if email is empty
                if (TextUtils.isEmpty(Email)) {
                    emailtext.setError("Email is required");
                    return;
                } else {
                    //Store email in User class
                    user.setEmail(Email);
                }
                //Set error is password is empty
                if(TextUtils.isEmpty(Password)) {
                    passwordtext.setError("Password is required");
                    return;
                } else if(Password.length() < 6) {
                    //Error if password is less that 6 characters
                    passwordtext.setError("Password must be more that 5 characters long");
                    return;
                } else if (!Password.equals(Password2)){
                    //Set errors if both password fields do not match
                    passwordtext.setError("Passwords do not match");
                    passwordtext2.setError("Passwords do not match");
                    return;
                } else{
                    //Store password in User class
                    user.setPassword(Password);
                }

                //Pass intent and user to next activity
                Intent i = new Intent(getApplicationContext(), SignUp1.class);
                i.putExtra("User", user);
                startActivity(i);
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //If the user backs out to the login screen, all information previously typed into user will
                //be deleted
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });
    }
}