package com.example.oops.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.oops.DataClass.SliderData;
import com.example.oops.R;
import com.example.oops.activity.VideoPlay;
import com.facebook.drawee.view.SimpleDraweeView;
import com.smarteist.autoimageslider.SliderViewAdapter;
import java.util.ArrayList;

public class SliderAdapter  extends
        SliderViewAdapter<SliderAdapter.SliderAdapterVH> {

    private Context context;
    ArrayList<SliderData> sliderData;

    public SliderAdapter(Context context, ArrayList<SliderData> sliderDataArrayList) {
        this.context = context;
        this.sliderData = sliderDataArrayList;
    }

    public void renewItems(ArrayList<SliderData> sliderItems) {
        this.sliderData = sliderItems;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        this.sliderData.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(SliderData sliderItem) {
        this.sliderData.add(sliderItem);
        notifyDataSetChanged();
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_layout, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {

        SliderData sliderItem = sliderData.get(position);

        viewHolder.simpleDraweeView.setImageURI(sliderItem.getBannerLink());


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (sliderData.get(position).getType("mov").equalsIgnoreCase("mov")) {
                    Intent j = new Intent(context, VideoPlay.class);
                    j.putExtra("moviesId", sliderData.get(position).getMovieId());
                    j.putExtra("name", sliderData.get(position).getMovieName());
                   j.putExtra("fav","");
                    context.startActivity(j);

//                }

            }
        });
    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return sliderData.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        SimpleDraweeView simpleDraweeView;


        public SliderAdapterVH(View itemView) {
            super(itemView);
            simpleDraweeView = itemView.findViewById(R.id.imageView);
            this.itemView = itemView;
        }
    }

}
