package com.example.oops.activity;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import com.example.oops.DataClass.EditData;
import com.example.oops.DataClass.ResponseData;
import com.example.oops.EntityClass.ProfileEntity;
import com.example.oops.R;
import com.example.oops.ResponseClass.EditProfileResponse;
import com.example.oops.Utils.AppCommon;
import com.example.oops.Utils.ViewUtils;
import com.example.oops.retrofit.AppService;
import com.example.oops.retrofit.ServiceGenerator;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Profile extends AppCompatActivity {
    @BindView(R.id.ll_profile)
    LinearLayout ll_profile;
    @BindView(R.id.txtHeading)
    AppCompatTextView txtHeading;
    @BindView(R.id.editTextName)
    TextView editTextName;
    String seditTextName;
    @BindView(R.id.editTextMobielNumber)
    TextView editTextMobielNumber;
    String seditTextMobielNumber;
    @BindView(R.id.editTextEmail)
    TextView editTextEmail;
    String seditTextEmail;
    @BindView(R.id.imgBackPressed)
    AppCompatImageView imgBackPressed;
    @BindView(R.id.editTextPassWord)
    AppCompatEditText editTextPassWord;
    @BindView(R.id.editTextConfirmPassword)
    AppCompatEditText editTextConfirmPassword;
    String userNameValidation = "^[A-Za-z ,.'-]+$";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        ButterKnife.bind(this);
        setData();
        initView();
        imgBackPressed.setVisibility(View.VISIBLE);
    }
    @OnClick(R.id.imgBackPressed)
    public  void setImgBackPressed(){
        onBackPressed();
    }

    private void setData() {
        ResponseData userData = new Gson().fromJson(AppCommon.getInstance(this).getUserObject() , ResponseData.class);
        editTextName.setText(userData.getUserName());
        editTextMobielNumber.setText(userData.getPhone());
        editTextEmail.setText(userData.getEmail());
    }

    private void initView() {
        txtHeading.setText(getString(R.string.profile));
    }

    public void update(View view){
        seditTextName = editTextName.getText().toString().trim();
        seditTextMobielNumber = editTextMobielNumber.getText().toString().trim();
        seditTextEmail = editTextEmail.getText().toString().trim();
      /*  seditTextPassWord = editTextPassWord.getText().toString().trim();
        seditTextConfirmPassword  = editTextConfirmPassword.getText().toString().trim();*/
        if(seditTextName.isEmpty() && !seditTextName.matches(userNameValidation))
            editTextName.setError("Please Enter Valid Name");
       else if(seditTextMobielNumber.isEmpty())
            editTextMobielNumber.setError("Please enter phone number");
       else if(seditTextEmail.isEmpty())
            editTextMobielNumber.setError("Please enter emailId");
       /* else if (seditTextPassWord.isEmpty())
            editTextPassWord.setText("Please enter password");
        else    if (seditTextConfirmPassword.isEmpty())
                editTextConfirmPassword.setText("Please enter confirm password");
        else if (!seditTextConfirmPassword.equals(seditTextPassWord))
        editTextConfirmPassword.setText("Please valid password");*/
            else
                callApi(seditTextEmail,seditTextMobielNumber);

    }

    private void callApi( String email, String phone) {
        if (AppCommon.getInstance(this).isConnectingToInternet(this)) {
            final Dialog dialog = ViewUtils.getProgressBar(Profile.this);
            AppCommon.getInstance(this).setNonTouchableFlags(this);
            AppService apiService = ServiceGenerator.createService(AppService.class , AppCommon.getInstance(this).getToken());
            Call call = apiService.ProfileApi(new ProfileEntity( AppCommon.getInstance(this).getId(), AppCommon.getInstance(this).getUserId(),email,phone));
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(Profile.this).clearNonTouchableFlags(Profile.this);
                    dialog.dismiss();
//                    RegistrationResponse change  to ProfileApiResPonse
                    EditProfileResponse authResponse = (EditProfileResponse) response.body();
                    if (authResponse != null) {
                        Log.i("Response::", new Gson().toJson(authResponse));
                        if (authResponse.getCode() == 200) {
                            showSnackbar(ll_profile,authResponse.getMessage(),Snackbar.LENGTH_SHORT);
                            updateData(authResponse.getData());
//                           Response
                        } else {
                            showSnackbar(ll_profile,authResponse.getMessage(),Snackbar.LENGTH_SHORT);
                        }
                    } else {
                        AppCommon.getInstance(Profile.this).showDialog(Profile.this, "Server Error");
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    dialog.dismiss();
                    AppCommon.getInstance(Profile.this).clearNonTouchableFlags(Profile.this);
                    showSnackbar(ll_profile,getResources().getString(R.string.ServerError),Snackbar.LENGTH_SHORT);

                }
            });
        }

    }

    private void updateData(EditData data) {
        ResponseData userData = new Gson().fromJson(AppCommon.getInstance(this).getUserObject() , ResponseData.class);
        userData.setEmail(data.getEmail());
        userData.setUserName(data.getUserName());
        userData.setPhone(data.getPhone());
        AppCommon.getInstance(this).setUserObject(new Gson().toJson(userData));
    }

    public void showSnackbar(View view, String message, int duration) {
        Snackbar snackbar = Snackbar.make(view, message, duration);
        snackbar.setActionTextColor(Color.WHITE);
        snackbar.setBackgroundTint(getResources().getColor(R.color.colorPrimaryDark));
        snackbar.show();
    }

}
