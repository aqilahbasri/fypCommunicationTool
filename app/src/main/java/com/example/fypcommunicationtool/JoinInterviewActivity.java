package com.example.fypcommunicationtool;

import androidx.annotation.NonNull;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.fypcommunicationtool.assessment.Users;
import com.example.fypcommunicationtool.utilities.Constants;
import com.example.fypcommunicationtool.utilities.PreferenceManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class JoinInterviewActivity extends BaseActivity {

    private static final String TAG = "JoinInterviewActivity";
    private boolean isApplied, isScheduled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //if no applications
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String id = user.getUid();

        DatabaseReference mainRef = db.getReference().child("ManageOnlineInterview");

        mainRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("NewApplication").child(id).exists())
                    isApplied = true;
                if (snapshot.child("ScheduledInterview").child(id).exists())
                    isScheduled = true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Error: " + error.getMessage());
            }
        });

        PreferenceManager manager = new PreferenceManager(getApplicationContext());

        Users users = new Users();
        users.setFullName(manager.getString(Constants.KEY_FULL_NAME));
        users.setEmail(manager.getString(Constants.KEY_EMAIL));
        users.setFcmToken(manager.getString(Constants.KEY_FCM_TOKEN));
        users.setUserID(manager.getString(Constants.KEY_USER_ID));
        users.setProfileImage(manager.getString(Constants.KEY_PROFILE_IMAGE));

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new JoinInterviewFragment(isApplied, isScheduled, users)).commit();
            }
        }, 1000);

    }
}