package com.example.stats;

import androidx.appcompat.app.AppCompatActivity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Bundle;
import android.widget.Button;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.view.View;
import android.content.Intent;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.EditText;
import android.os.Handler;
import android.os.Bundle;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class WeekActivity extends AppCompatActivity {

    TextView statsTitle, weekDisplay;
    Button b2Dashbtn, weekButton, dayButton;
    private String date;

    Calendar c1=Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week);

        //display (button) titles
        statsTitle=(TextView) findViewById(R.id.statsTitle);
        b2Dashbtn=(Button)findViewById(R.id.backToDashButton);
        weekButton= (Button)findViewById(R.id.weekButton);
        dayButton= (Button) findViewById(R.id.dayButton);

        //first day of week
        weekDisplay = (TextView) findViewById(R.id.date_textView);
        c1.set(Calendar.DAY_OF_WEEK, 1);

        int year1 = c1.get(Calendar.YEAR);
        int month1 = c1.get(Calendar.MONTH)+1;
        int day1 = c1.get(Calendar.DAY_OF_MONTH);

        //last day of week
        c1.set(Calendar.DAY_OF_WEEK, 7);

        int year7 = c1.get(Calendar.YEAR);
        int month7 = c1.get(Calendar.MONTH)+1;
        int day7 = c1.get(Calendar.DAY_OF_MONTH);

        weekDisplay.setText("Week of: " +year1 + month1 + day1  + "-" + year7 + month7 + day1);




        dayButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });
    }



}