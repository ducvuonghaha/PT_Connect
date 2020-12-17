package com.tanxe.supple_online.screen;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.tanxe.supple_online.R;
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

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    public static final String BASE_URL = "https://supple-connect.herokuapp.com";
    public static final String APP_ID = "268725ca7c8069b";

    private EditText edtUsername;
    private EditText edtPassword;
    private Button btnSignIn;
    private TextView tvForgotPass;
    private TextView tvDangKy;
    List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();

    }

    @Override
    public void onBackPressed() {
        return;
    }
    public void initView() {
        tvForgotPass = findViewById(R.id.tvForgotPass);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        tvDangKy = findViewById(R.id.tvDangKy);
        btnSignIn = findViewById(R.id.btnSignIn);
        tvForgotPass.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);
        tvDangKy.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == tvDangKy) {
            startNewActivity(SignUpActivity.class);
        } else if (view == btnSignIn) {
            String username = edtUsername.getText().toString();
            String password = edtPassword.getText().toString();
            if (username.isEmpty()) {
                edtUsername.setError("Tên đăng nhập không được để trống");
            } else if (password.isEmpty()) {
                edtPassword.setError("Mật khẩu không được để trống");
            } else {
                login(username, password);
            }
        } else if (view == tvForgotPass) {

            AlertDialog.Builder mBuilder = new AlertDialog.Builder(LoginActivity.this);
            View mView = View.inflate(LoginActivity.this, R.layout.forget_password_item, null);
            mBuilder.setView(mView);
            mBuilder.setCancelable(true);
            AlertDialog dialog = mBuilder.create();
            dialog.show();

            EditText edtPhoneSignUp;
            Button btnSend;
            Button btnCancelForgetPass;

            btnSend = mView.findViewById(R.id.btnSend);
            edtPhoneSignUp = mView.findViewById(R.id.edtPhoneSignUp);
            btnCancelForgetPass = mView.findViewById(R.id.btnCancelForgetPass);

            btnSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String phone = edtPhoneSignUp.getText().toString().trim();

                    if (phone.isEmpty()) {
                        edtPhoneSignUp.setError("Không được để trống");
                        edtPhoneSignUp.requestFocus();

                    } else {
                       checkPhoneNo(phone);
                    }

                }
            });

            btnCancelForgetPass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });


        }
    }


    private void checkPhoneNo(String phone) {
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

        userService.checkPhoNo(phone).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response.body().equals("Have PhoneNo")) {
                    Intent intent = new Intent(LoginActivity.this, VerifyForgotPassActivity.class);
                    intent.putExtra("phoneNo", phone);
                    startActivity(intent);
                } else if (response.body().equals("No PhoneNo")) {
                    showMessegeWarning("Số điện thoại chưa được đăng kí");
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Sever hiện không hoạt động. Vui lòng đợi!", Toast.LENGTH_SHORT).show();
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
                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("My Data", MODE_PRIVATE);
                    Editor editor = sharedPreferences.edit();
                    editor.putString("id", userList.get(0).getId());
                    editor.putString("username", userList.get(0).getUsername());
                    editor.putString("password", userList.get(0).getPassword());
                    editor.apply();
                    startNewActivity(HomeActivity.class);
                } else if (userList.size() == 0) {
                    showMessegeWarning("Đăng nhập thất bại. Vui lòng kiểm tra lại tài khoản hoặc mật khẩu");
                }

            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Sever hiện không hoạt động. Vui lòng đợi!", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private boolean isNetworkOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getNetworkInfo(0);
        return netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED;
    }


}
