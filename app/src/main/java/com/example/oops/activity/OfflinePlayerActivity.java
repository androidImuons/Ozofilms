package com.example.oops.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.cj.videoprogressview.LightProgressView;
import com.cj.videoprogressview.VolumeProgressView;
import com.example.oops.Ooops;
import com.example.oops.R;
import com.example.oops.Utils.PlayerUtils;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.PlaybackPreparer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.offline.DownloadHelper;
import com.google.android.exoplayer2.offline.DownloadRequest;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoRendererEventListener;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.Formatter;
import java.util.Locale;

public class OfflinePlayerActivity extends AppCompatActivity implements View.OnClickListener, VideoRendererEventListener, PlaybackPreparer, PlayerControlView.VisibilityListener {


    private static final String TAG = "OfflinePlayer";
    protected static CookieManager DEFAULT_COOKIE_MANAGER;
    // Exoplayer
    private ImageView imgBackPlayer;
    private ImageView imgBwd;
    private ImageView exoPlay;
    private ImageView exoPause;
    private ImageView imgFwd;
    private LinearLayout linMediaController;
    private TextView tvPlayerCurrentTime;
    private ProgressBar exoProgressbar;
    private TextView tvPlayerEndTime;
    private TextView tvPlaybackSpeed;
    private TextView tvPlayBackSpeedSymbol;
    private ImageView imgFullScreenEnterExit;
    private PlayerView playerView;
    private SimpleExoPlayer simpleExoPlayer;
    private FrameLayout frameLayoutMain;
    int tapCount = 1;
    private StringBuilder mFormatBuilder;
    private Formatter mFormatter;
    private Handler handler;
    private MediaSource mediaSource;
    private DataSource.Factory dataSourceFactory;
    private static final boolean AUTO_HIDE = true;
    private static final int AUTO_HIDE_DELAY_MILLIS = 2000;
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    ImageButton retry,btn_back,btn_subtitle,btn_settings;
    ProgressBar progressBar;
    LightProgressView mLightPeogressView;
    VolumeProgressView mVolumeProgressView;
    AudioManager mAudioManager;
    GestureDetector mGestureDetector;
    protected int mStreamVolume;
    protected float mBrightness;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            hide();
        }
    };

    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }

        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    String offlineVideoLink,title;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.offlinevideoplayer);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            title =  bundle.getString("video_title");
            offlineVideoLink =  bundle.getString("video_url");

        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        dataSourceFactory = buildDataSourceFactory();
        if (CookieHandler.getDefault() != DEFAULT_COOKIE_MANAGER) {
            CookieHandler.setDefault(DEFAULT_COOKIE_MANAGER);
        }

        FullScreencall();
        initView();
        prepareView();
        setOnClickListner();
    }

    private void initView() {
        playerView = findViewById(R.id.player_view);
        retry = findViewById(R.id.retry_btn);
        progressBar = findViewById(R.id.progress_bar);
        btn_back = findViewById(R.id.btn_back);
        btn_subtitle = findViewById(R.id.btn_subtitle);
        btn_settings = findViewById(R.id.btn_settings);
        mLightPeogressView = findViewById(R.id.lpv);
        mVolumeProgressView = findViewById(R.id.vpv);
        mBrightness = PlayerUtils.scanForActivity(this).getWindow().getAttributes().screenBrightness;
        mAudioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
        retry.setOnClickListener(this);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btn_subtitle.setVisibility(View.GONE);
        btn_settings.setVisibility(View.GONE);
        mBrightness = PlayerUtils.scanForActivity(this).getWindow().getAttributes().screenBrightness;
        mAudioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.adjustStreamVolume(
                AudioManager.STREAM_MUSIC,           // or STREAM_ACCESSIBILITY, STREAM_ALARM, STREAM DTMF, STREAM_NOTIFCATION, STREAM_RING, STREAM_SYSTEM, STREAM_VOICE_CALL
                AudioManager.ADJUST_LOWER,           // or ADJUST_RAISE, ADJUST_SAME
                0                                    // or FLAG_PLAY_SOUND, FLAG_REMOVE_SOUND_AND_VIBRATE, FLAG_SHOW_UI, FLAG_VIBRATE, FLAG_ALLOW_RINGER_MODES
        );

        playerView.setOnTouchListener(new OfflinePlayerActivity.OnSwipeTouchListener(OfflinePlayerActivity.this) {

            public void onSwipeTop(float diffY) {

                slideToChangeVolume(diffY);

            }

            public void onSwipeRight(float diffX) {


                slideToChangeBrightness(diffX);


            }

            public void onSwipeLeft(float diffX) {

                slideToChangeBrightness(diffX);


            }

            public void onSwipeBottom(float diffY) {

                slideToChangeVolume(diffY);


            }

        });
    }
    public void prepareView() {

        setProgress();
    }



    private void initExoplayer() {

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);


        DefaultAllocator defaultAllocator = new DefaultAllocator(true, C.DEFAULT_BUFFER_SEGMENT_SIZE);
        DefaultLoadControl defaultLoadControl = new DefaultLoadControl(defaultAllocator,
                DefaultLoadControl.DEFAULT_MIN_BUFFER_MS,
                DefaultLoadControl.DEFAULT_MAX_BUFFER_MS,
                DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_MS,
                DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS,
                DefaultLoadControl.DEFAULT_TARGET_BUFFER_BYTES,
                DefaultLoadControl.DEFAULT_PRIORITIZE_TIME_OVER_SIZE_THRESHOLDS
        );

        DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(this, null,
                DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER);

        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this,renderersFactory, trackSelector,defaultLoadControl);
        playerView.setUseController(true);
        playerView.requestFocus();
        playerView.setPlayer(simpleExoPlayer);
        simpleExoPlayer.setRepeatMode(Player.REPEAT_MODE_ONE);
        simpleExoPlayer.setPlayWhenReady(true); //run file/link when ready to play.
        simpleExoPlayer.setVideoDebugListener(this); //for listening to resolution change and  outputing the resolution



        DownloadRequest downloadRequest = Ooops.getInstance().getDownloadTracker().getDownloadRequest(Uri.parse(offlineVideoLink));
        MediaSource mediaSource = DownloadHelper.createMediaSource(downloadRequest,dataSourceFactory);

