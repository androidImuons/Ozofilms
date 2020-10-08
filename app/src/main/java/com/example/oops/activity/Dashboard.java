package com.example.oops.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.example.oops.R;
import com.example.oops.Utils.AppCommon;
import com.example.oops.fragment.DownloadVideo;
import com.example.oops.fragment.FavouriteFragment;
import com.example.oops.fragment.HomeFragment;
import com.example.oops.fragment.MoreScreenFragment;
import com.example.oops.fragment.SearchHere;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import butterknife.BindView;

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
        Log.d("dhdhjj",""+AppCommon.getInstance(this).getNotificationObj());
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
}

