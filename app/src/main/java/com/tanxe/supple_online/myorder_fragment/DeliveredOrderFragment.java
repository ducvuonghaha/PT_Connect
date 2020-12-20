package com.tanxe.supple_online.myorder_fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tanxe.supple_online.R;
import com.tanxe.supple_online.adapter.OrderAdapter;
import com.tanxe.supple_online.model.Carts;
import com.tanxe.supple_online.service.CartService;

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

public class DeliveredOrderFragment extends Fragment {

    private RecyclerView rcvMyOrderDelivered;
    private List<Carts> cartsList;
    private OrderAdapter orderAdapter;
    private LinearLayout llNoneOrderDelivered, llOrderDelivered;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delivered_order, container, false);
        initView(view);
        cartsList = new ArrayList<>();
        orderAdapter = new OrderAdapter(getContext(), cartsList);
        rcvMyOrderDelivered.setAdapter(orderAdapter);
        rcvMyOrderDelivered.setLayoutManager(new LinearLayoutManager(getContext()));
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("My Data", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        getAllCart(username);
        return view;
    }

    private void initView(View view) {
        rcvMyOrderDelivered = (RecyclerView)view.findViewById(R.id.rcvMyOrderDelivered);
        llOrderDelivered = view.findViewById(R.id.llOrderDelivered);
        llNoneOrderDelivered = view.findViewById(R.id.llNoneOrderDelivered);
    }

    private void getAllCart(String username) {
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
        CartService cartService = retrofit.create(CartService.class);
        cartService.getAllOrderDelivered(username).enqueue(new Callback<List<Carts>>() {
            @Override
            public void onResponse(Call<List<Carts>> call, Response<List<Carts>> response) {
                List<Carts> list = response.body();
                if (list.size() == 0) {
                    llNoneOrderDelivered.setVisibility(View.VISIBLE);
                    llOrderDelivered.setVisibility(View.GONE);
                } else {
                    llNoneOrderDelivered.setVisibility(View.GONE);
                    cartsList.addAll(list);
                    orderAdapter.notifyDataSetChanged();

                    Log.e("caa", cartsList.size() + "");
                }

            }

            @Override
            public void onFailure(Call<List<Carts>> call, Throwable t) {
                Log.e("XXXXXX", t.getCause() + "");
            }
        });
    }
}
