package com.example.kiosk.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Model_Facility_Company {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("name")
    @Expose
    private String name;


    @SerializedName("profile_pic")
    @Expose
    private String profile_pic;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }


    public String getprofile_pic() {
        return profile_pic;
    }

    public void setprofile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }


}