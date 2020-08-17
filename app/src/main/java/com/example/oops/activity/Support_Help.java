package com.example.oops.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.example.oops.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Support_Help extends AppCompatActivity {
    @BindView(R.id.txtHeading)
    AppCompatTextView txtHeading;
    @BindView(R.id.editTextMessage)
    AppCompatEditText editTextMessage;
    String seditTextMessage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.support_help);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        txtHeading.setText(getString(R.string.support_help));
    }
}
