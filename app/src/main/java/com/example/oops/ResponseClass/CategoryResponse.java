package com.example.oops.ResponseClass;

import com.example.oops.DataClass.CategoryListData;
import com.example.oops.DataClass.MoviesData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CategoryResponse {
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private ArrayList<CategoryListData> data;

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

    public ArrayList<CategoryListData> getData() {
        return data;
    }

    public void setData(ArrayList<CategoryListData> data) {
        this.data = data;
    }
}
