package com.example.eventapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.eventapp.Adapter.CustomItemClickListener;
import com.example.eventapp.Adapter.EventRecyclerAdapter;
import com.example.eventapp.Adapter.RecyclerViewTouchListener;
import com.example.eventapp.Model.Item;
import com.example.eventapp.Model.apiInterface;
import com.example.eventapp.Service.MyFirebaseMessagingService;
import com.example.eventapp.Service.apiService;
import com.example.eventapp.Model.Utilities;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private ArrayList<Item> events;
    RecyclerView recyclerView;
    private EventRecyclerAdapter eventAdapter;
    private apiService apiInit = new apiService();
    private ProgressDialog pDialog;
    apiInterface apiI = apiService.getClient().create(apiInterface.class);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init recycler view
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        pDialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        pDialog.setCancelable(false);

        if (getIntent().getExtras()!=null){

            String event_id;

            for (String key : getIntent().getExtras().keySet()){

                if (key.equals("event_id")){
                    event_id = getIntent().getExtras().getString(key);
                    goToDetail(event_id);
                }

            }
        }

        //melakukan pencarian data
        doSearch("","quran");



        //listener buat item click dari recycler
        recyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(getApplicationContext(), recyclerView, new CustomItemClickListener() {

            @Override
                public void onClick(View view, int position) {

                    String eventID = events.get(position).getId();


                goToDetail(eventID);

                }

                @Override
                public void onLongClick(View view, int position) {


                    String eventID = events.get(position).getId();

                    goToDetail(eventID);
                }
        }));

    }

    //procedur ke DetailActivity
    void goToDetail(String eventId){

        Intent intent = new Intent(this,DetailActivity.class);
        intent.putExtra("eventId", eventId);

        startActivity(intent);
    }

    public void doSearch(String keyword, String type) {

        Call<ArrayList<Item>> Itemmm;
        pDialog.setMessage(" Harap Tunggu ");
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        showDialog();

        if("".equals(keyword)) {
            Itemmm = apiI.getJenis(type,0);
        } else {
            Itemmm = apiI.getSearch(type,keyword,0);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);


        //Input data to recycler view
        Itemmm.enqueue(new Callback<ArrayList<Item>>() {
            @Override
            public void onResponse(Call<ArrayList<Item>> call, Response<ArrayList<Item>> response) {

                if(response.body() == null) {

                    Toast.makeText(getApplicationContext(), " Data Not Found ", Toast.LENGTH_LONG).show();
                    recyclerView.setVisibility(View.GONE);

                }else {

                    hideDialog();
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

                Toast.makeText(getApplicationContext(), " Error, Try Again ", Toast.LENGTH_LONG).show();
                recyclerView.setVisibility(View.GONE);
            }
        });
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
