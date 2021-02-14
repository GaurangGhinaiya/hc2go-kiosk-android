package com.example.kiosk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.brother.ptouch.sdk.Printer;
import com.brother.ptouch.sdk.PrinterInfo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.kiosk.API.GlobalServiceApi;
import com.example.kiosk.Model.Model_Facility_Data;
import com.example.kiosk.Model.Model_Facility_Response;
import com.example.kiosk.Model.RoomEmpart_Details.Model_RoomEmpartDetails_Data;
import com.example.kiosk.Project.SignInOut_Activity;
import com.example.kiosk.Utill.Preferences;
import com.example.kiosk.Utill.utills;
import com.example.kiosk.common.Common;
import com.example.kiosk.printprocess.PrinterModelInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.example.kiosk.API.GlobalServiceApi.Image_url;

public class MainActivity extends AppCompatActivity {

    TextView tv_timezone;
    TextView tv_SELECT_FACILITY;
    TextView tv_SELECT_Printer;
    List<String> Language_data_namelist = new ArrayList<>();
    List<Model_Facility_Data> agency_data_list = new ArrayList<>();

    String FACILITY_LOGO = "";
    public static String mac_ip_address123 = "";
    String TIMEZONE = "";
    String Printer_MODEL = "";

    String Printer_Type = "1";

    Preferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        context = MainActivity.this;
        preferences = new Preferences(context);

        method_get_agency_list();

        agency_data_list = new ArrayList<>();
        Language_data_namelist = new ArrayList<>();
        Language_data_namelist.add("Eastern Standard Time");
        Language_data_namelist.add("Central Standard Time");
        Language_data_namelist.add("Mountain Standard Time");
        Language_data_namelist.add("Pacific Standard Time");
        Language_data_namelist.add("Alaska Standard Time");
        Language_data_namelist.add("Hawaii-Aleutian Standard Time");


        tv_timezone = findViewById(R.id.tv_timezone);
        tv_SELECT_FACILITY = findViewById(R.id.tv_SELECT_FACILITY);
        tv_SELECT_Printer = findViewById(R.id.tv_SELECT_Printer);
        LinearLayout tv_select_timezone = findViewById(R.id.tv_select_timezone);
        LinearLayout ll_facility = findViewById(R.id.ll_facility);

        final EditText et_device_mac_address = findViewById(R.id.et_device_mac_address);
        final EditText et_devicename = findViewById(R.id.et_devicename);

