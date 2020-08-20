package com.example.oops.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.oops.R;
import com.example.oops.model.SearchModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebSeriesAdapter  extends RecyclerView.Adapter<WebSeriesAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<SearchModel> searchModelList;

    public WebSeriesAdapter(Context context, ArrayList<SearchModel> searchModelList) {
        this.searchModelList = searchModelList;
        this.context = context;
    }

    @Override
    public WebSeriesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.webseriesadapter, parent, false);

        return new WebSeriesAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(WebSeriesAdapter.MyViewHolder holder, int position) {
        final SearchModel loanModel = searchModelList.get(position);

        Glide.with(context).load(searchModelList.get(position).getImage()).into(holder.imgWebSeries);

    }

    @Override
    public int getItemCount() {
        return searchModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imgWebSeries)
        AppCompatImageView imgWebSeries;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }




}

