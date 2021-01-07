package com.example.kiosk;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import com.brother.ptouch.sdk.LabelInfo;
import com.brother.ptouch.sdk.Printer;
import com.brother.ptouch.sdk.PrinterInfo;
import com.brother.ptouch.sdk.PrinterStatus;

import com.bumptech.glide.Glide;
import com.example.kiosk.API.GlobalServiceApi;
import com.example.kiosk.Utill.Preferences;
import com.example.kiosk.Utill.utills;





import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

public class SetupActivity extends AppCompatActivity {


    Context context;
    TextView tv_temp_label, tv_temp;
    ImageView iv_profile_image;


    String FACILITY_LOGO = "";
    String mac_ip_address = "";
    String Date_final = "";
    String USER_Name = "";
    Bitmap decodedByte;

    String Printer_Type = "1";
    String TIMEZONE = "";

    String device_id = "7c:25:da:1e:d3:ad";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        context = SetupActivity.this;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Preferences preferences = new Preferences(context);


        TIMEZONE = "" + preferences.getTIMEZONE();
        FACILITY_LOGO = "" + preferences.getFACILITY();
        mac_ip_address = "" + preferences.getWIFI_USB_PRINTER_id();
        Printer_Type = "" + preferences.getWIFI_USB_PRINTER_Type();
        device_id = "" + preferences.getPRE_DeviceMacAddress();


        tv_temp = findViewById(R.id.tv_temp);
        tv_temp_label = findViewById(R.id.tv_temp_label);
        iv_profile_image = findViewById(R.id.iv_profile_image);


        LinearLayout ll_Print_Badge = findViewById(R.id.ll_Print_Badge);
        ll_Print_Badge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                method_set_print();
            }
        });

        final ImageView iv_1 = findViewById(R.id.iv_1);
        final ImageView iv_2 = findViewById(R.id.iv_2);
        final ImageView iv_3 = findViewById(R.id.iv_3);


        iv_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_3.setColorFilter(getResources().getColor(R.color.green));
            }
        });

        iv_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_1.setColorFilter(getResources().getColor(R.color.green));
            }
        });


        iv_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_2.setColorFilter(getResources().getColor(R.color.green));
            }
        });


        TextView tv_click_here = findViewById(R.id.tv_click_here);
        tv_click_here.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                method_get();
            }
        });


    }

    Dialog progressDialog = null;

    private void method_get() {
        if (utills.isOnline(context)) {
            progressDialog = utills.startLoader(context);

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
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                                String code = jsonObject.getString("code");
                                JSONObject response = jsonObject.getJSONObject("response");

                                String name = response.getString("name");
                                String temperature = response.getString("temperature");
                                String temperature_label = response.getString("temperature_label");
                                String mask = response.getString("mask");
                                String timestamp = response.getString("timestamp");
                                String mac = response.getString("mac");
                                String image = response.getString("image");

                                tv_temp_label.setText("" + temperature_label);
                                tv_temp.setText("" + temperature + " F");


                                USER_Name = name;
                                String base64String = image;
                                String base64Image = base64String.split(",")[1];
                                byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                                decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                //  iv_profile_image.setImageBitmap(decodedByte);

                                Glide.with(context)
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

    private void method_set_print() {


        if (TIMEZONE.equalsIgnoreCase("Eastern Standard Time")) {
            Date_final = "" + formatDateToString("GMT-5");
        } else if (TIMEZONE.equalsIgnoreCase("Central Standard Time")) {
            Date_final = "" + formatDateToString("GMT-6");
        } else if (TIMEZONE.equalsIgnoreCase("Mountain Standard Time")) {
            Date_final = "" + formatDateToString("GMT-7");
        } else if (TIMEZONE.equalsIgnoreCase("Pacific Standard Time")) {
            Date_final = "" + formatDateToString("GMT-8");
        } else if (TIMEZONE.equalsIgnoreCase("Alaska Standard Time")) {
            Date_final = "" + formatDateToString("GMT-9");
        } else if (TIMEZONE.equalsIgnoreCase("Hawaii-Aleutian Standard Time")) {
            Date_final = "" + formatDateToString("GMT-10");
        } else {
            Date_final = "";
        }


        final Dialog dialog_card = new Dialog(context);
        dialog_card.setContentView(R.layout.dialog_final_print);
        dialog_card.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Window window = dialog_card.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        final LinearLayout ll_print_layout = dialog_card.findViewById(R.id.ll_print_layout);
        TextView tv_user_name = dialog_card.findViewById(R.id.tv_user_name);
        TextView tv_user_date = dialog_card.findViewById(R.id.tv_user_date);
        TextView tv_user_family = dialog_card.findViewById(R.id.tv_user_family);
        TextView tv_PRINT_BADGE = dialog_card.findViewById(R.id.tv_PRINT_BADGE);

        ImageView iv_facility_image = dialog_card.findViewById(R.id.iv_facility_image);
        ImageView iv_user_image = dialog_card.findViewById(R.id.iv_user_image);


        if (USER_Name.equalsIgnoreCase("") || USER_Name.equalsIgnoreCase("null")) {
            String[] Textlist = {"Samir Landry", "Finbar Hammond", "Dominik Jackson"
                    , "Ebrahim Kaur", "Louisa Moore", "Kimora Meza"
                    , "Amalie Romero", "Marvin Dodd", "Hayleigh Reyna"};
            String randomText = Textlist[new Random().nextInt(Textlist.length)];
            tv_user_name.setText(randomText);
        } else {
            tv_user_name.setText("NAME: " + USER_Name);
        }

        tv_user_date.setText("" + Date_final);

        Glide.with(context)
                .load(decodedByte)
                .into(iv_user_image);

        Glide.with(context)
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

        try {


            final Printer printer = new Printer();
            PrinterInfo settings = printer.getPrinterInfo();
            settings.printerModel = PrinterInfo.Model.QL_810W;

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

            // Connect, then print
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (printer.startCommunication()) {
                        PrinterStatus result = printer.printImage(bitmap);
                        if (result.errorCode != PrinterInfo.ErrorCode.ERROR_NONE) {
                            Log.d("TAG", "ERROR - " + result.errorCode);
                            Toast.makeText(context, "ERROR - " + result.errorCode, Toast.LENGTH_SHORT).show();
                        }
                        printer.endCommunication();
                    }/*else{
                    Toast.makeText(context,"Failed to connect",Toast.LENGTH_SHORT).show();
                }*/
                }
            }).start();
        } catch (Exception e) {
            Toast.makeText(context, "Failed to connect", Toast.LENGTH_SHORT).show();
        }

    }


