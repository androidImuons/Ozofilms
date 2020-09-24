package com.example.oops.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.oops.DataClass.MoviesSearchModule;
import com.example.oops.DataClass.SearchData;
import com.example.oops.DataClass.WebSearchModule;
import com.example.oops.DataClass.WebSearchResponse;
import com.example.oops.R;
import com.example.oops.ResponseClass.CategoryResponse;
import com.example.oops.ResponseClass.MovieDeatilsResponse;
import com.example.oops.ResponseClass.MoviesSearchResponse;
import com.example.oops.Utils.AppCommon;
import com.example.oops.Utils.ViewUtils;
import com.example.oops.activity.CategoryListActivity;
import com.example.oops.activity.VideoPlay;
import com.example.oops.activity.VideoPlayerSeries;
import com.example.oops.adapter.GridCategoryAdapter;
import com.example.oops.adapter.GridSearchAdapter;
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

public class SearchHere extends Fragment {

    @BindView(R.id.editTextSearchHere)
    AppCompatEditText editTextSearchHere;
    String seditTextSearchHere;
    @BindView(R.id.searchHere)
    AppCompatImageView searchHere;
    @BindView(R.id.recylerview)
    RecyclerView recylerview;

    @BindView(R.id.radioButton)
    RadioButton radioButton;

    @BindView(R.id.radioButton2)
    RadioButton radioButton2;

    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout swiperefresh;

    Dialog dialog;
    int offset;
    GridSearchAdapter gridCategoryAdapter;

    ArrayList<MoviesSearchModule> moviesSearchModules;
    ArrayList<WebSearchModule> webSearchModuleArrayList;
    ArrayList<SearchData> searchDataArrayList ;
    //boolean isLastCurrent stat
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    int isMovies = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.search_here, container, false);

        ButterKnife.bind(this, view);
        init();

        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                searchDataArrayList.clear();
                callSearchApi();
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                searchDataArrayList.clear();
                callSearchApi();
            }
        });
        return view;

    }

    private void init() {
        searchDataArrayList = new ArrayList<>();
        gridCategoryAdapter = new GridSearchAdapter(this, searchDataArrayList);
       // AutoFitGridLayoutManager layoutManager = new AutoFitGridLayoutManager(this, 500);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 3 , RecyclerView.VERTICAL , false);
       // LinearLayoutManager mLayoutManagerMy = new LinearLayoutManager(getContext() , LinearLayoutManager.VERTICAL , false);

        recylerview.setLayoutManager(mLayoutManager);
        recylerview.setItemAnimator(new DefaultItemAnimator());
        recylerview.setAdapter(gridCategoryAdapter);

        editTextSearchHere.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchDataArrayList.clear();
                    callSearchApi();
                    return true;
                }
                return false;
            }
        });
        callSearchApi();
    }

    private void callSearchApi() {

        if (radioButton.isChecked()) {
            isMovies = 1;
        } else {
            isMovies = 0;
        }
        if (AppCommon.getInstance(getContext()).isConnectingToInternet(getContext())) {
            if(!swiperefresh.isRefreshing())
            dialog = ViewUtils.getProgressBar(getActivity());
            AppCommon.getInstance(getContext()).setNonTouchableFlags(getActivity());
            AppService apiService = ServiceGenerator.createService(AppService.class);
            Map<String, String> entityMap = new HashMap<>();
            entityMap.put("id", String.valueOf(AppCommon.getInstance(getContext()).getId()));
            entityMap.put("userId", String.valueOf(AppCommon.getInstance(getContext()).getUserId()));
            entityMap.put("searchString", String.valueOf(editTextSearchHere.getText().toString()));
            entityMap.put("offset", String.valueOf(offset));
            entityMap.put("isMovie", String.valueOf(isMovies));
            Call call;
            if (isMovies == 1)
                call = apiService.searchMoviesApi(entityMap);
            else
                call = apiService.searchWebApi(entityMap);
            int finalIsMovies = isMovies;
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(getContext()).clearNonTouchableFlags(getActivity());
                    if(!swiperefresh.isRefreshing())
                    dialog.dismiss();
                    else
                        swiperefresh.setRefreshing(false);
                    if (finalIsMovies == 1) {
                        MoviesSearchResponse authResponse = (MoviesSearchResponse) response.body();
                        if (authResponse != null) {
                            Log.i("Response::", new Gson().toJson(authResponse));
                            if (authResponse.getCode() == 200) {
                                setDataMovies(authResponse.getData());
                            } else {
                                Toast.makeText(getContext(), authResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            AppCommon.getInstance(getContext()).showDialog(getActivity(), authResponse.getMessage());
                        }
                    } else {
                        WebSearchResponse authResponse = (WebSearchResponse) response.body();
                        if (authResponse != null) {
                            Log.i("Response::", new Gson().toJson(authResponse));
                            if (authResponse.getCode() == 200) {
                                setDataWeb(authResponse.getData());
                            } else {
                                Toast.makeText(getContext(), authResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            AppCommon.getInstance(getContext()).showDialog(getActivity(), authResponse.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    if(!swiperefresh.isRefreshing())
                        dialog.dismiss();
                    else
                        swiperefresh.setRefreshing(false);
                    AppCommon.getInstance(getContext()).clearNonTouchableFlags(getActivity());
                    // loaderView.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Server Error", Toast.LENGTH_SHORT).show();
                }
            });


        } else {
            // no internet
            Toast.makeText(getContext(), "Please check your internet", Toast.LENGTH_SHORT).show();
        }
    }

    private void setDataWeb(ArrayList<WebSearchModule> data) {
        for (int i = 0; i < data.size(); i++) {
            searchDataArrayList.add(new SearchData(data.get(i).getId(),
                    data.get(i).getSeriesId(), data.get(i).getSeriesName()
                    , data.get(i).getImageLink()));
        }
        gridCategoryAdapter.notifyDataSetChanged();
    }

    private void setDataMovies(ArrayList<MoviesSearchModule> data) {
       // searchDataArrayList = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            boolean isMovieValue;
            if (isMovies == 1)
                isMovieValue = true;
            else
                isMovieValue = false;
            searchDataArrayList.add(new SearchData(data.get(i).getId(),
                    data.get(i).getMovieId(), data.get(i).getMovieName()
                    , data.get(i).getImageLink() ));
        }

        gridCategoryAdapter.update(searchDataArrayList , offset);
    }

    @OnClick(R.id.searchHere)
    public void setSearchHere() {
        seditTextSearchHere = editTextSearchHere.getText().toString().trim();
    }

    public void callFragmet(int position) {
        offset = offset +1;
        callSearchApi();
    }

    public void clickRelativeMovies(int adapterPosition) {
        if(isMovies ==1) {
            startActivity(new Intent(getContext(), VideoPlay.class)
                    .putExtra("moviesId", searchDataArrayList.get(adapterPosition).getMovieId())
                    .putExtra("name", searchDataArrayList.get(adapterPosition).getMovieId()));
        }else {
            startActivity(new Intent(getContext(), VideoPlayerSeries.class)
                    .putExtra("seriesId", searchDataArrayList.get(adapterPosition).getMovieId())
                    .putExtra("name", searchDataArrayList.get(adapterPosition).getMovieName()));
            Toast.makeText(getContext(), "Web Show will open very soon!!", Toast.LENGTH_SHORT).show();
        }
    }
}