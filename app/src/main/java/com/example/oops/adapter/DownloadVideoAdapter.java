package com.example.oops.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.oops.DataClass.CategoryListData;
import com.example.oops.Ooops;
import com.example.oops.R;
import com.example.oops.Utils.AppUtil;
import com.example.oops.activity.Dashboard;
import com.example.oops.activity.OfflinePlayerActivity;
import com.example.oops.data.databasevideodownload.DatabaseClient;
import com.example.oops.data.databasevideodownload.VideoDownloadDao;
import com.example.oops.data.databasevideodownload.VideoDownloadTable;
import com.example.oops.fragment.DownloadVideo;
import com.example.oops.model.SearchModel;
import com.example.oops.model.VideoModel;
import com.google.android.exoplayer2.offline.Download;
import com.google.android.exoplayer2.offline.DownloadRequest;
import com.google.android.exoplayer2.util.Log;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DownloadVideoAdapter  extends RecyclerView.Adapter<DownloadVideoAdapter.MyViewHolder> {
    private Context mCtx;
    private List<VideoDownloadTable> taskList;
    VideoDownloadTable task;
    AlertDialog.Builder builder;

    public DownloadVideoAdapter(Context mCtx, List<VideoDownloadTable> taskList) {
        this.mCtx = mCtx;
        this.taskList = taskList;

    }
    @Override
    public DownloadVideoAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.downloadsvideo_adapter, parent, false);

        return new DownloadVideoAdapter.MyViewHolder(itemView);
    }



    public void onBindViewHolder(DownloadVideoAdapter.MyViewHolder holder, int position, List<Object> payloads) {

        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
        } else {
            Bundle o = (Bundle) payloads.get(0);
            for (String key : o.keySet()) {
                if (key.equals("percentDownloaded")) {

                    Download download = (Download) payloads.get(position);

                    DownloadRequest downloadRequest = Ooops.getInstance().getDownloadTracker().getDownloadRequest(download.request.uri);

                    if (download.state == Download.STATE_COMPLETED) {
//                        holder.progressBarPercentage.setVisibility(View.GONE);
                    } else {
//                        holder.progressBarPercentage.setVisibility(View.VISIBLE);
//                        holder.progressBarPercentage.setProgress((int) download.getPercentDownloaded());
                    }
                    String percentage = AppUtil.floatToPercentage(download.getPercentDownloaded());
//                    String downloadInMb = AppUtil.getProgressDisplayLine(download.getBytesDownloaded(), downloadRequest.data.length);


                    if (download.state == Download.STATE_DOWNLOADING || download.state == Download.STATE_COMPLETED) {
                        holder.tvDownloadVideoPercentage.setVisibility(View.VISIBLE);
                        holder.tvDownloadVideoPercentage.setText("Size: " + AppUtil.formatFileSize(download.getBytesDownloaded()) + " | Progress: " + percentage);

                    } else {
                        holder.tvDownloadVideoPercentage.setVisibility(View.INVISIBLE);

                    }
                    holder.tvDownloadVideoStatus.setText(AppUtil.downloadStatusFromId(download));


                }
            }
        }
    }

    @Override
    public void onBindViewHolder(DownloadVideoAdapter.MyViewHolder holder, int position) {
        VideoDownloadTable t = taskList.get(position);
        holder.txtMovieName.setText(t.getMovieName());
        holder.txtMovieType.setText(t.getMovieType());
        Glide.with(mCtx).load(t.getUrlImage()).into(holder.imgDownload);
        holder.txtDescription.setText(t.getMovieId());


    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.imgDownload)
        AppCompatImageView imgDownload;
        @BindView(R.id.txtMovieName)
        AppCompatTextView txtMovieName;
        @BindView(R.id.txtMovieType)
        AppCompatTextView txtMovieType;
        @BindView(R.id.txtDescription)
AppCompatTextView txtDescription;
@BindView(R.id.tv_downloaded_percentage)
        TextView tvDownloadVideoPercentage;
@BindView(R.id.tv_downloaded_status)
        TextView tvDownloadVideoStatus;
@BindView(R.id.img_overflow)
AppCompatImageView img_overflow;
        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            imgDownload.setOnClickListener(this);

            img_overflow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    VideoDownloadTable task = taskList.get(getAdapterPosition());
                    deleteTask(task);
                }
            });

        }

        @Override
        public void onClick(View view) {
            VideoDownloadTable task = taskList.get(getAdapterPosition());
            Bundle bundle = new Bundle();
            bundle.putString("video_url", task.getUrlVideo());
            Intent intent = new Intent(mCtx, OfflinePlayerActivity.class);
            intent.putExtras(bundle);
            mCtx.startActivity(intent);
            android.util.Log.i("SUNIL2",""+task.getUrlVideo());
            Log.i("SUNIL3",""+task.getUrlVideo());
        }



    }

    private void deleteTask(VideoDownloadTable task) {
        class DeleteTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(mCtx).getAppDatabase().videoDownloadDao().delete(task);
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                Intent i = new Intent(mCtx, Dashboard.class);

            }
        }

        DeleteTask dt = new DeleteTask();
        dt.execute();

    }


    private class DeleteAsyncTask extends AsyncTask<VideoDownloadTable,Void,Void>{

        private VideoDownloadDao noteDao;

        DeleteAsyncTask(VideoDownloadDao noteDao){
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(VideoDownloadTable... notes) {
            noteDao.delete(notes[0]);
            return null;
        }
    }


    private void getTasks() {
        class GetTasks extends AsyncTask<Void, Void, List<VideoDownloadTable>> {

            @Override
            protected List<VideoDownloadTable> doInBackground(Void... voids) {
                List<VideoDownloadTable> taskList = DatabaseClient
                        .getInstance(mCtx)
                        .getAppDatabase()
                        .videoDownloadDao()
                        .getAll();
                return taskList;
            }

            @Override
            protected void onPostExecute(List<VideoDownloadTable> tasks) {
                super.onPostExecute(tasks);

            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }
    }









