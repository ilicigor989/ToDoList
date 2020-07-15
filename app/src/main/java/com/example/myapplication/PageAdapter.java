package com.example.myapplication;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PageAdapter extends FragmentPagerAdapter {

    private int tabsNum;


    public PageAdapter(FragmentManager fm, int tabsNum) {

        super(fm);
        this.tabsNum=tabsNum;
    }

    @Override
    public Fragment getItem(int i) {

        switch (i) {

            case 0:
                return new QuickLoginFragment();
            case 1:
                return new GmailLoginFragment();

            case 2:
                return new FacebookLoginFragment();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return tabsNum;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
