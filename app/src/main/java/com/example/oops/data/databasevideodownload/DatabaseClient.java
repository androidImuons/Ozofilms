package com.example.oops.data.databasevideodownload;

import android.content.Context;

import androidx.room.Room;

public class DatabaseClient {

    private Context mCtx;
    private static DatabaseClient mInstance;

    //our app database object
    private VideoDownloadDataBase videoDownloadDataBase;

    private DatabaseClient(Context mCtx) {
        this.mCtx = mCtx;

        //creating the app database with Room database builder
        //MyToDos is the name of the database
        videoDownloadDataBase = Room.databaseBuilder(mCtx, VideoDownloadDataBase.class, "MyToDos").allowMainThreadQueries().build();
    }

    public static synchronized DatabaseClient getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new DatabaseClient(mCtx);
        }
        return mInstance;
    }

    public VideoDownloadDataBase getAppDatabase() {
        return videoDownloadDataBase;
    }
}
