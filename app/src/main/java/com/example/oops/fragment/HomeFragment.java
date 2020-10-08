package com.example.oops.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.oops.DataClass.MoviesData;
import com.example.oops.DataClass.SliderData;
import com.example.oops.EntityClass.LoginEntity;
import com.example.oops.R;
import com.example.oops.ResponseClass.AllMoviesResponse;
import com.example.oops.ResponseClass.RegistrationResponse;
import com.example.oops.ResponseClass.SliderResponse;
import com.example.oops.Utils.AppCommon;
import com.example.oops.Utils.ViewUtils;
import com.example.oops.activity.CategoryListActivity;
import com.example.oops.activity.Dashboard;
import com.example.oops.activity.Login;
import com.example.oops.activity.VideoPlay;
import com.example.oops.adapter.HomeMoviesAdapter;
import com.example.oops.adapter.SliderAdapter;
import com.example.oops.adapter.ViewPagerAdapter;
import com.example.oops.retrofit.AppService;
import com.example.oops.retrofit.ServiceGenerator;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.rd.PageIndicatorView;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    @BindView(R.id.ll_home)
    LinearLayout ll_home;
    @BindView(R.id.viewPager)
    SliderView viewPager;
    @BindView(R.id.SliderDots)
    LinearLayout SliderDots;
    @BindView(R.id.pageIndicatorView)
    PageIndicatorView pageIndicatorView;
    @BindView(R.id.recycleView)
    RecyclerView recyclerView;

    ArrayList<SliderData> sliderDataArrayList;

    HomeMoviesAdapter homeMoviesAdapter;
    ArrayList<MoviesData> moviesDataArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        ButterKnife.bind(this, view);
        init();
        sliderApi();
        allMoviesApi();
        return view;
    }

    private void init() {
        moviesDataArrayList = new ArrayList<>();
        homeMoviesAdapter = new HomeMoviesAdapter(HomeFragment.this , moviesDataArrayList );
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(homeMoviesAdapter);
    }

    private void allMoviesApi() {
        if (AppCommon.getInstance(getContext()).isConnectingToInternet(getContext())) {
            final Dialog dialog = ViewUtils.getProgressBar(getActivity());
            AppCommon.getInstance(getContext()).setNonTouchableFlags(getActivity());
            AppService apiService = ServiceGenerator.createService(AppService.class , AppCommon.getInstance(getContext()).getToken());
            Map<String , String> entityMap = new HashMap<>();
            entityMap.put("id" , String.valueOf(AppCommon.getInstance(getContext()).getId()));
            entityMap.put("userId" , String.valueOf(AppCommon.getInstance(getContext()).getUserId()));
            Call<AllMoviesResponse> call = apiService.allMoviesApi(entityMap);
            call.enqueue(new Callback<AllMoviesResponse>() {
                @Override
                public void onResponse(Call<AllMoviesResponse> call, Response<AllMoviesResponse> response) {
                    AppCommon.getInstance(getActivity()).clearNonTouchableFlags(getActivity());
                    dialog.dismiss();
                    AllMoviesResponse authResponse = response.body();
                    if (authResponse != null) {
                        Log.e("Slider Response::", new Gson().toJson(authResponse));
                        if (authResponse.getCode() == 200) {
                            setData(authResponse.getData());
                        } else {
                            showSnackbar(ll_home,authResponse.getMessage(),Snackbar.LENGTH_SHORT);
                        }
                    } else {
                        AppCommon.getInstance(getActivity()).showDialog(getActivity(), authResponse.getMessage());
                    }
                }
                @Override
                public void onFailure(Call<AllMoviesResponse> call, Throwable t) {
                    dialog.dismiss();
                    AppCommon.getInstance(getActivity()).clearNonTouchableFlags(getActivity());
                    showSnackbar(ll_home,getResources().getString(R.string.ServerError),Snackbar.LENGTH_SHORT);
                }
            });


        } else {
            showSnackbar(ll_home,getResources().getString(R.string.NoInternet),Snackbar.LENGTH_SHORT);
        }
    }

    private void setData(ArrayList<MoviesData> data) {
        moviesDataArrayList = data;
        homeMoviesAdapter.update(moviesDataArrayList);
    }

    private void sliderApi() {

        if (AppCommon.getInstance(getContext()).isConnectingToInternet(getContext())) {
            final Dialog dialog = ViewUtils.getProgressBar(getActivity());
            AppCommon.getInstance(getContext()).setNonTouchableFlags(getActivity());
            AppService apiService = ServiceGenerator.createService(AppService.class , AppCommon.getInstance(getContext()).getToken());
            Map<String , String> entityMap = new HashMap<>();
            entityMap.put("id" , String.valueOf(AppCommon.getInstance(getContext()).getId()));
            entityMap.put("userId" , String.valueOf(AppCommon.getInstance(getContext()).getUserId()));
            Call<SliderResponse> call = apiService.sliderApi(entityMap);
            call.enqueue(new Callback<SliderResponse>() {
                @Override
                public void onResponse(Call<SliderResponse> call, Response<SliderResponse> response) {
                    AppCommon.getInstance(getActivity()).clearNonTouchableFlags(getActivity());
                    dialog.dismiss();
                    SliderResponse authResponse = response.body();
                    if (authResponse != null) {
                        Log.e("Slider Response::", new Gson().toJson(authResponse));
                        if (authResponse.getCode() == 200) {
                            sliderDataArrayList = authResponse.getData();
                            viewPagerSlider();
                            // callLoginApi(new LoginEntity(authResponse.getData().getUserId(), authResponse.getData().getPassword() , fireBase));
                        } else {
                            showSnackbar(ll_home,authResponse.getMessage(),Snackbar.LENGTH_SHORT);
                        }
                    } else {
                        AppCommon.getInstance(getActivity()).showDialog(getActivity(), authResponse.getMessage());
                    }
                }
                @Override
                public void onFailure(Call<SliderResponse> call, Throwable t) {
                    dialog.dismiss();
                    AppCommon.getInstance(getActivity()).clearNonTouchableFlags(getActivity());
                    showSnackbar(ll_home,getResources().getString(R.string.ServerError),Snackbar.LENGTH_SHORT);
                }
            });
        } else {
            showSnackbar(ll_home,getResources().getString(R.string.NoInternet),Snackbar.LENGTH_SHORT);
        }


    }

    private void viewPagerSlider() {
        SliderAdapter adapter = new SliderAdapter(getContext() , sliderDataArrayList);
        viewPager.setSliderAdapter(adapter);
        viewPager.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        viewPager.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        viewPager.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        viewPager.setIndicatorSelectedColor(Color.WHITE);
        viewPager.setIndicatorUnselectedColor(Color.GRAY);
        viewPager.setScrollTimeInSec(3); //set scroll delay in seconds :
        viewPager.startAutoCycle();
    }

    public void catList(int adapterPosition) {
        startActivity(new Intent(getActivity() , CategoryListActivity.class)
                .putExtra("data" ,new Gson().toJson(moviesDataArrayList.get(adapterPosition))));
    }

    public void moviesDeatils(String movieId, String name) {
        startActivity(new Intent(getContext() , VideoPlay.class)
                .putExtra("moviesId" , movieId).putExtra("name" , name)
                .putExtra("fav",""));
    }

    public void showSnackbar(View view, String message, int duration)
    {
        Snackbar snackbar= Snackbar.make(view, message, duration);
        snackbar.setActionTextColor(Color.WHITE);
        snackbar.setBackgroundTint(getResources().getColor(R.color.colorPrimaryDark));
        snackbar.show();
    }
}
