package com.sitechdev.vehicle.pad.module.online_audio;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.kaolafm.opensdk.api.broadcast.BroadcastDetails;
import com.kaolafm.opensdk.api.broadcast.BroadcastRequest;
import com.kaolafm.opensdk.http.core.HttpCallback;
import com.kaolafm.opensdk.http.error.ApiException;
import com.kaolafm.opensdk.utils.ListUtil;
import com.kaolafm.sdk.core.mediaplayer.BroadcastRadioListManager;
import com.kaolafm.sdk.core.mediaplayer.BroadcastRadioPlayerManager;
import com.kaolafm.sdk.core.mediaplayer.IPlayerListChangedListener;
import com.kaolafm.sdk.core.mediaplayer.OnPlayItemInfoListener;
import com.kaolafm.sdk.core.mediaplayer.PlayItem;
import com.kaolafm.sdk.core.mediaplayer.PlayItemType;
import com.kaolafm.sdk.core.mediaplayer.PlayerManager;
import com.kaolafm.sdk.core.mediaplayer.PlayerRadioListManager;
import com.kaolafm.sdk.vehicle.GeneralCallback;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.sitechdev.vehicle.lib.event.BindEventBus;
import com.sitechdev.vehicle.lib.imageloader.GlideApp;
import com.sitechdev.vehicle.lib.util.Constant;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.lib.util.ThreadUtils;
import com.sitechdev.vehicle.lib.util.TimeUtils;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.BaseActivity;
import com.sitechdev.vehicle.pad.event.TeddyEvent;
import com.sitechdev.vehicle.pad.kaola.KaolaPlayManager;
import com.sitechdev.vehicle.pad.kaola.PlayItemAdapter;
import com.sitechdev.vehicle.pad.manager.VoiceSourceManager;
import com.sitechdev.vehicle.pad.manager.VoiceSourceType;
import com.sitechdev.vehicle.pad.router.RouterConstants;
import com.sitechdev.vehicle.pad.view.CommonToast;
import com.sitechdev.vehicle.pad.view.ScrollTextView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

