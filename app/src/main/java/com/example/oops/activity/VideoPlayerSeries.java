package com.example.oops.activity;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oops.DataClass.EpisodeData;
import com.example.oops.DataClass.SeasonData;
import com.example.oops.Ooops;
import com.example.oops.R;
import com.example.oops.ResponseClass.CommonResponse;
import com.example.oops.ResponseClass.EpisodeResponse;
import com.example.oops.ResponseClass.SeasonResponse;
import com.example.oops.Utils.AppCommon;
import com.example.oops.Utils.AppUtil;
import com.example.oops.Utils.DemoDownloadService;
import com.example.oops.Utils.DownloadTracker;
import com.example.oops.Utils.RecyclerItemClickListener;
import com.example.oops.Utils.TrackKey;
import com.example.oops.Utils.ViewUtils;
import com.example.oops.adapter.EpisodeAdapter;
import com.example.oops.adapter.SeasonAdapter;
import com.example.oops.data.database.AppDatabase;
import com.example.oops.data.database.Subtitle;
import com.example.oops.data.database.Video;
import com.example.oops.data.databasevideodownload.DatabaseClient;
import com.example.oops.data.databasevideodownload.VideoDownloadTable;
import com.example.oops.data.model.VideoSource;
import com.example.oops.retrofit.AppService;
import com.example.oops.retrofit.ServiceGenerator;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.offline.Download;
import com.google.android.exoplayer2.offline.DownloadHelper;
import com.google.android.exoplayer2.offline.DownloadManager;
import com.google.android.exoplayer2.offline.DownloadRequest;
import com.google.android.exoplayer2.offline.DownloadService;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.android.exoplayer2.offline.Download.STATE_COMPLETED;
import static com.google.android.exoplayer2.offline.Download.STATE_DOWNLOADING;
import static com.google.android.exoplayer2.offline.Download.STATE_FAILED;
import static com.google.android.exoplayer2.offline.Download.STATE_QUEUED;
import static com.google.android.exoplayer2.offline.Download.STATE_REMOVING;
import static com.google.android.exoplayer2.offline.Download.STATE_RESTARTING;
import static com.google.android.exoplayer2.offline.Download.STATE_STOPPED;

public class VideoPlayerSeries extends Activity implements View.OnClickListener, DownloadTracker.Listener{

    @BindView(R.id.txtVideoHeading)
    AppCompatTextView txtVideoHeading;

    @BindView(R.id.txtSoryLine)
    AppCompatTextView txtSoryLine;

    @BindView(R.id.recylerview)
    RecyclerView recylerview;

    @BindView(R.id.seasonRecycleView)
    RecyclerView seasonRecycleView;

    @BindView(R.id.sdvImage)
    SimpleDraweeView sdvImage;


    @BindView(R.id.seasonList)
    RelativeLayout seasonList;

    @BindView(R.id.seasonbtn)
    TextView seasonbtn;
     @BindView(R.id.like)
     ImageView like;
     @BindView(R.id.imgPlayVideo)
             ImageView imgPlayVideo;

    private List<Video> videoUriList = new ArrayList<>();


    /*@BindView(R.id.seasonSpinner)
    Spinner seasonSpinner;*/
    private List<Subtitle> subtitleList = new ArrayList<>();

