
package com.example.kiosk.Model.Pass;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Model_HPass_Data {

    @SerializedName("last_answer")
    @Expose
    private Model_HPass_LastAnswer lastAnswer;

    @SerializedName("user")
    @Expose
    private Model_HPass_User user;

    public Model_HPass_LastAnswer getLastAnswer() {
        return lastAnswer;
    }

    public void setLastAnswer(Model_HPass_LastAnswer lastAnswer) {
        this.lastAnswer = lastAnswer;
    }

    public Model_HPass_User getUser() {
        return user;
    }

    public void setUser(Model_HPass_User user) {
        this.user = user;
    }

}
