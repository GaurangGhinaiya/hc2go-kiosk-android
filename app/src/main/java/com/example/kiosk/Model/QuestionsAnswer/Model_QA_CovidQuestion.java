
package com.example.kiosk.Model.QuestionsAnswer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Model_QA_CovidQuestion {

    @SerializedName("id")
    @Expose
    private Integer id;


    @SerializedName("plain_question")
    @Expose
    private String plainQuestion;



    @SerializedName("question_type")
    @Expose
    private String questionType;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPlainQuestion() {
        return plainQuestion;
    }

    public void setPlainQuestion(String plainQuestion) {
        this.plainQuestion = plainQuestion;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }


    public String answer="";


}
