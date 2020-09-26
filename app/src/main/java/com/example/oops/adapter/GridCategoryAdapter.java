package com.example.oops.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oops.DataClass.CategoryListData;
import com.example.oops.R;
import com.example.oops.ResponseClass.CategoryResponse;
import com.example.oops.Utils.AppCommon;
import com.example.oops.activity.CategoryListActivity;
import com.example.oops.activity.VideoPlay;
import com.example.oops.fragment.HomeFragment;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GridCategoryAdapter extends RecyclerView.Adapter<GridCategoryAdapter.CategoryHolder> {

    Activity activity;
    ArrayList<CategoryListData> categoryList;

    public GridCategoryAdapter(Activity activity, ArrayList<CategoryListData> categoryList) {
        this.activity = activity;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_item, parent, false);

        return new CategoryHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
        if(categoryList.get(position).getImageLink() != null && !categoryList.get(position).getImageLink().isEmpty() )
            holder.moviesImage.setController( AppCommon.getInstance(activity).getDraweeController(holder.moviesImage , categoryList.get(position).getImageLink() , 500));
        holder.moviesNmae.setText(categoryList.get(position).getMovieName());
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public void update(ArrayList<CategoryListData> data) {
        categoryList = data;
        notifyDataSetChanged();
    }

    public class CategoryHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.moviesImage)
        SimpleDraweeView moviesImage;
        @BindView(R.id.moviesNmae)
        TextView moviesNmae;

        public CategoryHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

    }
}
