package com.example.oops.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.oops.DataClass.EpisodeData;
import com.example.oops.Ooops;
import com.example.oops.R;
import com.example.oops.ResponseClass.EpisodeResponse;
import com.example.oops.Utils.AppCommon;
import com.example.oops.Utils.AppUtil;
import com.example.oops.Utils.DemoDownloadService;
import com.example.oops.Utils.DownloadTracker;
import com.example.oops.Utils.RecyclerItemClickListener;
import com.example.oops.Utils.TrackKey;
import com.example.oops.Utils.ViewUtils;
import com.example.oops.adapter.EpisodeAdapter;
import com.example.oops.data.database.AppDatabase;
import com.example.oops.data.database.Subtitle;
import com.example.oops.data.database.Video;
import com.example.oops.data.databasevideodownload.DatabaseClient;
import com.example.oops.data.databasevideodownload.VideoDownloadTable;
import com.example.oops.data.model.VideoSource;
import com.example.oops.retrofit.AppService;
import com.example.oops.retrofit.ServiceGenerator;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
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

public class EpisodePlayActivity extends AppCompatActivity implements View.OnClickListener, DownloadTracker.Listener {


    ProgressDialog pDialog;
    protected static final CookieManager DEFAULT_COOKIE_MANAGER;
    DownloadHelper downloadHelper;
    // Saved instance state keys.
    private static final String KEY_TRACK_SELECTOR_PARAMETERS = "track_selector_parameters";
    private static final String KEY_WINDOW = "window";
    private static final String KEY_POSITION = "position";
    private static final String KEY_AUTO_PLAY = "auto_play";
    ImageView img;
    static {
        DEFAULT_COOKIE_MANAGER = new CookieManager();
        DEFAULT_COOKIE_MANAGER.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER);
    }



    DownloadRequest downloadRequest;
    LinearLayout llParentContainer;

    List<TrackKey> trackKeys = new ArrayList<>();
    List<String> optionsToDownload = new ArrayList<String>();

    List<EpisodeData> videoListOfUri =new ArrayList<>();

    DefaultTrackSelector.Parameters qualityParams;

    @BindView(R.id.recylerview)
    RecyclerView recylerview;

    private DataSource.Factory dataSourceFactory;
    ArrayList<EpisodeData> episodeDataArrayList;

    private DefaultTrackSelector trackSelector;

    private DefaultTrackSelector.Parameters trackSelectorParameters;
    private TrackGroupArray lastSeenTrackGroupArray;

    private boolean startAutoPlay;
    private int startWindow;

    private long startPosition;


    Button btnAbc;
    String millisInString;

    private DownloadTracker downloadTracker;
    private DownloadManager downloadManager;
    private DownloadHelper myDownloadHelper;



    private String videoUrl,Abv,stringPosition,see;
    private long videoDurationInSeconds;
    private Runnable runnableCode;
    private Handler handler;
    int removePosition;

    ArrayList<Integer> hiddenPositions = new ArrayList<>();

    @BindView(R.id.sdvImage)
    com.facebook.drawee.view.SimpleDraweeView sdvImage;
    @BindView(R.id.txtVideoHeading)
    AppCompatTextView txtVideoHeading;
    @BindView(R.id.txtSoryLine)
    AppCompatTextView txtSoryLine;
    @BindView(R.id.txtVideoType)
    AppCompatTextView txtVideoType;
    String videourl,name,storyDescription,episodeNo,episodeThumnailImage,episodeId,json;
    @BindView(R.id.imgPlayVideo)
    AppCompatImageView imgPlayVideo;
    private AppDatabase database;
    private List<Video> videoUriList = new ArrayList<>();
    private List<Subtitle> subtitleList = new ArrayList<>();
    ImageButton nextBtn;
    EpisodeAdapter episodeAdapter;
    String sessionID;
    String JSON;
    String  stringVideo;

    List<EpisodeData> episodeDataList;

    ImageView imgDownload;
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
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        dataSourceFactory = buildDataSourceFactory();
        if (CookieHandler.getDefault() != DEFAULT_COOKIE_MANAGER) {
            CookieHandler.setDefault(DEFAULT_COOKIE_MANAGER);
        }
        setContentView(R.layout.episodeplayactivity);
        ButterKnife.bind(this);
        Intent i = this.getIntent();
        episodeThumnailImage = i.getStringExtra("episodeThumnailImage");
        episodeNo = i.getStringExtra("episodeNo");
        storyDescription = i.getStringExtra("storyDescription");
        name = i.getStringExtra("name");
        episodeId = i.getStringExtra("episodeId");
        videourl = i.getStringExtra("videourl");
        sessionID = i.getStringExtra("sessionID");
        JSON = i.getStringExtra("Json");



        videoListOfUri = Arrays.asList(new GsonBuilder().create().fromJson(JSON, EpisodeData[].class));




        txtVideoType.setText("Episode : " + episodeNo);
        Abv = i.getStringExtra("Abv");

        json =i.getStringExtra("json");
