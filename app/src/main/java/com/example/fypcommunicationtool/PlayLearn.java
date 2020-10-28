package com.example.fypcommunicationtool;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.view.View.INVISIBLE;


public class PlayLearn extends AppCompatActivity implements View.OnClickListener{

    private Button learnbtn, chlgbtn, practicebtn, practiceebtn ;
    String data;
    DatabaseReference reference;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_learn);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        data = extras.getString("catTitle");

        toolbar = (Toolbar) findViewById(R.id.main_learning_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(data);

        reference = FirebaseDatabase.getInstance().getReference().child("LEARNING").child(data);

        practicebtn= (Button) findViewById(R.id.practicebtn);
        practiceebtn= (Button) findViewById(R.id.practiceebtn);


        if(!data.equals("ALPHABET")){
            practicebtn.setVisibility(INVISIBLE);
        }

        if(!data.equals("NUMBER")){
            practiceebtn.setVisibility(INVISIBLE);
        }


        practicebtn= (Button) findViewById(R.id.practicebtn);
        practicebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPracticeAlphabet();
            }
        });


            practiceebtn= (Button) findViewById(R.id.practiceebtn);
            practiceebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openPracticeNumber();
                }
            });



        learnbtn = (Button) findViewById(R.id.learnbtn);
        learnbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLearn();
            }
        });

        chlgbtn = (Button) findViewById(R.id.chlgbtn);
        chlgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openQuiz();
            }
        });


    }

    //go to live recognition page alphabet
    private void openPracticeAlphabet() {
        Intent intent = new Intent(this, recognition.class);
        intent.putExtra("catTitle", data);
        startActivity(intent);
    }

    //go to practice page number
    private void openPracticeNumber() {
        Intent intent = new Intent(this, Practice.class);
        intent.putExtra("catTitle", data);
        startActivity(intent);
    }



    //go to learn page
    public void openLearn() {
        Intent intent = new Intent(this, Learn.class );
        intent.putExtra("catTitle", data);
        startActivity(intent);
    }

    //go to challenge page
    public void openQuiz() {
        Intent intent = new Intent(this, Questions.class);
        intent.putExtra("catTitle", data);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {

    }
}
