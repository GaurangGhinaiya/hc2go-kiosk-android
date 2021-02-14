
package com.example.kiosk.Model.Pass;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Model_HPass_LastAnswer {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private Integer userId;

    @SerializedName("answer")
    @Expose
    private List<Model_HPass_Answer> answer = null;

    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("deleted_at")
    @Expose
    private Object deletedAt;
    @SerializedName("is_verify_identify")
    @Expose
    private Integer isVerifyIdentify;
    @SerializedName("is_verify_employer")
    @Expose
    private Integer isVerifyEmployer;
    @SerializedName("is_unmatched_answer")
    @Expose
    private Integer isUnmatchedAnswer;
    @SerializedName("expiry_date")
    @Expose
    private String expiryDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public List<Model_HPass_Answer> getAnswer() {
        return answer;
    }

    public void setAnswer(List<Model_HPass_Answer> answer) {
        this.answer = answer;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Object getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Object deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Integer getIsVerifyIdentify() {
        return isVerifyIdentify;
    }

    public void setIsVerifyIdentify(Integer isVerifyIdentify) {
        this.isVerifyIdentify = isVerifyIdentify;
    }

    public Integer getIsVerifyEmployer() {
        return isVerifyEmployer;
    }

    public void setIsVerifyEmployer(Integer isVerifyEmployer) {
        this.isVerifyEmployer = isVerifyEmployer;
    }

    public Integer getIsUnmatchedAnswer() {
        return isUnmatchedAnswer;
    }

    public void setIsUnmatchedAnswer(Integer isUnmatchedAnswer) {
        this.isUnmatchedAnswer = isUnmatchedAnswer;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

}
