package com.example.nutrobud;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nutrobud.DashActivity;
import com.example.nutrobud.Dashboard;
import com.example.nutrobud.R;

import org.w3c.dom.Text;

public class Login extends AppCompatActivity {

    EditText EmailText, PasswordText;
    Button loginbtn, signupbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EmailText = findViewById(R.id.email);
        PasswordText = findViewById(R.id.password);
        loginbtn = findViewById(R.id.LoginBtn);
        signupbtn = findViewById(R.id.SignUpBtn);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = EmailText.getText().toString().trim();
                String Password = PasswordText.getText().toString().trim();

                if (TextUtils.isEmpty(Email)) {
                    EmailText.setError("Email is required");
                    return;
                }

                if (TextUtils.isEmpty(Password)) {
                    PasswordText.setError("Password is required");
                    return;
                }

                //Connect Firebase Authenicator here to check the email and password using .getText()
                //Hardcoding until then to prove working...
                if (Email.equals("NutroBud") && Password.equals("123456")) {
                    Toast.makeText(Login.this, "Log in Successful!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), DashActivity.class));
                } else {
                    Toast.makeText(Login.this, "Your email or password is incorrect", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(getApplicationContext(), DashActivity.class));
                }
            }
        });

        signupbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(getApplicationContext(), SignUpLoginInfo.class));
            }
        });
    }
}
