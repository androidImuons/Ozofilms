package com.example.oops.DataClass;

public class SearchData {
    private int id ;
    String movieId , movieName , imageLink;


    public SearchData(int id, String movieId, String movieName, String imageLink) {
        this.id = id;
        this.movieId = movieId;
        this.movieName = movieName;
        this.imageLink = imageLink;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }
}
