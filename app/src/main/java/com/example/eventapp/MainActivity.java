package com.example.eventapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.eventapp.Adapter.CustomItemClickListener;
import com.example.eventapp.Adapter.EventListAdapter;
import com.example.eventapp.Adapter.EventRecyclerAdapter;
import com.example.eventapp.Adapter.RecyclerViewTouchListener;
import com.example.eventapp.Model.EventModel;
import com.example.eventapp.Model.Item;
import com.example.eventapp.Model.apiInterface;
import com.example.eventapp.Service.apiService;
import com.example.eventapp.Model.Utilities;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Item> events;
    private EventListAdapter eventListAdapter;
    RecyclerView recyclerView;
    private EventRecyclerAdapter eventAdapter;
    private apiService apiInit;
    apiInterface apiI;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiInit = new apiService();
        apiI = apiService.getClient().create(apiInterface.class);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

//        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
//        recyclerView.setLayoutManager(layoutManager);

//        recyclerView.setAdapter(eventAdapter);
//        RecyclerView.Adapter adapter = new EventRecyclerAdapter(events);
//        recyclerView.setAdapter(adapter);

        doSearch("","quran");

        recyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(getApplicationContext(), recyclerView, new CustomItemClickListener() {

            @Override
                public void onClick(View view, int position) {

                    String eventName = events.get(position).getTitle();
                    String eventDesc = Utilities.FCurrency(Long.parseLong(events.get(position).getHarga().get(0).getHarga()));
                    String eventID = events.get(position).getId();

                goToDetail(eventName, eventDesc, eventID);

                }

                @Override
                public void onLongClick(View view, int position) {

                    String eventName = events.get(position).getTitle();
                    String eventDesc = Utilities.FCurrency(Long.parseLong(events.get(position).getHarga().get(0).getHarga()));
                    String eventID = events.get(position).getId();

                    goToDetail(eventName, eventDesc, eventID);
                }
        }));

    }


    void goToDetail(String eventName, String eventDesc, String eventId){

        Intent intent = new Intent(this,DetailActivity.class);
        intent.putExtra("eventName", eventName);
        intent.putExtra("eventDesc", eventDesc);
        intent.putExtra("eventId", eventId);

        startActivity(intent);
    }

    public void doSearch(String keyword, String type) {
        Call<ArrayList<Item>> Itemmm;
        if("".equals(keyword)) {
            Itemmm = apiI.getJenis(type,0);
        } else {
            Itemmm = apiI.getSearch(type,keyword,0);
        }
        Integer tot = (type == "epaper") ? 1 : 2;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        Itemmm.enqueue(new Callback<ArrayList<Item>>() {
            @Override
            public void onResponse(Call<ArrayList<Item>> call, Response<ArrayList<Item>> response) {

                if(response.body() == null) {

                    //empty_view.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);

                }else {

                    events = new ArrayList<>(response.body());
                    eventAdapter = new EventRecyclerAdapter (new ArrayList<>(response.body()),getApplicationContext());
                    eventAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(eventAdapter);
                    //empty_view.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                }
            }
            @Override
            public void onFailure(Call<ArrayList<Item>> call, Throwable t) {
                //empty_view.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        });
    }
}
