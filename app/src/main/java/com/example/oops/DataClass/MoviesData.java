package com.example.oops.DataClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MoviesData {
    @SerializedName("categoryName")
    @Expose
    private String categoryName;
    @SerializedName("categorgyId")
    @Expose
    private int categorgyId;
    @SerializedName("categoryList")
    @Expose
    private ArrayList<CategoryListData> categoryList;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public ArrayList<CategoryListData> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(ArrayList<CategoryListData> categoryList) {
        this.categoryList = categoryList;
    }

    public int getCategorgyId() {
        return categorgyId;
    }

    public void setCategorgyId(int categorgyId) {
        this.categorgyId = categorgyId;
    }
}
