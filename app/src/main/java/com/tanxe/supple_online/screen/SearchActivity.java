package com.tanxe.supple_online.screen;

import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.tanxe.supple_online.R;
import com.tanxe.supple_online.adapter.CoachesAdapter;
import com.tanxe.supple_online.model.User;
import com.tanxe.supple_online.service.UserService;
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

import static com.tanxe.supple_online.screen.LoginActivity.BASE_URL;

public class SearchActivity extends AppCompatActivity {
    private EditText edtNameHLV;
    private RecyclerView rvSearchHLV;
    List<User> coachList;
    CoachesAdapter coachesAdapter;
    private ImageView btnBackFromSearchCoaches;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        coachList = new ArrayList<>();
        coachesAdapter = new CoachesAdapter(SearchActivity.this, coachList);
        rvSearchHLV.setAdapter(coachesAdapter);
        rvSearchHLV.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
        getAllCoach();
        edtNameHLV.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString().toLowerCase().trim();
                List<User> newlist = new ArrayList<>();
                for (User user : coachList) {
                    if (user.getFullname().toLowerCase().contains(text)) {
                        newlist.add(user);
                    }
                }
                coachesAdapter.updateList(newlist);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnBackFromSearchCoaches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        coachList = new ArrayList<>();
        edtNameHLV = findViewById(R.id.edtNameHLV);
        rvSearchHLV = (RecyclerView) findViewById(R.id.rvSearchHLV);
        btnBackFromSearchCoaches = (ImageView) findViewById(R.id.btnBackFromSearchCoaches);
    }

    private void getAllCoach() {
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
        UserService userService = retrofit.create(UserService.class);
        userService.getAllCoach().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                coachList.clear();
                SharedPreferences sharedPreferences =getSharedPreferences("My Data", MODE_PRIVATE);
                String id = sharedPreferences.getString("id", "");
                List<User> coaches = response.body();
                for (int i = 0; i < coaches.size(); i++) {
                    if (coaches.get(i).getId().equals(id)){
                        coaches.remove(i);
                        break;
                    }
                }
                coachList.addAll(coaches);
                coachesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e("Failure", t.getLocalizedMessage() + "");
            }
        });
    }
}