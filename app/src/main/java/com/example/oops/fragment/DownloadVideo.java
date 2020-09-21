package com.example.oops.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.example.oops.Ooops;
import com.example.oops.R;
import com.example.oops.Utils.AppUtil;
import com.example.oops.adapter.DownloadedVideoAdapter;
import com.example.oops.model.VideoModel;
import com.google.android.exoplayer2.offline.Download;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DownloadVideo extends Fragment {
    @BindView(R.id.txtHeading)
    AppCompatTextView txtHeading;
    @BindView(R.id.imgBackPressed)
    AppCompatImageView imgBackPressed;
    @BindView(R.id.recylerview)
    RecyclerView recylerview;
    private List<Download> downloadedVideoList;
    private DownloadedVideoAdapter downloadedVideoAdapter;
    private Runnable runnableCode;
    private Handler handler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.downloadvideofragment, container, false);
        ButterKnife.bind(this, view);
        txtHeading.setText(getString(R.string.downloads));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recylerview.setLayoutManager(linearLayoutManager);
        ((SimpleItemAnimator) recylerview.getItemAnimator()).setSupportsChangeAnimations(false);
        loadVideos();

        handler = new Handler();
        runnableCode = new Runnable() {
            @Override
            public void run() {
                List<Download> exoVideoList = new ArrayList<>();
                for (Map.Entry<Uri, Download> entry : Ooops.getInstance().getDownloadTracker().downloads.entrySet()) {
                    Uri keyUri = entry.getKey();
                    Download download = entry.getValue();
                    exoVideoList.add(download);
                }
                downloadedVideoAdapter.onNewData(exoVideoList);
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(runnableCode);
        return view;

    }

    @OnClick(R.id.imgBackPressed)
    public void setImgBackPressed() {

    }


    private void loadVideos() {
        downloadedVideoList = new ArrayList<>();

        for (Map.Entry<Uri, Download> entry : Ooops.getInstance().getDownloadTracker().downloads.entrySet()) {
            Uri keyUri = entry.getKey();
            Download download = entry.getValue();
            downloadedVideoList.add(download);
        }


        downloadedVideoAdapter = new DownloadedVideoAdapter(getContext(), DownloadVideo.this);
        recylerview.setAdapter(downloadedVideoAdapter);
        downloadedVideoAdapter.addItems(downloadedVideoList);


    }

    public void openBottomSheet(Download download){

        VideoModel videoModel = AppUtil.getVideoDetail(download.request.id);

//        String statusTitle = videoModel.getVideoName();

        View dialogView = getLayoutInflater().inflate(R.layout.fragment_bottom_sheet_dialog, null);
        BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
        dialog.setContentView(dialogView);

        TextView tvVideoTitle =  dialog.findViewById(R.id.tv_video_title);
        LinearLayout llDownloadStart = dialog.findViewById(R.id.ll_start_download);
        LinearLayout llDownloadResume = dialog.findViewById(R.id.ll_resume_download);
        LinearLayout llDownloadPause = dialog.findViewById(R.id.ll_pause_download);
        LinearLayout llDownloadDelete = dialog.findViewById(R.id.ll_delete_download);

        llDownloadStart.setVisibility(View.GONE);


        if(download.state == Download.STATE_DOWNLOADING){
            llDownloadPause.setVisibility(View.VISIBLE);
            llDownloadResume.setVisibility(View.GONE);

        }else if(download.state == Download.STATE_STOPPED){
            llDownloadPause.setVisibility(View.GONE);
            llDownloadResume.setVisibility(View.VISIBLE);

        } else if(download.state == Download.STATE_QUEUED){
            llDownloadStart.setVisibility(View.VISIBLE);
            llDownloadPause.setVisibility(View.GONE);
            llDownloadResume.setVisibility(View.GONE);
        }else {
            llDownloadStart.setVisibility(View.GONE);
            llDownloadPause.setVisibility(View.GONE);
            llDownloadResume.setVisibility(View.GONE);
        }

//        tvVideoTitle.setText(statusTitle);
        llDownloadStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ooops.getInstance().getDownloadManager().addDownload(download.request);
                dialog.dismiss();
            }
        });
        llDownloadResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ooops.getInstance().getDownloadManager().addDownload(download.request, Download.STOP_REASON_NONE);

                dialog.dismiss();
            }
        });

        llDownloadPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ooops.getInstance().getDownloadManager().addDownload(download.request, Download.STATE_STOPPED);
                dialog.dismiss();
            }
        });

        llDownloadDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ooops.getInstance().getDownloadManager().removeDownload(download.request.id);


                dialog.dismiss();
            }
        });

        dialog.show();




    }

    @Override
    public void onStop() {
        super.onStop();
        handler.removeCallbacks(runnableCode);
    }
}
