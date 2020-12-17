package com.tanxe.supple_online.screen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.tanxe.supple_online.R;
import com.tanxe.supple_online.service.UserService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.tanxe.supple_online.screen.LoginActivity.BASE_URL;

public class UpdateProfileUserActivity extends AppCompatActivity {
    private EditText edtEmailProfile;
    private TextView textView8;
    private EditText edtFullnameProfile;
    private RadioGroup radioGroup;
    private Button btnUpdateUserProfile;
    private RadioButton rdbBoy;
    private RadioButton rdbGirl;
    private RadioButton rdbElse;
    String sex;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile_user);
        init();
        Intent intent = getIntent();
        String fullname = intent.getStringExtra("fullname");
        String sex1 = intent.getStringExtra("sex");
        String email = intent.getStringExtra("email");
        edtEmailProfile.setText(email);
        edtFullnameProfile.setText(fullname);
        if (sex1.equals("Nam")) {
            rdbBoy.setChecked(true);
        } else if (sex1.equals("Nữ")) {
            rdbGirl.setChecked(true);
        } else {
            rdbElse.setChecked(true);
        }
        btnUpdateUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullname = edtFullnameProfile.getText().toString().trim();
                String email = edtEmailProfile.getText().toString().trim();
                if (rdbBoy.isChecked() == true) {
                    sex = "Nam";
                } else if (rdbBoy.isChecked() == true) {
                    sex = "Nữ";
                } else if (rdbElse.isChecked() == true) {
                    sex = "None";
                }
                if (fullname.isEmpty()) {
                    edtFullnameProfile.setError("Không được để trống");
                    edtFullnameProfile.requestFocus();
                } else if (email.isEmpty()) {
                    edtEmailProfile.setError("Không được để trống");
                    edtEmailProfile.requestFocus();
                } else if (rdbElse.isChecked() == false && !rdbBoy.isChecked() == false && !rdbGirl.isChecked() == false) {
                    Toast.makeText(UpdateProfileUserActivity.this, "Mời bạn chon giới tính", Toast.LENGTH_SHORT).show();
                } else if (!email.matches(emailPattern)) {
                    edtEmailProfile.setError("Email không đúng định dạng");
                    edtEmailProfile.requestFocus();
                } else {
                    SharedPreferences sharedPreferences = getSharedPreferences("My Data", MODE_PRIVATE);
                    String id = sharedPreferences.getString("id", "");
                    updateUser(id, fullname, email, sex);
                }
            }
        });
    }

    private void init() {
        edtEmailProfile = (EditText) findViewById(R.id.edtEmailProfile);
        textView8 = (TextView) findViewById(R.id.textView8);
        edtFullnameProfile = (EditText) findViewById(R.id.edtFullnameProfile);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        btnUpdateUserProfile = (Button) findViewById(R.id.btnUpdateUserProfile);
        rdbBoy = findViewById(R.id.rdbBoy);
        rdbGirl = findViewById(R.id.rdbGirl);
        rdbElse = findViewById(R.id.rdbElse);

    }

    private void updateUser(String id, String fullname, String email, String sex) {
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
        userService.updateProfile(id, fullname, email, sex).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body().equals("Update User Success")) {
                    Toast.makeText(UpdateProfileUserActivity.this, "Bạn cập nhật thành công", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UpdateProfileUserActivity.this, ProfileActivity.class);
                    startActivity(intent);
                } else if (response.body().equals("Update User Failure")) {
                    Toast.makeText(UpdateProfileUserActivity.this, "Bạn cập nhật thành công", Toast.LENGTH_SHORT).show();
                } else if (response.body().equals("User already exists")) {
                    Toast.makeText(UpdateProfileUserActivity.this, "Thông tin của bạn chưa thay đổi", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("QQQ", t.getLocalizedMessage() + "");
            }
        });
    }

}