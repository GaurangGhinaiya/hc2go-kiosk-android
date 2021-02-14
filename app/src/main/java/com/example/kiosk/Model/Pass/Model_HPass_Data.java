
package com.example.kiosk.Model.Pass;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Model_HPass_Data {

    @SerializedName("questions")
    @Expose
    private List<Model_HPass_Answer> questions = null;


    @SerializedName("answers")
    @Expose
    private List<Model_HPass_Answer> answers = null;

    @SerializedName("user")
    @Expose
    private Model_HPass_User user;



    public List<Model_HPass_Answer> getquestions() {
        return questions;
    }
    public void setquestions(List<Model_HPass_Answer> questions) {
        this.questions = questions;
    }


    public List<Model_HPass_Answer> getAnswer() {
        return answers;
    }
    public void setAnswer(List<Model_HPass_Answer> answers) {
        this.answers = answers;
    }





    public Model_HPass_User getUser() {
        return user;
    }

    public void setUser(Model_HPass_User user) {
        this.user = user;
    }

}
