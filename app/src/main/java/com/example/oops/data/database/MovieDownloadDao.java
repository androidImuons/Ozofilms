package com.example.oops.data.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.oops.data.databasevideodownload.VideoDownloadTable;

import java.util.List;

@Dao
public interface MovieDownloadDao {
    @Insert
    void  insert(MovieDetailsTable movieDetailsTable);

    @Update
    void update(MovieDetailsTable movieDetailsTable);

    @Delete
    void delete(MovieDetailsTable movieDetailsTable);


    @Query("DELETE FROM moviedetails_table")
    void  deleteAllMovieDetails();

    @Query("SELECT * FROM moviedetails_table ORDER BY   applicationId DESC")
    LiveData<List<MovieDetailsTable>> getAllMovieDetails();
}
