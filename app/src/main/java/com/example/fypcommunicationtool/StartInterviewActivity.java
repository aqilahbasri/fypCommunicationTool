package com.example.fypcommunicationtool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.fypcommunicationtool.listeners.UsersListener;
import com.example.fypcommunicationtool.utilities.Constants;
import com.example.fypcommunicationtool.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class StartInterviewActivity extends AppCompatActivity {

//    private PreferenceManager preferenceManager;
    private static final String TAG = "StartInterviewActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_interview);

//        preferenceManager = new PreferenceManager(getApplicationContext());
//        Log.e(TAG, "fullName: "+preferenceManager.getString(Constants.KEY_FULL_NAME));
    }

//    private void sendFCMTokenToDatabase(String token) {
//        FirebaseDatabase db = FirebaseDatabase.getInstance();
//        DatabaseReference reference = db.getReference(Constants.KEY_COLLECTION_USERS)
//                .child(preferenceManager.getString(Constants.KEY_USER_ID));
//        Map map = new HashMap();
//        map.put(Constants.KEY_FCM_TOKEN, token);
//        reference.updateChildren(map).addOnCompleteListener(new OnCompleteListener() {
//            @Override
//            public void onComplete(@NonNull Task task) {
//                if (task.isSuccessful()) {
//                    Toast.makeText(getApplicationContext(), "Token updated successfully", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.e(TAG, "Error: "+e.getMessage());
//            }
//        });
//    }


}