package com.example.oops.data.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "moviedetails_table")
public class MovieDetailsTable {
    @PrimaryKey(autoGenerate = true)
    private  int id;
    private int applicationId;
    private  int movieId;
    private  String movieName;
    private String movieShortDescription;
    private  String movieLongDescription;
    private  int movieCategory;
    private  String movieType;
    private String cast;
    private String director;
    private int totalViews;
    private int trailer;
    private int video;
    private String subtitles;
    private int thumbnailImage;
    private int bannerImage;
    private String audioFile;
    private String addedOn;
    private String releaseDate;
    private String videoLink;
    private String imageLink;
    private String trailerLink;
    private String bannerLink;
    private String categoryName;

    public MovieDetailsTable(int applicationId, int movieId, String movieName, String movieShortDescription, String movieLongDescription, int movieCategory, String movieType, String cast, String director, int totalViews, int trailer, int video, String subtitles, int thumbnailImage, int bannerImage, String audioFile, String addedOn, String releaseDate, String videoLink, String imageLink, String trailerLink, String bannerLink, String categoryName) {
        this.applicationId = applicationId;
        this.movieId = movieId;
        this.movieName = movieName;
        this.movieShortDescription = movieShortDescription;
        this.movieLongDescription = movieLongDescription;
        this.movieCategory = movieCategory;
        this.movieType = movieType;
        this.cast = cast;
        this.director = director;
        this.totalViews = totalViews;
        this.trailer = trailer;
        this.video = video;
        this.subtitles = subtitles;
        this.thumbnailImage = thumbnailImage;
        this.bannerImage = bannerImage;
        this.audioFile = audioFile;
        this.addedOn = addedOn;
        this.releaseDate = releaseDate;
        this.videoLink = videoLink;
        this.imageLink = imageLink;
        this.trailerLink = trailerLink;
        this.bannerLink = bannerLink;
        this.categoryName = categoryName;
    }


    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getApplicationId() {
        return applicationId;
    }

    public int getMovieId() {
        return movieId;
    }

    public String getMovieName() {
        return movieName;
    }

    public String getMovieShortDescription() {
        return movieShortDescription;
    }

    public String getMovieLongDescription() {
        return movieLongDescription;
    }

    public int getMovieCategory() {
        return movieCategory;
    }

    public String getMovieType() {
        return movieType;
    }

    public String getCast() {
        return cast;
    }

    public String getDirector() {
        return director;
    }

    public int getTotalViews() {
        return totalViews;
    }

    public int getTrailer() {
        return trailer;
    }

    public int getVideo() {
        return video;
    }

    public String getSubtitles() {
        return subtitles;
    }

    public int getThumbnailImage() {
        return thumbnailImage;
    }

    public int getBannerImage() {
        return bannerImage;
    }

    public String getAudioFile() {
        return audioFile;
    }

    public String getAddedOn() {
        return addedOn;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public String getImageLink() {
        return imageLink;
    }

    public String getTrailerLink() {
        return trailerLink;
    }

    public String getBannerLink() {
        return bannerLink;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
