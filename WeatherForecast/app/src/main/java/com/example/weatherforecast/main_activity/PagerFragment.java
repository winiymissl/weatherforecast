package com.example.weatherforecast.main_activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.weatherforecast.R;
import com.example.weatherforecast.menu_activity.DangerActivity;
import com.example.weatherforecast.adpater.ListViewWeatherAdapter;
import com.example.weatherforecast.adpater.RecyclerViewHourlyAdapter;
import com.example.weatherforecast.database.MyDBH;
import com.example.weatherforecast.database.bean.DataBaseVariableNames;
import com.example.weatherforecast.utils.TableOperation;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.qweather.sdk.bean.base.Code;
import com.qweather.sdk.bean.warning.WarningBean;
import com.qweather.sdk.bean.weather.WeatherDailyBean;
import com.qweather.sdk.bean.weather.WeatherHourlyBean;
import com.qweather.sdk.view.HeConfig;
import com.qweather.sdk.view.QWeather;

import java.util.ArrayList;
import java.util.List;


public class PagerFragment extends Fragment implements View.OnClickListener {

    private Cursor cursor;
    private TextView textView_text;
    private TextView textView_danger;
    private RecyclerView recyclerView;
    private TextView textView_left;
    private TextView textView_down;
    private TextView textView_right;
    private TextView textView_right_down;
    private TextView textView_clothes;
    private TextView textView_temp;
    private TextView textView_name;
    private TextView textView_desc;
    private TextView textView_SPI;
    private TextView textView_car_wash;
    private TextView textView_comfort;
    private TextView textView_trip;
    private TextView textView_flow;

    private SwipeRefreshLayout swipeRefreshLayout;

    public PagerFragment() {
    }

    // 声明一个回调监听器
    public PagerFragment(Cursor cursor) {
        this.cursor = cursor;
        cursor.moveToFirst();
    }

    private String text;
    private ListView listView;

    @SuppressLint("Range")
    private void initFragment_other(Cursor cursor) {
        try {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textView_temp.setText(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_REALTIME_TEMP)) + "℃");
                    textView_desc.setText(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_REALTIME_TEXT)));
                    String province_name = cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_CITY_INFO_PROVINCE_NAME));
                    String city_name = cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_CITY_INFO_CITY_NAME));
                    String county_name = cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_CITY_INFO_COUNTY_NAME));
                    textView_name.setText(new String(province_name + "\n" + city_name + "市" + "\n" + county_name + "区"));
                    textView_text.setText(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_WEATHER_INFO_LIFE_SPORT)));
                }
            });
//
//            Thread thread = new Thread() {
//                @Override
//                public void run() {
//                    super.run();
            QWeather.getWarning(getActivity(), cursor.getString(cursor.getColumnIndex(DataBaseVariableNames._ID)), new QWeather.OnResultWarningListener() {
                @Override
                public void onError(Throwable throwable) {

                }

                @Override
                public void onSuccess(WarningBean warningBean) {

                    if (warningBean.getCode() == Code.OK) {

                        List<WarningBean.WarningBeanBase> list = warningBean.getWarningList();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textView_danger.setClickable(true);
                                textView_danger.setVisibility(View.VISIBLE);
                            }
                        });
                        if (list.size() > 0) {
                            String s = list.get(0).getText();
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    textView_danger.setText(s);
                                }
                            });
                        } else {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    textView_danger.setClickable(false);
                                    textView_danger.setVisibility(View.INVISIBLE);
                                }
                            });
                        }
                    }
                }
            });
