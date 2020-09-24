package com.example.oops.data.databasevideodownload;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "videodownload_table")
public class VideoDownloadTable  implements Serializable {
    @PrimaryKey(autoGenerate = true)
private  int id;
    private int timeStamp;
private  String movieId;
private  String movieName;
private String movieType;
private  String urlVideo;
private  String urlImage;


    public String getMovieType() {
        return movieType;
    }

    public void setMovieType(String movieType) {
        this.movieType = movieType;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getTimeStamp() {
        return timeStamp;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getMovieName() {
        return movieName;
    }

    public String getUrlVideo() {
        return urlVideo;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setTimeStamp(int timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public void setUrlVideo(String urlVideo) {
        this.urlVideo = urlVideo;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }
}
