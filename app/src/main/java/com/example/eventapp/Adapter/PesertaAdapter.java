package com.example.eventapp.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.eventapp.Model.PesertaModel;
import com.example.eventapp.R;

import java.util.ArrayList;

public class PesertaAdapter extends RecyclerView.Adapter<PesertaAdapter.PesertaViewHolder> {

    private ArrayList<PesertaModel> pesertaModels;
    private OnItemClickListener mListener;


    public interface OnItemClickListener {

        void onItemClick(int position);
        void onDeleteClick(int position);

    }

    public void setOnItemClickListener(OnItemClickListener listener) {

        mListener = listener;

    }

    public static class PesertaViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_peserta_name, tv_peserta_phone, tv_peserta_email;
        private ImageButton btn_delete;

        public PesertaViewHolder(View itemView, final OnItemClickListener listener) {

            super(itemView);

            tv_peserta_name = itemView.findViewById(R.id.tv_pesertaName);
            tv_peserta_email = itemView.findViewById(R.id.tv_pesertaEmail);
            tv_peserta_phone = itemView.findViewById(R.id.tv_pesertaTelp);
            btn_delete = itemView.findViewById(R.id.btn_delete);


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



            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }

    public PesertaAdapter(ArrayList<PesertaModel> pesertaModels) {

        this.pesertaModels = pesertaModels;
    }

    @Override
    public PesertaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.people_item, parent, false);
        PesertaViewHolder evh = new PesertaViewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(PesertaViewHolder holder, int position) {

        holder.tv_peserta_name.setText(String.valueOf(pesertaModels.get(position).getNamaPeserta()));
        holder.tv_peserta_phone.setText(pesertaModels.get(position).getPhone());
        holder.tv_peserta_email.setText(pesertaModels.get(position).getEmail());

    }

    @Override
    public int getItemCount() {
        return pesertaModels.size();
    }

}
