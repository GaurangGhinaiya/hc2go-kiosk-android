package com.example.kiosk.Project.CS;

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
import com.example.kiosk.Model.PhoneNumber.Model_Phone_Response;
import com.example.kiosk.Model.QuestionsAnswer.Model_QA_User;
import com.example.kiosk.Project.HPSP.HPSP_EpartmentDetails_Activity;
import com.example.kiosk.Project.SignOut_complete_Activity;
import com.example.kiosk.R;
import com.example.kiosk.Utill.Preferences;
import com.example.kiosk.Utill.utills;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;


public class CS_signout_Activity extends AppCompatActivity {


    Context ctx;
    Preferences preferences;
    EditText et_employe_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cs_signout);
        ctx = CS_signout_Activity.this;
        preferences = new Preferences(ctx);

        et_employe_id = findViewById(R.id.et_employe_id);
        LinearLayout ll_back = findViewById(R.id.ll_back);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        LinearLayout ll_SignOut = findViewById(R.id.ll_SignOut);
        ll_SignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (et_employe_id.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(ctx, "Please Enter Employee ID", Toast.LENGTH_SHORT).show();
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
            String gmtTime = "" + utills.formatDate_currenttime();

            progressDialog = utills.startLoader(ctx);
            AndroidNetworking.post(GlobalServiceApi.API_staff_signout)
                    .addBodyParameter("branch_id", "" + preferences.getBranchid())
                    .addBodyParameter("company_id", "" + preferences.getCompany_id())
                    .addBodyParameter("employee_code", "" + et_employe_id.getText().toString())
                    .addBodyParameter("signout", "" + gmtTime)
                    .build()
                    .getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String result) {
                            utills.stopLoader(progressDialog);
                            Log.e("check_staff_employee", result);
                            if (result == null || result == "") return;


                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                String flag = jsonObject.getString("flag");
                                String message = jsonObject.getString("message");
                                if (flag.equalsIgnoreCase("true")) {

                                    Intent i = new Intent(ctx, SignOut_complete_Activity.class);
                                    startActivity(i);

                                }

                                Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
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
