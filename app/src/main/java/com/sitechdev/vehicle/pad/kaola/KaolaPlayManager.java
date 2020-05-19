package com.sitechdev.vehicle.pad.kaola;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.kaolafm.opensdk.OpenSDK;
import com.kaolafm.opensdk.api.operation.OperationRequest;
import com.kaolafm.opensdk.api.operation.model.column.Column;
import com.kaolafm.opensdk.api.operation.model.column.ColumnGrp;
import com.kaolafm.opensdk.http.core.HttpCallback;
import com.kaolafm.opensdk.http.error.ApiException;
import com.kaolafm.sdk.core.mediaplayer.IPlayerStateListener;
import com.kaolafm.sdk.core.mediaplayer.PlayItem;
import com.kaolafm.sdk.core.mediaplayer.PlayerListManager;
import com.kaolafm.sdk.core.mediaplayer.PlayerManager;
import com.sitechdev.vehicle.lib.event.EventBusUtils;
import com.sitechdev.vehicle.lib.util.Constant;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.pad.app.AppApplication;
import com.sitechdev.vehicle.pad.event.AppEvent;
import com.sitechdev.vehicle.pad.event.MapEvent;
import com.sitechdev.vehicle.pad.manager.VoiceSourceManager;
import com.sitechdev.vehicle.pad.module.main.MainActivity;
import com.sitechdev.vehicle.pad.router.RouterConstants;
import com.sitechdev.vehicle.pad.router.RouterUtils;
import com.sitechdev.vehicle.pad.util.AppVariants;
import com.sitechdev.vehicle.pad.view.CommonToast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.sitechdev.vehicle.lib.util.BuildConfig.DEBUG;
import static com.sitechdev.vehicle.pad.kaola.KaolaPlayManager.PlayType.CAR_FUN;
import static com.sitechdev.vehicle.pad.kaola.KaolaPlayManager.PlayType.CHILD_PAPERS;
import static com.sitechdev.vehicle.pad.kaola.KaolaPlayManager.PlayType.LIFE_ALL;
import static com.sitechdev.vehicle.pad.kaola.KaolaPlayManager.PlayType.SITEV_NEWS;

/**
 * Description:
 *
 * @author Steve_qi
 * @date: 2019/8/22
 */
public class KaolaPlayManager {

    public List<Column> mColumns = new ArrayList<>();
    public Column mCurrentColumn;

    private KaolaPlayManager() {
    }


    public static class SingletonHolder {
        public static KaolaPlayManager INSTANCE = new KaolaPlayManager();
    }

    public volatile PlayType mPlayType = null;

    public enum PlayType {
        SITEV_NEWS, CHILD_PAPERS, CAR_FUN, LIFE_ALL
    }

    public void acquireKaolaData() {
        SitechDevLog.e(MainActivity.class.getSimpleName(), "---------------");
        new OperationRequest().getColumnList("0", true, new HttpCallback<List<ColumnGrp>>() {
            @Override
            public void onSuccess(List<ColumnGrp> columnGrps) {
                if (columnGrps == null || columnGrps.isEmpty()) {
                    SitechDevLog.i(this.getClass().getSimpleName(), "听伴数据未获取到 ========== ");
                    return;
                }
                mColumns = (List<Column>) columnGrps.get(0).getChildColumns();
                SitechDevLog.i(this.getClass().getSimpleName(), "AI 电台分类大小 ========== " + columnGrps.size());
                if (mColumns != null) {

                    if (mColumns.size() >= 1 && mColumns.get(0) != null) {
                        mCallback.onSuccess(0, mColumns.get(0).getTitle());
                        SitechDevLog.e(KaolaPlayManager.class.getSimpleName(), " 新特速报 === " + mColumns.get(0).getTitle());
                    }
                    if (mColumns.size() >= 2 && mColumns.get(1) != null) {
                        mCallback.onSuccess(1, mColumns.get(1).getTitle());
                        SitechDevLog.e(KaolaPlayManager.class.getSimpleName(), " 少儿读物 === " + mColumns.get(1).getTitle());
                    }
                    if (mColumns.size() >= 3 && mColumns.get(2) != null) {
                        mCallback.onSuccess(2, mColumns.get(2).getTitle());
                        SitechDevLog.e(KaolaPlayManager.class.getSimpleName(), " 车海娱乐 === " + mColumns.get(2).getTitle());
                    }
                    if (mColumns.size() >= 4 && mColumns.get(3) != null) {
                        mCallback.onSuccess(3, mColumns.get(3).getTitle());
                        SitechDevLog.e(KaolaPlayManager.class.getSimpleName(), " 生活一点通 === " + mColumns.get(3).getTitle());
                    }

                }
                if (DEBUG)
                    for (int i = 0; i < columnGrps.size(); i++) {
                        SitechDevLog.e(KaolaListActivity.class.getSimpleName(), columnGrps.get(i).toString());
                    }

            }

            @Override
            public void onError(ApiException e) {
                SitechDevLog.e(this.getClass().getSimpleName(), e.getMessage());
                CommonToast.makeText(AppApplication.getContext(), "错误信息 ----> 错误码：" + e.getCode() + " -----> 错误信息：" + e.getMessage());
            }
        });
    }

