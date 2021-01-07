package com.example.kiosk.Project.HPSP;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.kiosk.Project.SignInOut_Activity;

import com.example.kiosk.R;
import com.example.kiosk.Utill.Preferences;



public class HPSP_VehicelsDetails_Activity extends AppCompatActivity {


    Context ctx;
    Preferences preferences;

    EditText et_Make_Model, et_Color, et_LicensePlateNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicels_details);
        ctx = HPSP_VehicelsDetails_Activity.this;
        preferences = new Preferences(ctx);

        et_Make_Model = findViewById(R.id.et_Make_Model);
        et_Color = findViewById(R.id.et_Color);
        et_LicensePlateNumber = findViewById(R.id.et_LicensePlateNumber);

        LinearLayout ll_start_over = findViewById(R.id.ll_start_over);
        ll_start_over.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                finishAffinity();
                Intent i = new Intent(ctx, SignInOut_Activity.class);
                i.putExtra("is_next_type",true);
                startActivity(i);

            }
        });

        LinearLayout ll_Next = findViewById(R.id.ll_Next);
        ll_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ctx, HPSP_visitInformation_Activity.class);
                i.putExtra("show_vehicales","yes");
                i.putExtra("et_Make_Model",""+et_Make_Model.getText().toString());
                i.putExtra("et_Color",""+et_Color.getText().toString());
                i.putExtra("et_LicensePlateNumber",""+et_LicensePlateNumber.getText().toString());
                startActivity(i);
            }
        });


        LinearLayout ll_Skip = findViewById(R.id.ll_Skip);
        ll_Skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ctx, HPSP_visitInformation_Activity.class);
                i.putExtra("show_vehicales","no");
                i.putExtra("et_Make_Model","");
                i.putExtra("et_Color","");
                i.putExtra("et_LicensePlateNumber","");
                startActivity(i);
            }
        });

        LinearLayout ll_NoCar = findViewById(R.id.ll_NoCar);
        ll_NoCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ctx, HPSP_visitInformation_Activity.class);
                i.putExtra("show_vehicales","no");
                i.putExtra("et_Make_Model","");
                i.putExtra("et_Color","");
                i.putExtra("et_LicensePlateNumber","");
                startActivity(i);
            }
        });


    }


}
