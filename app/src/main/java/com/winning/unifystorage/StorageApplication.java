package com.winning.unifystorage;

import android.app.Application;

import com.winning.unifystorage_core.UStorage;

public class StorageApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        UStorage.initialize(this);
    }
}
