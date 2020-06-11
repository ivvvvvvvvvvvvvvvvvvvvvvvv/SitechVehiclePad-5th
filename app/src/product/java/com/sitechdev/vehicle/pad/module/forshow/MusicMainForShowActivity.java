package com.sitechdev.vehicle.pad.module.forshow;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.BaseActivity;
import com.sitechdev.vehicle.pad.module.music.MusicManager;
import com.sitechdev.vehicle.pad.module.music.adapter.MusicPagerAdapter;
import com.sitechdev.vehicle.pad.module.music.fragment.LocalMusicFragment;
import com.sitechdev.vehicle.pad.module.music.service.MusicInfo;
import com.sitechdev.vehicle.pad.router.RouterConstants;
import com.sitechdev.vehicle.pad.view.CommonToast;
import com.sitechdev.vehicle.pad.view.ScrollTextView;

import java.util.ArrayList;
import java.util.List;

@Route(path = RouterConstants.FRAGMENT_LOCAL_SHOW_MUSIC)
public class MusicMainForShowActivity extends BaseActivity {

    private Context context;

    private ViewPager vPager;
    private String[] mTitles = {"本地音乐"};
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
        return R.layout.activity_music_main_for_show;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ((TextView) findViewById(R.id.tv_sub_title)).setText("本地音乐");
        vPager = findViewById(R.id.music_main_viewpager);
        mFragments = new ArrayList<>();
        mFragments.add(LocalMusicFragment.newInstance());
        mAdapter = new MusicPagerAdapter(getSupportFragmentManager(), mFragments);
        vPager.setAdapter(mAdapter);
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
        findViewById(R.id.iv_sub_back).setOnClickListener(this);
    }

    @Override
    protected void initData() {
        playHolder.show();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.iv_sub_back:
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
        private TextView subtitle;
        private ScrollTextView tv_bottom_title;

        public PlayHolder(View view) {
            this.holderView = view;
            findView();
        }

        private void findView() {
            btn_pre = holderView.findViewById(R.id.btn_pre);
            btn_next = holderView.findViewById(R.id.btn_next);
            btn_pause_play = holderView.findViewById(R.id.btn_pause_play);
            tv_bottom_title = holderView.findViewById(R.id.tv_bottom_title);
            subtitle = holderView.findViewById(R.id.subtitle);
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
                tv_bottom_title.setText(current.musicName);
                subtitle.setText(current.artist);
                switch (status){
                    case MusicManager.OnMusicChangeListener.PAUSE:
                        btn_pause_play.setImageResource(R.drawable.pc_play);
                        break;
                    case MusicManager.OnMusicChangeListener.RESUME:
                        btn_pause_play.setImageResource(R.drawable.pc_pause);
                        break;
                }
            }else {
                tv_bottom_title.setText("");
                btn_pause_play.setImageResource(R.drawable.pc_play);
            }
        }

        @Override
        public void onMusicListUpdate(List<MusicInfo> infos, MusicInfo info, int status, int postion) {
            //TODO
        }
    }
}
