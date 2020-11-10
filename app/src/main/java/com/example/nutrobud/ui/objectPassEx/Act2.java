package com.example.nutrobud.ui.objectPassEx;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.nutrobud.R;

public class Act2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act2);

        final TextView firstNameTextView = findViewById(R.id.userExFirstNameAct2);
        final TextView secondNameTextView = findViewById(R.id.userExSecondNameAct2);
        final EditText emailTxt = findViewById(R.id.userExEmail);
        final EditText passwordTxt = findViewById(R.id.userExPassword);
        Button submitBtn = findViewById(R.id.userExBtn2);

        final UserEx myUserFromAct1 = getIntent().getParcelableExtra("anyNameYouWant");//make sure the name matches from what you set it in Act1

        firstNameTextView.setText("First Name from ACT I: "+myUserFromAct1.getFirstName());
        secondNameTextView.setText("Second Name from ACT II: "+myUserFromAct1.getSecondName());

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myUserFromAct1.setEmail(emailTxt.getText().toString());
                myUserFromAct1.setPassword(passwordTxt.getText().toString());
                //At this point, I have populated my UserEx object, i.e., myUser, with the data the user has input.


                Intent intent = new Intent(getApplicationContext(), Act3.class);
                intent.putExtra("alsoAnyNameYouWant", myUserFromAct1);
                startActivity(intent);
            }
        });
    }
}