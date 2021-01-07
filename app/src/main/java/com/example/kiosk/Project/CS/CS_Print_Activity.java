package com.example.kiosk.Project.CS;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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


public class CS_Print_Activity extends AppCompatActivity {


    Context ctx;
    Preferences preferences;


    ImageView iv_profile_image;
    LinearLayout ll_get_temprature;
    TextView tv_temprature, tv_temperature_lablel;
    String device_id = "7c:25:da:1e:d3:ad";


    String FACILITY_LOGO = "";
    String mac_ip_address = "";

    Bitmap decodedByte;
    String Printer_Type = "1";
    String TIMEZONE = "";
    String user_image = "";
    String temperature = "";

    List<Model_QA_CovidQuestion> list_services_temp_move = new ArrayList<>();


    LinearLayout ll_printing_process;
    RelativeLayout rl_main_print;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);
        ctx = CS_Print_Activity.this;
        preferences = new Preferences(ctx);

        TIMEZONE = "" + preferences.getTIMEZONE();
        FACILITY_LOGO = "" + preferences.getFACILITY();
        mac_ip_address = "" + preferences.getWIFI_USB_PRINTER_id();
        Printer_Type = "" + preferences.getWIFI_USB_PRINTER_Type();
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


        CardView card_print = findViewById(R.id.card_print);
        card_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (preferences.getMain_Type().equalsIgnoreCase("5")) {
                    method_signup_cs(5);
                } else if (preferences.getMain_Type().equalsIgnoreCase("1")) {
                    method_signup_Healthcare_provide(1, GlobalServiceApi.API_siginProvider);
                } else if (preferences.getMain_Type().equalsIgnoreCase("3")) {
                    method_signup_Healthcare_provide(3, GlobalServiceApi.API_sigin_volunteer);
                } else if (preferences.getMain_Type().equalsIgnoreCase("2")) {
                    method_family_guest(2);
                }


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
                                decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);


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
            progressDialog = utills.startLoader(ctx);

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

            AndroidNetworking.post(GlobalServiceApi.API_sigin_staff)
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
                                    method_set_print();
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


    private void method_signup_Healthcare_provide(int type, String API) {
        if (utills.isOnline(ctx)) {
            progressDialog = utills.startLoader(ctx);

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


            List<Model_RoomEmpartDetails_Data> agency_data_list_temp = new ArrayList<>();
            String getpatient_list = preferences.getpatient_list();
            Type listType2 = new TypeToken<List<Model_RoomEmpartDetails_Data>>() {
            }.getType();
            agency_data_list_temp = new Gson().fromJson(getpatient_list, listType2);

            JSONArray array_visit = new JSONArray();
            if (agency_data_list_temp != null) {
                if (agency_data_list_temp.size() > 0) {
                    for (int i = 0; i < agency_data_list_temp.size(); i++) {
                        JSONObject obj = new JSONObject();
                        try {
                            obj.put("room_id", "" + agency_data_list_temp.get(i).getRoomId());
                            obj.put("resident_id", agency_data_list_temp.get(i).getId());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        array_visit.put(obj);
                    }
                }
            }

            JSONObject obj_personal_info = new JSONObject();
            try {
                // obj_personal_info.put("id", "" + preferences.getUser_Id());
                obj_personal_info.put("name", "" + preferences.getUser_Name());
                obj_personal_info.put("phone", "" + preferences.getUser_phone());
                obj_personal_info.put("company", "" + preferences.getUser_company());

            } catch (JSONException e) {
                e.printStackTrace();
            }


            String gmtTime = "" + utills.formatDate_currenttime();
            AndroidNetworking.post(API)
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
                    .addBodyParameter("vehicle_model", "" + preferences.getvehicle_model())
                    .addBodyParameter("vehicle_color", "" + preferences.getvehicle_color())
                    .addBodyParameter("vehicle_plate", "" + preferences.getvehicle_plate())
                    .addBodyParameter("personal_info", "" + obj_personal_info)
                    .addBodyParameter("visit_rooms", "" + array_visit)
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
                                    method_set_print();
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


    private void method_family_guest(int type) {
        if (utills.isOnline(ctx)) {
            progressDialog = utills.startLoader(ctx);

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


            List<Model_RoomEmpartDetails_Data> agency_data_list_temp = new ArrayList<>();
            String getpatient_list = preferences.getpatient_list();
            Type listType2 = new TypeToken<List<Model_RoomEmpartDetails_Data>>() {
            }.getType();
            agency_data_list_temp = new Gson().fromJson(getpatient_list, listType2);

            JSONArray array_visit = new JSONArray();
            if (agency_data_list_temp != null) {
                if (agency_data_list_temp.size() > 0) {
                    for (int i = 0; i < agency_data_list_temp.size(); i++) {
                        JSONObject obj = new JSONObject();
                        try {
                            obj.put("room_id", "" + agency_data_list_temp.get(i).getRoomId());
                            obj.put("resident_id", agency_data_list_temp.get(i).getId());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        array_visit.put(obj);
                    }
                }
            }

//            JSONObject obj_personal_info = new JSONObject();
//            try {
//                // obj_personal_info.put("id", "" + preferences.getUser_Id());
//                obj_personal_info.put("name", "" + preferences.getUser_Name());
//                obj_personal_info.put("phone", "" + preferences.getUser_phone());
//                obj_personal_info.put("family_type", "" + preferences.getfamily_type());
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }


            String gmtTime = "" + utills.formatDate_currenttime();
            AndroidNetworking.post(GlobalServiceApi.API_sigin_family_guest)
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
                    .addBodyParameter("vehicle_model", "" + preferences.getvehicle_model())
                    .addBodyParameter("vehicle_color", "" + preferences.getvehicle_color())
                    .addBodyParameter("vehicle_plate", "" + preferences.getvehicle_plate())
                    // .addBodyParameter("personal_info", ""+obj_personal_info)
                    .addBodyParameter("visit_rooms", "" + array_visit)
                    .addBodyParameter("family_type", "" + preferences.getfamily_type())
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
                                    method_set_print();
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


    private void method_set_print() {
        String Date_final = "";
        String time_final = "";

        if (TIMEZONE.equalsIgnoreCase("Eastern Standard Time")) {
            Date_final = "" + utills.formatDateToString("GMT-5");
            time_final = "" + utills.formatDateToString_time("GMT-5");
        } else if (TIMEZONE.equalsIgnoreCase("Central Standard Time")) {
            Date_final = "" + utills.formatDateToString("GMT-6");
            time_final = "" + utills.formatDateToString_time("GMT-6");
        } else if (TIMEZONE.equalsIgnoreCase("Mountain Standard Time")) {
            Date_final = "" + utills.formatDateToString("GMT-7");
            time_final = "" + utills.formatDateToString_time("GMT-7");
        } else if (TIMEZONE.equalsIgnoreCase("Pacific Standard Time")) {
            Date_final = "" + utills.formatDateToString("GMT-8");
            time_final = "" + utills.formatDateToString_time("GMT-8");
        } else if (TIMEZONE.equalsIgnoreCase("Alaska Standard Time")) {
            Date_final = "" + utills.formatDateToString("GMT-9");
            time_final = "" + utills.formatDateToString_time("GMT-9");
        } else if (TIMEZONE.equalsIgnoreCase("Hawaii-Aleutian Standard Time")) {
            Date_final = "" + utills.formatDateToString("GMT-10");
            time_final = "" + utills.formatDateToString_time("GMT-10");
        } else {
            Date_final = "";
            time_final = "";
        }

        final Dialog dialog_card = new Dialog(ctx);
        dialog_card.setContentView(R.layout.dialog_print_pass);
        dialog_card.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Window window = dialog_card.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        final LinearLayout ll_print_layout = dialog_card.findViewById(R.id.ll_print_layout);
        TextView tv_user_name = dialog_card.findViewById(R.id.tv_user_name);
        TextView tv_user_date = dialog_card.findViewById(R.id.tv_user_date);
        TextView tv_user_time = dialog_card.findViewById(R.id.tv_user_time);
        TextView tv_user_family = dialog_card.findViewById(R.id.tv_user_family);
        TextView tv_type_pass = dialog_card.findViewById(R.id.tv_type_pass);
        TextView tv_PRINT_BADGE = dialog_card.findViewById(R.id.tv_PRINT_BADGE);

        ImageView iv_facility_image = dialog_card.findViewById(R.id.iv_facility_image);
        ImageView iv_user_image = dialog_card.findViewById(R.id.iv_user_image);


        tv_user_name.setText("" + preferences.getUser_Name());


        if (preferences.getMain_Type().equalsIgnoreCase("5")) {
            tv_user_family.setText("Community Staff");
            tv_type_pass.setText("Staff");
        } else if (preferences.getMain_Type().equalsIgnoreCase("1")) {
            tv_user_family.setText("Visiting Doctor");
            tv_type_pass.setText("Doctor");
        } else if (preferences.getMain_Type().equalsIgnoreCase("3")) {
            tv_user_family.setText("Volunteer");
            tv_type_pass.setText("Volunteer");
        } else if (preferences.getMain_Type().equalsIgnoreCase("2")) {
            tv_user_family.setText("Family");
            tv_type_pass.setText("Family Member");
        }


        tv_user_date.setText("" + Date_final);
        tv_user_time.setText("" + time_final);

        Glide.with(ctx)
                .load(decodedByte)
                .into(iv_user_image);

        Glide.with(ctx)
                .load(FACILITY_LOGO)
                .into(iv_facility_image);

        dialog_card.show();

        tv_PRINT_BADGE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Bitmap final_last_bitmap = utills.getBitmapFromView(ll_print_layout, ll_print_layout.getHeight(), ll_print_layout.getWidth());
                // File file_screenshort = utills.method_save_bitmap(final_last_bitmap);
                print_wifi(final_last_bitmap);
                // printButtonOnClick(file_screenshort);


                dialog_card.dismiss();
            }
        });


    }

    private void print_wifi(final Bitmap bitmap) {

        rl_main_print.setVisibility(View.GONE);
        ll_printing_process.setVisibility(View.VISIBLE);

        try {
            final Printer printer = new Printer();
            PrinterInfo settings = printer.getPrinterInfo();
            if (preferences.getPrinter_MODEL().equalsIgnoreCase("QL_820NWB")) {
                settings.printerModel = PrinterInfo.Model.QL_820NWB;
            } else if (preferences.getPrinter_MODEL().equalsIgnoreCase("QL_810W")) {
                settings.printerModel = PrinterInfo.Model.QL_810W;
            } else {
                settings.printerModel = PrinterInfo.Model.QL_810W;
            }
            if (Printer_Type.equalsIgnoreCase("2")) {
                settings.port = PrinterInfo.Port.USB;
            } else {
                settings.port = PrinterInfo.Port.NET;
            }
            settings.ipAddress = "" + mac_ip_address;
            settings.workPath = getFilesDir().getAbsolutePath();
            // Print Settings
            settings.labelNameIndex = LabelInfo.QL700.W62RB.ordinal();
            settings.printMode = PrinterInfo.PrintMode.FIT_TO_PAGE;
            settings.paperSize = PrinterInfo.PaperSize.CUSTOM;
            settings.isAutoCut = true;
            printer.setPrinterInfo(settings);


            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //Your code goes here
                        if (printer.startCommunication()) {
                            PrinterStatus result = printer.printImage(bitmap);
                            if (result.errorCode != PrinterInfo.ErrorCode.ERROR_NONE) {
                                Log.e("TAG", "ERROR - suc" + result.errorCode);
                                Toast.makeText(ctx, getString(R.string.error_message_none), Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(ctx, Print_complete_Activity.class);
                                startActivity(i);
                            } else {
                                showToast("ERROR - " + result);
                            }
                            printer.endCommunication();
                        } else {
                            PrinterStatus mPrintResult = printer.getPrinterStatus();
                            Log.e("TAG", "ERROR - " + mPrintResult.errorCode.toString());
                            showToast("ERROR - " + mPrintResult.errorCode.toString());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();


        } catch (Exception e) {
            showToast("Failed to connect");
        }

    }


    public void showToast(final String toast) {

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            public void run() {
                rl_main_print.setVisibility(View.VISIBLE);
                ll_printing_process.setVisibility(View.GONE);
                Toast.makeText(ctx, toast, Toast.LENGTH_SHORT).show();
            }
        });



    }


    @Override
    public void onBackPressed() {
        // super.onBackPressed();
    }
}
