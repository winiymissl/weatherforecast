package com.example.weatherforecast.main_activity;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.weatherforecast.R;
import com.example.weatherforecast.menu_activity.FuzzySearchActivity;
import com.example.weatherforecast.menu_activity.SettingsActivity;
import com.example.weatherforecast.addcity_activity.AddProvinceActivity;
import com.example.weatherforecast.addcity_activity.DelPlaceActivity;
import com.example.weatherforecast.adpater.MyPagerAdapter;
import com.example.weatherforecast.database.MyDBH;
import com.example.weatherforecast.database.bean.DataBaseVariableNames;
import com.example.weatherforecast.utils.IconSelector;
import com.example.weatherforecast.utils.TableOperation;
import com.google.android.material.tabs.TabLayout;
import com.qweather.sdk.bean.geo.GeoBean;
import com.qweather.sdk.view.HeConfig;
import com.qweather.sdk.view.QWeather;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout dl;
    private Toolbar toolbar;
    private List<String[]> city;

    private String id;
    private List allPlace;
    private ViewPager viewPager;
    private MyPagerAdapter myPagerAdapter;
    private FragmentManager fragmentManager;
    private List<Fragment> fragmentList;
    private ProgressBar progressBar;
    //    private MyLocationListener myLocationListener;
    private double latitude;
    private double longitude;
    private LocationClient locationClient;
    private Uri uri;
    private ImageView imageView;


    class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            latitude = location.getLatitude();
            //获取经度信息
            longitude = location.getLongitude();
            String province = location.getProvince();    //获取省份
            String cityName = location.getCity();    //获取城市
            String district = location.getDistrict();    //获取区县
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(decimalFormat.format(longitude));
            stringBuilder.append(",");
            stringBuilder.append(decimalFormat.format(latitude));
            Log.d("locationEn", stringBuilder.toString() + province + cityName + district);
            Thread thread = new Thread() {
                @Override
                public void run() {
                    super.run();
                    TableOperation.inTable(MainActivity.this, cityName, district);
                }
            };
            thread.start();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            QWeather.getGeoCityLookup(MainActivity.this, location.getAdCode(), new QWeather.OnResultGeoListener() {
                @Override
                public void onError(Throwable throwable) {

                }

                @SuppressLint("Range")
                @Override
                public void onSuccess(GeoBean geoBean) {
                    String id = geoBean.getLocationBean().get(0).getId();
                    MyDBH myDBH = MyDBH.getMyDBH(MainActivity.this);
                    Cursor query = myDBH.query(id);
                    query.moveToFirst();
                    runOnUiThread(new Runnable() {
                        @SuppressLint("Range")
                        @Override
                        public void run() {
                            try {
                                PagerFragment pagerFragment = new PagerFragment(query);
                                fragmentList.add(pagerFragment);
                                myPagerAdapter.notifyDataSetChanged();
                                locationClient.stop();
                            } catch (Exception e) {
                                Log.d("myPagerAdapternotifyDataSetChanged", e.toString());
                            }
                        }
                    });
                }
            });
        }
    }

    public LocationClient location_Client;

    class MyLocationListener_gps extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            latitude = location.getLatitude();
            //获取经度信息
            longitude = location.getLongitude();
            String province = location.getProvince();    //获取省份
            String cityName = location.getCity();    //获取城市
            String district = location.getDistrict();    //获取区县
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(decimalFormat.format(longitude));
            stringBuilder.append(",");
            stringBuilder.append(decimalFormat.format(latitude));
            Log.d("locationEn", stringBuilder.toString() + province + cityName + district);
            Thread thread = new Thread() {

                @Override
                public void run() {
                    super.run();
                    TableOperation.inTable(MainActivity.this, cityName, district);
                }
            };
            thread.start();
            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            QWeather.getGeoCityLookup(MainActivity.this, location.getAdCode(), new QWeather.OnResultGeoListener() {
                @Override
                public void onError(Throwable throwable) {
                    Log.d("`onErrorCheckIsEmpty`", throwable.toString());
                }

                @SuppressLint("Range")
                @Override
                public void onSuccess(GeoBean geoBean) {
                    String id = geoBean.getLocationBean().get(0).getId();
                    MyDBH myDBH = MyDBH.getMyDBH(MainActivity.this);
                    Cursor query = myDBH.query(id);
                    query.moveToFirst();
                    Iterator<Fragment> it = fragmentList.iterator();
                    boolean flag = false;
                    while (it.hasNext()) {
                        PagerFragment temp = (PagerFragment) it.next();
                        Cursor cursor = temp.getCursor();
                        if ((cursor.getString(cursor.getColumnIndex(DataBaseVariableNames._ID)).equals(id))) {
                            flag = true;
                        }
                    }

                    if (!flag) {
                        runOnUiThread(new Runnable() {
                            @SuppressLint("Range")
                            @Override
                            public void run() {
                                PagerFragment pagerFragment = new PagerFragment(query);
                                fragmentList.add(pagerFragment);
                                myPagerAdapter.notifyDataSetChanged();
                                int i = fragmentList.size();
                                viewPager.setCurrentItem(i - 1, true);
                                location_Client.stop();
                            }
                        });
                    } else {
                        location_Client.stop();
                    }
                }
            });
        }
    }

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HeConfig.init("HE2309190206361969", "4e5102dba60a43c7ae910bc0e195634b");

        imageView = findViewById(R.id.iv_main_activity);
        dl = findViewById(R.id.dl);
        try {
            SharedPreferences preferences = getSharedPreferences("config", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            String pic = preferences.getString("main_activity", "null");
            uri = Uri.parse(pic);
            getContentResolver().takePersistableUriPermission(uri, (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION));
            imageView.setImageURI(uri);
        } catch (Exception e) {
            Log.d("changeForWallPager", e.toString());
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        try {
            setSupportActionBar(toolbar);
            toolbar.setSubtitle("jackie");
            ActionBar bar = getSupportActionBar();
            if (bar != null) {
                bar.setDisplayHomeAsUpEnabled(true);
                bar.setTitle("wang");
                bar.setHomeAsUpIndicator(android.R.drawable.ic_menu_sort_by_size);
            }
            findViewById(R.id.gps);
            /*
             * 打开时使用SQLite数据库加载之前缓存的数据，加载在碎片里，用户向下拉刷新时，更新，（此时使用ViewPager2）
             * */
            fragmentList = new ArrayList<>();
            MyDBH myDBH = MyDBH.getMyDBH(this);
            Cursor cursor = myDBH.queryAll();
//            TabLayout tableLayout = findViewById(R.id.tl_main_activity);
//            tableLayout.setupWithViewPager(viewPager);
            if (cursor.moveToFirst()) {
                do {
                    String id = cursor.getString(cursor.getColumnIndex(DataBaseVariableNames._ID));
                    Cursor query = myDBH.query(id);
                    PagerFragment pagerFragment = new PagerFragment(query);
                    fragmentList.add(pagerFragment);
                } while (cursor.moveToNext());
            } else {
                //申请网络权限
                //检查是否允许
                if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
                } else {
                    Toast.makeText(this, "定位中...", Toast.LENGTH_SHORT).show();
                }
            }
            fragmentManager = getSupportFragmentManager();
            myPagerAdapter = new MyPagerAdapter(fragmentManager, fragmentList, this);
            viewPager = findViewById(R.id.vp);
            viewPager.setAdapter(myPagerAdapter);
            viewPager.setOffscreenPageLimit(10);
        } catch (Exception e) {
            Log.d("CheckIsSuccess", "CheckIsSuccess" + e);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                LocationClientOption option = new LocationClientOption();
                option.setIsNeedAddress(true);
                option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);
                option.setCoorType("bd09ll");
                option.setScanSpan(0);
                locationClient = null;
                try {
                    locationClient.setAgreePrivacy(true);
                    locationClient = new LocationClient(getApplicationContext());
                    locationClient.registerLocationListener(new MyLocationListener());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                locationClient.setLocOption(option);
                locationClient.start();
            } else {
                Toast.makeText(this, "把定位给我打开！！", Toast.LENGTH_SHORT).show();
                //清空活动栈
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);

                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        }
        if (requestCode == 111) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    SharedPreferences preferences = getSharedPreferences("config", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    String pic = preferences.getString("main_activity", "null");
                    uri = Uri.parse(pic);
                    imageView.setImageURI(uri);
                } catch (Exception e) {
                    Log.d("cjalanshihouasdfasdf", e.toString());
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.gps) {
            LocationClientOption option = new LocationClientOption();
            option.setIsNeedAddress(true);
            option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);
            option.setCoorType("bd09ll");
            option.setScanSpan(0);
            location_Client = null;
            try {
                location_Client.setAgreePrivacy(true);
                location_Client = new LocationClient(getApplicationContext());
                location_Client.registerLocationListener(new MyLocationListener_gps());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            location_Client.setLocOption(option);
            location_Client.start();
        }
        //选择了bar的按钮
        if (item.getItemId() == R.id.addCity) {
            //用户通过输入城市名字，得到城市
            Intent in = new Intent(this, AddProvinceActivity.class);
            startActivity(in);
        }
        if (item.getItemId() == R.id.delCity) {
            Intent in = new Intent(this, DelPlaceActivity.class);
            startActivityForResult(in, 11);
        }
        if (item.getItemId() == R.id.fuzzy_search) {
            Intent in = new Intent(this, FuzzySearchActivity.class);
            startActivity(in);
        }
        if (item.getItemId() == android.R.id.home) {
            dl.openDrawer(GravityCompat.START);
        }
        if (item.getItemId() == R.id.settings) {
            Intent in = new Intent(this, SettingsActivity.class);
            startActivity(in);
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11) {
//      重新
            fragmentList.clear();
            MyDBH myDBH = MyDBH.getMyDBH(this);
            Cursor cursor = myDBH.queryAll();
            if (cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex(DataBaseVariableNames._ID));
                    Cursor query = myDBH.query(id);
                    PagerFragment pagerFragment = new PagerFragment(query);
                    fragmentList.add(pagerFragment);
                } while (cursor.moveToNext());
                myPagerAdapter = new MyPagerAdapter(fragmentManager, fragmentList, this);
                viewPager.setAdapter(myPagerAdapter);
                viewPager.setOffscreenPageLimit(10);
            }
        }
    }
}