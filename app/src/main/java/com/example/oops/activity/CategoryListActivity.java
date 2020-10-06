package com.example.oops.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.oops.DataClass.CategoryListData;
import com.example.oops.DataClass.MoviesData;
import com.example.oops.EntityClass.LoginEntity;
import com.example.oops.R;
import com.example.oops.ResponseClass.CategoryResponse;
import com.example.oops.ResponseClass.RegistrationResponse;
import com.example.oops.Utils.AppCommon;
import com.example.oops.Utils.ViewUtils;
import com.example.oops.adapter.GridCategoryAdapter;
import com.example.oops.adapter.HomeMoviesAdapter;
import com.example.oops.fragment.HomeFragment;
import com.example.oops.retrofit.AppService;
import com.example.oops.retrofit.ServiceGenerator;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryListActivity extends AppCompatActivity {

    MoviesData moviesData = new MoviesData();
    GridCategoryAdapter gridCategoryAdapter;

    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    @BindView(R.id.txtHeading)
    AppCompatTextView txtHeading;
    ArrayList<CategoryListData> categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);
        ButterKnife.bind(this);
        if(getIntent()!= null){
            moviesData = new Gson().fromJson(getIntent().getStringExtra("data") , MoviesData.class);
            txtHeading.setText(moviesData.getCategoryName());
        }
        init();
        callCatApi();
    }

    private void callCatApi() {
        if (AppCommon.getInstance(this).isConnectingToInternet(this)) {
            final Dialog dialog = ViewUtils.getProgressBar(CategoryListActivity.this);
            AppCommon.getInstance(this).setNonTouchableFlags(this);
            AppService apiService = ServiceGenerator.createService(AppService.class);
            Map<String , String> entityMap = new HashMap<>();
            entityMap.put("id" , String.valueOf(AppCommon.getInstance(this).getId()));
            entityMap.put("userId" , String.valueOf(AppCommon.getInstance(this).getUserId()));
            entityMap.put("movieCategory" , String.valueOf(moviesData.getCategorgyId()));
            Call call = apiService.categoryApi(entityMap);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(CategoryListActivity.this).clearNonTouchableFlags(CategoryListActivity.this);
                    dialog.dismiss();
                    CategoryResponse authResponse = (CategoryResponse) response.body();
                    if (authResponse != null) {
                        Log.i("Response::", new Gson().toJson(authResponse));
                        if (authResponse.getCode() == 200) {
                            setData(authResponse.getData());
                        } else {
                            Toast.makeText(CategoryListActivity.this,authResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        AppCommon.getInstance(CategoryListActivity.this).showDialog(CategoryListActivity.this, authResponse.getMessage());
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    dialog.dismiss();
                    AppCommon.getInstance(CategoryListActivity.this).clearNonTouchableFlags(CategoryListActivity.this);
                    // loaderView.setVisibility(View.GONE);
                    Toast.makeText(CategoryListActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            });


        } else {
            // no internet
            Toast.makeText(this, "Please check your internet", Toast.LENGTH_SHORT).show();
        }
    }

    private void setData(ArrayList<CategoryListData> data) {
        gridCategoryAdapter.update(data);
    }

    private void init() {
        categoryList = new ArrayList<>();
        gridCategoryAdapter = new GridCategoryAdapter(this , categoryList );
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this , 3);
        recycleView.setLayoutManager(mLayoutManager);
        recycleView.setItemAnimator(new DefaultItemAnimator());
        recycleView.setAdapter(gridCategoryAdapter);
    }

    @OnClick(R.id.imgBackPressed)
    void backBtn(){
        onBackPressed();
    }

    public void moviesDeatils(String movieId, String movieName) {
        startActivity(new Intent(this , VideoPlay.class)
                .putExtra("moviesId" , movieId).putExtra("name" , movieName)
                .putExtra("fav",""));
    }
}