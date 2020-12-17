package com.tanxe.supple_online.screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.tanxe.supple_online.R;
import com.tanxe.supple_online.service.UserService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.tanxe.supple_online.screen.LoginActivity.BASE_URL;

public class VerifyPhoneNoActivity extends AppCompatActivity {
    private EditText edtVerifyPhone;
    private Button btnVerify;
    String verificationCodeBySystem = "";
    private String phoneNo;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone_no);
        init();
        mAuth = FirebaseAuth.getInstance();
        phoneNo = getIntent().getStringExtra("phoneNo");
        sendVerificationToUser(phoneNo);
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = edtVerifyPhone.getText().toString().trim();
                if (code.isEmpty() || code.length()<6){
                    edtVerifyPhone.setError("Wrong OTP...");
                    edtVerifyPhone.requestFocus();
                }else {
                    verifyCode(code);
                }
            }
        });
    }

    private void sendVerificationToUser(String phoneNo) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+84"+ phoneNo)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);


    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationCodeBySystem = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Log.e("Faillllll",e.getMessage());
        }
    };

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCodeBySystem, code);
        signUpUserByCredential(credential);
    }

    private void signUpUserByCredential(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(VerifyPhoneNoActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String username = getIntent().getStringExtra("username");
                    String password = getIntent().getStringExtra("password");
                    String email = getIntent().getStringExtra("email");
                    String fullname = getIntent().getStringExtra("fullname");
                    String types = getIntent().getStringExtra("types");
                    String status = getIntent().getStringExtra("status");
                    String sex = getIntent().getStringExtra("sex");
                    if (types.equals("Coach")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(VerifyPhoneNoActivity.this);
                        builder.setTitle("Thông báo");
                        View view1 = LayoutInflater.from(VerifyPhoneNoActivity.this).inflate(R.layout.it_notification, null, false);
                        builder.setView(view1);
                        Dialog dialog = builder.show();
                        Button btnUpdateCoach;
                        Button btnCancelUpdate;
                        btnUpdateCoach = (Button) view1.findViewById(R.id.btnConfirmCoach);
                        btnCancelUpdate = (Button) view1.findViewById(R.id.btnCancelUpdate);
                        btnUpdateCoach.setText("Cập nhật ngay");
                        btnCancelUpdate.setText("Bỏ qua");
                        btnUpdateCoach.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                signUpUser(username, password, phoneNo, email, fullname, types, status,sex);
                                Intent intent=new Intent(VerifyPhoneNoActivity.this,UpdateCoachActivity.class);
                                intent.putExtra("fullname", fullname);
                                startActivity(intent);
                            }
                        });

                        btnCancelUpdate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                signUpUser(username, password, phoneNo, email, fullname, types, status,sex);
                                dialog.dismiss();

                            }
                        });
                    }else {
                        signUpUser(username, password, phoneNo, email, fullname, types, status,sex);
                    }

                } else {
                    Log.e("Fail Verify", task.getException().getMessage() + "");
                }
            }
        });
    }

    private void init() {
        edtVerifyPhone = (EditText) findViewById(R.id.edtVerifyPhone);
        btnVerify = (Button) findViewById(R.id.btnVerify);
    }

    private void signUpUser(String username, String password, String phone , String email, String fullname, String types,String status,String sex) {
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
        userService.signUp(username, password, fullname, phone, email,types,status,sex).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body().equals("Create Account Success")) {
                    Toast.makeText(VerifyPhoneNoActivity.this, "Tạo tài khoản thành công. Mời bạn đăng nhập", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(VerifyPhoneNoActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else if (response.body().equals("Create Account Failure")) {
                    Toast.makeText(VerifyPhoneNoActivity.this, "Tạo tài khoản thất bại", Toast.LENGTH_SHORT).show();
                } else if (response.body().equals("Account already exists")) {
                    Toast.makeText(VerifyPhoneNoActivity.this, "Tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
                } else if (response.body().equals("Email hoặc Số điện thoại đã được sử dụng")) {
                    Toast.makeText(VerifyPhoneNoActivity.this, response.body() + "", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("QQQ", t.getLocalizedMessage() + "");
            }
        });

    }
}