package com.example.weatherforecast.adpater;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class MyPagerAdapter extends FragmentPagerAdapter {
//    public MyPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
//        super(fragmentActivity);
//    }
//
//    public MyPagerAdapter(@NonNull Fragment fragment) {
//        super(fragment);
//    }
//
//    public MyPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
//        super(fragmentManager, lifecycle);
//    }

    //    @NonNull
//    @Override
//    public Fragment createFragment(int position) {
//        return l.get(position);
//    }
//
//    @Override
    public Context context;
    List<Fragment> l;

    //
    public MyPagerAdapter(FragmentManager fm, List l, Activity context) {
        super(fm);
        this.l = l;
        this.context = context;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        return l.get(position);
    }

    @Override
    public int getCount() {
        return l.size();
    }

}
