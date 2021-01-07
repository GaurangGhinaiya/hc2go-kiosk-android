package com.example.kiosk.Project;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class SimpleScannerActivity extends AppCompatActivity implements ZBarScannerView.ResultHandler {
    private ZBarScannerView mScannerView;

    Context context;
    String TAG = "sacnner";
    Preferences preferences;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_simple_scanner);
        context = SimpleScannerActivity.this;
        preferences = new Preferences(context);
        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new ZBarScannerView(this);
        contentFrame.addView(mScannerView);

        // Set the scanner view as the content view

    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera(1);          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {


        Log.e(TAG, rawResult.getContents());      // Prints scan results
        Log.e(TAG, rawResult.getBarcodeFormat().getName());     // Prints the scan format (qrcode, pdf417 etc.)

        method_get_api("" + rawResult.getContents());

        mScannerView.stopCamera();


    }

    Dialog progressDialog = null;

    private void method_get_api(String link) {
        if (utills.isOnline(context)) {
            progressDialog = utills.startLoader(context);
            AndroidNetworking.get(link + "?kiosk")
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

                                    Model_HPass_Response modelHPassResponse = new Gson().fromJson(result, Model_HPass_Response.class);
                                    if (modelHPassResponse.getData() != null) {

                                        Model_HPass_Data hPass_data = new Model_HPass_Data();
                                        hPass_data = modelHPassResponse.getData();


                                        if (hPass_data.getLastAnswer() != null) {
                                            Model_HPass_LastAnswer hPassLastAnswer = new Model_HPass_LastAnswer();
                                            hPassLastAnswer = hPass_data.getLastAnswer();

                                            List<Model_HPass_Answer> answers_list = new ArrayList<>();

                                            if(hPassLastAnswer.getAnswer()!= null){
                                                answers_list = hPassLastAnswer.getAnswer();
                                            }

                                            String getlast_date = hPassLastAnswer.getDate();
                                            //  String getlast_date = "2021-01-04 01:56:16";
                                            String currenttime = utills.formatDate_currenttime();

                                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            Date convertedDate_last = new Date();
                                            Date convertedDate_current = new Date();
                                            try {

                                                convertedDate_last = dateFormat.parse(getlast_date);
                                                convertedDate_current = dateFormat.parse(currenttime);

                                                long different = convertedDate_current.getTime() - convertedDate_last.getTime();
                                                long hoursInMilli = 1000 * 60 * 60;
                                                long elapsedHours = different / hoursInMilli;

                                                if (elapsedHours > 8) {
                                                    if(answers_list.size()>0){
                                                        preferences.setHomecarePassQList(""+new Gson().toJson(answers_list));
                                                    }else {
                                                        preferences.setHomecarePassQList("");
                                                    }
                                                } else {
                                                    preferences.setHomecarePassQList("");
                                                }
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                        }

                                        if (hPass_data.getUser() != null) {
                                            Model_HPass_User qa_user = new Model_HPass_User();
                                            qa_user = hPass_data.getUser();

                                            if (preferences.getMain_Type().equalsIgnoreCase("1")) {
                                                method_add_provider(qa_user,GlobalServiceApi.API_update_provider_info);
                                            } else if (preferences.getMain_Type().equalsIgnoreCase("3")) {
                                                method_add_provider(qa_user,GlobalServiceApi.API_update_volunteer_info);
                                            }else if (preferences.getMain_Type().equalsIgnoreCase("2")) {
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

    private void method_add_provider(Model_HPass_User qa_user,String API) {
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
                    .addBodyParameter("name", "" +qa_user.getName())
                    .addBodyParameter("email", "" +qa_user.getEmail())
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