    ArrayList<SeasonData> data;
    ArrayAdapter<SeasonData> adapter;
    ArrayList<EpisodeData> episodeDataArrayList;
    EpisodeAdapter episodeAdapter;
    SeasonAdapter seasonAdapter;
    private AppDatabase database;
    String videoUrl,episodeNo,episodeThumnailImage,episodeId,stringPosition;
    @BindView(R.id.imgBackPressed)
    AppCompatImageView imgBackPressed;
    ImageView imgDownload;
    protected static final CookieManager DEFAULT_COOKIE_MANAGER;
    private static final String KEY_TRACK_SELECTOR_PARAMETERS = "track_selector_parameters";
    private static final String KEY_WINDOW = "window";
    private static final String KEY_POSITION = "position";
    private static final String KEY_AUTO_PLAY = "auto_play";
    static {
        DEFAULT_COOKIE_MANAGER = new CookieManager();
        DEFAULT_COOKIE_MANAGER.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER);
    }


    List<TrackKey> trackKeys = new ArrayList<>();
    List<String> optionsToDownload = new ArrayList<String>();

    DefaultTrackSelector.Parameters qualityParams;

    ProgressDialog pDialog;

    private DataSource.Factory dataSourceFactory;


    private DefaultTrackSelector trackSelector;

    private DefaultTrackSelector.Parameters trackSelectorParameters;
    private TrackGroupArray lastSeenTrackGroupArray;


    private boolean startAutoPlay;
    private int startWindow;

    private long startPosition;


    Button btnAbc;
    private Runnable runnableCode;
    private DownloadTracker downloadTracker;
    private DownloadManager downloadManager;
    private DownloadHelper myDownloadHelper;
    String movieId,json,movieId1;
    String millisInString,name,storyDescription;
    private Handler handler;
    ArrayList<ArrayList<EpisodeData>> list;
    String thumbnailImage,categoryName;
    String see;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataSourceFactory = buildDataSourceFactory();
        if (CookieHandler.getDefault() != DEFAULT_COOKIE_MANAGER) {
            CookieHandler.setDefault(DEFAULT_COOKIE_MANAGER);
        }
        setContentView(R.layout.activity_video_player_series);
        ButterKnife.bind(this);
        data = new ArrayList<>();

        if (getIntent() != null) {
             movieId = getIntent().getStringExtra("seriesId");
             name = getIntent().getStringExtra("name");
            txtVideoHeading.setText(name);
            movieId1 = String.valueOf(movieId);
            callGetSessionApi(movieId);
        }
        setLayout();
        initializeDb();

        if (savedInstanceState != null) {
            trackSelectorParameters = savedInstanceState.getParcelable(KEY_TRACK_SELECTOR_PARAMETERS);
            startAutoPlay = savedInstanceState.getBoolean(KEY_AUTO_PLAY);
            startWindow = savedInstanceState.getInt(KEY_WINDOW);
            startPosition = savedInstanceState.getLong(KEY_POSITION);
        } else {
            trackSelectorParameters = new DefaultTrackSelector.ParametersBuilder().build();
            clearStartPosition();
        }

        Ooops application = (Ooops) getApplication();
        downloadTracker = application.getDownloadTracker();
        downloadManager = application.getDownloadManager();


        try {
            DownloadService.start(this, DemoDownloadService.class);
        } catch (IllegalStateException e) {
            DownloadService.startForeground(this, DemoDownloadService.class);
        }
        handler = new Handler();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        millisInString  = dateFormat.format(new Date());


        imgDownload = (ImageView) findViewById(R.id.imgDownload);
//        prepareView();
        imgDownload.setOnClickListener(VideoPlayerSeries.this);
        runnableCode = new Runnable() {
            @Override
            public void run() {
//               observerVideoStatus();
                handler.postDelayed(this, 1000);
            }
        };

        handler.post(runnableCode);



        //getInit();
    }
