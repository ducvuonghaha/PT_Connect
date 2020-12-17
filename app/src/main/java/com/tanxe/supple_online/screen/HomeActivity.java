package com.tanxe.supple_online.screen;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.User;
import com.tanxe.supple_online.R;
import com.tanxe.supple_online.helper.BaseActivity;
import com.tanxe.supple_online.main_fragment.FindFragment;
import com.tanxe.supple_online.main_fragment.HomeFragment;
import com.tanxe.supple_online.main_fragment.MallFragment;
import com.tanxe.supple_online.main_fragment.NotificationFrament;
import com.tanxe.supple_online.service.UserService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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

public class HomeActivity extends BaseActivity {
    List<com.tanxe.supple_online.model.User> userList;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectFragment;
            SharedPreferences sharedPreferences = getSharedPreferences("My Data", MODE_PRIVATE);
            String id = sharedPreferences.getString("id", "");
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    if (id==null || id==""){
                        break;
                    }else {
                        selectFragment = new HomeFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, selectFragment).commit();
                        break;
                    }
                case R.id.navigation_mall:
                    if (id==null || id==""){
                        break;
                    }else {
                        selectFragment = new MallFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, selectFragment).commit();
                        break;
                    }
                case R.id.navigation_notifications:
                    if (id==null || id==""){
                        break;
                    }else {
                        selectFragment = new NotificationFrament();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, selectFragment).commit();
                        break;
                    }
                case R.id.navigation_finding_coach:
                    if (id==null || id==""){
                        break;
                    }else {
                        selectFragment = new FindFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, selectFragment).commit();
                        break;
                    }
            }
            return true;
        }
    };

    @Override
    public void onBackPressed() {
        return;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_user);
        BottomNavigationView navigation = findViewById(R.id.navigationUser);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, new HomeFragment()).commit();
        userList = new ArrayList<>();
        SharedPreferences sharedPreferences = getSharedPreferences("My Data", MODE_PRIVATE);
        String id = sharedPreferences.getString("id", "");
        String username = sharedPreferences.getString("username", "");
        getInformationUser(username);


    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences("My Data", MODE_PRIVATE);
        String id = sharedPreferences.getString("id", "");
        loginCometChat(id);
    }

    private void loginCometChat(String Uid) {
        if (CometChat.getLoggedInUser() == null) {
            CometChat.login(Uid, "442730f8eccc9a4e48f6df5eaf59c6c838ca2bf6", new CometChat.CallbackListener<User>() {
                @Override
                public void onSuccess(User user) {
//                    startActivity(new Intent(MainActivity.this, CometChatUnified.class));
                }

                @Override
                public void onError(CometChatException e) {
                    Log.e("Fail", e.getDetails() + "");
                }
            });
        } else {
            CometChat.login(Uid, "442730f8eccc9a4e48f6df5eaf59c6c838ca2bf6", new CometChat.CallbackListener<User>() {
                @Override
                public void onSuccess(User user) {
                    Log.e("AAA", user.getUid());
                }

                @Override
                public void onError(CometChatException e) {
                    Log.e("Fail", e.getDetails() + "");
                }
            });
        }
    }

    private void createUserCometChat(String Uid, String name) {
        com.cometchat.pro.models.User user = new com.cometchat.pro.models.User();
        user.setUid(Uid);
        user.setName(name);
        CometChat.createUser(user, "442730f8eccc9a4e48f6df5eaf59c6c838ca2bf6", new CometChat.CallbackListener<com.cometchat.pro.models.User>() {
            @Override
            public void onSuccess(com.cometchat.pro.models.User user) {

            }

            @Override
            public void onError(CometChatException e) {
                Log.e("Fail", e.getDetails() + "");
            }
        });
    }

    private void getInformationUser(String userlogin) {
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
        userService.getInforUser(userlogin).enqueue(new Callback<List<com.tanxe.supple_online.model.User>>() {
            @Override
            public void onResponse(Call<List<com.tanxe.supple_online.model.User>> call, Response<List<com.tanxe.supple_online.model.User>> response) {
                userList = response.body();
                createUserCometChat(userList.get(0).getId(), userList.get(0).getFullname());
            }

            @Override
            public void onFailure(Call<List<com.tanxe.supple_online.model.User>> call, Throwable t) {
                Log.e("XXXXXX", t.getLocalizedMessage());
            }
        });
    }
}
