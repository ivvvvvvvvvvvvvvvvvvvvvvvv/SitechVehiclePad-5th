package com.sitechdev.vehicle.pad.module.music.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.sitechdev.vehicle.lib.event.BindEventBus;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.bean.BaseFragment;
import com.sitechdev.vehicle.pad.event.MusicControlEvent;
import com.sitechdev.vehicle.pad.event.SysEvent;
import com.sitechdev.vehicle.pad.manager.VoiceSourceManager;
import com.sitechdev.vehicle.pad.manager.VoiceSourceType;
import com.sitechdev.vehicle.pad.module.music.MusicConfig;
import com.sitechdev.vehicle.pad.module.music.MusicManager;
import com.sitechdev.vehicle.pad.module.music.MusicUtils;
import com.sitechdev.vehicle.pad.module.music.adapter.LocalMusicAdapter2;
import com.sitechdev.vehicle.pad.module.music.service.MusicInfo;
import com.sitechdev.vehicle.pad.util.MediaScanister;
import com.sitechdev.vehicle.pad.view.CustomPlaySeekBar;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 本地音乐
 */

@BindEventBus
@VoiceSourceType(VoiceSourceManager.SUPPORT_TYPE_LOCAL)
public class LocalMusicFragment extends BaseFragment implements
        LocalMusicAdapter2.OnCheckEmptyListener, View.OnClickListener,
        CustomPlaySeekBar.OnMusicInfoChangedListener {

    private Context context;

    private RecyclerView vLocalMusicList;

    private TwinklingRefreshLayout refreshLayout;
    private View listLayout;

    private View emptyView;

    private TextView musicName, singer;
    private ImageView musicIcon;
    private int mFirstVisiblePosition = 0;
    private int mLastVisiblePosition = 0;

    //    private LocalMusicAdapter adapter;
    private LocalMusicAdapter2 adapter;
    private CustomPlaySeekBar customPlaySeekBar;

    public LocalMusicFragment() {

    }

    public static LocalMusicFragment newInstance() {
        LocalMusicFragment fragment = new LocalMusicFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        MusicManager.getInstance().removeMusicChangeListener(adapter);
        MusicManager.getInstance().removeMusicListUpdateListener(adapter);
        MusicManager.getInstance().removeMusicChangeListener(customPlaySeekBar);
        MusicManager.getInstance().removeMusicListUpdateListener(customPlaySeekBar);
        MusicManager.getInstance().removeMusicPositionChangeListener(customPlaySeekBar);
        super.onDestroy();
    }

    @Override
    protected void initData() {
        super.initData();
        if (null != MusicConfig.getInstance().getCurrentMusicInfo()) {
            musicName.setText(MusicConfig.getInstance().getCurrentMusicInfo().musicName);
            singer.setText(MusicConfig.getInstance().getCurrentMusicInfo().artist);
            customPlaySeekBar.setProgress(MusicConfig.getInstance().getProgress());
            musicIcon.setImageBitmap(MusicUtils.getArtwork(getActivity(),
                    MusicConfig.getInstance().getCurrentMusicInfo().songId,
                    MusicConfig.getInstance().getCurrentMusicInfo().albumId, true));
        } else {
            musicName.setText("- - -");
            singer.setText("- -");
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_local_music;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        emptyView = mContentView.findViewById(R.id.local_music_empty_view);
        customPlaySeekBar = mContentView.findViewById(R.id.music_usb_play_ctr);
        customPlaySeekBar.setMusicSource(VoiceSourceManager.LOCAL_MUSIC);
        vLocalMusicList = mContentView.findViewById(R.id.local_music_list);
        refreshLayout = mContentView.findViewById(R.id.local_music_refresh_layout);
        musicName = mContentView.findViewById(R.id.music_usb_play_name);
        singer = mContentView.findViewById(R.id.music_usb_play_singer);
        musicIcon = mContentView.findViewById(R.id.music_usb_play_icon);
        listLayout = mContentView.findViewById(R.id.music_usb_play_list_layout);
        refreshLayout.setEnableRefresh(true);
        refreshLayout.setEnableLoadmore(false);
        MusicManager.getInstance().addMusicChangeListener(customPlaySeekBar);
        MusicManager.getInstance().addMusicListUpdateListener(customPlaySeekBar);
        MusicManager.getInstance().addMusicPositionChangeListener(customPlaySeekBar);
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                refresh();
            }
        });
        vLocalMusicList.setLayoutManager(new LinearLayoutManager(context));
        vLocalMusicList.setItemAnimator(new DefaultItemAnimator());
        adapter = new LocalMusicAdapter2(context);
        adapter.setOnCheckEmptyListener(this);
        adapter.setOnMusicItemSelectListener(new LocalMusicAdapter2.OnMusicItemSelectListener() {
            @Override
            public void onItemSelected(int position) {
                RecyclerView.LayoutManager layoutManager = vLocalMusicList.getLayoutManager();
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                    mFirstVisiblePosition = linearManager.findFirstVisibleItemPosition();
                    mLastVisiblePosition = linearManager.findLastVisibleItemPosition();
                }
                if (position < mFirstVisiblePosition || (mLastVisiblePosition != 0 && position > mLastVisiblePosition)) {
                    vLocalMusicList.scrollToPosition(position);
                }
            }
        });
        vLocalMusicList.setAdapter(adapter);
        MusicManager.getInstance().addMusicListUpdateListener(adapter);
        MusicManager.getInstance().addMusicChangeListener(adapter);
        customPlaySeekBar.setOnMusicInfoChangedListener(this);
    }

    private void refresh() {
        if (!MediaScanister.getInstance().isScaing()) {
            MediaScanister.getInstance().scan(context,
                    Environment.getExternalStorageDirectory().getAbsolutePath(),
                    "audio/*",
                    new MediaScanister.OnScanCompleteListener() {
                        @Override
                        public void onScanComplete() {
                            refreshLayout.post(new Runnable() {
                                @Override
                                public void run() {
                                    refreshLayout.finishRefreshing();
                                }
                            });
                        }
                    });
        } else {
            refreshLayout.finishRefreshing();
        }
    }

    @Override
    public void onCheckEmpty(boolean isEmpty) {
        if (isEmpty) {
            listLayout.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
            MusicConfig.getInstance().setCurrentMusicInfo(null);
            customPlaySeekBar.setEnabled(false);
            musicName.setText("- - -");
            singer.setText("- -");
            musicIcon.setImageResource(R.drawable.img_song_card);
        } else {
            emptyView.setVisibility(View.GONE);
            listLayout.setVisibility(View.VISIBLE);
            customPlaySeekBar.setEnabled(true);
        }
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onMusicInfoChanged(MusicInfo current, int status) {
        if (null != current) {
            MusicConfig.getInstance().setCurrentMusicInfo(current);
            musicName.setText(current.musicName);
            singer.setText(current.artist);
            Bitmap bitmap = MusicUtils.getArtwork(getActivity(), current.songId, current.albumId,
                    true);
            if (null != bitmap) {
                musicIcon.setImageBitmap(bitmap);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Object o) {
        if (o instanceof MusicControlEvent) {
            MusicControlEvent event = (MusicControlEvent) o;
            if (MusicControlEvent.EVENT_CONTROL_MUSIC_PLAY_IF_ON_TOP.equals(event.getKey())) {
                if((int)event.getBean() != MusicConfig.MUSIC_CURRENT_SHOWN_USB){
                    return;
                }
                if (null != adapter.musicInfos && adapter.musicInfos.size() > 0 && !MusicManager.getInstance().isLocalPlaying()) {
                    if (null != MusicConfig.getInstance().getCurrentMusicInfo()) {
                        MusicManager.getInstance().resume((code, s) -> {
                        });
                    } else {
                        MusicManager.getInstance().play(adapter.musicInfos.get(0).songId,
                                (code, s) -> {
                                });
                    }
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSysEvent(SysEvent event) {
        if (event.getEvent() == SysEvent.EB_SYS_USB_STATE) {
            boolean isMounted = (boolean) event.getObj();
            if (!isMounted) {
                onCheckEmpty(true);
            }
        }
    }
}
