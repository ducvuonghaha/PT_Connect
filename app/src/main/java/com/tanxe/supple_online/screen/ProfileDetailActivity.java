package com.tanxe.supple_online.screen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tanxe.supple_online.R;

public class ProfileDetailActivity extends AppCompatActivity {
    private TextView tvFullnane_User;
    private TextView tvUsernameUser;
    private TextView tvPasswordUser;
    private TextView tvPhoneUser;
    private TextView tvEmailUser;
    private Button btnUpdateUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_detail);
        init();
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("dataUser");
        tvUsernameUser.setText(bundle.getString("username"));
        tvPasswordUser.setText(bundle.getString("password"));
        tvEmailUser.setText(bundle.getString("email"));
        tvFullnane_User.setText( bundle.getString("fullname"));
        tvPhoneUser.setText(bundle.getString("phone"));

        btnUpdateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sex =bundle.getString("sex" );
                String fullname=tvFullnane_User.getText().toString();
                String email=tvEmailUser.getText().toString();
                Intent intent1=new Intent(ProfileDetailActivity.this,UpdateProfileUserActivity.class);
                intent1.putExtra("sex",sex);
                intent1.putExtra("fullname",fullname);
                intent1.putExtra("email",email);
                startActivity(intent1);
            }
        });

    }
    private void init() {
        tvFullnane_User = (TextView) findViewById(R.id.tvFullnane_User);
        tvUsernameUser = (TextView) findViewById(R.id.tvUsername_User);
        tvPasswordUser = (TextView) findViewById(R.id.tvPassword_User);
        tvPhoneUser = (TextView) findViewById(R.id.tvPhone_User);
        tvEmailUser = (TextView) findViewById(R.id.tvEmail_User);
        btnUpdateUser =  findViewById(R.id.btnUpdateUser);

    }
}