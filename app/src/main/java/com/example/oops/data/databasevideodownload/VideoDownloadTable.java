package com.example.oops.data.databasevideodownload;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "videodownload_table")
public class VideoDownloadTable  implements Serializable {
    @PrimaryKey(autoGenerate = true)
private  int id;

private  String movieId;
private  String movieName;
private String movieType;
private String movieDescription;
private  String urlVideo;
private  String urlImage;
    @ColumnInfo(name = "timestamp")
    String timestamp;


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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMovieDescription() {
        return movieDescription;
    }

    public void setMovieDescription(String movieDescription) {
        this.movieDescription = movieDescription;
    }
}
