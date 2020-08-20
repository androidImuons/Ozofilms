package com.example.oops.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.oops.R;
import com.example.oops.model.SearchModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchAdapter  extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<SearchModel> searchModelList;

    public SearchAdapter(Context context, ArrayList<SearchModel> searchModelList) {
        this.searchModelList = searchModelList;
        this.context = context;
    }

    @Override
    public SearchAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_adapter, parent, false);

        return new SearchAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SearchAdapter.MyViewHolder holder, int position) {
        final SearchModel loanModel = searchModelList.get(position);
        holder.txtName.setText(loanModel.getHeading());
        Glide.with(context).load(searchModelList.get(position).getImage()).into(holder.searchRelatedImage);

    }

    @Override
    public int getItemCount() {
        return searchModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
       @BindView(R.id.searchRelatedImage)
        AppCompatImageView searchRelatedImage;
       @BindView(R.id.txtName)
        AppCompatTextView txtName;
        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }




}

