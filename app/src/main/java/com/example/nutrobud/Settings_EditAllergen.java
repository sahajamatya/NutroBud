package com.example.nutrobud;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nutrobud.R;

import java.util.ArrayList;

public class Settings_EditAllergen extends AppCompatActivity {
    ArrayList<String> ingredient_no = new ArrayList<>();
    TextView viewno, editAllergen;
    Button addnoBtn, rmNoBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings__edit_allergen);

        editAllergen = findViewById(R.id.editAllergenText);
        viewno = findViewById(R.id.textView2);      //viewno is the textview inside scrollview
        ingredient_no.add("Nuts");                  //Hardcoding array for testing
        ingredient_no.add("Milk");
        ingredient_no.add("Apple");
        ingredient_no.add("Peanuts");
        ingredient_no.add("Dairy");

        String output = ingredient_no.get(0);      //output is what will be printed inside scrollview
        if (ingredient_no.size() > 1) {
            for (int i = 1; i < ingredient_no.size(); i++)
                output = output + "\n" + ingredient_no.get(i);
        }

        viewno.setText((output));       //printing output to scrollview

        addnoBtn = (Button) findViewById(R.id.addBtn);   //addnoBtn is the add button
        addnoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Item = editAllergen.getText().toString().trim();
                int duplicate = 0;
                int otherList = 0;

                //Check if Item is already in list of unwanted ingredients
                for (int j = 0; j < ingredient_no.size(); j++) {
                    if (Item.equals(ingredient_no.get(j)))
                        duplicate = 1;
                }


                if (duplicate == 0 && otherList == 0) {
                    ingredient_no.add(Item);

                    //Getting output for ingredients the user does not want
                    String output = ingredient_no.get(0);
                    if (ingredient_no.size() > 1) {
                        for (int i = 1; i < ingredient_no.size(); i++)
                            output = output + "\n" + ingredient_no.get(i);
                    }

                    viewno.setText(output);
                    editAllergen.setText("");
                } else if (duplicate == 1) {
                    Toast.makeText(Settings_EditAllergen.this, "Duplicate ingredient", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Settings_EditAllergen.this, "That's already in the other list! Remove it before adding here.", Toast.LENGTH_SHORT).show();
                }
            }
        }); //end of add button function

        rmNoBtn = (Button) findViewById(R.id.rmBtn);   //addnoBtn is the remove button
        rmNoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Item = editAllergen.getText().toString().trim();
                int found=0;

                for (int j = 0; j < ingredient_no.size(); j++) {
                    if (Item.equals(ingredient_no.get(j)))
                    {
                        ingredient_no.remove(j);
                        found = 1;
                    }
                }
                if (found == 1) {
                    //Getting output for ingredients the user does not want
                    String output = new String();

                    if(ingredient_no.size() > 0) {
                        output = ingredient_no.get(0);
                        if (ingredient_no.size() > 1) {
                            for (int i = 1; i < ingredient_no.size(); i++)
                                output = output + "\n" + ingredient_no.get(i);
                        }
                    }

                    Toast.makeText(Settings_EditAllergen.this, "Ingredient removed", Toast.LENGTH_SHORT).show();
                    viewno.setText(output);
                } else {
                    Toast.makeText(Settings_EditAllergen.this, "Ingredient not found in current list", Toast.LENGTH_SHORT).show();
                }
                editAllergen.setText("");
            }
        }); //end of remove button function
    }
}