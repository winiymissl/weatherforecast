package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.app.WallpaperManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn = findViewById(R.id.btn);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn) {
            DataBase dataBase = new DataBase(this);
            dataBase.getWritableDatabase();
            Toast.makeText(this, "连接成功", Toast.LENGTH_SHORT).show();
        }
    }
}