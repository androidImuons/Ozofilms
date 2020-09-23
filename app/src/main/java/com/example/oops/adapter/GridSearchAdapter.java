package com.example.oops.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oops.DataClass.SearchData;
import com.example.oops.R;
import com.example.oops.Utils.AppCommon;
import com.example.oops.activity.VideoPlay;
import com.example.oops.fragment.SearchHere;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GridSearchAdapter extends RecyclerView.Adapter<GridSearchAdapter.SearchHolder> {

    Fragment fragment;
    ArrayList<SearchData> searchDataArrayList;
    int offsetLevel = 29;
    public GridSearchAdapter(Fragment fragment, ArrayList<SearchData> searchDataArrayList) {
        this.fragment = fragment;
        this.searchDataArrayList = searchDataArrayList;
    }

    @NonNull
    @Override
    public SearchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_item, parent, false);

        return new SearchHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchHolder holder, int position) {
        if(searchDataArrayList.get(position).getImageLink() != null && !searchDataArrayList.get(position).getImageLink().isEmpty() )
        holder.moviesImage.setController( AppCommon.getInstance(fragment.getContext())
                .getDraweeController(holder.moviesImage , searchDataArrayList.get(position).getImageLink() , 500));
        holder.moviesNmae.setText(searchDataArrayList.get(position).getMovieName());

        if(position == offsetLevel){
            ((SearchHere)fragment).callFragmet(position);
        }
    }

    @Override
    public int getItemCount() {
        return searchDataArrayList.size();
    }

    public void update(ArrayList<SearchData> searchDataArrayList, int offset) {
        this.searchDataArrayList = searchDataArrayList;
        offsetLevel = 30*(offset+1)-1;
        notifyDataSetChanged();
    }

    public class SearchHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.moviesImage)
        SimpleDraweeView moviesImage;
        @BindView(R.id.moviesNmae)
        TextView moviesNmae;

        public SearchHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
        @OnClick(R.id.moviesClick)
        void setMoviesNmae()
        {
            ((SearchHere)fragment).clickRelativeMovies(getAdapterPosition());
        }
    }
}
