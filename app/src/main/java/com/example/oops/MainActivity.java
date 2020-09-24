package com.example.oops;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.oops.adapter.DownloadVideoAdapter;
import com.example.oops.adapter.DownloadedVideoAdapter;
import com.example.oops.data.databasevideodownload.DatabaseClient;
import com.example.oops.data.databasevideodownload.VideoDownloadTable;
import com.google.android.exoplayer2.offline.Download;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;

public class MainActivity extends Fragment {

    private RecyclerView recyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_main, container, false);
        ButterKnife.bind(this, view);
        recyclerView = view.findViewById(R.id.recyclerview_tasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        getTasks();
        return view;

    }




    private void getTasks() {
        class GetTasks extends AsyncTask<Void, Void, List<VideoDownloadTable>> {

            @Override
            protected List<VideoDownloadTable> doInBackground(Void... voids) {
                List<VideoDownloadTable> taskList = DatabaseClient
                        .getInstance(getActivity())
                        .getAppDatabase()
                        .videoDownloadDao()
                        .getAll();
                return taskList;
            }

            @Override
            protected void onPostExecute(List<VideoDownloadTable> tasks) {
                super.onPostExecute(tasks);
                DownloadVideoAdapter adapter = new DownloadVideoAdapter(getActivity(), tasks);
                recyclerView.setAdapter(adapter);
            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }

}
