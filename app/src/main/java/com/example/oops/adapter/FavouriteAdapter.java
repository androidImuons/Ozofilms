package com.example.oops.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavouriteHolder> {
    @NonNull
    @Override
    public FavouriteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class FavouriteHolder extends RecyclerView.ViewHolder {
        public FavouriteHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
