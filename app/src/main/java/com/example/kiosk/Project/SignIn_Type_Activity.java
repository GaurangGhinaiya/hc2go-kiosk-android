package com.example.kiosk.Project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.kiosk.Project.CS.CS_EmployeeId_Activity;
import com.example.kiosk.Project.CS.CS_signout_Activity;
import com.example.kiosk.Project.FG.FG_Type_Activity;
import com.example.kiosk.Project.HPSP.HPSP_Type_Activity;
import com.example.kiosk.Project.Resident.Resident_EpartmentDetails_Activity;
import com.example.kiosk.Project.Resident.Resident_Signout_No_Activity;
import com.example.kiosk.R;
import com.example.kiosk.Utill.Preferences;


public class SignIn_Type_Activity extends AppCompatActivity {


    Context ctx;
    Preferences preferences;

    boolean sign_in_out = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_type);
        ctx = SignIn_Type_Activity.this;
        preferences = new Preferences(ctx);


        Intent intent = getIntent();
        sign_in_out = intent.getBooleanExtra("sign_in_out", false);


        CardView card_healthchechk_provider = findViewById(R.id.card_healthchechk_provider);
        CardView card_FamilyorGuest = findViewById(R.id.card_FamilyorGuest);
        CardView card_Resident = findViewById(R.id.card_Resident);
        CardView card_CommunityStaff = findViewById(R.id.card_CommunityStaff);
        CardView card_Volunteer = findViewById(R.id.card_Volunteer);

        card_healthchechk_provider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preferences.setMain_Type("1");
                if (sign_in_out) {
                    Intent i = new Intent(ctx, Comman_signout_Activity.class);
                    startActivity(i);
                } else {

                    Intent i = new Intent(ctx, HPSP_Type_Activity.class);
                    startActivity(i);
                }


            }
        });
        card_FamilyorGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preferences.setMain_Type("2");
                if (sign_in_out) {
                    Intent i = new Intent(ctx, Comman_signout_Activity.class);
                    startActivity(i);
                } else {

                    Intent i = new Intent(ctx, FG_Type_Activity.class);
                    startActivity(i);
                }
            }
        });
        card_Volunteer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preferences.setMain_Type("3");
                if (sign_in_out) {
                    Intent i = new Intent(ctx, Comman_signout_Activity.class);
                    startActivity(i);
                } else {

                    Intent i = new Intent(ctx, HPSP_Type_Activity.class);
                    startActivity(i);
                }
            }
        });
        card_Resident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preferences.setMain_Type("4");
                if (sign_in_out) {
                    Intent i = new Intent(ctx, Resident_Signout_No_Activity.class);  // resident
                    startActivity(i);
                } else {

                    Intent i = new Intent(ctx, Resident_EpartmentDetails_Activity.class);
                    startActivity(i);
                }
            }
        });
        card_CommunityStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preferences.setMain_Type("5");
                if (sign_in_out) {
                    Intent i = new Intent(ctx, CS_signout_Activity.class);
                    startActivity(i);
                } else {

                    Intent i = new Intent(ctx, CS_EmployeeId_Activity.class);
                    startActivity(i);
                }
            }
        });

    }

    @Override
    protected void onResume() {
        preferences.setMain_Type("");
        super.onResume();
    }
}
