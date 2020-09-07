package com.example.oops.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

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
import com.example.oops.adapter.HomeMoviesAdapter;
import com.example.oops.adapter.ViewPagerAdapter;
import com.example.oops.retrofit.AppService;
import com.example.oops.retrofit.ServiceGenerator;
import com.google.gson.Gson;
import com.rd.PageIndicatorView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.SliderDots)
    LinearLayout SliderDots;
    @BindView(R.id.pageIndicatorView)
    PageIndicatorView pageIndicatorView;

    private int dotscount;
    ArrayList<SliderData> sliderDataArrayList;
    private ImageView[] dots;

    HomeMoviesAdapter homeMoviesAdapter;
    ArrayList<MoviesData> moviesDataArrayList;
    @BindView(R.id.recycleView)
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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
            Call call = apiService.allMoviesApi(entityMap);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(getActivity()).clearNonTouchableFlags(getActivity());
                    dialog.dismiss();
                    AllMoviesResponse authResponse = (AllMoviesResponse) response.body();
                    if (authResponse != null) {
                        Log.e("Slider Response::", new Gson().toJson(authResponse));
                        if (authResponse.getCode() == 200) {
                            setData(authResponse.getData());
                            // callLoginApi(new LoginEntity(authResponse.getData().getUserId(), authResponse.getData().getPassword() , fireBase));
                        } else {
                            Toast.makeText(getActivity(),authResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        AppCommon.getInstance(getActivity()).showDialog(getActivity(), authResponse.getMessage());
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    dialog.dismiss();
                    AppCommon.getInstance(getActivity()).clearNonTouchableFlags(getActivity());
                    // loaderView.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
                }
            });


        } else {
            // no internet
            Toast.makeText(getContext(), "Please check your internet", Toast.LENGTH_SHORT).show();
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
            Call call = apiService.sliderApi(entityMap);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(getActivity()).clearNonTouchableFlags(getActivity());
                    dialog.dismiss();
                    SliderResponse authResponse = (SliderResponse) response.body();
                    if (authResponse != null) {
                        Log.e("Slider Response::", new Gson().toJson(authResponse));
                        if (authResponse.getCode() == 200) {
                            sliderDataArrayList = authResponse.getData();
                            viewPagerSlider();
                            // callLoginApi(new LoginEntity(authResponse.getData().getUserId(), authResponse.getData().getPassword() , fireBase));
                        } else {
                            Toast.makeText(getActivity(),authResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        AppCommon.getInstance(getActivity()).showDialog(getActivity(), authResponse.getMessage());
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    dialog.dismiss();
                    AppCommon.getInstance(getActivity()).clearNonTouchableFlags(getActivity());
                    // loaderView.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
                }
            });


        } else {
            // no internet
            Toast.makeText(getContext(), "Please check your internet", Toast.LENGTH_SHORT).show();
        }


    }

    private void viewPagerSlider() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getActivity() , sliderDataArrayList);

        viewPager.setAdapter(viewPagerAdapter);

        dotscount = viewPagerAdapter.getCount();
        pageIndicatorView.setCount(dotscount);
        pageIndicatorView.setSelection(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {/*empty*/}

            @Override
            public void onPageSelected(int position) {
                pageIndicatorView.setSelection(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {/*empty*/}
        });
        viewPager.setCurrentItem(10000);
        dots = new ImageView[dotscount];

        for (int i = 0; i < dotscount; i++) {

            dots[i] = new ImageView(getActivity());
            dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.nonactive_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            SliderDots.addView(dots[i], params);
        }
    }

    public void catList(int adapterPosition) {
        startActivity(new Intent(getActivity() , CategoryListActivity.class)
                .putExtra("data" ,new Gson().toJson(moviesDataArrayList.get(adapterPosition))));
    }
}
