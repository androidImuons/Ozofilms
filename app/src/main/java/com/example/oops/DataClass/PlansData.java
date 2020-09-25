package com.example.oops.DataClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlansData {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("planName")
    @Expose
    private String planName;

    @SerializedName("durationType")
    @Expose
    private String durationType;

    @SerializedName("duration")
    @Expose
    private Integer duration;

    @SerializedName("planCost")
    @Expose
    private Integer planCost;

    @SerializedName("description")
    @Expose
    private String description;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getDurationType() {
        return durationType;
    }

    public void setDurationType(String durationType) {
        this.durationType = durationType;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getPlanCost() {
        return planCost;
    }

    public void setPlanCost(Integer planCost) {
        this.planCost = planCost;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
