package com.example.oops.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.example.oops.EntityClass.ChangePasswordEntitiy;
import com.example.oops.EntityClass.LoginEntity;
import com.example.oops.R;
import com.example.oops.ResponseClass.CommonResponse;
import com.example.oops.ResponseClass.CommonResponseObject;
import com.example.oops.ResponseClass.RegistrationResponse;
import com.example.oops.Utils.AppCommon;
import com.example.oops.Utils.ViewUtils;
import com.example.oops.retrofit.AppService;
import com.example.oops.retrofit.ServiceGenerator;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePassword extends AppCompatActivity {

    @BindView(R.id.editTextOldPassword)
    AppCompatEditText editTextOldPassword;
    String sOldPassword;
    @BindView(R.id.editTextNewPassword)
    AppCompatEditText editTextNewPassWord;
    String sNewPassword;
    @BindView(R.id.imgBackPressed)
    AppCompatImageView imgBackPressed;
    @BindView(R.id.editTextConfirmPassword)
    AppCompatEditText editTextConfirmPassword;
    String sConfirmPassword;
    @BindView(R.id.txtHeading)
    AppCompatTextView txtHeading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changepassword);
        ButterKnife.bind(this);
        initView();
        imgBackPressed.setVisibility(View.VISIBLE);
    }
    @OnClick(R.id.imgBackPressed)
    public  void setImgBackPressed(){
        onBackPressed();
    }

    private void initView() {
        txtHeading.setText(getString(R.string.change_password));
    }


    public void update(View view) {
        sConfirmPassword = editTextConfirmPassword.getText().toString().trim();
        sOldPassword = editTextOldPassword.getText().toString().trim();
        sNewPassword = editTextNewPassWord.getText().toString().trim();

        if (sOldPassword.isEmpty())
//            editTextOldPassword.setText("Please enter old password");
            Toast.makeText(ChangePassword.this, "Please enter old password", Toast.LENGTH_SHORT).show();
        else if (sNewPassword.isEmpty())

            Toast.makeText(ChangePassword.this, "Please enter new password", Toast.LENGTH_SHORT).show();
        else if (sConfirmPassword.isEmpty())
            Toast.makeText(ChangePassword.this, "Please enter confirm password", Toast.LENGTH_SHORT).show();
        else if (!sConfirmPassword.equals(sNewPassword)) {
            Toast.makeText(ChangePassword.this, "Please enter valid password", Toast.LENGTH_SHORT).show();
        } else
            callApi(sOldPassword, sNewPassword);

    }

    private void callApi(String oldPassword, String newPassword) {

        Log.d("PASSWORDS", "callApi: "+oldPassword+ "  "+newPassword);
        if (AppCommon.getInstance(this).isConnectingToInternet(this)) {
            final Dialog dialog = ViewUtils.getProgressBar(ChangePassword.this);
            AppCommon.getInstance(this).setNonTouchableFlags(this);
            AppService apiService = ServiceGenerator.createService(AppService.class, AppCommon.getInstance(this).getToken());
            Call<CommonResponseObject> call = apiService.ChangePasswordApi(new ChangePasswordEntitiy(
                    AppCommon.getInstance(this).getUserId(), oldPassword, newPassword , String.valueOf(AppCommon.getInstance(this).getId())));

            call.enqueue(new Callback<CommonResponseObject>() {
                @Override
                public void onResponse(Call<CommonResponseObject> call, Response<CommonResponseObject> response) {
                    AppCommon.getInstance(ChangePassword.this).clearNonTouchableFlags(ChangePassword.this);
                    dialog.dismiss();
                    CommonResponseObject authResponse = response.body();
                    if (authResponse != null) {
                        Log.d("FORGOT_PASSWORD", new Gson().toJson(response));

                        if (authResponse.getCode() == 200) {
                            Toast.makeText(ChangePassword.this, authResponse.getMessage(), Toast.LENGTH_LONG).show();
                            onBackPressed();
//                          Response
                        } else {
                            Toast.makeText(ChangePassword.this, authResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                       // AppCommon.getInstance(ChangePassword.this).showDialog(ChangePassword.this, "Server Error");
                    }
                }

                @Override
                public void onFailure(Call<CommonResponseObject> call, Throwable t) {
                    dialog.dismiss();
                    AppCommon.getInstance(ChangePassword.this).clearNonTouchableFlags(ChangePassword.this);
                    // loaderView.setVisibility(View.GONE);
                    Log.d("ERROR_PASS", "onFailure: "+call.toString());
                    Toast.makeText(ChangePassword.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            });


        } else {
            // no internet
            Toast.makeText(this, "Please check your internet", Toast.LENGTH_SHORT).show();
        }
    }

}
