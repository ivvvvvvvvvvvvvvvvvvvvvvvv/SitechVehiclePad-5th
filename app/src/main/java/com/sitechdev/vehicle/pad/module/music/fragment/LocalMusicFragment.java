package com.sitechdev.vehicle.pad.module.music.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.sitechdev.vehicle.lib.util.DensityUtils;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.module.music.MusicManager;
import com.sitechdev.vehicle.pad.module.music.adapter.LocalMusicAdapter;
import com.sitechdev.vehicle.pad.util.MediaScanister;
import com.sitechdev.vehicle.pad.view.RecycleViewDivider;

/**
 * 本地音乐
 */
public class LocalMusicFragment extends Fragment implements
        LocalMusicAdapter.OnCheckEmptyListener, View.OnClickListener {

    private Context context;

    private RecyclerView vLocalMusicList;

    private TwinklingRefreshLayout refreshLayout;

    private View emptyView;

    private LocalMusicAdapter adapter;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_local_music,
                container, false);
        initView(root);
        initData();
        return root;
    }

    private void initView(View root) {
        emptyView = root.findViewById(R.id.local_music_empty_view);
        vLocalMusicList = root.findViewById(R.id.local_music_list);
        refreshLayout = root.findViewById(R.id.local_music_refresh_layout);
        refreshLayout.setEnableRefresh(true);
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                refresh();
            }
        });
        root.findViewById(R.id.local_music_scan).setOnClickListener(this);
        vLocalMusicList.setLayoutManager(new LinearLayoutManager(context));
        vLocalMusicList.setItemAnimator(new DefaultItemAnimator());
        adapter = new LocalMusicAdapter(context);
        adapter.setOnCheckEmptyListener(this);
        vLocalMusicList.setAdapter(adapter);
        MusicManager.getInstance().addMusicListUpdateListener(adapter);
        MusicManager.getInstance().addMusicChangeListener(adapter);
        vLocalMusicList.addItemDecoration(new RecycleViewDivider(context,
                adapter, DensityUtils.dp2px(1),
                context.getResources().getColor(R.color.white_5),
                context.getResources().getColor(R.color.white_21)));
    }

    @Override
    public void onDestroy() {
        MusicManager.getInstance().removeMusicChangeListener(adapter);
        MusicManager.getInstance().removeMusicListUpdateListener(adapter);
        super.onDestroy();
    }

    private void initData() {
    }

    private void refresh(){
        if (!MediaScanister.getInstance().isScaing()){
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
        }else {
            refreshLayout.finishRefreshing();
        }
    }

    @Override
    public void onCheckEmpty(boolean isEmpty) {
        if (isEmpty){
            refreshLayout.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }else {
            emptyView.setVisibility(View.GONE);
            refreshLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.local_music_scan:
                refresh();
                break;
        }
    }
}
