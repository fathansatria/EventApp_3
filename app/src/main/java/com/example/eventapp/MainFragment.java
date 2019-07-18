package com.example.eventapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.eventapp.Adapter.EndlessRecyclerViewOnScrollListener;
import com.example.eventapp.Adapter.EventAdapter;
import com.example.eventapp.Model.Item;
import com.example.eventapp.Model.apiInterface;
import com.example.eventapp.Service.apiService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProgressDialog pDialog;
    private ArrayList<Item> events;
    private EventAdapter eventAdapter;
    private apiService apiInit = new apiService();
    apiInterface apiI = apiService.getClient().create(apiInterface.class);



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        recyclerView =  view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        pDialog = new ProgressDialog(getActivity(), R.style.MyAlertDialogStyle);
        pDialog.setCancelable(false);

        //melakukan pencarian data
        doSearch("","foto");



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
                    initAdapter();


                }
            }
            @Override
            public void onFailure(Call<ArrayList<Item>> call, Throwable t) {

                Toast.makeText(getActivity(), " Error, Try Again ", Toast.LENGTH_LONG).show();
                recyclerView.setVisibility(View.GONE);
            }
        });

        recyclerView.addOnScrollListener(new EndlessRecyclerViewOnScrollListener(linearLayoutManager) {

//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//
//                super.onScrolled(recyclerView, dx, dy);
//
//                Log.i("testScroll", String.valueOf(dy));
//
//                if(dy > 0){ // only when scrolling up
//
//                    final int visibleThreshold = 2;
//
//                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//
//                    int lastItem  = 0;
//
//                    if (layoutManager != null) {
//                        lastItem = layoutManager.findLastCompletelyVisibleItemPosition();
//
//                        int currentTotalCount = layoutManager.getItemCount();
//
//                        if(currentTotalCount <= lastItem + visibleThreshold){
//
//                            loadNextPage(currentTotalCount);
//
//                        }
//                    }
//
//
//                }
//            }

            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                //Log.i("ereasrs", String.valueOf(totalItemsCount));
                //if (totalItemsCount > 4) {
                //progressBar.setVisibility(view.VISIBLE);
                loadNextPage(totalItemsCount);
                //Log.i("pagex", String.valueOf(totalItemsCount));
                //}

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

    public void loadNextPage(final int page) {
        if (page < 5)
            return;
        Call<ArrayList<Item>> Itemmm;

        if ("".equals("")) {
            Itemmm = apiI.getJenis("foto", page);
        } else {
            Itemmm = apiI.getSearch("foto", "", page);
        }
        Itemmm.enqueue(new Callback<ArrayList<Item>>() {
            @Override
            public void onResponse(Call<ArrayList<Item>> call, Response<ArrayList<Item>> response) {

                if (response.body() == null) {
                    //Log.i("re","empty");
                } else {
                    eventAdapter.addData(new ArrayList<>(response.body()));
                    //afoto.notifyItemRangeChanged(0, afoto.getItemCount());
                    //progressBar.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Call<ArrayList<Item>> call, Throwable t) {
                // progressBar.setVisibility(View.GONE);
            }
        });

    }


    void initAdapter () {

        eventAdapter = new EventAdapter(events,getActivity());
        eventAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(eventAdapter);
        recyclerView.setVisibility(View.VISIBLE);

        eventAdapter.setOnItemClickListener( new EventAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(int position) {

                String eventID = events.get(position).getId();
                goToDetail(eventID);

            }

        });
    }



}
