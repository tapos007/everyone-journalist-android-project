package com.example.tapos.fullapplicationdevelopment.api;

import com.example.tapos.fullapplicationdevelopment.model.CreatorInfo;
import com.example.tapos.fullapplicationdevelopment.model.LoginInformation;
import com.example.tapos.fullapplicationdevelopment.model.PostList;
import com.example.tapos.fullapplicationdevelopment.model.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by tapos on 9/26/17.
 */

public interface APIService {


    //The register call

    @POST("/api/registration")
    @FormUrlEncoded
    Call<User> createUser(
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password,
            @Field("password_confirmation") String password_confirmation,
            @Field("phone_no") String phone_no);




    @POST("/api/login")
    @FormUrlEncoded
    Call<LoginInformation> UserLogin(
            @Field("username") String name,
            @Field("password") String password);


    @GET("/api/userinfo")
    Call<CreatorInfo> GetLoggedInUserData(
            @Header("Authorization") String authorization);



    @GET("/api/posts")
    Call<PostList> GetAllPostData(
            @Query("page") int apiKey);

}
