package com.shurik.memwor_24.pumpwimo.models;

public class Parametr {

    private String parametrName; // название параметра
    private int parametrImage;

    public Parametr(String parametrName, int parametrImage) {
        this.parametrName = parametrName;
        this.parametrImage = parametrImage;
    }

    public String getParametrName() {
        return parametrName;
    }

    public int getParametrImage() {
        return parametrImage;
    }
}
