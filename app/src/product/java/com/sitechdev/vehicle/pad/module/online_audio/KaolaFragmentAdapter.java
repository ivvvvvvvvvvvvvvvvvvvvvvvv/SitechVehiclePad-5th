package com.sitechdev.vehicle.pad.module.online_audio;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import io.reactivex.annotations.Nullable;

public class KaolaFragmentAdapter extends PagerAdapter {
    // 页面管理器
    private FragmentManager fragmentManager;
    private String[] titles;
    private String[] tags = new String[]{KaolaAudioSubPageFrag.class.getSimpleName(), KaolaAudioSubPageFrag.class.getSimpleName(), KaolaAudioSubPageFrag.class.getSimpleName(), KaolaAudioSubPageFrag.class.getSimpleName()};
    int defaultIndex; int subIndex;boolean playIfSuspend;
    // 构造方法
    public KaolaFragmentAdapter(FragmentManager fragmentManager,  String[] titles,Object... args) {
        this.fragmentManager = fragmentManager;
        this.titles = titles;
        if (args.length >= 3) {
            defaultIndex = (int) args[0];
            subIndex = (int) args[1];
            playIfSuspend = (boolean) args[2];
        }
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
        Fragment fragment = fragmentManager.findFragmentByTag(titles[position] + position);
        if (fragment != null) {
            container.removeView(fragment.getView());
        }
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Fragment fragment = fragmentManager.findFragmentByTag(tags[position] + position);
        if (fragment == null) {
            switch (position) {
                case 0:
                    fragment = new KaolaAudioSubPageFrag(defaultIndex, subIndex, playIfSuspend);
                    break;
                case 1:
                    fragment = new KaolaAudioCategoryPageFrag();
                    break;
                case 2:
                    fragment = new KaolaAudioBroadcastPageFrag();
                    break;
                case 3:
                    fragment = new KaolaAudioSearchPageFrag();
                    break;
            }
        }
        if (!fragment.isAdded()) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.add(fragment, fragment.getClass().getSimpleName() + position);
            ft.commit();
            fragmentManager.executePendingTransactions();
        }

        if (fragment.getView() != null) {
            if (fragment.getView().getParent() == null) {
                container.addView(fragment.getView());
            }
        }

        return fragment.getView();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}