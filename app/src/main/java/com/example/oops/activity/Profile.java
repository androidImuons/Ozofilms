package com.example.oops.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.example.oops.EntityClass.LoginEntity;
import com.example.oops.EntityClass.ProfileEntity;
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

public class Profile extends AppCompatActivity {
    @BindView(R.id.txtHeading)
    AppCompatTextView txtHeading;
    @BindView(R.id.editTextName)
    AppCompatEditText editTextName;
    String seditTextName;
    @BindView(R.id.editTextMobielNumber)
    AppCompatEditText editTextMobielNumber;
    String seditTextMobielNumber;
    @BindView(R.id.editTextEmail)
    AppCompatEditText editTextEmail;
    String seditTextEmail;
    @BindView(R.id.editTextPassWord)
    AppCompatEditText editTextPassWord;
    String seditTextPassWord;
    @BindView(R.id.editTextConfirmPassword)
    AppCompatEditText editTextConfirmPassword;
    String seditTextConfirmPassword;
    String userNameValidation = "^[A-Za-z ,.'-]+$";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        txtHeading.setText(getString(R.string.profile));
    }

    public void update(View view){
        seditTextName = editTextName.getText().toString().trim();
        seditTextMobielNumber = editTextMobielNumber.getText().toString().trim();
        seditTextEmail = editTextEmail.getText().toString().trim();
        seditTextPassWord = editTextPassWord.getText().toString().trim();
        seditTextConfirmPassword  = editTextConfirmPassword.getText().toString().trim();
        if(seditTextName.isEmpty() && !seditTextName.matches(userNameValidation))
            editTextName.setError("Please Enter Valid Name");
       else if(seditTextMobielNumber.isEmpty())
            editTextMobielNumber.setError("Please enter phone number");
        else if (seditTextPassWord.isEmpty())
            editTextPassWord.setText("Please enter password");
        else    if (seditTextConfirmPassword.isEmpty() && !seditTextConfirmPassword.equals(seditTextPassWord))
                editTextConfirmPassword.setText("Please enter correct password");
            else
                callApi(seditTextName,seditTextMobielNumber,seditTextEmail,seditTextPassWord,seditTextConfirmPassword);

    }

    private void callApi(String name, String phone, String email, String password, String confirmpassword) {
        if (AppCommon.getInstance(this).isConnectingToInternet(this)) {
            final Dialog dialog = ViewUtils.getProgressBar(Profile.this);
            AppCommon.getInstance(this).setNonTouchableFlags(this);
            AppService apiService = ServiceGenerator.createService(AppService.class);

            Call call = apiService.ProfileApi(new ProfileEntity( name, phone,email,password,confirmpassword));
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(Profile.this).clearNonTouchableFlags(Profile.this);
                    dialog.dismiss();
//                    RegistrationResponse change  to ProfileApiResPonse
                    RegistrationResponse authResponse = (RegistrationResponse) response.body();
                    if (authResponse != null) {
                        Log.i("Response::", new Gson().toJson(authResponse));
                        if (authResponse.getSuccess() == 200) {
//                           Response
                        } else {
                            Toast.makeText(Profile.this, authResponse.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        AppCommon.getInstance(Profile.this).showDialog(Profile.this, "Server Error");
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    dialog.dismiss();
                    AppCommon.getInstance(Profile.this).clearNonTouchableFlags(Profile.this);
                    // loaderView.setVisibility(View.GONE);
                    Toast.makeText(Profile.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}
