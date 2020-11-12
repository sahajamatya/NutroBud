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

import com.example.nutrobud.ui.home.User;

import java.util.ArrayList;
import java.util.List;

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

        final User user = getIntent().getParcelableExtra("User");

        nutrientview = findViewById(R.id.NutrientView);
        linlayout = findViewById(R.id.LinearLayout);
        daily_cal_text = findViewById(R.id.Daily_Cal_Text);
        reviewbtn = findViewById(R.id.ReviewBtn);
        backbtn = findViewById(R.id.BackBtn);

        //Get user's wanted list of vitamins and nutrients
        final List<String> ingredient_yes = user.getIngredientsYes();
        final ArrayList<Integer> ing_goals_yes = new ArrayList<Integer>();

        //Check if the user already has goals set
        if(user.getIngredientsYesGoalsQty() == null) {
            //If there were not any set
            //Make ing_yes_goals the same size as ingredient_yes so indexes match up
            for (int i = 0; i < ingredient_yes.size(); i++) {
                //-1 means no goal set
                ing_goals_yes.add(-1);
            }
        }else if(user.getIngredientsYesGoalsQty().size() == 0) {
            //If there list was already initialized but is not empty
            //Make ing_yes_goals the same size as ingredient_yes so indexes match up
            for (int i = 0; i < ingredient_yes.size(); i++) {
                //-1 means no goal set
                ing_goals_yes.add(-1);
            }
        }else {
            //If it does, parse each string value of the integer into an integer and save into local string
            for (int j = 0; j < user.getIngredientsYesGoalsQty().size(); j++) {
                ing_goals_yes.add(Integer.parseInt(user.getIngredientsYesGoalsQty().get(j)));
            }
        }

        //Set formatting for output
        final ArrayList<String> Output = new ArrayList<>();
        String temp = new String();
        if(ing_goals_yes.size() != 0) {
            for (int i = 0; i < ingredient_yes.size(); i++) {
                temp = ingredient_yes.get(i);
                //Check if there is a goal set or not
                if (ing_goals_yes.get(i) == -1) {
                    //If no goal set:
                    temp = temp + " | No daily goal set";
                } else {
                    //If goal is set:
                    temp = temp + " | " + user.getIngredientsYesGoalsQty().get(i) + " mg per day";
                }
                Output.add(temp);
            }
        }

        //Must adapt output to display in a ListView
        final ArrayAdapter outputAdapted = new ArrayAdapter(this, android.R.layout.simple_list_item_1, Output);

        //Display the output
        nutrientview.setAdapter(outputAdapted);

        //Will begin when a vitamin or nutrient is clicked
        nutrientview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String Item = ingredient_yes.get(i); //Must be "final" to be accessed later

                LayoutInflater layoutInflater = (LayoutInflater) SignUpGoals.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View goalView = layoutInflater.inflate(R.layout.popup, null);

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

                //Display which vitamin or nutrient the user chose
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

                        //Change the input goal from String to Integer
                        final int Goal = Integer.parseInt(Goal_s);

                        //Update goals list with user input goal
                        for (int j = 0; j < ingredient_yes.size(); j++) {
                            if (Item.equals(ingredient_yes.get(j))) {
                                ing_goals_yes.add(j, Goal);
                            }
                        }

                        //Update output
                        for (int j = 0; j < Output.size(); j++) {
                            if(Output.get(j).contains(Item)) {
                                Output.remove(j);
                                Output.add(j, ingredient_yes.get(j) + " | " + Goal_s + " mg per day");
                            }
                        }

                        //Update display
                        outputAdapted.notifyDataSetChanged();

                        //Make the popup go away
                        goalPopUp.dismiss();
                    }
                });
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Pass intent and user to next activity
                Intent i = new Intent(getApplicationContext(), SignUp2.class);
                i.putExtra("User", user);
                startActivity(i);
            }
        });

        reviewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dailyCal_s = daily_cal_text.getText().toString().trim();

                //Check if a calorie goal per day was set
                if(TextUtils.isEmpty(dailyCal_s))
                {
                    //If the user did not enter anyting, save a default value of 2000
                    user.setCalorieGoalsQty(2000);

                } else if(Integer.parseInt(dailyCal_s) < 1) {
                    //Set error if calorie is 0
                    daily_cal_text.setError("Daily calorie intake must be greater than 0");
                    return;
                }else{
                    //If it is not empty, save to user
                    user.setCalorieGoalsQty(Integer.parseInt(dailyCal_s));

                }

                //Parse all of the goals back into strings from integers
                List<String> ing_yes_goal_s= new ArrayList<String>();
                for(int j = 0; j < ing_goals_yes.size(); j++)
                {
                    ing_yes_goal_s.add(ing_goals_yes.get(j).toString());
                }

                //Set user's variable for goals
                user.setIngredientsYesGoalsQty(ing_yes_goal_s);

                //Pass intent and user to next activity
                Intent i = new Intent(SignUpGoals.this, SignUpReview.class);
                i.putExtra("User", user);
                startActivity(i);
            }
        });
    }
}