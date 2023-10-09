package com.example.weatherforecast.addcity_activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.weatherforecast.R;
import com.example.weatherforecast.entity.Province;

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

public class AddProvinceActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView listView;
    private List listProvince;
    private List listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_province);
        toolbar = findViewById(R.id.tb_province);
        listView = findViewById(R.id.lv_province);
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
                Request request = new Request.Builder().url("https://www.mxnzp.com/api/address/v3/list/province?app_id=hv1asqfhrgmty2gc&app_secret=N4EBbFemeY4EgkCVm5RqyTY3xbLd58PZ").build();
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    String data = response.body().string();
//                    使用Gson的方式：自动转化为对象
//                    Gson gson = new Gson();
//                    List<Province> list = gson.fromJson(data, new TypeToken<List<Province>>() {
//                    }.getType());
//                    Log.d("wang", "有问题");
//                    for (Province p : list) {
//                        Log.d("wang", p.toString());
//                    }
                    listProvince = new ArrayList();
                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray jsonArrayPlus = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArrayPlus.length(); i++) {
                        JSONObject object = jsonArrayPlus.getJSONObject(i);
                        Province p = new Province();
                        p.setNum(object.getInt("code"));
                        p.setName(object.getString("name"));
                        listProvince.add(p);
                    }
                    listAdapter = new ArrayList();
                    Iterator it = listProvince.iterator();
                    while (it.hasNext()) {
                        Province p = (Province) it.next();
                        listAdapter.add(p.getName());
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
                        ArrayAdapter Adapter = new ArrayAdapter(AddProvinceActivity.this, R.layout.item_city, listAdapter);
                        listView.setAdapter(Adapter);
                        listView.setSelection(0);
                    }
                });
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //使用okhttp申请服务器数据，输入value城市code得到更详细的数据
                        TextView textView = view.findViewById(R.id.tv_area);
                        Iterator it = listProvince.iterator();
                        int codeProvince = 0;
                        while (it.hasNext()) {
                            Province province = (Province) it.next();
                            if (province.getName().equals(textView.getText().toString())) {
                                codeProvince = province.getNum();
                            }
                        }
                        Intent in = new Intent(AddProvinceActivity.this, AddCityActivity.class);
                        in.putExtra("codeProvince", codeProvince);
                        in.putExtra("nameProvince", textView.getText().toString());
                        startActivity(in);
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