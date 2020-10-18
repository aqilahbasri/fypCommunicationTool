package com.example.learningmodule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Learn extends AppCompatActivity implements View.OnClickListener{


    private ImageButton nextbtn;
    private ImageButton backbtn;
    private TextView description;
    private ImageView imagelearn;
    private int slNum;
    private ActionBar toolbar;

    DatabaseReference reference;
    ArrayList<SLlist> sllist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String data = extras.getString("catTitle");

        toolbar = getSupportActionBar();
        toolbar.setTitle(data);

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
            Picasso.get().load(sllist.get(slNum).getImgurl()).into(imagelearn);

        }else {
            description.setText(sllist.get(slNum-1).getSldescription());
            Picasso.get().load(sllist.get(slNum-1).getImgurl()).into(imagelearn);
            slNum--;
        }
    }

    private void setSL() {
        description.setText(sllist.get(0).getSldescription());
        Picasso.get().load(sllist.get(0).getImgurl()).into(imagelearn);
        slNum = 0;
    }


    private void onClicky() {
        if(slNum<sllist.size()-1){
            slNum++;
            description.setText(sllist.get(slNum).getSldescription());
            Picasso.get().load(sllist.get(slNum).getImgurl()).into(imagelearn);
        }else {
//            Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/mute-deaf-communication-tool.appspot.com/o/LearningModule%2FAlphabet%2Flearn%2Fe.png?alt=media&token=34d690ef-f2a4-49af-87eb-57c18dd16e2d").into(imagelearn);
//            Intent intent = getIntent();
//            description.setText("E");
            Intent intent = new Intent(this, doneLearn.class );
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {

    }


       }




