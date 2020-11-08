package com.example.nutrobud;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.nutrobud.ui.home.User;

public class SignUp1 extends AppCompatActivity {

    RadioGroup Gender;
    RadioButton radioButton;
    EditText name_first, name_last, age;
    Button nextbtn, backbtn;

    Intent i = getIntent();
    User user = (User)i.getSerializableExtra("User");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up1);

        name_first = findViewById(R.id.Name_First);
        name_last = findViewById(R.id.Name_Last);
        age = findViewById(R.id.Age);
        Gender = findViewById(R.id.Gender);
        nextbtn = findViewById(R.id.NextBtn);
        backbtn = findViewById(R.id.BackBtn);

        //use .setHint once User class is made to store name input for user is back btn is used

        nextbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Store in User
                String FirstName = name_first.getText().toString().trim();
                String LastName = name_last.getText().toString().trim();
                String Age_s = age.getText().toString().trim();

                if (TextUtils.isEmpty(FirstName)) {
                    name_first.setError("First name is required");
                    return;
                }else{
                    user.setFirstName(FirstName);
                }
                if (TextUtils.isEmpty(LastName)) {
                    name_last.setError("Last name is required");
                    return;
                }else{
                    user.setSecondName(LastName);
                }
                if (TextUtils.isEmpty(Age_s)) {
                    age.setError("Age is required");
                    return;
                }
                else {
                    user.setAge(Integer.parseInt(Age_s));
                }

                startActivity(new Intent(SignUp1.this, SignUp2.class));
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUp1.this, SignUpLoginInfo.class));
            }
        });
    }

    public void checkButton(View v){
        int radioId = Gender.getCheckedRadioButtonId();

        radioButton = findViewById(radioId);

        //Set gender - change to save in User later
        if(radioButton.getText() == "Male") {
            user.setGender("Male");
        }else if(radioButton.getText() == "Female"){
            user.setGender("Female");
        }else{
            user.setGender("Other");
        }
    }
}