// 听伴广播播放页
@Route(path = RouterConstants.MUSIC_PLAY_ONLINE_BROADCAST)
@VoiceSourceType(VoiceSourceManager.SUPPORT_TYPE_KAOLA)
@BindEventBus
public class MusicKaolaBroadcastActivity extends BaseActivity implements
        VoiceSourceManager.MusicChangeListener, VoiceSourceManager.onPlaySourceMusicChangeListener {
    private static final String TAG = "MusicKaolaActivity";
    public static String title = "";

    private Context mContext;
    //new
    private Constant.TYPE TYPE_CODE;
    private long id;
    private boolean isAlbum;
    private String imageUrl;
    private int mCurPosition = 0;

    private boolean flag_FIRST_PLAY;
    private static KaolaPlayManager.PlayType mCurrentType = null;
    private List<PlayItemAdapter.Item> playDataItemList = new ArrayList<>();

    private RecyclerView vLocalMusicList;

    private TwinklingRefreshLayout refreshLayout;

    private MusicKaolaAdapter playListAdapter;

    private ImageView musicImageView, btn_pre, btn_next, btn_pause_play, tag;
    private TextView subtitle;
    private ScrollTextView tv_bottom_title;
    private int defaultImgResId = 0;
    private TextView tv_title;
    private FrameLayout playCoverHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flag_FIRST_PLAY = true;
        mContext = this;
        VoiceSourceManager.getInstance().addMusicChangeListener(this);
        KaolaPlayManager.SingletonHolder.INSTANCE.addPlayVoiceSourceManagerListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initData(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_music_kaola_main;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        tv_title = findViewById(R.id.tv_sub_title);
        subtitle = findViewById(R.id.subtitle);

        //左边播放条控制

        //主标题
        tv_bottom_title = findViewById(R.id.tv_bottom_title);
        //专辑图片
        musicImageView = findViewById(R.id.image);
        playCoverHolder = findViewById(R.id.play_cover_frame);
        //上一首
        btn_pre = findViewById(R.id.btn_pre);
        //下一首
        btn_next = findViewById(R.id.btn_next);
        //播放，暂停
        btn_pause_play = findViewById(R.id.btn_pause_play);
        tag = findViewById(R.id.tag);
        findViewById(R.id.play_bar_root).setVisibility(View.GONE);

        //数据传入
        initPlayListView();

        showProgressDialog();

        initData(getIntent());

    }

    private void initData(Intent intent) {
        TYPE_CODE = (Constant.TYPE) intent.getSerializableExtra(Constant.KEY_TYPE_KEY);
        if (TYPE_CODE == Constant.TYPE.FIRST_ENTERED) {
            SitechDevLog.e(TAG, "======== FIRST_ENT1ERED ");
            id = intent.getLongExtra(Constant.KEY_MEMBER_CODE, -1);
            imageUrl = intent.getStringExtra(Constant.KEY_IMG_URL);
            title = intent.getStringExtra(Constant.KEY_TITLE);
            mCurPosition = BroadcastRadioListManager.getInstance().getCurPosition();
            requestKaoLaInfo();
        } else {
            PlayItem curPlayItem = BroadcastRadioListManager.getInstance().getCurPlayItem();
            mCurPosition = BroadcastRadioListManager.getInstance().getCurPosition();
            SitechDevLog.e(TAG, "========== PLAYING   mCurPosition " + mCurPosition);
            if (tv_bottom_title != null && curPlayItem != null) {
                tv_bottom_title.setText(curPlayItem.getTitle());
            }
            setListData();
        }
        //默认图片索引
        GlideApp.with(this).load(imageUrl).into(musicImageView);
        tv_title.setText(title);
    }

    private void initPlayListView() {
        refreshLayout = findViewById(R.id.music_kaola_refresh_layout);
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setEnableLoadmore(false);
//        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
//            @Override
//            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
//                super.onLoadMore(refreshLayout);
//                SitechDevLog.e(TAG, "refreshLayout ============");
//                fetchMore(true);
//            }
//        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        vLocalMusicList = findViewById(R.id.music_kaola_list);
        vLocalMusicList.setLayoutManager(linearLayoutManager);
        vLocalMusicList.setItemAnimator(new DefaultItemAnimator());
//        vLocalMusicList.addItemDecoration(new RecycleViewDivider(this,
//                playListAdapter, DensityUtils.dp2px(20),
//                getResources().getColor(R.color.white_5),
//                getResources().getColor(R.color.white_21)));
        vLocalMusicList.setHasFixedSize(true);

        if (playListAdapter == null) {
            playListAdapter = new MusicKaolaAdapter(MusicKaolaBroadcastActivity.this, true);
            vLocalMusicList.setAdapter(playListAdapter);

            playListAdapter.setOnItemClickListener(new MusicKaolaAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(PlayItemAdapter.Item item ,int position) {
                    if (true) {
                        //todo  广播回放
                        return;
                    }
                    //1-直播中，2-回放，3-未开播
                    if (item.status == 3) {
                        return;
                    }
                    mCurPosition = position;
                    playListAdapter.setSelected(position);
                    if (item.status == 2) {
                        PlayerManager.getInstance(mContext).playPgc(item.id);
                    }else{
                        BroadcastRadioPlayerManager.getInstance().playBroadcast(item.id, new GeneralCallback<Boolean>() {
                            @Override
                            public void onResult(Boolean aBoolean) {
                                Log.e("","");
                            }

                            @Override
                            public void onError(int i) {
                                Log.e("","");
                            }

                            @Override
                            public void onException(Throwable throwable) {
                                Log.e("","");
                            }
                        });
                        new BroadcastRequest().getBroadcastDetails(item.id, new HttpCallback<BroadcastDetails>() {
                            @Override
                            public void onSuccess(BroadcastDetails broadcastDetails) {
                                Log.e("","");
                            }

                            @Override
                            public void onError(ApiException e) {

                            }
                        });
                    }

                }
            });
        } else {
            playListAdapter.notifyDataSetChanged();
        }
    }

    private void setListData() {
        ThreadUtils.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                ArrayList<PlayItem> playList = BroadcastRadioListManager.getInstance().getPlayList();
                if (playDataItemList == null) {
                    playDataItemList = new ArrayList<>();
                } else {
                    playDataItemList.clear();
                }
                //重组播放列表
                transformDataToItem(playList, playDataItemList);

                if (playListAdapter == null) {
                    playListAdapter = new MusicKaolaAdapter(MusicKaolaBroadcastActivity.this);
                    if (playDataItemList != null) {
                        SitechDevLog.e(TAG, "setListData mCurPosition ====" + mCurPosition);
                        playListAdapter.refreshData(playDataItemList, mCurPosition);
                    }
                    vLocalMusicList.setAdapter(playListAdapter);
                } else {
                    if (playDataItemList != null) {
                        playListAdapter.setData(playDataItemList);
                    }
                }
                playListAdapter.notifyDataSetChanged();
                SitechDevLog.e(TAG, "setListData smoothScrollToPosition mCurPosition ====" + mCurPosition);
                vLocalMusicList.smoothScrollToPosition(mCurPosition);
                cancelProgressDialog();
            }
        });

    }

    /**
     * 播单数据变化监听
     */
    private IPlayerListChangedListener mPlayerListChangedListener = dataList -> {
        SitechDevLog.e(TAG, "ai data changed ");
        if (dataList == null || dataList.isEmpty()) {
            return;
        }

    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        KaolaPlayManager.SingletonHolder.INSTANCE.clearPlayVoiceSourceManagerListener(this);
    }

    @Override
    protected void initListener() {
        super.initListener();
        findViewById(R.id.iv_sub_back).setOnClickListener(this);

        btn_pre.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        btn_pause_play.setOnClickListener(this);
    }

    @Override
    protected void initData() {
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_sub_back:
                finish();
                break;
            case R.id.btn_pre:
                playPre();
                break;
            case R.id.btn_next:
                playNext();
                break;
            case R.id.btn_pause_play:
                switchPlayPause();
                findViewById(R.id.btn_pause_play).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (playListAdapter != null) {
                            playListAdapter.notifyDataSetChanged();
                        }
                    }
                }, 200);
                break;
            default:
                break;
        }
    }

    private void playPre() {
        //todo 添加能否播放判断
        SitechDevLog.e(TAG, "========  playPre  was called");
//        boolean hasPre = KaolaPlayManager.SingletonHolder.INSTANCE.playPre(true);
//        if (hasPre) {
//            move2Pre();
//        }
    }

    private void move2Pre() {
        if (playListAdapter != null) {
            mCurPosition--;
            playListAdapter.setSelected(mCurPosition);
            vLocalMusicList.smoothScrollToPosition(mCurPosition);
        }
    }

    private void playNext() {
        //todo 添加能否播放判断
        SitechDevLog.e(TAG, "========  playNext  was called");
//        boolean hasNext = KaolaPlayManager.SingletonHolder.INSTANCE.playNext(true);
//        if (hasNext) {
//            move2Next();
//        }
    }

    private void move2Next() {
        if (playListAdapter != null) {
            SitechDevLog.e(TAG, "move2Next  mCurPosition ====" + mCurPosition);
            mCurPosition++;
            playListAdapter.setSelected(mCurPosition);
            vLocalMusicList.smoothScrollToPosition(mCurPosition);
            SitechDevLog.e(TAG, "move2Next end  mCurPosition ====" + mCurPosition);
        }
    }

    private void fetchMore(boolean isLoadMore) {

        //获取播放器中的播单用来显示
        if (!isLoadMore) {
            ArrayList<PlayItem> playList = BroadcastRadioListManager.getInstance().getPlayList();
            //有可能为空，因为数据有可能还没请求过来。
            if (!ListUtil.isEmpty(playList)) {
                CommonToast.makeText(mContext, "数据为空");
            }
        } else {
            PlayerRadioListManager.getInstance().fetchMorePlaylist(new OnPlayItemInfoListener() {
                @Override
                public void onPlayItemReady(PlayItem playItem) {
                    SitechDevLog.i(TAG, "onPlayItemReady=== ~~~ playItem~~~" + playItem.getTitle());
                }

                @Override
                public void onPlayItemUnavailable() {
                    CommonToast.makeText(mContext, "播放出错 ~~~ ~~~");
                    SitechDevLog.i(TAG, "onPlayItemUnavailable=== ~~~ ~~~");
                }

                @Override
                public void onPlayItemReady(List<PlayItem> list) {
                    SitechDevLog.i(TAG, "onPlayItemReady=== ~~~ list~~~" + list.size());
                    refreshLayout.finishLoadmore();

                }
            }, false, true);
        }
    }

    private synchronized void addPlayListToFullList(List<PlayItem> list) {
        if (playDataItemList != null) {
            playDataItemList.clear();
        }
        transformDataToItem(list, playDataItemList);

        if (playListAdapter != null) {
            playListAdapter.refreshDataChanged(playDataItemList, mCurPosition);
            SitechDevLog.e(TAG, "addPlayListToFullList  mCurPosition ====" + mCurPosition);
            vLocalMusicList.smoothScrollToPosition(mCurPosition);
        }
    }

    private void transformDataToItem(List<PlayItem> dataList, List<PlayItemAdapter.Item> datas) {
        for (int i = 0, size = dataList.size(); i < size; i++) {
            PlayItem item = dataList.get(i);
            PlayItemAdapter.Item sai = new PlayItemAdapter.Item();
            sai.id = item.getAudioId();
            sai.title = item.getTitle();
            sai.status = item.getStatus();
            sai.item = item;
            sai.details = TimeUtils.formatTime(item.getStartTime(), "HH:mm") + "-" + TimeUtils.formatTime(item.getFinishTime(), "HH:mm");
            datas.add(sai);
        }
    }

    /**
     * 切换暂停与继续播放
     */
    private void switchPlayPause() {
        SitechDevLog.e(TAG, "========  switchPlayPause  was called");
        KaolaPlayManager.SingletonHolder.INSTANCE.switchPlayPause(this);
        btn_pause_play.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshPlayStatusView();
            }
        }, 500);//切换有延迟
    }

    /**
     * 刷新view
     */
    private void refreshPlayStatusView() {
        if (btn_pause_play != null) {
            if (KaolaPlayManager.SingletonHolder.INSTANCE.isPlaying(this)) {
                btn_pause_play.setImageResource(R.drawable.pc_pause);
                KaolaPlayManager.setCoverPlayStartAnim(playCoverHolder);
            } else {
                btn_pause_play.setImageResource(R.drawable.pc_play);
                KaolaPlayManager.setCoverPlayPauseAnim(playCoverHolder);
            }
        }
    }

    /**
     * 请求数据信息
     */
    private void requestKaoLaInfo() {
        KaolaPlayManager.SingletonHolder.INSTANCE.playBroadcast(id, null);
    }

    @Override
    public void onMusicChange(String name) {
        if (tv_bottom_title != null) {
            tv_bottom_title.setText(name);
        }
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void onMusicResume() {

    }

    @Override
    public void onPlayerFailed(PlayItem item) {
        if (tv_bottom_title != null) {
            tv_bottom_title.setText(item != null ? item.getTitle() : "");
        }

    }

    @Override
    public void onMusicPause() {
        refreshPlayStatusView();
    }

    @Override
    public void onMusicPlaying(PlayItem item) {
        if (tv_bottom_title != null && item != null) {
            tv_bottom_title.setText(item.getTitle());
            subtitle.setText(item.getAlbumName());
            btn_pause_play.setImageResource(R.drawable.pc_pause);
            GlideApp.with(this).load(item.getAlbumPic()).circleCrop().into(musicImageView);
            if ((item.getType() == PlayItemType.BROADCAST_LIVING)) {
                GlideApp.with(this).load(R.drawable.tag_living).into(tag);
            } else {
                GlideApp.with(this).load(R.drawable.tag_not_living).into(tag);
            }
        }
        if (flag_FIRST_PLAY) {
            SitechDevLog.e(TAG, "onPlayerPlaying  flag_FIRST_PLAY = " + flag_FIRST_PLAY);
            flag_FIRST_PLAY = false;
        }
        mCurPosition = BroadcastRadioListManager.getInstance().getCurPosition();
        addPlayListToFullList(BroadcastRadioListManager.getInstance().getPlayList());
        cancelProgressDialog();

        refreshPlayStatusView();
    }

    @Override
    public void onMusicPlayEnd(PlayItem item) {
        int curPosition = BroadcastRadioListManager.getInstance().getCurPosition();
        if (mCurPosition != curPosition) {
            mCurPosition = curPosition;
            SitechDevLog.e(TAG, "onMusicPlayEnd  mCurPosition ====" + mCurPosition);
            if (playListAdapter != null) {
                playListAdapter.setSelected(mCurPosition);
            }
        }
        SitechDevLog.e(TAG, "===== onPlayerEnd ======== curPosition  ---- " + curPosition + " mCurPosition ----" + mCurPosition);
    }

    @Override
    public void onMusicPlayProgress(String url, int position, int duration, boolean isPreDownloadComplete) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(TeddyEvent event) {
        if (event.getEventKey().equals(TeddyEvent.EVENT_TEDDY_KAOLA_PLAY_UPDATElIST)) {
            int curPosition = BroadcastRadioListManager.getInstance().getCurPosition();
            if (mCurPosition != curPosition) {
//                mCurPosition = curPosition;
                SitechDevLog.e(TAG, "onEvent EVENT_TEDDY_KAOLA_PLAY_UPDATElIST mCurPosition ====" + mCurPosition);
                if (playListAdapter != null) {
                    playListAdapter.setSelected(curPosition);
                }
            }
        } else if (event.getEventKey().equals(TeddyEvent.EVENT_TEDDY_AUDIO_STOP)) {
            finish();
        }
    }

    private void getProgamList() {

    }
}