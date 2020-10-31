package com.example.nutrobud;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SignUp2 extends AppCompatActivity {
    EditText weight, ing_yes, ing_no;
    TextView viewno, viewyes;
    Button nextbtn, backbtn, addnobtn, rmvnobtn, addyesbtn, rmvyesbtn;
    ArrayList<String> ingredient_no = new ArrayList<>();
    ArrayList<String> ingredient_yes = new ArrayList<>();
    ArrayList<String> clear = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);

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
                startActivity(new Intent(getApplicationContext(), SignUpGoals.class));
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SignUp1.class));
            }
        });

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


                if (duplicate == 0 && otherList == 0) {
                    ingredient_no.add(Item);

                    //Getting output for ingredients the user does not want
                    String output = ingredient_no.get(0);
                    if (ingredient_no.size() > 1) {
                        for (int i = 1; i < ingredient_no.size(); i++)
                            output = output + ", " + ingredient_no.get(i);
                    }

                    viewno.setText(output);
                } else if (duplicate == 1) {
                    Toast.makeText(SignUp2.this, "Duplicate ingredient", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignUp2.this, "That's already in the other list! Remove it before adding here.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        rmvnobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Item = ing_no.getText().toString().trim();
                int found = 0;

                //Checking if requested item to remove is in the array
                for (int j = 0; j < ingredient_no.size(); j++) {
                    if (Item.equals(ingredient_no.get(j)))
                        ingredient_no.remove(j);
                    found = 1;
                }

                if (found == 1) {
                    //Getting output for ingredients the user does not want
                    String output = new String();

                    if(ingredient_no.size() > 0) {
                        output = ingredient_no.get(0);
                        if (ingredient_no.size() > 1) {
                            for (int i = 1; i < ingredient_no.size(); i++)
                                output = output + ", " + ingredient_no.get(i);
                        }
                    }

                    Toast.makeText(SignUp2.this, "Ingredient removed", Toast.LENGTH_SHORT).show();
                    viewno.setText(output);
                } else {
                    Toast.makeText(SignUp2.this, "Ingredient not found in current list", Toast.LENGTH_SHORT).show();
                }
            }
        });

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


                if (duplicate == 0 && otherList == 0) {
                    ingredient_yes.add(Item);
                    //Getting output for ingredients the user does not want
                    String output = ingredient_yes.get(0);
                    if (ingredient_yes.size() > 1) {
                        for (int i = 1; i < ingredient_yes.size(); i++)
                            output = output + ", " + ingredient_yes.get(i);
                    }

                    viewyes.setText(output);
                } else if (duplicate == 1) {
                    Toast.makeText(SignUp2.this, "Duplicate nutrient or vitamin", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignUp2.this, "That's already in the other list! Remove it before adding here.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        rmvyesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Item = ing_yes.getText().toString().trim();
                int found = 0;

                //Checking if requested item to remove is in the array
                for (int j = 0; j < ingredient_yes.size(); j++) {
                    if (Item.equals(ingredient_yes.get(j))) {
                        ingredient_yes.remove(j);
                        found = 1;
                    }
                }

                if (found == 1) {
                    //Getting output for nutrient or vitamin the user does not want
                    String output = new String();

                    if(ingredient_yes.size() > 0) {
                        output = ingredient_yes.get(0);
                        if (ingredient_yes.size() > 1) {
                            for (int i = 1; i < ingredient_yes.size(); i++)
                                output = output + ", " + ingredient_yes.get(i);
                        }
                    }

                    Toast.makeText(SignUp2.this, "Nutrient or vitamin removed", Toast.LENGTH_SHORT).show();
                    viewyes.setText(output);
                } else {
                    Toast.makeText(SignUp2.this, "Nutrient or vitamin not found in current list", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}