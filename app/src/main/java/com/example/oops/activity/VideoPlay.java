package com.example.oops.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oops.DataClass.CategoryListData;
import com.example.oops.DataClass.MovieDeatilsData;
import com.example.oops.Ooops;
import com.example.oops.R;
import com.example.oops.ResponseClass.CategoryResponse;
import com.example.oops.ResponseClass.CommonResponse;
import com.example.oops.ResponseClass.MovieDeatilsResponse;
import com.example.oops.Utils.AppCommon;
import com.example.oops.Utils.AppUtil;
import com.example.oops.Utils.DemoDownloadService;
import com.example.oops.Utils.DownloadTracker;
import com.example.oops.Utils.TrackKey;
import com.example.oops.Utils.ViewUtils;
import com.example.oops.adapter.RelatedAdapter;
import com.example.oops.data.database.AppDatabase;
import com.example.oops.data.database.MovieDetailsTable;
import com.example.oops.data.database.MovieDownloadDatabase;
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
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.offline.Download;
import com.google.android.exoplayer2.offline.DownloadHelper;
import com.google.android.exoplayer2.offline.DownloadManager;
import com.google.android.exoplayer2.offline.DownloadRequest;
import com.google.android.exoplayer2.offline.DownloadService;
import com.google.android.exoplayer2.source.BehindLiveWindowException;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.util.Util;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
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

import static com.google.android.exoplayer2.offline.Download.STATE_COMPLETED;
import static com.google.android.exoplayer2.offline.Download.STATE_DOWNLOADING;
import static com.google.android.exoplayer2.offline.Download.STATE_FAILED;
import static com.google.android.exoplayer2.offline.Download.STATE_QUEUED;
import static com.google.android.exoplayer2.offline.Download.STATE_REMOVING;
import static com.google.android.exoplayer2.offline.Download.STATE_RESTARTING;
import static com.google.android.exoplayer2.offline.Download.STATE_STOPPED;

public class VideoPlay extends Activity implements View.OnClickListener, DownloadTracker.Listener {
    @BindView(R.id.ll_video_play)
    LinearLayout ll_video_play;
    @BindView(R.id.txtVideoHeading)
    AppCompatTextView txtVideoHeading;
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
    AppCompatTextView txtHeading;
    @BindView(R.id.imgPlayVideo)
    AppCompatImageView imgPlayVideo;
    @BindView(R.id.like)
    ImageView like;

    private AppDatabase database;
    private List<Subtitle> subtitleList = new ArrayList<>();
    ArrayList<CategoryListData> categoryList;
    RelatedAdapter relatedAdapter;
    LinearLayout linearLayout;
    ImageView imgDownload;
    private List<Video> videoUriList = new ArrayList<>();
    ProgressDialog pDialog;
    protected static final CookieManager DEFAULT_COOKIE_MANAGER;
    String movieId, sMsg = "";

    // Saved instance state keys.
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
    private DataSource.Factory dataSourceFactory;
    private DefaultTrackSelector trackSelector;
    private DefaultTrackSelector.Parameters trackSelectorParameters;
    private TrackGroupArray lastSeenTrackGroupArray;

    private boolean startAutoPlay;
    private int startWindow;

    private long startPosition;
    @BindView(R.id.txtMessage)
    AppCompatTextView txtMessage;

    Button btnAbc;

    private DownloadTracker downloadTracker;
    private DownloadManager downloadManager;
    private DownloadHelper myDownloadHelper;


    private String videoUrl;
    private long videoDurationInSeconds;
    private Runnable runnableCode;
    private Handler handler;
    MovieDetailsTable movieDetailsTable;
    MovieDownloadDatabase mdb;
    String subTitle, videoLink, audioLink, addOn, releaseDate, movieName, thumbnailImage, movieType, shortDescription, longDescription, directorName, trailerLink, bannerLink, categoryName, cast;
    Context context;
    int movieCategory;
    String millisInString;
    //    DownloadRequest myDownloadRequest;
    int movieid;
    String sMovie;

    @BindView(R.id.imgBackPressed)
    ImageView imgBackPressed;

