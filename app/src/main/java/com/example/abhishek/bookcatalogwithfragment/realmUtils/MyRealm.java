package com.example.abhishek.bookcatalogwithfragment.realmUtils;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by abhishek on 30/3/18.
 */

public class MyRealm extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // The default Realm file is "default.realm" in Context.getFilesDir();
        // we'll change it to "myrealm.realm"
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().name("catalog.realm").build();
        Realm.setDefaultConfiguration(config);
    }
}
