package com.example.oops.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.example.oops.R;

import org.intellij.lang.annotations.Language;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AppSetting extends AppCompatActivity {
    @BindView(R.id.txtHeading)
    AppCompatTextView txtHeading;
    @BindView(R.id.imgArrowProfile)
    AppCompatImageView imgArrowProfile;
    @BindView(R.id.imgArrowLanguage)
    AppCompatImageView imgArrowLanguage;
    @BindView(R.id.imgArrowNotification)
    AppCompatImageView imgArrowNotification;
    @BindView(R.id.imgArrowVideoPlayBack)
    AppCompatImageView imgArrowVideoPlayBack;
    @BindView(R.id.imgArrowDownloads)
    AppCompatImageView imgArrowDownloads;
    @BindView(R.id.imgBackPressed)
    AppCompatImageView imgBackPressed;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appsetting);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        txtHeading.setText(getString(R.string.app_setting));
    }
@OnClick(R.id.imgBackPressed)
public  void setImgBackPressed(){
       onBackPressed();
}
    @OnClick(R.id.imgArrowProfile)
    public void setImgArrowProfile(View view) {
        startActivity(new Intent(AppSetting.this, Profile.class));
    }

    @OnClick(R.id.imgArrowLanguage)
    public void setImgArrowLanguage() {
        startActivity(new Intent(AppSetting.this, LanguageOption.class));
    }

    @OnClick(R.id.imgArrowNotification)
    public void setImgArrowNotification() {
        startActivity(new Intent(AppSetting.this, Notification.class));
    }

    @OnClick(R.id.imgArrowVideoPlayBack)
    public void setImgArrowVideoPlayBack() {
        startActivity(new Intent(AppSetting.this, VideoPlayBack.class));
    }

    @OnClick(R.id.imgArrowDownloads)
    public void setImgArrowDownloads() {
        startActivity(new Intent(AppSetting.this, Downloads.class));
    }
}
