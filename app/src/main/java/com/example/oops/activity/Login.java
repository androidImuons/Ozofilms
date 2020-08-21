package com.example.oops.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.oops.EntityClass.LoginEntity;
import com.example.oops.EntityClass.RegistrationEntity;
import com.example.oops.R;
import com.example.oops.ResponseClass.RegistrationResponse;
import com.example.oops.Utils.AppCommon;
import com.example.oops.Utils.ViewUtils;
import com.example.oops.retrofit.AppService;
import com.example.oops.retrofit.ServiceGenerator;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    @BindView(R.id.editTextUserName)
    EditText editTextUserName;

    @BindView(R.id.editTextPassWord)
    EditText editTextPassWord;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ButterKnife.bind(this);
    }

    public void register(View view) {
        startActivity(new Intent(this , UserRegistration.class));
    }
    private void callApi(String email, String password) {
        if (AppCommon.getInstance(this).isConnectingToInternet(this)) {
            final Dialog dialog = ViewUtils.getProgressBar(Login.this);
            AppCommon.getInstance(this).setNonTouchableFlags(this);
            AppService apiService = ServiceGenerator.createService(AppService.class);
            Call call = apiService.LoginApi(new LoginEntity( email, password));
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(Login.this).clearNonTouchableFlags(Login.this);
                    dialog.dismiss();
                    RegistrationResponse authResponse = (RegistrationResponse) response.body();
                    if (authResponse != null) {
                        Log.i("Response::", new Gson().toJson(authResponse));
                        if (authResponse.getSuccess() == 200) {
                            AppCommon.getInstance(Login.this).setToken(authResponse.getData().getToken());
                            AppCommon.getInstance(Login.this).setUserLogin(authResponse.getData().getUserId(), true);
                            AppCommon.getInstance(Login.this).setId(authResponse.getData().getId());
                            AppCommon.getInstance(Login.this).setUserObject(new Gson().toJson(authResponse.getData()));
                             startActivity(new Intent(Login.this, Dashboard.class));
                            // callLoginApi(new LoginEntity(authResponse.getData().getUserId(), authResponse.getData().getPassword() , fireBase));
                        } else {
                            Toast.makeText(Login.this,authResponse.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        AppCommon.getInstance(Login.this).showDialog(Login.this, authResponse.getMsg());
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    dialog.dismiss();
                    AppCommon.getInstance(Login.this).clearNonTouchableFlags(Login.this);
                    // loaderView.setVisibility(View.GONE);
                    Toast.makeText(Login.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            });


        } else {
            // no internet
            Toast.makeText(this, "Please check your internet", Toast.LENGTH_SHORT).show();
        }
    }

    public void login(View view) {
        String emailId = editTextUserName.getText().toString().trim();
        String pass = editTextPassWord.getText().toString().trim();
        if(emailId.isEmpty())
            editTextUserName.setError("Please enter email id");
        else if(pass.isEmpty())
            editTextPassWord.setError("Plese enter password");
        else
            callApi(emailId , pass);
    }
}
