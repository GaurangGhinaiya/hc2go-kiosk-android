package com.example.kiosk.Project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kiosk.R;
import com.example.kiosk.Utill.Preferences;


public class Print_complete_Activity extends AppCompatActivity {


    Context ctx;
    Preferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_process);
        ctx = Print_complete_Activity.this;
        preferences = new Preferences(ctx);



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        finishAffinity();

        Intent i = new Intent(ctx, SignInOut_Activity.class);
        startActivity(i);

    }
}
