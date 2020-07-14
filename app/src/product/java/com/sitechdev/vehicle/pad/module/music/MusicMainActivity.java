package com.sitechdev.vehicle.pad.module.music;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.sitechdev.vehicle.lib.event.EventBusUtils;
import com.sitechdev.vehicle.lib.util.ThreadUtils;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.BaseActivity;
import com.sitechdev.vehicle.pad.event.MusicControlEvent;
import com.sitechdev.vehicle.pad.manager.VoiceSourceManager;
import com.sitechdev.vehicle.pad.module.music.adapter.MusicPagerAdapter;
import com.sitechdev.vehicle.pad.module.music.fragment.BtMusicFragment;
import com.sitechdev.vehicle.pad.module.music.fragment.LocalMusicFragment;
import com.sitechdev.vehicle.pad.router.RouterConstants;
import com.sitechdev.vehicle.pad.view.TabLayout;

import java.util.ArrayList;
import java.util.List;

@Route(path = RouterConstants.FRAGMENT_LOCAL_MUSIC)
public class MusicMainActivity extends BaseActivity {

    private TabLayout vTab;
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
        ThreadUtils.runOnUIThreadDelay(new Runnable() {
            @Override
            public void run() {
                EventBusUtils.postEvent(new MusicControlEvent(MusicControlEvent.EVENT_CONTROL_MUSIC_PLAY_IF_ON_TOP,index));
            }
        }, 500);//页面加载完成发送事件
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_music_main;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        vTab = findViewById(R.id.tv_sub_title_tab);
        vPager = findViewById(R.id.music_main_viewpager);
        List<Fragment> mFragments = new ArrayList<>();
        mFragments.add(LocalMusicFragment.newInstance());
        mFragments.add(BtMusicFragment.newInstance());
        MusicPagerAdapter mAdapter = new MusicPagerAdapter(getSupportFragmentManager(), mFragments);
        vPager.setAdapter(mAdapter);
        vTab.setupWithViewPager(vPager);
        vPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                EventBusUtils.postEvent(new MusicControlEvent(MusicControlEvent.EVENT_CONTROL_MUSIC_PLAY_IF_ON_TOP,i));
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        for (int i = 0; i < vTab.getTabCount(); i++) {
            TabLayout.Tab tab = vTab.getTabAt(i);
            tab.setText(mTitles[i]);
        }
        analyzeIntent(getIntent());
    }

    @Override
    protected void initListener() {
        super.initListener();
        findViewById(R.id.iv_phone_sub_back).setOnClickListener(this);
    }

    @Override
    protected void initData() {
        vPager.setCurrentItem(VoiceSourceManager.getInstance().getMusicSource() == VoiceSourceManager.BT_MUSIC ? 1 : 0);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_phone_sub_back:
                onBackPressed();
                break;
        }
    }

}
