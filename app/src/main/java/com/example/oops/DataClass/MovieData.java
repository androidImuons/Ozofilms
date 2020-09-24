package com.example.oops.DataClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieData {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("movieId")
    @Expose
    private String movieId;
    @SerializedName("movieName")
    @Expose
    private String movieName;
    @SerializedName("movieShortDescription")
    @Expose
    private String movieShortDescription;
    @SerializedName("movieLongDescription")
    @Expose
    private String movieLongDescription;
    @SerializedName("movieCategory")
    @Expose
    private Integer movieCategory;
    @SerializedName("movieType")
    @Expose
    private String movieType;
    @SerializedName("cast")
    @Expose
    private Object cast;
    @SerializedName("director")
    @Expose
    private Object director;
    @SerializedName("totalViews")
    @Expose
    private Integer totalViews;
    @SerializedName("trailer")
    @Expose
    private Integer trailer;
    @SerializedName("video")
    @Expose
    private Integer video;
    @SerializedName("subtitles")
    @Expose
    private String subtitles;
    @SerializedName("thumbnailImage")
    @Expose
    private Integer thumbnailImage;
    @SerializedName("bannerImage")
    @Expose
    private Integer bannerImage;
    @SerializedName("audioFile")
    @Expose
    private String audioFile;
    @SerializedName("addedOn")
    @Expose
    private String addedOn;
    @SerializedName("releaseDate")
    @Expose
    private String releaseDate;
    @SerializedName("videoLink")
    @Expose
    private String videoLink;
    @SerializedName("imageLink")
    @Expose
    private String imageLink;
    @SerializedName("trailerLink")
    @Expose
    private String trailerLink;
    @SerializedName("bannerLink")
    @Expose
    private String bannerLink;
    @SerializedName("categoryName")
    @Expose
    private String categoryName;

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

    public String getMovieShortDescription() {
        return movieShortDescription;
    }

    public void setMovieShortDescription(String movieShortDescription) {
        this.movieShortDescription = movieShortDescription;
    }

    public String getMovieLongDescription() {
        return movieLongDescription;
    }

    public void setMovieLongDescription(String movieLongDescription) {
        this.movieLongDescription = movieLongDescription;
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

    public Object getCast() {
        return cast;
    }

    public void setCast(Object cast) {
        this.cast = cast;
    }

    public Object getDirector() {
        return director;
    }

    public void setDirector(Object director) {
        this.director = director;
    }

    public Integer getTotalViews() {
        return totalViews;
    }

    public void setTotalViews(Integer totalViews) {
        this.totalViews = totalViews;
    }

    public Integer getTrailer() {
        return trailer;
    }

    public void setTrailer(Integer trailer) {
        this.trailer = trailer;
    }

    public Integer getVideo() {
        return video;
    }

    public void setVideo(Integer video) {
        this.video = video;
    }

    public String getSubtitles() {
        return subtitles;
    }

    public void setSubtitles(String subtitles) {
        this.subtitles = subtitles;
    }

    public Integer getThumbnailImage() {
        return thumbnailImage;
    }

    public void setThumbnailImage(Integer thumbnailImage) {
        this.thumbnailImage = thumbnailImage;
    }

    public Integer getBannerImage() {
        return bannerImage;
    }

    public void setBannerImage(Integer bannerImage) {
        this.bannerImage = bannerImage;
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

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getTrailerLink() {
        return trailerLink;
    }

    public void setTrailerLink(String trailerLink) {
        this.trailerLink = trailerLink;
    }

    public String getBannerLink() {
        return bannerLink;
    }

    public void setBannerLink(String bannerLink) {
        this.bannerLink = bannerLink;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