        TextView tv_START_SETUP = findViewById(R.id.tv_START_SETUP);
        tv_START_SETUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mac_ip_address123.equalsIgnoreCase("")) {
                    Toast.makeText(context, "Please Browse your printer mac-address", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TIMEZONE.equalsIgnoreCase("")) {
                    Toast.makeText(context, "Please select your timezone", Toast.LENGTH_SHORT).show();
                    return;
                } else if (FACILITY_LOGO.equalsIgnoreCase("")) {
                    Toast.makeText(context, "Please select facility", Toast.LENGTH_SHORT).show();
                    return;
                } else if (Printer_MODEL.equalsIgnoreCase("")) {
                    Toast.makeText(context, "Please select Printer Model", Toast.LENGTH_SHORT).show();
                    return;
                } else if (et_device_mac_address.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(context, "Please enter device mac-address", Toast.LENGTH_SHORT).show();
                    return;
                } else {

                    preferences.setWIFI_USB_PRINTER_id("" + mac_ip_address123);
                    preferences.setWIFI_USB_PRINTER_Type("" + Printer_Type);

                    preferences.setFACILITY("" + FACILITY_LOGO);
                    preferences.setTIMEZONE("" + TIMEZONE);
                    preferences.setPrinter_MODEL("" + Printer_MODEL);

                    preferences.setPRE_DeviceMacAddress("" + et_device_mac_address.getText().toString());
                    preferences.setPRE_DeviceName("" + et_devicename.getText().toString());


                    method_register("" + et_device_mac_address.getText().toString(), "" + et_devicename.getText().toString());


                }
            }
        });

        tv_select_timezone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                method_dialog_language();
            }
        });

        ll_facility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                method_dialog_Facility();
            }
        });

        LinearLayout ll_Printer = findViewById(R.id.ll_Printer);
        ll_Printer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                method_dialog_Printer();
            }
        });

        //  setPreferences();

        TextView tv_USB_BASED_PRINTER = findViewById(R.id.tv_USB_BASED_PRINTER);
        tv_USB_BASED_PRINTER.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Printer_Type = "2";
                final Dialog dialog_usb = new Dialog(context);
                dialog_usb.setContentView(R.layout.dialog_usb);
                dialog_usb.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                Window window = dialog_usb.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                LinearLayout card_save = dialog_usb.findViewById(R.id.card_save);
                final EditText et_Printer_mac_address = dialog_usb.findViewById(R.id.et_Printer_mac_address);
                card_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog_usb.dismiss();
                        mac_ip_address123 = "" + et_Printer_mac_address.getText().toString();
                    }
                });

                dialog_usb.show();

            }
        });

        TextView tv_BROWSE_PRINTER = findViewById(R.id.tv_BROWSE_PRINTER);
        tv_BROWSE_PRINTER.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Printer_Type = "1";
                Intent printerList = new Intent(context, Activity_NetPrinterList.class);
                printerList.putExtra("modelName", "");
                startActivityForResult(printerList, Common.PRINTER_SEARCH);
            }
        });


    }

    Dialog dialog_county;

    private void method_dialog_language() {

        dialog_county = new Dialog(this);
        dialog_county.setContentView(R.layout.dialog_languge);
        dialog_county.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        Window window = dialog_county.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


        LinearLayout ll_cancel = dialog_county.findViewById(R.id.ll_cancel);
        ll_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_county.dismiss();
            }
        });


        final ListView Listview_language = dialog_county.findViewById(R.id.Listview_language);
        Listview_language.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        Listview_language.setFastScrollEnabled(false);

        CustomAdapter_Language customAdapter_new = new CustomAdapter_Language(this, Language_data_namelist);
        Listview_language.setAdapter(customAdapter_new);

        dialog_county.show();
    }

    class CustomAdapter_Language extends BaseAdapter {


        Context context;
        List<String> spinnerArray;
        LayoutInflater inflter;

        public CustomAdapter_Language(Context applicationContext, List<String> spinnerArray) {
            this.context = applicationContext;

            this.spinnerArray = spinnerArray;
            inflter = (LayoutInflater.from(applicationContext));
        }


        @Override
        public int getCount() {
            return spinnerArray.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int position, View view, ViewGroup viewGroup) {
            view = inflter.inflate(R.layout.spinner_item_lang, null);


            final TextView text_lang = (TextView) view.findViewById(R.id.text_lang);
            final LinearLayout ll_chechk_lang = (LinearLayout) view.findViewById(R.id.ll_chechk_lang);


            text_lang.setText("" + spinnerArray.get(position));

            ll_chechk_lang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tv_timezone.setText("" + spinnerArray.get(position));
                    TIMEZONE = "" + spinnerArray.get(position);
                    if (dialog_county != null) {
                        dialog_county.dismiss();
                    }
                }
            });


            return view;
        }
    }


    private final static int PERMISSION_WRITE_EXTERNAL_STORAGE = 10001;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onStart() {
        super.onStart();

    }


    private boolean isPermitWriteStorage() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


    Context context;
    Dialog progressDialog = null;

    private void method_get_agency_list() {
        if (utills.isOnline(context)) {
            progressDialog = utills.startLoader(context);
            AndroidNetworking.get(GlobalServiceApi.API_get_company_branches)
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

                                    Model_Facility_Response agency_responce = new Gson().fromJson(result, Model_Facility_Response.class);

                                    if (agency_responce.getData() != null) {
                                        agency_data_list = new ArrayList<>();
                                        agency_data_list = agency_responce.getData();
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

    Dialog Dialog_Printer;

    private void method_dialog_Printer() {

        Dialog_Printer = new Dialog(this);
        Dialog_Printer.setContentView(R.layout.dialog_languge);
        Dialog_Printer.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        Window window = Dialog_Printer.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


        TextView tv_name_dialog = Dialog_Printer.findViewById(R.id.tv_name_dialog);
        tv_name_dialog.setText("SELECT PRINTER");
        LinearLayout ll_cancel = Dialog_Printer.findViewById(R.id.ll_cancel);
        ll_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog_Printer.dismiss();
            }
        });


        final ListView Listview_language = Dialog_Printer.findViewById(R.id.Listview_language);
        Listview_language.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        Listview_language.setFastScrollEnabled(false);

        List<String> print_data_namelist = new ArrayList<>();
        print_data_namelist = new ArrayList<>();
        print_data_namelist.add("QL_820NWB");
        print_data_namelist.add("QL_810W");
        CustomAdapter_Printer customAdapter_new = new CustomAdapter_Printer(this, print_data_namelist);
        Listview_language.setAdapter(customAdapter_new);

        Dialog_Printer.show();
    }


    class CustomAdapter_Printer extends BaseAdapter {

        Context context;
        List<String> spinnerArray;
        LayoutInflater inflter;

        public CustomAdapter_Printer(Context applicationContext, List<String> spinnerArray) {
            this.context = applicationContext;

            this.spinnerArray = spinnerArray;
            inflter = (LayoutInflater.from(applicationContext));
        }


        @Override
        public int getCount() {
            return spinnerArray.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int position, View view, ViewGroup viewGroup) {

            view = inflter.inflate(R.layout.spinner_item_lang, null);


            final TextView text_lang = (TextView) view.findViewById(R.id.text_lang);
            final LinearLayout ll_chechk_lang = (LinearLayout) view.findViewById(R.id.ll_chechk_lang);


            text_lang.setText("" + spinnerArray.get(position));

            ll_chechk_lang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tv_SELECT_Printer.setText("" + spinnerArray.get(position));
                    Printer_MODEL = "" + spinnerArray.get(position);
                    if (Dialog_Printer != null) {
                        Dialog_Printer.dismiss();
                    }
                }
            });


            return view;
        }
    }


    Dialog Dialog_Facility;

    private void method_dialog_Facility() {

        Dialog_Facility = new Dialog(this);
        Dialog_Facility.setContentView(R.layout.dialog_languge);
        Dialog_Facility.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        Window window = Dialog_Facility.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


        TextView tv_name_dialog = Dialog_Facility.findViewById(R.id.tv_name_dialog);
        tv_name_dialog.setText("SELECT FACILITY");
        LinearLayout ll_cancel = Dialog_Facility.findViewById(R.id.ll_cancel);
        ll_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog_Facility.dismiss();
            }
        });


        final ListView Listview_language = Dialog_Facility.findViewById(R.id.Listview_language);
        Listview_language.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        Listview_language.setFastScrollEnabled(false);

        CustomAdapterFacility customAdapter_new = new CustomAdapterFacility(this, agency_data_list);
        Listview_language.setAdapter(customAdapter_new);

        Dialog_Facility.show();
    }

    class CustomAdapterFacility extends BaseAdapter {


        Context context;
        List<Model_Facility_Data> spinnerArray;
        LayoutInflater inflter;

        public CustomAdapterFacility(Context applicationContext, List<Model_Facility_Data> spinnerArray) {
            this.context = applicationContext;

            this.spinnerArray = spinnerArray;
            inflter = (LayoutInflater.from(applicationContext));
        }


        @Override
        public int getCount() {
            return spinnerArray.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int position, View view, ViewGroup viewGroup) {
            view = inflter.inflate(R.layout.spinner_item_logo, null);


            final TextView text_lang = (TextView) view.findViewById(R.id.text_lang);
            final ImageView iv_agency = (ImageView) view.findViewById(R.id.iv_agency);
            final LinearLayout ll_chechk_lang = (LinearLayout) view.findViewById(R.id.ll_chechk_lang);


            if (spinnerArray.get(position).getcompany() != null) {
                if (spinnerArray.get(position).getcompany().getprofile_pic() != null) {
                    Glide.with(context)
                            .load(Image_url + spinnerArray.get(position).getcompany().getprofile_pic())
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(iv_agency);

                }

                text_lang.setText("" + spinnerArray.get(position).getcompany().getname() + " - " + spinnerArray.get(position).getcode());

            }


            ll_chechk_lang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tv_SELECT_FACILITY.setText("" + spinnerArray.get(position).getcompany().getname() + " - " + spinnerArray.get(position).getcode());
                    FACILITY_LOGO = "" + Image_url + "" + spinnerArray.get(position).getcompany().getprofile_pic();
                    if (Dialog_Facility != null) {
                        Dialog_Facility.dismiss();
                    }

                    preferences.setCompany_id("" + spinnerArray.get(position).getcompany_id());
                    preferences.setBranchid("" + spinnerArray.get(position).getId());
                }
            });


            return view;
        }
    }


