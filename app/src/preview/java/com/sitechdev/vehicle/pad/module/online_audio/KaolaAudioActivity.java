package com.sitechdev.vehicle.pad.module.online_audio;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.kaolafm.sdk.core.mediaplayer.PlayItem;
import com.sitechdev.vehicle.lib.event.BindEventBus;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.BaseActivity;
import com.sitechdev.vehicle.pad.event.TeddyEvent;
import com.sitechdev.vehicle.pad.kaola.KaolaPlayManager;
import com.sitechdev.vehicle.pad.manager.VoiceSourceManager;
import com.sitechdev.vehicle.pad.manager.VoiceSourceType;
import com.sitechdev.vehicle.pad.view.TabLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

@VoiceSourceType(VoiceSourceManager.SUPPORT_TYPE_KAOLA)
@BindEventBus
public class KaolaAudioActivity extends BaseActivity implements
        VoiceSourceManager.MusicChangeListener, VoiceSourceManager.onPlaySourceMusicChangeListener {
    private static final String TAG = "MusicKaolaForShowActivity";

    private Context mContext;
    //new
    private TabLayout tabLayout;

    private ViewPager pager;
    // 页面类型字符串数组
    private ArrayList<Fragment> fragmentlist = new ArrayList<>();
    // 初始化页面集合的方法
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        VoiceSourceManager.getInstance().addMusicChangeListener(this);
        KaolaPlayManager.SingletonHolder.INSTANCE.setPlayVoiceSourceManagerListener(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.audio_kaola_main_frame;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        initFrags();
        initTabLayout();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    @Override
    protected void initData() {
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {

            default:
                break;
        }
    }

    private void initFrags() {
        fragmentlist.add(new KaolaAudioSubPageFrag());
        fragmentlist.add(new KaolaAudioCategoryPageFrag());
    }

    private void initTabLayout() {
        tabLayout = findViewById(R.id.tv_sub_title);
        pager = findViewById(R.id.vp);
        findViewById(R.id.iv_sub_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tabLayout.setupWithViewPager(pager);
        KaolaFragmentAdapter adapter = new KaolaFragmentAdapter(getSupportFragmentManager(), fragmentlist, new String[]{"AI电台","专辑分类","在线广播"});
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

    @Override
    public void onMusicChange(String name) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void onMusicPause() {

    }

    @Override
    public void onMusicResume() {

    }

    @Override
    public void onPlayerFailed(PlayItem item) {

    }

    @Override
    public void onMusicPlaying(PlayItem item) {
    }

    @Override
    public void onMusicPlayEnd(PlayItem item) {
    }

    @Override
    public void onMusicPlayProgress(String s, int i, int i1, boolean b) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(TeddyEvent event) {
    }
}