    /**
     * @param context
     * @param index   0, 新特速报 1,少儿读物 2,车海娱乐 3,生活一点通
     */
    public void toPlayListActivity(Context context, int index) {
        toPlayListActivity(context, index, -1);
    }

    public void toPlayListActivity(Context context, int index, int deepIndex) {

        if (null == mColumns || mColumns.size() == 0) {
//            CommonToast.makeText(context, "当前数据异常~~~");
            acquireKaolaData();
            return;
        }
        if (mColumns.size() <= index) {
            CommonToast.makeText(context, "当前数据异常~~~");
            return;
        }
        switch (index) {
            case 0:
                mPlayType = SITEV_NEWS;
                break;
            case 1:
                mPlayType = CHILD_PAPERS;
                break;
            case 2:
                mPlayType = CAR_FUN;
                break;
            case 3:
                mPlayType = LIFE_ALL;
                break;
        }
        mCurrentColumn = mColumns.get(index);
//        if (deepIndex == 3 && index == 3) {
//            Intent intent = new Intent(KaolaListActivity.mContext, KaolaListActivity.class);
//            KaolaListActivity.mContext.startActivity(intent);
//            return;
//        }
        if (isTopAct(context, KaolaListActivity.class)) {
            EventBusUtils.postEvent(new AppEvent(AppEvent.EVENT_APP_KAOLA_UPDATE, index, deepIndex));
        } else {
            RouterUtils.getInstance().getPostcard(RouterConstants.KAOLA_RADIO_LIST)
                    .withInt("pageIndex", index)
                    .withInt("deepIndex", deepIndex)
                    .navigation();
        }
    }

    private boolean isTopAct(Context mContext, Class act) {
        String topActivityName = "";
        ActivityManager am = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTasks = am
                .getRunningTasks(1);
        if (runningTasks != null && !runningTasks.isEmpty()) {
            ActivityManager.RunningTaskInfo taskInfo = runningTasks.get(0);
            topActivityName = taskInfo.topActivity.getClassName();
        }
        if (act.getName().equals(topActivityName)) {
            return true;
        }
        return false;
    }

    public boolean playAnother() {
        SitechDevLog.e(this.getClass().getSimpleName(), "========  playAnother  was called");
        if (PlayerManager.getInstance(AppApplication.getContext()).canSwitchNextOrPre()) {
            int size = PlayerListManager.getInstance().getPlayListSize();
            PlayItem curPlayItem = PlayerListManager.getInstance().getCurPlayItem();
            PlayItem nextPlayItem = null;
            while (true) {
                nextPlayItem = PlayerListManager.getInstance().getPlayList().get(new Random().nextInt(size));
                if (curPlayItem != nextPlayItem) {
                    break;
                }
            }
            PlayerManager.getInstance(AppApplication.getContext()).play(nextPlayItem);
            SitechDevLog.e(KaolaPlayManager.class.getSimpleName(), " ============== change another song =============");
            return true;
        } else {
            SitechDevLog.e(KaolaPlayManager.class.getSimpleName(), " ============== can't change another song =============");
            return false;
        }
    }

    public boolean playNext() {
        SitechDevLog.e(this.getClass().getSimpleName(), "========  playNext  was called");
        boolean hasNext = PlayerManager.getInstance(AppApplication.getContext()).hasNext();
        if (PlayerManager.getInstance(AppApplication.getContext()).hasNext()) {
            PlayerManager.getInstance(AppApplication.getContext()).playNext();
//            CommonToast.makeText(AppApplication.getContext(), "下一曲");
        } else {
            CommonToast.makeText(AppApplication.getContext(), "已经是最后一首啦");
        }
        return hasNext;
    }

    public boolean playPre() {
        SitechDevLog.e(this.getClass().getSimpleName(), "========  playPre  was called");
        boolean hasPre = PlayerManager.getInstance(AppApplication.getContext()).hasPre();
        if (PlayerManager.getInstance(AppApplication.getContext()).hasPre()) {
            PlayerManager.getInstance(AppApplication.getContext()).playPre();
//            CommonToast.makeText(AppApplication.getContext(), "上一曲");
        } else {
            CommonToast.makeText(AppApplication.getContext(), "已经是第一首啦");
        }
        return hasPre;
    }

