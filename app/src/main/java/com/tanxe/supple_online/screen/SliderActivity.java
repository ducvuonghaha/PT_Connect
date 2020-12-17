package com.tanxe.supple_online.screen;

import androidx.viewpager.widget.ViewPager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.cometchat.pro.core.AppSettings;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.tanxe.supple_online.R;
import com.tanxe.supple_online.adapter.SliderAdapter;
import com.tanxe.supple_online.helper.BaseActivity;
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

import static com.tanxe.supple_online.screen.LoginActivity.APP_ID;
import static com.tanxe.supple_online.screen.LoginActivity.BASE_URL;

public class SliderActivity extends BaseActivity {
    List<User> userList;
    ViewPager viewPager;
    private LinearLayout mDotsLayout;
    private SliderAdapter sliderAdapter;
    private Button btnSkip;
    private TextView[] mDots;
    public String region = "us";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);
        initView();
        InitializeCometChat();
        viewPager.setAdapter(sliderAdapter);
        viewPager.addOnPageChangeListener(viewListener);
        addDot(0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences("My Data", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        String password = sharedPreferences.getString("password", "");
        login(username, password);

    }

    public void addDot(int position) {
        mDots = new TextView[3];
        mDotsLayout.removeAllViews();
        for (int i = 0; i < mDots.length; i++) {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.colorWhite));
            mDotsLayout.addView(mDots[i]);
        }

        if (mDots.length > 0) {
            mDots[position].setTextColor(getResources().getColor(R.color.colorDot));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {
        }

        @Override
        public void onPageSelected(int i) {
            addDot(i);
        }

        @Override
        public void onPageScrollStateChanged(int i) {
        }
    };

    private void initView() {
        btnSkip = findViewById(R.id.btnSkip);
        viewPager = findViewById(R.id.sliderViewPager);
        mDotsLayout = findViewById(R.id.dotsLayout);
        sliderAdapter = new SliderAdapter(this);
        btnSkip.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("My Data", MODE_PRIVATE);
                String username = sharedPreferences.getString("username", "");
                String password = sharedPreferences.getString("password", "");
                if (username.isEmpty() && password.isEmpty()) {
                    startNewActivity(LoginActivity.class);
                } else {
                    return;
                }

            }
        });
    }

    private void login(String username, String password) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        UserService userService = retrofit.create(UserService.class);

        userService.login(username, password).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                userList = new ArrayList<>();
                userList = response.body();
                Log.e("size", userList.size() + "");
                if (userList.size() > 0) {
                    startNewActivity(HomeActivity.class);
                } else if (userList.size() == 0) {
                }

            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(SliderActivity.this, "Sever hiện không hoạt động. Vui lòng đợi!", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void InitializeCometChat() {
        AppSettings appSettings = new AppSettings.AppSettingsBuilder().subscribePresenceForAllUsers().setRegion(region).build();

        CometChat.init(this, APP_ID, appSettings, new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String successMessage) {
            }

            @Override
            public void onError(CometChatException e) {
                Log.e("aaaaa", e.getLocalizedMessage());
            }
        });
    }
}