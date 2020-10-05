package com.example.oops.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.cj.videoprogressview.LightProgressView;
import com.cj.videoprogressview.VolumeProgressView;
import com.example.oops.Ooops;
import com.example.oops.R;
import com.example.oops.Utils.PlayerController;
import com.example.oops.Utils.PlayerUtils;
import com.example.oops.Utils.VideoPlayer;

import com.example.oops.adapter.SubtitleAdapter;
import com.example.oops.data.database.AppDatabase;
import com.example.oops.data.model.VideoSource;
import com.google.android.exoplayer2.text.CaptionStyleCompat;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener, PlayerController {

    private static final String TAG = "PlayerActivity";
    private PlayerView playerView;
    private VideoPlayer player;
    private ImageButton mute, unMute, subtitle, setting, preBtn,retry,nextBtn, back;
    private ProgressBar progressBar;
    private AlertDialog alertDialog;
    private boolean mFirstTouch;
    private boolean mChangeBrightness;
    private boolean mChangeVolume;
    int selectedPosition=0;
    private VideoSource videoSource;

    private boolean disableBackPress = false;

    LightProgressView mLightPeogressView;
    VolumeProgressView mVolumeProgressView;
    AudioManager mAudioManager;
    GestureDetector mGestureDetector;
    protected int mStreamVolume;
    protected float mBrightness;




    private final AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {
                @Override
                public void onAudioFocusChange(int focusChange) {
                    switch (focusChange) {
                        case AudioManager.AUDIOFOCUS_GAIN:
                            if (player != null)

                                break;
                        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:

                            if (player != null)
                                player.getPlayer().setPlayWhenReady(false);
                            break;
                        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                        case AudioManager.AUDIOFOCUS_LOSS:

                            if (player != null)
                                player.getPlayer().setPlayWhenReady(false);
                            break;
                    }
                }
            };



    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        setContentView(R.layout.activity_player);

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        getDataFromIntent();
        setupLayout();
        initSource();
    }

    private void getDataFromIntent() {
        videoSource = getIntent().getParcelableExtra("videoSource");
        Intent i = this.getIntent();

        selectedPosition = Integer.parseInt(i.getStringExtra("selectedPosition"));
//       selectedPosition=Integer.valueOf(getIntent().getParcelableExtra("selectedPosition"));
        Log.d("AHDHHD",""+videoSource);
    }

    private void setupLayout() {
        playerView = findViewById(R.id.demo_player_view);
        progressBar = findViewById(R.id.progress_bar);

        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);


        subtitle = findViewById(R.id.btn_subtitle);
        setting = findViewById(R.id.btn_settings);

        retry = findViewById(R.id.retry_btn);
        back = findViewById(R.id.btn_back);
        preBtn = findViewById(R.id.btn_prev);
        mLightPeogressView = findViewById(R.id.lpv);
        nextBtn = findViewById(R.id.btn_next);
        mVolumeProgressView = findViewById(R.id.vpv);
        mBrightness = PlayerUtils.scanForActivity(this).getWindow().getAttributes().screenBrightness;
        mAudioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);

        playerView.getSubtitleView().setVisibility(View.GONE);


        subtitle.setOnClickListener(this);
        setting.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
        preBtn.setOnClickListener(this);
        retry.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    private void initSource() {

        if (videoSource.getVideos() == null) {
            Toast.makeText(this, "can not play video", Toast.LENGTH_SHORT).show();
            return;
        }

        player = new VideoPlayer(playerView, getApplicationContext(), videoSource, this,selectedPosition);

        checkIfVideoHasSubtitle();


        mBrightness = PlayerUtils.scanForActivity(this).getWindow().getAttributes().screenBrightness;
        mAudioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.adjustStreamVolume(
                AudioManager.STREAM_MUSIC,           // or STREAM_ACCESSIBILITY, STREAM_ALARM, STREAM DTMF, STREAM_NOTIFCATION, STREAM_RING, STREAM_SYSTEM, STREAM_VOICE_CALL
                AudioManager.ADJUST_LOWER,           // or ADJUST_RAISE, ADJUST_SAME
                0                                    // or FLAG_PLAY_SOUND, FLAG_REMOVE_SOUND_AND_VIBRATE, FLAG_SHOW_UI, FLAG_VIBRATE, FLAG_ALLOW_RINGER_MODES
        );






        playerView.setOnTouchListener(new OnSwipeTouchListener(PlayerActivity.this) {

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




        //optional setting
        playerView.getSubtitleView().setVisibility(View.GONE);
//        player.seekToOnDoubleTap();

        playerView.setControllerVisibilityListener(visibility ->
        {
            Log.i(TAG, "onVisibilityChange: " + visibility);
            if (player.isLock())
                playerView.hideController();

            back.setVisibility(visibility == View.VISIBLE && !player.isLock() ? View.VISIBLE : View.GONE);
        });

    }

    @Override
    public void onStart() {
        super.onStart();

        if (player != null)

            player.resumePlayer();

    }

    @Override
    public void onResume() {
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        super.onResume();

        hideSystemUi();
        if (player != null)

            player.resumePlayer();

    }

    @Override
    public void onPause() {
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        super.onPause();

        if (player != null)

            player.releasePlayer();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAudioManager != null) {
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
            mAudioManager = null;
        }
        if (player != null) {
            player.releasePlayer();
            player = null;
        }
//        Ooops.getRefWatcher(this).watch(this);
    }

    @Override
    public void onBackPressed() {
        if (disableBackPress)
            return;

        super.onBackPressed();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        hideSystemUi();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onClick(View view) {
        int controllerId = view.getId();

        switch (controllerId) {

            case R.id.btn_settings:
                player.setSelectedQuality(this);
                break;
            case R.id.btn_subtitle:
                prepareSubtitles();
                break;

            case R.id.exo_rew:
                player.seekToSelectedPosition(0, true);
                break;
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.retry_btn:
                initSource();
                showProgressBar(true);
                showRetryBtn(false);
                break;
            case R.id.btn_next:


                player.seekToNext();
                checkIfVideoHasSubtitle();
                break;
            case R.id.btn_prev:
                player.seekToPrevious();
                checkIfVideoHasSubtitle();
                break;
            default:
                break;
        }
    }

    /***********************************************************
     UI config
     ***********************************************************/
    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    public void showSubtitle(boolean show) {

        if (player == null || playerView.getSubtitleView() == null)
            return;

        if (!show) {
            playerView.getSubtitleView().setVisibility(View.GONE);
            return;
        }

        alertDialog.dismiss();
        playerView.getSubtitleView().setVisibility(View.VISIBLE);
    }

    @Override
    public void changeSubtitleBackground() {
        CaptionStyleCompat captionStyleCompat = new CaptionStyleCompat(Color.YELLOW, Color.TRANSPARENT, Color.TRANSPARENT,
                CaptionStyleCompat.EDGE_TYPE_DROP_SHADOW, Color.LTGRAY, null);
        playerView.getSubtitleView().setStyle(captionStyleCompat);
    }

    private boolean checkIfVideoHasSubtitle() {
        if (player.getCurrentVideo().getSubtitles() == null ||
                player.getCurrentVideo().getSubtitles().size() == 0) {
            subtitle.setImageResource(R.drawable.exo_no_subtitle_btn);
            return true;
        }

        subtitle.setImageResource(R.drawable.exo_subtitle_btn);
        return false;
    }

    private void prepareSubtitles() {
        if (player == null || playerView.getSubtitleView() == null)
            return;

        if (checkIfVideoHasSubtitle()) {
            Toast.makeText(this, getString(R.string.no_subtitle), Toast.LENGTH_SHORT).show();
            return;
        }

        player.pausePlayer();
        showSubtitleDialog();

    }

    private void showSubtitleDialog() {
        //init subtitle dialog
        getWindow().setFlags(LayoutParams.FLAG_FULLSCREEN, LayoutParams.FLAG_FULLSCREEN);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);


        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        View view = inflater.inflate(R.layout.subtitle_selection_dialog, null);

        builder.setView(view);
        alertDialog = builder.create();

        // set the height and width of dialog
        LayoutParams layoutParams = new LayoutParams();
        layoutParams.copyFrom(alertDialog.getWindow().getAttributes());
        layoutParams.width = LayoutParams.WRAP_CONTENT;
        layoutParams.height = LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.CENTER;

        alertDialog.getWindow().setAttributes(layoutParams);

        RecyclerView recyclerView = view.findViewById(R.id.subtitle_recycler_view);
        recyclerView.setAdapter(new SubtitleAdapter(player.getCurrentVideo().getSubtitles(), player));

        for (int i = 0; i < player.getCurrentVideo().getSubtitles().size(); i++) {
            Log.d("subtitle", "showSubtitleDialog: " + player.getCurrentVideo().getSubtitles().get(i).getTitle());
        }

        TextView noSubtitle = view.findViewById(R.id.no_subtitle_text_view);
        noSubtitle.setOnClickListener(view1 -> {
            if (playerView.getSubtitleView().getVisibility() == View.VISIBLE)
                showSubtitle(false);
            alertDialog.dismiss();
            player.resumePlayer();
        });

        Button cancelDialog = view.findViewById(R.id.cancel_dialog_btn);
        cancelDialog.setOnClickListener(view1 -> {
            alertDialog.dismiss();
            player.resumePlayer();
        });

        // to prevent dialog box from getting dismissed on outside touch
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    public void setMuteMode(boolean mute) {
        if (player != null && playerView != null) {
            if (mute) {
                this.mute.setVisibility(View.GONE);
                unMute.setVisibility(View.VISIBLE);
            } else {
                unMute.setVisibility(View.GONE);
                this.mute.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void showProgressBar(boolean visible) {
        progressBar.setVisibility(visible ? View.VISIBLE : View.GONE);
    }



    @Override
    public void showRetryBtn(boolean visible) {
        retry.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void audioFocus() {
        mAudioManager.requestAudioFocus(
                mOnAudioFocusChangeListener,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);
    }

    @Override
    public void setVideoWatchedLength() {
        AppDatabase.Companion.getDatabase(getApplicationContext()).videoDao().
                updateWatchedLength(player.getCurrentVideo().getUrl(), player.getWatchedLength());
    }

    @Override
    public void videoEnded() {
        AppDatabase.Companion.getDatabase(getApplicationContext()).videoDao().
                updateWatchedLength(player.getCurrentVideo().getUrl(), 0);
        player.seekToNext();
    }

    @Override
    public void disableNextButtonOnLastVideo(boolean disable) {
        if(disable){
            nextBtn.setImageResource(R.drawable.exo_disable_next_btn);
            nextBtn.setEnabled(false);
            return;
        }

        nextBtn.setImageResource(R.drawable.exo_next_btn);
        nextBtn.setEnabled(true);
    }


    protected void slideToChangeBrightness(float deltaY) {
        Window window = PlayerUtils.scanForActivity(this).getWindow();
        LayoutParams attributes = window.getAttributes();
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
                mBrightness = PlayerActivity.this.getWindow().getAttributes().screenBrightness;
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
                        if (e2.getX() > PlayerUtils.getScreenWidth(PlayerActivity.this, true) / 2) {
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
