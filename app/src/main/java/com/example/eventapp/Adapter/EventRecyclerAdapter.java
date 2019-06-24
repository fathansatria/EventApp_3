package com.example.eventapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.eventapp.Model.Item;
import com.example.eventapp.Model.Utilities;
import com.example.eventapp.R;

import java.util.ArrayList;

public class EventRecyclerAdapter extends RecyclerView.Adapter<EventRecyclerAdapter.EventViewHolder> {

    private ArrayList<Item> dataList;
    private String eventName, eventDesc;
    Context context;

    public EventRecyclerAdapter(ArrayList<Item> dataList, Context context) {

        this.dataList = dataList;
        this.context = context;
    }

    @NonNull
    @Override
    public EventRecyclerAdapter.EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.event_item, parent, false);
        return new EventViewHolder(view);


    }

    @Override
    public void onBindViewHolder(EventRecyclerAdapter.EventViewHolder holder, int position) {

        Glide.with(context).load(dataList.get(position).getImage()).into(holder.iv_event_pics);
        holder.tv_event_name.setText(String.valueOf(dataList.get(position).getTitle()));
        holder.tv_event_desc.setText(Utilities.FCurrency(Long.parseLong(dataList.get(position).getHarga().get(0).getHarga())));


    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }


    public static class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tv_event_name, tv_event_desc;
        private ImageView iv_event_pics;

        //private CustomItemClickListener mListener;


        public EventViewHolder(View itemView) {

            super(itemView);
            tv_event_name = (TextView) itemView.findViewById(R.id.tv_eventName);
            tv_event_desc = (TextView) itemView.findViewById(R.id.tv_eventDesc);
            iv_event_pics = (ImageView) itemView.findViewById(R.id.iv_event);



        }


        @Override
        public void onClick(View v) {

            int pos = getLayoutPosition();

        }
    }



}
