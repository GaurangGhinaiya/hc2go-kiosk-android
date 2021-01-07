
package com.example.kiosk.Model.Pass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Model_HPass_Answer {

    @SerializedName("question_type")
    @Expose
    private String questionType;

    @SerializedName("answer")
    @Expose
    private String answer;

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("homecare_question_id")
    @Expose
    private Integer homecare_question_id;

    @SerializedName("plain_question")
    @Expose
    private String plainQuestion;
    @SerializedName("default_answer")
    @Expose
    private String defaultAnswer;

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Integer getid() {
        return id;
    }

    public void setid(Integer id) {
        this.id = id;
    }

    public String getPlainQuestion() {
        return plainQuestion;
    }

    public void setPlainQuestion(String plainQuestion) {
        this.plainQuestion = plainQuestion;
    }

    public String getDefaultAnswer() {
        return defaultAnswer;
    }

    public void setDefaultAnswer(String defaultAnswer) {
        this.defaultAnswer = defaultAnswer;
    }


    public Integer gethomecare_question_id() {
        return homecare_question_id;
    }

    public void sethomecare_question_id(Integer homecare_question_id) {
        this.homecare_question_id = homecare_question_id;
    }

}
