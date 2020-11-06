package com.example.trendz.ui.gallery;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.example.trendz.R;
import com.example.trendz.WithdrawalPagerAdapter;
import com.example.trendz.WithdrawalfundFragment;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    TabLayout tabLayout;
    TabItem WithdrawalFund, WithdrawalList;
    ViewPager viewPager;
    WithdrawalPagerAdapter withdrawalPagerAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        tabLayout = root.findViewById(R.id.withdrawalTabbar);
        WithdrawalFund = root.findViewById(R.id.withdrawalFund);
        WithdrawalList = root.findViewById(R.id.withdrawalList);
        viewPager = root.findViewById(R.id.withdrawalviewPager);

        withdrawalPagerAdapter = new WithdrawalPagerAdapter(getChildFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(withdrawalPagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                if (tab.getPosition() == 0 || tab.getPosition() == 1)
                    withdrawalPagerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return root;
    }

}