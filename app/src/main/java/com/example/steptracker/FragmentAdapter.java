package com.example.steptracker;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class FragmentAdapter extends FragmentPagerAdapter {


    public FragmentAdapter(@NonNull FragmentManager fm, Context context) {
        super(fm);
        this.mContext = context;
    }

    private static final int[] TAB_TITLES = new int[]{R.string.day, R.string.week, R.string.month};
    private Context mContext;

    @NonNull
    @Override
    public Fragment getItem(int position) {
        ShowFragment fragment = new ShowFragment();
        Bundle bundle = new Bundle();
        bundle.putString("", "Keep moving each day to meet your steps goal and sedentary time");
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }
}
