package com.example.oops.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oops.DataClass.MoviesData;
import com.example.oops.R;
import com.example.oops.fragment.HomeFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeMoviesAdapter extends RecyclerView.Adapter<HomeMoviesAdapter.HomeHolder> {
    Fragment fragment;
    ArrayList<MoviesData> moviesDataArrayList;
    MostViewAdapter mostViewAdapter;

    public HomeMoviesAdapter(Fragment fragment, ArrayList<MoviesData> moviesDataArrayList) {
        this.fragment = fragment;
        this.moviesDataArrayList = moviesDataArrayList;
    }

    @NonNull
    @Override
    public HomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cat_movies_item, parent, false);

        return new HomeHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeHolder holder, int position) {
        holder.catName.setText(moviesDataArrayList.get(position).getCategoryName());
        mostViewAdapter = new MostViewAdapter(fragment, moviesDataArrayList.get(position).getCategoryList());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(fragment.getContext(), LinearLayoutManager.HORIZONTAL, false);
        holder.recylerview.setLayoutManager(mLayoutManager);
        holder.recylerview.setItemAnimator(new DefaultItemAnimator());
        holder.recylerview.setAdapter(mostViewAdapter);
    }

    @Override
    public int getItemCount() {
        return moviesDataArrayList.size();
    }

    public void update(ArrayList<MoviesData> moviesDataArrayList) {
        this.moviesDataArrayList = moviesDataArrayList;
        notifyDataSetChanged();
    }

    public class HomeHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.catName)
        TextView catName;

        @BindView(R.id.recylerviewMostViewed)
        RecyclerView recylerview;

        @BindView(R.id.catList)
        LinearLayout catList;

        public HomeHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.catList)
        void  setCatList(){
            ((HomeFragment)fragment).catList(getAdapterPosition());
        }
    }
}
