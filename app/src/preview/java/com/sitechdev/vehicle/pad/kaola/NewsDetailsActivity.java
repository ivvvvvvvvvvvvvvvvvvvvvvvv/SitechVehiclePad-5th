package com.sitechdev.vehicle.pad.kaola;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.kaolafm.opensdk.api.operation.model.ImageFile;
import com.kaolafm.opensdk.api.operation.model.column.ColumnMember;
import com.kaolafm.opensdk.api.operation.model.column.RadioDetailColumnMember;
import com.kaolafm.sdk.core.mediaplayer.IPlayerListChangedListener;
import com.kaolafm.sdk.core.mediaplayer.IPlayerStateListener;
import com.kaolafm.sdk.core.mediaplayer.OnPlayItemInfoListener;
import com.kaolafm.sdk.core.mediaplayer.PlayItem;
import com.kaolafm.sdk.core.mediaplayer.PlayerListManager;
import com.kaolafm.sdk.core.mediaplayer.PlayerManager;
import com.kaolafm.sdk.core.mediaplayer.PlayerRadioListManager;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.sitechdev.vehicle.lib.util.Constant;
import com.sitechdev.vehicle.lib.util.CustomPopWindow;
import com.sitechdev.vehicle.lib.util.DensityUtils;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.BaseActivity;
import com.sitechdev.vehicle.pad.manager.VoiceSourceManager;
import com.sitechdev.vehicle.pad.manager.VoiceSourceType;
import com.sitechdev.vehicle.pad.router.RouterConstants;
import com.sitechdev.vehicle.pad.view.CommonToast;
import com.sitechdev.vehicle.pad.view.RecycleViewDivider;
import com.sitechdev.vehicle.pad.view.RecycleViewKLDivider;
import com.sitechdev.vehicle.pad.view.ScrollTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.kaolafm.opensdk.api.operation.model.ImageFile.KEY_COVER;
import static com.sitechdev.vehicle.pad.BuildConfig.DEBUG;

