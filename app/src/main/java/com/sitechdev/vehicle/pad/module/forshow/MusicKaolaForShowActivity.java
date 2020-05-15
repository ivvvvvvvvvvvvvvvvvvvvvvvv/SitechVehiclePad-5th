package com.sitechdev.vehicle.pad.module.forshow;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
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
import com.sitechdev.vehicle.lib.imageloader.GlideApp;
import com.sitechdev.vehicle.lib.util.Constant;
import com.sitechdev.vehicle.lib.util.DensityUtils;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.lib.util.ThreadUtils;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.BaseActivity;
import com.sitechdev.vehicle.pad.kaola.ColumnMemberMamager;
import com.sitechdev.vehicle.pad.kaola.KaolaPlayManager;
import com.sitechdev.vehicle.pad.kaola.PlayItemAdapter;
import com.sitechdev.vehicle.pad.manager.VoiceSourceManager;
import com.sitechdev.vehicle.pad.manager.VoiceSourceType;
import com.sitechdev.vehicle.pad.router.RouterConstants;
import com.sitechdev.vehicle.pad.util.FontUtil;
import com.sitechdev.vehicle.pad.view.CommonToast;
import com.sitechdev.vehicle.pad.view.RecycleViewDivider;
import com.sitechdev.vehicle.pad.view.ScrollTextView;

import java.util.ArrayList;
import java.util.List;

