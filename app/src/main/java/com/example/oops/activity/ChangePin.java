package com.example.oops.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import com.example.oops.EntityClass.ChangePinEntity;
import com.example.oops.EntityClass.LoginEntity;
import com.example.oops.R;
import com.example.oops.ResponseClass.LogoutResponse;
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

public class ChangePin extends AppCompatActivity {
    @BindView(R.id.editTextOldPin)
    AppCompatEditText editTextOldPin;
    String seditTextOldPin;
    @BindView(R.id.editTextNewPin)
    AppCompatEditText editTextNewPin;
    String seditTextNewPin;
    @BindView(R.id.editTextConfirmPin)
    AppCompatEditText editTextConfirmPin;
    String seditTextConfirmPin;
    @BindView(R.id.btnSubmit)
    AppCompatButton btnSubmit;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_pin);
        ButterKnife.bind(this);
    }
    @OnClick(R.id.btnSubmit)
    public  void setBtnSubmit(){
        seditTextOldPin = editTextOldPin.getText().toString().trim();
        seditTextNewPin = editTextNewPin.getText().toString().trim();
        seditTextConfirmPin = editTextConfirmPin.getText().toString().trim();
        if(seditTextOldPin.length() <4){
            editTextOldPin.setError("Please enter Old Pin");

        }
        else if(seditTextNewPin.length() <4){
            editTextNewPin.setError("Please enter New Pin");

        }
        else if(seditTextConfirmPin.length() <4){
            editTextConfirmPin.setError("Please enter confirm Pin");
        }
        else if(!seditTextConfirmPin.matches(seditTextNewPin)){
            editTextConfirmPin.setError("Please enter correct pin");
        }
        else {
            callApi(seditTextOldPin , seditTextNewPin);

        }

    }


    private void callApi(String oldPin, String newPin) {
        if (AppCommon.getInstance(this).isConnectingToInternet(this)) {
            final Dialog dialog = ViewUtils.getProgressBar(ChangePin.this);
            AppCommon.getInstance(this).setNonTouchableFlags(this);
            AppService apiService = ServiceGenerator.createService(AppService.class);
            Call call = apiService.changePin(new ChangePinEntity(AppCommon.getInstance(this).getUserId(),oldPin, newPin,AppCommon.getInstance(this).getId()));
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(ChangePin.this).clearNonTouchableFlags(ChangePin.this);
                    dialog.dismiss();
                    LogoutResponse authResponse = (LogoutResponse) response.body();

                    if (authResponse != null) {
                        Log.i("Response::", new Gson().toJson(authResponse));
                        if (authResponse.getCode() == 200) {

                            Toast.makeText(ChangePin.this, "Change Pin Successfully", Toast.LENGTH_SHORT).show();
                         startActivity(new Intent(ChangePin.this,AppSetting.class));
                        } else {
                            Toast.makeText(ChangePin.this,authResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        AppCommon.getInstance(ChangePin.this).showDialog(ChangePin.this, authResponse.getMessage());
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    dialog.dismiss();
                    AppCommon.getInstance(ChangePin.this).clearNonTouchableFlags(ChangePin.this);

                    // loaderView.setVisibility(View.GONE);
                    Toast.makeText(ChangePin.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            });


        } else {
            // no internet
            Toast.makeText(this, "Please check your internet", Toast.LENGTH_SHORT).show();
        }
    }

}
