package com.example.oops.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.oops.DataClass.CategoryListData;
import com.example.oops.DataClass.MoviesData;
import com.example.oops.R;
import com.example.oops.fragment.HomeFragment;
import com.example.oops.model.SearchModel;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class  MostViewAdapter extends RecyclerView.Adapter<MostViewAdapter.MyViewHolder> {
    Fragment fragment;
    ArrayList<CategoryListData> moviesDataArrayList;


    public MostViewAdapter(Fragment fragment, ArrayList<CategoryListData> moviesDataArrayList) {
        this.fragment = fragment;
        this.moviesDataArrayList = moviesDataArrayList;
    }

    @Override
    public MostViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_movies_item, parent, false);

        return new MostViewAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MostViewAdapter.MyViewHolder holder, int position) {
        /*final SearchModel loanModel = searchModelList.get(position);

        Glide.with(context).load(searchModelList.get(position).getImage()).into(holder.imgMostViewed);*/
        holder.imgMostViewed.setImageURI(moviesDataArrayList.get(position).getImageLink());
    }

    @Override
    public int getItemCount() {
        return moviesDataArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.movie_img)
        SimpleDraweeView imgMostViewed;


        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }

        @OnClick(R.id.movie_img)
        void setImgMostViewed(){
            ((HomeFragment)fragment).moviesDeatils(moviesDataArrayList.get(getAdapterPosition()).getMovieId() ,moviesDataArrayList.get(getAdapterPosition()).getMovieName() );
        }
    }




}

