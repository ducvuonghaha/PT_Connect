package com.tanxe.supple_online.screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.tanxe.supple_online.R;
import com.tanxe.supple_online.adapter.MyOrderPagerAdapter;
import com.tanxe.supple_online.adapter.ProfilePagerAdapter;

public class MyOrderActivity extends AppCompatActivity {

    public static TabLayout tabMyOrder;
    private ViewPager myOrderPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        initView();
        MyOrderPagerAdapter myOrderPagerAdapter =new MyOrderPagerAdapter(getSupportFragmentManager());
        myOrderPager.setAdapter(myOrderPagerAdapter);
        myOrderPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabMyOrder));
        tabMyOrder.setupWithViewPager(myOrderPager);
        tabMyOrder.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(myOrderPager));
    }

    private void initView(){
        tabMyOrder=findViewById(R.id.tabMyOrder);
        myOrderPager=findViewById(R.id.myOrderPager);
    }
}