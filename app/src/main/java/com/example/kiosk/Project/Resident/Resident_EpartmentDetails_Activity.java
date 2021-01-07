package com.example.kiosk.Project.Resident;

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

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.example.kiosk.API.GlobalServiceApi;
import com.example.kiosk.Model.RoomEmpart_Details.Model_RoomEmpartDetails_Data;
import com.example.kiosk.Model.RoomEmpart_Details.Model_RoomEmpartDetails_Response;


import com.example.kiosk.R;
import com.example.kiosk.Utill.Preferences;
import com.example.kiosk.Utill.utills;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Resident_EpartmentDetails_Activity extends AppCompatActivity {


    Context ctx;
    Preferences preferences;

    EditText et_apat_room_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apartdetails);
        ctx = Resident_EpartmentDetails_Activity.this;
        preferences = new Preferences(ctx);


        et_apat_room_number = findViewById(R.id.et_apat_room_number);

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
                if (et_apat_room_number.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(ctx, "Please Enter number", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    method_check_staff_employee_code();
                }
            }
        });


    }


    Dialog progressDialog = null;

    private void method_check_staff_employee_code() {
        if (utills.isOnline(ctx)) {
            progressDialog = utills.startLoader(ctx);
            AndroidNetworking.post(GlobalServiceApi.API_get_resident)
                    .addBodyParameter("branch_id", "" + preferences.getBranchid())
                    .addBodyParameter("company_id", "" + preferences.getCompany_id())
                    .addBodyParameter("room_number", "" + et_apat_room_number.getText().toString())
                    .build()
                    .getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String result) {
                            utills.stopLoader(progressDialog);
                            Log.e("API_get_resident", result);
                            if (result == null || result == "") return;
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                String flag = jsonObject.getString("flag");
                                String message = jsonObject.getString("message");
                                if (flag.equalsIgnoreCase("true")) {

                                    Model_RoomEmpartDetails_Response agency_responce = new Gson().fromJson(result, Model_RoomEmpartDetails_Response.class);

                                    if (agency_responce.getData() != null) {

                                        List<Model_RoomEmpartDetails_Data> agency_data_list = new ArrayList<>();
                                        agency_data_list = agency_responce.getData();

                                            Intent i = new Intent(ctx, R_PatientList_Activity.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putString("arraylist_member", new Gson().toJson(agency_data_list));
                                            i.putExtras(bundle);
                                            startActivity(i);

                                    }

                                } else {
                                    Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            utills.stopLoader(progressDialog);
                            Log.d("API", anError.toString());
                        }
                    });
        }
    }



}
