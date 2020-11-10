package com.example.nutrobud.ui.objectPassEx;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.nutrobud.R;

import org.w3c.dom.Text;

public class Act3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act3);

        final UserEx myUserFromAct2 = getIntent().getParcelableExtra("alsoAnyNameYouWant");

        TextView firstName = findViewById(R.id.userExResultFirstName);
        TextView secondName = findViewById(R.id.userExResultSecondName);
        TextView email = findViewById(R.id.userExResultEmail);
        TextView password = findViewById(R.id.userExResultPassword);

        firstName.setText("First Name: "+myUserFromAct2.getFirstName());
        secondName.setText("Second Name: "+myUserFromAct2.getSecondName());
        email.setText("Email: "+myUserFromAct2.getEmail());
        password.setText("Password: "+myUserFromAct2.getPassword());
    }
}