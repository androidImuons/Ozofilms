package com.example.oops.data.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.oops.data.databasevideodownload.VideoDownloadDao;
import com.example.oops.data.databasevideodownload.VideoDownloadDataBase;
import com.example.oops.data.databasevideodownload.VideoDownloadTable;

@Database(entities = {MovieDetailsTable.class},version = 1)
public  abstract  class MovieDownloadDatabase extends RoomDatabase {
    private static MovieDownloadDatabase instance;

    public  abstract MovieDownloadDao movieDownloadDao();

    public  static  synchronized MovieDownloadDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    MovieDownloadDatabase.class,"movie_database")
                    .fallbackToDestructiveMigration().allowMainThreadQueries().build();
        }
        return  instance;
    }
    private  static Callback roomCallback = new Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new MovieDownloadDatabase.PopulateDbAsyncTask(instance).execute();
        }
    };
    private  static  class PopulateDbAsyncTask extends AsyncTask<Void,Void,Void> {
        private MovieDownloadDao movieDownloadDao;
        private  PopulateDbAsyncTask(MovieDownloadDatabase db){
            movieDownloadDao = db.movieDownloadDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {


            return null;
        }
    }
}
