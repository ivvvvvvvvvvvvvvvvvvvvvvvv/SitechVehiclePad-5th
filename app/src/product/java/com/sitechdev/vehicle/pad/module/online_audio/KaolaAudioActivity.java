package com.sitechdev.vehicle.pad.module.online_audio;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.kaolafm.sdk.core.mediaplayer.PlayItem;
import com.sitechdev.vehicle.lib.event.BindEventBus;
import com.sitechdev.vehicle.lib.event.EventBusUtils;
import com.sitechdev.vehicle.lib.imageloader.GlideApp;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.BaseActivity;
import com.sitechdev.vehicle.pad.event.KaolaEvent;
import com.sitechdev.vehicle.pad.event.TeddyEvent;
import com.sitechdev.vehicle.pad.kaola.KaolaPlayManager;
import com.sitechdev.vehicle.pad.manager.VoiceSourceManager;
import com.sitechdev.vehicle.pad.manager.VoiceSourceType;
import com.sitechdev.vehicle.pad.router.RouterConstants;
import com.sitechdev.vehicle.pad.router.RouterUtils;
import com.sitechdev.vehicle.pad.view.TabLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

//听伴 主页面
@Route(path = RouterConstants.MUSIC_PLAY_ONLINE_MAIN)
@VoiceSourceType(VoiceSourceManager.SUPPORT_TYPE_KAOLA)
@BindEventBus
public class KaolaAudioActivity extends BaseActivity implements
        VoiceSourceManager.MusicChangeListener {
    private static final String TAG = "MusicKaolaForShowActivity";
    @Autowired
    public int deepIndex = -1;
    @Autowired
    public String queryString = "";
    @Autowired
    public int pageIndex = -1;
    @Autowired
    public boolean playIfSuspend = false;
    private Context mContext;
    //new
    private TabLayout tabLayout;

    private ViewPager pager;
    // 页面类型字符串数组

    private ImageView icon, pre, next, play, list;
    private TextView title;
    private TextView subTitle;
    String[] titleArr = new String[]{"AI电台", "专辑分类", "在线广播", "搜索"};
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
            subTitle.setText(" - " + item.getAlbumName());
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
        mContext = this;
        ARouter.getInstance().inject(this);
        super.onCreate(savedInstanceState);
        VoiceSourceManager.getInstance().addMusicChangeListener(this);
        KaolaPlayManager.SingletonHolder.INSTANCE.addPlayVoiceSourceManagerListener(sourceListener);
//        getLastCustomNonConfigurationInstance();
    }

