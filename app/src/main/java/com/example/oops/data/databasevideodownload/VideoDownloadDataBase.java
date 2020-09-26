package com.example.oops.data.databasevideodownload;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {VideoDownloadTable.class},version = 2, exportSchema = false)
public  abstract class VideoDownloadDataBase extends RoomDatabase {


    public  abstract VideoDownloadDao videoDownloadDao();


}
