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

public class courseworkDetail {

    Activity activity;
    View view;
    ListView listView;

    courseworkDetail(Activity activity, View view) {
        super();
        this.activity = activity;
        this.view = view;
    }

    protected void viewList() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        listView = view.findViewById(R.id.listView);
        ArrayList<String> detailsArrayList = new ArrayList<>();

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, detailsArrayList);
        DatabaseReference detailsRef = database.getReference().child("STUDENT_COURSEWORK").child("COURSEWORK_LEVEL1");
        listView.setAdapter(adapter);

        //Retrieve question details and due date
        detailsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String value = dataSnapshot.getValue().toString();
                String name = dataSnapshot.getKey();
                detailsArrayList.add(name + " : " +value);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                adapter.notifyDataSetChanged();
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