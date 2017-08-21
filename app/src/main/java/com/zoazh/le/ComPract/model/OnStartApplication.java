package com.zoazh.le.ComPract.model;

import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Zatic Prasertarcha on 6/3/2560.
 */

public class OnStartApplication extends android.app.Application  {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
