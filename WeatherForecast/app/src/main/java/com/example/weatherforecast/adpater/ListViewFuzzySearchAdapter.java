package com.example.weatherforecast.adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.weatherforecast.R;
import com.qweather.sdk.bean.geo.GeoBean;

import java.util.List;

public class ListViewFuzzySearchAdapter extends BaseAdapter {
    private Context context;
    private List list;

    public ListViewFuzzySearchAdapter(Context context, List list) {
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
        View view = null;
        if (convertView == null) {
            view = View.inflate(context, R.layout.item_fuzzy_search, null);

        } else {
            view = convertView;
        }
        TextView textView = view.findViewById(R.id.tv_fuzzy_listview_1);
        GeoBean.LocationBean locationBean = (GeoBean.LocationBean) list.get(position);
        String text = locationBean.getAdm1() + "  " + locationBean.getAdm2() + "  " + locationBean.getName() + "  " + locationBean.getCountry();
        textView.setText(text);
        return view;
    }
}
