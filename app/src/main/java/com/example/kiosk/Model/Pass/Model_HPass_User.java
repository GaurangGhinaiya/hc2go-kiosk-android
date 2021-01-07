
package com.example.kiosk.Model.Pass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Model_HPass_User {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("email")
    @Expose
    private Object email;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("company")
    @Expose
    private String company;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


  public String gettitle() {
        return title;
    }

    public void settitle(String title) {
        this.title = title;
    }

    public Object getEmail() {
        return email;
    }

    public void setEmail(Object email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

}
