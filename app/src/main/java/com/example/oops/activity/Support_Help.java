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
import com.example.oops.EntityClass.SupportHelpEntity;
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

public class Support_Help extends AppCompatActivity {
    @BindView(R.id.txtHeading)
    AppCompatTextView txtHeading;


    @BindView(R.id.editTextFullName)
    AppCompatEditText editTextFullName;
    String sFullName;
    @BindView(R.id.editTextMobile)
    AppCompatEditText editTextMobile;
    String sMobileNumber;
    @BindView(R.id.editTexEmail)
    AppCompatEditText editTexEmail;
    String sEmail;
    @BindView(R.id.editTextMessage)
    AppCompatEditText editTextMessage;
    String  sMessage;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.support_help);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        txtHeading.setText(getString(R.string.support_help));
    }

    public void support(View view){
        sFullName = editTextFullName.getText().toString().trim();
        sMobileNumber = editTextMobile.getText().toString().trim();
        sMessage = editTextMessage.getText().toString().trim();
        sEmail = editTexEmail.getText().toString().trim();

        if(sFullName.isEmpty())
            editTextFullName.setText("Please enter name");
       else  if (sMobileNumber.isEmpty())
            editTextMobile.setText("Please enter phone number");
       else if(sMessage.isEmpty())
           editTextMessage.setText("Please enter message");
//       else
//            callApi(sFullName , sMobileNumber,sEmail,sMessage);

    }

    private void callApi(String name, String mobile,String email,String message) {
        if (AppCommon.getInstance(this).isConnectingToInternet(this)) {
            final Dialog dialog = ViewUtils.getProgressBar(Support_Help.this);
            AppCommon.getInstance(this).setNonTouchableFlags(this);
            AppService apiService = ServiceGenerator.createService(AppService.class);
//            Change
            Call call = apiService.SupportApi(new SupportHelpEntity( name, mobile,email,message));
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(Support_Help.this).clearNonTouchableFlags(Support_Help.this);
                    dialog.dismiss();
                    RegistrationResponse authResponse = (RegistrationResponse) response.body();
                    if (authResponse != null) {
                        Log.i("Response::", new Gson().toJson(authResponse));
                        if (authResponse.getSuccess() == 200) {

                            // startActivity(new Intent(Login.this, .class));
                            // callLoginApi(new LoginEntity(authResponse.getData().getUserId(), authResponse.getData().getPassword() , fireBase));
                        } else {
                            Toast.makeText(Support_Help.this, authResponse.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        AppCommon.getInstance(Support_Help.this).showDialog(Support_Help.this, "Server Error");
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    dialog.dismiss();
                    AppCommon.getInstance(Support_Help.this).clearNonTouchableFlags(Support_Help.this);
                    // loaderView.setVisibility(View.GONE);
                    Toast.makeText(Support_Help.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            });


        } else {
            // no internet
            Toast.makeText(this, "Please check your internet", Toast.LENGTH_SHORT).show();
        }
    }

}
