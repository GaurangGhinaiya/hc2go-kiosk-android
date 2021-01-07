package com.example.kiosk.Project.HPSP;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.example.kiosk.API.GlobalServiceApi;

import com.example.kiosk.Model.QuestionsAnswer.Model_QA_CovidQuestion;
import com.example.kiosk.Model.QuestionsAnswer.Model_QA_Only_Response;

import com.example.kiosk.Project.CS.CS_FinalAnswer_Activity;
import com.example.kiosk.Project.SignInOut_Activity;

import com.example.kiosk.R;
import com.example.kiosk.Utill.MyTagHandler;
import com.example.kiosk.Utill.Preferences;
import com.example.kiosk.Utill.utills;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class HPSP_Using_QuestionAnswer_Activity extends AppCompatActivity {

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
        ctx = HPSP_Using_QuestionAnswer_Activity.this;
        preferences = new Preferences(ctx);

        list_services_temp_move = new ArrayList<>();


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
                i.putExtra("is_next_type", true);
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



        if (!preferences.getHomecarePassQList().equalsIgnoreCase("")) {
            method_get_agency_list_after_8hours(preferences.getHomecarePassQList());
        } else {
            method_get_agency_list();
        }


        super.onResume();
    }


    Dialog progressDialog = null;
    private void method_get_agency_list() {
        if (utills.isOnline(ctx)) {
            progressDialog = utills.startLoader(ctx);
            AndroidNetworking.post(GlobalServiceApi.API_get_covid19_questions)
                    .addBodyParameter("branch_id", "" + preferences.getBranchid())
                    .addBodyParameter("company_id", "" + preferences.getCompany_id())
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


                                    Model_QA_Only_Response agency_responce = new Gson().fromJson(result, Model_QA_Only_Response.class);
                                    if (agency_responce.getData() != null) {
                                        List<Model_QA_CovidQuestion> list_services_temp = new ArrayList<>();
                                        list_services_temp = agency_responce.getData();
                                        if (list_services_temp.size() > 0) {

                                                list_services_temp_move = new ArrayList<>();
                                                list_services_temp_move.addAll(list_services_temp);
                                                method_first_time();


                                        }
                                    }
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



    private void method_get_agency_list_after_8hours(String homecarePassQList) {

//        List<Model_HPass_Answer> list_homecare_temp = new ArrayList<>();
//        Type listType2 = new TypeToken<List<Model_HPass_Answer>>() {}.getType();
//        list_homecare_temp = new Gson().fromJson(homecarePassQList, listType2);
//

        if (utills.isOnline(ctx)) {
            progressDialog = utills.startLoader(ctx);
            AndroidNetworking.post(GlobalServiceApi.API_getKioskHomecareCovidQuestions)
                    .addBodyParameter("branch_id", "" + preferences.getBranchid())
                    .addBodyParameter("company_id", "" + preferences.getCompany_id())
                    .addBodyParameter("covid_answer", "" + homecarePassQList)
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


                                    Model_QA_Only_Response agency_responce = new Gson().fromJson(result, Model_QA_Only_Response.class);
                                    if (agency_responce.getData() != null) {
                                        List<Model_QA_CovidQuestion> list_services_temp = new ArrayList<>();
                                        list_services_temp = agency_responce.getData();
                                        if (list_services_temp.size() > 0) {

                                            list_services_temp_move = new ArrayList<>();
                                            list_services_temp_move.addAll(list_services_temp);
                                            method_first_time();


                                        }
                                    }
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



//    private void method_setdata(List<Model_QA_CovidQuestion> list_kiosk_temp, String homecarePassQList) {
//
//        list_services_temp_move = new ArrayList<>();
//
//        List<Model_HPass_Answer> list_homecare_temp = new ArrayList<>();
//        Type listType2 = new TypeToken<List<Model_HPass_Answer>>() {}.getType();
//        list_homecare_temp = new Gson().fromJson(homecarePassQList, listType2);
//
//
//        for (int j = 0; j < list_kiosk_temp.size(); j++) {
//
//            String i_id = "" + list_kiosk_temp.get(j).getId();
//
//            boolean found = false;
//            int poss = 0;
//
//            for (int i_select = 0; i_select < list_homecare_temp.size(); i_select++) {
//                if (i_id.equalsIgnoreCase("" + list_homecare_temp.get(i_select).getQuestionId())) {
//                    found = true;
//                    poss = i_select;
//                }
//            }
//
//            if (found) {
//                Model_QA_CovidQuestion suB_services_data2 = new Model_QA_CovidQuestion();
//                suB_services_data2.setId(list_homecare_temp.get(poss).getQuestionId());
//                suB_services_data2.setPlainQuestion("" + list_homecare_temp.get(poss).getPlainQuestion());
//                suB_services_data2.setQuestionType("0");
//                suB_services_data2.answer = "";
//                list_services_temp_move.add(suB_services_data2);
//                poss = 0;
//            } else {
//                Model_QA_CovidQuestion suB_services_data2 = new Model_QA_CovidQuestion();
//                suB_services_data2.setId(list_kiosk_temp.get(j).getId());
//                suB_services_data2.setPlainQuestion("" + list_kiosk_temp.get(j).getPlainQuestion());
//                suB_services_data2.setQuestionType("0");
//                suB_services_data2.answer = "";
//                list_services_temp_move.add(suB_services_data2);
//            }
//
//        }
//        method_first_time();
//
//    }

}
