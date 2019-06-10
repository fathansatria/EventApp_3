package com.example.eventapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.eventapp.Model.EventModel;
import com.example.eventapp.R;

import java.util.List;

public class EventListAdapter extends BaseAdapter {

    private List<EventModel> eventData;
    private Activity activity;
    private static LayoutInflater inflater=null;

    public EventListAdapter(Activity a, List<EventModel> eventData) {
        activity = a;
        this.eventData = eventData;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return eventData.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi =convertView;

        if(convertView==null)

            vi = inflater.inflate(R.layout.event_item,null);


        TextView tv_name= (TextView)vi.findViewById(R.id.tv_eventName);
        TextView tv_desc = (TextView)vi.findViewById(R.id.tv_eventDesc);


        EventModel event = eventData.get(position);

        tv_name.setText(event.getEventName());
        tv_desc.setText(event.getDescription());

        return vi;
    }
}
