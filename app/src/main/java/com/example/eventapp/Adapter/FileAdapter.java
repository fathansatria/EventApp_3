package com.example.eventapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.eventapp.Model.Files;
import com.example.eventapp.R;

import java.io.File;
import java.util.ArrayList;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.FilesViewHolder> {

    private ArrayList<Files> listOfFiles;
    private OnItemClickListener mListener;
    private Context context;


    public interface OnItemClickListener {

        void onItemClick(int position);
        void onDownloadClick(int position);

    }

    public void setOnItemClickListener(OnItemClickListener listener) {

        mListener = listener;

    }

    public static class FilesViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_file_name, tv_file_detail;
        private ImageButton btn_download;


        public FilesViewHolder(View itemView, final OnItemClickListener listener) {

            super(itemView);

            tv_file_name =  itemView.findViewById(R.id.tv_file_name);
            tv_file_detail =  itemView.findViewById(R.id.tv_file_detail);
            btn_download = itemView.findViewById(R.id.btn_Download);

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

            btn_download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDownloadClick(position);
                        }
                    }
                }
            });

        }
    }

    public FileAdapter(ArrayList<Files> items, Context context) {

        this.listOfFiles = items;
        this.context = context;
    }

    @Override
    public FilesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.file_item, parent, false);
        FilesViewHolder evh = new FilesViewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(FilesViewHolder holder, int position) {

        holder.tv_file_name.setText(String.valueOf(listOfFiles.get(position).getName()));
        holder.tv_file_detail.setText(listOfFiles.get(position).getDetail());

    }

    @Override
    public int getItemCount() {
        return listOfFiles.size();
    }

    public void addData(ArrayList<Files> items ){

        for (Files item : items){

            this.listOfFiles.add(item);

        }

        notifyDataSetChanged();
    }



}
