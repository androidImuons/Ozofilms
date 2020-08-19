package com.example.oops.retrofit;


import android.graphics.drawable.Animatable2;

import com.example.oops.EntityClass.ChangePasswordEntitiy;
import com.example.oops.EntityClass.LoginEntity;
import com.example.oops.EntityClass.LogoutEntity;
import com.example.oops.EntityClass.RegistrationEntity;
import com.example.oops.ResponseClass.LogoutResponse;
import com.example.oops.ResponseClass.RegistrationResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/*
 * Created by Tabish on 19-05-2020.
 */
public interface AppService {

    @POST("user/login")
    Call<RegistrationResponse> LoginApi(@Body LoginEntity loginEntity);
 @POST("user/registration")
    Call<RegistrationResponse> RegisterApiCall(@Body RegistrationEntity loginEntity);
//Logout
    @POST("user/Logout")
Call<LogoutResponse>  LogoutApiCall(@Body LogoutEntity logoutEntity);
//    Change Password
    @POST("user/changePassword")
    Call<RegistrationResponse> ChangePasswordApi(@Body ChangePasswordEntitiy changePasswordEntitiy);
}
