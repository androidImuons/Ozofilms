package com.example.oops.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oops.DataClass.MovieDeatilsData;
import com.example.oops.R;
import com.example.oops.ResponseClass.CommonResponse;
import com.example.oops.ResponseClass.MovieDeatilsResponse;
import com.example.oops.Utils.AppCommon;
import com.example.oops.Utils.ViewUtils;
import com.example.oops.retrofit.AppService;
import com.example.oops.retrofit.ServiceGenerator;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoPlay extends AppCompatActivity {
    @BindView(R.id.txtVideoHeading)
    AppCompatTextView txtVideoHeading;
    String stxtVideoHeading;

    @BindView(R.id.txtContentType)
    AppCompatTextView txtContentType;
    @BindView(R.id.txtRate)
    AppCompatTextView txtRate;
    @BindView(R.id.txtVideoType)
    AppCompatTextView txtVideoType;
    @BindView(R.id.txtSoryLine)
    AppCompatTextView txtSoryLine;
    @BindView(R.id.txtCastName)
    AppCompatTextView txtCastName;
    @BindView(R.id.txtDirectorName)
    AppCompatTextView txtDirectorName;
    @BindView(R.id.recylerview)
    RecyclerView recylerview;
    @BindView(R.id.sdvImage)
    SimpleDraweeView sdvImage;
    @BindView(R.id.txtHeading)
    AppCompatTextView  txtHeading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_player);
        ButterKnife.bind(this);
        if(getIntent()!= null){
            String movieId = getIntent().getStringExtra("moviesId");
            String name = getIntent().getStringExtra("name");
            txtHeading.setText(name);
            callGetMoviesDetails(movieId);
        }
    }

    @OnClick(R.id.imgBackPressed)
    public  void setImgBackPressed(){
        onBackPressed();
    }


    private void callGetMoviesDetails(String movieId) {

        if (AppCommon.getInstance(this).isConnectingToInternet(this)) {
            Dialog dialog = ViewUtils.getProgressBar(VideoPlay.this);
            AppCommon.getInstance(this).setNonTouchableFlags(this);
            AppService apiService = ServiceGenerator.createService(AppService.class , AppCommon.getInstance(this).getToken());
            Map<String , String> entityMap = new HashMap<>();
            entityMap.put("id" , String.valueOf(AppCommon.getInstance(this).getId()));
            entityMap.put("userId" , String.valueOf(AppCommon.getInstance(this).getUserId()));
            entityMap.put("movieId" , String.valueOf(movieId));
            Call call = apiService.MOVIE_DEATILS_RESPONSE_CALL(entityMap);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(VideoPlay.this).clearNonTouchableFlags(VideoPlay.this);
                    dialog.dismiss();
                    MovieDeatilsResponse authResponse = (MovieDeatilsResponse) response.body();
                    if (authResponse != null) {
                        Log.i("Response::", new Gson().toJson(authResponse));
                        if (authResponse.getCode() == 200) {
                            if(authResponse.getData() != null)
                            setData(authResponse.getData());
                        } else {

                            Toast.makeText(VideoPlay.this, authResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        AppCommon.getInstance(VideoPlay.this).showDialog(VideoPlay.this, "Server Error");
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    dialog.dismiss();
                    AppCommon.getInstance(VideoPlay.this).clearNonTouchableFlags(VideoPlay.this);
                    // loaderView.setVisibility(View.GONE);
                    Toast.makeText(VideoPlay.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            });


        } else {
            // no internet
            Toast.makeText(this, "Please check your internet", Toast.LENGTH_SHORT).show();
        }
    }

    private void setData(MovieDeatilsData data) {
        if(data.getMovieLongDescription() != null)
        txtSoryLine.setText(data.getMovieLongDescription());
        else
            txtSoryLine.setText("N/A");

        if(data.getCast() != null)
            txtCastName.setText(data.getCast());
        else
            txtCastName.setText("N/A");

        if(data.getDirector() != null)
            txtDirectorName.setText(data.getDirector());
        else
            txtDirectorName.setText("N/A");

        if(data.getCategoryName() != null)
            txtContentType.setText(data.getCategoryName());
        else
            txtContentType.setText("N/A");

        sdvImage.setController(AppCommon.getInstance(this).getDraweeController(sdvImage , data.getBannerLink() , 1024));
    }
}
