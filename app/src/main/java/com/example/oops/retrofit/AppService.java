package com.example.oops.retrofit;


import com.example.oops.EntityClass.LoginEntity;
import com.example.oops.EntityClass.RegistrationEntity;
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


}
