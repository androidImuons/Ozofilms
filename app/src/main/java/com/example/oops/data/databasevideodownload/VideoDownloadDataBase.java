package com.example.oops.data.databasevideodownload;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {VideoDownloadTable.class},version = 1)
public  abstract class VideoDownloadDataBase extends RoomDatabase {
    private static VideoDownloadDataBase instance;

    public  abstract VideoDownloadDao videoDownloadDao();

    public  static  synchronized VideoDownloadDataBase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    VideoDownloadDataBase.class,"video_database")
                    .fallbackToDestructiveMigration().build();
        }
        return  instance;
    }
    private  static Callback roomCallback = new Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };
private  static  class PopulateDbAsyncTask extends AsyncTask<Void,Void,Void>{
private VideoDownloadDao videoDownloadDao;
private  PopulateDbAsyncTask(VideoDownloadDataBase db){
    videoDownloadDao = db.videoDownloadDao();
}
    @Override
    protected Void doInBackground(Void... voids) {
    videoDownloadDao.insert(new VideoDownloadTable(12444,"dfg","Abc","fhfhhf","gggg"));
        videoDownloadDao.insert(new VideoDownloadTable(12445,"dfg1","Abc1","fhfhhf1","gggg1"));
        videoDownloadDao.insert(new VideoDownloadTable(12446,"dfg2","Abc2","fhfhhf2","gggg2"));
        return null;
    }
}
}