//                }
//            };
//            thread.start();
            List<WeatherDailyBean.DailyBean> l = new ArrayList();
            WeatherDailyBean.DailyBean weatherDailyBean1 = new WeatherDailyBean.DailyBean();
            weatherDailyBean1.setTempMax(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_1DAY_TEMP_MAX)));
            weatherDailyBean1.setIconDay(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_1DAY_ICON)));
            weatherDailyBean1.setTempMin(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_1DAY_TEMP_MIN)));
            weatherDailyBean1.setTextDay(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_1DAY_DESC)));
            weatherDailyBean1.setFxDate(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_1DAY_TIME)));
            l.add(weatherDailyBean1);

            WeatherDailyBean.DailyBean weatherDailyBean2 = new WeatherDailyBean.DailyBean();
            weatherDailyBean2.setTempMax(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_2DAY_TEMP_MAX)));
            weatherDailyBean2.setIconDay(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_2DAY_ICON)));
            weatherDailyBean2.setTempMin(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_2DAY_TEMP_MIN)));
            weatherDailyBean2.setTextDay(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_2DAY_DESC)));
            weatherDailyBean2.setFxDate(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_2DAY_TIME)));
            l.add(weatherDailyBean2);

            WeatherDailyBean.DailyBean weatherDailyBean3 = new WeatherDailyBean.DailyBean();
            weatherDailyBean3.setTempMax(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_3DAY_TEMP_MAX)));
            weatherDailyBean3.setIconDay(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_3DAY_ICON)));
            weatherDailyBean3.setTempMin(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_3DAY_TEMP_MIN)));
            weatherDailyBean3.setTextDay(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_3DAY_DESC)));
            weatherDailyBean3.setFxDate(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_3DAY_TIME)));
            l.add(weatherDailyBean3);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ListViewWeatherAdapter listViewAdapter = new ListViewWeatherAdapter(getActivity(), l);
                    listView.setAdapter(listViewAdapter);
                }
            });
            List<WeatherHourlyBean.HourlyBean> hourList = new ArrayList();
            WeatherHourlyBean.HourlyBean hourlyBean1 = new WeatherHourlyBean.HourlyBean();
            hourlyBean1.setText(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_1_DESC)));
            hourlyBean1.setFxTime(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_1_TIME)));
            hourlyBean1.setTemp(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_1_TEMP)));
            hourlyBean1.setIcon(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_1_ICON)));
            hourList.add(hourlyBean1);

            WeatherHourlyBean.HourlyBean hourlyBean2 = new WeatherHourlyBean.HourlyBean();
            hourlyBean2.setText(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_2_DESC)));
            hourlyBean2.setFxTime(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_2_TIME)));
            hourlyBean2.setTemp(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_2_TEMP)));
            hourlyBean2.setIcon(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_2_ICON)));
            hourList.add(hourlyBean2);

            WeatherHourlyBean.HourlyBean hourlyBean3 = new WeatherHourlyBean.HourlyBean();
            hourlyBean3.setText(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_3_DESC)));
            hourlyBean3.setFxTime(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_3_TIME)));
            hourlyBean3.setIcon(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_3_ICON)));
            hourlyBean3.setTemp(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_3_TEMP)));
            hourList.add(hourlyBean3);

            WeatherHourlyBean.HourlyBean hourlyBean4 = new WeatherHourlyBean.HourlyBean();
            hourlyBean4.setText(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_4_DESC)));
            hourlyBean4.setIcon(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_4_ICON)));
            hourlyBean4.setFxTime(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_4_TIME)));
            hourlyBean4.setTemp(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_4_TEMP)));
            hourList.add(hourlyBean4);

            WeatherHourlyBean.HourlyBean hourlyBean5 = new WeatherHourlyBean.HourlyBean();
            hourlyBean5.setText(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_5_DESC)));
            hourlyBean5.setFxTime(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_5_TIME)));
            hourlyBean5.setIcon(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_5_ICON)));
            hourlyBean5.setTemp(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_5_TEMP)));
            hourList.add(hourlyBean5);

            WeatherHourlyBean.HourlyBean hourlyBean6 = new WeatherHourlyBean.HourlyBean();
            hourlyBean6.setText(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_6_DESC)));
            hourlyBean6.setIcon(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_6_ICON)));
            hourlyBean6.setFxTime(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_6_TIME)));
            hourlyBean6.setTemp(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_6_TEMP)));
            hourList.add(hourlyBean6);

            WeatherHourlyBean.HourlyBean hourlyBean7 = new WeatherHourlyBean.HourlyBean();
            hourlyBean7.setText(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_7_DESC)));
            hourlyBean7.setFxTime(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_7_TIME)));
            hourlyBean7.setIcon(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_7_ICON)));
            hourlyBean7.setTemp(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_7_TEMP)));
            hourList.add(hourlyBean7);

            WeatherHourlyBean.HourlyBean hourlyBean8 = new WeatherHourlyBean.HourlyBean();
            hourlyBean8.setText(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_8_DESC)));
            hourlyBean8.setFxTime(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_8_TIME)));
            hourlyBean8.setIcon(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_8_ICON)));
            hourlyBean8.setTemp(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_8_TEMP)));
            hourList.add(hourlyBean8);

            WeatherHourlyBean.HourlyBean hourlyBean9 = new WeatherHourlyBean.HourlyBean();
            hourlyBean9.setText(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_9_DESC)));
            hourlyBean9.setFxTime(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_9_TIME)));
            hourlyBean9.setIcon(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_9_ICON)));
            hourlyBean9.setTemp(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_9_TEMP)));
            hourList.add(hourlyBean9);


            WeatherHourlyBean.HourlyBean hourlyBean10 = new WeatherHourlyBean.HourlyBean();
            hourlyBean10.setText(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_10_DESC)));
            hourlyBean10.setIcon(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_10_ICON)));
            hourlyBean10.setFxTime(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_10_TIME)));
            hourlyBean10.setTemp(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_10_TEMP)));
            hourList.add(hourlyBean10);

            WeatherHourlyBean.HourlyBean hourlyBean11 = new WeatherHourlyBean.HourlyBean();
            hourlyBean11.setText(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_11_DESC)));
            hourlyBean11.setFxTime(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_11_TIME)));
            hourlyBean11.setIcon(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_11_ICON)));
            hourlyBean11.setTemp(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_11_TEMP)));
            hourList.add(hourlyBean11);

            WeatherHourlyBean.HourlyBean hourlyBean12 = new WeatherHourlyBean.HourlyBean();
            hourlyBean12.setText(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_12_DESC)));
            hourlyBean12.setFxTime(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_12_TIME)));
            hourlyBean12.setIcon(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_12_ICON)));
            hourlyBean12.setTemp(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_12_TEMP)));
            hourList.add(hourlyBean12);

            WeatherHourlyBean.HourlyBean hourlyBean13 = new WeatherHourlyBean.HourlyBean();
            hourlyBean13.setText(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_13_DESC)));
            hourlyBean13.setIcon(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_13_ICON)));
            hourlyBean13.setFxTime(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_13_TIME)));
            hourlyBean13.setTemp(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_13_TEMP)));
            hourList.add(hourlyBean13);

            WeatherHourlyBean.HourlyBean hourlyBean14 = new WeatherHourlyBean.HourlyBean();
            hourlyBean14.setText(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_14_DESC)));
            hourlyBean14.setFxTime(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_14_TIME)));
            hourlyBean14.setIcon(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_14_ICON)));
            hourlyBean14.setTemp(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_14_TEMP)));
            hourList.add(hourlyBean14);

            WeatherHourlyBean.HourlyBean hourlyBean15 = new WeatherHourlyBean.HourlyBean();
            hourlyBean15.setText(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_15_DESC)));
            hourlyBean15.setIcon(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_15_ICON)));
            hourlyBean15.setFxTime(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_15_TIME)));
            hourlyBean15.setTemp(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_15_TEMP)));
            hourList.add(hourlyBean15);

            WeatherHourlyBean.HourlyBean hourlyBean16 = new WeatherHourlyBean.HourlyBean();
            hourlyBean16.setText(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_16_DESC)));
            hourlyBean16.setIcon(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_16_ICON)));
            hourlyBean16.setFxTime(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_16_TIME)));
            hourlyBean16.setTemp(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_16_TEMP)));
            hourList.add(hourlyBean16);

            WeatherHourlyBean.HourlyBean hourlyBean17 = new WeatherHourlyBean.HourlyBean();
            hourlyBean17.setText(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_17_DESC)));
            hourlyBean17.setFxTime(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_17_TIME)));
            hourlyBean17.setIcon(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_17_ICON)));
            hourlyBean17.setTemp(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_17_TEMP)));
            hourList.add(hourlyBean17);

            WeatherHourlyBean.HourlyBean hourlyBean18 = new WeatherHourlyBean.HourlyBean();
            hourlyBean18.setText(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_18_DESC)));
            hourlyBean18.setIcon(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_18_ICON)));
            hourlyBean18.setFxTime(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_18_TIME)));
            hourlyBean18.setTemp(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_18_TEMP)));
            hourList.add(hourlyBean18);

            WeatherHourlyBean.HourlyBean hourlyBean19 = new WeatherHourlyBean.HourlyBean();
            hourlyBean19.setText(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_19_DESC)));
            hourlyBean19.setIcon(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_19_ICON)));
            hourlyBean19.setFxTime(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_19_TIME)));
            hourlyBean19.setTemp(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_19_TEMP)));
            hourList.add(hourlyBean19);

            WeatherHourlyBean.HourlyBean hourlyBean20 = new WeatherHourlyBean.HourlyBean();
            hourlyBean20.setText(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_20_DESC)));
            hourlyBean20.setIcon(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_20_ICON)));
            hourlyBean20.setFxTime(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_20_TIME)));
            hourlyBean20.setTemp(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_20_TEMP)));
            hourList.add(hourlyBean20);

            WeatherHourlyBean.HourlyBean hourlyBean21 = new WeatherHourlyBean.HourlyBean();
            hourlyBean21.setText(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_21_DESC)));
            hourlyBean21.setIcon(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_21_ICON)));
            hourlyBean21.setFxTime(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_21_TIME)));
            hourlyBean21.setTemp(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_21_TEMP)));
            hourList.add(hourlyBean21);

            WeatherHourlyBean.HourlyBean hourlyBean22 = new WeatherHourlyBean.HourlyBean();
            hourlyBean22.setText(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_22_DESC)));
            hourlyBean22.setIcon(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_22_ICON)));
            hourlyBean22.setFxTime(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_22_TIME)));
            hourlyBean22.setTemp(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_22_TEMP)));
            hourList.add(hourlyBean22);

            WeatherHourlyBean.HourlyBean hourlyBean23 = new WeatherHourlyBean.HourlyBean();
            hourlyBean23.setText(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_23_DESC)));
            hourlyBean23.setFxTime(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_23_TIME)));
            hourlyBean23.setIcon(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_23_ICON)));
            hourlyBean23.setTemp(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_23_TEMP)));
            hourList.add(hourlyBean23);

            WeatherHourlyBean.HourlyBean hourlyBean24 = new WeatherHourlyBean.HourlyBean();
            hourlyBean24.setText(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_24_DESC)));
            hourlyBean24.setFxTime(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_24_TIME)));
            hourlyBean24.setIcon(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_24_ICON)));
            hourlyBean24.setTemp(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_24_TEMP)));
            hourList.add(hourlyBean24);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                    linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    RecyclerViewHourlyAdapter recyclerViewHourlyAdapter = new RecyclerViewHourlyAdapter(getActivity(), hourList);
                    recyclerView.setAdapter(recyclerViewHourlyAdapter);
