package com.example.oops.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import com.example.oops.DataClass.ResponseData;
import com.example.oops.DataClass.SocialData;
import com.example.oops.EntityClass.LogoutEntity;
import com.example.oops.R;
import com.example.oops.ResponseClass.CommonResponse;
import com.example.oops.ResponseClass.CommonResponseObject;
import com.example.oops.ResponseClass.ForgotPassResponse;
import com.example.oops.ResponseClass.LogoutResponse;
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

public class EnterPin extends AppCompatActivity {
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
    @BindView(R.id.imgLogout)
    AppCompatImageView imgLogout;
    @BindView(R.id.forgotPin)
    TextView forgotPin;
    @BindView(R.id.tVuser)
    AppCompatTextView userName;
    SocialData socialData;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_pin);
        ButterKnife.bind(this);

        if (getIntent() != null) {
            socialData = new Gson().fromJson(getIntent().getStringExtra("data"), SocialData.class);
            imgLogout.setVisibility(View.GONE);
            forgotPin.setVisibility(View.GONE);
            forgotPin.setEnabled(false);


        }
        if(socialData==null) {
            ResponseData userData = new Gson().fromJson(AppCommon.getInstance(this).getUserObject() , ResponseData.class);

            imgLogout.setVisibility(View.VISIBLE);
            forgotPin.setVisibility(View.VISIBLE);
            userName.setText(userData.getUserName());


        }else {
            userName.setText(socialData.getUserName());
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
                    AppCommon.getInstance(EnterPin.this).onHideKeyBoard(EnterPin.this);
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
            if (socialData != null) {
                callInsertApi(p1, p2, p3, p4);
            } else
                callApiPin(p1, p2, p3, p4);
        }
    }

    private void callInsertApi(String p1, String p2, String p3, String p4) {
        String pin = p1 + p2 + p3 + p4;
        if (AppCommon.getInstance(this).isConnectingToInternet(this)) {
            Dialog dialog = ViewUtils.getProgressBar(EnterPin.this);
            AppCommon.getInstance(this).setNonTouchableFlags(this);
            AppService apiService = ServiceGenerator.createService(AppService.class, socialData.getToken());
            Map<String, String> entityMap = new HashMap<>();
            entityMap.put("id", String.valueOf(socialData.getId()));
            entityMap.put("userId", String.valueOf(socialData.getUserId()));
            entityMap.put("pin", String.valueOf(pin));
            Call call = apiService.insertPin(entityMap);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(EnterPin.this).clearNonTouchableFlags(EnterPin.this);
                    dialog.dismiss();
                    CommonResponseObject authResponse = (CommonResponseObject) response.body();
                    if (authResponse != null) {
                        Log.i("Response::", new Gson().toJson(authResponse));
                        if (authResponse.getCode() == 200) {
                            AppCommon.getInstance(EnterPin.this).setToken(socialData.getToken());
                            AppCommon.getInstance(EnterPin.this).setUserLogin(socialData.getUserId(), true);
                            AppCommon.getInstance(EnterPin.this).setId(socialData.getId());
                            AppCommon.getInstance(EnterPin.this).setUserObject(new Gson().toJson(socialData));
                            startActivity(new Intent(EnterPin.this, Dashboard.class));
                            finishAffinity();
                        } else {
                            et1.setText("");
                            et2.setText("");
                            et3.setText("");
                            et4.setText("");
                            et1.requestFocus();
                            showSnackbar(ll_enter_pin,authResponse.getMessage(),Snackbar.LENGTH_SHORT);
                        }
                    } else {
                        AppCommon.getInstance(EnterPin.this).showDialog(EnterPin.this, "Server Error");
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    dialog.dismiss();
                    AppCommon.getInstance(EnterPin.this).clearNonTouchableFlags(EnterPin.this);
                    showSnackbar(ll_enter_pin,getResources().getString(R.string.ServerError),Snackbar.LENGTH_SHORT);
                }
            });


        } else {
            showSnackbar(ll_enter_pin,getResources().getString(R.string.NoInternet),Snackbar.LENGTH_SHORT);
        }
    }

    private void callApiPin(String p1, String p2, String p3, String p4) {
        String pin = p1 + p2 + p3 + p4;
        if (AppCommon.getInstance(this).isConnectingToInternet(this)) {
            Dialog dialog = ViewUtils.getProgressBar(EnterPin.this);
            AppCommon.getInstance(this).setNonTouchableFlags(this);
            AppService apiService = ServiceGenerator.createService(AppService.class, AppCommon.getInstance(this).getToken());
            Map<String, String> entityMap = new HashMap<>();
            entityMap.put("id", String.valueOf(AppCommon.getInstance(this).getId()));
            entityMap.put("userId", String.valueOf(AppCommon.getInstance(this).getUserId()));
            entityMap.put("pin", String.valueOf(pin));
            Call call = apiService.pinApi(entityMap);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(EnterPin.this).clearNonTouchableFlags(EnterPin.this);
                    dialog.dismiss();
                    CommonResponse authResponse = (CommonResponse) response.body();
                    if (authResponse != null) {
                        Log.i("Response::", new Gson().toJson(authResponse));
                        if (authResponse.getCode() == 200) {
                            startActivity(new Intent(EnterPin.this, Dashboard.class));
                            finishAffinity();
                        } else {
                            et1.setText("");
                            et2.setText("");
                            et3.setText("");
                            et4.setText("");
                            et1.requestFocus();
                            showSnackbar(ll_enter_pin,authResponse.getMessage(),Snackbar.LENGTH_SHORT);
                        }
                    } else {
                        AppCommon.getInstance(EnterPin.this).showDialog(EnterPin.this, "Server Error");
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    dialog.dismiss();
                    AppCommon.getInstance(EnterPin.this).clearNonTouchableFlags(EnterPin.this);
                    // loaderView.setVisibility(View.GONE);
                    showSnackbar(ll_enter_pin,getResources().getString(R.string.ServerError),Snackbar.LENGTH_SHORT);
                }
            });


        } else {
            showSnackbar(ll_enter_pin,getResources().getString(R.string.NoInternet),Snackbar.LENGTH_SHORT);
        }


    }

    @OnClick(R.id.imgLogout)
    public void setImgLogout() {
        AlertDialog.Builder adb = new AlertDialog.Builder(this,R.style.MyDialogTheme1);
        // adb.setTitle(getResources().getString(R.string.app_name));
        adb.setTitle( Html.fromHtml("<font color='#FFFFFF'>TEEVOREEL</font>"));
        adb.setIcon(R.mipmap.ic_launcher_round);
        //  adb.setMessage(getResources().getString(R.string.r_u_sure_logout_message));
        adb.setMessage( Html.fromHtml("<font color='#FFFFFF'>Are you sure you want to logout? </font>"));

        adb.setPositiveButton(getResources().getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        callApi();
                        //startActivity(new Intent());
                        // finishAffinity();
                    }

                });
        adb.setNegativeButton(getString(R.string.no_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        adb.show();
    }

    private void callApi() {
        if (AppCommon.getInstance(this).isConnectingToInternet(this)) {
            final Dialog dialog = ViewUtils.getProgressBar(this);
            AppCommon.getInstance(this).setNonTouchableFlags(this);
            AppService apiService = ServiceGenerator.createService(AppService.class);
            Call call = apiService.LogoutApiCall(new LogoutEntity(AppCommon.getInstance(this).getId(), AppCommon.getInstance(this).getUserId()));
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(EnterPin.this).clearNonTouchableFlags(EnterPin.this);
                    dialog.dismiss();
                    LogoutResponse authResponse = (LogoutResponse) response.body();
                    if (authResponse != null) {
                        Log.i("Response::", new Gson().toJson(authResponse));
                        if (authResponse.getCode() == 200) {
                            AppCommon.getInstance(EnterPin.this).clearPreference();
                            startActivity(new Intent(EnterPin.this, Login.class));
                            finishAffinity();
                            showSnackbar(ll_enter_pin,getResources().getString(R.string.Logout),Snackbar.LENGTH_SHORT);
                        } else {
                            showSnackbar(ll_enter_pin,authResponse.getMessage(),Snackbar.LENGTH_SHORT);
                        }
                    } else {
                        AppCommon.getInstance(EnterPin.this).showDialog(EnterPin.this, "Server Error");
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    dialog.dismiss();
                    AppCommon.getInstance(EnterPin.this).clearNonTouchableFlags(EnterPin.this);
                    showSnackbar(ll_enter_pin,getResources().getString(R.string.ServerError),Snackbar.LENGTH_SHORT);
                }
            });
        } else {
            showSnackbar(ll_enter_pin,getResources().getString(R.string.NoInternet),Snackbar.LENGTH_SHORT);
        }
    }

    @OnClick(R.id.forgotPin)
    void setreset() {
        callforgotApi();
    }

    private void callforgotApi() {
        ResponseData userData = new Gson().fromJson(AppCommon.getInstance(this).getUserObject(), ResponseData.class);
        if (AppCommon.getInstance(this).isConnectingToInternet(this)) {
            final Dialog dialog = ViewUtils.getProgressBar(EnterPin.this);
            AppCommon.getInstance(this).setNonTouchableFlags(this);
            AppService apiService = ServiceGenerator.createService(AppService.class);
            Map<String, String> entityMap = new HashMap<>();
            entityMap.put("email", userData.getEmail());
            Call call = apiService.forgotPin(entityMap);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(EnterPin.this).clearNonTouchableFlags(EnterPin.this);
                    dialog.dismiss();
                    ForgotPassResponse authResponse = (ForgotPassResponse) response.body();
                    if (authResponse != null) {
                        Log.i("Response::", new Gson().toJson(authResponse));
                        if (authResponse.getCode() == 200) {
                            showSnackbar(ll_enter_pin,authResponse.getMessage(),Snackbar.LENGTH_SHORT);
                            startActivity(new Intent(EnterPin.this, ForgotPassword.class)
                                    .putExtra("data", new Gson().toJson(authResponse.getData()))
                                    .putExtra("isPassword", false));
                        } else {
                            showSnackbar(ll_enter_pin,authResponse.getMessage(),Snackbar.LENGTH_SHORT);
                        }
                    } else {
                        AppCommon.getInstance(EnterPin.this).showDialog(EnterPin.this, authResponse.getMessage());
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    dialog.dismiss();
                    AppCommon.getInstance(EnterPin.this).clearNonTouchableFlags(EnterPin.this);
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
