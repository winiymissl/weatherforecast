package com.example.weatherforecast.adpater;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weatherforecast.R;
import com.example.weatherforecast.utils.IconSelector;
import com.qweather.sdk.bean.weather.WeatherDailyBean;

import java.util.List;

public class ListViewWeatherAdapter extends BaseAdapter {
    private final Context context;//context of the main activity
    private final List list;//the data of the weather information

    public ListViewWeatherAdapter(Context context, List list) {
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
            view = View.inflate(context, R.layout.item_weather, null);
        } else {
            view = convertView;
        }

        ImageView imageView = view.findViewById(R.id.iv_weather);
        TextView time = view.findViewById(R.id.tv_time);
        TextView desc = view.findViewById(R.id.tv_desc);
        TextView tempMax = view.findViewById(R.id.tv_tempHigh);
        TextView tempMin = view.findViewById(R.id.tv_tempLow);

        WeatherDailyBean.DailyBean weatherInformation = (WeatherDailyBean.DailyBean) list.get(position);
//        imageView.setImageURI();
        imageView.setImageResource(IconSelector.getIcon(weatherInformation.getIconDay()));
        time.setText(weatherInformation.getFxDate());
        desc.setText(weatherInformation.getTextDay());
        tempMax.setText(weatherInformation.getTempMax());
        tempMin.setText(weatherInformation.getTempMin());
        return view;
    }
}