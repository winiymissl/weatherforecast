package com.example.weatherforecast.addcity_activity;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.weatherforecast.main_activity.MainActivity;
import com.example.weatherforecast.R;
import com.example.weatherforecast.database.MyDBH;
import com.example.weatherforecast.database.bean.DataBaseVariableNames;

import java.util.ArrayList;
import java.util.List;

public class DelPlaceActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private Toolbar toolbar;
    private ArrayAdapter arrayAdapter;
    private List<String> list;
    private List<String> listNum;

    @SuppressLint({"RestrictedApi", "Range"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_del_place);
        //传输了一个bundle，这个是拥有城市的list
        listView = findViewById(R.id.lv_delPlace);
        toolbar = findViewById(R.id.tb_delPlace);
        //放的是android的toolbar
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            toolbar.setTitle("删除城市");
        }
        //assert bundle != null;    通常在调试阶段使用,断言语句
        MyDBH myDBH = MyDBH.getMyDBH(this);
        Cursor cursor = myDBH.queryAll();
        list = new ArrayList();
        listNum = new ArrayList();
        if (cursor.moveToFirst()) {
            do {
                StringBuilder s = new StringBuilder();
                s.append(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_CITY_INFO_PROVINCE_NAME)) + ",");
                s.append(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_CITY_INFO_CITY_NAME)) + ",");
                s.append(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames.COLUMN_CITY_INFO_COUNTY_NAME)));
                listNum.add(cursor.getString(cursor.getColumnIndex(DataBaseVariableNames._ID)));
                list.add(s.toString());
            } while (cursor.moveToNext());
        }
        arrayAdapter = new ArrayAdapter(this, R.layout.item_listplace, list);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
//            Intent intent = new Intent(DelPlaceActivity.this, MainActivity.class);
//            intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
            setResult(11);
            finish();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent intent = new Intent(DelPlaceActivity.this, MainActivity.class);
//        intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
        setResult(11);
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (list.size() > 1) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setPositiveButton("sure", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String i = listNum.get(position);
                    MyDBH myDBH = MyDBH.getMyDBH(DelPlaceActivity.this);
                    myDBH.del(i);
                    list.remove(position);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            arrayAdapter.notifyDataSetChanged();
                        }
                    });
                }
            });
            alertDialog.setNegativeButton("No!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alertDialog.setTitle("亲爱的用户：你好！").setMessage("是否删除: " + list.get(position).toString()).create();
            alertDialog.show();
        }
    }
}