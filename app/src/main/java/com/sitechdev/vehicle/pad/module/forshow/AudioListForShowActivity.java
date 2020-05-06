package com.sitechdev.vehicle.pad.module.forshow;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kaolafm.sdk.core.mediaplayer.IPlayerStateListener;
import com.kaolafm.sdk.core.mediaplayer.PlayItem;
import com.kaolafm.sdk.core.mediaplayer.PlayerManager;
import com.sitechdev.vehicle.lib.imageloader.GlideApp;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.BaseActivity;
import com.sitechdev.vehicle.pad.model.AudioListForShowBean;
import com.sitechdev.vehicle.pad.module.music.MusicManager;
import com.sitechdev.vehicle.pad.view.CommonToast;
import com.sitechdev.vehicle.pad.view.ScrollTextView;

import java.util.ArrayList;

public class AudioListForShowActivity extends BaseActivity {
    Context mContext;

    ImageView iv_back;
    ImageView btn_pre;
    ImageView btn_pause_play;
    ImageView btn_next;
    ImageView btn_pop_list;
    ScrollTextView tv_bottom_title;
    TextView tv_title;
    LinearLayout play_bar_root;
    int[] imgs = {R.drawable.teddy_top_1, R.drawable.teddy_top_2, R.drawable.teddy_top_3, R.drawable.teddy_top_4, R.drawable.teddy_top_5, R.drawable.teddy_top_6, R.drawable.teddy_top_7};
    String[] titles = {"百变布鲁克","儿童英语儿歌早教歌曲","金色童谣","格林童话","中国寓言故事","小红帽与大灰狼","儿童故事广播剧"};
    String[] subTitles = {"","","精选中国著名童诗","演绎经典格林通话","朗朗上口，旋律优美","宝贝听故事，每天给孩子一个睡前故事","必读经典名著"};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_audio_play_main;
    }

    @Override
    protected void initViewBefore() {
        super.initViewBefore();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        iv_back = findViewById(R.id.iv_sub_back);
        btn_pre = findViewById(R.id.btn_pre);
        btn_pause_play = findViewById(R.id.btn_pause_play);
        btn_next = findViewById(R.id.btn_next);
        btn_pop_list = findViewById(R.id.btn_pop_list);
        tv_bottom_title = findViewById(R.id.tv_bottom_title);
        tv_title = findViewById(R.id.tv_sub_title);

        play_bar_root = findViewById(R.id.play_bar_root);
        if (PlayerManager.getInstance(mContext).isPlaying()) {
            play_bar_root.setVisibility(View.VISIBLE);
        } else {
            play_bar_root.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initData();
        initListener();
    }

    @Override
    protected void initData() {
        int type = getIntent().getIntExtra("type", -1);
        switch (type) {
            case 0:
                tv_title.setText("新特速报");
                break;
            case 1:
                tv_title.setText("少儿读物");
                break;
            case 2:
                tv_title.setText("车嗨娱乐");
                break;
            case 3:
                tv_title.setText("生活一点通");
                break;
            default:
                tv_title.setText("少儿读物");
                break;
        }
        initCard();
    }

    private void initCard() {
        ArrayList<AudioListForShowBean> data = initMockData();
        View card1 = findViewById(R.id.card1);
        View card2 = findViewById(R.id.card2);
        View card3 = findViewById(R.id.card3);
        View card4 = findViewById(R.id.card4);
        View card5 = findViewById(R.id.card5);
        View card6 = findViewById(R.id.card6);
        View card7 = findViewById(R.id.card7);

        card1.setOnClickListener(this);
        card2.setOnClickListener(this);
        card3.setOnClickListener(this);
        card4.setOnClickListener(this);
        card5.setOnClickListener(this);
        card6.setOnClickListener(this);
        card7.setOnClickListener(this);

        GlideApp.with(this).load(data.get(0).getImageResource()).into((ImageView) card1.findViewById(R.id.image));
        GlideApp.with(this).load(data.get(1).getImageResource()).into((ImageView) card2.findViewById(R.id.image));
        GlideApp.with(this).load(data.get(2).getImageResource()).into((ImageView) card3.findViewById(R.id.image));
        GlideApp.with(this).load(data.get(3).getImageResource()).into((ImageView) card4.findViewById(R.id.image));
        GlideApp.with(this).load(data.get(4).getImageResource()).into((ImageView) card5.findViewById(R.id.image));
        GlideApp.with(this).load(data.get(5).getImageResource()).into((ImageView) card6.findViewById(R.id.image));
        GlideApp.with(this).load(data.get(6).getImageResource()).into((ImageView) card7.findViewById(R.id.image));

        ((TextView)card1.findViewById(R.id.title)).setText(data.get(0).getTitle());
        ((TextView)card2.findViewById(R.id.title)).setText(data.get(1).getTitle());
        ((TextView)card3.findViewById(R.id.title)).setText(data.get(2).getTitle());
        ((TextView)card4.findViewById(R.id.title)).setText(data.get(3).getTitle());
        ((TextView)card5.findViewById(R.id.title)).setText(data.get(4).getTitle());
        ((TextView)card6.findViewById(R.id.title)).setText(data.get(5).getTitle());
        ((TextView)card7.findViewById(R.id.title)).setText(data.get(6).getTitle());

        ((TextView)card3.findViewById(R.id.subtitle)).setText(data.get(2).getSubTitle());
        ((TextView)card4.findViewById(R.id.subtitle)).setText(data.get(3).getSubTitle());
        ((TextView)card5.findViewById(R.id.subtitle)).setText(data.get(4).getSubTitle());
        ((TextView)card6.findViewById(R.id.subtitle)).setText(data.get(5).getSubTitle());
        ((TextView)card7.findViewById(R.id.subtitle)).setText(data.get(6).getSubTitle());
    }

    private ArrayList initMockData() {
        ArrayList<AudioListForShowBean> data = new ArrayList();
        for (int i = 0; i < imgs.length; i++) {
            AudioListForShowBean bean = new AudioListForShowBean();
            bean.setImg(imgs[i]);
            bean.setTitle(titles[i]);
            bean.setSubTitle(subTitles[i]);
            data.add(bean);
        }
        return data;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (PlayerManager.getInstance(mContext).isPlaying()) {
            play_bar_root.setVisibility(View.VISIBLE);
        } else {
            play_bar_root.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        iv_back.setOnClickListener(this);
        btn_pre.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        btn_pause_play.setOnClickListener(this);
        btn_pop_list.setOnClickListener(this);


        PlayerManager.getInstance(mContext).addPlayerStateListener(playerStateListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_sub_back:
                finish();
                break;
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
            case R.id.btn_pop_list:
            case R.id.card1:
            case R.id.card2:
            case R.id.card3:
            case R.id.card4:
            case R.id.card5:
            case R.id.card6:
            case R.id.card7:
                Intent intent = new Intent(AudioListForShowActivity.this, MusicMainForShowActivity.class);
                startActivity(intent);
                break;
        }
    }

    private IPlayerStateListener playerStateListener = new IPlayerStateListener() {
        @Override
        public void onIdle(PlayItem playItem) {
            SitechDevLog.e(AudioListForShowActivity.class.getSimpleName(), "====== onIdle =======");
        }

        @Override
        public void onPlayerPreparing(PlayItem playItem) {
            SitechDevLog.e(AudioListForShowActivity.class.getSimpleName(), "====== onPlayerPreparing =======");
        }

        @Override
        public void onPlayerPlaying(PlayItem playItem) {
            if (playItem != null) {
                tv_bottom_title.setText(playItem.getTitle());
                btn_pause_play.setActivated(true);
            }

            SitechDevLog.e(AudioListForShowActivity.class.getSimpleName(), "====== onPlayerPlaying =======");
        }

        @Override
        public void onPlayerPaused(PlayItem playItem) {
            SitechDevLog.e(AudioListForShowActivity.class.getSimpleName(), "====== onPlayerPaused =======");
            btn_pause_play.setActivated(false);
        }

        @Override
        public void onProgress(String s, int i, int i1, boolean b) {
            SitechDevLog.e(AudioListForShowActivity.class.getSimpleName(), "====== onProgress =======");
        }

        @Override
        public void onPlayerFailed(PlayItem playItem, int i, int i1) {
            SitechDevLog.e(AudioListForShowActivity.class.getSimpleName(), "====== onPlayerFailed =======");
        }

        @Override
        public void onPlayerEnd(PlayItem playItem) {
            SitechDevLog.e(AudioListForShowActivity.class.getSimpleName(), "====== onPlayerEnd =======");
        }

        @Override
        public void onSeekStart(String s) {
            SitechDevLog.e(AudioListForShowActivity.class.getSimpleName(), "====== onSeekStart =======");
        }

        @Override
        public void onSeekComplete(String s) {
            SitechDevLog.e(AudioListForShowActivity.class.getSimpleName(), "====== onSeekComplete =======");
        }

        @Override
        public void onBufferingStart(PlayItem playItem) {
            SitechDevLog.e(AudioListForShowActivity.class.getSimpleName(), "====== onBufferingStart =======");
        }

        @Override
        public void onBufferingEnd(PlayItem playItem) {
            SitechDevLog.e(AudioListForShowActivity.class.getSimpleName(), "====== onBufferingEnd =======");
        }
    };
}
