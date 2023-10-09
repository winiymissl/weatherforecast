package com.example.weatherforecast.addcity_activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.weatherforecast.main_activity.MainActivity;
import com.example.weatherforecast.R;
import com.example.weatherforecast.database.MyDBH;
import com.example.weatherforecast.database.bean.DataBaseVariableNames;
import com.example.weatherforecast.entity.County;
import com.qweather.sdk.bean.air.AirNowBean;
import com.qweather.sdk.bean.base.Code;
import com.qweather.sdk.bean.base.IndicesType;
import com.qweather.sdk.bean.base.Lang;
import com.qweather.sdk.bean.base.Range;
import com.qweather.sdk.bean.geo.GeoBean;
import com.qweather.sdk.bean.indices.IndicesBean;
import com.qweather.sdk.bean.weather.WeatherDailyBean;
import com.qweather.sdk.bean.weather.WeatherHourlyBean;
import com.qweather.sdk.bean.weather.WeatherNowBean;
import com.qweather.sdk.view.QWeather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AddCountyActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ListView listView;

    private List listCounty;
    private List listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_county);
        Intent in = getIntent();
        int codeProvince = in.getIntExtra("codeProvince", 0);
        String nameProvince = in.getStringExtra("nameProvince");
        int codeCity = in.getIntExtra("codeCity", 0);
        Log.d("thingCode", codeCity + ":" + codeProvince);
        String nameCity = in.getStringExtra("nameCity");
        toolbar = findViewById(R.id.tb_county);
        listView = findViewById(R.id.lv_county);
        //toolbar的初始化
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setTitle("选择城市");
        }
        //向服务器申请数据，返回一个json格式的数据
        new Thread() {
            @Override
            public void run() {
                super.run();
                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder().url("https://www.mxnzp.com/api/address/v3/list/area?cityCode=" + codeCity + "&provinceCode=" + codeProvince + "&app_id=hv1asqfhrgmty2gc&app_secret=N4EBbFemeY4EgkCVm5RqyTY3xbLd58PZ").build();
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    String data = response.body().string();
                    Log.d("wang", data);
                    listCounty = new ArrayList();
                    listAdapter = new ArrayList();
                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray jsonArrayPlus = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArrayPlus.length(); i++) {
                        JSONObject object = jsonArrayPlus.getJSONObject(i);
                        County p = new County();
                        p.setCodeCity(codeCity);
                        p.setProvince(codeProvince);
                        p.setNum(object.getInt("code"));
                        p.setName(object.getString("name"));
                        listCounty.add(p);
                    }
                    Iterator its = listCounty.iterator();
                    while (its.hasNext()) {
                        County p = (County) its.next();
                        listAdapter.add(p.getName());
                        Log.d("wang", p.getName());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 在此处更新适配器或其他 UI 组件
                        ArrayAdapter Adapter = new ArrayAdapter(AddCountyActivity.this, R.layout.item_city, listAdapter);
                        listView.setAdapter(Adapter);
                    }
                });
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        TextView textView = view.findViewById(R.id.tv_area);
//                        Iterator it = listCounty.iterator();
//                        int codeCounty = 0;
//                        while (it.hasNext()) {
//                            County p = (County) it.next();
//                            if (p.getName().equals(textView.getText().toString())) {
//                                codeCounty = p.getNum();
//                            }
//                        }
//                       String a = nameProvince;
                        String b = nameCity;
                        if (b.equals("市辖区") || b.equals("县")) {
                            b = "";
                        }
                        String c = textView.getText().toString();
                        String finalB = b;
                        Thread thread = new Thread() {
                            @Override
                            public void run() {
                                super.run();
                                QWeather.getGeoCityLookup(AddCountyActivity.this, c, finalB, Range.CN, 1, Lang.ZH_HANS, new QWeather.OnResultGeoListener() {
                                    @Override
                                    public void onError(Throwable throwable) {

                                    }

                                    @Override
                                    public void onSuccess(GeoBean geoBean) {
                                        Log.d("getGeoCityLookUp", geoBean.getLocationBean().get(0).toString());
                                        if (geoBean.getCode() == Code.OK) {
                                            GeoBean.LocationBean bean = geoBean.getLocationBean().get(0);
                                            String id = bean.getId();
                                            MyDBH myDBH1 = MyDBH.getMyDBH(AddCountyActivity.this);
                                            ContentValues contentValues1 = new ContentValues();
                                            contentValues1.put(DataBaseVariableNames._ID, id);
                                            contentValues1.put(DataBaseVariableNames.COLUMN_CITY_INFO_PROVINCE_NAME, bean.getAdm1());
                                            contentValues1.put(DataBaseVariableNames.COLUMN_CITY_INFO_CITY_NAME, bean.getAdm2());
                                            contentValues1.put(DataBaseVariableNames.COLUMN_CITY_INFO_COUNTY_NAME, bean.getName());
                                            //先进行一次遍历,删除之前的数据。
                                            myDBH1.del(id);
                                            try {
                                                myDBH1.inert(id, contentValues1);
                                            } catch (Exception e) {
                                                Log.d("dataBaseInertMethod", e.toString());
                                            }

                                            QWeather.getWeather3D(AddCountyActivity.this, id, new QWeather.OnResultWeatherDailyListener() {
                                                @Override
                                                public void onError(Throwable throwable) {
                                                    Log.d("getWeather3DonError", throwable.toString());
                                                }

                                                @Override
                                                public void onSuccess(WeatherDailyBean weatherDailyBean) {
                                                    Log.d("getWeather3D", weatherDailyBean.getDaily().get(0).getFxDate() + ":   " + weatherDailyBean.getDaily().size());
                                                    if (weatherDailyBean.getCode() == Code.OK) {
                                                        WeatherDailyBean.DailyBean dailyBean1 = weatherDailyBean.getDaily().get(0);
                                                        WeatherDailyBean.DailyBean dailyBean2 = weatherDailyBean.getDaily().get(1);
                                                        WeatherDailyBean.DailyBean dailyBean3 = weatherDailyBean.getDaily().get(2);
                                                        ContentValues contentValues = new ContentValues();
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_1DAY_TIME, dailyBean1.getFxDate());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_1DAY_ICON, dailyBean1.getIconDay());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_1DAY_DESC, dailyBean1.getTextDay());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_1DAY_TEMP_MAX, dailyBean1.getTempMax());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_1DAY_TEMP_MIN, dailyBean1.getTempMin());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_2DAY_TIME, dailyBean2.getFxDate());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_2DAY_ICON, dailyBean2.getIconDay());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_2DAY_DESC, dailyBean2.getTextDay());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_2DAY_TEMP_MAX, dailyBean2.getTempMax());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_2DAY_TEMP_MIN, dailyBean2.getTempMin());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_3DAY_TIME, dailyBean3.getFxDate());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_3DAY_ICON, dailyBean3.getIconDay());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_3DAY_DESC, dailyBean3.getTextDay());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_3DAY_TEMP_MAX, dailyBean3.getTempMax());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_3DAY_TEMP_MIN, dailyBean3.getTempMin());
                                                        Log.d("WeatherDailyBean", "可以运行");
                                                        myDBH1.upData(id, contentValues);
                                                    }
                                                }
                                            });
                                            QWeather.getWeather24Hourly(AddCountyActivity.this, id, new QWeather.OnResultWeatherHourlyListener() {
                                                @Override
                                                public void onError(Throwable throwable) {

                                                }

                                                @Override
                                                public void onSuccess(WeatherHourlyBean weatherHourlyBean) {
                                                    if (weatherHourlyBean.getCode() == Code.OK) {
                                                        List<WeatherHourlyBean.HourlyBean> hourly = weatherHourlyBean.getHourly();
                                                        ContentValues contentValues = new ContentValues();
                                                        WeatherHourlyBean.HourlyBean hourlyBean1 = hourly.get(0);
                                                        WeatherHourlyBean.HourlyBean hourlyBean2 = hourly.get(1);
                                                        WeatherHourlyBean.HourlyBean hourlyBean3 = hourly.get(2);
                                                        WeatherHourlyBean.HourlyBean hourlyBean4 = hourly.get(3);
                                                        WeatherHourlyBean.HourlyBean hourlyBean5 = hourly.get(4);
                                                        WeatherHourlyBean.HourlyBean hourlyBean6 = hourly.get(5);
                                                        WeatherHourlyBean.HourlyBean hourlyBean7 = hourly.get(6);
                                                        WeatherHourlyBean.HourlyBean hourlyBean8 = hourly.get(7);
                                                        WeatherHourlyBean.HourlyBean hourlyBean9 = hourly.get(8);
                                                        WeatherHourlyBean.HourlyBean hourlyBean10 = hourly.get(9);
                                                        WeatherHourlyBean.HourlyBean hourlyBean11 = hourly.get(10);
                                                        WeatherHourlyBean.HourlyBean hourlyBean12 = hourly.get(11);
                                                        WeatherHourlyBean.HourlyBean hourlyBean13 = hourly.get(12);
                                                        WeatherHourlyBean.HourlyBean hourlyBean14 = hourly.get(13);
                                                        WeatherHourlyBean.HourlyBean hourlyBean15 = hourly.get(14);
                                                        WeatherHourlyBean.HourlyBean hourlyBean16 = hourly.get(15);
                                                        WeatherHourlyBean.HourlyBean hourlyBean17 = hourly.get(16);
                                                        WeatherHourlyBean.HourlyBean hourlyBean18 = hourly.get(17);
                                                        WeatherHourlyBean.HourlyBean hourlyBean19 = hourly.get(18);
                                                        WeatherHourlyBean.HourlyBean hourlyBean20 = hourly.get(19);
                                                        WeatherHourlyBean.HourlyBean hourlyBean21 = hourly.get(20);
                                                        WeatherHourlyBean.HourlyBean hourlyBean22 = hourly.get(21);
                                                        WeatherHourlyBean.HourlyBean hourlyBean23 = hourly.get(22);
                                                        WeatherHourlyBean.HourlyBean hourlyBean24 = hourly.get(23);
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_1_TIME, hourlyBean1.getFxTime());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_1_DESC, hourlyBean1.getText());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_1_ICON, hourlyBean1.getIcon());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_1_TEMP, hourlyBean1.getTemp());

                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_2_TIME, hourlyBean2.getFxTime());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_2_DESC, hourlyBean2.getText());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_2_ICON, hourlyBean2.getIcon());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_2_TEMP, hourlyBean2.getTemp());

                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_3_TIME, hourlyBean3.getFxTime());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_3_ICON, hourlyBean3.getIcon());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_3_DESC, hourlyBean3.getText());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_3_TEMP, hourlyBean3.getTemp());

                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_4_TIME, hourlyBean4.getFxTime());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_4_ICON, hourlyBean4.getIcon());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_4_DESC, hourlyBean4.getText());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_4_TEMP, hourlyBean4.getTemp());

                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_5_TIME, hourlyBean5.getFxTime());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_5_ICON, hourlyBean5.getIcon());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_5_DESC, hourlyBean5.getText());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_5_TEMP, hourlyBean5.getTemp());

                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_6_TIME, hourlyBean6.getFxTime());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_6_ICON, hourlyBean6.getIcon());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_6_DESC, hourlyBean6.getText());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_6_TEMP, hourlyBean6.getTemp());

                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_7_TIME, hourlyBean7.getFxTime());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_7_ICON, hourlyBean7.getIcon());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_7_DESC, hourlyBean7.getText());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_7_TEMP, hourlyBean7.getTemp());


                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_8_TIME, hourlyBean8.getFxTime());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_8_ICON, hourlyBean8.getIcon());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_8_DESC, hourlyBean8.getText());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_8_TEMP, hourlyBean8.getTemp());

                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_9_TIME, hourlyBean9.getFxTime());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_9_ICON, hourlyBean9.getIcon());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_9_DESC, hourlyBean9.getText());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_9_TEMP, hourlyBean9.getTemp());

                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_10_TIME, hourlyBean10.getFxTime());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_10_ICON, hourlyBean10.getIcon());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_10_DESC, hourlyBean10.getText());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_10_TEMP, hourlyBean10.getTemp());

                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_11_TIME, hourlyBean11.getFxTime());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_11_ICON, hourlyBean11.getIcon());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_11_DESC, hourlyBean11.getText());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_11_TEMP, hourlyBean11.getTemp());

                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_12_TIME, hourlyBean12.getFxTime());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_12_ICON, hourlyBean12.getIcon());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_12_DESC, hourlyBean12.getText());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_12_TEMP, hourlyBean12.getTemp());

                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_13_TIME, hourlyBean13.getFxTime());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_13_ICON, hourlyBean13.getIcon());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_13_DESC, hourlyBean13.getText());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_13_TEMP, hourlyBean13.getTemp());

                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_14_TIME, hourlyBean14.getFxTime());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_14_ICON, hourlyBean14.getIcon());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_14_DESC, hourlyBean14.getText());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_14_TEMP, hourlyBean14.getTemp());

                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_15_TIME, hourlyBean15.getFxTime());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_15_ICON, hourlyBean15.getIcon());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_15_DESC, hourlyBean15.getText());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_15_TEMP, hourlyBean15.getTemp());

                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_16_TIME, hourlyBean16.getFxTime());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_16_ICON, hourlyBean16.getIcon());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_16_DESC, hourlyBean16.getText());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_16_TEMP, hourlyBean16.getTemp());

                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_17_TIME, hourlyBean17.getFxTime());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_17_ICON, hourlyBean17.getIcon());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_17_DESC, hourlyBean17.getText());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_17_TEMP, hourlyBean17.getTemp());

                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_18_TIME, hourlyBean18.getFxTime());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_18_ICON, hourlyBean18.getIcon());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_18_DESC, hourlyBean18.getText());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_18_TEMP, hourlyBean18.getTemp());

                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_19_TIME, hourlyBean19.getFxTime());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_19_ICON, hourlyBean19.getIcon());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_19_DESC, hourlyBean19.getText());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_19_TEMP, hourlyBean19.getTemp());

                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_20_TIME, hourlyBean20.getFxTime());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_20_ICON, hourlyBean20.getIcon());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_20_DESC, hourlyBean20.getText());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_20_TEMP, hourlyBean20.getTemp());

                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_21_TIME, hourlyBean21.getFxTime());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_21_ICON, hourlyBean21.getIcon());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_21_DESC, hourlyBean21.getText());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_21_TEMP, hourlyBean21.getTemp());

                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_22_TIME, hourlyBean22.getFxTime());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_22_ICON, hourlyBean22.getIcon());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_22_DESC, hourlyBean22.getText());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_22_TEMP, hourlyBean22.getTemp());

                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_23_TIME, hourlyBean23.getFxTime());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_23_ICON, hourlyBean23.getIcon());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_23_DESC, hourlyBean23.getText());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_23_TEMP, hourlyBean23.getTemp());

                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_24_TIME, hourlyBean24.getFxTime());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_24_ICON, hourlyBean24.getIcon());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_24_DESC, hourlyBean24.getText());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_24_TEMP, hourlyBean24.getTemp());
                                                        myDBH1.upData(id, contentValues);
                                                    }
                                                }
                                            });
                                            QWeather.getAirNow(AddCountyActivity.this, id, Lang.ZH_HANS, new QWeather.OnResultAirNowListener() {
                                                @Override
                                                public void onError(Throwable throwable) {

                                                }

                                                @Override
                                                public void onSuccess(AirNowBean airNowBean) {
                                                    if (airNowBean.getCode() == Code.OK) {
                                                        AirNowBean.NowBean now = airNowBean.getNow();
                                                        ContentValues contentValues = new ContentValues();
                                                        contentValues.put(DataBaseVariableNames.COLUMN_WEATHER_INFO_AIR_NOW_PM2_5, now.getPm2p5());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_WEATHER_INFO_AIR_NOW_LEVEL, now.getLevel());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_WEATHER_INFO_AIR_NOW_CATEGORY, now.getCategory());
                                                        myDBH1.upData(id, contentValues);
                                                    }
                                                }
                                            });
                                            QWeather.getWeatherNow(AddCountyActivity.this, id, new QWeather.OnResultWeatherNowListener() {
                                                @Override
                                                public void onError(Throwable throwable) {

                                                }

                                                @Override
                                                public void onSuccess(WeatherNowBean weatherNowBean) {
                                                    if (weatherNowBean.getCode() == Code.OK) {
                                                        ContentValues contentValues = new ContentValues();
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_REALTIME_TEMP, weatherNowBean.getNow().getTemp());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_FORECAST_INFO_REALTIME_TEXT, weatherNowBean.getNow().getText());
                                                        myDBH1.upData(id, contentValues);
                                                    }
                                                }
                                            });
                                            List<IndicesType> indices = new ArrayList<>();
                                            indices.add(IndicesType.ALL);
                                            QWeather.getIndices1D(AddCountyActivity.this, id, Lang.ZH_HANS, indices, new QWeather.OnResultIndicesListener() {
                                                @Override
                                                public void onError(Throwable throwable) {

                                                }

                                                @Override
                                                public void onSuccess(IndicesBean indicesBean) {
                                                    if (indicesBean.getCode() == Code.OK) {
                                                        List<IndicesBean.DailyBean> list = indicesBean.getDailyList();
                                                        IndicesBean.DailyBean dailyBean = list.get(0);
                                                        IndicesBean.DailyBean dailyBean1 = list.get(1);
                                                        IndicesBean.DailyBean dailyBean2 = list.get(2);
                                                        IndicesBean.DailyBean dailyBean7 = list.get(7);
                                                        IndicesBean.DailyBean dailyBean15 = list.get(15);
                                                        IndicesBean.DailyBean dailyBean8 = list.get(8);
                                                        IndicesBean.DailyBean dailyBean5 = list.get(5);
                                                        Log.d("IndicesBeanInformation", dailyBean1.getName() + ":" + dailyBean1.getType());
                                                        ContentValues contentValues = new ContentValues();
                                                        contentValues.put(DataBaseVariableNames.COLUMN_WEATHER_INFO_LIFE_SPORT, dailyBean.getText());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_WEATHER_INFO_LIFE_CAR_WASH, dailyBean1.getText());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_WEATHER_INFO_LIFE_CLOTHES, dailyBean2.getText());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_WEATHER_INFO_LIFE_COMFORT, dailyBean7.getText());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_WEATHER_INFO_LIFE_SPI, dailyBean15.getText());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_WEATHER_INFO_LIFE_FLOW, dailyBean8.getText());
                                                        contentValues.put(DataBaseVariableNames.COLUMN_WEATHER_INFO_LIFE_TRIP, dailyBean5.getText());
                                                        myDBH1.upData(id, contentValues);
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        };
                        thread.start();
                        try {
                            thread.sleep(600);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        try {
                            Intent intent = new Intent(AddCountyActivity.this, MainActivity.class);
                            intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            Log.d("startActivityException", "startActivityException");
                        } catch (Exception e) {

                        }
                    }
                });
            }
        }.start();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}