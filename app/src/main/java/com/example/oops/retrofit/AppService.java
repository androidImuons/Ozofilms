package com.example.oops.retrofit;


import android.graphics.drawable.Animatable2;

import com.example.oops.DataClass.WebSearchModule;
import com.example.oops.DataClass.WebSearchResponse;
import com.example.oops.EntityClass.ChangePasswordEntitiy;
import com.example.oops.EntityClass.ChangePinEntity;
import com.example.oops.EntityClass.LoginEntity;
import com.example.oops.EntityClass.LogoutEntity;
import com.example.oops.EntityClass.ProfileEntity;
import com.example.oops.EntityClass.RegistrationEntity;
import com.example.oops.ResponseClass.AllMoviesResponse;
import com.example.oops.ResponseClass.CategoryResponse;
import com.example.oops.ResponseClass.CommonResponse;
import com.example.oops.ResponseClass.CommonResponseObject;
import com.example.oops.ResponseClass.EditProfileResponse;
import com.example.oops.ResponseClass.EpisodeResponse;
import com.example.oops.ResponseClass.FavouriteResponse;
import com.example.oops.ResponseClass.ForgotPassResponse;
import com.example.oops.ResponseClass.LogoutResponse;
import com.example.oops.ResponseClass.MovieDeatilsResponse;
import com.example.oops.ResponseClass.MoviesSearchResponse;
import com.example.oops.ResponseClass.RegistrationResponse;
import com.example.oops.ResponseClass.RelativeResponse;
import com.example.oops.ResponseClass.SeasonResponse;
import com.example.oops.ResponseClass.SliderResponse;
import com.example.oops.ResponseClass.SocialResponse;
import com.example.oops.fragment.FavouriteFragment;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
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
    Call<LogoutResponse> LogoutApiCall(@Body LogoutEntity logoutEntity);

    //    Change Password
    @POST("user/changePassword")
    Call<RegistrationResponse> ChangePasswordApi(@Body ChangePasswordEntitiy changePasswordEntitiy);

    @POST("user/editProfile")
    Call<EditProfileResponse> ProfileApi(@Body ProfileEntity profileEntity);

    @FormUrlEncoded
    @POST("video/homeSlider")
    Call<SliderResponse> sliderApi(@FieldMap Map<String, String> entityMap);

    @FormUrlEncoded
    @POST("video/getAllMovies")
    Call<AllMoviesResponse> allMoviesApi(@FieldMap Map<String, String> entityMap);

    @FormUrlEncoded
    @POST("video/moviesWithCategories")
    Call<CategoryResponse> categoryApi(@FieldMap Map<String, String> entityMap);

    @FormUrlEncoded
    @POST("user/verifyPin")
    Call<CommonResponse> pinApi(@FieldMap Map<String, String> entityMap);

    @FormUrlEncoded
    @POST("user/insertPin")
    Call<CommonResponseObject> insertPin(@FieldMap Map<String, String> entityMap);

    @FormUrlEncoded
    @POST("video/getSingleMovie")
    Call<MovieDeatilsResponse> MOVIE_DEATILS_RESPONSE_CALL(@FieldMap Map<String, String> entityMap);

    @FormUrlEncoded
    @POST("video/getRelatedMovies")
    Call<CategoryResponse> GetRelativeMovies(@FieldMap Map<String, String> entityMap);

    @POST("user/changePin")
    Call<LogoutResponse> changePin(@Body ChangePinEntity changePinEntity);

    @FormUrlEncoded
    @POST("user/forgotPassword")
    Call<ForgotPassResponse> forgotPassword(@FieldMap Map<String, String> entityMap);

    @FormUrlEncoded
    @POST("user/forgotPin")
    Call<ForgotPassResponse> forgotPin(@FieldMap Map<String, String> entityMap);

    @FormUrlEncoded
    @POST("user/resetPassword")
    Call<CommonResponseObject> UpdatePassword(@FieldMap Map<String, String> entityMap);

    @FormUrlEncoded
    @POST("user/resetPin")
    Call<CommonResponseObject> UpdatePin(@FieldMap Map<String, String> entityMap);

    @FormUrlEncoded
    @POST("video/searchVideos")
    Call<MoviesSearchResponse> searchMoviesApi(@FieldMap Map<String, String> entityMap);

    @FormUrlEncoded
    @POST("video/searchVideos")
    Call<WebSearchResponse> searchWebApi(@FieldMap Map<String, String> entityMap);

    @FormUrlEncoded
    @POST("user/socialLogin")
    Call<SocialResponse> socialLogin(@FieldMap Map<String, String> entityMap);

    @FormUrlEncoded
    @POST("video/getSeasons")
    Call<SeasonResponse> getSeasons(@FieldMap Map<String, String> entityMap);


    @FormUrlEncoded
    @POST("video/getEpisodes")
    Call<EpisodeResponse> getEdpisodes(@FieldMap Map<String, String> entityMap);

    @FormUrlEncoded
    @POST("video/addRemoveFavourite")
    Call<CommonResponse> addAndRemoveFavurite(@FieldMap Map<String, String> entityMap);

    @FormUrlEncoded
    @POST("video/myFavouriteList")
    Call<FavouriteResponse> favouriteCall(@FieldMap Map<String, String> entityMap);
}
