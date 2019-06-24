package com.example.eventapp.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.eventapp.MainActivity;
import com.example.eventapp.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    String data;

    @Override
    public void onNewToken(String s) {

        Log.e("NEW_TOKEN", s);

    }

    private static final String TAG = "FCM Service";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //super.onMessageReceived(remoteMessage);

        data = remoteMessage.getData().get("event_id");
        showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());






    }

    private void showNotification(String title, String body) {

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "info_test";

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                    "Notification", NotificationManager.IMPORTANCE_DEFAULT);

            notificationChannel.setDescription("Event Information");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.GREEN);
            notificationChannel.setVibrationPattern(new long[]{0,500,250,500});
            notificationChannel.enableLights(true);
            notificationManager.createNotificationChannel(notificationChannel);


        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

        Intent myIntent = new Intent(this, MainActivity.class);
        myIntent.putExtra("event_id", data);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                myIntent,
                Intent.FILL_IN_ACTION);

        notificationBuilder.
                setAutoCancel(true).
                setDefaults(Notification.DEFAULT_ALL).
                setWhen(System.currentTimeMillis()).
                setSmallIcon(R.drawable.ic_notif).
                setContentTitle(title).
                setContentIntent(pendingIntent).
                setContentText(body).setContentInfo("Info");

        notificationManager.notify(new Random().nextInt(), notificationBuilder.build());




    }
}
