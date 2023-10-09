package com.example.weatherforecast.entity;

public class City {
    private int num;
    private String name;
    private int Province;

    public int getProvince() {
        return Province;
    }

    public void setProvince(int province) {
        Province = province;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