//                    recyclerViewHourlyAdapter.notifyDataSetChanged();
                    textView_left.setText("PM2.5:" + "\n" + cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_WEATHER_INFO_AIR_NOW_PM2_5)));
                    textView_down.setText("空气质量级数:" + "\n" + cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_WEATHER_INFO_AIR_NOW_LEVEL)));
                    textView_right.setText("空气质量等级:");
                    textView_right_down.setText(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_WEATHER_INFO_AIR_NOW_CATEGORY)));
                }
            });
        } catch (Exception e) {
            Log.d("FragmentException", e.toString());
        }
    }

    public Cursor getCursor() {
        return cursor;
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    @SuppressLint("Range")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = null;
        try {
            rootView = inflater.inflate(R.layout.fragment_pager, container, false);
            HeConfig.init("HE2309190206361969", "4e5102dba60a43c7ae910bc0e195634b");
            swipeRefreshLayout = rootView.findViewById(R.id.srl);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    getActivity().runOnUiThread(new Runnable() {
                        @SuppressLint("Range")
                        @Override
                        public void run() {
                            String city_name = cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_CITY_INFO_CITY_NAME));
                            String county_name = cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_CITY_INFO_COUNTY_NAME));
                            TableOperation.inTable(getContext(), city_name, county_name);
                            try {
                                Thread.sleep(700);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            MyDBH myDBH = MyDBH.getMyDBH(getActivity());
                            Cursor query = myDBH.query(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames._ID)));
                            query.moveToFirst();
                            initFragment_other(query);
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });
                }
            });
            listView = rootView.findViewById(R.id.lv);
            textView_temp = rootView.findViewById(R.id.tv);
            textView_name = rootView.findViewById(R.id.tv_name);
            textView_desc = rootView.findViewById(R.id.tv_);
            textView_danger = rootView.findViewById(R.id.tv_danger);
            textView_danger.setOnClickListener(this);
            textView_text = rootView.findViewById(R.id.tv_text);
            recyclerView = rootView.findViewById(R.id.rv_hourly);
            textView_left = rootView.findViewById(R.id.tv_left_up);
            textView_down = rootView.findViewById(R.id.tv_left_down);
            textView_right = rootView.findViewById(R.id.tv_right);
            textView_right_down = rootView.findViewById(R.id.tv_right_down);
            textView_clothes = rootView.findViewById(R.id.tv_clothes);

            textView_clothes.setOnClickListener(this);
            textView_SPI = rootView.findViewById(R.id.tv_SPI);
            textView_SPI.setOnClickListener(this);
            textView_car_wash = rootView.findViewById(R.id.tv_carWash);
            textView_car_wash.setOnClickListener(this);
            textView_comfort = rootView.findViewById(R.id.tv_comfort);
            textView_comfort.setOnClickListener(this);

            textView_trip = rootView.findViewById(R.id.tv_trip);
            textView_trip.setOnClickListener(this);
            textView_flow = rootView.findViewById(R.id.tv_flow);
            textView_flow.setOnClickListener(this);


            initFragment();
        } catch (Exception e) {
            Log.d("FragmentCheckIsSuccess", e.toString());
        }
        return rootView;
    }

    @SuppressLint("Range")
    private void initFragment() {

        try {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textView_temp.setText(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_REALTIME_TEMP)) + "℃");
                    textView_desc.setText(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_REALTIME_TEXT)));
                    String province_name = cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_CITY_INFO_PROVINCE_NAME));
                    String city_name = cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_CITY_INFO_CITY_NAME));
                    String county_name = cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_CITY_INFO_COUNTY_NAME));
                    textView_name.setText(new String(province_name + "\n" + city_name + "市" + "\n" + county_name + "区"));
                    textView_text.setText(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_WEATHER_INFO_LIFE_SPORT)));
                }
            });
