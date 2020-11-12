package com.example.nutrobud;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Calendar_MainActivity extends AppCompatActivity {
    private static final String TAG ="Calendar_MainActivity";
    private TextView theDate;
    private Button btnGoCalendar;

    private Button btnGoGoals;
    private Button btnGoStatistics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        theDate = (TextView) findViewById(R.id.date);
        btnGoCalendar = (Button) findViewById(R.id.btnGoCalendar);

        btnGoGoals = (Button) findViewById(R.id.btnGoGoals);
        btnGoStatistics = (Button) findViewById(R.id.btnGoStatistics);

        Intent incomingIntent = getIntent();
        String date = incomingIntent.getStringExtra("date");
        theDate.setText(date);

        btnGoCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Calendar_MainActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });

        btnGoGoals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_goals = new Intent(Calendar_MainActivity.this, GoalsActivity.class);
                startActivity(intent_goals);
            }
        });

        btnGoStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_statistics = new Intent(Calendar_MainActivity.this, StatisticsActivity.class);
                startActivity(intent_statistics);
            }
        });
    }

}
