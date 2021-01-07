package com.example.kiosk;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.kiosk.Project.SignInOut_Activity;
import com.example.kiosk.Utill.Preferences;


public class Splash_Activity extends AppCompatActivity {


    Context ctx;
     Preferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ctx = Splash_Activity.this;
         preferences = new Preferences(ctx);

        Intent intent = new Intent(Splash_Activity.this, MyService.class);
        startService(intent);

        marshmallowRuntimePermission();



    }


    public void marshmallowRuntimePermission() {
        ActivityCompat.requestPermissions(Splash_Activity.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                100);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED ) {
                    method_next_screen();
                } else {
                   finish();
                }
                return;
            }
        }
    }

    private void method_next_screen() {



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (preferences.getWIFI_USB_PRINTER_id().equalsIgnoreCase("")) {
                    if (preferences.getPRE_DeviceMacAddress().equalsIgnoreCase("")) {
                        Intent i = new Intent(Splash_Activity.this, MainActivity.class);
                        startActivity(i);
                    } else {
                        Intent i = new Intent(Splash_Activity.this, MainActivity.class);
                        startActivity(i);
                    }
                } else {
                    Intent i = new Intent(Splash_Activity.this, SignInOut_Activity.class);
                    startActivity(i);
                }
                finish();

            }
        }, 2000);


    }


}
