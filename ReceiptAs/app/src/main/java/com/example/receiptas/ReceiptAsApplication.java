package com.example.receiptas;

import android.app.Application;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class ReceiptAsApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }
}
