package com.example.oops.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oops.DataClass.FavouriteData;
import com.example.oops.DataClass.MovieData;
import com.example.oops.R;
import com.example.oops.custom.OTTTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavouriteHolder> {

    List<MovieData> movieData;
    private Context context;

    public FavouriteAdapter(List<MovieData> movieData, Context context) {
        this.movieData = movieData;
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

        holder.movieName.setText(movieData.get(position).getMovieName());




    }

    @Override
    public int getItemCount() {

        Log.d("FAB_SIZE", "getItemCount: "+movieData.size());
        return movieData.size();
    }

    public class FavouriteHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.movieImage)
        ImageView movieImage;

        @BindView(R.id.movieName)
        OTTTextView movieName;


        @BindView(R.id.movieDescription)
        OTTTextView movieDescription;

        public FavouriteHolder(@NonNull View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
