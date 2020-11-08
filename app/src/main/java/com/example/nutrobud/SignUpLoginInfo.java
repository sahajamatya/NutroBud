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
    User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_login_info);

        emailtext = findViewById(R.id.EmailText);
        passwordtext = findViewById(R.id.PasswordText);
        passwordtext2 = findViewById(R.id.PasswordText2);
        nextbtn = findViewById(R.id.NextBtn);
        backbtn = findViewById(R.id.BackBtn);

        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check if a user has already entered in an email
                if(user.getEmail() != null)
                {
                    emailtext.setText(user.getEmail(), TextView.BufferType.EDITABLE);
                }

                passwordtext.setText(user.getPassword(), TextView.BufferType.EDITABLE);
                passwordtext2.setText(user.getPassword(), TextView.BufferType.EDITABLE);

                String Email = emailtext.getText().toString().trim();
                String Password = passwordtext.getText().toString().trim();
                String Password2 = passwordtext2.getText().toString().trim();

                if (TextUtils.isEmpty(Email)) {
                    emailtext.setError("Email is required");
                    return;
                } else {
                    user.setEmail(Email);
                }
                if(TextUtils.isEmpty(Password)) {
                    passwordtext.setError("Password is required");
                    return;
                } else if(Password.length() < 6) {
                    passwordtext.setError("Password must be more that 5 characters long");
                    return;
                } else if (!Password.equals(Password2)){
                    passwordtext.setError("Passwords do not match");
                    passwordtext2.setError("Passwords do not match");
                    return;
                } else{
                    user.setPassword(Password);
                }

                Intent i = new Intent(SignUpLoginInfo.this, SignUp1.class);
                i.putExtra("User", (Parcelable) user);
                startActivity(i);
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