package com.example.test;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class DataBase extends SQLiteOpenHelper {
    public static final String CREATE_NAME = "weather.db";
    public static int VERSION = 1;
    public static final String TABLE_NAME = "myWeather";
    static String i = "wahah";
    public static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + "_id INTEGER PRIMARY KEY AUTOINCREMENT," + i + " TEXT," + "column2 TEXT," + "column3 INTEGER," + "column4 REAL)";
    public Context context;

    public DataBase(Context context) {
        super(context, CREATE_NAME, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        Toast.makeText(context, "创建成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
