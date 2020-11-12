package com.example.nutrobud;

import androidx.appcompat.app.AppCompatActivity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.view.View;
import android.os.Handler;
import android.os.Bundle;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.example.nutrobud.ui.home.User;
import com.example.nutrobud.ui.home.Stats;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


public class GoalsActivity extends AppCompatActivity {
    //nutrient progress bars
    TextView tv1,tv2;
    private ProgressBar progressBar1, progressBar2;
    int progressStatus1=70, progressStatus2=10; // hardcoded for now- working on retrieving data from database
    int nutr1goal=100;
    int nutr2goal=100;

    TextView titleView;
    TextView ratio;

    //goal calorie progress
    //int currentCals=1700; // hardcoded for now- working on retrieving data from database
    //int calGoals=2000;
    int currentCals;
    int calGoals;
    private Handler hdlr = new Handler ();

    //get today's date
    private TextView dateTimeDisplay;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;

    // get data from Firestore
    private FirebaseFirestore userDB = FirebaseFirestore.getInstance();
    private List<User> userData= new ArrayList<>();
    private User userDBData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);
        Resources res = getResources();


        titleView = (TextView) findViewById(R.id.titleView); // "goals" title
        titleView = (TextView) findViewById(R.id.calorieText); // "CALORIES" title

        // display current date
        dateTimeDisplay = (TextView) findViewById(R.id.date_textView);
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("EEE- MMM d");
        date = dateFormat.format(calendar.getTime());
        dateTimeDisplay.setText(date);

        // get data from "users" Firestore collection
        userDB.collection("users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()) {
                    List <DocumentSnapshot> userDBDataList = queryDocumentSnapshots.getDocuments();
                    for(DocumentSnapshot d: userDBDataList) {
                        userDBData = d.toObject(User.class);
                        userData.add(userDBData);
                    }
                }
            }
        });

        calGoals = userData.get(0).getCalorieGoalsQty();
        currentCals = userData.get(0).getStats().get(date).getCaloriesTrackedQty();

        // horizontal progress bar for calories, but in circular shape
        Drawable drawable = res.getDrawable(R.drawable.circular);
        final ProgressBar mProgress = (ProgressBar) findViewById(R.id.calProgressBar);
        mProgress.setProgress(0); // main progress starting at 0. Will correspond to database once pulled successfully
        mProgress.setSecondaryProgress(calGoals); // secondary progress aka max progress aka calorie goals
        mProgress.setMax(calGoals); // maximum progress aka calorie goals
        mProgress.setProgressDrawable(drawable);

        ratio = (TextView) findViewById(R.id.ratioView);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (currentCals<=calGoals) {
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
        // hardcoded progress bar for first nutrient. Will update dynamically once data pulled from firestore sucessfully
        tv1=(TextView)findViewById(R.id.pbar1ratio);
        progressBar1=findViewById(R.id.pbar1);
        new Thread(new Runnable(){
            @Override
            public void run() {
                while(progressStatus1<=nutr1goal) {
                    //update progress bar
                    hdlr.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar1.setProgress(progressStatus1);
                            // show progress on TextView
                            tv1.setText(" Protein: " + progressStatus1 + "/" + nutr1goal + "mg");
                        }
                    });
                    try{
                        Thread.sleep(16);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        // hardcoded progress bar for second nutrient. Will update dynamically once data pulled from firestore sucessfully
        tv2=(TextView)findViewById(R.id.pbar2ratio);
        progressBar2=findViewById(R.id.pbar2);
        new Thread(new Runnable(){
            @Override
            public void run() {
                while(progressStatus2<=nutr2goal) {
                    //update progress bar
                    hdlr.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar2.setProgress(progressStatus2);
                            // show progress on TextView
                            tv2.setText(" Vit C: " +progressStatus2 + "/"+ nutr2goal + "mg");
                        }
                    });
                    try{
                        Thread.sleep(16);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}