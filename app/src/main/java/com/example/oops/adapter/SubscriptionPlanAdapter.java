package com.example.oops.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oops.DataClass.PlansData;
import com.example.oops.R;
import com.example.oops.ResponseClass.FavouriteResponse;
import com.example.oops.Utils.AppCommon;
import com.example.oops.Utils.ViewUtils;
import com.example.oops.custom.OTTTextView;
import com.example.oops.retrofit.AppService;
import com.example.oops.retrofit.ServiceGenerator;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubscriptionPlanAdapter extends RecyclerView.Adapter<SubscriptionPlanAdapter.SubscriptionViewHolder> {

    ArrayList<PlansData> plansDataArrayList;
    Context context;
    onItemClickListener mItemClickListener;

    public SubscriptionPlanAdapter(ArrayList<PlansData> plansDataArrayList, Context context, onItemClickListener mItemClickListener) {
        this.plansDataArrayList = plansDataArrayList;
        this.context = context;
        this.mItemClickListener = mItemClickListener;

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
        holder.duration.setText("" + plansDataArrayList.get(position).getDuration());
        holder.planCost.setText("" + plansDataArrayList.get(position).getPlanCost());
        holder.description1.setText(plansDataArrayList.get(position).getDescription());
        //  holder.discountAmount.setPaintFlags(holder.discountAmount.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        holder.planName.setText(plansDataArrayList.get(position).getPlanName());
       /* holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
          *//*  @Override
            public void onClick(View view) {

            }*//*

              //  Toast.makeText(context,"Under Construction",Toast.LENGTH_LONG).show()

        }*/
    }


    @Override
    public int getItemCount() {
        Log.d("SDVOVDOVDOVSD", "getItemCount: " + plansDataArrayList.size());
        return plansDataArrayList.size();
    }

    public void update(ArrayList<PlansData> plansDataArrayList) {
        this.plansDataArrayList = plansDataArrayList;
        notifyDataSetChanged();
    }

    class SubscriptionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.rl_subscription)
        RelativeLayout relativeLayout;

        @BindView(R.id.tVduration)
        OTTTextView duration;

        @BindView(R.id.tVdurationType)
        OTTTextView durationType;


        @BindView(R.id.tVplanCost)
        OTTTextView planCost;

        @BindView(R.id.tVdescription1)
        OTTTextView description1;

        @BindView(R.id.tVplanName)
        OTTTextView planName;

        public SubscriptionViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClickListener(view, getAdapterPosition(), plansDataArrayList.get(getAdapterPosition()));
            }
        }
    }

    public interface onItemClickListener {
        void onItemClickListener(View view, int position, PlansData plansDataArrayList);
    }
}
