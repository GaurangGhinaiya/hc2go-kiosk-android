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

import com.example.kiosk.Model.RoomEmpart_Details.Model_RoomEmpartDetails_Data;

import com.example.kiosk.R;
import com.example.kiosk.Utill.Preferences;
import com.example.kiosk.Utill.utills;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class HPSP_VisitElse_Activity extends AppCompatActivity {


    Context ctx;
    Preferences preferences;

    int visit_type = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visiting_else);
        ctx = HPSP_VisitElse_Activity.this;
        preferences = new Preferences(ctx);


        Bundle extras = getIntent().getExtras();
        String arraylist_a = extras.getString("arraylist_member");
        Type listType = new TypeToken<List<Model_RoomEmpartDetails_Data>>() {
        }.getType();

        List<Model_RoomEmpartDetails_Data> agency_data_list = new ArrayList<>();
        agency_data_list = new Gson().fromJson(arraylist_a, listType);


        if (preferences.getpatient_list().equalsIgnoreCase("") || preferences.getpatient_list().equalsIgnoreCase("null")) {
            preferences.setpatient_list("" + new Gson().toJson(agency_data_list));
        } else {

            List<Model_RoomEmpartDetails_Data> agency_data_list_temp = new ArrayList<>();
            String getpatient_list = preferences.getpatient_list();
            Type listType2 = new TypeToken<List<Model_RoomEmpartDetails_Data>>() {}.getType();
            agency_data_list_temp = new Gson().fromJson(getpatient_list, listType2);
            agency_data_list.addAll(agency_data_list_temp);
            preferences.setpatient_list("" + new Gson().toJson(agency_data_list));
        }

        Log.e("getpatient_list", "" + preferences.getpatient_list());

        //   new Gson().toJson(agency_data_list)


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
                if (visit_type == 0) {
                    Toast.makeText(ctx, "Will you be Visiting anyone Else?  Yes or No", Toast.LENGTH_SHORT).show();
                    return;
                } else if (visit_type == 1) {
                    Intent i = new Intent(ctx, HPSP_EpartmentDetails_Activity.class);
                    startActivity(i);
                    finish_activity();
                } else if (visit_type == 2) {
                    Intent i = new Intent(ctx, HPSP_VehicelsDetails_Activity.class);
                    startActivity(i);
                }
            }
        });


        LinearLayout ll_visit_no = findViewById(R.id.ll_visit_no);
        LinearLayout ll_visit_yes = findViewById(R.id.ll_visit_yes);

        ll_visit_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                visit_type = 1;
                Intent i = new Intent(ctx, HPSP_EpartmentDetails_Activity.class);
                startActivity(i);
                finish_activity();
            }
        });

        ll_visit_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                visit_type = 2;
                Intent i = new Intent(ctx, HPSP_VehicelsDetails_Activity.class);
                startActivity(i);
            }
        });


    }

    private void finish_activity() {
        HPSP_PatientList_Activity.method_finish();
        finish();
    }


}