@OnClick(R.id.imgBackPressed)
public void setImgBackPressed(){
        onBackPressed();
}
    private void setLayout() {

        episodeDataArrayList = new ArrayList<>();
        episodeAdapter = new EpisodeAdapter(this, getApplicationContext(),episodeDataArrayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recylerview.setLayoutManager(mLayoutManager);
        recylerview.setItemAnimator(new DefaultItemAnimator());
        recylerview.setAdapter(episodeAdapter);

        data = new ArrayList<>();
        seasonAdapter = new SeasonAdapter(this, data);
        RecyclerView.LayoutManager mLayoutManager1 = new GridLayoutManager(this ,3 , RecyclerView.VERTICAL , false);
        seasonRecycleView.setLayoutManager(mLayoutManager1);
        seasonRecycleView.setItemAnimator(new DefaultItemAnimator());
        seasonRecycleView.setAdapter(seasonAdapter);
        imgPlayVideo.setOnClickListener(view -> goToPlayerActivity(makeVideoSource(videoUriList, 0)));

    }

    private void initializeDb() {
        database = AppDatabase.Companion.getDatabase(getApplicationContext());
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        database.clearAllTables();
        database = null;

    }
    private void callGetEpisodeList(int position) {
        if (AppCommon.getInstance(this).isConnectingToInternet(this)) {
            Dialog dialog = ViewUtils.getProgressBar(VideoPlayerSeries.this);
            AppCommon.getInstance(this).setNonTouchableFlags(this);
            AppService apiService = ServiceGenerator.createService(AppService.class, AppCommon.getInstance(this).getToken());
            Map<String, String> entityMap = new HashMap<>();
            entityMap.put("id", String.valueOf(AppCommon.getInstance(this).getId()));
            entityMap.put("userId", String.valueOf(AppCommon.getInstance(this).getUserId()));
            entityMap.put("seasonId", String.valueOf(data.get(position).getSeasonId()));
            Call call = apiService.getEdpisodes(entityMap);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(VideoPlayerSeries.this).clearNonTouchableFlags(VideoPlayerSeries.this);
                    dialog.dismiss();
                    EpisodeResponse authResponse = (EpisodeResponse) response.body();
                    if (authResponse != null) {
                        Log.i("Test", new Gson().toJson(authResponse));
                        if (authResponse.getCode() == 200) {
                            if (authResponse.getData() != null) {
                                setDataEpisode(authResponse.getData());
                                 see = data.get(position).getSeasonId();
//                                List<Epis>

                            }
                               /* setData(authResponse.getData());
                            videoUrl= authResponse.getData().getVideoLink();*/


                        } else {

                            Toast.makeText(VideoPlayerSeries.this, authResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        AppCommon.getInstance(VideoPlayerSeries.this).showDialog(VideoPlayerSeries.this, "Server Error");
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    dialog.dismiss();
                    AppCommon.getInstance(VideoPlayerSeries.this).clearNonTouchableFlags(VideoPlayerSeries.this);
                    // loaderView.setVisibility(View.GONE);
                    Toast.makeText(VideoPlayerSeries.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            });


        } else {
            // no internet
            Toast.makeText(this, "Please check your internet", Toast.LENGTH_SHORT).show();
        }
    }

    private void setDataEpisode(ArrayList<EpisodeData> data) {

        episodeDataArrayList = data;
        episodeAdapter.update(episodeDataArrayList);

       json = new Gson().toJson(episodeDataArrayList);


        Log.d("SDOOSDVVD", "setDataEpisode: "+episodeDataArrayList);

        makeListOfUri(data);
    }


    private void makeListOfUri(ArrayList<EpisodeData> data) {


        recylerview.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // TODO Handle item click
                        stringPosition = String.valueOf(position);
                      episodeNo   = String.valueOf(episodeDataArrayList.get(position).getEpisodeNo());
                      Intent i = new Intent(getApplicationContext(),EpisodePlayActivity.class);
                      i.putExtra("videourl",episodeDataArrayList.get(position).getVideoLink());
                      i.putExtra("name",name);
                      i.putExtra("episodeThumnailImage",episodeDataArrayList.get(position).getThumbnailLink());
                        i.putExtra("episodeNo",episodeNo);
                        i.putExtra("episodeId",episodeDataArrayList.get(position).getEpisodeId());
                      i.putExtra("storyDescription",storyDescription);
                      i.putExtra("sessionID",see);
                      i.putExtra("Abv",stringPosition);
                        i.putExtra("Json",json);



                        startActivity(i);

                    }
                }));
        videoUriList.add(new Video(videoUrl , Long.getLong("zero" , 1)));

        /*videoUriList.add(new Video("https://5b44cf20b0388.streamlock.net:8443/vod/smil:bbb.smil/playlist.m3u8", Long.getLong("zero", 1)));

        subtitleList.add(new Subtitle(2, "German", "https://durian.blender.org/wp-content/content/subtitles/sintel_en.srt"));
        subtitleList.add(new Subtitle(2, "French", "https://durian.blender.org/wp-content/content/subtitles/sintel_fr.srt"));
*/
        if (database.videoDao().getAllUrls().size() == 0) {
            database.videoDao().insertAllVideoUrl(videoUriList);
    database.videoDao().insertAllSubtitleUrl(subtitleList);
        }

    }

    private void getInit() {

     /*   adapter =
                new ArrayAdapter<SeasonData>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        seasonSpinner.setAdapter(adapter);
        seasonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View v,
                                       int postion, long arg3) {
                // TODO Auto-generated method stub

                callGetEpisodeList(postion);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

     */
    }

    private void callGetSessionApi(String movieId) {
        if (AppCommon.getInstance(this).isConnectingToInternet(this)) {
            Dialog dialog = ViewUtils.getProgressBar(VideoPlayerSeries.this);
            AppCommon.getInstance(this).setNonTouchableFlags(this);
            AppService apiService = ServiceGenerator.createService(AppService.class, AppCommon.getInstance(this).getToken());
            Map<String, String> entityMap = new HashMap<>();
            entityMap.put("id", String.valueOf(AppCommon.getInstance(this).getId()));
            entityMap.put("userId", String.valueOf(AppCommon.getInstance(this).getUserId()));
            entityMap.put("seriesId", String.valueOf(movieId));
            Call call = apiService.getSeasons(entityMap);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(VideoPlayerSeries.this).clearNonTouchableFlags(VideoPlayerSeries.this);
                    dialog.dismiss();
                    SeasonResponse authResponse = (SeasonResponse) response.body();
                    if (authResponse != null) {
                        Log.i("Test", new Gson().toJson(authResponse));
                        if (authResponse.getCode() == 200) {
                            if (authResponse.getData() != null) {
                                setData(authResponse.getData());
                            }
                               /* setData(authResponse.getData());
                            videoUrl= authResponse.getData().getVideoLink();*/


                        } else {

                            Toast.makeText(VideoPlayerSeries.this, authResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        AppCommon.getInstance(VideoPlayerSeries.this).showDialog(VideoPlayerSeries.this, "Server Error");
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    dialog.dismiss();
                    AppCommon.getInstance(VideoPlayerSeries.this).clearNonTouchableFlags(VideoPlayerSeries.this);
                    // loaderView.setVisibility(View.GONE);
                    Toast.makeText(VideoPlayerSeries.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            });


        } else {
            // no internet
            Toast.makeText(this, "Please check your internet", Toast.LENGTH_SHORT).show();
        }
    }

    private void setData(ArrayList<SeasonData> data) {
        this.data = data;
        getInit();
        seasonAdapter.update(data);
        sdvImage.setImageURI(data.get(0).getThumbnailLink());
        txtSoryLine.setText(data.get(0).getSeasonDetails());
        for(int i=0;i<data.size();i++){

             thumbnailImage = data.get(i).getThumbnailLink();
            storyDescription = data.get(i).getSeasonDetails();


        }

        seasonbtn.setText("Season "+data.get(0).getSeasonNo());
        callGetEpisodeList(0);        //   adapter.notifyDataSetChanged();
    }

    public void setSeasonClick(int adapterPosition) {
        callGetEpisodeList(adapterPosition);
        seasonList.setVisibility(View.GONE);
        seasonbtn.setText("Season "+data.get(adapterPosition).getSeasonNo());
    }

    @OnClick(R.id.seasonbtn)
    void setClick(){
        seasonList.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.like)
    void  setLike(){
        if(like.isSelected()){
            like.setSelected(false);

        }else
            like.setSelected(true);
        addAndRemoveLike(like.isSelected());
    }

    private void addAndRemoveLike(boolean selected) {
        if (AppCommon.getInstance(this).isConnectingToInternet(this)) {
            Dialog dialog = ViewUtils.getProgressBar(VideoPlayerSeries.this);
            AppCommon.getInstance(this).setNonTouchableFlags(this);
            AppService apiService = ServiceGenerator.createService(AppService.class, AppCommon.getInstance(this).getToken());
            Map<String, String> entityMap = new HashMap<>();
            entityMap.put("id", String.valueOf(AppCommon.getInstance(this).getId()));
            entityMap.put("userId", String.valueOf(AppCommon.getInstance(this).getUserId()));
            entityMap.put("type", String.valueOf("Series"));
            entityMap.put("serMovId", String.valueOf(movieId));

            Call call = apiService.addAndRemoveFavurite(entityMap);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(VideoPlayerSeries.this).clearNonTouchableFlags(VideoPlayerSeries.this);
                    dialog.dismiss();
                    CommonResponse authResponse = (CommonResponse) response.body();
                    if (authResponse != null) {
                        Log.i("Test", new Gson().toJson(authResponse));
                        if (authResponse.getCode() == 200) {
                            if (authResponse.getData() != null) {
                                if(authResponse.getMessage().equals("Added To Favourite Successfully")){
                                    like.setSelected(true);
                                }else {
                                    like.setSelected(false);
                                }
                            }
                               /* setData(authResponse.getData());
                            videoUrl= authResponse.getData().getVideoLink();*/


                        } else {

                            Toast.makeText(VideoPlayerSeries.this, authResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        AppCommon.getInstance(VideoPlayerSeries.this).showDialog(VideoPlayerSeries.this, "Server Error");
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    dialog.dismiss();
                    AppCommon.getInstance(VideoPlayerSeries.this).clearNonTouchableFlags(VideoPlayerSeries.this);
                    // loaderView.setVisibility(View.GONE);
                    Toast.makeText(VideoPlayerSeries.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            });


        } else {
            // no internet
            Toast.makeText(this, "Please check your internet", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        releasePlayer();
        clearStartPosition();
        setIntent(intent);




    }

    @Override
    public void onStart() {
        super.onStart();

        downloadTracker.addListener(VideoPlayerSeries.this);


        if (Util.SDK_INT > 23) {
            initializePlayer();
            setProgress();

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23) {
            initializePlayer();
            setProgress();


        }


    }

    @Override
    public void onPause() {
        super.onPause();

    }
    @Override
    public void onStop() {
        super.onStop();
        downloadTracker.removeListener(this);
        handler.removeCallbacks(runnableCode);


    }









    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public void onClick(View view) {

        switch (view.getId()) {



            case R.id.imgDownload:
//                if (DatabaseClient.getInstance(this).getAppDatabase().videoDownloadDao().isDataExist(movieid. )) {
                fetchDownloadOptions();

                // data not exist.
//                    Toast.makeText(VideoPlay.this," Not Data Exist"+movieid +"  " +sMovie,Toast.LENGTH_SHORT).show();

//                } else {
//                    // data already exist.
//                    Toast.makeText(VideoPlay.this," Exist" +movieid +"  " +sMovie,Toast.LENGTH_SHORT).show();
//                }


//


                break;

        }


    }







    private void fetchDownloadOptions() {
        trackKeys.clear();

        if (pDialog == null || !pDialog.isShowing()) {
            pDialog = new ProgressDialog(VideoPlayerSeries.this);
            pDialog.setTitle(null);
            pDialog.setCancelable(false);
            pDialog.setMessage("Preparing Download Options...");
            pDialog.show();
            Log.i("DHHDHDH",videoUrl);
        }


        DownloadHelper downloadHelper = DownloadHelper.forHls(VideoPlayerSeries.this, Uri.parse(videoUrl), dataSourceFactory, new DefaultRenderersFactory(VideoPlayerSeries.this));


        downloadHelper.prepare(new DownloadHelper.Callback() {
            @Override
            public void onPrepared(DownloadHelper helper) {
                // Preparation completes. Now other DownloadHelper methods can be called.
                myDownloadHelper = helper;
                for (int i = 0; i < helper.getPeriodCount(); i++) {
                    TrackGroupArray trackGroups = helper.getTrackGroups(i);
                    for (int j = 0; j < trackGroups.length; j++) {
                        TrackGroup trackGroup = trackGroups.get(j);
                        for (int k = 0; k < trackGroup.length; k++) {
                            Format track = trackGroup.getFormat(k);
                            if (shouldDownload(track)) {
                                trackKeys.add(new TrackKey(trackGroups, trackGroup, track));
                            }
                        }
                    }
                }



                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }


                optionsToDownload.clear();
                showDownloadOptionsDialog(myDownloadHelper, trackKeys);
            }

            @Override
            public void onPrepareError(DownloadHelper helper, IOException e) {

            }
        });
    }


    private void showDownloadOptionsDialog(DownloadHelper helper, List<TrackKey> trackKeyss) {

        if (helper == null) {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(VideoPlayerSeries.this);
        builder.setTitle("Select Download Format");
        int checkedItem = 1;


        for (int i = 0; i < trackKeyss.size(); i++) {
            TrackKey trackKey = trackKeyss.get(i);
            long bitrate = trackKey.getTrackFormat().bitrate;
            long getInBytes =  (bitrate * 128)/8;
            String getInMb = AppUtil.formatFileSize(getInBytes);
            String videoResoultionDashSize =  " "+trackKey.getTrackFormat().height +"      ("+getInMb+")";
            optionsToDownload.add(i, videoResoultionDashSize);
        }

        // Initialize a new array adapter instance
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(
                VideoPlayerSeries.this, // Context
                android.R.layout.simple_list_item_single_choice, // Layout
                optionsToDownload // List
        );

        TrackKey trackKey = trackKeyss.get(0);
        qualityParams = ((DefaultTrackSelector) trackSelector).getParameters().buildUpon()
                .setMaxVideoSize(trackKey.getTrackFormat().width, trackKey.getTrackFormat().height)
                .setMaxVideoBitrate(trackKey.getTrackFormat().bitrate)
                .build();

        builder.setSingleChoiceItems(arrayAdapter, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                TrackKey trackKey = trackKeyss.get(i);

                qualityParams = ((DefaultTrackSelector) trackSelector).getParameters().buildUpon()
                        .setMaxVideoSize(trackKey.getTrackFormat().width, trackKey.getTrackFormat().height)
                        .setMaxVideoBitrate(trackKey.getTrackFormat().bitrate)
                        .build();



            }
        });
        // Set the a;ert dialog positive button
        builder.setPositiveButton("Download", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {


                for (int periodIndex = 0; periodIndex < helper.getPeriodCount(); periodIndex++) {
                    MappingTrackSelector.MappedTrackInfo mappedTrackInfo = helper.getMappedTrackInfo(/* periodIndex= */ periodIndex);
                    helper.clearTrackSelections(periodIndex);
                    for (int i = 0; i < mappedTrackInfo.getRendererCount(); i++) {
//                        TrackGroupArray rendererTrackGroups = mappedTrackInfo.getTrackGroups(i);
                        helper.addTrackSelection(
                                periodIndex,
                                qualityParams);
                    }

                }



                DownloadRequest downloadRequest = helper.getDownloadRequest(Util.getUtf8Bytes(videoUrl));
                if (downloadRequest.streamKeys.isEmpty()) {
                    // All tracks were deselected in the dialog. Don't start the download.
                    return;
                }


                startDownload(downloadRequest);


                dialogInterface.dismiss();

            }
        });

        AlertDialog dialog = builder.create();
        dialog.setCancelable(true);
        dialog.show();
    }

    private void startDownload(DownloadRequest downloadRequestt) {

        DownloadRequest   myDownloadRequest = downloadRequestt;

        //       downloadManager.addDownload(downloadRequestt);

        if (myDownloadRequest.uri.toString().isEmpty()) {
            Toast.makeText(this, "Try Again!!", Toast.LENGTH_SHORT).show();

            return;
        } else {



            saveTask();
            downloadManager.addDownload(myDownloadRequest);

        }


    }



    private void initializePlayer() {


        TrackSelection.Factory trackSelectionFactory = new AdaptiveTrackSelection.Factory();

        //    DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(this, null, DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER);
        RenderersFactory renderersFactory =  ((Ooops) getApplication()).buildRenderersFactory(true)  ;

        trackSelector = new DefaultTrackSelector(trackSelectionFactory);
        trackSelector.setParameters(trackSelectorParameters);
        lastSeenTrackGroupArray = null;

        DefaultAllocator defaultAllocator = new DefaultAllocator(true, C.DEFAULT_BUFFER_SEGMENT_SIZE);

        DefaultLoadControl defaultLoadControl = new DefaultLoadControl(defaultAllocator,
                DefaultLoadControl.DEFAULT_MIN_BUFFER_MS,
                DefaultLoadControl.DEFAULT_MAX_BUFFER_MS,
                DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_MS,
                DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS,
                DefaultLoadControl.DEFAULT_TARGET_BUFFER_BYTES,
                DefaultLoadControl.DEFAULT_PRIORITIZE_TIME_OVER_SIZE_THRESHOLDS
        );







    }

    private boolean shouldDownload(Format track) {
        return track.height != 240 && track.sampleMimeType.equalsIgnoreCase("video/avc");
    }




    /**
     * Returns a new DataSource factory.
     */
    private DataSource.Factory buildDataSourceFactory() {
        return ((Ooops) getApplication()).buildDataSourceFactory();
    }






    private void setProgress() {


        handler = new Handler();
        //Make sure you update Seekbar on UI thread
        handler.post(new Runnable() {

            @Override
            public void run() {



                handler.postDelayed(this, 1000);

            }
        });
    }









    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();

    }











    @Override
    public void onDownloadsChanged(Download download) {
        switch (download.state) {
            case STATE_QUEUED:
//                imgDownload.setImageResource(R.drawable.app_setting);
//                imgDownload.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Toast.makeText(VideoPlay.this,"queue",Toast.LENGTH_SHORT).show();
//                    }
//                });
                break;

            case STATE_STOPPED:


                break;
            case STATE_DOWNLOADING:

//                imgDownload.setImageResource(R.drawable.ic_logout);
//                imgDownload.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Toast.makeText(VideoPlay.this,"Video is added in downloading",Toast.LENGTH_SHORT).show();
//                    }
//                });

                Log.d("EXO DOWNLOADING ", +download.getBytesDownloaded() + " " + download.contentLength);
                Log.d("EXO  DOWNLOADING ", "" + download.getPercentDownloaded());


                break;
            case STATE_COMPLETED:

//imgDownload.setImageResource(R.drawable.country_icon);
//                imgDownload.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Toast.makeText(VideoPlay.this,"download Completed",Toast.LENGTH_SHORT).show();
//                    }
//                });
//                progressBarPercentage.setVisibility(View.GONE);


                Log.d("EXO COMPLETED ", +download.getBytesDownloaded() + " " + download.contentLength);
                Log.d("EXO  COMPLETED ", "" + download.getPercentDownloaded());


                if(download.request.uri.toString().equals(videoUrl)){

                    imgDownload.setImageResource(R.drawable.ic_lock);
                }



                break;

            case STATE_FAILED:


                break;

            case STATE_REMOVING:


                break;

            case STATE_RESTARTING:

                break;

        }

    }

    private void releasePlayer() {

        updateTrackSelectorParameters();



        trackSelector = null;


    }

    private void updateTrackSelectorParameters() {
        if (trackSelector != null) {
            trackSelectorParameters = trackSelector.getParameters();
        }
    }



    private void clearStartPosition() {
        startAutoPlay = true;
        startWindow = C.INDEX_UNSET;
        startPosition = C.TIME_UNSET;
    }







    private void saveTask() {


        class SaveTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                //creating a task
                VideoDownloadTable task = new VideoDownloadTable();
                task.setTimestamp(millisInString);
                task.setMovieId(movieId);
                task.setMovieName(name);
                task.setMovieDescription(storyDescription);
                task.setMovieType("Web Series");
                task.setUrlVideo(videoUrl);
                task.setUrlImage(thumbnailImage);
                Log.i("SUNIL!",videoUrl);
                //adding to database
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .videoDownloadDao()
                        .insert(task);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
//                finish();
//                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
            }
        }

        SaveTask st = new SaveTask();
        st.execute();
    }






}