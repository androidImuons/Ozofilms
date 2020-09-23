package com.example.oops.data.databasevideodownload;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "videodownload_table")
public class VideoDownloadTable {
    @PrimaryKey(autoGenerate = true)
private  int id;
    private int timeStamp;
private  String movieId;
private  String movieName;
private  String urlVideo;
private  String urlImage;


    public VideoDownloadTable(int timeStamp, String movieId, String movieName, String urlVideo, String urlImage) {
        this.timeStamp = timeStamp;
        this.movieId = movieId;
        this.movieName = movieName;
        this.urlVideo = urlVideo;
        this.urlImage = urlImage;

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


}
