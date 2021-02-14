package com.example.kiosk.Project.HPSP;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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
import com.example.kiosk.R;
import com.example.kiosk.Utill.Preferences;
import com.example.kiosk.Utill.utills;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import static android.view.inputmethod.InputMethodManager.HIDE_IMPLICIT_ONLY;


public class HPSP_FirstTime_Activity extends AppCompatActivity {


    Context ctx;
    Preferences preferences;
    EditText et_name,et_Title,et_NameOfEmployer,et_company_name,et_EmailID,et_PhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firsttime);
        ctx = HPSP_FirstTime_Activity.this;
        preferences = new Preferences(ctx);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        et_name = findViewById(R.id.et_name);
        et_Title = findViewById(R.id.et_Title);
        et_NameOfEmployer = findViewById(R.id.et_NameOfEmployer);
        et_EmailID = findViewById(R.id.et_EmailID);
        et_PhoneNumber = findViewById(R.id.et_PhoneNumber);
        et_company_name = findViewById(R.id.et_company_name);


        LinearLayout ll_Register = findViewById(R.id.ll_Register);
        ll_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (preferences.getMain_Type().equalsIgnoreCase("1")) {
                    method_check_staff_employee_code(GlobalServiceApi.API_add_provider_info);
                } else if (preferences.getMain_Type().equalsIgnoreCase("3")) {
                    method_check_staff_employee_code(GlobalServiceApi.API_add_volunteer_info);
                }
            }
        });

        LinearLayout ll_main = findViewById(R.id.ll_main);
        ll_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
            }
        });
    }


    Dialog progressDialog = null;

    private void method_check_staff_employee_code(String API) {
        if (utills.isOnline(ctx)) {
            progressDialog = utills.startLoader(ctx);
            AndroidNetworking.post(API)
                    .addBodyParameter("branch_id", "" + preferences.getBranchid())
                    .addBodyParameter("company_id", "" + preferences.getCompany_id())
                    .addBodyParameter("name", "" + et_name.getText().toString())
                    .addBodyParameter("title", "" + et_Title.getText().toString())
                    .addBodyParameter("email", "" + et_EmailID.getText().toString())
                    .addBodyParameter("phone", "" + et_PhoneNumber.getText().toString())
                    .addBodyParameter("company", "" + et_company_name.getText().toString())
                    .build()
                    .getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String result) {
                            utills.stopLoader(progressDialog);
                            Log.e("API_add_provider_info", result);
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



                                    Intent i = new Intent(ctx, HPSP_EpartmentDetails_Activity.class);
                                    startActivity(i);

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

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(HIDE_IMPLICIT_ONLY, 0);

    }

}