//         mediaSource = buildMediaSource(Uri.parse(offlineVideoLink));
        simpleExoPlayer.prepare(mediaSource,false,true);
        simpleExoPlayer.addListener(new ExoPlayer.EventListener() {

            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                Log.v(TAG, "Listener-onTracksChanged...");
            }

            @Override
            public void onLoadingChanged(boolean isLoading) {
                Log.v(TAG, "Listener-onLoadingChanged...isLoading:" + isLoading);
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                Log.v(TAG, "Listener-onPlayerStateChanged..." + playbackState);

                switch (playbackState) {
                    case ExoPlayer.STATE_IDLE:
                        Log.d(TAG, "playbackState : " + "STATE_IDLE");
                        break;
                    case ExoPlayer.STATE_BUFFERING:
                        Log.d(TAG, "playbackState : " + "STATE_BUFFERING");
                        progressBar.setVisibility(View.VISIBLE);
                        retry.setVisibility(View.VISIBLE);
                        break;
                    case ExoPlayer.STATE_READY:
                        Log.d(TAG, "playbackState : " + "STATE_READY");
                        progressBar.setVisibility(View.GONE);
                        retry.setVisibility(View.GONE);
                        break;
                    case ExoPlayer.STATE_ENDED:
                        Log.d(TAG, "playbackState : " + "STATE_ENDED");
                        break;
                    default:

                        break;
                }
//                switch (playbackState) {
//                    case ExoPlayer.STATE_IDLE:
//                        Log.d(TAG, "playbackState : " + "STATE_IDLE");
//                        break;
//                    case ExoPlayer.STATE_BUFFERING:
//                        Log.d(TAG, "playbackState : " + "STATE_BUFFERING");
//                        exoProgressbar.setVisibility(View.VISIBLE);
//                        break;
//                    case ExoPlayer.STATE_READY:
//                        Log.d(TAG, "playbackState : " + "STATE_READY");
//                        exoProgressbar.setVisibility(View.GONE);
//                        break;
//                    case ExoPlayer.STATE_ENDED:
//                        Log.d(TAG, "playbackState : " + "STATE_ENDED");
//                        break;
//                    default:
//
//                        break;
//                }
            }
            @Override
            public void onRepeatModeChanged(int repeatMode) {
                Log.v(TAG, "Listener-onRepeatModeChanged...");
            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                Log.v(TAG, "Listener-onPlayerError...");
                simpleExoPlayer.stop();
                simpleExoPlayer.prepare(mediaSource);
                simpleExoPlayer.setPlayWhenReady(true);
            }


            @Override
            public void onPositionDiscontinuity(int reason) {

            }


            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
                Log.v(TAG, "Listener-onPlaybackParametersChanged...");
            }

            /**
             * Called when all pending seek requests have been processed by the simpleExoPlayer. This is guaranteed
             * to happen after any necessary changes to the simpleExoPlayer state were reported to
             * {@link #onPlayerStateChanged(boolean, int)}.
             */
            @Override
            public void onSeekProcessed() {

            }
        });

        initBwd();
        initFwd();

    }

    private void setProgress() {

        handler = new Handler();
        //Make sure you update Seekbar on UI thread
        handler.post(new Runnable() {

            @Override
            public void run() {
                if (simpleExoPlayer != null) {
//                    tvPlayerCurrentTime.setText(stringForTime((int) simpleExoPlayer.getCurrentPosition()));
//                    tvPlayerEndTime.setText(stringForTime((int) simpleExoPlayer.getDuration()));

                    handler.postDelayed(this, 1000);
                }
            }
        });
    }

    private void initBwd() {
//        imgBwd.requestFocus();
//        imgBwd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                simpleExoPlayer.seekTo(simpleExoPlayer.getCurrentPosition() - 10000);
//            }
//        });
    }

    private void initFwd() {
//        imgFwd.requestFocus();
//        imgFwd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                simpleExoPlayer.seekTo(simpleExoPlayer.getCurrentPosition() + 10000);
//            }
//        });

    }

    private void setOnClickListner() {
//        imgFullScreenEnterExit.setOnClickListener(this);
//        tvPlaybackSpeed.setOnClickListener(this);
//        tvPlaybackSpeed.setOnClickListener(this);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishActivity();
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public void onClick(View view) {

      if(view.getId()==R.id.retry_btn)

        showProgressBar(true);
        showRetryBtn(false);

    }

    private void showRetryBtn(boolean b) {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void showProgressBar(boolean b) {
        retry.setVisibility(View.VISIBLE);
    }



    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        releasePlayer();
        setIntent(intent);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            title = bundle.getString("title");
            offlineVideoLink = bundle.getString("link");
        }


    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initExoplayer();
            if (playerView != null) {
                playerView.onResume();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || playerView == null) {
            initExoplayer();
            if (playerView != null) {
                playerView.onResume();
            }
        }

        FullScreencall();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            if (playerView != null) {
                playerView.onPause();
            }
            releasePlayer();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            if (playerView != null) {
                playerView.onPause();
            }
            releasePlayer();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }


    public void finishActivity(){
        OfflinePlayerActivity.this.finish();
    }


    private void releasePlayer() {
        if (simpleExoPlayer != null) {

            simpleExoPlayer.release();
            simpleExoPlayer = null;

        }

    }

    @Override
    public void onVideoEnabled(DecoderCounters counters) {

    }

    @Override
    public void onVideoDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {

    }

    @Override
    public void onVideoInputFormatChanged(Format format) {

    }

    @Override
    public void onDroppedFrames(int count, long elapsedMs) {

    }

    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {

    }

    @Override
    public void onRenderedFirstFrame(Surface surface) {

    }

    @Override
    public void onVideoDisabled(DecoderCounters counters) {

    }

    public void FullScreencall() {
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }


    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    private String stringForTime(int timeMs) {
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }


    @Override
    public void preparePlayback() {
        initExoplayer();

    }

    @Override
    public void onVisibilityChange(int visibility) {

    }

    private DataSource.Factory buildDataSourceFactory() {
        return ((Ooops) getApplication()).buildDataSourceFactory();
    }

    private MediaSource buildMediaSource(Uri uri) {
        return buildMediaSource(uri, null);
    }

    @SuppressWarnings("unchecked")
    private MediaSource buildMediaSource(Uri uri, @Nullable String overrideExtension) {
        @C.ContentType int type = Util.inferContentType(uri, overrideExtension);
        switch (type) {
            case C.TYPE_DASH:
                return new DashMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(uri);
            case C.TYPE_SS:
                return new SsMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(uri);
            case C.TYPE_HLS:
                return new HlsMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(uri);
            case C.TYPE_OTHER:
                return new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
            default: {
                throw new IllegalStateException("Unsupported type: " + type);
            }
        }
    }

    protected void slideToChangeBrightness(float deltaY) {
        Window window = PlayerUtils.scanForActivity(this).getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        int height = PlayerUtils.getScreenHeight(getApplicationContext(), false);
        if (mBrightness == -1.0f) mBrightness = 0.5f;
        float brightness = deltaY * 2 / height * 1.0f + mBrightness;
        if (brightness < 0) {
            brightness = 0f;

        }
        if (brightness > 1.0f) brightness = 1.0f;
        mLightPeogressView.setProgress(brightness);


        attributes.screenBrightness = brightness;
        window.setAttributes(attributes);
    }

    protected void slideToChangeVolume(float deltaY) {
        int streamMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int height = PlayerUtils.getScreenHeight(getApplicationContext(), false);
        float deltaV = deltaY * 2 / height * streamMaxVolume;
        float index = mStreamVolume + deltaV;
        if (index > streamMaxVolume) {
            index = streamMaxVolume;

        } else if (index < 0) {
            index = 0;
        }
        mVolumeProgressView.setProgress(index / streamMaxVolume);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int) index, 0);
    }


    public class OnSwipeTouchListener implements View.OnTouchListener {

        private final GestureDetector gestureDetector;

        public OnSwipeTouchListener(Context ctx) {
            gestureDetector = new GestureDetector(ctx, new GestureListener());
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (gestureDetector.onTouchEvent(event)) {
                playerView.showController();
                return true;
            } else {
                mVolumeProgressView.setVisibility(View.GONE);
                mLightPeogressView.setVisibility(View.GONE);
            }
            //return super.onTouchEvent(event);
            return gestureDetector.onTouchEvent(event);
        }

        private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

            private static final int SWIPE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;
            private boolean mFirstTouch;
            private boolean mChangeBrightness;
            private boolean mChangeVolume;

            @Override
            public boolean onDown(MotionEvent e) {
                mStreamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                mBrightness = OfflinePlayerActivity.this.getWindow().getAttributes().screenBrightness;
                return true;
            }



            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                if (e1 == null || e2 == null) {
                    mVolumeProgressView.setVisibility(View.GONE);
                    mLightPeogressView.setVisibility(View.GONE);
                    return false;
                }else{
                    mFirstTouch = true;
                    mChangeBrightness = false;
                    mChangeVolume = false;
                }

                float deltaY = e1.getY() - e2.getY();
                if (mFirstTouch) {
                    if (Math.abs(distanceX) < Math.abs(distanceY)) {
                        if (e2.getX() > PlayerUtils.getScreenWidth(OfflinePlayerActivity.this, true) / 2) {
                            mChangeVolume = true;
                            mVolumeProgressView.setVisibility(View.VISIBLE);
                        } else {
                            mChangeBrightness = true;
                            mLightPeogressView.setVisibility(View.VISIBLE);
                        }

                    }

                    mFirstTouch = false;
                }


                if (mChangeBrightness) {
                    slideToChangeBrightness(deltaY);

                } else if (mChangeVolume) {
                    slideToChangeVolume(deltaY);
                }


                return true;

            }


            @Override
            public boolean onDoubleTap(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                boolean result = false;
                try {
                    float diffY = e2.getY() - e1.getY();
                    float diffX = e2.getX() - e1.getX();
                    if (Math.abs(diffX) > Math.abs(diffY)) {
                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {

                            if (diffX > 0) {
                               /* mVolumeProgressView.setVisibility(View.GONE);
                                mLightPeogressView.setVisibility(View.VISIBLE);*/
                                onSwipeRight(diffX);

                            }/* else {
                               // mVolumeProgressView.setVisibility(View.GONE);
                                mLightPeogressView.setVisibility(View.VISIBLE);*//*
                                onSwipeLeft(diffX);
                            }*/
                            result = true;
                        }
                    } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {

                            onSwipeBottom(diffY);

                        }
                        result = true;
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                return result;
            }
        }

        public void onSwipeRight(float diffX) {
        }

        public void onSwipeLeft(float diffX) {
        }

        public void onSwipeTop(float diffY) {
        }

        public void onSwipeBottom(float diffY) {
        }

    }

}