package com.example.weatherforecast.entity;


public class County {
    private int num;
    private String name;
    private int Province;
    private int codeCity;

    public int getProvince() {
        return Province;
    }

    public void setProvince(int codeProvince) {
        this.Province = codeProvince;
    }

    public int getCodeCity() {
        return codeCity;
    }

    public void setCodeCity(int codeCity) {
        this.codeCity = codeCity;
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
