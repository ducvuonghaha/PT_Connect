package com.tanxe.supple_online.service;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ReportService {


    @FormUrlEncoded
    @POST("/sendReportFromUser")
    Call<String> sendReportFromUser(@Field("Username") String username,
                        @Field("Content") String content,
                        @Field("Detail") String detail,
                        @Field("Token") String token);
}
