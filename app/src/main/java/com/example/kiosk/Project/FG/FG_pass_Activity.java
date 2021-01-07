package com.example.kiosk.Project.FG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.kiosk.Project.HPSP.HPSP_EpartmentDetails_Activity;

import com.example.kiosk.R;
import com.example.kiosk.Utill.Preferences;


public class FG_pass_Activity extends AppCompatActivity {


    Context ctx;
    Preferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fg_pass);
        ctx = FG_pass_Activity.this;
        preferences = new Preferences(ctx);

        LinearLayout ll_back = findViewById(R.id.ll_back);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        LinearLayout ll_Next = findViewById(R.id.ll_Next);
        ll_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ctx, HPSP_EpartmentDetails_Activity.class);
                startActivity(i);
            }
        });

    }
}
