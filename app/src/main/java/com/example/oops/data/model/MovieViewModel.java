package com.example.oops.data.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.oops.data.database.MovieDetailsTable;
import com.example.oops.data.database.MovieDownloadRepository;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {
    private MovieDownloadRepository movieDownloadRepository;
    private LiveData<List<MovieDetailsTable>> allMovie;
    public MovieViewModel(@NonNull Application application) {
        super(application);
        movieDownloadRepository = new MovieDownloadRepository(application);
        allMovie = movieDownloadRepository.getAllMovie();
    }

    public void insert(MovieDetailsTable movieDetailsTable){
        movieDownloadRepository.insert(movieDetailsTable);
    }
    public  void update(MovieDetailsTable movieDetailsTable){
        movieDownloadRepository.update(movieDetailsTable);
    }
    public void delete(MovieDetailsTable movieDetailsTable){
        movieDownloadRepository.delete(movieDetailsTable);
    }
    public  void deleteAllVideos(){
        movieDownloadRepository.deleteAllMovieDownloads();
    }
    public  LiveData<List<MovieDetailsTable>> getAllVideos(){
        return  allMovie;
    }
}
