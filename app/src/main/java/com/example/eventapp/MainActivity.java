package com.example.eventapp;


import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.eventapp.Database.DatabaseHelper;
import com.example.eventapp.Model.apiInterface;
import com.example.eventapp.Service.apiService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {


    apiInterface apiI = apiService.getClient().create(apiInterface.class);
    private ImageButton btn_notification, backButton;
    private TextView appName, nNotif;
    private DatabaseHelper db;
    private ArrayList<NotifItem> notifItems;
    private int jumlahNotif = 0;
    private Fragment notifFragment, mainFragment;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btn_notification = findViewById(R.id.btn_notif);
        appName = findViewById(R.id.title_event);
        backButton = findViewById(R.id.btn_back_notif);
        nNotif = findViewById(R.id.cart_badge);
        db = new DatabaseHelper(this);
        mainFragment = new MainFragment();
        cekNotif();

        FirebaseMessaging.getInstance().subscribeToTopic("global")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (!task.isSuccessful()) {

                            Log.d("notif", "Notification Received");

                        }

                    }
                });


        if(savedInstanceState == null){

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mainFragment).commit();

        }


        btn_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                notifFragment = new NotificationFragment();

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        notifFragment, notifFragment.toString()).addToBackStack(mainFragment.toString()).commit();

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();

            }
        });

    }



    @Override
    public void onBackPressed() {

        super.onBackPressed();

        if (btn_notification.getVisibility() == View.GONE){

            btn_notification.setVisibility(View.VISIBLE);
        }

        if (appName.getVisibility() == View.GONE){

            appName.setVisibility(View.VISIBLE);
        }

        if (backButton.getVisibility() == View.VISIBLE){

            backButton.setVisibility(View.GONE);
        }

        if (nNotif.getVisibility() == View.GONE && jumlahNotif != 0){

            nNotif.setVisibility(View.VISIBLE);
        }

        cekNotif();

        if (notifFragment != null && notifFragment.isVisible()) {


            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    mainFragment, mainFragment.toString()).addToBackStack(mainFragment.
                    toString()).commit();

        }



    }

    public void cekNotif(){

        jumlahNotif = 0;
        notifItems = db.getAllNotif();

        for (NotifItem n : notifItems){

            if(n.getStatus().equals("unread")){
                jumlahNotif ++;
            }

        }

        if(jumlahNotif == 0){
            nNotif.setVisibility(View.GONE);
        }
        else {
            nNotif.setVisibility(View.VISIBLE);
            nNotif.setText(String.valueOf(jumlahNotif));
        }


    }
}
