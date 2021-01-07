package com.example.kiosk;

import android.os.StrictMode;
import android.util.Log;

import androidx.multidex.MultiDexApplication;
public class Myapp extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);


    }

}