    private static boolean isBehindLiveWindow(ExoPlaybackException e) {
        if (e.type != ExoPlaybackException.TYPE_SOURCE) {
            return false;
        }
        Throwable cause = e.getSourceException();
        while (cause != null) {
            if (cause instanceof BehindLiveWindowException) {
                return true;
            }
            cause = cause.getCause();
        }
        return false;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataSourceFactory = buildDataSourceFactory();
        if (CookieHandler.getDefault() != DEFAULT_COOKIE_MANAGER) {
            CookieHandler.setDefault(DEFAULT_COOKIE_MANAGER);
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.video_player);
        ButterKnife.bind(this);
        setLayout();
        initializeDb();

        if (getIntent() != null) {
            movieId = getIntent().getStringExtra("moviesId");
            String name = getIntent().getStringExtra("name");
            sMsg = getIntent().getStringExtra("fav");
            txtVideoHeading.setText(name);
            txtHeading.setVisibility(View.GONE);
            callGetMoviesDetails(movieId);
        }
//        movieid = Integer.parseInt(movieId);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
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
        mdb = MovieDownloadDatabase.getInstance(getApplicationContext());

        try {
            DownloadService.start(this, DemoDownloadService.class);
        } catch (IllegalStateException e) {
            DownloadService.startForeground(this, DemoDownloadService.class);
        }
        handler = new Handler();
        imgDownload = (ImageView) findViewById(R.id.imgDownload);
//        prepareView();
        imgDownload.setOnClickListener(VideoPlay.this);
//        videoDurationInSeconds = MediaPlayer.create(VideoPlay.this, Uri.parse(videoUrl)).getDuration();
//        videoDurationInSeconds = videoDurationInSeconds % 60 ;
        //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // millisInString = dateFormat.format(new Date());
        long time = System.currentTimeMillis();
        millisInString = String.valueOf(time);

        runnableCode = new Runnable() {
            @Override
            public void run() {
//               observerVideoStatus();
                handler.postDelayed(this, 1000);
            }
        };

        handler.post(runnableCode);
        imgBackPressed.setVisibility(View.VISIBLE);


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        database.clearAllTables();
        database = null;

    }

