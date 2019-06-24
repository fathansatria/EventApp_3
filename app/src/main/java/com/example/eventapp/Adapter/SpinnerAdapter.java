package com.example.eventapp.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.eventapp.Model.PesertaModel;

import java.util.ArrayList;
import java.util.List;

public class SpinnerAdapter extends ArrayAdapter<PesertaModel> {

    private Context context;
    private ArrayList<PesertaModel> values;

    public SpinnerAdapter(Context context, int textViewResourceId,
                       ArrayList<PesertaModel> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

//    @Override
//    public int getCount() {
//
//        // don't display last item. It is used as hint.
//
//        int count = super.getCount();
//
//        if (count > 0){
//            return count - 1;
//        }
//        else{
//            return count;
//        }
//
//    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        // Then you can get the current item using the values array (Users array) and the current position
        // You can NOW reference each method you has created in your bean object (User class)
        label.setText(values.get(position).getNamaPeserta());

        // And finally return your dynamic (or custom) view for each spinner item
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        View v = super.getDropDownView(position, convertView, parent);
        TextView tv = ((TextView) v);
        tv.setText(values.get(position).getNamaPeserta());
        tv.setTextColor(Color.BLACK);
        return v;

    }

}
