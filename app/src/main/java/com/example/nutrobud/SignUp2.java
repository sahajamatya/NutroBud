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

import java.util.ArrayList;

public class SignUp2 extends AppCompatActivity {

    EditText weight, ing_yes, ing_no;
    TextView viewno, viewyes;
    Button nextbtn, addnobtn, addyesbtn;
    ArrayList<String> ingredient_no = new ArrayList<>();
    ArrayList<String> Ingredient_yes = new ArrayList<>();
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

        nextbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(getApplicationContext(), SignUpGoals.class));
            }
        });

        addnobtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String Item = ing_no.getText().toString().trim();
                ingredient_no.add(Item);
                int i;

                System.out.print(ingredient_no);

                //Getting output for ingredients the user does not want
                String output = ingredient_no.get(0);
                if(ingredient_no.size() > 1) {
                    for (i = 1; i < ingredient_no.size() - 1; i++)
                        output = output + ", " + ingredient_no.get(i);
                    output = ingredient_no.get(i + 1);
                }

                viewno.setText(output);
            }
        });
    }


}