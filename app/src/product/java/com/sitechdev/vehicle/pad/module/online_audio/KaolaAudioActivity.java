package com.sitechdev.vehicle.pad.module.online_audio;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kaolafm.sdk.core.mediaplayer.PlayItem;
import com.sitechdev.vehicle.lib.event.BindEventBus;
import com.sitechdev.vehicle.lib.imageloader.GlideApp;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.BaseActivity;
import com.sitechdev.vehicle.pad.event.TeddyEvent;
import com.sitechdev.vehicle.pad.kaola.KaolaPlayManager;
import com.sitechdev.vehicle.pad.manager.VoiceSourceManager;
import com.sitechdev.vehicle.pad.manager.VoiceSourceType;
import com.sitechdev.vehicle.pad.router.RouterConstants;
import com.sitechdev.vehicle.pad.router.RouterUtils;
import com.sitechdev.vehicle.pad.view.TabLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

//听伴 主页面
@VoiceSourceType(VoiceSourceManager.SUPPORT_TYPE_KAOLA)
@BindEventBus
public class KaolaAudioActivity extends BaseActivity implements
        VoiceSourceManager.MusicChangeListener {
    private static final String TAG = "MusicKaolaForShowActivity";

    private Context mContext;
    //new
    private TabLayout tabLayout;

    private ViewPager pager;
    // 页面类型字符串数组
    private ArrayList<Fragment> fragmentlist = new ArrayList<>();

    private ImageView icon, pre, next, play, list;
    private TextView title;
    private TextView subTitle;
    private VoiceSourceManager.onPlaySourceMusicChangeListener sourceListener = new VoiceSourceManager.onPlaySourceMusicChangeListener() {
        @Override
        public void onMusicPause() {
            play.setImageResource(R.drawable.pc_play);
        }

        @Override
        public void onMusicResume() {
            play.setImageResource(R.drawable.pc_pause);
        }

        @Override
        public void onPlayerFailed(PlayItem item) {

        }

        @Override
        public void onMusicPlaying(PlayItem item) {
            title.setText(item.getTitle());
            subTitle.setText(item.getAlbumName());
            GlideApp.with(KaolaAudioActivity.this).load(item.getAlbumPic()).into(icon);
            play.setImageResource(R.drawable.pc_pause);
        }

        @Override
        public void onMusicPlayEnd(PlayItem item) {

        }

        @Override
        public void onMusicPlayProgress(String s, int i, int i1, boolean b) {

        }
    };
    // 初始化页面集合的方法
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        VoiceSourceManager.getInstance().addMusicChangeListener(this);
        KaolaPlayManager.SingletonHolder.INSTANCE.addPlayVoiceSourceManagerListener(sourceListener);
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
        KaolaPlayManager.SingletonHolder.INSTANCE.clearPlayVoiceSourceManagerListener(sourceListener);
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
            case R.id.pre:
                KaolaPlayManager.SingletonHolder.INSTANCE.playPre();
                break;
            case R.id.next:
                KaolaPlayManager.SingletonHolder.INSTANCE.playNext();
                break;
            case R.id.playpause:
                if (KaolaPlayManager.SingletonHolder.INSTANCE.isPlaying(this)) {
                    play.setImageResource(R.drawable.pc_play);
                } else {
                    play.setImageResource(R.drawable.pc_pause);
                }
                KaolaPlayManager.SingletonHolder.INSTANCE.switchPlayPause(this);
                break;
            case R.id.list:
            case R.id.icon:
            case R.id.title:
            case R.id.sub_title:
                List playlist = KaolaPlayManager.SingletonHolder.INSTANCE.getPlayList();
                if (playlist != null && playlist.size() > 0) {
                    if (KaolaPlayManager.SingletonHolder.INSTANCE.isCurPlayingBroadcast()) {
                        RouterUtils.getInstance().navigation(RouterConstants.MUSIC_PLAY_ONLINE_BROADCAST);
                    } else {
                        RouterUtils.getInstance().navigation(RouterConstants.MUSIC_PLAY_ONLINE);
                    }
                }
                break;
            default:
                break;
        }
    }

    private void initFrags() {
        fragmentlist.add(new KaolaAudioSubPageFrag());
        fragmentlist.add(new KaolaAudioCategoryPageFrag());
        fragmentlist.add(new KaolaAudioBroadcastPageFrag());
        fragmentlist.add(new KaolaAudioSearchPageFrag());
    }

    private void initTabLayout() {
        tabLayout = findViewById(R.id.tv_sub_title);

        icon = findViewById(R.id.icon);
        pre = findViewById(R.id.pre);
        next = findViewById(R.id.next);
        play = findViewById(R.id.playpause);
        list = findViewById(R.id.list);
        title = findViewById(R.id.title);
        subTitle = findViewById(R.id.sub_title);

        pre.setOnClickListener(this);
        next.setOnClickListener(this);
        play.setOnClickListener(this);
        list.setOnClickListener(this);
        title.setOnClickListener(this);
        icon.setOnClickListener(this);

        pager = findViewById(R.id.vp);
        findViewById(R.id.iv_sub_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tabLayout.setupWithViewPager(pager);
        KaolaFragmentAdapter adapter = new KaolaFragmentAdapter(getSupportFragmentManager(), fragmentlist, new String[]{"AI电台", "专辑分类", "在线广播", "搜索"});
        pager.setAdapter(adapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                for (int i = 0; i < tabLayout.getTabCount(); i++) {
                    if (tabLayout.getTabAt(i) == tab && i == 3) {
                        findViewById(R.id.player_holder).setVisibility(View.GONE);
                    } else {
                        findViewById(R.id.player_holder).setVisibility(View.VISIBLE);
                    }
                }
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
        title.setText(name);
    }

    @Override
    public void pause() {
        play.setImageResource(R.drawable.pc_play);
    }

    @Override
    public void resume() {
        play.setImageResource(R.drawable.pc_pause);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(TeddyEvent event) {
    }
}
