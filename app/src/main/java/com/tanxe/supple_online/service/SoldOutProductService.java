package com.tanxe.supple_online.service;

import com.tanxe.supple_online.model.Products;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SoldOutProductService {

    @GET("/allSoldProduct")
    Call<List<Products>> getAllSoldProducts();
}
