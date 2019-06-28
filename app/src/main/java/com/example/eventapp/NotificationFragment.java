package com.example.eventapp;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventapp.Adapter.CustomItemClickListener;
import com.example.eventapp.Adapter.NotificationAdapter;
import com.example.eventapp.Adapter.PesertaRecyclerAdapter;
import com.example.eventapp.Adapter.RecyclerViewTouchListener;
import com.example.eventapp.Database.DatabaseHelper;
import com.example.eventapp.Model.PesertaModel;
import java.util.ArrayList;


public class NotificationFragment extends Fragment {

    private RecyclerView recyclerView;
    private DatabaseHelper db;
    private NotificationAdapter notifAdapter;
    private ArrayList<NotifItem> notifItems;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View appName = getActivity().findViewById(R.id.title_event);
        View btnNotif = getActivity().findViewById(R.id.btn_notif);
        View backBtn = getActivity().findViewById(R.id.btn_back_notif);
        View etJumlahNotif = getActivity().findViewById(R.id.cart_badge);

        View view = inflater.inflate(R.layout.fragment_notification, container, false);


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


        db = new DatabaseHelper(getActivity());
        recyclerView =  view.findViewById(R.id.recycler_view_notif);
        recyclerView.setHasFixedSize(true);

        notifItems = db.getAllNotif();
        final ArrayList<PesertaModel> pesertaModels = new ArrayList<>();

//        for (NotifItem n : notifItems){
//
//            PesertaModel pesertaModel = new PesertaModel();
//            pesertaModel.setNamaPeserta(n.getNotifTitle());
//            pesertaModel.setPhone(n.getNotifContent());
//            pesertaModel.setId_event(n.getValue());
//
//            pesertaModels.add(pesertaModel);
//
//        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        notifAdapter = new NotificationAdapter(notifItems);
        recyclerView.setAdapter(notifAdapter);



//        recyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(getActivity(), recyclerView, new CustomItemClickListener() {
//
//
//            @Override
//            public void onClick(View view, int position) {
//
//                String eventID = String.valueOf(pesertaModels.get(position).getId_event());
//
//                NotifItem n1 = new NotifItem();
//                n1.setValue(pesertaModels.get(position).getId_event());
//                n1.setNotifTitle(pesertaModels.get(position).getNamaPeserta());
//                n1.setNotifContent(pesertaModels.get(position).getPhone());
//                n1.setStatus("read");
//
//                db.updateNotif(n1);
//                goToDetail(eventID);
//
//            }
//
//            @Override
//            public void onLongClick(View view, int position) {
//
//
//                String eventID = String.valueOf(pesertaModels.get(position).getId_event());
//                goToDetail(eventID);
//
//            }
//
//        }));

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

                //Toast.makeText(getActivity(), notifItems.get(position).getType(), Toast.LENGTH_LONG).show();

                if(notifItems.get(position).getType().equals("3")){

                    notifAdapter.notifyDataSetChanged();
                    goToWeb(value);

                }
                else {

                    notifAdapter.notifyDataSetChanged();
                    goToDetail(value);
                }



            }

            @Override
            public void onDeleteClick(int position) {

                removeItem(position);

            }
        });

        return view;
    }


    void goToDetail(String eventId){

        Intent intent = new Intent(getActivity(),DetailActivity.class);
        intent.putExtra("eventId", eventId);
        startActivity(intent);

    }

    void goToWeb(String value){

        Intent intent = new Intent(getActivity(),WebActivity.class);
        intent.putExtra("value", value);
        startActivity(intent);

    }

    public void removeItem(int position) {

        db.deleteNotification(notifItems.get(position));

        notifItems.remove(position);
        notifAdapter.notifyItemRemoved(position);
        notifAdapter.notifyDataSetChanged();

    }




}
