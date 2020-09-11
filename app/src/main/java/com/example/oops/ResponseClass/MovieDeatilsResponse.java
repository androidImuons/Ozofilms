package com.example.oops.ResponseClass;

import com.example.oops.DataClass.MovieDeatilsData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieDeatilsResponse {
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private MovieDeatilsData data;

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

    public MovieDeatilsData getData() {
        return data;
    }

    public void setData(MovieDeatilsData data) {
        this.data = data;
    }
}
