package com.example.kiosk.Project.FG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.kiosk.Project.HPSP.HPSP_FirstTime_Activity;
import com.example.kiosk.Project.HPSP.HPSP_Using_PhoneNumber_Activity;
import com.example.kiosk.Project.SimpleScannerActivity;
import com.example.kiosk.R;
import com.example.kiosk.Utill.Preferences;


public class FG_Type_Activity extends AppCompatActivity {


    Context ctx;
    Preferences preferences;

    int sign_type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fg_type);
        ctx = FG_Type_Activity.this;
        preferences = new Preferences(ctx);

        LinearLayout ll_back = findViewById(R.id.ll_back);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        LinearLayout ll_Next = findViewById(R.id.ll_Next);
        ll_Next.setVisibility(View.GONE);
        ll_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sign_type == 0) {
                    Toast.makeText(ctx, "Please Select One Type", Toast.LENGTH_SHORT).show();
                    return;
                } else if (sign_type == 1) {
                    Intent i = new Intent(ctx, FG_FirstTime_Activity.class);
                    startActivity(i);
                } else if (sign_type == 2) {
                    Intent i = new Intent(ctx, FG_Using_PhoneNumber_Activity.class);
                    startActivity(i);
                } else if (sign_type == 3) {
                    Intent i = new Intent(ctx, SimpleScannerActivity.class);
                    startActivity(i);
                }
            }
        });

        CardView card_first_time = findViewById(R.id.card_first_time);
        card_first_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sign_type = 1;
                Intent i = new Intent(ctx, FG_FirstTime_Activity.class);
                startActivity(i);
            }
        });

        CardView card_phone_number = findViewById(R.id.card_phone_number);
        card_phone_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sign_type = 2;
                Intent i = new Intent(ctx, FG_Using_PhoneNumber_Activity.class);
                startActivity(i);
            }
        });

        CardView card_pass = findViewById(R.id.card_pass);
        card_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sign_type = 3;
                Intent i = new Intent(ctx, SimpleScannerActivity.class);
                startActivity(i);
            }
        });


    }

    @Override
    protected void onResume() {
        preferences.setHomecarePassQList("");
        super.onResume();
    }
}
