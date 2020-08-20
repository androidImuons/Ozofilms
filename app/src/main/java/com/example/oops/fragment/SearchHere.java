package com.example.oops.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;

import com.example.oops.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchHere extends Fragment {
    @BindView(R.id.editTextSearchHere)
    AppCompatEditText editTextSearchHere;
    String seditTextSearchHere;
    @BindView(R.id.searchHere)
    AppCompatImageView searchHere;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.search_here, container, false);

ButterKnife.bind(this,view);
        return view;

    }
    @OnClick(R.id.searchHere)
    public  void  setSearchHere(){
        seditTextSearchHere = editTextSearchHere.getText().toString().trim();
    }

}
