package com.example.fypcommunicationtool.assessment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fypcommunicationtool.JoinInterviewFragment;
import com.example.fypcommunicationtool.R;
import com.example.fypcommunicationtool.network.ApiClient;
import com.example.fypcommunicationtool.network.ApiService;
import com.example.fypcommunicationtool.utilities.Constants;
import com.example.fypcommunicationtool.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.installations.InstallationTokenResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OutgoingInterviewActivity extends AppCompatActivity {

    private static final String TAG = "OutgoingInterview";
    TextView username;
    ImageView userImage;
    ImageView rejectButton;
    PreferenceManager manager;
    private String inviterToken = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outgoing_interview);
        manager = new PreferenceManager(getApplicationContext());

        username = findViewById(R.id.userName);
        userImage = findViewById(R.id.meetingInitiator);
        rejectButton = findViewById(R.id.rejectButton);

        FirebaseInstallations.getInstance().getToken(true).addOnCompleteListener(new OnCompleteListener<InstallationTokenResult>() {
            @Override
            public void onComplete(@NonNull Task<InstallationTokenResult> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    inviterToken = task.getResult().getToken();
                }
            }
        });

        //TODO: set video icon
        Users user = (Users) getIntent().getSerializableExtra("users");
        String meetingType = getIntent().getStringExtra("type");

        if (user != null) {
            Log.e(TAG, "Profile image: " + user.profileImage);

            username.setText(user.getFullName());
            Glide.with(getApplicationContext()).load(user.profileImage).into(userImage);
        }


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (meetingType != null && user != null) {
                    initiateMeeting(meetingType, user.getFcmToken());
                }
            }
        }, 1500);


        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void initiateMeeting(String meetingType, String receiverToken) {
        try {
            JSONArray tokens = new JSONArray();
            tokens.put(receiverToken);

            JSONObject body = new JSONObject();
            JSONObject data = new JSONObject();

            data.put(Constants.REMOTE_MSG_TYPE, Constants.REMOTE_MSG_INVITATION);
            data.put(Constants.REMOTE_MSG_MEETING_TYPE, meetingType);
            data.put(Constants.KEY_FULL_NAME, manager.getString(Constants.KEY_FULL_NAME));
            data.put(Constants.KEY_EMAIL, manager.getString(Constants.KEY_EMAIL));
            data.put(Constants.REMOTE_MSG_INVITER_TOKEN, inviterToken);

            body.put(Constants.REMOTE_MSG_DATA, data);
            body.put(Constants.REMOTE_MSG_REGISTRATION_IDS, tokens);

            sendRemoteMessage(body.toString(), Constants.REMOTE_MSG_INVITATION);

        } catch (JSONException e) {
            Log.e(TAG, "Line 96: " + e.getMessage());
            e.printStackTrace();
        }

    }

    private void sendRemoteMessage(String remoteMessageBody, String type) {

        ApiClient.getClient().create(ApiService.class).sendRemoteMessage(
                remoteMessageBody
        ).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                Log.e(TAG, "header: ");

                if (response.isSuccessful()) {
                    if (type.equals(Constants.REMOTE_MSG_INVITATION)) {
                        Toast.makeText(getApplicationContext(), "Invitation sent successfully",
                                Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Invitation sent successfully");
                    }

                } else {
                    Log.e(TAG, "Error: " + response.errorBody().toString());
                    finish();
                }

            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Log.e(TAG, t.getMessage());
                finish();
            }
        });
    }
}