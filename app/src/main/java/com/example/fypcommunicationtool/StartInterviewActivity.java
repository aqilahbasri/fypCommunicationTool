package com.example.fypcommunicationtool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.fypcommunicationtool.assessment.OutgoingInterviewActivity;
import com.example.fypcommunicationtool.assessment.Users;
import com.example.fypcommunicationtool.assessment.UsersListener;

public class StartInterviewActivity extends AppCompatActivity implements UsersListener {

    private UsersListener usersListener;
    private Users users;

    private static final String TAG = "StartInterviewActivity";

    public StartInterviewActivity() {
    }

    public StartInterviewActivity(UsersListener usersListener, Users users) {
        this.usersListener = usersListener;
        this.users = users;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_interview);

        usersListener.initiateVideoMeeting(users);

    }

    @Override
    public void initiateVideoMeeting(Users users) {
        if (users.getFcmToken() == null || users.getFcmToken().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), users.getFullName()+"is not available for meeting", Toast.LENGTH_SHORT)
                    .show();
        }
        else {
            Intent intent = new Intent(getApplicationContext(), OutgoingInterviewActivity.class);
            intent.putExtra("user", users);
            intent.putExtra("type", "video");
            startActivity(intent);

            Toast.makeText(getApplicationContext(), "Video meeting with "+users.getFullName(), Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void initiateAudioMeeting(Users users) {

    }
}