package com.example.oops.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oops.R;

import butterknife.BindView;

public class VideoPlay extends AppCompatActivity {
    @BindView(R.id.txtVideoHeading)
    AppCompatTextView txtVideoHeading;
    String stxtVideoHeading;

    @BindView(R.id.txtContentType)
    AppCompatTextView txtContentType;
    String stxtContentType;
    @BindView(R.id.txtRate)
    AppCompatTextView txtRate;
    String stxtRate;
    @BindView(R.id.txtVideoType)
    AppCompatTextView txtVideoType;
    String stxtVideoType;
    @BindView(R.id.txtSoryLine)
    AppCompatTextView txtSoryLine;
    String stxtSoryLine;
    @BindView(R.id.txtCastName)
    AppCompatTextView txtCastName;
    String stxtCastName;
    @BindView(R.id.txtDirectorName)
    AppCompatTextView txtDirectorName;
    String stxtDirectorName;
    @BindView(R.id.recylerview)
    RecyclerView recylerview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_player);
    }
}
