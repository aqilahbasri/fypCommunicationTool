package com.example.fypcommunicationtool;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CourseworkDetails {

    private static final String TAG = "CourseworkDetails";
    ArrayList<ManageCourseworkModalClass> newApplicationList;
    Activity activity;
    View view;
    RecyclerView mRecyclerView;

    CourseworkDetails(Activity activity, View view) {
        super();
        this.activity = activity;
        this.view = view;
    }

    protected void viewList() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mRecyclerView = view.findViewById(R.id.list_name);
//        mRecyclerView.setLayoutManager(new GridLayoutManager(activity, 1));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        newApplicationList = new ArrayList<>();

        DatabaseReference detailsRef = database.getReference().child("ManageCoursework").child("CourseworkQuestions");
        detailsRef.keepSynced(true);

        /*
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


         */

        detailsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                newApplicationList.clear();
//                String key = null;
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ManageCourseworkModalClass manageCourseworkModalClass;
                    manageCourseworkModalClass = ds.getValue(ManageCourseworkModalClass.class);
                    newApplicationList.add(manageCourseworkModalClass);
//                    key = ds.getKey();
                }

                SubmitCourseworkAdapter adapter = new SubmitCourseworkAdapter(activity, newApplicationList);
                mRecyclerView.setAdapter(adapter);
//                Collections.reverse(newApplicationList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(activity, error.toString(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, error.toString());
            }
        });

    }
}