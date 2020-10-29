package com.example.nutrobud;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SignUpGoals extends AppCompatActivity {

    EditText goaltext, daily_cal_text;
    TextView nvview;
    LinearLayout linlayout;
    Button savebtn, cancelbtn, reviewbtn, backbtn;
    PopupWindow goalPopUp;

    ListView nutrientview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_goals);

        nutrientview = findViewById(R.id.NutrientView);
        linlayout = findViewById(R.id.LinearLayout);
        daily_cal_text = findViewById(R.id.Daily_Cal_Text);
        reviewbtn = findViewById(R.id.ReviewBtn);
        backbtn = findViewById(R.id.BackBtn);

        //This will be from user. Hard coded for now to show usability
        final ArrayList<String> ingredient_yes = new ArrayList<>();
        ingredient_yes.add("A");
        ingredient_yes.add("C");
        ingredient_yes.add("Iron");

        //This will also be from user
        final ArrayList<Integer> ing_yes_count = new ArrayList<>();
        //Make ing_yes_count the same size as ingrerdient_yes so indexes match up
        for(int i = 0; i < ingredient_yes.size(); i++)
        {
            //-1 means no goal set
            ing_yes_count.add(-1);
        }

        final ArrayList<String> Output = new ArrayList<>();
        String temp = new String();
        for(int i = 0; i < ingredient_yes.size(); i++)
        {
            temp = ingredient_yes.get(i) + " | No daily goal set";
            Output.add(temp);
        }

        final ArrayAdapter outputAdapted = new ArrayAdapter(this, android.R.layout.simple_list_item_1, Output);

        nutrientview.setAdapter(outputAdapted);

        nutrientview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //goaltext.onEditorAction(EditorInfo.IME_ACTION_DONE);

                final String Item = ingredient_yes.get(i); //Must be "final" to be accessed later

                LayoutInflater layoutInflater = (LayoutInflater) SignUpGoals.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View goalView = layoutInflater.inflate(R.layout.popup, null);

                //GoalDialog = new AlertDialog.Builder(SignUpGoals.this);

                savebtn = goalView.findViewById(R.id.SaveBtn);
                cancelbtn = goalView.findViewById(R.id.CancelBtn);
                nvview = goalView.findViewById(R.id.NVView);
                goaltext = goalView.findViewById(R.id.GoalText);

                //instantiate popupwindow
                goalPopUp = new PopupWindow(goalView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

                //display popup window
                goalPopUp.showAtLocation(linlayout, Gravity.CENTER, 0, 0);
                goalPopUp.setFocusable(true);
                goalPopUp.update();

                nvview.setText(Item);

                cancelbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        goalPopUp.dismiss();
                    }
                });

                savebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String Goal_s = goaltext.getText().toString().trim();

                        if (TextUtils.isEmpty(Goal_s)) {
                            Toast.makeText(SignUpGoals.this, "No changes made", Toast.LENGTH_SHORT).show();
                            goalPopUp.dismiss();
                            return;
                        }

                        final int Goal = Integer.parseInt(Goal_s);

                        //Will use Arraylist<Integer> wantNutrientsGoal from user
                        for (int j = 0; j < ingredient_yes.size(); j++) {
                            if (Item.equals(ingredient_yes.get(j))) {
                                ing_yes_count.add(j, Goal);
                                Output.add(j, ingredient_yes.get(j) + " | " + Goal_s + " grams per day");
                            }
                        }

                        outputAdapted.notifyDataSetChanged();

                        goalPopUp.dismiss();
                    }
                });
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SignUp2.class));
            }
        });

        reviewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dailyCal_s = daily_cal_text.getText().toString().trim();

                if(!TextUtils.isEmpty(dailyCal_s))
                {
                    int dailyCal = Integer.parseInt(dailyCal_s);
                    //Store in User at this point
                }

                startActivity(new Intent(getApplicationContext(), SignUpReview.class));
            }
        });
    }
}