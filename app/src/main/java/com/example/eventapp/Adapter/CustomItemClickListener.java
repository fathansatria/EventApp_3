package com.example.eventapp.Adapter;

import android.view.View;

public interface CustomItemClickListener {
    public void onClick(View v, int position);
    void onLongClick(View view, int position);

}
