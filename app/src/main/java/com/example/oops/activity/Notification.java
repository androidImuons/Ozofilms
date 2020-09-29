package com.example.oops.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.example.oops.R;
import com.example.oops.Utils.AppCommon;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Notification extends AppCompatActivity {
    @BindView(R.id.txtHeading)
    AppCompatTextView txtHeading;
    @BindView(R.id.imgBackPressed)
    AppCompatImageView imgBackPressed;
    @BindView(R.id.switchButton)
    Switch switchButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);
        ButterKnife.bind(this);
        txtHeading.setText(getString(R.string.notification));
        imgBackPressed.setVisibility(View.VISIBLE);

        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (switchButton.isChecked()) {

                    AppCommon.getInstance(Notification.this).storeNotificationObject("true");
                }
                else {

                    AppCommon.getInstance(Notification.this).storeNotificationObject("false");


                }
            }
        });
//        switchButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////
//            }
//        });

//          sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
//        editor = sharedPreferences.edit();
//        editor.putString("k1", sedittext_email);
//        editor.putString("k2", sedittext_password);
//
//        editor.commit();

    }

    @OnClick(R.id.imgBackPressed)
    public void setImgBackPressed() {
        onBackPressed();
    }


}
