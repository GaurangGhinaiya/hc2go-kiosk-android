package com.example.kiosk.Model.QuestionsAnswer;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Model_QA_Only_Response {

@SerializedName("flag")
@Expose
private Boolean flag;
@SerializedName("data")
@Expose
private List<Model_QA_CovidQuestion> data = null;
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

public List<Model_QA_CovidQuestion> getData() {
return data;
}

public void setData(List<Model_QA_CovidQuestion> data) {
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