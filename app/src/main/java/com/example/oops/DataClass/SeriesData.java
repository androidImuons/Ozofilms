package com.example.oops.DataClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SeriesData {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("seriesId")
    @Expose
    private String seriesId;
    @SerializedName("seriesName")
    @Expose
    private String seriesName;
    @SerializedName("seriesShortDescription")
    @Expose
    private String seriesShortDescription;
    @SerializedName("seriesLongDescription")
    @Expose
    private String seriesLongDescription;
    @SerializedName("seriesCategory")
    @Expose
    private Integer seriesCategory;
    @SerializedName("seriesType")
    @Expose
    private String seriesType;
    @SerializedName("totalViews")
    @Expose
    private Integer totalViews;
    @SerializedName("thumbnail")
    @Expose
    private Integer thumbnail;
    @SerializedName("trailer")
    @Expose
    private Integer trailer;
    @SerializedName("banner")
    @Expose
    private Integer banner;
    @SerializedName("releaseData")
    @Expose
    private String releaseData;
    @SerializedName("addedOn")
    @Expose
    private String addedOn;
    @SerializedName("imageLink")
    @Expose
    private String imageLink;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(String seriesId) {
        this.seriesId = seriesId;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public String getSeriesShortDescription() {
        return seriesShortDescription;
    }

    public void setSeriesShortDescription(String seriesShortDescription) {
        this.seriesShortDescription = seriesShortDescription;
    }

    public String getSeriesLongDescription() {
        return seriesLongDescription;
    }

    public void setSeriesLongDescription(String seriesLongDescription) {
        this.seriesLongDescription = seriesLongDescription;
    }

    public Integer getSeriesCategory() {
        return seriesCategory;
    }

    public void setSeriesCategory(Integer seriesCategory) {
        this.seriesCategory = seriesCategory;
    }

    public String getSeriesType() {
        return seriesType;
    }

    public void setSeriesType(String seriesType) {
        this.seriesType = seriesType;
    }

    public Integer getTotalViews() {
        return totalViews;
    }

    public void setTotalViews(Integer totalViews) {
        this.totalViews = totalViews;
    }

    public Integer getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Integer thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Integer getTrailer() {
        return trailer;
    }

    public void setTrailer(Integer trailer) {
        this.trailer = trailer;
    }

    public Integer getBanner() {
        return banner;
    }

    public void setBanner(Integer banner) {
        this.banner = banner;
    }

    public String getReleaseData() {
        return releaseData;
    }

    public void setReleaseData(String releaseData) {
        this.releaseData = releaseData;
    }

    public String getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(String addedOn) {
        this.addedOn = addedOn;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }
}
