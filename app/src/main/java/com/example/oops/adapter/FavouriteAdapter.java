package com.example.oops.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oops.DataClass.CommonFavModule;
import com.example.oops.DataClass.FavouriteData;
import com.example.oops.DataClass.MovieData;
import com.example.oops.R;
import com.example.oops.activity.VideoPlay;
import com.example.oops.activity.VideoPlayerSeries;
import com.example.oops.custom.OTTTextView;
import com.example.oops.fragment.FavouriteFragment;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavouriteHolder> {


    Fragment fragment;
    ArrayList<CommonFavModule> commonFavModule;
    Context context;

    public FavouriteAdapter(Fragment fragment, Context context, ArrayList<CommonFavModule> commonFavModule) {
        this.fragment = fragment;
        this.commonFavModule = commonFavModule;
        this.context = context;
    }

    @NonNull
    @Override
    public FavouriteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fabvourite_movie_item_list, parent, false);
        return new FavouriteAdapter.FavouriteHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteHolder holder, int position) {

        holder.movieName.setText(commonFavModule.get(position).getName());
        holder.movieDescription.setText(commonFavModule.get(position).getShortDiscrp());
        holder.movieImage.setImageURI(commonFavModule.get(position).getImagUrl());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//if(!commonFavModule.get(position).getType("mov")=null){
                if (commonFavModule.get(position).getType("mov").equalsIgnoreCase("mov")) {
                    Intent j = new Intent(context, VideoPlay.class);
                    j.putExtra("moviesId", commonFavModule.get(position).getMovSerId());
                    j.putExtra("name", commonFavModule.get(position).getName());
                    context.startActivity(j);

                                }
                else   if (commonFavModule.get(position).getType("ser").equalsIgnoreCase("ser")) {
                    Intent j = new Intent(context, VideoPlayerSeries.class);
                    j.putExtra("seriesId", commonFavModule.get(position).getMovSerId());
                    j.putExtra("name", commonFavModule.get(position).getName());
                    context.startActivity(j);

                }


            }
        });

    }

    @Override
    public int getItemCount() {

        return commonFavModule.size();
    }

    public void update(ArrayList<CommonFavModule> commonFavModule) {
        this.commonFavModule = commonFavModule;
        notifyDataSetChanged();
    }

    public class FavouriteHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.movieImage)
        SimpleDraweeView movieImage;

        @BindView(R.id.movieName)
        OTTTextView movieName;

        @BindView(R.id.linearLayout)
        LinearLayout linearLayout;

        @BindView(R.id.movieDescription)
        OTTTextView movieDescription;

        public FavouriteHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
