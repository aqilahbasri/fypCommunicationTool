package com.example.fypcommunicationtool.BroadcastReceiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.core.app.NotificationCompat;

import com.example.fypcommunicationtool.MainActivity;
import com.example.fypcommunicationtool.MainActivityLearning;
import com.example.fypcommunicationtool.R;
import com.example.fypcommunicationtool.SettingFragment;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(context, MainActivityLearning.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = (NotificationCompat.Builder)new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_coronavirus_24)
                .setContentTitle("Communication Tool for Mute and Deaf")
                .setContentText("Lets learn Sign Language today!")
                .setSound(alarmSound)
                .setAutoCancel(true)
                .setWhen(when)
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{1000,1000,1000,1000});
        notificationManager.notify(0, builder.build());
    }
}