//    @Override
//    public Object onRetainCustomNonConfigurationInstance() {
//        return super.onRetainCustomNonConfigurationInstance();
//    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        ARouter.getInstance().inject(this);
        super.onNewIntent(intent);
        if (!TextUtils.isEmpty(queryString)) {
            pager.setCurrentItem(3);
            EventBusUtils.postEvent(new TeddyEvent(TeddyEvent.EVENT_TEDDY_KAOLA_QUERY_KEYWORDS, queryString));
            queryString = "";
        }
    }

    @Override
    protected int getLayoutId() {
        return  R.layout.audio_kaola_main_frame;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        for (int i = 0; i < getSupportFragmentManager().getFragments().size(); i++) {
            Fragment fragment = getSupportFragmentManager().getFragments().get(i);
            if (fragment instanceof KaolaAudioSearchPageFrag) {
                outState.putParcelable("search_page_saveinstance", ((KaolaAudioSearchPageFrag) fragment).getSaveInstanceState());
                break;
            }
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey("search_page_saveinstance")) {
            EventBusUtils.postStickyEvent(new KaolaEvent(KaolaEvent.EB_KAOLA_RESTORE_SEARCH_STATUS, savedInstanceState.getParcelable("search_page_saveinstance")));
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
//        initFrags();
        initTabLayout();
        if (KaolaPlayManager.SingletonHolder.INSTANCE.isPlaying(this)) {
            PlayItem item = KaolaPlayManager.SingletonHolder.INSTANCE.getCurPlayingItem();
            if (item != null) {
                GlideApp.with(KaolaAudioActivity.this).load(item.getAlbumPic()).into(icon);
            }
        }
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

    //    private void initFrags() {
//        fragmentlist.clear();
//        fragmentlist.add(new KaolaAudioSubPageFrag(pageIndex, deepIndex, playIfSuspend));
//        fragmentlist.add(new KaolaAudioCategoryPageFrag());
//        fragmentlist.add(new KaolaAudioBroadcastPageFrag());
//        fragmentlist.add(new KaolaAudioSearchPageFrag());
//    }
    KaolaFragmentAdapter adapter;

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
        GlideApp.with(KaolaAudioActivity.this).load("").placeholder(R.drawable.default_audio_2).into(icon);
        title.setText("无内容");
        pager = findViewById(R.id.vp);
        findViewById(R.id.iv_sub_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tabLayout.setupWithViewPager(pager);

        adapter = new KaolaFragmentAdapter(getSupportFragmentManager(), titleArr, pageIndex, deepIndex, playIfSuspend, queryString);
        pager.setAdapter(adapter);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(getTabView(i));
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View bottom = findViewById(R.id.bottom_space);
                for (int i = 0; i < tabLayout.getTabCount(); i++) {
                    if (tabLayout.getTabAt(i) == tab && i == 3) {
                        findViewById(R.id.player_holder).setVisibility(View.GONE);
                        if (isLandscape()) {
                            ViewGroup.LayoutParams layoutParams = bottom.getLayoutParams();
                            layoutParams.height = (int) getResources().getDimension(R.dimen.kaola_margin_bottom_land_space_after_hide_playholder);
                            bottom.setLayoutParams(layoutParams);
                            EventBusUtils.postEvent(new KaolaEvent(KaolaEvent.EB_KAOLA_REFRESH_SEARCH_VIEW));
                        }
                    } else {
                        findViewById(R.id.player_holder).setVisibility(View.VISIBLE);
                        if (isLandscape()) {
                            ViewGroup.LayoutParams layoutParams = bottom.getLayoutParams();
                            layoutParams.height = (int) getResources().getDimension(isLandscape() ? R.dimen.kaola_margin_bottom_land : R.dimen.kaola_margin_bottom_land);
                            bottom.setLayoutParams(layoutParams);
                        }
                    }
                }
                View text = tab.getCustomView().findViewById(android.R.id.text1);
                selectTabAni(text);
                text.setAlpha(1);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View text = tab.getCustomView().findViewById(android.R.id.text1);
                unselectTabAni(text);
                text.setAlpha(0.5f);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        int defaultIndex = 0;
        if (!TextUtils.isEmpty(queryString)) {
            defaultIndex = 3;
            pager.setCurrentItem(defaultIndex);
        } else {
            View text = tabLayout.getTabAt(defaultIndex).getCustomView().findViewById(android.R.id.text1);
            selectTabAni(text);
            text.setAlpha(1f);
        }
        queryString = "";
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

    /**
     * 自定义Tab的View
     *
     * @param currentPosition
     * @return
     */
    private View getTabView(int currentPosition) {
        View view = LayoutInflater.from(KaolaAudioActivity.this).inflate(R.layout.layout_kaola_tab, null);
        TextView textView = view.findViewById(android.R.id.text1);
        textView.setText(titleArr[currentPosition]);
        return view;
    }

    private void selectTabAni(View view) {
        if (null != view) {
            ValueAnimator animx = ObjectAnimator
                    .ofFloat(view, "ScaleX", 1F, 1.1F)
                    .setDuration(100);
            ValueAnimator animy = ObjectAnimator
                    .ofFloat(view, "ScaleY", 1F, 1.1F)
                    .setDuration(100);
            AnimatorSet set = new AnimatorSet();
            set.playTogether(animx, animy);
            set.setTarget(view);
            set.setInterpolator(new LinearInterpolator());
            animx.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    view.setScaleX(1.1f);
                    view.setScaleY(1.1f);
                }
            });
            set.start();
        }
    }

    private void unselectTabAni(View view) {
        if (null != view) {
            ValueAnimator animx = ObjectAnimator
                    .ofFloat(view, "ScaleX", 1.1F, 1F)
                    .setDuration(100);
            ValueAnimator animy = ObjectAnimator
                    .ofFloat(view, "ScaleY", 1.1F, 1F)
                    .setDuration(100);
            AnimatorSet set = new AnimatorSet();
            set.playTogether(animx, animy);
            set.setTarget(view);
            set.setInterpolator(new LinearInterpolator());
            animx.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    view.setScaleX(1.0f);
                    view.setScaleY(1.0f);
                }
            });
            set.start();
        }
    }

}
