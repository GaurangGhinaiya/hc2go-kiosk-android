package com.example.kiosk.Project.FG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kiosk.Project.HPSP.HPSP_Using_QuestionAnswer_Activity;
import com.example.kiosk.Project.SignInOut_Activity;
import com.example.kiosk.R;
import com.example.kiosk.Utill.Preferences;


public class FG_visitInformation_Activity extends AppCompatActivity {


    Context ctx;
    Preferences preferences;
    TextView tv_name, tv_MobilePhone, tv_MakeModel, tv_Color, tv_LicensePlateNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_information_fg);
        ctx = FG_visitInformation_Activity.this;
        preferences = new Preferences(ctx);


        Intent intent = getIntent();
        String show_vehicales = "" + intent.getStringExtra("show_vehicales");
        String et_Make_Model = "" + intent.getStringExtra("et_Make_Model");
        String et_Color = "" + intent.getStringExtra("et_Color");
        String et_LicensePlateNumber = "" + intent.getStringExtra("et_LicensePlateNumber");


        tv_name = findViewById(R.id.tv_name);
        tv_MobilePhone = findViewById(R.id.tv_MobilePhone);

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
        tv_VehicleInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        tv_MakeModel.setText("" + et_Make_Model);
        tv_Color.setText("" + et_Color);
        tv_LicensePlateNumber.setText("" + et_LicensePlateNumber);

        tv_name.setText("" + preferences.getUser_Name());
        tv_MobilePhone.setText("" + preferences.getUser_phone());


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
                preferences.setvehicle_model("" + tv_MakeModel.getText().toString());
                preferences.setvehicle_color("" + tv_Color.getText().toString());
                preferences.setvehicle_plate("" + tv_LicensePlateNumber.getText().toString());
                Intent i = new Intent(ctx, HPSP_Using_QuestionAnswer_Activity.class);
                startActivity(i);
            }
        });


    }
}
