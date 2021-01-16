package com.example.fypcommunicationtool.services;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.fypcommunicationtool.assessment.IncomingInvitationActivity;
import com.example.fypcommunicationtool.utilities.Constants;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MessagingService extends FirebaseMessagingService {

    private static final String TAG = "MessagingService";

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.e(TAG, "Token: "+token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String type = remoteMessage.getData().get(Constants.REMOTE_MSG_TYPE);

        if(type!=null) {
            if(type.equals(Constants.REMOTE_MSG_INVITATION)) {
                Intent intent = new Intent(getApplicationContext(), IncomingInvitationActivity.class);
                intent.putExtra(
                        Constants.REMOTE_MSG_MEETING_TYPE,
                        remoteMessage.getData().get(Constants.REMOTE_MSG_MEETING_TYPE)
                );
                intent.putExtra(Constants.KEY_FULL_NAME,
                        remoteMessage.getData().get(Constants.KEY_FULL_NAME));
                intent.putExtra(Constants.KEY_EMAIL,
                        remoteMessage.getData().get(Constants.KEY_EMAIL));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }

        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Remote message received: "+remoteMessage.getNotification().getBody());
        }
        else Log.e(TAG, "Remote message received: null");

    }
}
