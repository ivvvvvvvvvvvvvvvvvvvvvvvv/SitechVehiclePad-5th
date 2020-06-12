package com.sitechdev.vehicle.pad.module.music;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.BaseActivity;
import com.sitechdev.vehicle.pad.module.music.adapter.MusicPagerAdapter;
import com.sitechdev.vehicle.pad.module.music.fragment.LocalMusicFragment;
import com.sitechdev.vehicle.pad.module.music.fragment.OtherMusicFragment;
import com.sitechdev.vehicle.pad.router.RouterConstants;

import java.util.ArrayList;
import java.util.List;

@Route(path = RouterConstants.FRAGMENT_LOCAL_MUSIC)
public class MusicMainActivity extends BaseActivity {

    private SlidingTabLayout vTab;
    private ViewPager vPager;
    private String[] mTitles = {"USB", "蓝牙"};
    private int index = 0;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        analyzeIntent(intent);
    }

    private void analyzeIntent(Intent intent) {
        if (null != intent) {
            index = intent.getIntExtra("index", 0);
        }
        if (null != vPager) {
            vPager.setCurrentItem(index);
        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_music_main;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        vTab = findViewById(R.id.music_main_tablayout);
        vPager = findViewById(R.id.music_main_viewpager);
        List<Fragment> mFragments = new ArrayList<>();
        mFragments.add(LocalMusicFragment.newInstance());
        mFragments.add(OtherMusicFragment.newInstance());
        MusicPagerAdapter mAdapter = new MusicPagerAdapter(getSupportFragmentManager(), mFragments);
        vPager.setAdapter(mAdapter);
        vTab.setViewPager(vPager, mTitles);
        vTab.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                int count = vTab.getTabCount();
                for (int i = 0; i < count; i++) {
                    vTab.getTitleView(i).setTextSize(24);
                }
                vTab.getTitleView(position).setTextSize(27);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
        vPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                int count = vTab.getTabCount();
                for (int i = 0; i < count; i++) {
                    vTab.getTitleView(i).setTextSize(24);
                }
                vTab.getTitleView(position).setTextSize(27);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        analyzeIntent(getIntent());
    }

    @Override
    protected void initListener() {
        super.initListener();
        findViewById(R.id.music_main_back).setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.music_main_back:
                onBackPressed();
                break;
        }
    }

}
