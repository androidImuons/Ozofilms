package com.example.oops.ResponseClass;

import com.example.oops.DataClass.ResponseData;
import com.example.oops.DataClass.SocialData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SocialResponse {
    @SerializedName("code")
    @Expose
    private Integer success;
    @SerializedName("message")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private SocialData data;

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public SocialData getData() {
        return data;
    }

    public void setData(SocialData data) {
        this.data = data;
    }
}
