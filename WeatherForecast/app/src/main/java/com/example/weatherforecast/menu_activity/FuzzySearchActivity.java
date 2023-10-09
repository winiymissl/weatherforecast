package com.example.weatherforecast.menu_activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.weatherforecast.main_activity.MainActivity;
import com.example.weatherforecast.R;
import com.example.weatherforecast.utils.TableOperation;
import com.example.weatherforecast.adpater.ListViewFuzzySearchAdapter;
import com.qweather.sdk.bean.base.Code;
import com.qweather.sdk.bean.geo.GeoBean;
import com.qweather.sdk.view.QWeather;

import java.util.List;

public class FuzzySearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuzzy_search);
        ImageView imageView = findViewById(R.id.iv_fuzzy_activity);

        Toolbar toolbar = findViewById(R.id.toolbar_fuzzy_search);
        EditText editText = findViewById(R.id.et_fuzzy_search);
        ListView listview = findViewById(R.id.lv_fuzzy_search);
        try {
            SharedPreferences preferences = getSharedPreferences("config", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            String pic = preferences.getString("fuzzy_activity_activity", "null");
            Uri uri = Uri.parse(pic);
            Log.d("dainchulaichengshiminghzi", uri.toString());
            getContentResolver().takePersistableUriPermission(uri, (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION));
            imageView.setImageURI(uri);
        } catch (Exception e) {
            Log.d("dainchulaichengshiminghzi", e.toString());
        } finally {
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView textView = view.findViewById(R.id.tv_fuzzy_listview_1);
                    String i = textView.getText().toString();
                    String[] place = i.split("  ");
                    TableOperation.inTable(FuzzySearchActivity.this, place[1], place[2]);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    Intent intent = new Intent(FuzzySearchActivity.this, MainActivity.class);
                    intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });

            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    listview.setVisibility(View.VISIBLE);
                    if (!String.valueOf(s).equals("")) {
                        QWeather.getGeoCityLookup(FuzzySearchActivity.this, String.valueOf(s), new QWeather.OnResultGeoListener() {
                            @Override
                            public void onError(Throwable throwable) {
                                Log.d("chaxunCheck", throwable.toString());
                            }

                            @Override
                            public void onSuccess(GeoBean geoBean) {
                                if (geoBean.getCode() == Code.OK) {
                                    List<GeoBean.LocationBean> locationBean = geoBean.getLocationBean();
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ListViewFuzzySearchAdapter listViewFuzzySearchAdapter = new ListViewFuzzySearchAdapter(FuzzySearchActivity.this, locationBean);
                                            listview.setAdapter(listViewFuzzySearchAdapter);
                                        }
                                    });
                                }
                            }
                        });
                    } else {
                        listview.setVisibility(View.GONE);

                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            toolbar.setTitle("搜索城市");
            setSupportActionBar(toolbar);
            ActionBar bar = getSupportActionBar();
            //如果得到的不为空
            if (bar != null) {
                bar.setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}