package com.example.oops.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oops.DataClass.MoviesData;
import com.example.oops.DataClass.PlansData;
import com.example.oops.R;
import com.example.oops.custom.OTTTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubscriptionPlanAdapter extends RecyclerView.Adapter<SubscriptionPlanAdapter.SubscriptionViewHolder>{

    ArrayList<PlansData> plansDataArrayList;
    Context context;


    public SubscriptionPlanAdapter(ArrayList<PlansData> plansDataArrayList, Context context) {
        this.plansDataArrayList = plansDataArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public SubscriptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.subscription_offers_item_lists, parent, false);
        return new SubscriptionPlanAdapter.SubscriptionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SubscriptionViewHolder holder, int position) {



        holder.durationType.setText(plansDataArrayList.get(position).getDurationType());
        holder.duration.setText(""+plansDataArrayList.get(position).getDuration());
        holder.planCost.setText(""+plansDataArrayList.get(position).getPlanCost());
        holder.description1.setText(plansDataArrayList.get(position).getDescription());
        holder.discountAmount.setPaintFlags(holder.discountAmount.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);



    }

    @Override
    public int getItemCount()
    {

        Log.d("SDVOVDOVDOVSD", "getItemCount: "+plansDataArrayList.size());
        return plansDataArrayList.size();
    }

    public void update(ArrayList<PlansData> plansDataArrayList) {
        this.plansDataArrayList = plansDataArrayList;
        notifyDataSetChanged();
    }

    class SubscriptionViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tVduration)
        OTTTextView duration;

        @BindView(R.id.tVdurationType)
        OTTTextView durationType;


        @BindView(R.id.tVplanCost)
        OTTTextView planCost;

        @BindView(R.id.tVdescription1)
        OTTTextView description1;



        @BindView(R.id.tVdiscountAmount)
        OTTTextView discountAmount;


        public SubscriptionViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
