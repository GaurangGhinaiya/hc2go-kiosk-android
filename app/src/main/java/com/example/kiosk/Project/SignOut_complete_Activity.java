package com.example.kiosk.Project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kiosk.MainActivity;
import com.example.kiosk.R;
import com.example.kiosk.Splash_Activity;
import com.example.kiosk.Utill.Preferences;


public class SignOut_complete_Activity extends AppCompatActivity {


    Context ctx;
    Preferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_signout);
        ctx = SignOut_complete_Activity.this;
        preferences = new Preferences(ctx);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent i = new Intent(ctx, SignInOut_Activity.class);
                startActivity(i);

                finish();
                finishAffinity();

            }
        }, 5000);




    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent(ctx, SignInOut_Activity.class);
        startActivity(i);

        finish();
        finishAffinity();

    }
}
