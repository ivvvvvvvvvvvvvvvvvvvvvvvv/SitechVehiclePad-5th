package com.sitechdev.vehicle.pad.module.online_audio;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import io.reactivex.annotations.Nullable;

// 带状态车源列表页面适配器
public class KaolaFragmentAdapter extends PagerAdapter {
    // 页面集合
    private List<Fragment> fragments;
    // 页面管理器
    private FragmentManager fragmentManager;
    private String[] titles;

    // 构造方法
    public KaolaFragmentAdapter(FragmentManager fragmentManager, List<Fragment> fragments, String[] titles) {
        this.fragments = fragments;
        this.fragmentManager = fragmentManager;
        this.titles = titles;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
        container.removeView(fragments.get(position).getView());
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Fragment fragment = fragments.get(position);
        if (!fragment.isAdded()) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.add(fragment, fragment.getClass().getSimpleName());
            ft.commit();
            fragmentManager.executePendingTransactions();
        }

        if (fragment.getView() != null) {
            if (fragment.getView().getParent() == null) {
                container.addView(fragment.getView());
            }
        }

        assert fragment.getView() != null;
        return fragment.getView();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}