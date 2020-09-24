package com.example.oops.data.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.oops.data.databasevideodownload.VideoDownloadDao;
import com.example.oops.data.databasevideodownload.VideoDownloadDataBase;
import com.example.oops.data.databasevideodownload.VideoDownloadTable;


import java.util.List;

public class MovieDownloadRepository {
    private MovieDownloadDao movieDownloadDao;
    private LiveData<List<MovieDetailsTable>> allMovie;

    public  MovieDownloadRepository(Application application) {
        MovieDownloadDatabase movieDownloadDatabase = MovieDownloadDatabase.getInstance(application);
        movieDownloadDao = movieDownloadDatabase.movieDownloadDao();
        allMovie = movieDownloadDao.getAllMovieDetails();
    }
    public  void insert(MovieDetailsTable movieDetailsTable){
        new MovieDownloadRepository.InsertMovieDetailsAsyncTask(movieDownloadDao).execute(movieDetailsTable);

    }
    public  void update(MovieDetailsTable  movieDetailsTable){
        new MovieDownloadRepository.UpdateMovieDetailsAsyncTask(movieDownloadDao).execute(movieDetailsTable);
    }
    public  void delete(MovieDetailsTable movieDetailsTable){
        new MovieDownloadRepository.DeleteMovieDetailsAsyncTask(movieDownloadDao).execute(movieDetailsTable);
    }
    public void deleteAllMovieDownloads(){
        new MovieDownloadRepository.DeleteAllMovieDetailsAsyncTask(movieDownloadDao).execute();

    }
    public  LiveData<List<MovieDetailsTable>> getAllMovie(){
        return  allMovie;
    }
    private  static  class InsertMovieDetailsAsyncTask extends AsyncTask<MovieDetailsTable,Void,Void> {
        private  MovieDownloadDao movieDownloadDao;
        private  InsertMovieDetailsAsyncTask(MovieDownloadDao movieDownloadDao){
            this.movieDownloadDao = movieDownloadDao;
        }
        @Override
        protected Void doInBackground(MovieDetailsTable... movieDetailsTables) {
            movieDownloadDao.insert(movieDetailsTables[0]);
            return null;
        }
    }


    private  static  class UpdateMovieDetailsAsyncTask extends AsyncTask<MovieDetailsTable,Void,Void>{
        private  MovieDownloadDao movieDownloadDao;
        private  UpdateMovieDetailsAsyncTask(MovieDownloadDao movieDownloadDao){
            this.movieDownloadDao = movieDownloadDao;
        }
        @Override
        protected Void doInBackground(MovieDetailsTable... movieDetailsTables) {
            movieDownloadDao.update(movieDetailsTables[0]);
            return null;
        }
    }

    private  static  class DeleteMovieDetailsAsyncTask extends AsyncTask<MovieDetailsTable,Void,Void>{
        private  MovieDownloadDao movieDownloadDao;
        private  DeleteMovieDetailsAsyncTask(MovieDownloadDao movieDownloadDao){
            this.movieDownloadDao = movieDownloadDao;
        }
        @Override
        protected Void doInBackground(MovieDetailsTable... movieDetailsTables) {
            movieDownloadDao.delete(movieDetailsTables[0]);
            return null;
        }
    }
    private  static  class DeleteAllMovieDetailsAsyncTask extends AsyncTask<Void,Void,Void>{
        private  MovieDownloadDao movieDownloadDao;
        private  DeleteAllMovieDetailsAsyncTask(MovieDownloadDao movieDownloadDao){
            this.movieDownloadDao = movieDownloadDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            movieDownloadDao.deleteAllMovieDetails();
            return null;
        }
    }
}
