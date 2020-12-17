package com.tanxe.supple_online.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.tanxe.supple_online.profile_fragment.ProfileCoachFragment;
import com.tanxe.supple_online.profile_fragment.ProfileUserFragment;

public class ProfilePagerAdapter extends FragmentPagerAdapter {
    public ProfilePagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment=null;
        switch (position){
            case 0:
                fragment=new ProfileUserFragment();
                break;
            case 1:
                fragment=new ProfileCoachFragment();
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
                title = "Profile User";
                break;
            case 1:
                title = "Profile Coach";
                break;
        }
        return title;
    }
}
