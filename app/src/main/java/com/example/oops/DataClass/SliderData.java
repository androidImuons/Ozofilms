package com.example.oops.DataClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SliderData {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("movieId")
    @Expose
    private String movieId;
    @SerializedName("movieName")
    @Expose
    private String movieName;
    @SerializedName("movieDetails")
    @Expose
    private String movieDetails;
    @SerializedName("movieCategory")
    @Expose
    private Integer movieCategory;
    @SerializedName("movieType")
    @Expose
    private String movieType;
    @SerializedName("totalViews")
    @Expose
    private Integer totalViews;
    @SerializedName("videoLink")
    @Expose
    private String videoLink;
    @SerializedName("subtitles")
    @Expose
    private String subtitles;
    @SerializedName("thumbnailImage")
    @Expose
    private String thumbnailImage;
    @SerializedName("audioFile")
    @Expose
    private String audioFile;
    @SerializedName("addedOn")
    @Expose
    private String addedOn;
    @SerializedName("releaseDate")
    @Expose
    private String releaseDate;
    @SerializedName("imageLink")
    @Expose
    private String imageLink;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getMovieDetails() {
        return movieDetails;
    }

    public void setMovieDetails(String movieDetails) {
        this.movieDetails = movieDetails;
    }

    public Integer getMovieCategory() {
        return movieCategory;
    }

    public void setMovieCategory(Integer movieCategory) {
        this.movieCategory = movieCategory;
    }

    public String getMovieType() {
        return movieType;
    }

    public void setMovieType(String movieType) {
        this.movieType = movieType;
    }

    public Integer getTotalViews() {
        return totalViews;
    }

    public void setTotalViews(Integer totalViews) {
        this.totalViews = totalViews;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public String getSubtitles() {
        return subtitles;
    }

    public void setSubtitles(String subtitles) {
        this.subtitles = subtitles;
    }

    public String getThumbnailImage() {
        return thumbnailImage;
    }

    public void setThumbnailImage(String thumbnailImage) {
        this.thumbnailImage = thumbnailImage;
    }

    public String getAudioFile() {
        return audioFile;
    }

    public void setAudioFile(String audioFile) {
        this.audioFile = audioFile;
    }

    public String getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(String addedOn) {
        this.addedOn = addedOn;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }
}
