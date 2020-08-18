package com.example.oops.ResponseClass;

import com.example.oops.DataClass.ResponseData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegistrationResponse {
    @SerializedName("code")
    @Expose
    private Integer success;
    @SerializedName("message")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private ResponseData data;

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

    public ResponseData getData() {
        return data;
    }

    public void setData(ResponseData data) {
        this.data = data;
    }
}
