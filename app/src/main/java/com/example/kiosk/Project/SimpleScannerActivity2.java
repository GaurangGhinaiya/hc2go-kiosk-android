package com.example.kiosk.Project;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.budiyev.android.codescanner.CodeScannerView;

import com.example.kiosk.API.GlobalServiceApi;
import com.example.kiosk.Model.Pass.Model_HPass_Answer;
import com.example.kiosk.Model.Pass.Model_HPass_Data;
import com.example.kiosk.Model.Pass.Model_HPass_LastAnswer;
import com.example.kiosk.Model.Pass.Model_HPass_Response;
import com.example.kiosk.Model.Pass.Model_HPass_User;
import com.example.kiosk.Model.PhoneNumber.Model_Phone_Response;
import com.example.kiosk.Model.QuestionsAnswer.Model_QA_User;
import com.example.kiosk.Project.FG.FG_EpartmentDetails_Activity;
import com.example.kiosk.Project.HPSP.HPSP_EpartmentDetails_Activity;
import com.example.kiosk.R;
import com.example.kiosk.Utill.Preferences;
import com.example.kiosk.Utill.utills;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class SimpleScannerActivity2 extends AppCompatActivity {

    Context context;
    String TAG = "sacnner";
    Preferences preferences;
    private boolean mPermissionGranted;
    private static final int RC_PERMISSION = 10;


    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_simple_scanner);
        context = SimpleScannerActivity2.this;
        preferences = new Preferences(context);
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        scannerView.setVisibility(View.GONE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                mPermissionGranted = false;
                requestPermissions(new String[]{Manifest.permission.CAMERA}, RC_PERMISSION);
            } else {
                mPermissionGranted = true;
            }
        } else {
            mPermissionGranted = true;
        }


        if (mPermissionGranted) {

        }


        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setTorchEnabled(true);
        integrator.setBeepEnabled(false);

        integrator.initiateScan();


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                Log.e(TAG, "Cancelled");
                finish();
            } else {
                Log.e(TAG, "" + result.getContents());
                Log.e(TAG, "" + result.getFormatName());
                // Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                method_get_api("" + result.getContents());

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    Dialog progressDialog = null;

    private void method_get_api(String link) {


        if (utills.isOnline(context)) {


            int index = link.lastIndexOf('/');
            String lastString = "" + link.substring(index + 1);

            Log.e("onCreate: ", "" + lastString.trim());


            progressDialog = utills.startLoader(context);
            AndroidNetworking.post(GlobalServiceApi.API_get_homecare_pass)
                    .addBodyParameter("user_id", "" + lastString)
                    .addBodyParameter("company_id", "" + preferences.getCompany_id())
                    .addBodyParameter("branch_id", "" + preferences.getBranchid())
                    .build()
                    .getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String result) {
                            utills.stopLoader(progressDialog);
                            Log.e("Getfind_agency", result);
                            if (result == null || result == "") return;

                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                String flag = jsonObject.getString("flag");
                                String message = jsonObject.getString("message");
                                if (flag.equalsIgnoreCase("true")) {

                                    preferences.setIS_Homecare_Pass("true");

                                    Model_HPass_Response modelHPassResponse = new Gson().fromJson(result, Model_HPass_Response.class);
                                    if (modelHPassResponse.getData() != null) {

                                        Model_HPass_Data hPass_data = new Model_HPass_Data();
                                        hPass_data = modelHPassResponse.getData();


                                        List<Model_HPass_Answer> questions_list = new ArrayList<>();
                                        if (hPass_data.getquestions() != null) {
                                            questions_list = hPass_data.getquestions();
                                            if (questions_list.size() > 0) {
                                                preferences.setHomecarePassQList("" + new Gson().toJson(questions_list));
                                            } else {
                                                preferences.setHomecarePassQList("");
                                            }
                                        } else {
                                            preferences.setHomecarePassQList("");
                                        }


                                        List<Model_HPass_Answer> answer_list = new ArrayList<>();
                                        if (hPass_data.getAnswer() != null) {
                                            answer_list = hPass_data.getAnswer();
                                            if (answer_list.size() > 0) {
                                                preferences.setHomecarePassANSList("" + new Gson().toJson(answer_list));
                                            } else {
                                                preferences.setHomecarePassANSList("");
                                            }
                                        } else {
                                            preferences.setHomecarePassANSList("");
                                        }



                                        if (hPass_data.getUser() != null) {
                                            Model_HPass_User qa_user = new Model_HPass_User();
                                            qa_user = hPass_data.getUser();

                                            if (preferences.getMain_Type().equalsIgnoreCase("1")) {
                                                method_add_provider(qa_user, GlobalServiceApi.API_update_provider_info);
                                            } else if (preferences.getMain_Type().equalsIgnoreCase("3")) {
                                                method_add_provider(qa_user, GlobalServiceApi.API_update_volunteer_info);
                                            } else if (preferences.getMain_Type().equalsIgnoreCase("2")) {
                                                method_add_fg(qa_user);
                                            }


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

    private void method_add_provider(Model_HPass_User qa_user, String API) {
        if (utills.isOnline(context)) {
            progressDialog = utills.startLoader(context);
            AndroidNetworking.post(API)
                    .addBodyParameter("branch_id", "" + preferences.getBranchid())
                    .addBodyParameter("company_id", "" + preferences.getCompany_id())
                    .addBodyParameter("name", "" + qa_user.getName())
                    .addBodyParameter("title", "" + qa_user.getName())
                    .addBodyParameter("email", "" + qa_user.getEmail())
                    .addBodyParameter("phone", "" + qa_user.getPhone())
                    .addBodyParameter("company", "" + qa_user.getCompany())
                    .build()
                    .getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String result) {
                            utills.stopLoader(progressDialog);
                            Log.e("API_add_info", result);
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

    private void method_add_fg(Model_HPass_User qa_user) {
        if (utills.isOnline(context)) {
            progressDialog = utills.startLoader(context);
            AndroidNetworking.post(GlobalServiceApi.API_update_family_guest_info)
                    .addBodyParameter("branch_id", "" + preferences.getBranchid())
                    .addBodyParameter("company_id", "" + preferences.getCompany_id())
                    .addBodyParameter("name", "" + qa_user.getName())
                    .addBodyParameter("email", "" + qa_user.getEmail())
                    .addBodyParameter("phone", "" + qa_user.getPhone())
                    .build()
                    .getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String result) {
                            utills.stopLoader(progressDialog);
                            Log.e("API_add_info_fg", result);
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
                                        preferences.setfamily_type("" + qa_user.getfamily_type());

                                    }


                                    Intent i = new Intent(context, FG_EpartmentDetails_Activity.class);
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