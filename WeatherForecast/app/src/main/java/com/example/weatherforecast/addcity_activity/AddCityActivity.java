package com.example.weatherforecast.addcity_activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.weatherforecast.R;
import com.example.weatherforecast.entity.City;

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

public class AddCityActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ListView listView;
    private List listCity;
    private List listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);
        Intent in = getIntent();
        int codeProvince = in.getIntExtra("codeProvince", 0);
        String nameProvince = in.getStringExtra("nameProvince");
        toolbar = findViewById(R.id.tb_city);
        listView = findViewById(R.id.lv_city);
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
                Request request = new Request.Builder().url("https://www.mxnzp.com/api/address/v3/list/city?provinceCode=" + codeProvince + "&app_id=hv1asqfhrgmty2gc&app_secret=N4EBbFemeY4EgkCVm5RqyTY3xbLd58PZ").build();
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    String data = response.body().string();
                    Log.d("wang", data);
                    listCity = new ArrayList();
                    listAdapter = new ArrayList();
                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray jsonArrayPlus = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArrayPlus.length(); i++) {
                        JSONObject object = jsonArrayPlus.getJSONObject(i);
                        City p = new City();
                        p.setProvince(codeProvince);
                        p.setNum(object.getInt("code"));
                        p.setName(object.getString("name"));
                        listCity.add(p);
                    }
                    Iterator its = listCity.iterator();
                    while (its.hasNext()) {
                        City p = (City) its.next();
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
                        ArrayAdapter Adapter = new ArrayAdapter(AddCityActivity.this, R.layout.item_city, listAdapter);
                        listView.setAdapter(Adapter);
                        listView.setSelection(0);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent in = new Intent(AddCityActivity.this, AddCountyActivity.class);
                                TextView textView = view.findViewById(R.id.tv_area);
                                Iterator it = listCity.iterator();
                                int codeCity = 0;
                                while (it.hasNext()) {
                                    City p = (City) it.next();
                                    if (p.getName().equals(textView.getText().toString())) {
                                        codeCity = p.getNum();
                                    }
                                }
                                in.putExtra("codeProvince", codeProvince);
                                in.putExtra("nameProvince", nameProvince);
                                in.putExtra("codeCity", codeCity);
                                in.putExtra("nameCity", textView.getText().toString());
                                startActivity(in);
                            }
                        });
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