package com.example.fypcommunicationtool;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AssessmentMenuActivity extends BaseActivity {

    private static final String TAG = "AssessmentMenuActivity";
    private boolean isCompleteAssessment;
    private boolean isCompleteSubmission;
    private boolean isCompleteInterview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String id = user.getUid();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference assessmentRef = database.getReference().child("AssessmentMark");
        DatabaseReference courseworkRef = database.getReference().child("ManageCoursework").child("CourseworkSubmissions");
        DatabaseReference interviewRef = database.getReference().child("ManageOnlineInterview").child("CompletedInterview");

        assessmentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(id).exists()) {
                    isCompleteAssessment = true;
                } else isCompleteAssessment = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "error: " + error.getMessage());
            }
        });

        courseworkRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(id).exists()) {
                    isCompleteSubmission = true;
                } else isCompleteSubmission = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "error: " + error.getMessage());
            }
        });

        interviewRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(id).exists()) {
                    isCompleteInterview = true;
                } else isCompleteInterview = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "error: " + error.getMessage());
            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (savedInstanceState == null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new AssessmentHomeFragment(isCompleteAssessment, isCompleteInterview, isCompleteSubmission)).commit();
                }
            }
        }, 2000);
    }

}