@Route(path = RouterConstants.MUSIC_PLAY_SHOW)
@VoiceSourceType(VoiceSourceManager.SUPPORT_TYPE_KAOLA)
public class MusicKaolaForShowActivity extends BaseActivity implements
        VoiceSourceManager.MusicChangeListener, VoiceSourceManager.onPlaySourceMusicChangeListener {
    private static final String TAG = "MusicKaolaForShowActivity";

    private Context mContext;
    //new
    private Constant.TYPE TYPE_CODE;
    private ColumnMember mColumnMember;
    private RadioDetailColumnMember mRadioDetailColumnMember;
    private int mCurPosition = 0;

    private boolean flag_FIRST_PLAY;
    private static KaolaPlayManager.PlayType mCurrentType = null;
    private List<PlayItemAdapter.Item> playDataItemList = new ArrayList<>();

    private RecyclerView vLocalMusicList;

    private TwinklingRefreshLayout refreshLayout;

    private IPlayerListChangedListener mPlayerListChangedListener;

    private MusicKaolaForShowAdapter playListAdapter;

    private ImageView musicImageView, btn_pre, btn_next, btn_pause_play;
    private TextView subtitle;
    private ScrollTextView tv_bottom_title;
    private int defaultImgResId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flag_FIRST_PLAY = true;
        mContext = this;
        mCurrentType = KaolaPlayManager.SingletonHolder.INSTANCE.mPlayType;
        VoiceSourceManager.getInstance().addMusicChangeListener(this);
        KaolaPlayManager.SingletonHolder.INSTANCE.setPlayVoiceSourceManagerListener(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_music_kaola_main_for_show;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        TextView tv_title = (TextView) findViewById(R.id.tv_sub_title);

        //左边播放条控制

        //主标题
        tv_bottom_title = findViewById(R.id.tv_bottom_title);
        //专辑图片
        musicImageView = findViewById(R.id.image);
        //上一首
        btn_pre = findViewById(R.id.btn_pre);
        //下一首
        btn_next = findViewById(R.id.btn_next);
        //播放，暂停
        btn_pause_play = findViewById(R.id.btn_pause_play);

        //数据传入
        initPlayListView();

        showProgressDialog();

        //NEW CODE========================
        Intent intent = getIntent();
        TYPE_CODE = (Constant.TYPE) intent.getSerializableExtra(Constant.KEY_TYPE_KEY);
        if (TYPE_CODE == Constant.TYPE.FIRST_ENTERED) {
            SitechDevLog.e(TAG, "======== FIRST_ENTERED ");
            mColumnMember = (ColumnMember) intent.getSerializableExtra(Constant.KEY_MEMBER_CODE);
            ColumnMemberMamager.SingltonHolder.INSTANCE.mColumnMember = mColumnMember;
            mRadioDetailColumnMember = (RadioDetailColumnMember) mColumnMember;
            requestKaoLaInfo();
        } else {
            mColumnMember = ColumnMemberMamager.SingltonHolder.INSTANCE.mColumnMember;
            PlayItem curPlayItem = PlayerListManager.getInstance().getCurPlayItem();
            mCurPosition = PlayerListManager.getInstance().getCurPosition();
            SitechDevLog.e(TAG, "========== PLAYING   mCurPosition " + mCurPosition);
            if (tv_bottom_title != null && curPlayItem != null) {
                tv_bottom_title.setText(curPlayItem.getTitle());
            }
            setListData();
        }
        //默认图片索引
        defaultImgResId = intent.getIntExtra(Constant.KEY_DEFAULT_IMG_RESID, 0);
        GlideApp.with(this).load(defaultImgResId).into(musicImageView);

        String title = ColumnMemberMamager.SingltonHolder.INSTANCE.mColumnMember.getTitle();
        tv_title.setText(title);
    }

    private void initPlayListView() {
        TextView playListTextView=findViewById(R.id.tip_playlist_title);
        playListTextView.setTypeface(FontUtil.getInstance().getMainFont_Min_i());

        refreshLayout = findViewById(R.id.music_kaola_refresh_layout);
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setEnableLoadmore(false);
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
        vLocalMusicList.addItemDecoration(new RecycleViewDivider(this,
                playListAdapter, DensityUtils.dp2px(20),
                getResources().getColor(R.color.white_5),
                getResources().getColor(R.color.white_21)));
    }

    private void setListData() {

        ArrayList<PlayItem> playList = PlayerListManager.getInstance().getPlayList();
        SitechDevLog.e(TAG, "-----------setListData------------");
        if (playDataItemList == null) {
            playDataItemList = new ArrayList<>();
        } else {
            playDataItemList.clear();
        }
        //重组播放列表
        transformDataToItem(playList, playDataItemList);

        if (playListAdapter == null) {
            playListAdapter = new MusicKaolaForShowAdapter(this);
            if (playDataItemList != null) {
                playListAdapter.refreshData(playDataItemList, mCurPosition);
            }
            vLocalMusicList.setAdapter(playListAdapter);
        }

        playListAdapter.setOnItemClickListener(new MusicKaolaForShowAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(long playItemID, int position) {
                SitechDevLog.e(TAG, "position ====" + position);
                mCurPosition = position;
                playListAdapter.setSelected(position);
                PlayerManager.getInstance(mContext).playAudioFromPlayList(playItemID);
            }
        });
        vLocalMusicList.smoothScrollToPosition(mCurPosition);
        cancelProgressDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        KaolaPlayManager.SingletonHolder.INSTANCE.clearPlayVoiceSourceManagerListener();
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
                break;
            default:
                break;
        }
    }

    private void playPre() {
        SitechDevLog.e(TAG, "========  playPre  was called");
        boolean hasPre = KaolaPlayManager.SingletonHolder.INSTANCE.playPre();
        if (hasPre && playListAdapter != null) {
            mCurPosition--;
            playListAdapter.setSelected(mCurPosition);
        }
    }

    private void playNext() {
        SitechDevLog.e(TAG, "========  playNext  was called");
        boolean hasNext = KaolaPlayManager.SingletonHolder.INSTANCE.playNext();
        if (hasNext && playListAdapter != null) {
            mCurPosition++;
            playListAdapter.setSelected(mCurPosition);
        }
    }


    private void fetchMore(boolean isLoadMore) {
        //播单数据变化监听
        mPlayerListChangedListener = dataList -> {
            SitechDevLog.e(TAG, "ai data changed ");
            List<PlayItemAdapter.Item> datas = new ArrayList<>();
            transformDataToItem(dataList, datas);

            playListAdapter.setData(datas);
            playListAdapter.setSelectedShow(mCurPosition);
        };
        PlayerListManager.getInstance().registerPlayerListChangedListener(mPlayerListChangedListener);

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

                }

                @Override
                public void onPlayItemUnavailable() {
                    CommonToast.makeText(mContext, "播放出错 ~~~ ~~~");
                }

                @Override
                public void onPlayItemReady(List<PlayItem> list) {

                }
            }, false, false);
        }
    }

    private void transformDataToItem(ArrayList<PlayItem> dataList, List<PlayItemAdapter.Item> datas) {
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
        refreshPlayStatusView();
    }

    /**
     * 刷新view
     */
    private void refreshPlayStatusView() {
        ThreadUtils.runOnUIThread(() -> {
            if (PlayerManager.getInstance(this).isPlaying()) {
                if (btn_pause_play != null) {
                    btn_pause_play.setImageResource(R.drawable.pc_pause);
                }
            } else {
                if (btn_pause_play != null) {
                    btn_pause_play.setImageResource(R.drawable.pc_play);
                }
            }
        });
    }

    /**
     * 请求数据信息
     */
    private void requestKaoLaInfo() {
        if (mColumnMember != null) {
            SitechDevLog.e(TAG, mColumnMember.toString());
        }

        PlayerManager.getInstance(this).playPgc(mRadioDetailColumnMember.getRadioId());
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
    }

    @Override
    public void resume() {
        refreshPlayStatusView();
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
        if (tv_bottom_title != null && item != null) {
            tv_bottom_title.setText(item.getTitle());
            btn_pause_play.setActivated(true);
//            GlideApp.with(this).load(item.getAlbumPic()).placeholder(R.drawable.img_song_card).into(musicImageView);
//            musicImageView.setImageResource();
        }
        setListData();
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
            playListAdapter.setSelected(mCurPosition);
        }
        SitechDevLog.e(TAG, "===== onPlayerEnd ======== curPosition  ---- " + curPosition + " mCurPosition ----" + mCurPosition);
    }

    @Override
    public void onMusicPlayProgress(String s, int i, int i1, boolean b) {

    }
}
