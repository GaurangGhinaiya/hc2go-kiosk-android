package com.example.kiosk.Project.Resident;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.example.kiosk.API.GlobalServiceApi;
import com.example.kiosk.Model.RoomEmpart_Details.Model_RoomEmpartDetails_Data;
import com.example.kiosk.Project.SignOut_complete_Activity;
import com.example.kiosk.R;
import com.example.kiosk.Utill.Preferences;
import com.example.kiosk.Utill.utills;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class Resident_List_Signout_Activity extends AppCompatActivity {


    Context ctx;

    Preferences preferences;

    List<Model_RoomEmpartDetails_Data> agency_data_list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_list);
        ctx = Resident_List_Signout_Activity.this;


        preferences = new Preferences(ctx);
        preferences.setUser_Id("");
        Bundle extras = getIntent().getExtras();
        String arraylist_a = extras.getString("arraylist_member");
        Type listType = new TypeToken<List<Model_RoomEmpartDetails_Data>>() {
        }.getType();


        agency_data_list = new ArrayList<>();
        agency_data_list = new Gson().fromJson(arraylist_a, listType);


        TextView tv_next = findViewById(R.id.tv_next);
        TextView tv_title_name = findViewById(R.id.tv_title_name);
        tv_title_name.setText("Select Resident");
        tv_next.setText("Sign Out");


        RecyclerView rv_patientlist = findViewById(R.id.rv_patientlist);
        rv_patientlist.setLayoutManager(new LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL, false));

        Adapter_Image adapter_image = new Adapter_Image(ctx, agency_data_list);
        rv_patientlist.setAdapter(adapter_image);


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

                if (preferences.getUser_Id().equalsIgnoreCase("")) {
                    Toast.makeText(ctx, "Please select any one Resident", Toast.LENGTH_SHORT).show();
                } else {

                    method_check_staff_employee_code();

                }
            }
        });


    }

    class Adapter_Image extends RecyclerView.Adapter<Adapter_Image.MyViewHolder> {
        private List<Model_RoomEmpartDetails_Data> arrayList;
        Context mcontext;
        int pos_selected = -1;

        public Adapter_Image(Context context, List<Model_RoomEmpartDetails_Data> arrayList) {
            this.arrayList = arrayList;
            mcontext = context;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tv_patient_name;
            LinearLayout ll_selected_layout;

            public MyViewHolder(View view) {
                super(view);
                tv_patient_name = view.findViewById(R.id.tv_patient_name);
                ll_selected_layout = view.findViewById(R.id.ll_selected_layout);
            }
        }


        @Override
        public MyViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
            final View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cell_patient_list, parent, false);

            return new MyViewHolder(itemView);
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            holder.tv_patient_name.setText("" + arrayList.get(position).getName());

            if (pos_selected == position) {
                holder.ll_selected_layout.setBackground(getResources().getDrawable(R.drawable.bg_trans_white));
            } else {
                holder.ll_selected_layout.setBackground(getResources().getDrawable(R.drawable.bg_trans_blue));
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pos_selected = position;
                    holder.ll_selected_layout.setBackground(getResources().getDrawable(R.drawable.bg_trans_white));
                    preferences.setUser_Id("" + arrayList.get(position).getId());
                    notifyDataSetChanged();
                }
            });

        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

    }

    Dialog progressDialog = null;

    private void method_check_staff_employee_code() {
        if (utills.isOnline(ctx)) {
            String gmtTime = "" + utills.formatDate_currenttime();

            progressDialog = utills.startLoader(ctx);
            AndroidNetworking.post(GlobalServiceApi.API_resident_signout)
                    .addBodyParameter("branch_id", "" + preferences.getBranchid())
                    .addBodyParameter("company_id", "" + preferences.getCompany_id())
                    .addBodyParameter("user_id", "" + preferences.getUser_Id())
                    .addBodyParameter("signout", "" + gmtTime)
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

                                    Intent i = new Intent(ctx, SignOut_complete_Activity.class);
                                    startActivity(i);

                                }

                                Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
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
