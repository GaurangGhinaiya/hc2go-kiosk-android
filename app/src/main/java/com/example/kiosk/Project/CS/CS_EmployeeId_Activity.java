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
import com.example.kiosk.Model.Model_Facility_Data;
import com.example.kiosk.Model.Model_Facility_Response;
import com.example.kiosk.Model.QuestionsAnswer.Model_QA_CovidQuestion;
import com.example.kiosk.Model.QuestionsAnswer.Model_QA_Data;
import com.example.kiosk.Model.QuestionsAnswer.Model_QA_Response;
import com.example.kiosk.Model.QuestionsAnswer.Model_QA_User;

import com.example.kiosk.R;
import com.example.kiosk.Utill.Preferences;
import com.example.kiosk.Utill.utills;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class CS_EmployeeId_Activity extends AppCompatActivity {


    Context context;
    Preferences preferences;
    EditText et_EmployeeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cs_employeid);
        context = CS_EmployeeId_Activity.this;
        preferences = new Preferences(context);

        et_EmployeeID = findViewById(R.id.et_EmployeeID);
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

                if (et_EmployeeID.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(context, "Please Enter Employee ID", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    method_check_staff_employee_code();
                }

            }
        });

    }


    Dialog progressDialog = null;

    private void method_check_staff_employee_code() {
        if (utills.isOnline(context)) {
            progressDialog = utills.startLoader(context);
            AndroidNetworking.post(GlobalServiceApi.API_check_staff_employee_code)
                    .addBodyParameter("branch_id", "" + preferences.getBranchid())
                    .addBodyParameter("company_id", "" + preferences.getCompany_id())
                    .addBodyParameter("employee_code", "" + et_EmployeeID.getText().toString())
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

                                    Model_QA_Response agency_responce = new Gson().fromJson(result, Model_QA_Response.class);

                                    if (agency_responce.getData() != null) {
                                        Model_QA_Data model_qa_data = new Model_QA_Data();
                                        model_qa_data = agency_responce.getData();



                                         if(model_qa_data.getUser() != null){
                                             Model_QA_User qa_user =new Model_QA_User();
                                             qa_user = model_qa_data.getUser();
                                             preferences.setUser_Id(""+qa_user.getId());
                                            preferences.setUser_Name(""+qa_user.getName());

                                        }

                                        List<Model_QA_CovidQuestion> agency_data_list = new ArrayList<>();
                                        agency_data_list = model_qa_data.getCovidQuestions();

                                        if (agency_data_list.size() > 0) {
                                            Intent i = new Intent(context, CS_QuestionAnswer_Activity.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putString("arraylist_QA", new Gson().toJson(agency_data_list));
                                            i.putExtras(bundle);
                                            startActivity(i);

                                        }

                                    }

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
