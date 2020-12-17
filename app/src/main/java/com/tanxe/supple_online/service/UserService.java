package com.tanxe.supple_online.service;

import com.tanxe.supple_online.model.User;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface UserService {

    @GET("/loginApp")
    Call<List<User>> login(@Query("Username") String username,
                           @Query("Password") String password);
//
//    @GET("getUser")
//    Call<List<User>> getAllUsers(@Query("Username") String username);

    @FormUrlEncoded
    @POST("/signUpUser")
    Call<String> signUp(@Field("Username") String username,
                        @Field("Password") String password,
                        @Field("Fullname") String fullname,
                        @Field("Phone") String phone,
                        @Field("Email") String email,
                        @Field("Type") String type,
                        @Field("Status") String status,
                        @Field("Sex") String sex);


    @FormUrlEncoded
    @POST("/updateProfile")
    Call<String> updateProfile(@Field("_id") String id,
                               @Field("Fullname") String fullname,
                               @Field("Email") String email,
                               @Field("Sex") String sex);

    @FormUrlEncoded
    @POST("/resetPass")
    Call<String> resetPass(@Field("_id") String id,
                           @Field("Username") String username,
                           @Field("Password") String pass,
                           @Field("Phone") String phone,
                           @Field("Email") String email);

    @FormUrlEncoded
    @POST("/changePass")
    Call<String> changePass(@Field("_id") String id,
                            @Field("Username") String username,
                            @Field("Password") String pass);

    @GET("GetInforUser")
    Call<List<User>> getInforUser(@Query("userOnline") String userLogin);

    //for Coach
    @GET("/getAllCoach")
    Call<List<User>> getAllCoach();

    @Multipart
    @POST("/checkInforCoach")
    Call<String> updateCoach(@Part MultipartBody.Part image,
                             @Part("_id") RequestBody _id,
                             @Part("Fullname") RequestBody fullname,
                             @Part("Workplace") RequestBody Workplace,
                             @Part("Age") RequestBody Age,
                             @Part("Specialized") RequestBody Specialized,
                             @Part("Background") RequestBody Background,
                             @Part("Status") RequestBody status,
                             @Part("Token") RequestBody token,
                             @Part("Username") RequestBody username,
                             @Part("Address") RequestBody address);

    @GET("/getCoachForType")
    Call<List<User>> getCoachForType(@Query("Specialized") List<String> specialized);

    @GET("/getHotCoach")
    Call<List<User>> getHotCoach();

    @FormUrlEncoded
    @POST("/verifyPhoneNo")
    Call<String> verifyPhoneNo(@Field("Username") String username,
                               @Field("Phone") String phone,
                               @Field("Email") String email);

    @FormUrlEncoded
    @POST("/switchCoach")
    Call<String> switchCoach(@Field("Status") String status,
                             @Field("_id") String id);

    @FormUrlEncoded
    @POST("/checkPhoneNo")
    Call<String> checkPhoNo(@Field("Phone") String phone);

    @FormUrlEncoded
    @POST("/getInforFromPhone")
    Call<List<User>> getInforFromPhone(@Field("Phone") String phone);


    @FormUrlEncoded
    @POST("/matchCoach")
    Call<List<User>> matchCoach(@Field("Workplace") String Workplace,
                                @Field("Specialized") String Specialized,
                                @Field("Sex") String Sex,
                                @Field("AgeFrom") String AgeFrom,
                                @Field("AgeTo") String AgeTo);


}
