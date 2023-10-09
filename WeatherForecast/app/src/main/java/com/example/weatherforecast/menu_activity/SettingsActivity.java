package com.example.weatherforecast.menu_activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.weatherforecast.R;
import com.example.weatherforecast.addcity_activity.AddCountyActivity;
import com.example.weatherforecast.main_activity.MainActivity;

import java.io.FileNotFoundException;

public class SettingsActivity extends AppCompatActivity {
    private static final int MAIN_ACTIVITY_REQUEST_CODE = 1;
    private static final int FUZZY_SEARCH_ACTIVITY_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 请求权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        Toolbar toolbar = findViewById(R.id.toolbar_settings);
        Button btn_1 = findViewById(R.id.btn_settings_1);
        Button btn_2 = findViewById(R.id.btn_settings_2);
        Button btn_cancel = findViewById(R.id.btn_settings_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("config", Context.MODE_PRIVATE);
//第一个参数是文件的名字，第二个设置文件是公共或者是私有的，这里是app的数据，也就是私有的
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*"); // 选择照片的 MIME 类型
                startActivityForResult(intent, MAIN_ACTIVITY_REQUEST_CODE);
            }
        });
        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*"); // 选择照片的 MIME 类型
                startActivityForResult(intent, FUZZY_SEARCH_ACTIVITY_REQUEST_CODE);
            }
        });
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        bar.setTitle("设置");
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                Toast.makeText(this, "为了正常使用请允许", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MAIN_ACTIVITY_REQUEST_CODE) {
            if (data != null) {
                Uri selectedImageUri = data.getData();
                SharedPreferences preferences = getSharedPreferences("config", Context.MODE_PRIVATE);
//第一个参数是文件的名字，第二个设置文件是公共或者是私有的，这里是app的数据，也就是私有的
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("main_activity", selectedImageUri.toString());
                editor.commit();
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
        if (requestCode == FUZZY_SEARCH_ACTIVITY_REQUEST_CODE) {
            if (data != null) {
                Uri selectedImageUri = data.getData();
                SharedPreferences preferences = getSharedPreferences("config", Context.MODE_PRIVATE);
//第一个参数是文件的名字，第二个设置文件是公共或者是私有的，这里是app的数据，也就是私有的
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("fuzzy_activity_activity", selectedImageUri.toString());
                editor.commit();
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
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