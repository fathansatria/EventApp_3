package com.example.eventapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.eventapp.Adapter.NotificationAdapter;
import com.example.eventapp.Database.DatabaseHelper;
import com.example.eventapp.Model.NotifItem;
import java.util.ArrayList;


public class NotificationFragment extends Fragment {

    private RecyclerView recyclerView;
    private LinearLayout recyclerLayout, emptyLayout;
    private DatabaseHelper db;
    private NotificationAdapter notifAdapter;
    private ArrayList<NotifItem> notifItems;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        db = new DatabaseHelper(getActivity());

        View appName = getActivity().findViewById(R.id.title_event);
        View btnNotif = getActivity().findViewById(R.id.btn_notif);
        View backBtn = getActivity().findViewById(R.id.btn_back_notif);
        View etJumlahNotif = getActivity().findViewById(R.id.cart_badge);
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        emptyLayout = view.findViewById(R.id.empty_notif);
        recyclerLayout = view.findViewById(R.id.recycler_view_layout);
        recyclerView =  view.findViewById(R.id.recycler_view_notif);
        recyclerView.setHasFixedSize(true);


        if( appName instanceof TextView) {

            TextView appsName = (TextView) appName;
            appsName.setVisibility(View.GONE);

        }

        if( btnNotif instanceof ImageButton) {

            ImageButton btnNotify = (ImageButton) btnNotif;
            btnNotify.setVisibility(View.GONE);

        }

        if( backBtn instanceof ImageButton) {

            ImageButton btnBack = (ImageButton) backBtn;
            btnBack.setVisibility(View.VISIBLE);

        }

        if( etJumlahNotif instanceof TextView) {

            TextView nNotif = (TextView) etJumlahNotif;
            nNotif.setVisibility(View.GONE);

        }


        //cek ada notif
        notifItems = db.getAllNotif();

        if(notifItems.size() == 0 ){

            recyclerLayout.setVisibility(View.GONE);

            emptyLayout.setVisibility(View.VISIBLE);
            emptyLayout.setGravity(Gravity.CENTER);

        }
        else {

            recyclerLayout.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);

        }

        //int recycler view
        setUpRecycler();


        return view;
    }


    public void goToDetail(String eventId){

        Intent intent = new Intent(getActivity(),DetailActivity.class);
        intent.putExtra("eventId", eventId);
        startActivity(intent);

    }

    public void goToWeb(String value){

        Intent intent = new Intent(getActivity(),WebActivity.class);
        intent.putExtra("value", value);
        startActivity(intent);

    }

    public void setUpRecycler(){

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        notifAdapter = new NotificationAdapter(notifItems);
        recyclerView.setAdapter(notifAdapter);

        notifAdapter.setOnItemClickListener(new NotificationAdapter.OnItemClickListener() {


            @Override
            public void onItemClick(int position) {

                String value = String.valueOf(notifItems.get(position).getValue());
                NotifItem n1 = new NotifItem();
                n1.setValue(notifItems.get(position).getValue());
                n1.setNotifTitle(notifItems.get(position).getNotifTitle());
                n1.setNotifContent(notifItems.get(position).getNotifContent());
                n1.setType(notifItems.get(position).getType());
                n1.setStatus("read");
                db.updateNotif(n1);


                if(notifItems.get(position).getType().equals("3")){

                    notifAdapter.notifyDataSetChanged();
                    goToWeb(value);

                }
                else {

                    notifAdapter.notifyDataSetChanged();
                    goToDetail(value);

                }

            }

        });
    }





}
