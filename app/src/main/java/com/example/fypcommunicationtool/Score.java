package com.example.fypcommunicationtool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Score extends AppCompatActivity implements View.OnClickListener{

    private Button backhome;
    private TextView score;

    private FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    private int xp;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("LEADERBOARD");

        backhome = (Button) findViewById(R.id.backhome);
        backhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backhome();
            }
        });

        score = findViewById(R.id.score);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String data = extras.getString("finalscore");
        score.setText(data + "%");

        xp = Integer.parseInt(data)*20;

        UploadScore userscore = new UploadScore(userID, xp);
        databaseReference.child(userID).setValue(userscore);


    }

    public void backhome() {
        Intent intent = new Intent(this, MainActivityLearning.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {

    }
}
