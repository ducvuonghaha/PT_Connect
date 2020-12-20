package com.tanxe.supple_online.screen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.tanxe.supple_online.R;
import com.squareup.picasso.Picasso;

public class DetailProfileCoachActivity extends AppCompatActivity {
    private ImageView imgAvatar;
    private TextView tvFullnane_Coach;
    private TextView tvAgeProfileCoach;
    private TextView tvBackground_Coach;
    private TextView tvAddressCoach;
    private TextView tvWorkplaceCoach;
    private TextView tvSpecialize;
    private Button btnUpdateProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_profile_coach);
        init();
        Intent intent=getIntent();
        String fullname=intent.getStringExtra("fullname");
        String address=intent.getStringExtra("address");
        String background=intent.getStringExtra("background");
        String age=intent.getStringExtra("age");
        String imageProfile=intent.getStringExtra("imageProfile");
        String place=intent.getStringExtra("place");
        String specialize=intent.getStringExtra("specialize");

        Picasso.get().load(imageProfile).into(imgAvatar);
        tvAddressCoach.setText(address);
        tvAgeProfileCoach.setText(age +" tuổi");
        tvBackground_Coach.setText(background);
        tvFullnane_Coach.setText(fullname);
        tvWorkplaceCoach.setText(place);
        tvSpecialize.setText(specialize);
        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DetailProfileCoachActivity.this, UpdateCoachActivity.class);
                intent.putExtra("fullname", fullname);
                startActivity(intent);
            }
        });
    }
    private void init(){

        imgAvatar = (ImageView) findViewById(R.id.imgAvatar);
        tvFullnane_Coach = (TextView) findViewById(R.id.tvFullnane_Coach);
        tvAgeProfileCoach = (TextView) findViewById(R.id.tvAgeProfileCoach);
        tvBackground_Coach = (TextView) findViewById(R.id.tvBackground_Coach);
        tvAddressCoach = (TextView) findViewById(R.id.tvAddress_Coach);
        tvWorkplaceCoach = (TextView) findViewById(R.id.tvWorkplace_Coach);
        tvSpecialize = (TextView) findViewById(R.id.tvSpecialize);
        btnUpdateProfile = (Button) findViewById(R.id.btnUpdateProfile);

    }
}