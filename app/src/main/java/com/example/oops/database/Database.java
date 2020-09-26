package com.example.oops.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.oops.DataClass.MovieDeatilsData;
import com.facebook.stetho.json.ObjectMapper;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Database extends SQLiteOpenHelper {


    private static final String MOVIEINFO_TABLE_NAME = "MOinfo";


    public Database(Context context)

    {
        super(context, "MovieDetailsDataBase.db", null, 10678);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String tableEmp="create table emp(movieId text,movieName text,imageLink text)";
        db.execSQL(tableEmp);

//        db.execSQL("CREATE TABLE " + EMPINFO_TABLE_NAME + " (" + EMP_ID
//                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + EMP_NAME
//                + " TEXT NOT NULL, " + EMP_DOB + " TEXT NOT NULL, " + EMP_PHONE + " TEXT NOT NULL, " + EMP_ADDR + " TEXT NOT NULL);"
//        );
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
    public void insertData(MovieDeatilsData movieDeatilsData)
    {
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("movieId",movieDeatilsData.getMovieId());
        values.put("movieName",movieDeatilsData.getMovieName());
        values.put("imageLink",movieDeatilsData.getImageLink());

        Log.d("VALUES", "insertData: "+values);

        sqLiteDatabase.insert("emp",null,values);

    }
    public String fetchData()
    {

        Map<String,MovieDeatilsData> movielist= new HashMap<>();

        JSONObject jsonObject= new JSONObject();
        ObjectMapper objectMapper=new ObjectMapper();



         ArrayList<MovieDeatilsData> stringArrayList=new ArrayList<MovieDeatilsData>();
        String fetchdata="select * from emp";
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery(fetchdata, null);


        Log.d("CURSOR_SIZE", "fetchData: "+cursor.getCount());


        if(cursor.moveToFirst()){
            do
            {
                MovieDeatilsData movieDeatilsData=new MovieDeatilsData();
                movieDeatilsData.setMovieId(cursor.getString(0));
                movieDeatilsData.setMovieName(cursor.getString(1));
                movieDeatilsData.setImageLink(cursor.getString(2));

                stringArrayList.add(movieDeatilsData);




                Log.d("SJDSVDOVDOVD", "fetchData: "+stringArrayList);




//                try {
//                    jsonObject.put("movieId",cursor.getString(0));
//                    jsonObject.put("movieName",cursor.getString(1));
//                    jsonObject.put("imageLink",cursor.getString(2));
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }


                //stringArrayList.add(cursor.getString(0));
                //   stringArrayList.add(cursor.getString(1));
                //   stringArrayList.add(cursor.getString(2));
            } while (cursor.moveToNext());
        }

        String json = new Gson().toJson(stringArrayList);

        Log.d("BSBOBDOSOOD", "fetchData: "+json);
        jsonObject= objectMapper.convertValue(movielist,JSONObject.class);
        return json;
    }

    public Cursor getData(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from contacts where movieId="+id+"", null );
        return res;
    }
}