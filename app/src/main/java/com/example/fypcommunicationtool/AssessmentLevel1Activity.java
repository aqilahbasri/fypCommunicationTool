package com.example.fypcommunicationtool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class AssessmentLevel1Activity extends AppCompatActivity {

    Boolean doubleBackToExitPressedOnce = false;
    EditText answer;
    TextView question;
    ImageView questionImage;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    DatabaseReference ques = myRef.child("ASSESSMENT_LEVEL");

    private TextView mTextViewCountDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_level1);

        question = findViewById(R.id.question);
        answer = findViewById(R.id.answer);
        questionImage = findViewById(R.id.question_image);
        mTextViewCountDown = findViewById(R.id.timer);

        new CountDownTimer(900000, 1000) {

            public void onTick(long millisUntilFinished) {
//                mTextViewCountDown.setText("Time left: " + millisUntilFinished / 1000);
                String text = String.format(Locale.getDefault(), "Time left: %02d min: %02d sec",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60);
                mTextViewCountDown.setText(text);
            }

            public void onFinish() {
                mTextViewCountDown.setText("Your time is up!");
            }
        }.start();
    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference q1ques = ques.child("LEVEL_1_QUESTIONS").child("Q1").child("Q1_ques");
        DatabaseReference q1image = ques.child("LEVEL_1_QUESTIONS").child("Q1").child("Q1_ques_img");
        DatabaseReference q1ans = ques.child("LEVEL_1_QUESTIONS").child("Q1").child("Q1ans");

        q1ques.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String q1question = dataSnapshot.getValue().toString();
                question.setText(q1question);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        q1image.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String link = dataSnapshot.getValue(String.class);
                Glide.with(AssessmentLevel1Activity.this).asGif().load(link).into(questionImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AssessmentLevel1Activity.this, "Error accessing database", Toast.LENGTH_SHORT).show();
//               ((TextView) findViewById(R.id.menuPage_title)).setText("Error fetch from database");
            }
        });
    }

    @Override
    public void onBackPressed() {

        if (isTaskRoot()) {    //Only can exit from Home fragment
//            if (doubleBackToExitPressedOnce) {
//                super.onBackPressed();
//            }
//            else {
                doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Are you sure you want to exit? Once you start, you cannot exit.", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce=false;
                    }
                }, 2000);
//            }
        }

//        else {
//            super.onBackPressed();
//        }
    }
}