
package com.example.kiosk.Model.QuestionsAnswer;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Model_QA_Data {

    @SerializedName("user")
    @Expose
    private Model_QA_User user;
    @SerializedName("covid_questions")
    @Expose
    private List<Model_QA_CovidQuestion> covidQuestions = null;

    public Model_QA_User getUser() {
        return user;
    }

    public void setUser(Model_QA_User user) {
        this.user = user;
    }

    public List<Model_QA_CovidQuestion> getCovidQuestions() {
        return covidQuestions;
    }

    public void setCovidQuestions(List<Model_QA_CovidQuestion> covidQuestions) {
        this.covidQuestions = covidQuestions;
    }

}
