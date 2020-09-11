package com.example.oops.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.oops.DataClass.SliderData;
import com.example.oops.R;
import com.example.oops.Utils.AppCommon;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

public class ViewPagerAdapter   extends PagerAdapter {

    private Activity context;
    private LayoutInflater layoutInflater;
    private Integer [] images = {R.drawable.header_background,R.drawable.header_background,R.drawable.header_background,R.drawable.header_background};
    ArrayList<SliderData> sliderData;
    public ViewPagerAdapter(Activity context, ArrayList<SliderData> dots) {
        this.context = context;
        sliderData = dots;
    }

    @Override
    public int getCount() {
        return sliderData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.custom_layout, null);
        SimpleDraweeView imageView = (SimpleDraweeView) view.findViewById(R.id.imageView);
        //imageView.setColAppCommon.getInstance(context).getDraweeController(imageView, sliderData.get(position).getImageLink(),500);
        imageView.setImageURI(sliderData.get(position).getBannerLink());

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }
}

