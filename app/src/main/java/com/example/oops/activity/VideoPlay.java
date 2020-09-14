package com.example.oops.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oops.DataClass.CategoryListData;
import com.example.oops.DataClass.MovieDeatilsData;
import com.example.oops.R;
import com.example.oops.ResponseClass.CategoryResponse;
import com.example.oops.ResponseClass.CommonResponse;
import com.example.oops.ResponseClass.MovieDeatilsResponse;
import com.example.oops.Utils.AppCommon;
import com.example.oops.Utils.ViewUtils;
import com.example.oops.adapter.RelatedAdapter;
import com.example.oops.data.database.AppDatabase;
import com.example.oops.data.database.Subtitle;
import com.example.oops.data.database.Video;
import com.example.oops.data.model.VideoSource;
import com.example.oops.retrofit.AppService;
import com.example.oops.retrofit.ServiceGenerator;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoPlay extends AppCompatActivity {
    @BindView(R.id.txtVideoHeading)
    AppCompatTextView txtVideoHeading;
    String stxtVideoHeading;

    @BindView(R.id.txtContentType)
    AppCompatTextView txtContentType;
    @BindView(R.id.txtRate)
    AppCompatTextView txtRate;
    @BindView(R.id.txtVideoType)
    AppCompatTextView txtVideoType;
    @BindView(R.id.txtSoryLine)
    AppCompatTextView txtSoryLine;
    @BindView(R.id.txtCastName)
    AppCompatTextView txtCastName;
    @BindView(R.id.txtDirectorName)
    AppCompatTextView txtDirectorName;
    @BindView(R.id.recylerview)
    RecyclerView recylerview;
    @BindView(R.id.sdvImage)
    SimpleDraweeView sdvImage;
    @BindView(R.id.txtHeading)
    AppCompatTextView  txtHeading;
    @BindView(R.id.imgPlayVideo)
    AppCompatImageView imgPlayVideo;
    private AppDatabase database;
    private List<Subtitle> subtitleList = new ArrayList<>();
    ArrayList<CategoryListData> categoryList;
    RelatedAdapter relatedAdapter;

    private List<Video> videoUriList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_player);
        ButterKnife.bind(this);
        setLayout();
        initializeDb();

        if(getIntent()!= null){
            String movieId = getIntent().getStringExtra("moviesId");
            String name = getIntent().getStringExtra("name");
            txtVideoHeading.setText(name);
            txtHeading.setVisibility(View.GONE);

            callGetMoviesDetails(movieId);
        }

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        database.clearAllTables();
        database = null;

    }
    private void setLayout() {

        categoryList = new ArrayList<>();
        relatedAdapter = new RelatedAdapter(VideoPlay.this , categoryList );
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recylerview.setLayoutManager(mLayoutManager);
        recylerview.setItemAnimator(new DefaultItemAnimator());
        recylerview.setAdapter(relatedAdapter);


        imgPlayVideo.setOnClickListener(view -> goToPlayerActivity(makeVideoSource(videoUriList, 0)));
    }

    private void initializeDb() {
        database = AppDatabase.Companion.getDatabase(getApplicationContext());
    }

    @OnClick(R.id.imgBackPressed)
    public  void setImgBackPressed(){
        onBackPressed();
    }


    private void callGetMoviesDetails(String movieId) {

        if (AppCommon.getInstance(this).isConnectingToInternet(this)) {
            Dialog dialog = ViewUtils.getProgressBar(VideoPlay.this);
            AppCommon.getInstance(this).setNonTouchableFlags(this);
            AppService apiService = ServiceGenerator.createService(AppService.class , AppCommon.getInstance(this).getToken());
            Map<String , String> entityMap = new HashMap<>();
            entityMap.put("id" , String.valueOf(AppCommon.getInstance(this).getId()));
            entityMap.put("userId" , String.valueOf(AppCommon.getInstance(this).getUserId()));
            entityMap.put("movieId" , String.valueOf(movieId));
            Call call = apiService.MOVIE_DEATILS_RESPONSE_CALL(entityMap);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(VideoPlay.this).clearNonTouchableFlags(VideoPlay.this);
                    dialog.dismiss();
                    MovieDeatilsResponse authResponse = (MovieDeatilsResponse) response.body();
                    if (authResponse != null) {
                        Log.i("Response::", new Gson().toJson(authResponse));
                        if (authResponse.getCode() == 200) {
                            if(authResponse.getData() != null)
                            setData(authResponse.getData());
                        } else {

                            Toast.makeText(VideoPlay.this, authResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        AppCommon.getInstance(VideoPlay.this).showDialog(VideoPlay.this, "Server Error");
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    dialog.dismiss();
                    AppCommon.getInstance(VideoPlay.this).clearNonTouchableFlags(VideoPlay.this);
                    // loaderView.setVisibility(View.GONE);
                    Toast.makeText(VideoPlay.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            });


        } else {
            // no internet
            Toast.makeText(this, "Please check your internet", Toast.LENGTH_SHORT).show();
        }
    }
    private void callRelativeMovies(MovieDeatilsData data) {

        if (AppCommon.getInstance(this).isConnectingToInternet(this)) {
            Dialog dialog = ViewUtils.getProgressBar(VideoPlay.this);
            AppCommon.getInstance(this).setNonTouchableFlags(this);
            AppService apiService = ServiceGenerator.createService(AppService.class , AppCommon.getInstance(this).getToken());
            Map<String , String> entityMap = new HashMap<>();
            entityMap.put("id" , String.valueOf(AppCommon.getInstance(this).getId()));
            entityMap.put("userId" , String.valueOf(AppCommon.getInstance(this).getUserId()));
            entityMap.put("movieId" , String.valueOf(data.getMovieId()));
            entityMap.put("category" , String.valueOf(data.getMovieCategory()));
            Call call = apiService.GetRelativeMovies(entityMap);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(VideoPlay.this).clearNonTouchableFlags(VideoPlay.this);
                    dialog.dismiss();
                    CategoryResponse authResponse = (CategoryResponse) response.body();
                    if (authResponse != null) {
                        Log.i("Response::", new Gson().toJson(authResponse));
                        if (authResponse.getCode() == 200) {
                            if(authResponse.getData() != null)
                                if(authResponse.getData().size()!= 0) {
                                    categoryList = authResponse.getData();
                                    relatedAdapter.upadate(authResponse.getData());
                                }
                        } else {

                            Toast.makeText(VideoPlay.this, authResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        AppCommon.getInstance(VideoPlay.this).showDialog(VideoPlay.this, "Server Error");
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    dialog.dismiss();
                    AppCommon.getInstance(VideoPlay.this).clearNonTouchableFlags(VideoPlay.this);
                    // loaderView.setVisibility(View.GONE);
                    Toast.makeText(VideoPlay.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            });


        } else {
            // no internet
            Toast.makeText(this, "Please check your internet", Toast.LENGTH_SHORT).show();
        }
    }

    private void setData(MovieDeatilsData data) {
        callRelativeMovies(data);
        if(data.getMovieLongDescription() != null)
        txtSoryLine.setText(data.getMovieLongDescription());
        else
            txtSoryLine.setText("N/A");

        if(data.getCast() != null)
            txtCastName.setText(data.getCast());
        else
            txtCastName.setText("N/A");

        if(data.getDirector() != null)
            txtDirectorName.setText(data.getDirector());
        else
            txtDirectorName.setText("N/A");

        if(data.getCategoryName() != null)
            txtVideoType.setText(data.getCategoryName());
        else
            txtVideoType.setText("N/A");

        sdvImage.setController(AppCommon.getInstance(this).getDraweeController(sdvImage , data.getBannerLink() , 1024));
        makeListOfUri(data);
    }


    private void makeListOfUri(MovieDeatilsData data) {
        videoUriList.add(new Video(data.getVideoLink() , Long.getLong("zero" , 1)));

        /*videoUriList.add(new Video("https://5b44cf20b0388.streamlock.net:8443/vod/smil:bbb.smil/playlist.m3u8", Long.getLong("zero", 1)));

        subtitleList.add(new Subtitle(2, "German", "https://durian.blender.org/wp-content/content/subtitles/sintel_en.srt"));
        subtitleList.add(new Subtitle(2, "French", "https://durian.blender.org/wp-content/content/subtitles/sintel_fr.srt"));
*/
        if (database.videoDao().getAllUrls().size() == 0) {
            database.videoDao().insertAllVideoUrl(videoUriList);
            database.videoDao().insertAllSubtitleUrl(subtitleList);
        }

    }

    private VideoSource makeVideoSource(List<Video> videos, int index) {
        setVideosWatchLength();
        List<VideoSource.SingleVideo> singleVideos = new ArrayList<>();
        for (int i = 0; i < videos.size(); i++) {

            singleVideos.add(i, new VideoSource.SingleVideo(
                    videos.get(i).getVideoUrl(),
                    database.videoDao().getAllSubtitles(i + 1),
                    videos.get(i).getWatchedLength())
            );

        }
        return new VideoSource(singleVideos, index);
    }

    private List<Video> setVideosWatchLength() {
        List<Video> videosInDb = database.videoDao().getVideos();
        for (int i = 0; i < videosInDb.size(); i++) {
            //videoUriList.get(i).setWatchedLength(videosInDb.get(i).getWatchedLength());
            videoUriList.get(i).setWatchedLength(videosInDb.get(i).getWatchedLength());
        }
        return videoUriList;
    }


    //start player for result due to future features
    public void goToPlayerActivity(VideoSource videoSource) {
        int REQUEST_CODE = 1000;
        Intent intent = new Intent(getApplicationContext(), PlayerActivity.class);
        intent.putExtra("videoSource", videoSource);
        startActivityForResult(intent, REQUEST_CODE);
    }

    public void clickRelativeMovies(int adapterPosition) {
        startActivity(new Intent(this , VideoPlay.class)
                .putExtra("moviesId" , categoryList.get(adapterPosition).getMovieId())
                .putExtra("name" , categoryList.get(adapterPosition).getMovieName()));
    }
}
