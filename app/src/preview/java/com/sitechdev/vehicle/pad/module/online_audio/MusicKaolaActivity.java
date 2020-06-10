package com.sitechdev.vehicle.pad.module.online_audio;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.kaolafm.opensdk.api.operation.model.column.AlbumDetailColumnMember;
import com.kaolafm.opensdk.api.operation.model.column.Column;
import com.kaolafm.opensdk.api.operation.model.column.ColumnMember;
import com.kaolafm.opensdk.api.operation.model.column.RadioDetailColumnMember;
import com.kaolafm.opensdk.utils.ListUtil;
import com.kaolafm.sdk.core.mediaplayer.IPlayerListChangedListener;
import com.kaolafm.sdk.core.mediaplayer.OnPlayItemInfoListener;
import com.kaolafm.sdk.core.mediaplayer.PlayItem;
import com.kaolafm.sdk.core.mediaplayer.PlayerListManager;
import com.kaolafm.sdk.core.mediaplayer.PlayerManager;
import com.kaolafm.sdk.core.mediaplayer.PlayerRadioListManager;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.sitechdev.vehicle.lib.event.BindEventBus;
import com.sitechdev.vehicle.lib.imageloader.GlideApp;
import com.sitechdev.vehicle.lib.util.Constant;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.lib.util.ThreadUtils;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.BaseActivity;
import com.sitechdev.vehicle.pad.event.AppEvent;
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

