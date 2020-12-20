package com.tanxe.supple_online.main_fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.tanxe.supple_online.R;
import com.tanxe.supple_online.adapter.NotificationAdapter;
import com.tanxe.supple_online.model.Notification;
import com.tanxe.supple_online.service.NotificationService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static android.content.Context.MODE_PRIVATE;
import static com.tanxe.supple_online.screen.LoginActivity.BASE_URL;

public class NotificationFrament extends Fragment {

    private RecyclerView rcvListNotification;
    private GridLayoutManager gridLayoutManager;
    private ProgressBar progressBarNoti;
    final Handler handler = new Handler();
    String username;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        initView(view);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("My Data", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getNotification();
            }
        }, 3000);
        return view;
    }

    private void initView(View view) {
        rcvListNotification = (RecyclerView) view.findViewById(R.id.rcvListNotification);
        progressBarNoti = (ProgressBar) view.findViewById(R.id.progressBarNoti);
    }

    private List<Notification> notificationList;
    private NotificationAdapter notificationAdapter;

    private void getNotification() {
        notificationList = new ArrayList<>();
        notificationList.clear();

        getAllNotification(username);
        notificationAdapter = new NotificationAdapter(getContext(), notificationList);
        rcvListNotification.setAdapter(notificationAdapter);
        gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
        gridLayoutManager.setAutoMeasureEnabled(true);
        rcvListNotification.setLayoutManager(gridLayoutManager);
        rcvListNotification.setHasFixedSize(true);
        rcvListNotification.setNestedScrollingEnabled(false);
        rcvListNotification.scheduleLayoutAnimation();
        notificationAdapter.notifyDataSetChanged();
    }

    private void getAllNotification(String username) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        NotificationService notificationService = retrofit.create(NotificationService.class);
        notificationService.getAllNotification(username).enqueue(new Callback<List<Notification>>() {
            @Override
            public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
                List<Notification> notificationLists = new ArrayList<>();
                notificationLists = response.body();
                progressBarNoti.setVisibility(View.GONE);
                notificationList.addAll(notificationLists);
                notificationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Notification>> call, Throwable t) {
                Log.e("Failure", t.getLocalizedMessage() + "");
            }
        });
    }
}
