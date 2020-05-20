package com.sitechdev.vehicle.pad.module.online_audio;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.view.TabLayout;

import java.util.ArrayList;

/**
 * <pre>
 *      author : zyf
 *      time   : 2020/5/20
 * </pre>
 */
public class KaolaAudioSubPageFrag extends Fragment {
    private View root;
    private Context mContext;

    private TabLayout tabLayout;

    private ViewPager pager;

    private ArrayList<Fragment> fragmentlist = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (root == null) {
            root = inflater.inflate(R.layout.audio_kaola_sub_frame, null);
            initFrags();
            initContentView();
        }
        return root;
    }

    private void initFrags() {
        fragmentlist.add(new KaolaRecommendFrag());
        fragmentlist.add(new KaolaRecommendFrag());
        fragmentlist.add(new KaolaRecommendFrag());
        fragmentlist.add(new KaolaRecommendFrag());
        fragmentlist.add(new KaolaRecommendFrag());
    }

    private void initContentView() {
        tabLayout = root.findViewById(R.id.tv_sub_title);
        pager = root.findViewById(R.id.vp);
        tabLayout.setupWithViewPager(pager);
        KaolaFragmentAdapter adapter = new KaolaFragmentAdapter(getChildFragmentManager(), fragmentlist, new String[]{"推荐", "新特速报", "少儿读物", "车嗨娱乐", "生活一点通"});
        pager.setAdapter(adapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
}
