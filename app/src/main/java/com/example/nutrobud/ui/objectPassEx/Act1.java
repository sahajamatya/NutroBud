package com.example.nutrobud.ui.objectPassEx;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.nutrobud.R;

public class Act1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act1);

        final EditText firstNameTxt = findViewById(R.id.userExFirstName);
        final EditText secondNameTxt = findViewById(R.id.userExSecondName);
        Button submitBtn = findViewById(R.id.userExBtn1);

        //Don't worry about the "final" declaration. I only had to put final before instantiating
        //UserEx because it is being accessed by an inner class.
        final UserEx myUser = new UserEx();

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myUser.setFirstName(firstNameTxt.getText().toString());
                myUser.setSecondName(secondNameTxt.getText().toString());
                //At this point, I have populated my UserEx object, i.e., myUser, with the data the user has input.


                Intent intent = new Intent(getApplicationContext(), Act2.class);
                intent.putExtra("anyNameYouWant", myUser);
                startActivity(intent);
            }
        });
    }
}