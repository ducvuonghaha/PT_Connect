package com.tanxe.supple_online.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.tanxe.supple_online.myorder_fragment.DeliveredOrderFragment;
import com.tanxe.supple_online.myorder_fragment.DeliveringOrderFragment;

public class MyOrderPagerAdapter extends FragmentPagerAdapter {

    public MyOrderPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new DeliveringOrderFragment();
                break;
            case 1:
                fragment = new DeliveredOrderFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position) {
            case 0:
                title = "Đang giao";
                break;
            case 1:
                title = "Đã giao";
                break;
        }
        return title;
    }
}