//
//    BluetoothAdapter getBluetoothAdapter() {
//        final BluetoothAdapter bluetoothAdapter = BluetoothAdapter
//                .getDefaultAdapter();
//        if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled()) {
//            final Intent enableBtIntent = new Intent(
//                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            enableBtIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(enableBtIntent);
//        }
//        return bluetoothAdapter;
//    }
//
//    public void printButtonOnClick(File file_screenshort) {
//
//
//        BasePrint myPrint = null;
//        MsgHandle mHandle;
//        MsgDialog mDialog;
//
//
//        mDialog = new MsgDialog(this);
//        mHandle = new MsgHandle(this, mDialog);
//        myPrint = new ImagePrint(this, mHandle, mDialog);
//
//        // when use bluetooth print set the adapter
//        BluetoothAdapter bluetoothAdapter = getBluetoothAdapter();
//        myPrint.setBluetoothAdapter(bluetoothAdapter);
//
//        // set the printing data
//        ArrayList<String> mFiles = new ArrayList<String>();
//        mFiles.add("" + file_screenshort.getAbsolutePath());
//        ((ImagePrint) myPrint).setFiles(mFiles);
//
//        if (!checkUSB())
//            return;
//
//        // call function to print
//        myPrint.print();
//    }
//
//    @TargetApi(12)
//    boolean checkUSB() {
//        if (myPrint.getPrinterInfo().port != PrinterInfo.Port.USB) {
//            return true;
//        }
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
//            Message msg = mHandle.obtainMessage(Common.MSG_WRONG_OS);
//            mHandle.sendMessage(msg);
//            return false;
//        }
//        UsbManager usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
//        UsbDevice usbDevice = myPrint.getUsbDevice(usbManager);
//        if (usbDevice == null) {
//            Message msg = mHandle.obtainMessage(Common.MSG_NO_USB);
//            mHandle.sendMessage(msg);
//            return false;
//        }
//        PendingIntent permissionIntent = PendingIntent.getBroadcast(this, 0,
//                new Intent(ACTION_USB_PERMISSION), 0);
//        registerReceiver(mUsbReceiver, new IntentFilter(ACTION_USB_PERMISSION));
//        if (!usbManager.hasPermission(usbDevice)) {
//            Common.mUsbRequest = 0;
//            usbManager.requestPermission(usbDevice, permissionIntent);
//        } else {
//            Common.mUsbRequest = 1;
//        }
//        return true;
//    }
//
//    static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
//    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
//        @TargetApi(12)
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            if (ACTION_USB_PERMISSION.equals(action)) {
//                synchronized (this) {
//                    if (intent.getBooleanExtra(
//                            UsbManager.EXTRA_PERMISSION_GRANTED, false))
//                        Common.mUsbRequest = 1;
//                    else
//                        Common.mUsbRequest = 2;
//                }
//            }
//        }
//    };
//

    public String formatDateToString(String timeZone) {
        Date date = new Date();
        SimpleDateFormat DATEE_sdf = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat TIMEE_sdf = new SimpleDateFormat("HH:mm");
        if (timeZone == null || "".equalsIgnoreCase(timeZone.trim())) {
            timeZone = Calendar.getInstance().getTimeZone().getID();
        }
        DATEE_sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
        TIMEE_sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
        String datee = DATEE_sdf.format(date);
        String timee = TIMEE_sdf.format(date);
        return "" + "DATE: " + datee + " TIME: " + timee;
    }

}