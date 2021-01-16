package com.example.fypcommunicationtool.assessment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fypcommunicationtool.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class CallingActivity extends AppCompatActivity {

    private static final String TAG ="CallingActivity";
    private String receiverUserId="", receiverUserImage ="", receiverFullName = "";
    private String senderUserId="", senderUserImage ="", senderFullName = "";

    TextView username;
    ImageView userImage;
    ImageView rejectButton;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calling);

        username = findViewById(R.id.userName);
        userImage = findViewById(R.id.meetingInitiator);
        rejectButton = findViewById(R.id.rejectButton);

        senderUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        receiverUserId = getIntent().getExtras().getString("visit_user_id").toString();

        ref = database.getReference().child("Users");

        getAndSetUserProfileInfo();
    }

    private void getAndSetUserProfileInfo() {

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.child(receiverUserId).exists()) {
                    receiverUserImage = snapshot.child(receiverUserId).child("profileImage").getValue().toString();
                    receiverFullName = snapshot.child(receiverUserId).child("fullName").getValue().toString();

                    username.setText(receiverFullName);
                    Glide.with(getApplicationContext()).load(receiverUserImage).into(userImage);
                }

                if(snapshot.child(senderUserId).exists()) {
                    senderUserImage = snapshot.child(senderUserId).child("profileImage").getValue().toString();
                    senderFullName = snapshot.child(senderUserId).child("fullName").getValue().toString();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, error.getMessage());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        checkForReceivingCall();
        
        ref.child(receiverUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (!snapshot.hasChild("Calling") && !snapshot.hasChild("Ringing")) {

                    HashMap<String, Object> callingInfo = new HashMap<>();

                    callingInfo.put("calling", receiverUserId);

                    ref.child(senderUserId).child("Calling").updateChildren(callingInfo)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                HashMap<String, Object> ringingInfo = new HashMap<>();

                                ringingInfo.put("ringing", senderUserId);

                                ref.child(receiverUserId).child("Ringing").updateChildren(ringingInfo);
                            }

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void checkForReceivingCall() {
    }
}