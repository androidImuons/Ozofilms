package com.example.oops.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

public class UserRegistration extends AppCompatActivity {

    @BindView(R.id.editTextName)
    EditText editTextName;
    @BindView(R.id.editTextEmailId)
    EditText editTextEmailId;
    @BindView(R.id.editTextCountry)
    EditText editTextCountry;
    @BindView(R.id.editTextPhoneNumber)
    EditText editTextPhoneNumber;
    @BindView(R.id.editTextPassWord)
    EditText editTextPassWord;
    @BindView(R.id.checkBox2)
    CheckBox checkBox2;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_registration);
        ButterKnife.bind(this);

    }

    public void register(View view) {
        String name = editTextName.getText().toString().trim();
        String emailId = editTextEmailId.getText().toString().trim();
        String phoneNumber = editTextPhoneNumber.getText().toString().trim();
        String password = editTextPassWord.getText().toString().trim();

        if(name.isEmpty())
            editTextName.setError("Please enter user name");
        else if(emailId.isEmpty())
            editTextEmailId.setError("Please enter email id");
        else if(phoneNumber.isEmpty()){
            editTextPhoneNumber.setError("Please enter phone number");
        }else if(password.isEmpty()){
            editTextPassWord.setError("Please enter password");
        }else
            callApi(name , emailId,password,phoneNumber);

    }

    private void callApi(String name, String emailId, String password, String phoneNumber) {
        if (AppCommon.getInstance(this).isConnectingToInternet(this)) {
            final Dialog dialog = ViewUtils.getProgressBar(UserRegistration.this);
            AppCommon.getInstance(this).setNonTouchableFlags(this);
            AppService apiService = ServiceGenerator.createService(AppService.class);
            Call call = apiService.RegisterApiCall(new RegistrationEntity( name,emailId, password, phoneNumber, "free"));
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(UserRegistration.this).clearNonTouchableFlags(UserRegistration.this);
                    dialog.dismiss();
                    RegistrationResponse authResponse = (RegistrationResponse) response.body();
                    if (authResponse != null) {
                        Log.i("Response::", new Gson().toJson(authResponse));
                        if (authResponse.getSuccess() == 200) {
                            AppCommon.getInstance(UserRegistration.this).setToken(authResponse.getData().getToken());
                            AppCommon.getInstance(UserRegistration.this).setUserLogin(authResponse.getData().getUserId(), true);
                           // startActivity(new Intent(UserRegistration.this, .class));
                            // callLoginApi(new LoginEntity(authResponse.getData().getUserId(), authResponse.getData().getPassword() , fireBase));
                        } else {
                            Toast.makeText(UserRegistration.this, authResponse.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        AppCommon.getInstance(UserRegistration.this).showDialog(UserRegistration.this, "Server Error");
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    dialog.dismiss();
                    AppCommon.getInstance(UserRegistration.this).clearNonTouchableFlags(UserRegistration.this);
                    // loaderView.setVisibility(View.GONE);
                    Toast.makeText(UserRegistration.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            });


        } else {
            // no internet
            Toast.makeText(this, "Please check your internet", Toast.LENGTH_SHORT).show();
        }
    }
}
