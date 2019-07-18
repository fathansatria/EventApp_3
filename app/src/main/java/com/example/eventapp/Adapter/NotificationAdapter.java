package com.example.eventapp.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.eventapp.Model.NotifItem;
import com.example.eventapp.R;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private ArrayList<NotifItem> notifItems;
    private OnItemClickListener mListener;


    public interface OnItemClickListener {

        void onItemClick(int position);

    }

    public void setOnItemClickListener(OnItemClickListener listener) {

        mListener = listener;

    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {

        public TextView tvTitle, tvContent;

        public NotificationViewHolder(View itemView, final OnItemClickListener listener) {

            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_notif_title);
            tvContent = itemView.findViewById(R.id.tv_notif_content);


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

    public NotificationAdapter(ArrayList<NotifItem> notifItems) {
        this.notifItems = notifItems;
    }

    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notif_item, parent, false);
        NotificationViewHolder evh = new NotificationViewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(NotificationViewHolder holder, int position) {

        NotifItem currentItem = notifItems.get(position);

        holder.tvTitle.setText(currentItem.getNotifTitle());
        holder.tvContent.setText(currentItem.getNotifContent());
    }

    @Override
    public int getItemCount() {
        return notifItems.size();
    }

}