//@Route(path = RouterConstants.MUSIC_PLAY_SHOW)
@VoiceSourceType(VoiceSourceManager.SUPPORT_TYPE_KAOLA)
public class NewsDetailsActivity extends BaseActivity implements
        VoiceSourceManager.MusicChangeListener{

    public static KaolaPlayManager.PlayType mCurrentType = null;
    public static String title = "";

    private Constant.TYPE TYPE_CODE;
    private ColumnMember mColumnMember;
    RadioDetailColumnMember mRadioDetailColumnMember;
    private ImageView btn_pre;
    private ImageView btn_pause_play;
    private ImageView btn_next;
    private ImageView btn_pop_list;
    private ScrollTextView tv_bottom_title;
    private TextView tv_title;

    private ImageView iv_back;
    private TwinklingRefreshLayout trf_detail_playlist;
    private RecyclerView rv_item_list;

    private int mCurPosition = 0;

    private IPlayerListChangedListener mPlayerListChangedListener;

    private Context mContext;

    private boolean flag_FIRST_PLAY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flag_FIRST_PLAY = true;
        mContext = this;
        mCurrentType = KaolaPlayManager.SingletonHolder.INSTANCE.mPlayType;
        VoiceSourceManager.getInstance().addMusicChangeListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_news_details;
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        TYPE_CODE = (Constant.TYPE) intent.getSerializableExtra(Constant.KEY_TYPE_KEY);
        if (TYPE_CODE == Constant.TYPE.FIRST_ENTERED) {
            SitechDevLog.e(NewsDetailsActivity.class.getSimpleName(), "======== FIRST_ENTERED ");
            mColumnMember = (ColumnMember) intent.getSerializableExtra(Constant.KEY_MEMBER_CODE);
            ColumnMemberMamager.SingltonHolder.INSTANCE.mColumnMember = mColumnMember;
            mRadioDetailColumnMember = (RadioDetailColumnMember) mColumnMember;
            requestKaoLaInfo();
        } else {
            mColumnMember = ColumnMemberMamager.SingltonHolder.INSTANCE.mColumnMember;
            PlayItem curPlayItem = PlayerListManager.getInstance().getCurPlayItem();
            mCurPosition = PlayerListManager.getInstance().getCurPosition();
            SitechDevLog.e(NewsDetailsActivity.class.getSimpleName(), "========== PLAYING   mCurPosition " + mCurPosition);
            if (tv_bottom_title != null && curPlayItem != null) {
                tv_bottom_title.setText(curPlayItem.getTitle());
            }
            setListData();
        }
        title = ColumnMemberMamager.SingltonHolder.INSTANCE.mColumnMember.getTitle();
        tv_title.setText(title);
        PlayerManager.getInstance(this).addPlayerStateListener(mPlayerStateListener);
    }

    private void setListData() {

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_item_list.setLayoutManager(manager);

        ArrayList<PlayItem> playList = PlayerListManager.getInstance().getPlayList();
        SitechDevLog.d(NewsDetailsActivity.class.getSimpleName(), "-----------setListData------------");
        List<PlayItemAdapter.Item> datas = new ArrayList<>();
        transformDataToItem(playList, datas);

        mAdapter = new PlayItemAdapter(this);
        mAdapter.setData(datas);
        mAdapter.setSelectedShow(mCurPosition);

        rv_item_list.setAdapter(mAdapter);
        rv_item_list.addItemDecoration(new RecycleViewKLDivider(this,
                mAdapter, DensityUtils.dp2px(1),
                getResources().getColor(R.color.white_5),
                getResources().getColor(R.color.white_21)));

        mAdapter.notifyDataSetChanged();

        mAdapter.setOnItemClickListener(new PlayItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(long playItemID, int position) {
                SitechDevLog.e(NewsDetailsActivity.class.getSimpleName(), "position ====" + position);
                mCurPosition = position;
                mAdapter.setSelected(position);
                PlayerManager.getInstance(mContext).playAudioFromPlayList(playItemID);
            }
        });
        cancelProgressDialog();
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        iv_back = findViewById(R.id.iv_back);
        btn_pre = findViewById(R.id.btn_pre);
        btn_pause_play = findViewById(R.id.btn_pause_play);
        btn_next = findViewById(R.id.btn_next);
        btn_pop_list = findViewById(R.id.btn_pop_list);
        btn_pop_list.setVisibility(View.INVISIBLE);

        tv_bottom_title = findViewById(R.id.tv_bottom_title);

        tv_title = findViewById(R.id.tv_title);

        rv_item_list = findViewById(R.id.rv_item_list);
        trf_detail_playlist = findViewById(R.id.trf_detail_playlist);
        trf_detail_playlist.setEnableLoadmore(true);
        trf_detail_playlist.setEnableRefresh(false);

        trf_detail_playlist.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                SitechDevLog.e(NewsDetailsActivity.class.getSimpleName(), "refreshLayout ============");
                fetchMore(true);
            }
        });
        showProgressDialog();
    }

    @Override
    protected void initListener() {
        super.initListener();
        iv_back.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        btn_pause_play.setOnClickListener(this);
        btn_pre.setOnClickListener(this);
        btn_pop_list.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_pre:
                playPre();
                break;
            case R.id.btn_pause_play:
                switchPlayPause();
                break;
            case R.id.btn_next:
                playNext();
                break;
            case R.id.btn_pop_list:
                break;
        }
    }

    private void playPre() {

        SitechDevLog.e(this.getClass().getSimpleName(), "========  playPre  was called");
        if (PlayerManager.getInstance(this).hasPre()) {
            PlayerManager.getInstance(this).playPre();
            if (mAdapter != null) {
                mCurPosition--;
                mAdapter.setSelected(mCurPosition);
            }
        } else {
            CommonToast.makeText(mContext, "已经是第一首啦~~~");
        }
    }

    private void playNext() {
        SitechDevLog.e(this.getClass().getSimpleName(), "========  playNext  was called");
        if (PlayerManager.getInstance(this).hasNext()) {
            PlayerManager.getInstance(this).playNext();
            if (mAdapter != null) {
                mCurPosition++;
                mAdapter.setSelected(mCurPosition);
            }
        } else {
            CommonToast.makeText(mContext, "已经是最后一首啦~~~");
        }
    }

    private PlayItemAdapter mAdapter;

    private void fetchMore(boolean isLoadMore) {
        //播单数据变化监听
        mPlayerListChangedListener = dataList -> {
            SitechDevLog.e(NewsDetailsActivity.class.getSimpleName(), "ai data changed ");
            List<PlayItemAdapter.Item> datas = new ArrayList<>();
            transformDataToItem(dataList, datas);

            mAdapter.setData(datas);
            mAdapter.setSelectedShow(mCurPosition);
            trf_detail_playlist.finishLoadmore();
        };
        PlayerListManager.getInstance().registerPlayerListChangedListener(mPlayerListChangedListener);

        //获取播放器中的播单用来显示
        if (!isLoadMore) {
            ArrayList<PlayItem> playList = PlayerListManager.getInstance().getPlayList();
            //有可能为空，因为数据有可能还没请求过来。
            if (playList == null || playList.isEmpty()) {
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

    private void switchPlayPause() {
        SitechDevLog.e(this.getClass().getSimpleName(), "========  switchPlayPause  was called");
        if (PlayerManager.getInstance(this).isPlaying()) {
            PlayerManager.getInstance(this).pause();
        } else {
            PlayerManager.getInstance(this).play();
        }

    }

    private void requestKaoLaInfo() {
        if (mColumnMember != null)
            SitechDevLog.e(NewsDetailsActivity.class.getSimpleName(), mColumnMember.toString());

        PlayerManager.getInstance(this).playPgc(mRadioDetailColumnMember.getRadioId());
    }

    IPlayerStateListener mPlayerStateListener = new IPlayerStateListener() {
        @Override
        public void onIdle(PlayItem playItem) {
            SitechDevLog.e(NewsDetailsActivity.class.getSimpleName(), "======= onIdle ======");
        }

        @Override
        public void onPlayerPreparing(PlayItem playItem) {
            SitechDevLog.e(NewsDetailsActivity.class.getSimpleName(), "======= onPlayerPreparing ======");
        }

        @Override
        public void onPlayerPlaying(PlayItem playItem) {
            if (tv_bottom_title != null && playItem != null) {
                tv_bottom_title.setText(playItem.getTitle());
                btn_pause_play.setActivated(true);
            }
            setListData();
            if (flag_FIRST_PLAY) {
                SitechDevLog.e(NewsDetailsActivity.class.getSimpleName(), "onPlayerPlaying  flag_FIRST_PLAY = " + flag_FIRST_PLAY);
                flag_FIRST_PLAY = false;
            }

            SitechDevLog.e(NewsDetailsActivity.class.getSimpleName(), "====== onPlayerPlaying ======= mCurPosition = " + mCurPosition);
        }

        @Override
        public void onPlayerPaused(PlayItem playItem) {
            SitechDevLog.e(NewsDetailsActivity.class.getSimpleName(), "======= onPlayerPaused ======");
        }

        @Override
        public void onProgress(String s, int i, int i1, boolean b) {
            SitechDevLog.e(NewsDetailsActivity.class.getSimpleName(), "======= onProgress ======");
        }

        @Override
        public void onPlayerFailed(PlayItem playItem, int i, int i1) {
            SitechDevLog.e(NewsDetailsActivity.class.getSimpleName(), "====== onPlayerFailed =======");
        }

        @Override
        public void onPlayerEnd(PlayItem playItem) {
            int curPosition = PlayerListManager.getInstance().getCurPosition();
            if (mCurPosition != curPosition) {
                mCurPosition = curPosition;
                mAdapter.setSelected(mCurPosition);
            }
            SitechDevLog.e(NewsDetailsActivity.class.getSimpleName(), "===== onPlayerEnd ======== curPosition  ---- " + curPosition + " mCurPosition ----" + mCurPosition);
        }

        @Override
        public void onSeekStart(String s) {
            SitechDevLog.e(NewsDetailsActivity.class.getSimpleName(), "======= onSeekStart ======");
        }

        @Override
        public void onSeekComplete(String s) {
            SitechDevLog.e(NewsDetailsActivity.class.getSimpleName(), "======= onSeekComplete ======");
        }

        @Override
        public void onBufferingStart(PlayItem playItem) {
            SitechDevLog.e(NewsDetailsActivity.class.getSimpleName(), "======= onBufferingStart ======");
        }

        @Override
        public void onBufferingEnd(PlayItem playItem) {
            SitechDevLog.e(NewsDetailsActivity.class.getSimpleName(), "======= onBufferingEnd ======");
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SitechDevLog.e(NewsDetailsActivity.class.getSimpleName(), "onDestroy  flag_FIRST_PLAY = " + flag_FIRST_PLAY);
        flag_FIRST_PLAY = true;
    }

    @Override
    public void onMusicChange(String name) {
        tv_bottom_title.setText(name);
    }

    @Override
    public void pause() {
        btn_pause_play.setActivated(false);
    }

    @Override
    public void resume() {
        btn_pause_play.setActivated(true);
    }
}