//    private void setPreferences() {
//        SharedPreferences sharedPreferences = PreferenceManager
//                .getDefaultSharedPreferences(this);
//        // initialization for print
//        Printer printer = new Printer();
//        PrinterInfo printerInfo = printer.getPrinterInfo();
//        if (printerInfo == null) {
//            printerInfo = new PrinterInfo();
//            printer.setPrinterInfo(printerInfo);
//
//        }
//        if (sharedPreferences.getString("printerModel", "").equals("")) {
//            String printerModel = printerInfo.printerModel.toString();
//            PrinterModelInfo.Model model = PrinterModelInfo.Model.valueOf(printerModel);
//
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putString("printerModel", printerModel);
//            editor.putString("port", printerInfo.port.toString());
//            editor.putString("address", printerInfo.ipAddress);
//            editor.putString("macAddress", printerInfo.macAddress);
//            editor.putString("localName", printerInfo.getLocalName());
//
//            // Override SDK default paper size
//            editor.putString("paperSize", model.getDefaultPaperSize());
//
//            editor.putString("orientation", printerInfo.orientation.toString());
//            editor.putString("numberOfCopies",
//                    Integer.toString(printerInfo.numberOfCopies));
//            editor.putString("halftone", printerInfo.halftone.toString());
//            editor.putString("printMode", printerInfo.printMode.toString());
//            editor.putString("pjCarbon", Boolean.toString(printerInfo.pjCarbon));
//            editor.putString("pjDensity",
//                    Integer.toString(printerInfo.pjDensity));
//            editor.putString("pjFeedMode", printerInfo.pjFeedMode.toString());
//            editor.putString("align", printerInfo.align.toString());
//            editor.putString("leftMargin",
//                    Integer.toString(printerInfo.margin.left));
//            editor.putString("valign", printerInfo.valign.toString());
//            editor.putString("topMargin",
//                    Integer.toString(printerInfo.margin.top));
//            editor.putString("customPaperWidth",
//                    Integer.toString(printerInfo.customPaperWidth));
//            editor.putString("customPaperLength",
//                    Integer.toString(printerInfo.customPaperLength));
//            editor.putString("customFeed",
//                    Integer.toString(printerInfo.customFeed));
//            editor.putString("paperPosition",
//                    printerInfo.paperPosition.toString());
//            editor.putString("customSetting",
//                    sharedPreferences.getString("customSetting", ""));
//            editor.putString("rjDensity",
//                    Integer.toString(printerInfo.rjDensity));
//            editor.putString("rotate180",
//                    Boolean.toString(printerInfo.rotate180));
//            editor.putString("dashLine", Boolean.toString(printerInfo.dashLine));
//
//            editor.putString("peelMode", Boolean.toString(printerInfo.peelMode));
//            editor.putString("mode9", Boolean.toString(printerInfo.mode9));
//            editor.putString("pjSpeed", Integer.toString(printerInfo.pjSpeed));
//            editor.putString("pjPaperKind", printerInfo.pjPaperKind.toString());
//            editor.putString("printerCase",
//                    printerInfo.rollPrinterCase.toString());
//            editor.putString("printQuality", printerInfo.printQuality.toString());
//            editor.putString("skipStatusCheck",
//                    Boolean.toString(printerInfo.skipStatusCheck));
//            editor.putString("checkPrintEnd", printerInfo.checkPrintEnd.toString());
//            editor.putString("imageThresholding",
//                    Integer.toString(printerInfo.thresholdingValue));
//            editor.putString("scaleValue",
//                    Double.toString(printerInfo.scaleValue));
//            editor.putString("trimTapeAfterData",
//                    Boolean.toString(printerInfo.trimTapeAfterData));
//            editor.putString("enabledTethering",
//                    Boolean.toString(printerInfo.enabledTethering));
//
//
//            editor.putString("processTimeout",
//                    Integer.toString(printerInfo.timeout.processTimeoutSec));
//            editor.putString("sendTimeout",
//                    Integer.toString(printerInfo.timeout.sendTimeoutSec));
//            editor.putString("receiveTimeout",
//                    Integer.toString(printerInfo.timeout.receiveTimeoutSec));
//            editor.putString("connectionTimeout",
//                    Integer.toString(printerInfo.timeout.connectionWaitMSec));
//            editor.putString("closeWaitTime",
//                    Integer.toString(printerInfo.timeout.closeWaitDisusingStatusCheckSec));
//
//            editor.putString("overwrite",
//                    Boolean.toString(printerInfo.overwrite));
//            editor.putString("savePrnPath", printerInfo.savePrnPath);
//            editor.putString("softFocusing",
//                    Boolean.toString(printerInfo.softFocusing));
//            editor.putString("rawMode",
//                    Boolean.toString(printerInfo.rawMode));
//
//
//            editor.putString("workPath", printerInfo.workPath);
//            editor.putString("useLegacyHalftoneEngine",
//                    Boolean.toString(printerInfo.useLegacyHalftoneEngine));
//            editor.apply();
//        }
//
//    }


    private void method_register(String et_device_mac_address, String et_devicename) {
        if (utills.isOnline(context)) {
            progressDialog = utills.startLoader(context);

            AndroidNetworking.post(GlobalServiceApi.API_add_branch_wise_device)
                    .addBodyParameter("branch_id", "" + preferences.getBranchid())
                    .addBodyParameter("company_id", "" + preferences.getCompany_id())
                    .addBodyParameter("device_mac_address", "" + et_device_mac_address)
                    .addBodyParameter("device_name", "" + et_devicename)
                    .addBodyParameter("printer_name", "" + preferences.getPrinter_MODEL())
                    .addBodyParameter("timezone", "" + TIMEZONE)
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

                                    JSONObject jsondata = jsonObject.getJSONObject("data");
                                    String Unique_id = "" + jsondata.getString("id");
                                    preferences.setUnique_id("" +Unique_id);

                                    JSONObject jsoncompany = jsondata.getJSONObject("company");
                                    String profile_pic = "" + jsoncompany.getString("profile_pic");
                                    preferences.setFACILITY( "" + Image_url + "" + profile_pic);

                                    Intent i = new Intent(MainActivity.this, SignInOut_Activity.class);
                                    startActivity(i);
                                    finish();

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