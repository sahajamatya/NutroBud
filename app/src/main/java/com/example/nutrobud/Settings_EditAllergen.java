package com.example.nutrobud;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nutrobud.R;
import com.example.nutrobud.ui.home.User;

import java.util.ArrayList;
import java.util.List;

/*
   DISCLAIMER, CODE WRITTEN ON THIS FILE WAS PARTIALLY TAKEN FROM LYDIA SARVER
*/
public class Settings_EditAllergen extends AppCompatActivity {
    ArrayList<String> ingredient_no = new ArrayList<>();    //initializing variables
    TextView  editAllergen;
    Button addnoBtn;
    Button[] btnArr = new Button[15];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings__edit_allergen);
        final int[] i = new int[1]; //This is to make i work for both instances of button

        final LinearLayout linLayout = findViewById(R.id.linLayout);    //add buttons to this badboy
        editAllergen = findViewById(R.id.editAllergenText);

        List<String> ingredientsNo = new ArrayList<String>();               //HARDCODING FOR NOW
        ingredientsNo.add("nuts");
        ingredientsNo.add("almonds");
        ingredientsNo.add("chicken");

        ingredient_no = (ArrayList<String>) ingredientsNo; //Import Users list

        if (ingredient_no.size() > 0) {                                 //make buttons for all ingredient no
            for (i[0] = 0; i[0] < ingredient_no.size(); i[0]++){
                String str = ingredient_no.get(i[0]);
                btnArr[i[0]] = new Button(this);
                btnArr[i[0]].setText(str);
                btnArr[i[0]].setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                btnArr[i[0]].setCompoundDrawablesWithIntrinsicBounds(0,0,android.R.drawable.ic_menu_delete,0);
                linLayout.addView(btnArr[i[0]]);
                final int finalI = i[0];
                btnArr[i[0]].setOnClickListener(new View.OnClickListener() {    //if clicked, delete the button
                    @Override
                    public void onClick(View v) {
                        btnArr[finalI].setVisibility(View.GONE);
                        i[0]--;
                    }
                });
            }

        }

        addnoBtn = (Button) findViewById(R.id.addBtn);   //addnoBtn is the add button
        addnoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Item = editAllergen.getText().toString().trim();
                int duplicate = 0;

                //Check if Item is already in list of unwanted ingredients
                for (int j = 0; j < ingredient_no.size(); j++) {
                    if (Item.equals(ingredient_no.get(j)))
                        duplicate = 1;
                }

                if (duplicate == 0) {           //if not duplicate, create button for that item
                    ingredient_no.add(Item);

                    btnArr[i[0]] = new Button(btnArr[i[0]].getContext());
                    btnArr[i[0]].setText(Item);
                    btnArr[i[0]].setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                    btnArr[i[0]].setCompoundDrawablesWithIntrinsicBounds(0,0,android.R.drawable.ic_menu_delete,0);
                    btnArr[i[0]].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ViewGroup layout = (ViewGroup) btnArr[i[0]].getParent();
                            btnArr[i[0]].setVisibility(View.GONE);
//                            i[0]--;
                        }
                    });
                    linLayout.addView(btnArr[i[0]]);
//                    i[0]++;

                } else if (duplicate == 1) {
                    Toast.makeText(Settings_EditAllergen.this, "Duplicate ingredient", Toast.LENGTH_SHORT).show();
                }
            }
        }); //end of add button function

        //NOT DONE, NEED TO WORK ON UPDATING DB WITH NEW LIST OF ALLERGEN

    } //end of onCreate
}