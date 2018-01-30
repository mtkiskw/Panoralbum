package com.example.nttr.panobumapp;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by nttr on 2018/01/30.
 */

public class AlbumApplication extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
        Realm.init(this);
    }
}