    private void setLayout() {
        categoryList = new ArrayList<>();
        relatedAdapter = new RelatedAdapter(VideoPlay.this, categoryList);
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
    public void setImgBackPressed() {
        onBackPressed();
    }


    private void addAndRemoveLike(boolean selected) {
        if (AppCommon.getInstance(this).isConnectingToInternet(this)) {
            Dialog dialog = ViewUtils.getProgressBar(VideoPlay.this);
            AppCommon.getInstance(this).setNonTouchableFlags(this);
            AppService apiService = ServiceGenerator.createService(AppService.class, AppCommon.getInstance(this).getToken());
            Map<String, String> entityMap = new HashMap<>();
            entityMap.put("id", String.valueOf(AppCommon.getInstance(this).getId()));
            entityMap.put("userId", String.valueOf(AppCommon.getInstance(this).getUserId()));
            entityMap.put("type", String.valueOf("Movie"));
            entityMap.put("serMovId", String.valueOf(movieId));

            Call call = apiService.addAndRemoveFavurite(entityMap);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(VideoPlay.this).clearNonTouchableFlags(VideoPlay.this);
                    dialog.dismiss();
                    CommonResponse authResponse = (CommonResponse) response.body();
                    if (authResponse != null) {
                        Log.i("Test", new Gson().toJson(authResponse));
                        if (authResponse.getCode() == 200) {
                            if (authResponse.getData() != null) {
                                if (authResponse.getMessage().equals("Added To Favourite Successfully")) {
                                    like.setSelected(true);
                                } else {
                                    like.setSelected(false);
                                }
                            }
                        } else {
                            showSnackbar(ll_video_play,authResponse.getMessage(),Snackbar.LENGTH_SHORT);
                        }
                    } else {
                        AppCommon.getInstance(VideoPlay.this).showDialog(VideoPlay.this, "Server Error");
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    dialog.dismiss();
                    AppCommon.getInstance(VideoPlay.this).clearNonTouchableFlags(VideoPlay.this);
                    showSnackbar(ll_video_play,getResources().getString(R.string.ServerError),Snackbar.LENGTH_SHORT);

                    // loaderView.setVisibility(View.GONE);
                }
            });


        } else {
            showSnackbar(ll_video_play,getResources().getString(R.string.NoInternet),Snackbar.LENGTH_SHORT);
        }
    }


    private void callGetMoviesDetails(String movieId) {

        if (AppCommon.getInstance(this).isConnectingToInternet(this)) {
            Dialog dialog = ViewUtils.getProgressBar(VideoPlay.this);
            AppCommon.getInstance(this).setNonTouchableFlags(this);
            AppService apiService = ServiceGenerator.createService(AppService.class, AppCommon.getInstance(this).getToken());
            Map<String, String> entityMap = new HashMap<>();
            entityMap.put("id", String.valueOf(AppCommon.getInstance(this).getId()));
            entityMap.put("userId", String.valueOf(AppCommon.getInstance(this).getUserId()));
            entityMap.put("movieId", String.valueOf(movieId));
            Call call = apiService.MOVIE_DEATILS_RESPONSE_CALL(entityMap);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(VideoPlay.this).clearNonTouchableFlags(VideoPlay.this);
                    dialog.dismiss();
                    MovieDeatilsResponse authResponse = (MovieDeatilsResponse) response.body();
                    if (authResponse != null) {
                        Log.i("Test", new Gson().toJson(authResponse));
                        if (authResponse.getCode() == 200) {
                            if (authResponse.getData() != null)

                            videoUrl = authResponse.getData().getVideoLink();
                            movieName = authResponse.getData().getMovieName();
                            thumbnailImage = authResponse.getData().getImageLink();
                            sMovie = authResponse.getData().getMovieId();
//                            movieid = Integer.parseInt(sMovie);
                            movieType = authResponse.getData().getMovieType();
                            shortDescription = authResponse.getData().getMovieShortDescription();
                            longDescription = authResponse.getData().getMovieLongDescription();
                            directorName = authResponse.getData().getDirector();
                            trailerLink = authResponse.getData().getTrailerLink();
                            bannerLink = authResponse.getData().getBannerLink();
                            categoryName = authResponse.getData().getCategoryName();
                            cast = authResponse.getData().getCast();
                            movieCategory = authResponse.getData().getMovieCategory();
                            videoLink = authResponse.getData().getVideoLink();
                            audioLink = authResponse.getData().getAudioFile();
                            addOn = authResponse.getData().getAddedOn();
                            releaseDate = authResponse.getData().getReleaseDate();
                            subTitle = authResponse.getData().getSubtitles();
                            setData(authResponse.getData());

                        } else {
                            showSnackbar(ll_video_play,authResponse.getMessage(),Snackbar.LENGTH_SHORT);
                        }
                    } else {
                        AppCommon.getInstance(VideoPlay.this).showDialog(VideoPlay.this, "Server Error");
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    dialog.dismiss();
                    AppCommon.getInstance(VideoPlay.this).clearNonTouchableFlags(VideoPlay.this);
                    showSnackbar(ll_video_play,getResources().getString(R.string.ServerError),Snackbar.LENGTH_SHORT);

                    // loaderView.setVisibility(View.GONE);
                }
            });


        } else {
            showSnackbar(ll_video_play,getResources().getString(R.string.NoInternet),Snackbar.LENGTH_SHORT);

        }
    }

    private void callRelativeMovies(MovieDeatilsData data) {

        if (AppCommon.getInstance(this).isConnectingToInternet(this)) {
            Dialog dialog = ViewUtils.getProgressBar(VideoPlay.this);
            AppCommon.getInstance(this).setNonTouchableFlags(this);
            AppService apiService = ServiceGenerator.createService(AppService.class, AppCommon.getInstance(this).getToken());
            Map<String, String> entityMap = new HashMap<>();
            entityMap.put("id", String.valueOf(AppCommon.getInstance(this).getId()));
            entityMap.put("userId", String.valueOf(AppCommon.getInstance(this).getUserId()));
            entityMap.put("movieId", String.valueOf(data.getMovieId()));
            entityMap.put("category", String.valueOf(data.getMovieCategory()));
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
                            if (authResponse.getData() != null)
                                if (authResponse.getData().size() != 0) {
                                    categoryList = authResponse.getData();
                                    relatedAdapter.upadate(authResponse.getData());
                                }
                        } else {
                            if (sMsg.equals("fav")) {
                                txtMessage.setText("No like Related Movie Found");
                                txtMessage.setVisibility(View.VISIBLE);
                            } else if (!sMsg.equals("fav")) {
                                txtMessage.setText("No like Related Movie Found");
                                txtMessage.setVisibility(View.VISIBLE);
                            }

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
                    showSnackbar(ll_video_play,getResources().getString(R.string.ServerError),Snackbar.LENGTH_SHORT);

                }
            });


        } else {
            showSnackbar(ll_video_play,getResources().getString(R.string.NoInternet),Snackbar.LENGTH_SHORT);
        }
    }

    private void setData(MovieDeatilsData data) {

        if (data.getIsFavourite() == 0) {
            like.setSelected(false);

        } else {
            like.setSelected(true);
        }
        if (data.getMovieLongDescription() != null)
            txtSoryLine.setText(data.getMovieLongDescription());
        else
            txtSoryLine.setText("N/A");

        if (data.getCast() != null)
            txtCastName.setText(data.getCast());
        else
            txtCastName.setText("N/A");

        if (data.getDirector() != null)
            txtDirectorName.setText(data.getDirector());
        else
            txtDirectorName.setText("N/A");

        if (data.getCategoryName() != null)
            txtVideoType.setText(data.getCategoryName());
        else
            txtVideoType.setText("N/A");
        if (data.getBannerLink() != null && !data.getBannerLink().equals(""))
            sdvImage.setController(AppCommon.getInstance(this).getDraweeController(sdvImage, data.getBannerLink(), 1024));

        callRelativeMovies(data);
        makeListOfUri(data);
    }


    private void makeListOfUri(MovieDeatilsData data) {
        videoUriList.add(new Video(data.getVideoLink(), Long.getLong("zero", 1)));
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
        intent.putExtra("selectedPosition", "0");
        startActivityForResult(intent, REQUEST_CODE);
    }

    public void clickRelativeMovies(int adapterPosition) {
        startActivity(new Intent(this, VideoPlay.class)
                .putExtra("moviesId", categoryList.get(adapterPosition).getMovieId())
                .putExtra("name", categoryList.get(adapterPosition).getMovieName()));
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

        downloadTracker.addListener(this);


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
                getTasks();
                break;
        }
    }

    private void getTasks() {
        class GetTasks extends AsyncTask<Void, Void, List<VideoDownloadTable>> {

            @Override
            protected List<VideoDownloadTable> doInBackground(Void... voids) {
                List<VideoDownloadTable> taskList = DatabaseClient
                        .getInstance(VideoPlay.this)
                        .getAppDatabase()
                        .videoDownloadDao()
                        .getAll();
                return taskList;
            }

            @Override
            protected void onPostExecute(List<VideoDownloadTable> taskList) {
                super.onPostExecute(taskList);
                boolean downloaded = false;
                if(taskList!=null && taskList.size()!=0){
                    for(int i=0;i<taskList.size();i++){
                        String movieName=taskList.get(i).getMovieName();
                        if(txtVideoHeading.getText().toString().equals(movieName)){
                            downloaded=true;
                        }
                    }

                    if(downloaded){
                        imgDownload.setImageResource(R.drawable.ic_lock);
                        showSnackbar(ll_video_play,getResources().getString(R.string.AvaliableDownloads),Snackbar.LENGTH_SHORT);
                    }else{
                        fetchDownloadOptions();
                    }
                }else{
                    fetchDownloadOptions();
                }
            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }


    private void fetchDownloadOptions() {
        trackKeys.clear();
        if (pDialog == null || !pDialog.isShowing()) {
            pDialog = new ProgressDialog(VideoPlay.this);
            pDialog.setTitle(null);
            pDialog.setCancelable(false);
            pDialog.setMessage("Preparing Download Options...");
            pDialog.show();
        }

        DownloadHelper downloadHelper = DownloadHelper.forHls(VideoPlay.this,
                Uri.parse(videoUrl),
                dataSourceFactory,
                new DefaultRenderersFactory(VideoPlay.this));


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

        AlertDialog.Builder builder = new AlertDialog.Builder(VideoPlay.this, R.style.MyDialogTheme1);
        builder.setTitle(Html.fromHtml("<font color='#FFFFFF'>Select Download Format</font>"));

        int checkedItem = 1;


        for (int i = 0; i < trackKeyss.size(); i++) {
            TrackKey trackKey = trackKeyss.get(i);
            long bitrate = trackKey.getTrackFormat().bitrate;
            long getInBytes = (bitrate * 128) / 8;
            String getInMb = AppUtil.formatFileSize(getInBytes);
            String videoResoultionDashSize = " " + trackKey.getTrackFormat().height + "      (" + getInMb + ")";
            optionsToDownload.add(i, videoResoultionDashSize);
        }

        // Initialize a new array adapter instance
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(
                VideoPlay.this, // Context
                R.layout.mytextview, // Layout
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

        DownloadRequest myDownloadRequest = downloadRequestt;

        //       downloadManager.addDownload(downloadRequestt);

        if (myDownloadRequest.uri.toString().isEmpty()) {
            showSnackbar(ll_video_play,getResources().getString(R.string.TryAgain),Snackbar.LENGTH_SHORT);
            return;
        } else {
            saveTask();
            downloadManager.addDownload(myDownloadRequest);

        }


    }


    private void initializePlayer() {


        TrackSelection.Factory trackSelectionFactory = new AdaptiveTrackSelection.Factory();

        //    DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(this, null, DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER);
        RenderersFactory renderersFactory = ((Ooops) getApplication()).buildRenderersFactory(true);

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

                // Important: These constants are persisted into DownloadIndex. Do not change them.
                /**
                 * The download is waiting to be started. A download may be queued because the {@link
                 * DownloadManager}
                 *
                 * <ul>
                 *   <li>Is {@link DownloadManager#getDownloadsPaused() paused}
                 *   <li>Has {@link DownloadManager#getRequirements() Requirements} that are not met
                 *   <li>Has already started {@link DownloadManager#getMaxParallelDownloads()
                 *       maxParallelDownloads}
                 * </ul>
                 */
                break;
            case STATE_STOPPED:
                showSnackbar(ll_video_play,"The download is stopped for a specified "+download.stopReason+".",Snackbar.LENGTH_SHORT);
                break;
            case STATE_DOWNLOADING:
                showSnackbar(ll_video_play,getResources().getString(R.string.STATE_DOWNLOADING),Snackbar.LENGTH_SHORT);
                break;
            case STATE_COMPLETED:
                showSnackbar(ll_video_play,getResources().getString(R.string.STATE_COMPLETED),Snackbar.LENGTH_SHORT);
                if (download.request.uri.toString().equals(videoUrl)) {
                    imgDownload.setImageResource(R.drawable.ic_lock);
                }
                break;
            case STATE_FAILED:
                showSnackbar(ll_video_play,getResources().getString(R.string.STATE_FAILED),Snackbar.LENGTH_SHORT);
                break;
            case STATE_REMOVING:
                showSnackbar(ll_video_play,getResources().getString(R.string.STATE_REMOVING),Snackbar.LENGTH_SHORT);
                break;
            case STATE_RESTARTING:
                showSnackbar(ll_video_play,getResources().getString(R.string.STATE_RESTARTING),Snackbar.LENGTH_SHORT);
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
                VideoDownloadTable task = new VideoDownloadTable();
                task.setTimestamp(millisInString);
                task.setMovieId(movieId);
                task.setMovieName(movieName);
                task.setMovieType(categoryName);
                task.setUrlVideo(videoUrl);
                task.setMovieDescription(shortDescription);
                task.setUrlImage(thumbnailImage);
                Log.i("SUNIL!", videoUrl);
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .videoDownloadDao()
                        .insert(task);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
               // showSnackbar(ll_video_play,"Saved in Downloads",Snackbar.LENGTH_SHORT);
            }
        }

        SaveTask st = new SaveTask();
        st.execute();
    }

    @OnClick(R.id.like)
    public void setLike() {
        if (like.isSelected()) {
            like.setSelected(false);
        } else
            like.setSelected(true);
        addAndRemoveLike(like.isSelected());
    }


    public void showSnackbar(View view, String message, int duration)
    {
        Snackbar snackbar= Snackbar.make(view, message, duration);
        snackbar.setActionTextColor(Color.WHITE);
        snackbar.setBackgroundTint(getResources().getColor(R.color.colorPrimaryDark));
        snackbar.show();
    }
}
