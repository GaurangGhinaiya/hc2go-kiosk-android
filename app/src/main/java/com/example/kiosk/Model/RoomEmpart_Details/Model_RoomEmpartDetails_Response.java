
package com.example.kiosk.Model.RoomEmpart_Details;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Model_RoomEmpartDetails_Response {

    @SerializedName("flag")
    @Expose
    private Boolean flag;
    @SerializedName("data")
    @Expose
    private List<Model_RoomEmpartDetails_Data> data = null;
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

    public List<Model_RoomEmpartDetails_Data> getData() {
        return data;
    }

    public void setData(List<Model_RoomEmpartDetails_Data> data) {
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
