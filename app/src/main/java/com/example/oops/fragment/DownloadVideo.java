package com.example.oops.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oops.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DownloadVideo extends Fragment {
    @BindView(R.id.txtHeading)
    AppCompatTextView txtHeading;
    @BindView(R.id.imgBackPressed)
    AppCompatImageView imgBackPressed;
    @BindView(R.id.recylerview)
    RecyclerView recylerview;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.downloadvideofragment, container, false);
        ButterKnife.bind(this,view);
        txtHeading.setText(getString(R.string.downloads));

        return view;

    }
    @OnClick(R.id.imgBackPressed)
    public  void setImgBackPressed(){

    }
}
