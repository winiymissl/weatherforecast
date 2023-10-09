package com.example.weatherforecast.adpater;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherforecast.R;
import com.example.weatherforecast.utils.IconSelector;
import com.qweather.sdk.bean.weather.WeatherHourlyBean;

import java.util.List;

public class RecyclerViewHourlyAdapter extends RecyclerView.Adapter {
    private Context context;
    private List list;

    public RecyclerViewHourlyAdapter(Context context, List list) {
        this.context = context;
        this.list = list;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textView_text;
        public TextView textView_temp;
        public ImageView imageView;
        public TextView textView_hourly;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_text = itemView.findViewById(R.id.tv_text_hourly);
            textView_temp = itemView.findViewById(R.id.tv_temp_hourly);
            imageView = itemView.findViewById(R.id.iv_icon_hourly);
            textView_hourly = itemView.findViewById(R.id.tv_time_hourly);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listview_hourly, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    public int count = 0;

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //从这里绑定数据和视图
        WeatherHourlyBean.HourlyBean weather = (WeatherHourlyBean.HourlyBean) list.get(position);
        ViewHolder v = (ViewHolder) holder;
        v.textView_text.setText(weather.getText());
        v.textView_temp.setText(weather.getTemp() + "℃");

//        Log.d("RecycerViewCpoount", Integer.valueOf(count++).toString());
        Log.d("getFxTime", weather.getTemp());
        v.imageView.setImageResource(IconSelector.getIcon(weather.getIcon()));
        String dateTimeString = weather.getFxTime();
        String timePart = dateTimeString.substring(11, 16);
        Log.d("getFxTime", weather.getFxTime());
        v.textView_hourly.setText(timePart);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
