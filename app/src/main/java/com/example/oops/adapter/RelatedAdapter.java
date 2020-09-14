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
import com.example.oops.Utils.AppCommon;
import com.example.oops.activity.VideoPlay;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RelatedAdapter extends RecyclerView.Adapter<RelatedAdapter.RelatedHolder> {
    Activity activity;
    ArrayList<CategoryListData> categoryList;
    public RelatedAdapter(Activity videoPlay, ArrayList<CategoryListData> categoryList) {
        activity = videoPlay;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public RelatedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_item_related, parent, false);
        return  new RelatedHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RelatedHolder holder, int position) {
        holder.moviesImage.setController(AppCommon.getInstance(activity).getDraweeController(holder.moviesImage ,categoryList.get(position).getImageLink() , 500));
        holder.moviesNmae.setText(categoryList.get(position).getMovieName());
        holder.moviesNmae.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public void upadate(ArrayList<CategoryListData> data) {
        categoryList = data;
        notifyDataSetChanged();
    }

    public class RelatedHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.moviesNmae)
        TextView moviesNmae;
        @BindView(R.id.moviesImage)
        SimpleDraweeView moviesImage;

        public RelatedHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this , itemView);
        }
        @OnClick(R.id.moviesClick)
        void setMoviesNmae()
        {
            ((VideoPlay)activity).clickRelativeMovies(getAdapterPosition());
        }

    }
}
