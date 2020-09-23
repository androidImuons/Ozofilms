package com.example.oops.data.databasevideodownload;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class VideoRepository {
private  VideoDownloadDao videoDownloadDao;
private  LiveData<List<VideoDownloadTable>> allVideos;

public  VideoRepository(Application application) {
    VideoDownloadDataBase videoDownloadDataBase = VideoDownloadDataBase.getInstance(application);
    videoDownloadDao = videoDownloadDataBase.videoDownloadDao();
    allVideos = videoDownloadDao.getAllVideoDownloads();
}
public  void insert(VideoDownloadTable videoDownloadTable){
    new InsertVideoAsyncTask(videoDownloadDao).execute(videoDownloadTable);

}
public  void update(VideoDownloadTable  videoDownloadTable){
new UpdateVideoAsyncTask(videoDownloadDao).execute(videoDownloadTable);
}
public  void delete(VideoDownloadTable videoDownloadTable){
new DeleteVideoAsyncTask(videoDownloadDao).execute(videoDownloadTable);
}
public void deleteAllVideoDownloads(){
    new DeleteAllVideoAsyncTask(videoDownloadDao).execute();

}
public  LiveData<List<VideoDownloadTable>> getAllVideoDownloads(){
    return  allVideos;
}
private  static  class InsertVideoAsyncTask extends AsyncTask<VideoDownloadTable,Void,Void>{
private  VideoDownloadDao videoDownloadDao;
private  InsertVideoAsyncTask(VideoDownloadDao videoDownloadDao){
    this.videoDownloadDao = videoDownloadDao;
}
    @Override
    protected Void doInBackground(VideoDownloadTable... videoDownloadTables) {
       videoDownloadDao.insert(videoDownloadTables[0]);
        return null;
    }
}


    private  static  class UpdateVideoAsyncTask extends AsyncTask<VideoDownloadTable,Void,Void>{
        private  VideoDownloadDao videoDownloadDao;
        private  UpdateVideoAsyncTask(VideoDownloadDao videoDownloadDao){
            this.videoDownloadDao = videoDownloadDao;
        }
        @Override
        protected Void doInBackground(VideoDownloadTable... videoDownloadTables) {
            videoDownloadDao.update(videoDownloadTables[0]);
            return null;
        }
    }

    private  static  class DeleteVideoAsyncTask extends AsyncTask<VideoDownloadTable,Void,Void>{
        private  VideoDownloadDao videoDownloadDao;
        private  DeleteVideoAsyncTask(VideoDownloadDao videoDownloadDao){
            this.videoDownloadDao = videoDownloadDao;
        }
        @Override
        protected Void doInBackground(VideoDownloadTable... videoDownloadTables) {
            videoDownloadDao.delete(videoDownloadTables[0]);
            return null;
        }
    }
    private  static  class DeleteAllVideoAsyncTask extends AsyncTask<Void,Void,Void>{
        private  VideoDownloadDao videoDownloadDao;
        private  DeleteAllVideoAsyncTask(VideoDownloadDao videoDownloadDao){
            this.videoDownloadDao = videoDownloadDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            videoDownloadDao.deleteAllVideoDownloads();
            return null;
        }
    }
}
