package com.example.oops.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.oops.R;
import com.example.oops.fragment.DownloadVideo;
import com.example.oops.fragment.HomeFragment;
import com.example.oops.fragment.MoreScreenFragment;
import com.example.oops.fragment.SearchHere;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Dashboard extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;
    BottomNavigationView bottomNavigationView;

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
                fragment = new HomeFragment();
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
}


