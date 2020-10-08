package com.example.oops.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import com.example.oops.R;
import com.example.oops.Utils.AppCommon;
import com.google.android.material.snackbar.Snackbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Notification extends AppCompatActivity {
    @BindView(R.id.ll_notification)
    LinearLayout ll_notification;
    @BindView(R.id.txtHeading)
    AppCompatTextView txtHeading;
    @BindView(R.id.imgBackPressed)
    AppCompatImageView imgBackPressed;
    @BindView(R.id.switchButton)
    Switch switchButton;
    String str1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);
        ButterKnife.bind(this);
        txtHeading.setText(getString(R.string.notification));
        imgBackPressed.setVisibility(View.VISIBLE);
        switchButton.setChecked(Boolean.parseBoolean(AppCommon.getInstance(Notification.this).getNotificationObj()));

        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (switchButton.isChecked()) {
                    str1 = switchButton.getTextOn().toString();
                    showSnackbar(ll_notification,str1,Snackbar.LENGTH_SHORT);
                    AppCommon.getInstance(Notification.this).storeNotificationObject(str1);
                } else {
                    str1 = switchButton.getTextOff().toString();
                    showSnackbar(ll_notification,str1,Snackbar.LENGTH_SHORT);
                    AppCommon.getInstance(Notification.this).storeNotificationObject(str1);


                }
            }
        });
    }

    @OnClick(R.id.imgBackPressed)
    public void setImgBackPressed() {
        onBackPressed();
    }

    public void showSnackbar(View view, String message, int duration) {
        Snackbar snackbar = Snackbar.make(view, message, duration);
        snackbar.setActionTextColor(Color.WHITE);
        snackbar.setBackgroundTint(getResources().getColor(R.color.colorPrimaryDark));
        snackbar.show();
    }

}
