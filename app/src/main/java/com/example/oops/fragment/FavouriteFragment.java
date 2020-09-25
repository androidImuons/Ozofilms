package com.example.oops.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.oops.DataClass.FavouriteData;
import com.example.oops.DataClass.MovieData;
import com.example.oops.R;
import com.example.oops.ResponseClass.FavouriteResponse;
import com.example.oops.Utils.AppCommon;
import com.example.oops.Utils.ViewUtils;
import com.example.oops.adapter.FavouriteAdapter;
import com.example.oops.retrofit.AppService;
import com.example.oops.retrofit.ServiceGenerator;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavouriteFragment extends Fragment {

    String seditTextSearchHere;

    @BindView(R.id.fab_list_recylerview)
    RecyclerView recyclerView;


    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout swiperefresh;

    Dialog dialog;
    int offset;

    FavouriteAdapter favouriteAdapter;

    ArrayList<ArrayList<MovieData>> movieDataArrayList;

    ArrayList<FavouriteData> favouriteDataArrayList;
    List<MovieData> students;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favourite_fragment, container, false);

        ButterKnife.bind(this, view);
        init();
        callFavouriteApi();

        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //searchDataArrayList.clear();
                callFavouriteApi();
            }
        });
        return view;
    }


    private void init() {
        movieDataArrayList = new ArrayList<ArrayList<MovieData>>();
        favouriteDataArrayList = new ArrayList<>();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void callFavouriteApi() {
        if (AppCommon.getInstance(getContext()).isConnectingToInternet(getContext())) {
            if(!swiperefresh.isRefreshing())
                dialog = ViewUtils.getProgressBar(getActivity());
            AppCommon.getInstance(getContext()).setNonTouchableFlags(getActivity());
            AppService apiService = ServiceGenerator.createService(AppService.class);
            Map<String, String> entityMap = new HashMap<>();
            entityMap.put("id", String.valueOf(AppCommon.getInstance(getContext()).getId()));
            entityMap.put("userId", String.valueOf(AppCommon.getInstance(getContext()).getUserId()));
            Call call;
                call = apiService.favouriteCall(entityMap);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(getContext()).clearNonTouchableFlags(getActivity());
                    if(!swiperefresh.isRefreshing())
                        dialog.dismiss();
                    else
                        swiperefresh.setRefreshing(false);

                        FavouriteResponse authResponse = (FavouriteResponse) response.body();
                        if (authResponse != null) {
                            Log.i("FABVOURITE_LIST", new Gson().toJson(authResponse));
                            if (authResponse.getCode() == 200) {
                                //setDataMovies(authResponse.getData());


                                favouriteDataArrayList= authResponse.getData();
                                

                                for(FavouriteData favouriteData:favouriteDataArrayList){

                                    movieDataArrayList.add(favouriteData.getMovie());
                                }


                              //  favouriteAdapter = new FavouriteAdapter(movieDataArrayList, getContext());
                                recyclerView.setAdapter(favouriteAdapter);




                            } else {
                                Toast.makeText(getContext(), authResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            AppCommon.getInstance(getContext()).showDialog(getActivity(), authResponse.getMessage());
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
}
