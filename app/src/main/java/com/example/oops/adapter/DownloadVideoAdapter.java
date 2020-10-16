package com.example.oops.adapter;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.oops.Ooops;
import com.example.oops.R;
import com.example.oops.Utils.AppUtil;
import com.example.oops.activity.OfflinePlayerActivity;
import com.example.oops.data.databasevideodownload.DatabaseClient;
import com.example.oops.data.databasevideodownload.VideoDownloadTable;
import com.example.oops.fragment.DownloadVideo;
import com.google.android.exoplayer2.offline.Download;
import com.google.android.exoplayer2.offline.DownloadRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DownloadVideoAdapter extends RecyclerView.Adapter<DownloadVideoAdapter.MyViewHolder> {
    private Context mCtx;
    private List<VideoDownloadTable> taskList;
    List<Download> videosList;
    VideoDownloadTable task;
    AlertDialog.Builder builder;
    Fragment fragment;

    public DownloadVideoAdapter(Context mCtx, List<VideoDownloadTable> taskList , Fragment fragment) {
        this.mCtx = mCtx;
        this.taskList = taskList;
        this.videosList = new ArrayList<>();
        this.fragment = fragment;
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


                    if (download.state == Download.STATE_DOWNLOADING) {
                        holder.tvDownloadVideoStatus.setText("Downloading..."+ percentage);
                        holder.tvDownloadVideoStatus.setTextColor(Color.parseColor("#228B22"));
                       // holder.tvDownloadVideoPercentage.setText("Size: " + AppUtil.formatFileSize(download.getBytesDownloaded()) + " | Progress: " + percentage);
                    }  else if (download.state == Download.STATE_QUEUED) {
                        holder.tvDownloadVideoStatus.setText("Waiting Downloading..." );
                        holder.tvDownloadVideoStatus.setTextColor(Color.parseColor("#228B22"));
                       // holder.tvDownloadVideoPercentage.setText("Size: " + AppUtil.formatFileSize(download.getBytesDownloaded()) + " | Progress: " + percentage);
                    }  else if (download.state == Download.STATE_FAILED) {
                        holder.tvDownloadVideoStatus.setText("Downloading Failed." );
                        holder.tvDownloadVideoStatus.setTextColor(Color.parseColor("#800000"));
                       // holder.tvDownloadVideoPercentage.setText("Size: " + AppUtil.formatFileSize(download.getBytesDownloaded()) + " | Progress: " + percentage);
                    } else {
                        holder.tvDownloadVideoStatus.setText(AppUtil.downloadStatusFromId(download));
                        holder.tvDownloadVideoStatus.setTextColor(Color.parseColor("#800000"));
                       // holder.tvDownloadVideoPercentage.setVisibility(View.INVISIBLE);
                    }

                }
            }
        }
    }

    @Override
    public void onBindViewHolder(DownloadVideoAdapter.MyViewHolder holder, int position) {
        Download download = videosList.get(position);
        VideoDownloadTable t = taskList.get(position);
        holder.txtMovieName.setText(t.getMovieName());
        holder.txtMovieType.setText(t.getMovieType());
        Glide.with(mCtx).load(t.getUrlImage()).into(holder.imgDownload);
        holder.txtDescription.setText(t.getMovieDescription());
        holder.txtTimeStamp.setText(t.getTimestamp());
        if(download.getPercentDownloaded() == 100.0) {
            daysFind(t, holder, position);
        }else {
            String percentage = AppUtil.floatToPercentage(download.getPercentDownloaded());
            if (download.state == Download.STATE_DOWNLOADING) {
                holder.tvDownloadVideoStatus.setText("Downloading...");
                holder.tvDownloadVideoStatus.setTextColor(Color.parseColor("#228B22"));
                // holder.tvDownloadVideoPercentage.setText("Size: " + AppUtil.formatFileSize(download.getBytesDownloaded()) + " | Progress: " + percentage);
            } else {
                holder.tvDownloadVideoStatus.setText(AppUtil.downloadStatusFromId(download));
                holder.tvDownloadVideoStatus.setTextColor(Color.parseColor("#800000"));
                // holder.tvDownloadVideoPercentage.setVisibility(View.INVISIBLE);
            }
        }
        checkStatus(download , holder);

    }

    private void checkStatus(Download download, MyViewHolder holder) {
        if(download.state == Download.STATE_DOWNLOADING){
            holder.llDownloadPause.setVisibility(View.VISIBLE);
            holder.llDownloadResume.setVisibility(View.GONE);

        }else if(download.state == Download.STATE_STOPPED){
            holder.llDownloadPause.setVisibility(View.GONE);
            holder.llDownloadResume.setVisibility(View.VISIBLE);

        } else if(download.state == Download.STATE_QUEUED){
            holder.llDownloadStart.setVisibility(View.VISIBLE);
            holder.llDownloadPause.setVisibility(View.GONE);
            holder.llDownloadResume.setVisibility(View.GONE);
        }else {
            holder.llDownloadStart.setVisibility(View.GONE);
            holder.llDownloadPause.setVisibility(View.GONE);
            holder.llDownloadResume.setVisibility(View.GONE);
        }

        holder.llDownloadStart.setVisibility(View.GONE);
    }

    private void daysFind(VideoDownloadTable t, MyViewHolder holder, int position) {
        long finalDaymili = TimeUnit.DAYS.toMillis(15);
        long downloadMili = Long.parseLong(t.getTimestamp());
        finalDaymili = downloadMili + finalDaymili;
        long currenttime= System.currentTimeMillis();
        if(currenttime > finalDaymili){
            // 15 days complete
            VideoDownloadTable task = taskList.get(position);
            taskList.remove(position);
            deleteTask(task);
        }else {
            // leass then 15 days
            long diff = currenttime - finalDaymili;
            long days = TimeUnit.MILLISECONDS.toDays(finalDaymili -currenttime);
            if(days >= 5) {
                holder.tvDownloadVideoStatus.setText(String.valueOf(days) + " Days Left");
                holder.tvDownloadVideoStatus.setTextColor(Color.parseColor("#228B22"));
            }
            else if(days == 0) {
                holder.tvDownloadVideoStatus.setText("Video will expire today");
                holder.tvDownloadVideoStatus.setTextColor(Color.parseColor("#800000"));
            }else{

                holder.tvDownloadVideoStatus.setText(String.valueOf(days)+" Days Left");
                holder.tvDownloadVideoStatus.setTextColor(Color.parseColor("#800000"));
            }
        }
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }
    public void addItems(List<Download> lst) {
        this.videosList = lst;
    }

    public void update(List<VideoDownloadTable> tasks) {
        taskList = tasks;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder  {
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
        AppCompatImageView llDownloadDelete;
        @BindView(R.id.llDownloadPause)
        AppCompatImageView llDownloadPause;
        @BindView(R.id.llDownloadStart)
        AppCompatImageView llDownloadStart;
        @BindView(R.id.llDownloadResume)
        AppCompatImageView llDownloadResume;

        @BindView(R.id.txtTimeStamp)
        AppCompatTextView txtTimeStamp;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            //imgDownload.setOnClickListener(this);

            /*llDownloadDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    VideoDownloadTable task = taskList.get(getAdapterPosition());
//                    deleteTask(task);

                    //Uncomment the below code to Set the message and title from the strings.xml file
//                    builder.setMessage(R.string.dialog_message) .setTitle(R.string.dialog_title);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mCtx, R.style.MyDialogTheme1);
                    alertDialogBuilder.setTitle(Html.fromHtml("<font color='#FFFFFF'>Remove movie </font>"));
                    alertDialogBuilder.setIcon(R.drawable.ic_delete);

//                    alertDialogBuilder.setMessage("Are you sure, You want to remove this movie ?");
                    alertDialogBuilder.setMessage(Html.fromHtml("<font color='#FFFFFF'>Are you sure, You want to remove this movie ?</font>"));

                    alertDialogBuilder.setPositiveButton("yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    VideoDownloadTable task = taskList.get(getAdapterPosition());
                                    taskList.remove(getAdapterPosition());
                                    Ooops.getInstance().getDownloadManager().removeDownload(videosList.get(getAdapterPosition()).request.id);
                                    deleteTask(task);
                                }
                            });

                    alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });


                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }

            });
            llDownloadStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Ooops.getInstance().getDownloadManager().addDownload(videosList.get(getAdapterPosition()).request);
                    notifyDataSetChanged();
                   // ((DownloadVideo)fragment).updateData(getAdapterPosition());
                }
            });
            llDownloadResume.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Ooops.getInstance().getDownloadManager().addDownload(videosList.get(getAdapterPosition()).request, Download.STOP_REASON_NONE);
                    notifyDataSetChanged();
                    //((DownloadVideo)fragment).updateData(getAdapterPosition());
                }
            });
            llDownloadPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Ooops.getInstance().getDownloadManager().addDownload(videosList.get(getAdapterPosition()).request ,  Download.STATE_STOPPED);
                    notifyDataSetChanged();
                   // ((DownloadVideo)fragment).updateData(getAdapterPosition());
                }
            });*/


        }

        @OnClick(R.id.img_overflow)
        void setDelete(){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mCtx, R.style.MyDialogTheme1);
            alertDialogBuilder.setTitle(Html.fromHtml("<font color='#FFFFFF'>Remove movie </font>"));
            alertDialogBuilder.setIcon(R.drawable.ic_delete);

//                    alertDialogBuilder.setMessage("Are you sure, You want to remove this movie ?");
            alertDialogBuilder.setMessage(Html.fromHtml("<font color='#FFFFFF'>Are you sure, You want to remove this movie ?</font>"));

            alertDialogBuilder.setPositiveButton("yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            VideoDownloadTable task = taskList.get(getAdapterPosition());
                            taskList.remove(getAdapterPosition());
                            Ooops.getInstance().getDownloadManager().removeDownload(videosList.get(getAdapterPosition()).request.id);
                            deleteTask(task);
                        }
                    });

            alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });


            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        @OnClick(R.id.llDownloadResume)
        void setResume(){
            Ooops.getInstance().getDownloadManager().addDownload(videosList.get(getAdapterPosition()).request, Download.STOP_REASON_NONE);
            ((DownloadVideo)fragment).updateData(getAdapterPosition());
        }
        @OnClick(R.id.llDownloadPause)
        void setLlDownloadPause(){
            Ooops.getInstance().getDownloadManager().addDownload(videosList.get(getAdapterPosition()).request ,  Download.STATE_STOPPED);
            ((DownloadVideo)fragment).updateData(getAdapterPosition());
        }

        @OnClick(R.id.imgDownload)
        void setClick( ){
            VideoDownloadTable task = taskList.get(getAdapterPosition());
            Bundle bundle = new Bundle();
            bundle.putString("video_url", task.getUrlVideo());
            Intent intent = new Intent(mCtx, OfflinePlayerActivity.class);
            intent.putExtras(bundle);
            mCtx.startActivity(intent);
            android.util.Log.i("SUNIL2", "" + task.getUrlVideo());

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
                notifyDataSetChanged();
               // getTasks();
//                Intent i = new Intent(mCtx,DownloadVideo.class);
//                mCtx.startActivity(i);

            }
        }

        DeleteTask dt = new DeleteTask();
        dt.execute();

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
               // notifyDataSetChanged();
//                Intent i = new Intent(mCtx,DownloadVideo.class);
//                mCtx.startActivity(i);
//                mCtx.startActivity(mCtx,DownloadVideo.class);

            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }


}









