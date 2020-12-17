package com.tanxe.supple_online.screen;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.tanxe.supple_online.R;
import com.tanxe.supple_online.adapter.ProfilePagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class ProfileActivity extends AppCompatActivity {
    public static TabLayout tabProfile;
    private ViewPager profilePager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initView();

        ProfilePagerAdapter profilePagerAdapter=new ProfilePagerAdapter(getSupportFragmentManager());
        profilePager.setAdapter(profilePagerAdapter);
        profilePager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabProfile));
        tabProfile.setupWithViewPager(profilePager);
        tabProfile.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(profilePager));

    }
    private void initView(){
        tabProfile=findViewById(R.id.tabProfile);
        profilePager=findViewById(R.id.ProfilePager);
    }


}