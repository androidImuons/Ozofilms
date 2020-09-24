package com.example.oops.ResponseClass;

import com.example.oops.DataClass.MoviesData;
import com.example.oops.DataClass.PlansData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SubscriptionPlansResponse {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private ArrayList<PlansData> data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<PlansData> getData() {
        return data;
    }

    public void setData(ArrayList<PlansData> data) {
        this.data = data;
    }
}


