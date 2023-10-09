package com.example.weatherforecast.database;

import android.annotation.SuppressLint;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.weatherforecast.database.bean.DataBaseVariableNames;

public class MyDBH extends SQLiteOpenHelper {
    private Context context;
    public static final String DataBase_NAME = "weatherInformation.db";   //数据库的名字
    public static final int DATABASE_VERSION = 1;   //数据库版本
    //一共三张表：城市信息表，天气信息表，预报信息表
    public final String WEATHER_INFO = "WEATHER_INFO";
    public final String WEATHER_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + WEATHER_INFO + " (" + DataBaseVariableNames._ID + " TEXT, "
            + DataBaseVariableNames.COLUMN_CITY_INFO_PROVINCE_NAME + " TEXT,"
            + DataBaseVariableNames.COLUMN_CITY_INFO_CITY_NAME + " TEXT, "
            + DataBaseVariableNames.COLUMN_CITY_INFO_COUNTY_NAME + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_REALTIME_TEXT + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_REALTIME_TEMP + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_1DAY_TIME + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_1DAY_ICON + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_1DAY_DESC + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_1DAY_TEMP_MAX + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_1DAY_TEMP_MIN + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_2DAY_TIME + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_2DAY_ICON + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_2DAY_DESC + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_2DAY_TEMP_MAX + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_2DAY_TEMP_MIN + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_3DAY_TIME + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_3DAY_ICON + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_3DAY_DESC + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_3DAY_TEMP_MAX + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_3DAY_TEMP_MIN + " TEXT, "
            + DataBaseVariableNames.COLUMN_WEATHER_INFO_AIR_NOW_PM2_5 + " TEXT, " + DataBaseVariableNames.COLUMN_WEATHER_INFO_AIR_NOW_LEVEL + " TEXT, " + DataBaseVariableNames.COLUMN_WEATHER_INFO_AIR_NOW_CATEGORY + " TEXT, " + DataBaseVariableNames.COLUMN_WEATHER_INFO_LIFE_TRIP + " TEXT, " + DataBaseVariableNames.COLUMN_WEATHER_INFO_LIFE_CLOTHES + " TEXT, " + DataBaseVariableNames.COLUMN_WEATHER_INFO_LIFE_CAR_WASH + " TEXT, " + DataBaseVariableNames.COLUMN_WEATHER_INFO_LIFE_COMFORT + " TEXT, " + DataBaseVariableNames.COLUMN_WEATHER_INFO_LIFE_FLOW + " TEXT, " + DataBaseVariableNames.COLUMN_WEATHER_INFO_LIFE_SPORT + " TEXT, "
            + DataBaseVariableNames.COLUMN_WEATHER_INFO_LIFE_SPI + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_1_TIME + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_1_ICON + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_1_TEMP + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_1_DESC + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_2_TIME + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_2_ICON + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_2_TEMP + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_2_DESC + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_3_TIME + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_3_ICON + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_3_TEMP + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_3_DESC + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_4_TIME + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_4_ICON + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_4_TEMP + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_4_DESC + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_5_TIME + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_5_ICON + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_5_TEMP + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_5_DESC + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_6_TIME + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_6_ICON + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_6_TEMP + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_6_DESC + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_7_TIME + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_7_ICON + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_7_TEMP + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_7_DESC + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_8_TIME + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_8_ICON + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_8_TEMP + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_8_DESC + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_9_TIME + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_9_ICON + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_9_TEMP + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_9_DESC + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_10_TIME + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_10_ICON + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_10_TEMP + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_10_DESC + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_11_TIME + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_11_ICON + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_11_TEMP + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_11_DESC + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_12_TIME + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_12_ICON + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_12_TEMP + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_12_DESC + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_13_TIME + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_13_ICON + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_13_TEMP + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_13_DESC + " TEXT, "

            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_14_TIME + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_14_ICON + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_14_TEMP + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_14_DESC + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_15_TIME + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_15_ICON + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_15_TEMP + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_15_DESC + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_16_TIME + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_16_ICON + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_16_TEMP + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_16_DESC + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_17_TIME + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_17_ICON + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_17_TEMP + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_17_DESC + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_18_TIME + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_18_ICON + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_18_TEMP + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_18_DESC + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_19_TIME + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_19_ICON + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_19_TEMP + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_19_DESC + " TEXT, "

            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_20_TIME + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_20_ICON + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_20_TEMP + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_20_DESC + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_21_TIME + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_21_ICON + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_21_TEMP + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_21_DESC + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_22_TIME + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_22_ICON + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_22_TEMP + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_22_DESC + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_23_TIME + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_23_ICON + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_23_TEMP + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_23_DESC + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_24_TIME + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_24_ICON + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_24_TEMP + " TEXT, "
            + DataBaseVariableNames.COLUMN_FORECAST_INFO_HOUR_24_DESC + " TEXT)";


    public final String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS Persons (" + "PersonID INTEGER, " + "LastName TEXT, " + "FirstName TEXT, " + "Address TEXT, " + "City TEXT);";
    public static MyDBH myDBH;
    public SQLiteDatabase database;

    public MyDBH(Context context) {
        super(context, DataBase_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //建表
        db.execSQL(WEATHER_CREATE_TABLE);
    }

    public void dbClose() {
        database.close();
    }

    public static MyDBH getMyDBH(Context context) {
        if (myDBH == null) {
            myDBH = new MyDBH(context);
        }
        return myDBH;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Long inert(String id, ContentValues contentValues) {
        database = myDBH.getWritableDatabase();
        Long i = database.insert(WEATHER_INFO, null, contentValues);
        return i;
    }

    public void del(String id) {
        database = myDBH.getWritableDatabase();
        String[] args = new String[]{id};
        database.delete(WEATHER_INFO, DataBaseVariableNames._ID + " = ?", args);
    }

    public Cursor query(String id) {
        database = myDBH.getWritableDatabase();
        String selection = DataBaseVariableNames._ID + " = ?";
        String[] args = new String[]{id};
        Cursor cursor = database.query(WEATHER_INFO, null, selection, args, null, null, null);
        return cursor;
    }

    public Cursor queryAll() {
        Log.d("checkOnWhether", "checkOnWhether");
        database = myDBH.getWritableDatabase();
        Cursor cursor = database.query(WEATHER_INFO, null, null, null, null, null, null, null);
        return cursor;
    }

    public void upData(String id, ContentValues contentValues) {
        database = myDBH.getWritableDatabase();
        String clue = DataBaseVariableNames._ID + " =? ";
        String[] args = new String[]{id};
        database.update(WEATHER_INFO, contentValues, clue, args);
    }
}
