package com.example.oops.DataClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FavouriteData {
    @SerializedName("series")
    @Expose
    private ArrayList<SeriesData> series ;
    @SerializedName("movie")
    @Expose
    private ArrayList<MovieData> movie;

    public ArrayList<SeriesData> getSeries() {
        return series;
    }

    public void setSeries(ArrayList<SeriesData> series) {
        this.series = series;
    }

    public ArrayList<MovieData> getMovie() {
        return movie;
    }

    public void setMovie(ArrayList<MovieData> movie) {
        this.movie = movie;
    }
}
