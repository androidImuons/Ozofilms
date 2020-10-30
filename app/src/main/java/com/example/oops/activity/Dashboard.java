package com.example.oops.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.oops.EntityClass.ProfileEntity;
import com.example.oops.Ooops;
import com.example.oops.R;
import com.example.oops.ResponseClass.CommonResponse;
import com.example.oops.ResponseClass.EditProfileResponse;
import com.example.oops.Utils.AppCommon;
import com.example.oops.Utils.ViewUtils;
import com.example.oops.data.databasevideodownload.DatabaseClient;
import com.example.oops.fragment.DownloadVideo;
import com.example.oops.fragment.FavouriteFragment;
import com.example.oops.fragment.HomeFragment;
import com.example.oops.fragment.MoreScreenFragment;
import com.example.oops.fragment.SearchHere;
import com.example.oops.retrofit.AppService;
import com.example.oops.retrofit.ServiceGenerator;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Dashboard extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.rl_dashboard)
    RelativeLayout rl_dashboard;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        loadFragment(new HomeFragment());
//        viewPager = (ViewPager) findViewById(R.id.viewPager);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.home:
                fragment = new HomeFragment();

                break;

            case R.id.explore:
                fragment = new SearchHere();
                break;

            case R.id.favourite:
                fragment = new FavouriteFragment();
                break;

            case R.id.download:
                fragment = new DownloadVideo();
                break;

            case R.id.more:
                fragment = new MoreScreenFragment();
                break;
        }

        return loadFragment(fragment);
    }

    public void showSnackbar(View view, String message, int duration) {
        Snackbar snackbar = Snackbar.make(view, message, duration);
        snackbar.setActionTextColor(Color.WHITE);
        snackbar.setBackgroundTint(getResources().getColor(R.color.colorPrimaryDark));
        snackbar.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        callCheckToken();
    }

    private void callCheckToken() {
        if (AppCommon.getInstance(this).isConnectingToInternet(this)) {
          //  final Dialog dialog = ViewUtils.getProgressBar(Dashboard.this);
            //AppCommon.getInstance(this).setNonTouchableFlags(this);
            AppService apiService = ServiceGenerator.createService(AppService.class , AppCommon.getInstance(this).getToken());
            Map<String, String> entityMap = new HashMap<>();
           // entityMap.put("deviceId", String.valueOf(AppCommon.getInstance(this).getId()));
            entityMap.put("deviceId", AppCommon.getInstance(this).getDeviceId());

            Call call = apiService.checkToken(entityMap);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    //AppCommon.getInstance(Dashboard.this).clearNonTouchableFlags(Dashboard.this);
                   // dialog.dismiss();
//                    RegistrationResponse change  to ProfileApiResPonse
                    CommonResponse authResponse = (CommonResponse) response.body();
                    if (authResponse != null) {
                        Log.i("Response::", new Gson().toJson(authResponse));
                        if (authResponse.getCode() == 200) {

//                           Response
                        } else {
                            if(authResponse.getMessage().equals("logout"))
                            logoutDailog("User already login on another device.");
                        }
                    } else {
                        AppCommon.getInstance(Dashboard.this).showDialog(Dashboard.this, "Server Error");
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                   /* dialog.dismiss();
                    AppCommon.getInstance(Dashboard.this).clearNonTouchableFlags(Dashboard.this);
                    showSnackbar(rl_dashboard,getResources().getString(R.string.ServerError),Snackbar.LENGTH_SHORT);*/

                }
            });
        }
    }

    private void logoutDailog(String message) {
       /* AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard.this);
        builder.setCancelable(false);
        builder.setTitle(this.getResources().getString(R.string.app_name));
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                AppCommon.getInstance(Dashboard.this).clearPreference();
                Ooops.getInstance().getDownloadManager().removeAllDownloads();
                DatabaseClient.getInstance(Dashboard.this).getAppDatabase().videoDownloadDao().nukeTable();
                startActivity(new Intent(Dashboard.this, Login.class));
                finishAffinity();
            }
        });
        builder.show();*/

        final Dialog dialog = ViewUtils.popUp(Dashboard.this , true);
        TextView okBtn = dialog.findViewById(R.id.ok_button);
        TextView cancel = dialog.findViewById(R.id.cancel_button);
        TextView textMsg = dialog.findViewById(R.id.textMsg);
        textMsg.setText(message);
        cancel.setVisibility(View.GONE);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                AppCommon.getInstance(Dashboard.this).clearPreference();
                Ooops.getInstance().getDownloadManager().removeAllDownloads();
                DatabaseClient.getInstance(Dashboard.this).getAppDatabase().videoDownloadDao().nukeTable();
                startActivity(new Intent(Dashboard.this, Login.class));
                finishAffinity();
            }
        });

    }
}

