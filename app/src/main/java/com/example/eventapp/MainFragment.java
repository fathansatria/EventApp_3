package com.example.eventapp;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.example.eventapp.Adapter.CustomItemClickListener;
import com.example.eventapp.Adapter.EventRecyclerAdapter;
import com.example.eventapp.Adapter.RecyclerViewTouchListener;
import com.example.eventapp.Database.DatabaseHelper;
import com.example.eventapp.Model.Item;
import com.example.eventapp.Model.apiInterface;
import com.example.eventapp.Service.apiService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainFragment extends Fragment {

    private RecyclerView recyclerView;
    private DatabaseHelper db;
    private ProgressDialog pDialog;
    private ArrayList<Item> events;
    private EventRecyclerAdapter eventAdapter;
    private apiService apiInit = new apiService();
    apiInterface apiI = apiService.getClient().create(apiInterface.class);



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        db = new DatabaseHelper(getActivity());
        recyclerView =  view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        pDialog = new ProgressDialog(getActivity(), R.style.MyAlertDialogStyle);
        pDialog.setCancelable(false);

        //melakukan pencarian data
        doSearch("","quran");

        //listener buat item click dari recycler
        recyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(getActivity(), recyclerView, new CustomItemClickListener() {

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

        return view;

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

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);


        //Input data to recycler view
        Itemmm.enqueue(new Callback<ArrayList<Item>>() {
            @Override
            public void onResponse(Call<ArrayList<Item>> call, Response<ArrayList<Item>> response) {

                if(response.body() == null) {

                    Toast.makeText(getActivity(), " Data Not Found ", Toast.LENGTH_LONG).show();
                    recyclerView.setVisibility(View.GONE);

                }else {

                    hideDialog();
                    events = new ArrayList<>(response.body());
                    eventAdapter = new EventRecyclerAdapter(new ArrayList<>(response.body()),getActivity());
                    eventAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(eventAdapter);
                    recyclerView.setVisibility(View.VISIBLE);

                }
            }
            @Override
            public void onFailure(Call<ArrayList<Item>> call, Throwable t) {

                Toast.makeText(getActivity(), " Error, Try Again ", Toast.LENGTH_LONG).show();
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

    void goToDetail(String eventId) {

        Intent intent = new Intent(getActivity(),DetailActivity.class);
        intent.putExtra("eventId", eventId);

        startActivity(intent);
    }



}
