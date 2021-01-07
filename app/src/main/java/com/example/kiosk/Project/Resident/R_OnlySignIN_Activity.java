package com.example.kiosk.Project.Resident;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.brother.ptouch.sdk.LabelInfo;
import com.brother.ptouch.sdk.Printer;
import com.brother.ptouch.sdk.PrinterInfo;
import com.brother.ptouch.sdk.PrinterStatus;
import com.bumptech.glide.Glide;
import com.example.kiosk.API.GlobalServiceApi;
import com.example.kiosk.Model.QuestionsAnswer.Model_QA_CovidQuestion;
import com.example.kiosk.Model.RoomEmpart_Details.Model_RoomEmpartDetails_Data;
import com.example.kiosk.Project.Print_complete_Activity;
import com.example.kiosk.R;
import com.example.kiosk.Utill.Preferences;
import com.example.kiosk.Utill.utills;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class R_OnlySignIN_Activity extends AppCompatActivity {


    Context ctx;
    Preferences preferences;


    ImageView iv_profile_image;
    LinearLayout ll_get_temprature;
    TextView tv_temprature, tv_temperature_lablel;
    String device_id = "7c:25:da:1e:d3:ad";


    String user_image = "";
    String temperature = "";

    List<Model_QA_CovidQuestion> list_services_temp_move = new ArrayList<>();


    LinearLayout ll_printing_process;
    RelativeLayout rl_main_print;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resident_signin);
        ctx = R_OnlySignIN_Activity.this;
        preferences = new Preferences(ctx);


        device_id = "" + preferences.getPRE_DeviceMacAddress();

        ll_printing_process = findViewById(R.id.ll_printing_process);
        rl_main_print = findViewById(R.id.rl_main_print);

        iv_profile_image = findViewById(R.id.iv_profile_image);
        ll_get_temprature = findViewById(R.id.ll_get_temprature);
        tv_temprature = findViewById(R.id.tv_temprature);
        tv_temperature_lablel = findViewById(R.id.tv_temperature_lablel);

        LinearLayout ll_back = findViewById(R.id.ll_back);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        Bundle extras = getIntent().getExtras();
        String arraylist_a = extras.getString("arraylist_QA");
        Type listType = new TypeToken<List<Model_QA_CovidQuestion>>() {
        }.getType();


        list_services_temp_move = new ArrayList<>();
        list_services_temp_move = new Gson().fromJson(arraylist_a, listType);


        LinearLayout ll_CompleteSignIn = findViewById(R.id.ll_CompleteSignIn);
        ll_CompleteSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                method_signup_cs(4);
            }
        });


        ll_get_temprature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                method_get();
            }
        });

    }


    Dialog progressDialog = null;

    private void method_get() {
        if (utills.isOnline(ctx)) {
            progressDialog = utills.startLoader(ctx);

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("username", "hc2go");
                jsonObject.put("password", "123456");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            AndroidNetworking.post(GlobalServiceApi.PRINT_URL + device_id)
                    .addJSONObjectBody(jsonObject)
                    .build()
                    .getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String result) {
                            utills.stopLoader(progressDialog);
                            Log.e("result", result);
                            if (result == null || result == "") return;

                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                String message = jsonObject.getString("message");
                                Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();

                                String code = jsonObject.getString("code");
                                JSONObject response = jsonObject.getJSONObject("response");

                                String name = response.getString("name");
                                temperature = response.getString("temperature");
                                String temperature_label = response.getString("temperature_label");
                                String mask = response.getString("mask");
                                String timestamp = response.getString("timestamp");
                                String mac = response.getString("mac");
                                user_image = response.getString("image");

                                tv_temperature_lablel.setText("" + temperature_label);
                                tv_temprature.setText("" + temperature + " F");


                                String base64String = user_image;
                                String base64Image = base64String.split(",")[1];
                                byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);


                                Glide.with(ctx)
                                        .load(decodedByte)
                                        .into(iv_profile_image);


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

    private void method_signup_cs(int type) {
        if (utills.isOnline(ctx)) {

            rl_main_print.setVisibility(View.GONE);
            ll_printing_process.setVisibility(View.VISIBLE);

            JSONArray array = new JSONArray();

            if (list_services_temp_move.size() > 0) {
                for (int i = 0; i < list_services_temp_move.size(); i++) {
                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("answer", "" + list_services_temp_move.get(i).answer);
                        obj.put("question_id", list_services_temp_move.get(i).getId());
                        //  obj.put("question_id", "");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    array.put(obj);
                }
            }

            String gmtTime = "" + utills.formatDate_currenttime();

            AndroidNetworking.post(GlobalServiceApi.API_sigin_resident)
                    .addBodyParameter("branch_id", "" + preferences.getBranchid())
                    .addBodyParameter("company_id", "" + preferences.getCompany_id())
                    .addBodyParameter("user_id", "" + preferences.getUser_Id())
                    .addBodyParameter("device_name", "" + preferences.getPRE_DeviceName())
                    .addBodyParameter("ip", "" + preferences.getPRE_DeviceMacAddress())
                    .addBodyParameter("signin", "" + gmtTime) //date
                    .addBodyParameter("type", "" + type)
                    .addBodyParameter("covid_answer", "" + array)
                    .addBodyParameter("profile_pic", "" + user_image)
                    .addBodyParameter("temperature", "" + temperature)
                    .build()
                    .getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String result) {
                            utills.stopLoader(progressDialog);
                            Log.e("result", result);
                            if (result == null || result == "") return;
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                String flag = jsonObject.getString("flag");
                                String message = jsonObject.getString("message");
                                if (flag.equalsIgnoreCase("true")) {
                                    Intent i = new Intent(ctx, Print_complete_Activity.class);
                                    startActivity(i);
                                } else {
                                    Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
                                    rl_main_print.setVisibility(View.VISIBLE);
                                    ll_printing_process.setVisibility(View.GONE);
                                }

                            } catch (JSONException e) {
                                rl_main_print.setVisibility(View.VISIBLE);
                                ll_printing_process.setVisibility(View.GONE);
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            rl_main_print.setVisibility(View.VISIBLE);
                            ll_printing_process.setVisibility(View.GONE);
                            Log.d("API", anError.toString());
                        }
                    });


        }
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
    }
}
