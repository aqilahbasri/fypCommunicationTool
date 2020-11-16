package com.example.fypcommunicationtool;

import android.app.Activity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class viewResultsList {

    ArrayList<String> detailsArrayList;
    ArrayAdapter<String> adapter;
    FirebaseDatabase database;
    ListView list;
    Activity activity;
    View view;

    protected viewResultsList(Activity activity, View view) {
        super();
        this.activity = activity;
        this.view = view;
    }
    protected void displayList () {
        list = view.findViewById(R.id.section_list);
        detailsArrayList = new ArrayList<>();
        adapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, detailsArrayList);
        list.setAdapter(adapter);

        database = FirebaseDatabase.getInstance();
        DatabaseReference detailsRef;

        detailsRef = database.getReference().child("ASSESSMENT_MARK").child("LEVEL_1").child("Leyla");
        detailsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String value = dataSnapshot.getValue().toString();
                detailsArrayList.add(value);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}