package com.example.kiosk.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Model_Facility_Data {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("company_id")
    @Expose
    private Integer company_id;

    @SerializedName("company")
    @Expose
    private Model_Facility_Company company;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getcode() {
        return code;
    }

    public void setcode(String code) {
        this.code = code;
    }


    public Integer getcompany_id() {
        return company_id;
    }

    public void setcompany_id(Integer company_id) {
        this.company_id = company_id;
    }


    public Model_Facility_Company getcompany() {
        return company;
    }

    public void setcompany(Model_Facility_Company company) {
        this.company = company;
    }


}