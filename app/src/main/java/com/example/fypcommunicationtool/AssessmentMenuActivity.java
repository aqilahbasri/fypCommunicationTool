package com.example.fypcommunicationtool;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.fypcommunicationtool.utilities.Constants;
import com.example.fypcommunicationtool.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.installations.InstallationTokenResult;

import java.util.HashMap;
import java.util.Map;

public class AssessmentMenuActivity extends BaseActivity {

    private static final String TAG = "AssessmentMenuActivity";
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AssessmentHomeFragment()).commit();
        }
//        getSupportFragmentManager().addOnBackStackChangedListener(this);
        preferenceManager = new PreferenceManager(getApplicationContext());
        Log.e(TAG, "fullName: " + preferenceManager.getString(Constants.KEY_FULL_NAME));

        FirebaseInstallations.getInstance().getToken(true).addOnCompleteListener(new OnCompleteListener<InstallationTokenResult>() {
            @Override
            public void onComplete(@NonNull Task<InstallationTokenResult> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    String token = task.getResult().getToken();
                    sendFCMTokenToDatabase(token);
                }
            }
        });

    }

    private void sendFCMTokenToDatabase(String token) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference reference = db.getReference(Constants.KEY_COLLECTION_USERS)
                .child(preferenceManager.getString(Constants.KEY_USER_ID));
        Map map = new HashMap();
        map.put(Constants.KEY_FCM_TOKEN, token);
        reference.updateChildren(map).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Token updated successfully", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Error: " + e.getMessage());
            }
        });
    }
}