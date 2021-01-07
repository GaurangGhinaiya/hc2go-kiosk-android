package com.example.kiosk.Project.Resident;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kiosk.Model.QuestionsAnswer.Model_QA_CovidQuestion;
import com.example.kiosk.Project.CS.CS_Print_Activity;
import com.example.kiosk.R;
import com.example.kiosk.Utill.NonScrollListView;
import com.example.kiosk.Utill.Preferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class R_FinalAnswer_Activity extends AppCompatActivity {


    Context ctx;
    Preferences preferences;

    List<Model_QA_CovidQuestion> list_services_temp_move = new ArrayList<>();
    NonScrollListView Rv_ActivePatient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_answer);
        ctx = R_FinalAnswer_Activity.this;
        preferences = new Preferences(ctx);


        Bundle extras = getIntent().getExtras();
        String arraylist_a = extras.getString("arraylist_QA");
        Type listType = new TypeToken<List<Model_QA_CovidQuestion>>() {
        }.getType();


        list_services_temp_move = new ArrayList<>();
        list_services_temp_move = new Gson().fromJson(arraylist_a, listType);

        Rv_ActivePatient = findViewById(R.id.Rv_ActivePatient);

        LinearLayout ll_Changes = findViewById(R.id.ll_Changes);
        ll_Changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        LinearLayout ll_Confirm = findViewById(R.id.ll_Confirm);
        ll_Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ctx, R_OnlySignIN_Activity.class);
                Bundle bundle = new Bundle();
                bundle.putString("arraylist_QA", new Gson().toJson(list_services_temp_move));
                i.putExtras(bundle);
                startActivity(i);
            }
        });


        Adapter_Booking customAdapter_new = new Adapter_Booking(ctx, list_services_temp_move);
        Rv_ActivePatient.setAdapter(customAdapter_new);


    }

    public class Adapter_Booking extends BaseAdapter {

        Context context;
        List<Model_QA_CovidQuestion> listState;
        LayoutInflater inflater;


        public Adapter_Booking(Context applicationContext, List<Model_QA_CovidQuestion> spinnerArray) {
            this.context = applicationContext;
            this.listState = spinnerArray;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public int getCount() {
            return listState.size();
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
            ViewHolder viewHolder = null;
            if (view == null) {
                viewHolder = new ViewHolder();
                view = inflater.inflate(R.layout.datalist_answer_list, viewGroup, false);
                viewHolder.tv_que_name = view.findViewById(R.id.tv_que_name);
                viewHolder.tv_answer_your = view.findViewById(R.id.tv_answer_your);
                viewHolder.tv_que_no = view.findViewById(R.id.tv_que_no);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }


            viewHolder.tv_que_no.setText("" + (position + 1));
            viewHolder.tv_que_name.setText("" + listState.get(position).getPlainQuestion());

            String ans = "";

            if (listState.get(position).answer.equalsIgnoreCase("0")) {
                ans = "NO";
            } else if (listState.get(position).answer.equalsIgnoreCase("1")) {
                ans = "YES";
            }

            viewHolder.tv_answer_your.setText("" + ans);


            return view;
        }

        private class ViewHolder {
            TextView tv_que_name;
            TextView tv_answer_your;
            TextView tv_que_no;
        }

    }


}
