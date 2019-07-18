package com.example.eventapp.Adapter;

import android.content.Context;
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

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private ArrayList<Item> items;
    private OnItemClickListener mListener;
    private Context context;


    public interface OnItemClickListener {

        void onItemClick(int position);

    }

    public void setOnItemClickListener(OnItemClickListener listener) {

        mListener = listener;

    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_event_name, tv_event_desc;
        private ImageView iv_event_pics;


        public EventViewHolder(View itemView, final OnItemClickListener listener) {

            super(itemView);

            tv_event_name =  itemView.findViewById(R.id.tv_eventName);
            tv_event_desc =  itemView.findViewById(R.id.tv_eventDesc);
            iv_event_pics =  itemView.findViewById(R.id.iv_event);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

        }
    }

    public EventAdapter(ArrayList<Item> items, Context context) {

        this.items = items;
        this.context = context;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);
        EventViewHolder evh = new EventViewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {

        Glide.with(context).load(items.get(position).getImage()).into(holder.iv_event_pics);
        holder.tv_event_name.setText(String.valueOf(items.get(position).getTitle()));
        holder.tv_event_desc.setText(Utilities.FCurrency(Long.parseLong(items.get(position).getHarga().get(0).getHarga())));

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addData(ArrayList<Item> items ){

        for (Item item : items){

            this.items.add(item);

        }

        notifyDataSetChanged();
    }



}
