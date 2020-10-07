package com.example.oops.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.oops.DataClass.PlansData;
import com.example.oops.R;
import com.example.oops.ResponseClass.SubscriptionPlansResponse;
import com.example.oops.Utils.AppCommon;
import com.example.oops.Utils.ViewUtils;
import com.example.oops.adapter.SubscriptionPlanAdapter;
import com.example.oops.model.PaymentModel;
import com.example.oops.retrofit.AppService;
import com.example.oops.retrofit.ServiceGenerator;
import com.google.gson.Gson;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubscriptionActivity extends AppCompatActivity {

    @BindView(R.id.txtHeading)
    AppCompatTextView txtHeading;

    @BindView(R.id.Subscrib_plans_RV)
    RecyclerView recyclerView;

    SubscriptionPlanAdapter subscriptionPlanAdapter;
    ArrayList<PlansData> plansDataArrayList;
    @BindView(R.id.imgBackPressed)
    AppCompatImageView imgBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_subscription);
        ButterKnife.bind(this);
        txtHeading.setText(getString(R.string.subscription));
        init();
        loadPlansApi();
        imgBackPressed.setVisibility(View.VISIBLE);
    }

    private void init() {
        plansDataArrayList = new ArrayList<>();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(SubscriptionActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void loadPlansApi() {
        if (AppCommon.getInstance(this).isConnectingToInternet(this)) {
            final Dialog dialog = ViewUtils.getProgressBar(SubscriptionActivity.this);
            AppCommon.getInstance(this).setNonTouchableFlags(this);
            AppService apiService = ServiceGenerator.createService(AppService.class, AppCommon.getInstance(getApplicationContext()).getToken());
            Map<String, String> entityMap = new HashMap<>();
            entityMap.put("id", String.valueOf(AppCommon.getInstance(SubscriptionActivity.this).getId()));
            entityMap.put("userId", String.valueOf(AppCommon.getInstance(SubscriptionActivity.this).getUserId()));
            Call call = apiService.allSubscribtionPlansAPi(entityMap);

            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(getApplicationContext()).clearNonTouchableFlags(SubscriptionActivity.this);
                    dialog.dismiss();
                    SubscriptionPlansResponse authResponse = (SubscriptionPlansResponse) response.body();
                    if (authResponse != null) {
                        Log.e("SUBSCRIPTION_PLANS", new Gson().toJson(authResponse));
                        if (authResponse.getCode() == 200) {
                            plansDataArrayList = authResponse.getData();
                            subscriptionPlanAdapter = new SubscriptionPlanAdapter(plansDataArrayList, getApplicationContext(), new SubscriptionPlanAdapter.onItemClickListener() {
                                @Override
                                public void onItemClickListener(View view, int position, PlansData plansData) {
                                    float amount = Float.parseFloat(String.valueOf(plansData.getPlanCost()));
                                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                                    PaymentModel paymentModel = new PaymentModel();
                                    paymentModel.userId = String.valueOf(AppCommon.getInstance(SubscriptionActivity.this).getUserId());
                                    paymentModel.orderId = String.valueOf(timestamp.getTime());
                                    paymentModel.orderAmount = String.valueOf(amount);
                                    paymentModel.customerName = "Mayuri";
                                    paymentModel.customerPhone = "9922803527";
                                    paymentModel.customerEmail = "bawane1992mayuri@gmail,com";
                                    paymentModel.plan_details_id = String.valueOf(plansData.getId());
                                    openPaymentDialog(paymentModel);
                                }
                            });
                            recyclerView.setAdapter(subscriptionPlanAdapter);

                            Log.d("BWOBDOBDSD", "onResponse: " + plansDataArrayList);

                            // callLoginApi(new LoginEntity(authResponse.getData().getUserId(), authResponse.getData().getPassword() , fireBase));
                        } else {
                            Toast.makeText(SubscriptionActivity.this, authResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        AppCommon.getInstance(SubscriptionActivity.this).showDialog(SubscriptionActivity.this, authResponse.getMessage());
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    dialog.dismiss();
                    AppCommon.getInstance(SubscriptionActivity.this).clearNonTouchableFlags(SubscriptionActivity.this);
                    // loaderView.setVisibility(View.GONE);
                    Toast.makeText(SubscriptionActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // no internet
            Toast.makeText(SubscriptionActivity.this, "Please check your internet", Toast.LENGTH_SHORT).show();
        }
    }

    private void openPaymentDialog(PaymentModel paymentModel) {
        Toast.makeText(SubscriptionActivity.this, "Clicked on" + paymentModel.getOrderAmount(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(SubscriptionActivity.this, WebViewActivity.class);
        intent.putExtra("userId", String.valueOf(AppCommon.getInstance(SubscriptionActivity.this).getUserId()));
        intent.putExtra("orderId", paymentModel.getOrderId());
        intent.putExtra("orderAmount", paymentModel.getOrderAmount());
        intent.putExtra("customerName", paymentModel.getCustomerName());
        intent.putExtra("customerPhone", paymentModel.getCustomerPhone());
        intent.putExtra("customerEmail", paymentModel.customerEmail);
        intent.putExtra("plan_details_id", paymentModel.getPlan_details_id());
        startActivity(intent);
    }

    @OnClick(R.id.imgBackPressed)
    public void setImgBackPressed() {
        onBackPressed();
    }

}