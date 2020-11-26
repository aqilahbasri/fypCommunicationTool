package com.example.fypcommunicationtool;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class doneLearn extends AppCompatActivity implements View.OnClickListener {

    Button backhome;
    String data, img;
    private FirebaseAuth mAuth;
    String userID;
    DatabaseReference reference;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done_learn);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        data = extras.getString("catTitle");
        img = extras.getString("catimg");

        mAuth = FirebaseAuth.getInstance();
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        LearnBGM(view);

        reference = FirebaseDatabase.getInstance().getReference().child("LearntCategory").child(userID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UploadCategory learntcat = new UploadCategory(img, data);
                reference.child(data).setValue(learntcat);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        backhome = (Button) findViewById(R.id.backhome);
        backhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick();
            }

            private void onClick() {
                Intent intent = new Intent(doneLearn.this, MainActivityLearning.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onClick(View v) {

    }

    public void LearnBGM(View view){
        Intent intent = new Intent(doneLearn.this, LearnBGM.class);
        stopService(intent);
    }
}