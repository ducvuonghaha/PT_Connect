package com.tanxe.supple_online.service;

import com.tanxe.supple_online.model.Notification;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NotificationService {

    @GET("/getAllNotification")
    Call<List<Notification>> getAllNotification(@Query("Username") String username);
}
