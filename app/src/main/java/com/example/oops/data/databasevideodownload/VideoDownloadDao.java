package com.example.oops.data.databasevideodownload;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


@Dao
public interface VideoDownloadDao {
    @Query("SELECT * FROM VIDEODOWNLOAD_TABLE ORDER BY id DESC")
    List<VideoDownloadTable> getAll();

    @Insert
    void insert(VideoDownloadTable videoDownloadTable);

    @Delete
    void delete(VideoDownloadTable videoDownloadTable);

    @Update
    void update(VideoDownloadTable videoDownloadTable);

    @Query("SELECT * FROM videodownload_table WHERE movieId = :movieId")
    int isDataExist(boolean movieId);
}
