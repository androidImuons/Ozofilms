package com.example.oops.ResponseClass;

import com.example.oops.DataClass.MoviesSearchModule;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MoviesSearchResponse {
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private ArrayList<MoviesSearchModule> data;

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

    public ArrayList<MoviesSearchModule> getData() {
        return data;
    }

    public void setData(ArrayList<MoviesSearchModule> data) {
        this.data = data;
    }
}
