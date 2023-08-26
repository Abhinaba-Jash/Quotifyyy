package com.example.qoute;

public class DataHolder {
    private static DataHolder instance;
    private String data;

    private DataHolder() {
        data="";
        //this.data=data;
        // Private constructor to prevent instantiation.
    }

    public static synchronized DataHolder getInstance() {
        if (instance == null) {
            instance = new DataHolder();
        }
        return instance;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}

