package com.example.fypcommunicationtool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Learn extends AppCompatActivity implements View.OnClickListener{


    private ImageButton nextbtn;
    private ImageButton backbtn;
    private TextView description;
    private WebView imagelearn;
    private int slNum;
    private Toolbar toolbar;

    DatabaseReference reference;
    ArrayList<SLlist> sllist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String data = extras.getString("catTitle");

        toolbar = (Toolbar) findViewById(R.id.main_learning_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(data);

        String slref = data;

        backbtn = findViewById(R.id.backbtn);
        nextbtn = findViewById(R.id.nextbtn);
        description = findViewById(R.id.description);
        imagelearn = findViewById(R.id.imagelearn);

        sllist = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference().child("SignLanguage").child(slref);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    SLlist l = dataSnapshot1.getValue(SLlist.class);
                    sllist.add(l);
                }

                setSL();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClicky();
            }
        });
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickyBack();
            }
        });
    }

    private void onClickyBack() {
        if(slNum==0){
            description.setText(sllist.get(slNum).getSldescription());
            imagelearn.loadUrl(sllist.get(slNum).getImgurl());
            imagelearn.getSettings().setLoadWithOverviewMode(true);
            imagelearn.getSettings().setUseWideViewPort(true);



        }else {
            description.setText(sllist.get(slNum-1).getSldescription());
            imagelearn.loadUrl(sllist.get(slNum-1).getImgurl());
            imagelearn.getSettings().setLoadWithOverviewMode(true);
            imagelearn.getSettings().setUseWideViewPort(true);
            slNum--;
        }
    }

    private void setSL() {
        description.setText(sllist.get(0).getSldescription());
        imagelearn.loadUrl(sllist.get(0).getImgurl());
        imagelearn.getSettings().setLoadWithOverviewMode(true);
        imagelearn.getSettings().setUseWideViewPort(true);
        slNum = 0;
    }


    private void onClicky() {
        if(slNum<sllist.size()-1){
            slNum++;
            description.setText(sllist.get(slNum).getSldescription());
            imagelearn.loadUrl(sllist.get(slNum).getImgurl());
            imagelearn.getSettings().setLoadWithOverviewMode(true);
            imagelearn.getSettings().setUseWideViewPort(true);
        }else {
            Intent intent = new Intent(this, doneLearn.class );
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {

    }


       }