//
//            Thread thread = new Thread() {
//                @Override
//                public void run() {
//                    super.run();
            QWeather.getWarning(getActivity(), cursor.getString(cursor.getColumnIndex(DataBaseVariableNames._ID)), new QWeather.OnResultWarningListener() {
                @Override
                public void onError(Throwable throwable) {

                }

                @Override
                public void onSuccess(WarningBean warningBean) {
                    if (warningBean.getCode() == Code.OK) {
                        List<WarningBean.WarningBeanBase> list = warningBean.getWarningList();
                        textView_danger.setClickable(true);
                        textView_danger.setVisibility(View.VISIBLE);
                        if (list.size() > 0) {
                            String s = list.get(0).getText();
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    textView_danger.setText(s);
                                }
                            });
                        } else {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    textView_danger.setClickable(false);
                                    textView_danger.setVisibility(View.INVISIBLE);
                                }
                            });
                        }
                    }
                }
            });
            List<WeatherDailyBean.DailyBean> l = new ArrayList();
            WeatherDailyBean.DailyBean weatherDailyBean1 = new WeatherDailyBean.DailyBean();
            weatherDailyBean1.setTempMax(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_1DAY_TEMP_MAX)));
            weatherDailyBean1.setIconDay(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_1DAY_ICON)));
            weatherDailyBean1.setTempMin(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_1DAY_TEMP_MIN)));
            weatherDailyBean1.setTextDay(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_1DAY_DESC)));
            weatherDailyBean1.setFxDate(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_1DAY_TIME)));
            l.add(weatherDailyBean1);

            WeatherDailyBean.DailyBean weatherDailyBean2 = new WeatherDailyBean.DailyBean();
            weatherDailyBean2.setTempMax(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_2DAY_TEMP_MAX)));
            weatherDailyBean2.setIconDay(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_2DAY_ICON)));
            weatherDailyBean2.setTempMin(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_2DAY_TEMP_MIN)));
            weatherDailyBean2.setTextDay(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_2DAY_DESC)));
            weatherDailyBean2.setFxDate(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_2DAY_TIME)));
            l.add(weatherDailyBean2);

            WeatherDailyBean.DailyBean weatherDailyBean3 = new WeatherDailyBean.DailyBean();
            weatherDailyBean3.setTempMax(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_3DAY_TEMP_MAX)));
            weatherDailyBean3.setIconDay(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_3DAY_ICON)));
            weatherDailyBean3.setTempMin(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_3DAY_TEMP_MIN)));
            weatherDailyBean3.setTextDay(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_3DAY_DESC)));
            weatherDailyBean3.setFxDate(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_3DAY_TIME)));
            l.add(weatherDailyBean3);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ListViewWeatherAdapter listViewAdapter = new ListViewWeatherAdapter(getActivity(), l);
                    listView.setAdapter(listViewAdapter);
                }
            });