    public IPlayerStateListener mIPlayerStateListener = new IPlayerStateListener() {
        @Override
        public void onIdle(PlayItem playItem) {
            SitechDevLog.e(KaolaPlayManager.class.getSimpleName(), " ============== onIdle =============");
        }

        @Override
        public void onPlayerPreparing(PlayItem playItem) {
            SitechDevLog.e(KaolaPlayManager.class.getSimpleName(), " ============== onPlayerPreparing =============");
            mPlayCallback.onPrepare(playItem);
        }

        @Override
        public void onPlayerPlaying(PlayItem playItem) {
            mPlayCallback.onPlay();
            SitechDevLog.e(KaolaPlayManager.class.getSimpleName(), " ============== onPlayerPlaying =============");
            if (onPlaySourceMusicChangeListener != null) {
                onPlaySourceMusicChangeListener.onMusicPlaying(playItem);
            }
        }

        @Override
        public void onPlayerPaused(PlayItem playItem) {
            if (mPlayCallback != null) {
                mPlayCallback.onPause();
            }
            SitechDevLog.e(KaolaPlayManager.class.getSimpleName(), " ============== onPlayerPaused =============");
        }

        @Override
        public void onProgress(String s, int i, int i1, boolean b) {
            SitechDevLog.e(KaolaPlayManager.class.getSimpleName(), " ============== onProgress =============");
            if (onPlaySourceMusicChangeListener != null) {
                onPlaySourceMusicChangeListener.onMusicPlayProgress(s, i, i1, b);
            }
        }

        @Override
        public void onPlayerFailed(PlayItem playItem, int i, int i1) {
            SitechDevLog.e(KaolaPlayManager.class.getSimpleName(), " ============== onPlayerFailed =============");
            if (onPlaySourceMusicChangeListener != null) {
                onPlaySourceMusicChangeListener.onPlayerFailed(playItem);
            }
        }

        @Override
        public void onPlayerEnd(PlayItem playItem) {
            SitechDevLog.e(KaolaPlayManager.class.getSimpleName(), " ============== onPlayerEnd =============");
            if (onPlaySourceMusicChangeListener != null) {
                onPlaySourceMusicChangeListener.onMusicPlayEnd(playItem);
            }
        }

        @Override
        public void onSeekStart(String s) {
            SitechDevLog.e(KaolaPlayManager.class.getSimpleName(), " ============== onSeekStart =============");
        }

        @Override
        public void onSeekComplete(String s) {
            SitechDevLog.e(KaolaPlayManager.class.getSimpleName(), " ============== onSeekComplete =============");
        }

        @Override
        public void onBufferingStart(PlayItem playItem) {
            SitechDevLog.e(KaolaPlayManager.class.getSimpleName(), " ============== onBufferingStart =============");
        }

        @Override
        public void onBufferingEnd(PlayItem playItem) {
            SitechDevLog.e(KaolaPlayManager.class.getSimpleName(), " ============== onBufferingEnd =============");
        }
    };

    public void activeKaola() {

        OpenSDK.getInstance().activate(new HttpCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean isSuccess) {
                AppVariants.activeSuccess = true;
                Log.i("考拉APPDemo", isSuccess ? "激活SDK成功" : "激活SDK失败");
            }

            @Override
            public void onError(ApiException exception) {
                AppVariants.activeSuccess = false;
                Log.w("考拉APPDemo", "激活SDK失败，错误码=" + exception.getCode() + ",错误信息=" + exception.getMessage());
            }
        });
    }

    private Callback mCallback;

    public interface Callback {
        void onSuccess(int index, String textContent);
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    private PlayCallback mPlayCallback;

    public interface PlayCallback {
        void onPrepare(PlayItem playItem);

        void onPlay();

        void onPause();
    }

    public void setPlayCallback(PlayCallback playCallback) {
        mPlayCallback = playCallback;
    }

    private VoiceSourceManager.onPlaySourceMusicChangeListener onPlaySourceMusicChangeListener = null;

    public void setPlayVoiceSourceManagerListener(VoiceSourceManager.onPlaySourceMusicChangeListener playSourceMusicChangeListener) {
        onPlaySourceMusicChangeListener = playSourceMusicChangeListener;
    }

    public void clearPlayVoiceSourceManagerListener() {
        onPlaySourceMusicChangeListener = null;
    }
}
