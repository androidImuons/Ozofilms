package com.example.oops.DataClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SeasonData {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("seriesId")
    @Expose
    private String seriesId;
    @SerializedName("seasonId")
    @Expose
    private String seasonId;
    @SerializedName("seasonNo")
    @Expose
    private Integer seasonNo;
    @SerializedName("seasonDetails")
    @Expose
    private String seasonDetails;
    @SerializedName("thumbnail")
    @Expose
    private Integer thumbnail;
    @SerializedName("releaseDate")
    @Expose
    private String releaseDate;
    @SerializedName("addedOn")
    @Expose
    private String addedOn;
    @SerializedName("thumbnailLink")
    @Expose
    private String thumbnailLink;

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

    public String getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(String seasonId) {
        this.seasonId = seasonId;
    }

    public Integer getSeasonNo() {
        return seasonNo;
    }

    public void setSeasonNo(Integer seasonNo) {
        this.seasonNo = seasonNo;
    }

    public String getSeasonDetails() {
        return seasonDetails;
    }

    public void setSeasonDetails(String seasonDetails) {
        this.seasonDetails = seasonDetails;
    }

    public Integer getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Integer thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(String addedOn) {
        this.addedOn = addedOn;
    }

    public String getThumbnailLink() {
        return thumbnailLink;
    }

    public void setThumbnailLink(String thumbnailLink) {
        this.thumbnailLink = thumbnailLink;
    }

    @Override
    public String toString() {
        return "Season "+seasonNo;
    }
}
