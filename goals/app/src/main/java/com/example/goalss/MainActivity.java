package com.example.goalss;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.os.Handler;
import android.os.Bundle;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    //goal progress
    private ProgressBar pgsBar;
    int currentCals=1000;
    int calGoals=2000;
    TextView titleView;
    TextView ratio;
    private Handler hdlr = new Handler ();
    //get today's date
    private TextView dateTimeDisplay;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;

    private float goalIndicatorHeight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Resources res = getResources();



        titleView = (TextView) findViewById(R.id.titleView); // "goals" title
        ratio = (TextView) findViewById(R.id.ratioView); // goal ratio

        // display current date
        dateTimeDisplay = (TextView) findViewById(R.id.date_textView);
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("EEE- MMM d");
        date = dateFormat.format(calendar.getTime());
        dateTimeDisplay.setText(date);

        // add progress bar
        //pgsBar = (ProgressBar) findViewById(R.id.calProgressBar);
        //currentCals=pgsBar.getProgress();
        //txtView=(TextView) findViewById(R.id.tView);
        Drawable drawable = res.getDrawable(R.drawable.circular);
        final ProgressBar mProgress = (ProgressBar) findViewById(R.id.calProgressBar);
        mProgress.setProgress(0); // main progress
        mProgress.setSecondaryProgress(calGoals); // secondary progress
        mProgress.setMax(calGoals); // maximum progress
        mProgress.setProgressDrawable(drawable);

        ratio = (TextView) findViewById(R.id.ratioView);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (currentCals<=calGoals) {
                    //pStatus+=1;
                    //update progress bar and display current value in text view
                    hdlr.post(new Runnable() {
                        @Override
                        public void run() {
                            mProgress.setProgress(currentCals);
                            ratio.setText(currentCals + "/" + calGoals);
                        }
                    });
                    try {
                        //sleep for 100 ms to show progress slowly
                        Thread.sleep(16);
                    } catch(InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}