package com.example.oops.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.oops.DataClass.ForgotData;
import com.example.oops.R;
import com.example.oops.ResponseClass.CommonResponse;
import com.example.oops.ResponseClass.CommonResponseObject;
import com.example.oops.ResponseClass.ForgotPassResponse;
import com.example.oops.Utils.AppCommon;
import com.example.oops.Utils.ViewUtils;
import com.example.oops.retrofit.AppService;
import com.example.oops.retrofit.ServiceGenerator;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPassword extends Activity {

    @BindView(R.id.enterOtp)
    EditText enterOtp;

    @BindView(R.id.editTextNewPin)
    EditText editTextNewPin;
    @BindView(R.id.editTextConfirmPin)
    EditText editTextConfirmPin;
    @BindView(R.id.txtHead)
    TextView txtHead;

    ForgotData forgotData = null;
    boolean isPassword;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgor_password);
        ButterKnife.bind(this);
        if(getIntent() != null){
            isPassword = getIntent().getBooleanExtra("isPassword" , true);
            forgotData = new Gson().fromJson(getIntent().getStringExtra("data") , ForgotData.class);
            init();
        }
    }

    private void init() {
        if(isPassword){
            txtHead.setText(R.string.forgot_password);
            editTextNewPin.setHint(R.string.new_password);
            editTextConfirmPin.setHint(R.string.confirm_password);
        }else {
            txtHead.setText(R.string.forgot_pin);
            editTextNewPin.setHint(R.string.new_pin);
            editTextConfirmPin.setHint(R.string.confirm_pin);
        }
    }

    @OnClick(R.id.btnSubmit)
    void setEnterOtp() {
        String enterOtpStr = enterOtp.getText().toString().trim();
        String newPass = editTextNewPin.getText().toString().trim();
        String confrimPass = editTextConfirmPin.getText().toString().trim();
        if(isPassword) {
            if (enterOtpStr.isEmpty())
                enterOtp.setError("Please enter OTP");
            else if (newPass.isEmpty())
                editTextNewPin.setError("Please enter New Password");
            else if (confrimPass.isEmpty())
                editTextNewPin.setError("Please enter Confirm Password");
            else
                callResetPassApi(enterOtpStr, newPass, confrimPass);
        }else {
            if (enterOtpStr.isEmpty())
                enterOtp.setError("Please enter OTP");
            else if (newPass.isEmpty())
                editTextNewPin.setError("Please enter New PIN");
            else if (confrimPass.isEmpty())
                editTextNewPin.setError("Please enter Confirm PIN");
            else
                callResetPinApi(enterOtpStr, newPass, confrimPass);
        }

    }

    private void callResetPinApi(String enterOtpStr, String newPass, String confrimPass) {
        if (AppCommon.getInstance(this).isConnectingToInternet(this)) {
            final Dialog dialog = ViewUtils.getProgressBar(ForgotPassword.this);
            AppCommon.getInstance(this).setNonTouchableFlags(this);
            AppService apiService = ServiceGenerator.createService(AppService.class);
            Map<String , String> entityMap = new HashMap<>();
            entityMap.put("userId" , forgotData.getUserId());
            entityMap.put("id" , String.valueOf(forgotData.getId()));
            entityMap.put("newPin" , confrimPass);
            entityMap.put("otp" , enterOtpStr);
            Call call = apiService.UpdatePin(entityMap);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(ForgotPassword.this).clearNonTouchableFlags(ForgotPassword.this);
                    dialog.dismiss();
                    CommonResponse authResponse = (CommonResponse) response.body();

                    if (authResponse != null) {
                        Log.i("Response::", new Gson().toJson(authResponse));
                        if (authResponse.getCode() == 200) {
                            Toast.makeText(ForgotPassword.this, authResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        } else {
                            Toast.makeText(ForgotPassword.this,authResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        AppCommon.getInstance(ForgotPassword.this).showDialog(ForgotPassword.this, authResponse.getMessage());
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    dialog.dismiss();
                    AppCommon.getInstance(ForgotPassword.this).clearNonTouchableFlags(ForgotPassword.this);

                    // loaderView.setVisibility(View.GONE);
                    Toast.makeText(ForgotPassword.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            });


        } else {
            // no internet
            Toast.makeText(this, "Please check your internet", Toast.LENGTH_SHORT).show();
        }
    }

    private void callResetPassApi(String enterOtpStr, String newPass, String confrimPass) {
        if (AppCommon.getInstance(this).isConnectingToInternet(this)) {
            final Dialog dialog = ViewUtils.getProgressBar(ForgotPassword.this);
            AppCommon.getInstance(this).setNonTouchableFlags(this);
            AppService apiService = ServiceGenerator.createService(AppService.class);
            Map<String , String> entityMap = new HashMap<>();
            entityMap.put("userId" , forgotData.getUserId());
            entityMap.put("id" , String.valueOf(forgotData.getId()));
            entityMap.put("newPassword" , confrimPass);
            entityMap.put("otp" , enterOtpStr);
            Call call = apiService.UpdatePassword(entityMap);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(ForgotPassword.this).clearNonTouchableFlags(ForgotPassword.this);
                    dialog.dismiss();
                    CommonResponseObject authResponse = (CommonResponseObject) response.body();

                    if (authResponse != null) {
                        Log.i("Response::", new Gson().toJson(authResponse));
                        if (authResponse.getCode() == 200) {
                            Toast.makeText(ForgotPassword.this, authResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        } else {
                            Toast.makeText(ForgotPassword.this,authResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        AppCommon.getInstance(ForgotPassword.this).showDialog(ForgotPassword.this, authResponse.getMessage());
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    dialog.dismiss();
                    AppCommon.getInstance(ForgotPassword.this).clearNonTouchableFlags(ForgotPassword.this);

                    // loaderView.setVisibility(View.GONE);
                    Toast.makeText(ForgotPassword.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            });


        } else {
            // no internet
            Toast.makeText(this, "Please check your internet", Toast.LENGTH_SHORT).show();
        }
    }
}
