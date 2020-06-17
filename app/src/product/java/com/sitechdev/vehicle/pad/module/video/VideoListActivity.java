package com.sitechdev.vehicle.pad.module.video;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.BaseActivity;
import com.sitechdev.vehicle.pad.module.video.service.VideoInfo;
import com.sitechdev.vehicle.pad.router.RouterConstants;
import com.sitechdev.vehicle.pad.util.MediaScanister;
import com.sitechdev.vehicle.pad.view.CommonToast;
import com.sitechdev.vehicle.pad.view.VideoItemDecoration;

/**
 * <pre>
 *      author : zyf
 *      time   : 2020/6/11
 * </pre>
 */
@Route(path = RouterConstants.VIDEO_LIST)
public class VideoListActivity extends BaseActivity {
    private TwinklingRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private VideoListAdapter adapter;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_video_list;
    }

    @Override
    protected void initData() {
        adapter = new VideoListAdapter(this);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClick(new VideoListAdapter.OnItemClick() {
            @Override
            public void onClick(VideoInfo info) {
                String vPag = "com.sitechdev.vehicle.video";
                if (!checkApkExist(VideoListActivity.this, vPag)) {
                    CommonToast.makeText(VideoListActivity.this, "暂时无法播放视频");
                    return;
                }
                ComponentName com = new ComponentName(vPag, "com.sitechdev.vehicle.video.app.VideoPlayMainActivity"); //package;class
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setComponent(com);
                intent.putExtra("localPath", info.data);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    public static boolean checkApkExist(Context context, String packageName){
        if (TextUtils.isEmpty(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager()
                    .getApplicationInfo(packageName,
                            PackageManager.GET_UNINSTALLED_PACKAGES);

            return true;
        } catch (PackageManager.NameNotFoundException e) {

            return false;
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        TextView tv_title = findViewById(R.id.tv_sub_title);
        refreshLayout = findViewById(R.id.refresh_layout);
        refreshLayout.setEnableLoadmore(false);
        recyclerView = findViewById(R.id.recyc_list);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);

        layoutManager.setOrientation(isLandscape() ? GridLayoutManager.HORIZONTAL : GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new VideoItemDecoration());
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                refresh();
            }
        });
        tv_title.setText("我的视频");
    }

    private void refresh() {
        if (!MediaScanister.getInstance().isScaing()) {
            MediaScanister.getInstance().scan(this,
                    Environment.getExternalStorageDirectory().getAbsolutePath(),
                    "video/*",
                    new MediaScanister.OnScanCompleteListener() {
                        @Override
                        public void onScanComplete() {
                            refreshLayout.post(new Runnable() {
                                @Override
                                public void run() {
                                    refreshLayout.finishRefreshing();
                                    adapter.refreshData();
                                }
                            });
                        }
                    });
        } else {
            refreshLayout.finishRefreshing();
        }
    }

}
