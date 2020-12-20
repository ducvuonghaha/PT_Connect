package com.tanxe.supple_online.service;


import com.tanxe.supple_online.model.Carts;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface CartService {

    @POST("/upCart")
    Call<String> upCart(@Body Carts carts);

    @GET("/getCart")
    Call<List<Carts>> getAllOrder(@Query("Username") String username);

    @GET("/getDeliveringCart")
    Call<List<Carts>> getAllOrderDelivering(@Query("Username") String username);

    @GET("/getDeliveredCart")
    Call<List<Carts>> getAllOrderDelivered(@Query("Username") String username);
}
