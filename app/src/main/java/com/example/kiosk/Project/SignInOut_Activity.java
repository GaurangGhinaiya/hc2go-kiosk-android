package com.example.kiosk.Project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.bumptech.glide.Glide;
import com.example.kiosk.API.GlobalServiceApi;
import com.example.kiosk.MainActivity;
import com.example.kiosk.Project.CS.CS_signout_Activity;
import com.example.kiosk.Project.Resident.Resident_Signout_No_Activity;
import com.example.kiosk.R;
import com.example.kiosk.Splash_Activity;
import com.example.kiosk.Utill.Preferences;
import com.example.kiosk.Utill.utills;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.kiosk.API.GlobalServiceApi.Image_url;


public class SignInOut_Activity extends AppCompatActivity {


    Context ctx;
    Preferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_out);
        ctx = SignInOut_Activity.this;
        preferences = new Preferences(ctx);

        preferences.setpatient_list("");
        preferences.setMain_Type("");
        preferences.setUser_Id("");
        preferences.setUser_Name("");
        preferences.setUser_company("");
        preferences.setUser_phone("");
        preferences.setfamily_type("");
        preferences.setvehicle_model("");
        preferences.setvehicle_color("");
        preferences.setvehicle_plate("");
        preferences.setHomecarePassQList("");
        preferences.setHomecarePassANSList("");
        preferences.setIS_Homecare_Pass("");

        Intent intent_get = getIntent();
        if (intent_get != null) {
            boolean is_next_type = intent_get.getBooleanExtra("is_next_type", false);
            if (is_next_type) {
                Intent i = new Intent(ctx, SignIn_Type_Activity.class);
                i.putExtra("sign_in_out", false);
                startActivity(i);
            }
        }



        ImageView iv_company_logo = findViewById(R.id.iv_company_logo);
        String FACILITY_LOGO = "" + preferences.getFACILITY();
        Glide.with(ctx)
                .load(FACILITY_LOGO)
                .into(iv_company_logo);


        LinearLayout ll_sign_in = findViewById(R.id.ll_sign_in);
        ll_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Intent i = new Intent(ctx, SignOut_complete_Activity.class);
               Intent i = new Intent(ctx, Agree_Activity.class);
                startActivity(i);
            }
        });


        LinearLayout ll_sign_out = findViewById(R.id.ll_sign_out);
        ll_sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(ctx, SignIn_Type_Activity.class);
                i.putExtra("sign_in_out", true);
                startActivity(i);

            }
        });


        method_getdata();

    }



    private void method_getdata() {
        if (utills.isOnline(ctx)) {

            AndroidNetworking.get(GlobalServiceApi.API_get_branch_wise_device+""+preferences.getUnique_id())
                    .build()
                    .getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String result) {
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


                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            Log.d("API", anError.toString());
                        }
                    });


        }
    }


}
