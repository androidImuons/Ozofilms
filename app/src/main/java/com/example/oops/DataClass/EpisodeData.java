package com.example.oops.DataClass;

import android.media.SubtitleData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class EpisodeData {
    @SerializedName("episodeId")
    @Expose
    private String episodeId;
    @SerializedName("episodeNo")
    @Expose
    private Integer episodeNo;
    @SerializedName("episodeName")
    @Expose
    private String episodeName;
    @SerializedName("episodeDetails")
    @Expose
    private String episodeDetails;
    @SerializedName("videoLink")
    @Expose
    private String videoLink;
    @SerializedName("thumbnailImage")
    @Expose
    private Integer thumbnailImage;
    @SerializedName("releaseDate")
    @Expose
    private String releaseDate;
    @SerializedName("addedOn")
    @Expose
    private String addedOn;
    @SerializedName("thumbnailLink")
    @Expose
    private String thumbnailLink;
    @SerializedName("subtitle")
    @Expose
    private ArrayList<SubtitleDataObj> subtitle ;
    @SerializedName("audio")
    @Expose
    private ArrayList<AudioData> audio;



    public String getEpisodeId() {
        return episodeId;
    }

    public void setEpisodeId(String episodeId) {
        this.episodeId = episodeId;
    }

    public Integer getEpisodeNo() {
        return episodeNo;
    }

    public void setEpisodeNo(Integer episodeNo) {
        this.episodeNo = episodeNo;
    }

    public String getEpisodeName() {
        return episodeName;
    }

    public void setEpisodeName(String episodeName) {
        this.episodeName = episodeName;
    }

    public String getEpisodeDetails() {
        return episodeDetails;
    }

    public void setEpisodeDetails(String episodeDetails) {
        this.episodeDetails = episodeDetails;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public Integer getThumbnailImage() {
        return thumbnailImage;
    }

    public void setThumbnailImage(Integer thumbnailImage) {
        this.thumbnailImage = thumbnailImage;
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

    public ArrayList<SubtitleDataObj> getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(ArrayList<SubtitleDataObj> subtitle) {
        this.subtitle = subtitle;
    }

    public ArrayList<AudioData> getAudio() {
        return audio;
    }

    public void setAudio(ArrayList<AudioData> audio) {
        this.audio = audio;
    }
}
