package com.example.weatherforecast.entity;

public class WeatherInformation {
    private int id;
    private String time;
    private String desc;
    private String MinTemp;
    private String MaxTemp;

    public WeatherInformation(int id, String time, String desc, String MaxTemp,String MinTemp) {
        this.id = id;
        this.time = time;
        this.desc = desc;
        this.MaxTemp = MaxTemp;
        this.MinTemp=MinTemp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


    public String getMinTemp() {
        return MinTemp;
    }

    public void setMinTemp(String minTemp) {
        MinTemp = minTemp;
    }

    public String getMaxTemp() {
        return MaxTemp;
    }

    public void setMaxTemp(String maxTemp) {
        MaxTemp = maxTemp;
    }
}
