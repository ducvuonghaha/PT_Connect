package com.tanxe.supple_online.screen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.tanxe.supple_online.R;
import com.tanxe.supple_online.helper.BaseActivity;
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

public class SignUpActivity extends BaseActivity {

    private EditText edtSUUsername;
    private EditText edtSUFullName;
    private EditText edtSUEmail;
    private EditText edtSUPhone;
    private EditText edtSUPassword;
    private EditText edtSURepassword;
    private Button btnSignUp;
    private CheckBox ckHochvien;
    private CheckBox ckHLV;
    private RadioButton rdbBoy;
    private RadioButton rdbGirl;
    private RadioButton rdbElse;
    String sex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initView();
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rdbBoy.isChecked()==true) {
                    sex = "Nam";
                } else if (rdbBoy.isChecked()==true) {
                    sex = "Nữ";
                } else if (rdbElse.isChecked()==true) {
                    sex = "None";
                }
                String username = edtSUUsername.getText().toString();
                String fullname = edtSUFullName.getText().toString();
                String phone = edtSUPhone.getText().toString();
                String email = edtSUEmail.getText().toString();
                String password = edtSUPassword.getText().toString();
                String repassword = edtSURepassword.getText().toString();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                String passPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
                if (username.isEmpty()) {
                    edtSUUsername.setError("Tên đăng nhập không được để trống");
                } else if (fullname.isEmpty()) {
                    edtSUFullName.setError("Tên đày đủ không được để trống");
                } else if (phone.isEmpty()) {
                    edtSUPhone.setError("Số điện thoại không được để trống");
                } else if (email.isEmpty()) {
                    edtSUEmail.setError("Email không được để trống");
                } else if (password.isEmpty()) {
                    edtSUPassword.setError("Mật khẩu không được để trống");
                } else if (repassword.isEmpty()) {
                    edtSURepassword.setError("Mật khẩu không được để trống");
                } else if (username.length() < 6) {
                    edtSUUsername.setError("Tên đăng nhập ít nhất có 6 kí tự");
                    edtSUUsername.requestFocus();
                } else if (!(phone.length() == 10)) {
                    edtSUPhone.setError("Số điện thoại phải đủ 10 ký tự");
                } else if (!email.matches(emailPattern)) {
                    edtSUEmail.requestFocus();
                    edtSUEmail.setError("Email không đúng định dạng");
                } else if (rdbElse.isChecked()==false && !rdbBoy.isChecked()==false && !rdbGirl.isChecked()==false) {
                    showMessegeError("Mời bạn chon giới tính");
                }else if (!ckHochvien.isChecked() && !ckHLV.isChecked()) {
                    showMessegeError("Mòi bạn chọn chế độ");
                } else if (!password.matches(passPattern)) {
                    edtSUPassword.requestFocus();
                    edtSUPassword.setError("Mật khẩu ít nhất phải 8 ký tự gồm tối thiểu 1 chữ hoa, 1 chữ thường, 1 số và 1 ký tự đặc biệt");
                } else if (!password.equalsIgnoreCase(repassword)) {
                    edtSUPassword.setError("Mật khẩu không trùng khớp");
                    edtSUPassword.requestFocus();
                    edtSUPassword.setText(null);
                    edtSURepassword.setText(null);
                } else if (ckHochvien.isChecked()) {
                    signUpUser(username, password, phone, email, fullname, "Student", "None",sex);
                } else if (ckHLV.isChecked()) {
                    signUpUser(username, password, phone, email, fullname, "Coach", "None",sex);
                    //do something
                } else if (ckHLV.isChecked() && ckHochvien.isChecked()) {
                    signUpUser(username, password, phone, email, fullname, "Coach", "None",sex);
                }

            }
        });
    }

    private void initView() {
        ckHochvien = (CheckBox) findViewById(R.id.ckHochvien);
        ckHLV = (CheckBox) findViewById(R.id.ckHLV);
        edtSUUsername = (EditText) findViewById(R.id.edtSU_Username);
        edtSUFullName = (EditText) findViewById(R.id.edtSU_FullName);
        edtSUEmail = (EditText) findViewById(R.id.edtSU_Email);
        edtSUPhone = (EditText) findViewById(R.id.edtSU_Phone);
        edtSUPassword = (EditText) findViewById(R.id.edtSU_Password);
        edtSURepassword = (EditText) findViewById(R.id.edtSU_Repassword);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);

        rdbBoy = (RadioButton) findViewById(R.id.rdbBoy);
        rdbGirl = (RadioButton) findViewById(R.id.rdbGirl);
        rdbElse = (RadioButton) findViewById(R.id.rdbElse);
    }

    private void clearAllEdt() {
        edtSUUsername.setText("");
        edtSURepassword.setText("");
        edtSUPassword.setText("");
        edtSUFullName.setText("");
        edtSUPhone.setText("");
        edtSUEmail.setText("");
    }

    private void signUpUser(String username, String password, String phone, String email, String fullname, String types, String status,String sex) {
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
        userService.verifyPhoneNo(username, phone, email).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body().equals("Confirm")) {
                    Intent intent = new Intent(SignUpActivity.this, VerifyPhoneNoActivity.class);
                    intent.putExtra("phoneNo", phone);
                    intent.putExtra("username", username);
                    intent.putExtra("password", password);
                    intent.putExtra("email", email);
                    intent.putExtra("fullname", fullname);
                    intent.putExtra("types", types);
                    intent.putExtra("status", status);
                    intent.putExtra("sex", sex);
                    startActivity(intent);
                } else if (response.body().equals("Account already exists")) {
                    Toast.makeText(SignUpActivity.this, "Tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
                } else if (response.body().equals("Số điện thoại đã được sử dụng")) {
                    Toast.makeText(SignUpActivity.this, response.body() + "", Toast.LENGTH_SHORT).show();
                }else if (response.body().equals("Email đã được sử dụng")) {
                    Toast.makeText(SignUpActivity.this, response.body() + "", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("QQQ", t.getLocalizedMessage() + "");
            }
        });

    }
}