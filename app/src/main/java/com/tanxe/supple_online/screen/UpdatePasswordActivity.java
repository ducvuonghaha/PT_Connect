package com.tanxe.supple_online.screen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.tanxe.supple_online.R;
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

public class UpdatePasswordActivity extends AppCompatActivity {
    private EditText edtNewPassword;
    private EditText edtReNewPassword;
    private Button btnAccept;
    private List<User> userList;
    String passPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        init();
        userList=new ArrayList<>();
        String PhoneNoRequest=getIntent().getStringExtra("PhoneNoRequest");
        getInfor(PhoneNoRequest);
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass=edtNewPassword.getText().toString().trim();
                String repass=edtReNewPassword.getText().toString().trim();
                if (pass.isEmpty()){
                    edtNewPassword.setError("Không được để trống");
                    edtNewPassword.requestFocus();
                }else if(repass.isEmpty()){
                    edtReNewPassword.setError("Không được để trống");
                    edtReNewPassword.requestFocus();
                }else if(!(pass.equals(repass))){
                    edtReNewPassword.setError("Mật khẩu không trùng khớp");
                    edtReNewPassword.requestFocus();
                }else if (!pass.matches(passPattern)) {
                    edtNewPassword.setError("Mật khẩu ít nhất phải 8 ký tự gồm tối thiểu 1 chữ hoa, 1 chữ thường, 1 số và 1 ký tự đặc biệt");
                    edtNewPassword.requestFocus();
                }else {
                    String id = userList.get(0).getId();
                    String username=userList.get(0).getUsername();
                    changePass(id,username,pass);
                }

            }
        });
    }
    private void init(){
        edtNewPassword = (EditText) findViewById(R.id.edtNewPassword);
        edtReNewPassword = (EditText) findViewById(R.id.edtReNewPassword);
        btnAccept = (Button) findViewById(R.id.btnAccept);

    }
    private void changePass(String id, String username, String password) {
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
        userService.changePass(id, username, password).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body().equals("Change Success")) {
                    Toast.makeText(UpdatePasswordActivity.this ,"Thay đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(UpdatePasswordActivity.this,LoginActivity.class);
                    startActivity(intent);
                } else if (response.body().equals("Change Fail")) {
                    Toast.makeText(UpdatePasswordActivity.this, "Thay đổi mật khẩu thất bại. Xin kiểm tra lại", Toast.LENGTH_SHORT).show();
                } else if (response.body().equals("Duplicate")) {
                    Toast.makeText(UpdatePasswordActivity.this, "Mật khẩu đã trung với mật khẩu cũ. Mời kiểm tra lại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Fail Change Password", t.getLocalizedMessage());
            }
        });
    }
    private void getInfor(String phone) {
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
        userService.getInforFromPhone(phone).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response <List<User>> response) {
                userList=response.body();

            }

            @Override
            public void onFailure(Call <List<User>> call, Throwable t) {
                Log.e("QQQ", t.getLocalizedMessage() + "");
            }
        });

    }
}