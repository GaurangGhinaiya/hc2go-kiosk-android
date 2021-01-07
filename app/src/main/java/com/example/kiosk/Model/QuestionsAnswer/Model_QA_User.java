
package com.example.kiosk.Model.QuestionsAnswer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Model_QA_User {

    @SerializedName("id")
    @Expose
    private Integer id;


    @SerializedName("name")
    @Expose
    private String name;





    @SerializedName("email")
    @Expose
    private String email;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    @SerializedName("phone")
    @Expose
    private String phone;

    public String getphone() {
        return phone;
    }

    public void setphone(String phone) {
        this.phone = phone;
    }


    @SerializedName("company")
    @Expose
    private String company;

    public String getcompany() {
        return company;
    }

    public void setcompany(String company) {
        this.company = company;
    }


    @SerializedName("family_type")
    @Expose
    private String family_type;

    public String getfamily_type() {
        return family_type;
    }

    public void setfamily_type(String family_type) {
        this.family_type = family_type;
    }


}
