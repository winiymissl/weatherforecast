package com.example.weatherforecast.adpater;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.weatherforecast.R;

import java.util.List;

public class ListViewSettingsAdapter extends BaseAdapter {
    private List list;
    private Context context;

    public ListViewSettingsAdapter(Context context, List list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = View.inflate(context, R.layout.item_settings, null);
        } else {
            view = convertView;
        }
        String i = (String) list.get(position);
        TextView textView = view.findViewById(R.id.tv_settings);
        textView.setText(i);
        return view;
    }
}
