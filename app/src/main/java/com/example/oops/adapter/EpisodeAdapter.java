package com.example.oops.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oops.DataClass.EpisodeData;
import com.example.oops.R;
import com.example.oops.Utils.AppCommon;
import com.example.oops.activity.VideoPlay;
import com.facebook.drawee.view.SimpleDraweeView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.EpisodeHolder> {
    Activity activity;
    ArrayList<EpisodeData> episodeDataArrayList;
    Context context;

    public EpisodeAdapter(Activity activity, Context context,ArrayList<EpisodeData> episodeDataArrayList) {
        this.activity = activity;
        this.episodeDataArrayList = episodeDataArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public EpisodeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.season_item, parent, false);
        return  new EpisodeHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EpisodeHolder holder, int position) {
        holder.epiNo.setText("Episode "+episodeDataArrayList.get(position).getEpisodeNo());
        holder.epiName.setText(episodeDataArrayList.get(position).getEpisodeName());
        if(episodeDataArrayList.get(position).getThumbnailLink() != null && !episodeDataArrayList.get(position).getThumbnailLink().isEmpty() )
            holder.sdvImage.setController(AppCommon.getInstance(activity).getDraweeController(holder.sdvImage ,episodeDataArrayList.get(position).getThumbnailLink() , 300));


    }

    @Override
    public int getItemCount() {
        return episodeDataArrayList.size();
    }

    public void update(ArrayList<EpisodeData> episodeDataArrayList) {
        this.episodeDataArrayList = episodeDataArrayList;
        notifyDataSetChanged();
    }

    public class EpisodeHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.epiNo)
        TextView epiNo;

        @BindView(R.id.epiName)
        TextView epiName;

        @BindView(R.id.sdvImage)
        SimpleDraweeView sdvImage;
        @BindView(R.id.relativeLayout)
        RelativeLayout relativeLayout;


        public EpisodeHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this ,itemView);
        }
    }

}
