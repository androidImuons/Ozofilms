package com.example.oops.data.databasevideodownload;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


@Dao
public interface VideoDownloadDao {
    @Insert
    void  insert(VideoDownloadTable videoDownloadTable);

    @Update
    void update(VideoDownloadTable videoDownloadTable);

    @Delete
    void delete(VideoDownloadTable videoDownloadTable);




    @Query("DELETE FROM videodownload_table")
    void  deleteAllVideoDownloads();

    @Query("SELECT * FROM videodownload_table ORDER BY   timeStamp DESC")
    LiveData<List<VideoDownloadTable>> getAllVideoDownloads();
}
