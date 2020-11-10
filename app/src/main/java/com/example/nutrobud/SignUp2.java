package com.example.nutrobud;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nutrobud.ui.home.User;

import java.util.ArrayList;
import java.util.List;

public class SignUp2 extends AppCompatActivity {
    EditText weight, ing_yes, ing_no;
    TextView viewno, viewyes;
    Button nextbtn, backbtn, addnobtn, rmvnobtn, addyesbtn, rmvyesbtn;
    List<String> ingredient_no = new ArrayList<>();
    List<String> ingredient_yes = new ArrayList<>();
    List<String> clear = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);

        //Get user and it's stored data from the previous activity
        final User user = getIntent().getParcelableExtra("User");

        weight = findViewById(R.id.Weight);
        ing_no = findViewById((R.id.Ingredient_no));
        ing_yes = findViewById(R.id.Ingredient_yes);
        viewno = findViewById(R.id.ViewNo);
        viewyes = findViewById(R.id.ViewYes);
        nextbtn = findViewById(R.id.NextBtn);
        addnobtn = findViewById(R.id.AddNoBtn);
        addyesbtn = findViewById(R.id.AddYesBtn);
        backbtn = findViewById(R.id.BackBtn);
        rmvnobtn = findViewById(R.id.rmvNoBtn);
        rmvyesbtn = findViewById(R.id.rmvYesBtn);

        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String weight_s = weight.getText().toString().trim();

                //If a weight was entered, change it to an Integer and save in user
                if(!TextUtils.isEmpty(weight_s)) {
                    user.setWeight(Integer.parseInt(weight_s));
                }else{
                    //If a weight was not entered, store a -1 to denote that
                    user.setWeight(-1);
                }

                //Store user entered lists before changing activities
                user.setIngredientsNo(ingredient_no);
                user.setIngredientsYes(ingredient_yes);

                //Pass intent and user to the previous activity
                Intent i = new Intent(getApplicationContext(), SignUpGoals.class);
                i.putExtra("User", user);
                startActivity(i);
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Pass intent and user to the previous activity
                Intent i = new Intent(getApplicationContext(), SignUp1.class);
                i.putExtra("User", user);
                startActivity(i);
            }
        });

        //Add unwanted ingredient
        addnobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Item = ing_no.getText().toString().trim();
                int duplicate = 0;
                int otherList = 0;

                //Check if Item is already in list of unwanted ingredients
                for (int j = 0; j < ingredient_no.size(); j++) {
                    if (Item.equals(ingredient_no.get(j)))
                        duplicate = 1;
                }
                //Check for user trying to add something to both lists
                for (int j = 0; j < ingredient_yes.size(); j++) {
                    boolean match = Item.equals(ingredient_yes.get(j));
                    if (Item.equals(ingredient_yes.get(j)))
                        otherList = 1;
                }

                //Check if either errors were set
                if (duplicate == 0 && otherList == 0) {
                    //If no issue, add the ingredient to the unwanted list
                    ingredient_no.add(Item);

                    //Getting and formatting output for ingredients the user does not want
                    String output = ingredient_no.get(0);
                    if (ingredient_no.size() > 1) {
                        for (int i = 1; i < ingredient_no.size(); i++)
                            output = output + ", " + ingredient_no.get(i);
                    }

                    //Clear user input text for next input
                    ing_no.getText().clear();
                    //Show the formatted output to user
                    viewno.setText(output);
                } else if (duplicate == 1) {
                    //Error message
                    Toast.makeText(SignUp2.this, "Duplicate ingredient", Toast.LENGTH_SHORT).show();
                } else {
                    //Error message
                    Toast.makeText(SignUp2.this, "That's already in the other list! Remove it before adding here.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Remove unwanted ingredient
        rmvnobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Item = ing_no.getText().toString().trim();
                int found = 0;

                //Checking if requested item to remove is in the array
                for (int j = 0; j < ingredient_no.size(); j++) {
                    //If the ingredient is found, it weill be removed here
                    if (Item.equals(ingredient_no.get(j))) {
                        ingredient_no.remove(j);
                        found = 1;
                    }
                }

                //Checking found was made true
                if (found == 1) {
                    //Getting/formatting new output for ingredients the user does not want
                    String output = new String();

                    if(ingredient_no.size() > 0) {
                        output = ingredient_no.get(0);
                        if (ingredient_no.size() > 1) {
                            for (int i = 1; i < ingredient_no.size(); i++)
                                output = output + ", " + ingredient_no.get(i);
                        }
                    }
                    //Successful message
                    Toast.makeText(SignUp2.this, "Ingredient removed", Toast.LENGTH_SHORT).show();
                    //Clear user input text for next input
                    ing_no.getText().clear();
                    //Display new list to user
                    viewno.setText(output);
                } else {
                    //Error message
                    Toast.makeText(SignUp2.this, "Ingredient not found in current list", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Add wanted vitamin or nutrient
        addyesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Item = ing_yes.getText().toString().trim();
                int duplicate = 0;
                int otherList = 0;

                //Check for duplicate entries within the nutrient or vitamin wanted
                for (int j = 0; j < ingredient_yes.size(); j++) {
                    if (Item.equals(ingredient_yes.get(j)))
                        duplicate = 1;
                }

                //Check for user trying to add something to both lists
                for (int j = 0; j < ingredient_no.size(); j++) {
                    boolean match = Item.equals(ingredient_no.get(j));
                    if (Item.equals(ingredient_no.get(j)))
                        otherList = 1;
                }

                //Check if either error was set
                if (duplicate == 0 && otherList == 0) {
                    //If no errors, add item to the list
                    ingredient_yes.add(Item);

                    //Getting/formatting the output for vitamins and nutrients the user does want
                    String output = ingredient_yes.get(0);
                    if (ingredient_yes.size() > 1) {
                        for (int i = 1; i < ingredient_yes.size(); i++)
                            output = output + ", " + ingredient_yes.get(i);
                    }

                    //Clear user input text for next input
                    ing_yes.getText().clear();
                    //Show the new output to the user
                    viewyes.setText(output);
                } else if (duplicate == 1) {
                    //Error message
                    Toast.makeText(SignUp2.this, "Duplicate nutrient or vitamin", Toast.LENGTH_SHORT).show();
                } else {
                    //Error message
                    Toast.makeText(SignUp2.this, "That's already in the other list! Remove it before adding here.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //Remove an item from the wanted vitamins and nutrients list
        rmvyesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Item = ing_yes.getText().toString().trim();
                int found = 0;

                //Checking if requested item to remove is in the array
                for (int j = 0; j < ingredient_yes.size(); j++) {
                    if (Item.equals(ingredient_yes.get(j))) {
                        //If found, remove item
                        ingredient_yes.remove(j);
                        found = 1;
                    }
                }

                //Check if found is ture
                if (found == 1) {
                    //Getting/formatting new output for nutrient or vitamin the user does want
                    String output = new String();

                    if(ingredient_yes.size() > 0) {
                        output = ingredient_yes.get(0);
                        if (ingredient_yes.size() > 1) {
                            for (int i = 1; i < ingredient_yes.size(); i++)
                                output = output + ", " + ingredient_yes.get(i);
                        }
                    }

                    //Success message
                    Toast.makeText(SignUp2.this, "Nutrient or vitamin removed", Toast.LENGTH_SHORT).show();
                    //Clear user input text for next input
                    ing_yes.getText().clear();
                    //Show new output to user
                    viewyes.setText(output);
                } else {
                    //Error message
                    Toast.makeText(SignUp2.this, "Nutrient or vitamin not found in current list", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}