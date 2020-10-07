package com.example.oops.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oops.DataClass.EpisodeData;
import com.example.oops.DataClass.PlansData;
import com.example.oops.R;
import com.example.oops.Utils.AppCommon;
import com.example.oops.activity.VideoPlay;
import com.facebook.drawee.view.SimpleDraweeView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.EpisodeHolder> {

    ArrayList<EpisodeData> episodeDataArrayList;
    Context context;
    onItemClickListener mItemClickListener;

    LinearLayout layout;
    LinearLayout.LayoutParams params;

    public EpisodeAdapter(Context context, ArrayList<EpisodeData> episodeDataArrayList,onItemClickListener mItemClickListener) {
        this.episodeDataArrayList = episodeDataArrayList;
        this.context = context;
        this.mItemClickListener=mItemClickListener;
    }

    @NonNull
    @Override
    public EpisodeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.season_item, parent, false);
        return new EpisodeHolder(itemView);
    }

    private void Layout_hide() {
        params.height = 0;
        //itemView.setLayoutParams(params); //This One.
        layout.setLayoutParams(params);   //Or This one.

    }

    @Override
    public void onBindViewHolder(@NonNull EpisodeHolder holder, int position) {
        holder.epiNo.setText("Episode " + episodeDataArrayList.get(position).getEpisodeNo());
        holder.epiName.setText(episodeDataArrayList.get(position).getEpisodeName());
        if (episodeDataArrayList.get(position).getThumbnailLink() != null && !episodeDataArrayList.get(position).getThumbnailLink().isEmpty())
            holder.sdvImage.setController(AppCommon.getInstance(context).getDraweeController(holder.sdvImage, episodeDataArrayList.get(position).getThumbnailLink(), 300));


    }

    //    private void Layout_hide() {
//        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
//        params.height = 0;
//        params.width = 0;
//        holder.itemView.setLayoutParams(params);
//    }
    @Override
    public int getItemCount() {
        return episodeDataArrayList.size();
    }

    public void update(ArrayList<EpisodeData> episodeDataArrayList) {
        this.episodeDataArrayList = episodeDataArrayList;

        notifyDataSetChanged();
    }

    public class EpisodeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClickListener(view, getAdapterPosition(), episodeDataArrayList.get(getAdapterPosition()));
            }
        }
    }

    public interface onItemClickListener {
        void onItemClickListener(View view, int position,EpisodeData episodeData );
    }
}
