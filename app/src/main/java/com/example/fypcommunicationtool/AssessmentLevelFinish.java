package com.example.fypcommunicationtool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AssessmentLevelFinish extends AppCompatActivity {

    TextView scoreTxt;
    Button backToHomeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_level_finish);

        scoreTxt = findViewById(R.id.scoreTxt);
        backToHomeBtn = findViewById(R.id.backToHomeBtn);

        Intent intent = getIntent();
        double score = intent.getDoubleExtra("percentScore", 0);
        String scoreStr = intent.getStringExtra("finalScore");

        scoreTxt.setText("You scored "+scoreStr+"!");

        backToHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AssessmentLevelFinish.this, AssessmentMenuActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(AssessmentLevelFinish.this, AssessmentMenuActivity.class);
        startActivity(i);
    }
}