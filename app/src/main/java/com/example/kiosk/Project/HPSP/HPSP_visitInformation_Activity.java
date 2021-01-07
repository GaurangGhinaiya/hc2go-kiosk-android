package com.example.kiosk.Project.HPSP;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.kiosk.Project.SignInOut_Activity;

import com.example.kiosk.R;
import com.example.kiosk.Utill.Preferences;


public class HPSP_visitInformation_Activity extends AppCompatActivity {


    Context ctx;
    Preferences preferences;
    TextView tv_name, tv_MobilePhone,  tv_MakeModel, tv_Color, tv_LicensePlateNumber;

    EditText et_CompanyName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_information);
        ctx = HPSP_visitInformation_Activity.this;
        preferences = new Preferences(ctx);


        Intent intent = getIntent();
        String show_vehicales = "" + intent.getStringExtra("show_vehicales");
        String et_Make_Model = "" + intent.getStringExtra("et_Make_Model");
        String et_Color = "" + intent.getStringExtra("et_Color");
        String et_LicensePlateNumber = "" + intent.getStringExtra("et_LicensePlateNumber");


        tv_name = findViewById(R.id.tv_name);
        tv_MobilePhone = findViewById(R.id.tv_MobilePhone);
        et_CompanyName = findViewById(R.id.tv_CompanyName);
        et_CompanyName.setFocusable(false);
        et_CompanyName.setClickable(false);
        tv_MakeModel = findViewById(R.id.tv_MakeModel);
        tv_Color = findViewById(R.id.tv_Color);
        tv_LicensePlateNumber = findViewById(R.id.tv_LicensePlateNumber);


        LinearLayout ll_vehicales_details = findViewById(R.id.ll_vehicales_details);
        if (show_vehicales.equalsIgnoreCase("yes")) {
            ll_vehicales_details.setVisibility(View.VISIBLE);
        } else {
            ll_vehicales_details.setVisibility(View.GONE);
        }
        TextView tv_VehicleInformation = findViewById(R.id.tv_VehicleInformation);
        TextView tv_edit_company_name = findViewById(R.id.tv_edit_company_name);
        tv_VehicleInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_edit_company_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_CompanyName.setClickable(true);
                et_CompanyName.setFocusableInTouchMode(true);
                et_CompanyName.setFocusable(true);
                et_CompanyName.requestFocus();
                et_CompanyName.setBackground(getResources().getDrawable(R.drawable.bg_trans_blue));
            }
        });


        tv_MakeModel.setText("" + et_Make_Model);
        tv_Color.setText("" + et_Color);
        tv_LicensePlateNumber.setText("" + et_LicensePlateNumber);

        tv_name.setText("" + preferences.getUser_Name());
        tv_MobilePhone.setText("" + preferences.getUser_phone());
        et_CompanyName.setText("" + preferences.getUser_company());


        LinearLayout ll_StartOver = findViewById(R.id.ll_StartOver);
        ll_StartOver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                finishAffinity();
                Intent i = new Intent(ctx, SignInOut_Activity.class);
                i.putExtra("is_next_type", true);
                startActivity(i);
            }
        });

        LinearLayout ll_Next = findViewById(R.id.ll_Next);
        ll_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                et_CompanyName.setFocusable(false);
                et_CompanyName.setBackground(null);
                preferences.setUser_company(""+et_CompanyName.getText().toString());
                preferences.setvehicle_model("" + tv_MakeModel.getText().toString());
                preferences.setvehicle_color("" + tv_Color.getText().toString());
                preferences.setvehicle_plate("" + tv_LicensePlateNumber.getText().toString());

                Intent i = new Intent(ctx, HPSP_Using_QuestionAnswer_Activity.class);
                startActivity(i);
            }
        });


    }
}