// 听伴专辑播放页
@Route(path = RouterConstants.MUSIC_PLAY_ONLINE)
@VoiceSourceType(VoiceSourceManager.SUPPORT_TYPE_KAOLA)
@BindEventBus
public class MusicKaolaActivity extends BaseActivity implements
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

    private ImageView musicImageView, btn_pre, btn_next, btn_pause_play;
    private TextView subtitle;
    private ScrollTextView tv_bottom_title;
    private SeekBar musicSeekBar;
    private TextView seekStartTime, seekEndTime;
    private int defaultImgResId = 0;
    private TextView tv_title;
    private boolean seekUpdateFlag = true;
    private FrameLayout playCoverHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flag_FIRST_PLAY = true;
        mContext = this;
        mCurrentType = KaolaPlayManager.SingletonHolder.INSTANCE.mPlayType;
        VoiceSourceManager.getInstance().addMusicChangeListener(this);
        KaolaPlayManager.SingletonHolder.INSTANCE.addPlayVoiceSourceManagerListener(this);
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
        tv_title = (TextView) findViewById(R.id.tv_sub_title);

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
        musicSeekBar = findViewById(R.id.music_seek_bar);
        seekStartTime = findViewById(R.id.start_time);
        seekEndTime = findViewById(R.id.end_time);

        //数据传入
        initPlayListView();

        showProgressDialog();

        initData(getIntent());

        PlayerListManager.getInstance().registerPlayerListChangedListener(mPlayerListChangedListener);
    }

    private void initData(Intent intent) {
        TYPE_CODE = (Constant.TYPE) intent.getSerializableExtra(Constant.KEY_TYPE_KEY);
        if (TYPE_CODE == Constant.TYPE.FIRST_ENTERED) {
            SitechDevLog.e(TAG, "======== FIRST_ENT1ERED ");
            id = intent.getLongExtra(Constant.KEY_MEMBER_CODE, -1);
            isAlbum = intent.getBooleanExtra(Constant.KEY_IS_ALBUM, false);
            imageUrl = intent.getStringExtra(Constant.KEY_IMG_URL);
            title = intent.getStringExtra(Constant.KEY_TITLE);
            mCurPosition = PlayerListManager.getInstance().getCurPosition();
            requestKaoLaInfo();
        } else {
            PlayItem curPlayItem = PlayerListManager.getInstance().getCurPlayItem();
            mCurPosition = PlayerListManager.getInstance().getCurPosition();
            SitechDevLog.e(TAG, "========== PLAYING   mCurPosition " + mCurPosition);
            if (tv_bottom_title != null && curPlayItem != null) {
                tv_bottom_title.setText(curPlayItem.getTitle());
            }
            setListData();
        }
        //默认图片索引
        if (TextUtils.isEmpty(imageUrl)) {
            imageUrl = KaolaPlayManager.SingletonHolder.INSTANCE.getCurPlayingAlbumCover();
        }
        if (TextUtils.isEmpty(title)) {
            title = KaolaPlayManager.SingletonHolder.INSTANCE.getCurPlayingAlbumTitle();
        }
        GlideApp.with(this).load(imageUrl).placeholder(R.drawable.default_audio_round).circleCrop().into(musicImageView);
        tv_title.setText(title);
    }

    private void initPlayListView() {
        refreshLayout = findViewById(R.id.music_kaola_refresh_layout);
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setEnableLoadmore(true);
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                SitechDevLog.e(TAG, "refreshLayout ============");
                fetchMore(true);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        vLocalMusicList = findViewById(R.id.music_kaola_list);
        vLocalMusicList.setLayoutManager(linearLayoutManager);
        vLocalMusicList.setItemAnimator(new DefaultItemAnimator());
        vLocalMusicList.setHasFixedSize(true);
        mCurPosition = PlayerListManager.getInstance().getCurPosition();
        if (playListAdapter == null) {
            playListAdapter = new MusicKaolaAdapter(MusicKaolaActivity.this);
            vLocalMusicList.setAdapter(playListAdapter);
            playListAdapter.setSelected(mCurPosition);
            playListAdapter.setOnItemClickListener(new MusicKaolaAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(PlayItemAdapter.Item item, int position) {
                    mCurPosition = position;
                    playListAdapter.setSelected(position);
                    PlayerManager.getInstance(mContext).playAudioFromPlayList(item.id);
                }
            });
        } else {
            playListAdapter.setSelected(mCurPosition);
        }
        musicSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                seekUpdateFlag = false;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                KaolaPlayManager.SingletonHolder.INSTANCE.seekTo(MusicKaolaActivity.this, seekBar.getProgress());
            }
        });
    }

    private void setListData() {
        ThreadUtils.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                ArrayList<PlayItem> playList = PlayerListManager.getInstance().getPlayList();
                if (playDataItemList == null) {
                    playDataItemList = new ArrayList<>();
                } else {
                    playDataItemList.clear();
                }
                //重组播放列表
                transformDataToItem(playList, playDataItemList);

                if (playListAdapter == null) {
                    playListAdapter = new MusicKaolaAdapter(MusicKaolaActivity.this);
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
        addPlayListToFullList(dataList);
        cancelProgressDialog();
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
        SitechDevLog.e(TAG, "========  playPre  was called");
        boolean hasPre = KaolaPlayManager.SingletonHolder.INSTANCE.playPre();
        if (hasPre) {
            move2Pre();
        }
    }

    private void move2Pre() {
        if (playListAdapter != null) {
            mCurPosition--;
            playListAdapter.setSelected(mCurPosition);
            vLocalMusicList.smoothScrollToPosition(mCurPosition);
        }
    }

    private void playNext() {
        SitechDevLog.e(TAG, "========  playNext  was called");
        boolean hasNext = KaolaPlayManager.SingletonHolder.INSTANCE.playNext();
        if (hasNext) {
            move2Next();
        }
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
            ArrayList<PlayItem> playList = PlayerListManager.getInstance().getPlayList();
            //有可能为空，因为数据有可能还没请求过来。
            if (!ListUtil.isEmpty(playList)) {
                CommonToast.makeText(mContext, "数据为空 ~~~ ~~~");
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
            playListAdapter.setData(playDataItemList);
            SitechDevLog.e(TAG, "addPlayListToFullList  mCurPosition ====" + mCurPosition);
            playListAdapter.setSelectedShow(mCurPosition);
        }
    }

    private void transformDataToItem(List<PlayItem> dataList, List<PlayItemAdapter.Item> datas) {
        for (int i = 0, size = dataList.size(); i < size; i++) {
            PlayItem item = dataList.get(i);
            PlayItemAdapter.Item sai = new PlayItemAdapter.Item();
            sai.id = item.getAudioId();
            sai.title = item.getTitle();
            sai.details = item.getAlbumName();
            datas.add(sai);
        }
    }

    /**
     * 切换暂停与继续播放
     */
    private void switchPlayPause() {
        SitechDevLog.e(TAG, "========  switchPlayPause  was called");
        if (PlayerManager.getInstance(this).isPlaying()) {
            PlayerManager.getInstance(this).pause();
        } else {
            PlayerManager.getInstance(this).play();
        }
    }

    /**
     * 刷新view
     */
    private void refreshPlayStatusView() {
        ThreadUtils.runOnUIThread(() -> {
            if (PlayerManager.getInstance(this).isPlaying()) {
                if (btn_pause_play != null) {
                    btn_pause_play.setImageResource(R.drawable.pc_pause);
                    KaolaPlayManager.setCoverPlayStartAnim(playCoverHolder);
                }
            } else {
                if (btn_pause_play != null) {
                    btn_pause_play.setImageResource(R.drawable.pc_play);
                    KaolaPlayManager.setCoverPlayPauseAnim(playCoverHolder);
                }
            }
        });
    }

    /**
     * 请求数据信息
     */
    private void requestKaoLaInfo() {
        if (isAlbum) {
            KaolaPlayManager.SingletonHolder.INSTANCE.playAlbum(this, id);
        } else {
            KaolaPlayManager.SingletonHolder.INSTANCE.playPgc(this, id);
        }
    }

    @Override
    public void onMusicChange(String name) {
        if (tv_bottom_title != null) {
            tv_bottom_title.setText(name);
        }
    }

    @Override
    public void pause() {
        refreshPlayStatusView();
        if (playListAdapter != null && playListAdapter.getItemCount() > 0) {
            ThreadUtils.runOnUIThread(() -> {
                playListAdapter.notifyDataSetChanged();
            });
        }
    }

    @Override
    public void resume() {
        refreshPlayStatusView();
        if (playListAdapter != null && playListAdapter.getItemCount() > 0) {
            ThreadUtils.runOnUIThread(() -> {
                playListAdapter.notifyDataSetChanged();
            });
        }
    }

    @Override
    public void onMusicPause() {

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
    public void onMusicPlaying(PlayItem item) {
        seekUpdateFlag = true;
        if (tv_bottom_title != null && item != null) {
            tv_bottom_title.setText(item.getTitle());
            btn_pause_play.setImageResource(R.drawable.pc_pause);
            GlideApp.with(this).load(item.getAlbumPic()).placeholder(R.drawable.default_audio_round).circleCrop().into(musicImageView);
        }
        if (flag_FIRST_PLAY) {
            SitechDevLog.e(TAG, "onPlayerPlaying  flag_FIRST_PLAY = " + flag_FIRST_PLAY);
            flag_FIRST_PLAY = false;
        }
        SitechDevLog.e(TAG, "====== onPlayerPlaying ======= mCurPosition = " + mCurPosition);
    }

    @Override
    public void onMusicPlayEnd(PlayItem item) {
        int curPosition = PlayerListManager.getInstance().getCurPosition();
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
        seekStartTime.setText(KaolaPlayManager.getShowTimeString(position));
        if (seekUpdateFlag) {
            musicSeekBar.setProgress(position);
        }
        if (duration != musicSeekBar.getMax()) {
            musicSeekBar.setMax(duration);
        }
        if (!KaolaPlayManager.getShowTimeString(duration).equals(seekEndTime.getText().toString())) {
            seekEndTime.setText(KaolaPlayManager.getShowTimeString(duration));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(TeddyEvent event) {
        if (event.getEventKey().equals(TeddyEvent.EVENT_TEDDY_KAOLA_PLAY_UPDATElIST)) {
            int curPosition = PlayerListManager.getInstance().getCurPosition();
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(AppEvent event) {
        if (event.getEventKey().equals(AppEvent.EVENT_APP_KAOLA_UPDATE)) {
            int page = (int) event.getEventValue();
            int subIndex = (int) event.getEventValue2();
            if (KaolaPlayManager.SingletonHolder.INSTANCE.getOriginData() != null) {
                playColumSource(KaolaPlayManager.SingletonHolder.INSTANCE.getOriginData(), page, subIndex);
            }
        }
    }

    private void playColumSource(List<Column> data, int parentIndex, int childIndex) {
        final int index = parentIndex;
        for (int i = 0; i < data.size(); i++) {
            if (i == index) {
                if (data.get(i) != null && data.get(i).getColumnMembers() != null && childIndex < data.get(i).getColumnMembers().size()) {
                    ColumnMember ready2playColumn = data.get(i).getColumnMembers().get(childIndex);
                    tv_title.setText(ready2playColumn.getTitle());
                    KaolaPlayManager.SingletonHolder.INSTANCE.setCurPlayingAlbumTitle(ready2playColumn.getTitle());
                    if (null != ready2playColumn && ready2playColumn instanceof RadioDetailColumnMember) {
                        PlayerListManager.getInstance().clearPlayList();
                        KaolaPlayManager.SingletonHolder.INSTANCE.playPgc(this, ((RadioDetailColumnMember) ready2playColumn).getRadioId());
                        KaolaPlayManager.SingletonHolder.INSTANCE.setCurPlayingAlbumTitle(ready2playColumn.getTitle());
                        KaolaPlayManager.SingletonHolder.INSTANCE.setCurPlayingAlbumCover(ready2playColumn.getImageFiles());
                    } else if (null != ready2playColumn && ready2playColumn instanceof AlbumDetailColumnMember) {
                        PlayerListManager.getInstance().clearPlayList();
                        KaolaPlayManager.SingletonHolder.INSTANCE.playAlbum(this, ((AlbumDetailColumnMember) ready2playColumn).getAlbumId());
                        KaolaPlayManager.SingletonHolder.INSTANCE.setCurPlayingAlbumTitle(ready2playColumn.getTitle());
                        KaolaPlayManager.SingletonHolder.INSTANCE.setCurPlayingAlbumCover(ready2playColumn.getImageFiles());
                    }
                    break;
                }
            }
        }
    }

}