//        Log.i("Ahhhhn",""+removePosition);
        episodeDataArrayList = new ArrayList<>();
        episodeAdapter = new EpisodeAdapter(this, getApplicationContext(),episodeDataArrayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recylerview.setLayoutManager(mLayoutManager);
        recylerview.setItemAnimator(new DefaultItemAnimator());
        recylerview.setAdapter(episodeAdapter);
        recylerview.addOnItemTouchListener(
                new RecyclerItemClickListener(EpisodePlayActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // TODO Handle item click
                        stringPosition ="";
                        stringPosition = String.valueOf(position);



                        episodeNo   = String.valueOf(episodeDataArrayList.get(position).getEpisodeNo());
                        Intent i = new Intent(EpisodePlayActivity.this,EpisodePlayActivity.class);
                        i.putExtra("videourl",episodeDataArrayList.get(position).getVideoLink());
                        i.putExtra("name",name);
                        i.putExtra("episodeThumnailImage",episodeDataArrayList.get(position).getThumbnailLink());
                        i.putExtra("episodeNo",episodeNo);
                        i.putExtra("episodeId",episodeDataArrayList.get(position).getEpisodeId());
                        i.putExtra("storyDescription",storyDescription);
                        i.putExtra("sessionID",sessionID);
                        i.putExtra("Abv",stringPosition);
                        i.putExtra("Json",json);



                        startActivity(i);
                        Log.i("ACD",stringPosition);



                    }
                }));
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
        imgDownload = findViewById(R.id.imgDownload);

        imgDownload.setOnClickListener(this);
        Glide.with(this)
                .load(episodeThumnailImage)
                .into(sdvImage);
        txtVideoHeading.setText(name);
        txtSoryLine.setText(storyDescription);
        setLayout();
        initializeDb();
        makeListOfUri();
        callGetEpisodeList();
    }

    private void setLayout() {
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
    private void callGetEpisodeList() {

        if (AppCommon.getInstance(this).isConnectingToInternet(this)) {
            Dialog dialog = ViewUtils.getProgressBar(EpisodePlayActivity.this);
            AppCommon.getInstance(this).setNonTouchableFlags(this);
            AppService apiService = ServiceGenerator.createService(AppService.class, AppCommon.getInstance(this).getToken());
            Map<String, String> entityMap = new HashMap<>();
            entityMap.put("id", String.valueOf(AppCommon.getInstance(this).getId()));
            entityMap.put("userId", String.valueOf(AppCommon.getInstance(this).getUserId()));
            entityMap.put("seasonId", sessionID);
            Call call = apiService.getEdpisodes(entityMap);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(EpisodePlayActivity.this).clearNonTouchableFlags(EpisodePlayActivity.this);
                    dialog.dismiss();
                    EpisodeResponse authResponse = (EpisodeResponse) response.body();
                    if (authResponse != null) {
                        Log.i("Test", new Gson().toJson(authResponse));
                        if (authResponse.getCode() == 200) {
                            if (authResponse.getData() != null) {
                                setDataEpisode(authResponse.getData());


//                                List<Epis>

                            }
                               /* setData(authResponse.getData());
                            videoUrl= authResponse.getData().getVideoLink();*/


                        } else {

                            Toast.makeText(EpisodePlayActivity.this, authResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        AppCommon.getInstance(EpisodePlayActivity.this).showDialog(EpisodePlayActivity.this, "Server Error");
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    dialog.dismiss();
                    AppCommon.getInstance(EpisodePlayActivity.this).clearNonTouchableFlags(EpisodePlayActivity.this);
                    // loaderView.setVisibility(View.GONE);
                    Toast.makeText(EpisodePlayActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            });


        } else {
            // no internet
            Toast.makeText(this, "Please check your internet", Toast.LENGTH_SHORT).show();
        }
    }

    private void setDataEpisode(ArrayList<EpisodeData> data) {
        episodeDataArrayList.clear();
        episodeDataArrayList = data;
        json = new Gson().toJson(episodeDataArrayList);


        removePosition = Integer.parseInt(Abv);
        episodeDataArrayList.remove(removePosition);
        episodeAdapter.notifyDataSetChanged();
        episodeAdapter.update(episodeDataArrayList);





    }


    private void makeListOfUri() {
        for(int l = 0; l< videoListOfUri.size(); l++){
            stringVideo= videoListOfUri.get(l).getVideoLink();
            videoUriList.add(new Video(stringVideo , Long.getLong("zero" , 1)));

        }

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
            pDialog = new ProgressDialog(EpisodePlayActivity.this);
            pDialog.setTitle(null);
            pDialog.setCancelable(false);
            pDialog.setMessage("Preparing Download Options...");

            pDialog.show();
        }

//            videoUriList.add(new Video(stringVideo , Long.getLong("zero" , 1)));
             downloadHelper = DownloadHelper.forHls(EpisodePlayActivity.this, Uri.parse(videourl), dataSourceFactory, new DefaultRenderersFactory(EpisodePlayActivity.this));





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

        AlertDialog.Builder builder = new AlertDialog.Builder(EpisodePlayActivity.this,R.style.MyDialogTheme1);
       // builder.setTitle("Select Download Format");
        builder.setTitle( Html.fromHtml("<font color='#FFFFFF'>Select Download Format </font>"));

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
                EpisodePlayActivity.this, // Context
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


//                    videoUriList.add(new Video(stringVideo , Long.getLong("zero" , 1)));
                     downloadRequest = helper.getDownloadRequest(Util.getUtf8Bytes(videourl));





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


                if(download.request.uri.toString().equals(videourl)){

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

//                    videoUriList.add(new Video(stringVideo , Long.getLong("zero" , 1)));
                    VideoDownloadTable task = new VideoDownloadTable();
                    task.setTimestamp(millisInString);
                    task.setMovieId(episodeId);
                    task.setMovieName(name);
                    task.setMovieType("Web Series");
                    task.setUrlVideo( videourl);
                    task.setMovieDescription(storyDescription);
                    task.setUrlImage(episodeThumnailImage);

                    //adding to database
                    DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                            .videoDownloadDao()
                            .insert(task);



                //creating a task

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