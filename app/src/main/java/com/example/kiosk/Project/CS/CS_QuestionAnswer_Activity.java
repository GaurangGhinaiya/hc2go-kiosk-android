package com.example.kiosk.Project.CS;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kiosk.Model.QuestionsAnswer.Model_QA_CovidQuestion;

import com.example.kiosk.Project.SignInOut_Activity;

import com.example.kiosk.R;
import com.example.kiosk.Utill.MyTagHandler;
import com.example.kiosk.Utill.Preferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class CS_QuestionAnswer_Activity extends AppCompatActivity {


    Context ctx;
    Preferences preferences;
    TextView tv_q_number;
    TextView tv_question;
    int Que_POS = 0;

    List<Model_QA_CovidQuestion> list_services_temp_move = new ArrayList<>();

    LinearLayout ll_start_over;
    LinearLayout ll_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qestion_answer);
        ctx = CS_QuestionAnswer_Activity.this;
        preferences = new Preferences(ctx);


        Bundle extras = getIntent().getExtras();
        String arraylist_a = extras.getString("arraylist_QA");
        Type listType = new TypeToken<List<Model_QA_CovidQuestion>>() {
        }.getType();


        list_services_temp_move = new ArrayList<>();
        list_services_temp_move = new Gson().fromJson(arraylist_a, listType);


        tv_q_number = findViewById(R.id.tv_q_number);
        tv_question = findViewById(R.id.tv_question);

        ll_start_over = findViewById(R.id.ll_start_over);
        ll_back = findViewById(R.id.ll_back);
        ll_start_over.setVisibility(View.VISIBLE);
        ll_back.setVisibility(View.GONE);

        ll_start_over.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                finishAffinity();
                Intent i = new Intent(ctx, SignInOut_Activity.class);
                i.putExtra("is_next_type",true);
                startActivity(i);
            }
        });

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Que_POS--;
                if (Que_POS == 0) {
                    ll_back.setVisibility(View.GONE);
                    ll_start_over.setVisibility(View.VISIBLE);
                    method_back();
                } else if (Que_POS > 0) {
                    ll_back.setVisibility(View.VISIBLE);
                    ll_start_over.setVisibility(View.GONE);
                    method_back();
                } else {
                    Que_POS = 0;
                }

            }
        });


        LinearLayout ll_ans_yes = findViewById(R.id.ll_ans_yes);
        ll_ans_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                method_next("1");
            }
        });

        LinearLayout ll_ans_no = findViewById(R.id.ll_ans_no);
        ll_ans_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                method_next("0");
            }
        });


    }

    private void method_back() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tv_q_number.setText("" + (Que_POS + 1));
            tv_question.setText(Html.fromHtml(list_services_temp_move.get(Que_POS).getPlainQuestion(),
                    Html.FROM_HTML_MODE_COMPACT, null, new MyTagHandler()));
        } else {
            tv_q_number.setText("" + (Que_POS + 1));
            tv_question.setText(Html.fromHtml(
                    list_services_temp_move.get(Que_POS).getPlainQuestion(),
                    null, new MyTagHandler()));
        }


    }

    private void method_next(String ans) {

        list_services_temp_move.get(Que_POS).answer = ans;

        Que_POS++;


        if (Que_POS > 0) {
            ll_back.setVisibility(View.VISIBLE);
            ll_start_over.setVisibility(View.GONE);
        } else {
            ll_back.setVisibility(View.GONE);
            ll_start_over.setVisibility(View.VISIBLE);
        }

        if ((Que_POS >= list_services_temp_move.size())) {


            Intent i = new Intent(ctx, CS_FinalAnswer_Activity.class);
            Bundle bundle = new Bundle();
            bundle.putString("arraylist_QA", new Gson().toJson(list_services_temp_move));
            i.putExtras(bundle);
            startActivity(i);

            //next screen

        } else {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tv_q_number.setText("" + (Que_POS + 1));
                tv_question.setText(Html.fromHtml(list_services_temp_move.get(Que_POS).getPlainQuestion(),
                        Html.FROM_HTML_MODE_COMPACT, null, new MyTagHandler()));
            } else {
                tv_q_number.setText("" + (Que_POS + 1));
                tv_question.setText(Html.fromHtml(
                        list_services_temp_move.get(Que_POS).getPlainQuestion(),
                        null, new MyTagHandler()));
            }


        }

    }

    private void method_first_time() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tv_q_number.setText("" + (Que_POS + 1));
            tv_question.setText(Html.fromHtml(list_services_temp_move.get(Que_POS).getPlainQuestion(),
                    Html.FROM_HTML_MODE_COMPACT, null, new MyTagHandler()));
        } else {
            tv_q_number.setText("" + (Que_POS + 1));
            tv_question.setText(Html.fromHtml(
                    list_services_temp_move.get(Que_POS).getPlainQuestion(),
                    null, new MyTagHandler()));
        }
    }

    @Override
    protected void onResume() {
        Que_POS = 0;
        ll_start_over.setVisibility(View.VISIBLE);
        ll_back.setVisibility(View.GONE);
        method_first_time();
        super.onResume();
    }


}
