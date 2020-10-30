package com.example.oops.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.oops.DataClass.ResponseData;
import com.example.oops.DataClass.SocialData;
import com.example.oops.EntityClass.OTPEntity;
import com.example.oops.R;
import com.example.oops.ResponseClass.CommonResponse;
import com.example.oops.ResponseClass.RegistrationResponse;
import com.example.oops.Utils.AppCommon;
import com.example.oops.Utils.ViewUtils;
import com.example.oops.retrofit.AppService;
import com.example.oops.retrofit.ServiceGenerator;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OTPActivity extends AppCompatActivity {
    @BindView(R.id.ll_enter_pin)
    LinearLayout ll_enter_pin;
    @BindView(R.id.et1)
    EditText et1;
    @BindView(R.id.et2)
    EditText et2;
    @BindView(R.id.et3)
    EditText et3;
    @BindView(R.id.et4)
    EditText et4;
    OTPEntity otpEntity;
   

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p);
        ButterKnife.bind(this);
        if (getIntent() != null) {
            otpEntity = new Gson().fromJson(getIntent().getStringExtra("data"), OTPEntity.class);



        }
        et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }


            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    et2.requestFocus();
                } else if (s.length() == 0) {
                    et1.clearFocus();
                }
            }
        });

        et2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    et3.requestFocus();
                } else if (s.length() == 0) {
                    et2.requestFocus();
                }
            }
        });

        et3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    et4.requestFocus();
                } else if (s.length() == 0) {
                    et3.requestFocus();
                }
            }
        });

        et4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() == 1) {
                    AppCommon.getInstance(OTPActivity.this).onHideKeyBoard(OTPActivity.this);
                }else if (s.length() == 0) {
                    et3.requestFocus();
                }
            }

        });
    }
    @OnClick(R.id.submitIssue)
    void setPin() {
        String p1 = et1.getText().toString().trim();
        String p2 = et2.getText().toString().trim();
        String p3 = et3.getText().toString().trim();
        String p4 = et4.getText().toString().trim();
        if (!p1.isEmpty() && !p3.isEmpty() && !p2.isEmpty() && !p4.isEmpty()) {
            
                callApiPin(p1, p2, p3, p4);
        }
    }

    private void callApiPin(String p1, String p2, String p3, String p4) {
        String pin = p1 + p2 + p3 + p4;
        if (AppCommon.getInstance(this).isConnectingToInternet(this)) {
            Dialog dialog = ViewUtils.getProgressBar(OTPActivity.this);
            AppCommon.getInstance(this).setNonTouchableFlags(this);
            AppService apiService = ServiceGenerator.createService(AppService.class, AppCommon.getInstance(this).getToken());
            Map<String, String> entityMap = new HashMap<>();
            entityMap.put("id", String.valueOf(otpEntity.getId()));
            entityMap.put("user_id", otpEntity.getUser_id());
            entityMap.put("otp", String.valueOf(pin));
            entityMap.put("deviceId", otpEntity.getDeviceId());
            Call call = apiService.otpApi(entityMap);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(OTPActivity.this).clearNonTouchableFlags(OTPActivity.this);
                    dialog.dismiss();
                    RegistrationResponse authResponse = (RegistrationResponse) response.body();
                    if (authResponse != null) {
                        Log.i("Response::", new Gson().toJson(authResponse));
                        if (authResponse.getSuccess() == 200) {
                            AppCommon.getInstance(OTPActivity.this).setToken(authResponse.getData().getToken());
                            AppCommon.getInstance(OTPActivity.this).setUserLogin(authResponse.getData().getUserId(), true);
                            AppCommon.getInstance(OTPActivity.this).setId(authResponse.getData().getId());
                            AppCommon.getInstance(OTPActivity.this).setUserObject(new Gson().toJson(authResponse.getData()));
                            AppCommon.getInstance(OTPActivity.this).setDeviceId(otpEntity.getDeviceId());
                            startActivity(new Intent(OTPActivity.this, Dashboard.class));
                            finishAffinity();
                        } else {
                            et1.setText("");
                            et2.setText("");
                            et3.setText("");
                            et4.setText("");
                            et1.requestFocus();
                            showSnackbar(ll_enter_pin,authResponse.getMsg(), Snackbar.LENGTH_SHORT);
                        }
                    } else {
                        AppCommon.getInstance(OTPActivity.this).showDialog(OTPActivity.this, "Server Error");
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    dialog.dismiss();
                    AppCommon.getInstance(OTPActivity.this).clearNonTouchableFlags(OTPActivity.this);
                    // loaderView.setVisibility(View.GONE);
                    showSnackbar(ll_enter_pin,getResources().getString(R.string.ServerError),Snackbar.LENGTH_SHORT);
                }
            });


        } else {
            showSnackbar(ll_enter_pin,getResources().getString(R.string.NoInternet),Snackbar.LENGTH_SHORT);
        }


    }
    public void showSnackbar(View view, String message, int duration) {
        Snackbar snackbar = Snackbar.make(view, message, duration);
        snackbar.setActionTextColor(Color.WHITE);
        snackbar.setBackgroundTint(getResources().getColor(R.color.colorPrimaryDark));
        snackbar.show();
    }

}