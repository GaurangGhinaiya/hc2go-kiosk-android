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
import androidx.cardview.widget.CardView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.example.kiosk.API.GlobalServiceApi;
import com.example.kiosk.Model.PhoneNumber.Model_Phone_Response;

import com.example.kiosk.Model.QuestionsAnswer.Model_QA_User;

import com.example.kiosk.R;
import com.example.kiosk.Utill.Preferences;
import com.example.kiosk.Utill.utills;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class HPSP_Using_PhoneNumber_Activity extends AppCompatActivity {


    Context context;
    Preferences preferences;
    EditText et_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phonenumber);
        context = HPSP_Using_PhoneNumber_Activity.this;
        preferences = new Preferences(context);

        et_number = findViewById(R.id.et_number);
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

                if (et_number.getText().toString().equalsIgnoreCase("(###) ###-####")) {
                    Toast.makeText(context, "Please Enter number", Toast.LENGTH_SHORT).show();
                    return;
                } else {

                    if (preferences.getMain_Type().equalsIgnoreCase("1")) {
                        method_check_staff_employee_code(GlobalServiceApi.API_check_provider_phonenumber);
                    } else if (preferences.getMain_Type().equalsIgnoreCase("3")) {
                        method_check_staff_employee_code(GlobalServiceApi.API_check_volunteer_phonenumber);
                    }


                }

            }
        });


    }


    Dialog progressDialog = null;

    private void method_check_staff_employee_code(String API) {
        if (utills.isOnline(context)) {
            progressDialog = utills.startLoader(context);
            AndroidNetworking.post(API)
                    .addBodyParameter("branch_id", "" + preferences.getBranchid())
                    .addBodyParameter("company_id", "" + preferences.getCompany_id())
                    .addBodyParameter("phone", "" + et_number.getText().toString())
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

                                    Model_Phone_Response agency_responce = new Gson().fromJson(result, Model_Phone_Response.class);
                                    if (agency_responce.getData() != null) {
                                        Model_QA_User qa_user = new Model_QA_User();
                                        qa_user = agency_responce.getData();

                                        preferences.setUser_Id("" + qa_user.getId());
                                        preferences.setUser_Name("" + qa_user.getName());
                                        preferences.setUser_phone("" + qa_user.getphone());
                                        preferences.setUser_company("" + qa_user.getcompany());

                                    }

                                    Intent i = new Intent(context, HPSP_EpartmentDetails_Activity.class);
                                    startActivity(i);

                                } else {
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
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
