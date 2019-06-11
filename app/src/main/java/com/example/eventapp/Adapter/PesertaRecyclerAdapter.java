package com.example.eventapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.eventapp.DetailActivity;
import com.example.eventapp.MainActivity;
import com.example.eventapp.Model.EventModel;
import com.example.eventapp.Model.Item;
import com.example.eventapp.Model.PesertaModel;
import com.example.eventapp.Model.Utilities;
import com.example.eventapp.R;

import java.util.ArrayList;
import java.util.List;

public class PesertaRecyclerAdapter extends RecyclerView.Adapter<PesertaRecyclerAdapter.PesertaViewHolder> {

    private ArrayList<PesertaModel> pesertas;
    private String eventName, eventDesc;
    Context context;

    public PesertaRecyclerAdapter(ArrayList<PesertaModel> pesertas, Context context) {

        this.pesertas = pesertas;
        this.context = context;
    }

    @NonNull
    @Override
    public PesertaRecyclerAdapter.PesertaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.people_item, parent, false);
        return new PesertaViewHolder(view);


    }

    @Override
    public void onBindViewHolder(PesertaRecyclerAdapter.PesertaViewHolder holder, int position) {

        holder.tv_peserta_name.setText(String.valueOf(pesertas.get(position).getNamaPeserta()));
        holder.tv_peserta_phone.setText(pesertas.get(position).getPhone());
        holder.tv_peserta_email.setText(pesertas.get(position).getEmail());




    }

    @Override
    public int getItemCount() {
        return (pesertas != null) ? pesertas.size() : 0;
    }


    public static class PesertaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tv_peserta_name, tv_peserta_phone, tv_peserta_email;
        private ImageView iv_event_pics;

        //private CustomItemClickListener mListener;


        public PesertaViewHolder(View itemView) {

            super(itemView);
            tv_peserta_name = (TextView) itemView.findViewById(R.id.tv_pesertaName);
            tv_peserta_email = (TextView) itemView.findViewById(R.id.tv_pesertaEmail);
            tv_peserta_phone = (TextView) itemView.findViewById(R.id.tv_pesertaTelp);



        }


        @Override
        public void onClick(View v) {

            int pos = getLayoutPosition();

        }
    }



}
