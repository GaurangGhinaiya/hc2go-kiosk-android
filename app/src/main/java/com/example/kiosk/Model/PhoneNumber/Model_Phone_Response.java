
package com.example.kiosk.Model.PhoneNumber;

import com.example.kiosk.Model.QuestionsAnswer.Model_QA_User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Model_Phone_Response {

    @SerializedName("flag")
    @Expose
    private Boolean flag;
    @SerializedName("data")
    @Expose
    private Model_QA_User data;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("code")
    @Expose
    private Integer code;

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public Model_QA_User getData() {
        return data;
    }

    public void setData(Model_QA_User data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

}
