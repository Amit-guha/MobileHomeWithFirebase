package com.example.mobilehome.netWordkInfo;

import android.app.Application;

public class MyApp extends Application {
    public static MyApp mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized MyApp getInstance() {
        return mInstance;
    }

    public void setConnectivityLisitiner(ConnectivityInfo.ConnectivityLisitiner lisitiner) {
        ConnectivityInfo.connectivityLisitiner = lisitiner;
    }
}
