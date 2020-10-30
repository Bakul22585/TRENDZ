package com.example.trendz;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class WithdrawalPagerAdapter extends FragmentPagerAdapter {

    private int numOfTabs;
    public WithdrawalPagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Log.e("Get Position", String.valueOf(position));
        switch (position) {
            case 0:
                return new WithdrawalfundFragment();
            case 1:
                return new WithdrawalListFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        position = position+1;
        return super.getPageTitle(position);
    }
}
