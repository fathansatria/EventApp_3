package com.example.eventapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.eventapp.Database.DatabaseHelper;
import com.example.eventapp.DetailActivity;
import com.example.eventapp.DetailActivity2;
import com.example.eventapp.Model.PesertaModel;
import com.example.eventapp.Model.NotifItem;
import com.example.eventapp.R;

import java.util.ArrayList;

public class PesertaRecyclerAdapter extends RecyclerView.Adapter<PesertaRecyclerAdapter.PesertaViewHolder> {

    private ArrayList<PesertaModel> pesertas;
    private String eventName, eventDesc;
    Context context;
    private DatabaseHelper db;

    public PesertaRecyclerAdapter(ArrayList<PesertaModel> pesertas, Context context, DatabaseHelper database) {

        this.pesertas = pesertas;
        this.context = context;
        this.db = database;
    }

    @NonNull
    @Override
    public PesertaRecyclerAdapter.PesertaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.people_item, parent, false);
        return new PesertaViewHolder(view);


    }

    @Override
    public void onBindViewHolder(PesertaRecyclerAdapter.PesertaViewHolder holder, final int position) {

        holder.tv_peserta_name.setText(String.valueOf(pesertas.get(position).getNamaPeserta()));
        holder.tv_peserta_phone.setText(pesertas.get(position).getPhone());
        holder.tv_peserta_email.setText(pesertas.get(position).getEmail());

        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                db.deletePesertaDaftar(pesertas.get(position));

                NotifItem n = new NotifItem();
                n.setNotifTitle(pesertas.get(position).getNamaPeserta());
                n.setNotifContent(pesertas.get(position).getPhone());
                n.setValue(pesertas.get(position).getId_event());

                db.deleteNotification(n);

                pesertas.remove(position);

                try {

                    DetailActivity.notifyAdapter();

                }
                catch (Exception e) {

                    try {

                        DetailActivity2.notifyAdapter();

                    }
                    catch (Exception e1) {

                        Log.d("Adapter error", "no adapter can notify");

                    }

                }


                db.closeDB();



            }
        });




    }

    @Override
    public int getItemCount() {
        return (pesertas != null) ? pesertas.size() : 0;
    }


    public static class PesertaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tv_peserta_name, tv_peserta_phone, tv_peserta_email;
        private ImageButton btn_delete;

        //private CustomItemClickListener mListener;


        public PesertaViewHolder(View itemView) {

            super(itemView);
            tv_peserta_name = itemView.findViewById(R.id.tv_pesertaName);
            tv_peserta_email = itemView.findViewById(R.id.tv_pesertaEmail);
            tv_peserta_phone = itemView.findViewById(R.id.tv_pesertaTelp);
            btn_delete = itemView.findViewById(R.id.btn_delete);


        }


        @Override
        public void onClick(View v) {

            int pos = getLayoutPosition();

        }
    }



}
