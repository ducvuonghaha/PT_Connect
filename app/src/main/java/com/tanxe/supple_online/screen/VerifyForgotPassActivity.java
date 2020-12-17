package com.tanxe.supple_online.screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tanxe.supple_online.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyForgotPassActivity extends AppCompatActivity {
    EditText edtVerifyForgotPass;
    private Button btnVerify;
    String verificationCodeBySystem = "";
    private String phoneNo;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_forgot_pass);
        init();
        mAuth = FirebaseAuth.getInstance();
        phoneNo = getIntent().getStringExtra("phoneNo");
        sendVerificationToUser(phoneNo);
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = edtVerifyForgotPass.getText().toString().trim();
                if (code.isEmpty() || code.length() < 6) {
                    edtVerifyForgotPass.setError("Wrong OTP...");
                    edtVerifyForgotPass.requestFocus();
                } else {
                    verifyCode(code);

                }
            }
        });
    }

    private void sendVerificationToUser(String phoneNo) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+84" + phoneNo)       // Phone number to verify
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
            Log.e("Faillllll", e.getMessage());
        }
    };

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCodeBySystem, code);
        signUpUserByCredential(credential);
    }

    private void init() {
        edtVerifyForgotPass = (EditText) findViewById(R.id.edtVerifyForgotPass);
        btnVerify = (Button) findViewById(R.id.btnVerify);

    }
    private void signUpUserByCredential(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(VerifyForgotPassActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent=new Intent(VerifyForgotPassActivity.this,UpdatePasswordActivity.class);
                    intent.putExtra("PhoneNoRequest",phoneNo);
                    startActivity(intent);
                } else {
                    edtVerifyForgotPass.setError("Mời bạn kiểm tra lại mã");
                }
            }
        });
    }
}