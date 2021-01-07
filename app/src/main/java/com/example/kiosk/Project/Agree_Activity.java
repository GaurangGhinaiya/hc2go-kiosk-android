package com.example.kiosk.Project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.kiosk.R;
import com.example.kiosk.Utill.Preferences;


public class Agree_Activity extends AppCompatActivity {


    Context ctx;
    Preferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agree);
        ctx = Agree_Activity.this;
        preferences = new Preferences(ctx);

        ImageView iv_company_logo = findViewById(R.id.iv_company_logo);
        String FACILITY_LOGO = "" + preferences.getFACILITY();
        Glide.with(ctx)
                .load(FACILITY_LOGO)
                .into(iv_company_logo);

        LinearLayout ll_Agree = findViewById(R.id.ll_Agree);
        ll_Agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ctx, SignIn_Type_Activity.class);
                i.putExtra("sign_in_out",false);
                startActivity(i);
            }
        });




    }

}
