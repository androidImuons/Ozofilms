package com.example.oops.data.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.oops.data.databasevideodownload.VideoDownloadTable;
import com.example.oops.data.databasevideodownload.VideoRepository;

import java.util.List;

public class VideoViewModel extends AndroidViewModel {
    private VideoRepository videoRepository;
    private  LiveData<List<VideoDownloadTable>> allVideos;
    public VideoViewModel(@NonNull Application application) {
        super(application);
        videoRepository = new VideoRepository(application);
        allVideos = videoRepository.getAllVideoDownloads();
    }
    public void insert(VideoDownloadTable videoDownloadTable){
        videoRepository.insert(videoDownloadTable);
    }
    public  void update(VideoDownloadTable videoDownloadTable){
        videoRepository.update(videoDownloadTable);
    }
    public void delete(VideoDownloadTable videoDownloadTable){
        videoRepository.delete(videoDownloadTable);
    }
    public  void deleteAllVideos(){
        videoRepository.deleteAllVideoDownloads();
    }
    public  LiveData<List<VideoDownloadTable>> getAllVideos(){
        return  allVideos;
    }
}
