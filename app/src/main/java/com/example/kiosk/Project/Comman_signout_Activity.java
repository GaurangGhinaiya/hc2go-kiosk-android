package com.example.kiosk.Project;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.example.kiosk.API.GlobalServiceApi;
import com.example.kiosk.R;
import com.example.kiosk.Utill.Preferences;
import com.example.kiosk.Utill.utills;

import org.json.JSONException;
import org.json.JSONObject;


public class Comman_signout_Activity extends AppCompatActivity {


    Context ctx;
    Preferences preferences;
    EditText et_PhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signout_phonenumber);
        ctx = Comman_signout_Activity.this;
        preferences = new Preferences(ctx);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        et_PhoneNumber = findViewById(R.id.et_PhoneNumber);
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
                if (et_PhoneNumber.getText().toString().equalsIgnoreCase("(###) ###-####")) {
                    Toast.makeText(ctx, "Please Enter Phone Number", Toast.LENGTH_SHORT).show();
                    return;
                } else {

                    if (preferences.getMain_Type().equalsIgnoreCase("1")) {
                        method_check_staff_employee_code(1);
                    } else if (preferences.getMain_Type().equalsIgnoreCase("3")) {
                        method_check_staff_employee_code(3);
                    } else if (preferences.getMain_Type().equalsIgnoreCase("2")) {
                        method_check_staff_employee_code(2);
                    }

                }
            }
        });


    }

    Dialog progressDialog = null;

    private void method_check_staff_employee_code(int type) {
        if (utills.isOnline(ctx)) {
            String gmtTime = "" + utills.formatDate_currenttime();

            progressDialog = utills.startLoader(ctx);
            AndroidNetworking.post(GlobalServiceApi.API_visitor_signout)
                    .addBodyParameter("branch_id", "" + preferences.getBranchid())
                    .addBodyParameter("company_id", "" + preferences.getCompany_id())
                    .addBodyParameter("phone", "" + et_PhoneNumber.getText().toString())
                    .addBodyParameter("signout", "" + gmtTime)
                    .addBodyParameter("type", "" + type)
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
