package com.example.oops.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oops.DataClass.SeasonData;
import com.example.oops.R;
import com.example.oops.activity.VideoPlayerSeries;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SeasonAdapter extends RecyclerView.Adapter<SeasonAdapter.SeasonHolder> {
    Activity activity;
    ArrayList<SeasonData> data;
    public SeasonAdapter(Activity activity, ArrayList<SeasonData> data) {
        this.activity = activity;
        this.data =data;
    }

    @NonNull
    @Override
    public SeasonHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.season_list, parent, false);
        return  new SeasonHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SeasonHolder holder, int position) {
        holder.seasonText.setText("Season "+data.get(position).getSeasonNo());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void update(ArrayList<SeasonData> data) {
        this.data = data;
    }

    public class SeasonHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.seasonText)
        TextView seasonText;
        public SeasonHolder(@NonNull View itemView) {

            super(itemView);
            ButterKnife.bind(this ,itemView);
        }
        @OnClick(R.id.seasonText)
        void setSeasonText(){
            ((VideoPlayerSeries)activity).setSeasonClick(getAdapterPosition());
        }
    }
}
