package com.sitechdev.vehicle.pad.module.music;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.BaseActivity;
import com.sitechdev.vehicle.pad.module.music.adapter.MusicPagerAdapter;
import com.sitechdev.vehicle.pad.module.music.fragment.OtherMusicFragment;
import com.sitechdev.vehicle.pad.module.music.service.MusicInfo;
import com.sitechdev.vehicle.pad.view.CommonToast;
import com.sitechdev.vehicle.pad.view.ScrollTextView;

import java.util.ArrayList;
import java.util.List;

public class MusicMainActivity extends BaseActivity {

    private Context context;

    private SlidingTabLayout vTab;
    private ViewPager vPager;
    private String[] mTitles = {"音乐", "本地音乐"};
    private List<Fragment> mFragments;
    private MusicPagerAdapter mAdapter;
    private PlayHolder playHolder;
    private int index= 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        analyzeIntent(intent);
    }

    private void analyzeIntent(Intent intent){
        if (null != intent){
            index = intent.getIntExtra("index", 0);
        }
        if (null != vPager){
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
        mFragments = new ArrayList<>();
        mFragments.add(OtherMusicFragment.newInstance());
        mFragments.add(LocalMusicFragment.newInstance());
        mAdapter = new MusicPagerAdapter(getSupportFragmentManager(), mFragments);
        vPager.setAdapter(mAdapter);
        vTab.setViewPager(vPager, mTitles);
        vTab.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                int count = vTab.getTabCount();
                for (int i = 0; i < count; i++){
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
                if (position == 1 && null != playHolder){
                    playHolder.show();
                }else {
                    playHolder.hide();
                }
                int count = vTab.getTabCount();
                for (int i = 0; i < count; i++){
                    vTab.getTitleView(i).setTextSize(24);
                }
                vTab.getTitleView(position).setTextSize(27);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        playHolder = new PlayHolder(findViewById(R.id.play_detail_status));
        MusicManager.getInstance().addMusicChangeListener(playHolder);
        MusicManager.getInstance().addMusicListUpdateListener(playHolder);
        analyzeIntent(getIntent());
    }

    @Override
    protected void onDestroy() {
        MusicManager.getInstance().removeMusicChangeListener(playHolder);
        MusicManager.getInstance().removeMusicListUpdateListener(playHolder);
        super.onDestroy();
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
        switch (v.getId()){
            case R.id.music_main_back:
                onBackPressed();
                break;
        }
    }

    private static class PlayHolder implements View.OnClickListener,
            MusicManager.OnMusicChangeListener,
            MusicManager.OnMusicListUpdateListener{

        private View holderView;

        private ImageView btn_pre;
        private ImageView btn_next;
        private ImageView btn_pause_play;
        private ScrollTextView tv_bottom_title;

        public PlayHolder(View view) {
            this.holderView = view;
            findView();
        }

        private void findView() {
            btn_pre = holderView.findViewById(R.id.btn_pre);
            btn_next = holderView.findViewById(R.id.btn_next);
            btn_pause_play = holderView.findViewById(R.id.btn_pause_play);
            holderView.findViewById(R.id.btn_pop_list).setVisibility(View.GONE);
            tv_bottom_title = holderView.findViewById(R.id.tv_bottom_title);
            btn_pre.setOnClickListener(this);
            btn_next.setOnClickListener(this);
            btn_pause_play.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_pre:
                    MusicManager.getInstance().pre(new MusicManager.CallBack<String>() {
                        @Override
                        public void onCallBack(int code, String s) {
                            if (code != 0){
                                CommonToast.showToast(s);
                            }
                        }
                    });
                    break;
                case R.id.btn_next:
                    MusicManager.getInstance().next(new MusicManager.CallBack<String>() {
                        @Override
                        public void onCallBack(int code, String s) {
                            if (code != 0){
                                CommonToast.showToast(s);
                            }
                        }
                    });
                    break;
                case R.id.btn_pause_play:
                    MusicManager.getInstance().toggle(new MusicManager.CallBack<String>() {
                        @Override
                        public void onCallBack(int code, String s) {
                            if (code != 0){
                                CommonToast.showToast(s);
                            }
                        }
                    });
                    break;
            }
        }

        public void show() {
            if (null != holderView){
                holderView.setVisibility(View.VISIBLE);
            }
        }

        public void hide() {
            if (null != holderView){
                holderView.setVisibility(View.GONE);
            }
        }

        @Override
        public void onMusciChange(MusicInfo current, int status) {
            if (null != current){
                tv_bottom_title.setText("");
                tv_bottom_title.append(current.musicName);
                tv_bottom_title.append(" - ");
                tv_bottom_title.append(current.artist);
                switch (status){
                    case MusicManager.OnMusicChangeListener.PAUSE:
                        btn_pause_play.setActivated(false);
                        break;
                    case MusicManager.OnMusicChangeListener.RESUME:
                        btn_pause_play.setActivated(true);
                        break;
                }
            }else {
                tv_bottom_title.setText("");
                btn_pause_play.setActivated(false);
            }
        }

        @Override
        public void onMusicListUpdate(List<MusicInfo> infos, MusicInfo info, int status, int postion) {
            //TODO
        }
    }
}
