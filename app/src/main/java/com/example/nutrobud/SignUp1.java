package com.example.nutrobud;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nutrobud.ui.home.User;

import org.w3c.dom.Text;


public class SignUp1 extends AppCompatActivity {

    RadioGroup rgGender;
    RadioButton radioButton, buttonM, buttonF, buttonO;
    EditText name_first, name_last, age;
    Button nextbtn, backbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up1);

        //Get user and it's stored data from the previous activity
        final User user = getIntent().getParcelableExtra("User");
        user.setId(1); //Set place holder. Will be changed in the review portion

        name_first = findViewById(R.id.Name_First);
        name_last = findViewById(R.id.Name_Last);
        age = findViewById(R.id.Age);
        rgGender = findViewById(R.id.Gender);
        nextbtn = findViewById(R.id.NextBtn);
        backbtn = findViewById(R.id.BackBtn);
        buttonM = findViewById(R.id.Gender_Male);
        buttonF = findViewById(R.id.Gender_Female);
        buttonO = findViewById(R.id.Gender_Other);

        //Check if user has previously entered a first name
        if (user.getFirstName() != null) {
            //Set text to previously entered first name
            name_first.setText(user.getFirstName(), TextView.BufferType.EDITABLE);
        }
        //Check if user has previously entered a last name
        if (user.getSecondName() != null) {
            //Set text to previously entered first name
            name_last.setText(user.getSecondName(), TextView.BufferType.EDITABLE);
        }
        //Check if user have previously entered age (will be -1 if no age is entered
        if(user.getAge() != -1)
        {
            //Set text to previously entered age
            String age_s = Integer.toString(user.getAge());
            age.setText(age_s, TextView.BufferType.EDITABLE);
        }
        //Check if user has previously selected a gender
        if (user.getGender() != null) {
            String gender_s = user.getGender();
            buttonM.setChecked(false);
            buttonF.setChecked(false);
            buttonO.setChecked(false);
            //Check if user has previously entered a gender
            switch (gender_s) {
                case "Male":
                    buttonM.setChecked(true);
                    break;
                case "Female":
                    buttonF.setChecked(true);
                    break;
                case "Other":
                    buttonO.setChecked(true);
                    break;
            }
        }
        //If not gender has been selected yet, the gender will be automatically set to "Other"

        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String FirstName = name_first.getText().toString().trim();
                String LastName = name_last.getText().toString().trim();
                String Age_s = age.getText().toString().trim();

                //Set error if first name is empty
                if (TextUtils.isEmpty(FirstName)) {
                    name_first.setError("First name is required");
                    return;
                } else {
                    //Store in user
                    user.setFirstName(FirstName);
                }
                //Set error if last name is empty
                if (TextUtils.isEmpty(LastName)) {
                    name_last.setError("Last name is required");
                    return;
                } else {
                    //Store in user
                    user.setSecondName(LastName);
                }
                //Set error if age is empty
                if (TextUtils.isEmpty(Age_s)) {
                    age.setError("Age is required");
                    return;
                } else if(Integer.parseInt(Age_s) < 1) {
                    //Set error if user enters an age below 1
                    age.setError("Not a valid age");
                } else {
                    //Store in user
                    user.setAge(Integer.parseInt(Age_s));
                }

                //Pass intent and user to the next activity
                Intent i = new Intent(getApplicationContext(), SignUp2.class);
                i.putExtra("User", user);
                startActivity(i);
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Pass intent and user to the previous activity
                Intent i = new Intent(getApplicationContext(), SignUpLoginInfo.class);
                i.putExtra("User", user);
                startActivity(i);
            }
        });

        //Currently not working
      rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(RadioGroup group, int checkedId) {
              int radioId = rgGender.getCheckedRadioButtonId();
              buttonM.setChecked(false);
              buttonF.setChecked(false);
              buttonO.setChecked(false);

              switch (radioId) {
                  case R.id.Gender_Male:
                      user.setGender("Male");
                      buttonM.setChecked(true);
                      break;
                  case R.id.Gender_Female:
                      user.setGender("Female");
                      buttonF.setChecked(true);
                      break;
                  case R.id.Gender_Other:
                      user.setGender("Other");
                      buttonO.setChecked(true);
                      break;
              }
          }
      });
    }
}

