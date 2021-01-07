package com.example.kiosk.Project.FG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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

import com.example.kiosk.Model.RoomEmpart_Details.Model_RoomEmpartDetails_Data;
import com.example.kiosk.R;
import com.example.kiosk.Utill.Preferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class FG_PatientList_Activity extends AppCompatActivity {


    Context ctx;

    Preferences preferences;

    List<Model_RoomEmpartDetails_Data> agency_data_list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_list);
        ctx = FG_PatientList_Activity.this;
        FG_PatientList_activity = FG_PatientList_Activity.this;
        preferences = new Preferences(ctx);

        Bundle extras = getIntent().getExtras();
        String arraylist_a = extras.getString("arraylist_member");
        Type listType = new TypeToken<List<Model_RoomEmpartDetails_Data>>() {
        }.getType();


        agency_data_list = new ArrayList<>();
        agency_data_list = new Gson().fromJson(arraylist_a, listType);


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

                List<Model_RoomEmpartDetails_Data> list_services_test = new ArrayList<>();
                for (int j = 0; j < agency_data_list.size(); j++) {
                    if (agency_data_list.get(j).is_select_patient == 1) {
                        list_services_test.add(agency_data_list.get(j));
                    }
                }

                if (list_services_test.size() > 0) {
                    Intent i = new Intent(ctx, FG_VisitElse_Activity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("arraylist_member", new Gson().toJson(list_services_test));
                    i.putExtras(bundle);
                    startActivity(i);
                } else {
                    Toast.makeText(ctx, "Please select any one Resident", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    class Adapter_Image extends RecyclerView.Adapter<Adapter_Image.MyViewHolder> {
        private List<Model_RoomEmpartDetails_Data> arrayList;
        Context mcontext;

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
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (arrayList.get(position).is_select_patient == 0) {
                        arrayList.get(position).is_select_patient = 1;
                        holder.ll_selected_layout.setBackground(getResources().getDrawable(R.drawable.bg_trans_white));
                    } else {
                        arrayList.get(position).is_select_patient = 0;
                        holder.ll_selected_layout.setBackground(getResources().getDrawable(R.drawable.bg_trans_blue));

                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

    }

    public static Activity FG_PatientList_activity;

    public static void method_finish() {
        if (FG_PatientList_activity != null) {
            FG_PatientList_activity.finish();
        }
    }


}
