package com.example.eventapp.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.eventapp.Database.DatabaseHelper;
import com.example.eventapp.DetailActivity;
import com.example.eventapp.NotifItem;
import com.example.eventapp.R;
import com.example.eventapp.WebActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    String value, content, type;

    private DatabaseHelper db = new DatabaseHelper(this);

    public MyFirebaseMessagingService(){

        FirebaseMessaging.getInstance().subscribeToTopic("events")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (!task.isSuccessful()) {

                            Log.d(TAG, "Notification Received");

                        }

                    }
                });

    }

    @Override
    public void onNewToken(String s) {

        Log.e("NEW_TOKEN", s);
        FirebaseMessaging.getInstance().subscribeToTopic("all");
        System.out.println(s);

    }

    private static final String TAG = "FCM Service";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        value = remoteMessage.getData().get("value");
        content = remoteMessage.getData().get("content");
        type = remoteMessage.getData().get("type");


        NotifItem n1 = new NotifItem();

        n1.setValue(value);
        n1.setNotifTitle(remoteMessage.getData().get("title"));
        n1.setNotifContent(remoteMessage.getData().get("content"));
        n1.setType(type);
        n1.setStatus("unread");

        db.daftarNotif(n1);
        showNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("content"));


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


        if(type.equals("3")){

            Intent myIntent = new Intent(this, WebActivity.class);
            myIntent.putExtra("value", value);

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
        else {

            Intent myIntent = new Intent(this, DetailActivity.class);
            myIntent.putExtra("value", value);

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



}