//                }
//            };
//            thread.start();
            List<WeatherHourlyBean.HourlyBean> hourList = new ArrayList();
            WeatherHourlyBean.HourlyBean hourlyBean1 = new WeatherHourlyBean.HourlyBean();
            hourlyBean1.setText(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_1_DESC)));
            hourlyBean1.setFxTime(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_1_TIME)));
            hourlyBean1.setTemp(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_1_TEMP)));
            hourlyBean1.setIcon(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_1_ICON)));
            hourList.add(hourlyBean1);

            WeatherHourlyBean.HourlyBean hourlyBean2 = new WeatherHourlyBean.HourlyBean();
            hourlyBean2.setText(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_2_DESC)));
            hourlyBean2.setFxTime(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_2_TIME)));
            hourlyBean2.setTemp(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_2_TEMP)));
            hourlyBean2.setIcon(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_2_ICON)));
            hourList.add(hourlyBean2);

            WeatherHourlyBean.HourlyBean hourlyBean3 = new WeatherHourlyBean.HourlyBean();
            hourlyBean3.setText(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_3_DESC)));
            hourlyBean3.setFxTime(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_3_TIME)));
            hourlyBean3.setIcon(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_3_ICON)));
            hourlyBean3.setTemp(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_3_TEMP)));
            hourList.add(hourlyBean3);

            WeatherHourlyBean.HourlyBean hourlyBean4 = new WeatherHourlyBean.HourlyBean();
            hourlyBean4.setText(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_4_DESC)));
            hourlyBean4.setIcon(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_4_ICON)));
            hourlyBean4.setFxTime(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_4_TIME)));
            hourlyBean4.setTemp(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_4_TEMP)));
            hourList.add(hourlyBean4);

            WeatherHourlyBean.HourlyBean hourlyBean5 = new WeatherHourlyBean.HourlyBean();
            hourlyBean5.setText(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_5_DESC)));
            hourlyBean5.setFxTime(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_5_TIME)));
            hourlyBean5.setIcon(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_5_ICON)));
            hourlyBean5.setTemp(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_5_TEMP)));
            hourList.add(hourlyBean5);

            WeatherHourlyBean.HourlyBean hourlyBean6 = new WeatherHourlyBean.HourlyBean();
            hourlyBean6.setText(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_6_DESC)));
            hourlyBean6.setIcon(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_6_ICON)));
            hourlyBean6.setFxTime(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_6_TIME)));
            hourlyBean6.setTemp(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_6_TEMP)));
            hourList.add(hourlyBean6);

            WeatherHourlyBean.HourlyBean hourlyBean7 = new WeatherHourlyBean.HourlyBean();
            hourlyBean7.setText(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_7_DESC)));
            hourlyBean7.setFxTime(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_7_TIME)));
            hourlyBean7.setIcon(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_7_ICON)));
            hourlyBean7.setTemp(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_7_TEMP)));
            hourList.add(hourlyBean7);

            WeatherHourlyBean.HourlyBean hourlyBean8 = new WeatherHourlyBean.HourlyBean();
            hourlyBean8.setText(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_8_DESC)));
            hourlyBean8.setFxTime(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_8_TIME)));
            hourlyBean8.setIcon(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_8_ICON)));
            hourlyBean8.setTemp(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_8_TEMP)));
            hourList.add(hourlyBean8);

            WeatherHourlyBean.HourlyBean hourlyBean9 = new WeatherHourlyBean.HourlyBean();
            hourlyBean9.setText(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_9_DESC)));
            hourlyBean9.setFxTime(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_9_TIME)));
            hourlyBean9.setIcon(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_9_ICON)));
            hourlyBean9.setTemp(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_9_TEMP)));
            hourList.add(hourlyBean9);


            WeatherHourlyBean.HourlyBean hourlyBean10 = new WeatherHourlyBean.HourlyBean();
            hourlyBean10.setText(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_10_DESC)));
            hourlyBean10.setIcon(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_10_ICON)));
            hourlyBean10.setFxTime(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_10_TIME)));
            hourlyBean10.setTemp(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_10_TEMP)));
            hourList.add(hourlyBean10);

            WeatherHourlyBean.HourlyBean hourlyBean11 = new WeatherHourlyBean.HourlyBean();
            hourlyBean11.setText(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_11_DESC)));
            hourlyBean11.setFxTime(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_11_TIME)));
            hourlyBean11.setIcon(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_11_ICON)));
            hourlyBean11.setTemp(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_11_TEMP)));
            hourList.add(hourlyBean11);

            WeatherHourlyBean.HourlyBean hourlyBean12 = new WeatherHourlyBean.HourlyBean();
            hourlyBean12.setText(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_12_DESC)));
            hourlyBean12.setFxTime(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_12_TIME)));
            hourlyBean12.setIcon(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_12_ICON)));
            hourlyBean12.setTemp(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_12_TEMP)));
            hourList.add(hourlyBean12);

            WeatherHourlyBean.HourlyBean hourlyBean13 = new WeatherHourlyBean.HourlyBean();
            hourlyBean13.setText(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_13_DESC)));
            hourlyBean13.setIcon(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_13_ICON)));
            hourlyBean13.setFxTime(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_13_TIME)));
            hourlyBean13.setTemp(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_13_TEMP)));
            hourList.add(hourlyBean13);

            WeatherHourlyBean.HourlyBean hourlyBean14 = new WeatherHourlyBean.HourlyBean();
            hourlyBean14.setText(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_14_DESC)));
            hourlyBean14.setFxTime(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_14_TIME)));
            hourlyBean14.setIcon(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_14_ICON)));
            hourlyBean14.setTemp(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_14_TEMP)));
            hourList.add(hourlyBean14);

            WeatherHourlyBean.HourlyBean hourlyBean15 = new WeatherHourlyBean.HourlyBean();
            hourlyBean15.setText(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_15_DESC)));
            hourlyBean15.setIcon(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_15_ICON)));
            hourlyBean15.setFxTime(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_15_TIME)));
            hourlyBean15.setTemp(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_15_TEMP)));
            hourList.add(hourlyBean15);

            WeatherHourlyBean.HourlyBean hourlyBean16 = new WeatherHourlyBean.HourlyBean();
            hourlyBean16.setText(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_16_DESC)));
            hourlyBean16.setIcon(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_16_ICON)));
            hourlyBean16.setFxTime(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_16_TIME)));
            hourlyBean16.setTemp(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_16_TEMP)));
            hourList.add(hourlyBean16);

            WeatherHourlyBean.HourlyBean hourlyBean17 = new WeatherHourlyBean.HourlyBean();
            hourlyBean17.setText(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_17_DESC)));
            hourlyBean17.setFxTime(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_17_TIME)));
            hourlyBean17.setIcon(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_17_ICON)));
            hourlyBean17.setTemp(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_17_TEMP)));
            hourList.add(hourlyBean17);

            WeatherHourlyBean.HourlyBean hourlyBean18 = new WeatherHourlyBean.HourlyBean();
            hourlyBean18.setText(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_18_DESC)));
            hourlyBean18.setIcon(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_18_ICON)));
            hourlyBean18.setFxTime(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_18_TIME)));
            hourlyBean18.setTemp(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_18_TEMP)));
            hourList.add(hourlyBean18);

            WeatherHourlyBean.HourlyBean hourlyBean19 = new WeatherHourlyBean.HourlyBean();
            hourlyBean19.setText(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_19_DESC)));
            hourlyBean19.setIcon(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_19_ICON)));
            hourlyBean19.setFxTime(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_19_TIME)));
            hourlyBean19.setTemp(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_19_TEMP)));
            hourList.add(hourlyBean19);

            WeatherHourlyBean.HourlyBean hourlyBean20 = new WeatherHourlyBean.HourlyBean();
            hourlyBean20.setText(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_20_DESC)));
            hourlyBean20.setIcon(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_20_ICON)));
            hourlyBean20.setFxTime(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_20_TIME)));
            hourlyBean20.setTemp(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_20_TEMP)));
            hourList.add(hourlyBean20);

            WeatherHourlyBean.HourlyBean hourlyBean21 = new WeatherHourlyBean.HourlyBean();
            hourlyBean21.setText(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_21_DESC)));
            hourlyBean21.setIcon(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_21_ICON)));
            hourlyBean21.setFxTime(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_21_TIME)));
            hourlyBean21.setTemp(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_21_TEMP)));
            hourList.add(hourlyBean21);

            WeatherHourlyBean.HourlyBean hourlyBean22 = new WeatherHourlyBean.HourlyBean();
            hourlyBean22.setText(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_22_DESC)));
            hourlyBean22.setIcon(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_22_ICON)));
            hourlyBean22.setFxTime(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_22_TIME)));
            hourlyBean22.setTemp(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_22_TEMP)));
            hourList.add(hourlyBean22);

            WeatherHourlyBean.HourlyBean hourlyBean23 = new WeatherHourlyBean.HourlyBean();
            hourlyBean23.setText(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_23_DESC)));
            hourlyBean23.setFxTime(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_23_TIME)));
            hourlyBean23.setIcon(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_23_ICON)));
            hourlyBean23.setTemp(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_23_TEMP)));
            hourList.add(hourlyBean23);

            WeatherHourlyBean.HourlyBean hourlyBean24 = new WeatherHourlyBean.HourlyBean();
            hourlyBean24.setText(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_24_DESC)));
            hourlyBean24.setFxTime(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_24_TIME)));
            hourlyBean24.setIcon(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_24_ICON)));
            hourlyBean24.setTemp(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_24_TEMP)));
            hourList.add(hourlyBean24);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                    linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    RecyclerViewHourlyAdapter recyclerViewHourlyAdapter = new RecyclerViewHourlyAdapter(getActivity(), hourList);
                    recyclerView.setAdapter(recyclerViewHourlyAdapter);
                    textView_left.setText("PM2.5:" + "\n" + cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_WEATHER_INFO_AIR_NOW_PM2_5)));
                    textView_down.setText("空气质量级数:" + "\n" + cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_WEATHER_INFO_AIR_NOW_LEVEL)));
                    textView_right.setText("空气质量等级:");
                    textView_right_down.setText(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_WEATHER_INFO_AIR_NOW_CATEGORY)));
                    textView_clothes.setText("Clothes");
                    textView_SPI.setText("SPI");
                    textView_car_wash.setText("cAr\nWaSh");
                    textView_comfort.setText("Comfort");
                    textView_trip.setText("Trip");
                    textView_flow.setText("Flow");
                }
            });
        } catch (Exception e) {
            Log.d("FragmentException", e.toString());
        }
    }

    @SuppressLint("MissingInflatedId")

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_danger) {
            Intent in = new Intent(getActivity(), DangerActivity.class);
            Bundle b = new Bundle();
            String i = textView_danger.getText().toString();
            b.putString("text", i);
            in.putExtras(b);
            startActivity(in);
        }
        if (v.getId() == R.id.tv_clothes) {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialog);
            View view = getLayoutInflater().inflate(R.layout.item_bottomsheetdialog, null);
            TextView textView_bottomDialog = view.findViewById(R.id.tv_bottom_dialog_text);
            getActivity().runOnUiThread(new Runnable() {
                @SuppressLint("Range")
                @Override
                public void run() {
                    textView_bottomDialog.setText(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_WEATHER_INFO_LIFE_CLOTHES)));
                }
            });
            bottomSheetDialog.setContentView(view);
            bottomSheetDialog.show();
        }
        if (v.getId() == R.id.tv_SPI) {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialog);
            View view = getLayoutInflater().inflate(R.layout.item_bottomsheetdialog, null);
            TextView textView_bottomDialog = view.findViewById(R.id.tv_bottom_dialog_text);
            getActivity().runOnUiThread(new Runnable() {
                @SuppressLint("Range")
                @Override
                public void run() {
                    textView_bottomDialog.setText(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_WEATHER_INFO_LIFE_SPI)));
                }
            });
            bottomSheetDialog.setContentView(view);
            bottomSheetDialog.show();
        }
        if (v.getId() == R.id.tv_carWash) {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialog);
            View view = getLayoutInflater().inflate(R.layout.item_bottomsheetdialog, null);
            TextView textView_bottomDialog = view.findViewById(R.id.tv_bottom_dialog_text);
            getActivity().runOnUiThread(new Runnable() {
                @SuppressLint("Range")
                @Override
                public void run() {
                    textView_bottomDialog.setText(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_WEATHER_INFO_LIFE_CAR_WASH)));
                }
            });
            bottomSheetDialog.setContentView(view);
            bottomSheetDialog.show();
        }
        if (v.getId() == R.id.tv_comfort) {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialog);
            View view = getLayoutInflater().inflate(R.layout.item_bottomsheetdialog, null);
            TextView textView_bottomDialog = view.findViewById(R.id.tv_bottom_dialog_text);
            getActivity().runOnUiThread(new Runnable() {
                @SuppressLint("Range")
                @Override
                public void run() {
                    textView_bottomDialog.setText(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_WEATHER_INFO_LIFE_COMFORT)));
                }
            });
            bottomSheetDialog.setContentView(view);
            bottomSheetDialog.show();
        }
        if (v.getId() == R.id.tv_flow) {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialog);
            View view = getLayoutInflater().inflate(R.layout.item_bottomsheetdialog, null);
            TextView textView_bottomDialog = view.findViewById(R.id.tv_bottom_dialog_text);
            getActivity().runOnUiThread(new Runnable() {
                @SuppressLint("Range")
                @Override
                public void run() {
                    textView_bottomDialog.setText(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_WEATHER_INFO_LIFE_FLOW)));
                }
            });
            bottomSheetDialog.setContentView(view);
            bottomSheetDialog.show();
        }
        if (v.getId() == R.id.tv_trip) {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialog);
            View view = getLayoutInflater().inflate(R.layout.item_bottomsheetdialog, null);
            TextView textView_bottomDialog = view.findViewById(R.id.tv_bottom_dialog_text);
            getActivity().runOnUiThread(new Runnable() {
                @SuppressLint("Range")
                @Override
                public void run() {
                    textView_bottomDialog.setText(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_WEATHER_INFO_LIFE_TRIP)));
                }
            });
            bottomSheetDialog.setContentView(view);
            bottomSheetDialog.show();
        }
    }
}