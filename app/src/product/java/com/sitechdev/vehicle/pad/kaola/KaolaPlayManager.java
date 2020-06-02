package com.sitechdev.vehicle.pad.kaola;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import com.kaolafm.opensdk.OpenSDK;
import com.kaolafm.opensdk.ResType;
import com.kaolafm.opensdk.api.operation.OperationRequest;
import com.kaolafm.opensdk.api.operation.model.category.Category;
import com.kaolafm.opensdk.api.operation.model.column.Column;
import com.kaolafm.opensdk.api.operation.model.column.ColumnGrp;
import com.kaolafm.opensdk.http.core.HttpCallback;
import com.kaolafm.opensdk.http.error.ApiException;
import com.kaolafm.sdk.core.mediaplayer.BroadcastRadioListManager;
import com.kaolafm.sdk.core.mediaplayer.BroadcastRadioPlayerManager;
import com.kaolafm.sdk.core.mediaplayer.IPlayerStateListener;
import com.kaolafm.sdk.core.mediaplayer.PlayItem;
import com.kaolafm.sdk.core.mediaplayer.PlayerListManager;
import com.kaolafm.sdk.core.mediaplayer.PlayerManager;
import com.kaolafm.sdk.vehicle.GeneralCallback;
import com.sitechdev.vehicle.lib.event.EventBusUtils;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.pad.app.AppApplication;
import com.sitechdev.vehicle.pad.event.AppEvent;
import com.sitechdev.vehicle.pad.event.TeddyEvent;
import com.sitechdev.vehicle.pad.manager.VoiceSourceManager;
import com.sitechdev.vehicle.pad.module.main.MainActivity;
import com.sitechdev.vehicle.pad.router.RouterConstants;
import com.sitechdev.vehicle.pad.router.RouterUtils;
import com.sitechdev.vehicle.pad.util.AppVariants;
import com.sitechdev.vehicle.pad.view.CommonToast;

import java.text.DecimalFormat;
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

    private boolean isLoadingData = false;

    public void getkaolaCategory(HttpCallback<List<Category>> callback) {
        new OperationRequest().getCategoryTree(ResType.TYPE_ALBUM, callback);
    }

    public void getkaolaBroadcast(HttpCallback<List<Category>> callback) {
        new OperationRequest().getCategoryTree(ResType.TYPE_BROADCAST, callback);
    }

    public void acquireKaolaData() {
        if (isLoadingData) {
            return;
        }
        SitechDevLog.e(MainActivity.class.getSimpleName(), "---------------");
        isLoadingData = true;
        new OperationRequest().getColumnTree(true, httpCallback);
    }

    HttpCallback httpCallback = new HttpCallback<List<ColumnGrp>>() {
        @Override
        public void onSuccess(List<ColumnGrp> columnGrps) {
            isLoadingData = false;
            if (columnGrps == null || columnGrps.isEmpty()) {
                SitechDevLog.i(this.getClass().getSimpleName(), "听伴数据未获取到 ========== ");
                return;
            }
            mColumns = (List<Column>) columnGrps.get(0).getChildColumns();
            SitechDevLog.i(this.getClass().getSimpleName(), "AI 电台分类大小 ========== " + columnGrps.size());
            if (mColumns != null) {
                for (int i = 0; i < callbacks.size(); i++) {
                    callbacks.get(i).onDataGot(mColumns);
                }
                if (mColumns.size() >= 1 && mColumns.get(0) != null) {
                    for (int i = 0; i < callbacks.size(); i++) {
                        callbacks.get(i).onSuccess(0, mColumns.get(0).getTitle());
                    }
                    SitechDevLog.e(KaolaPlayManager.class.getSimpleName(), " 新特速报 === " + mColumns.get(0).getTitle());
                }
                if (mColumns.size() >= 2 && mColumns.get(1) != null) {
                    for (int i = 0; i < callbacks.size(); i++) {
                        callbacks.get(i).onSuccess(1, mColumns.get(1).getTitle());
                    }
                    SitechDevLog.e(KaolaPlayManager.class.getSimpleName(), " 少儿读物 === " + mColumns.get(1).getTitle());
                }
                if (mColumns.size() >= 3 && mColumns.get(2) != null) {
                    for (int i = 0; i < callbacks.size(); i++) {
                        callbacks.get(i).onSuccess(2, mColumns.get(2).getTitle());
                    }
                    SitechDevLog.e(KaolaPlayManager.class.getSimpleName(), " 车海娱乐 === " + mColumns.get(2).getTitle());
                }
                if (mColumns.size() >= 4 && mColumns.get(3) != null) {
                    for (int i = 0; i < callbacks.size(); i++) {
                        callbacks.get(i).onSuccess(3, mColumns.get(3).getTitle());
                    }
                    SitechDevLog.e(KaolaPlayManager.class.getSimpleName(), " 生活一点通 === " + mColumns.get(3).getTitle());
                }

            }
            if (DEBUG)
                for (int i = 0; i < columnGrps.size(); i++) {
                    SitechDevLog.e(KaolaPlayManager.class.getSimpleName(), columnGrps.get(i).toString());
                }

        }

        @Override
        public void onError(ApiException e) {
            isLoadingData = false;
            SitechDevLog.e(this.getClass().getSimpleName(), e.getMessage());
//                CommonToast.makeText(AppApplication.getContext(), "错误信息 ----> 错误码：" + e.getCode() + " -----> 错误信息：" + e.getMessage());
        }
    };

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

    private boolean curPlayingBroadcast = false;

    public boolean isCurPlayingBroadcast() {
        return curPlayingBroadcast;
    }

    public List<PlayItem> getPlayList() {
        return isCurPlayingBroadcast() ? BroadcastRadioListManager.getInstance().getPlayList() : PlayerListManager.getInstance().getPlayList();
    }

    public boolean isPlaying(Context context){
        if (curPlayingBroadcast) {
            return BroadcastRadioPlayerManager.getInstance().isPlaying();
        } else {
            return PlayerManager.getInstance(context).isPlaying();
        }
    }
    public void switchPlayPause(Context context) {
        if (isCurPlayingBroadcast()) {
            if (BroadcastRadioPlayerManager.getInstance().isPlaying()) {
                BroadcastRadioPlayerManager.getInstance().pause();
            } else {
                BroadcastRadioPlayerManager.getInstance().play();
            }
        } else {
            if (PlayerManager.getInstance(context).isPlaying()) {
                PlayerManager.getInstance(context).pause();
            } else {
                PlayerManager.getInstance(context).play();
            }
        }
    }

    public void setCurPlayingBroadcast(boolean curPlayingBroadcast) {
        this.curPlayingBroadcast = curPlayingBroadcast;
    }

    public boolean playAnother() {
        return playAnother(isCurPlayingBroadcast());
    }

    public boolean playAnother(boolean isBrocast) {
        boolean result = false;
        SitechDevLog.e(this.getClass().getSimpleName(), "========  playAnother  was called");
        if (isBrocast) {
            result = false;
        } else {
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
                EventBusUtils.postEvent(new TeddyEvent(TeddyEvent.EVENT_TEDDY_KAOLA_PLAY_UPDATElIST));
                SitechDevLog.e(KaolaPlayManager.class.getSimpleName(), " ============== change another song =============");
                result = true;
            } else {
                SitechDevLog.e(KaolaPlayManager.class.getSimpleName(), " ============== can't change another song =============");
                result = false;
            }
        }
        return result;
    }

    public boolean playNext() {
        return playNext(isCurPlayingBroadcast());
    }

    public boolean playNext(boolean isbrocast) {
        SitechDevLog.e(this.getClass().getSimpleName(), "========  playNext  was called");
        boolean hasNext = false;
        int curPosition = -1;
        if (isbrocast) {
            hasNext = BroadcastRadioPlayerManager.getInstance().hasNext();
            if (hasNext) {
                BroadcastRadioPlayerManager.getInstance().playNext();
                curPosition = BroadcastRadioListManager.getInstance().getCurPosition();
            } else {
                CommonToast.makeText(AppApplication.getContext(), "已经是最后一首啦");
            }
        } else {
            hasNext = PlayerManager.getInstance(AppApplication.getContext()).hasNext();
            if (hasNext) {
                curPosition = PlayerListManager.getInstance().getCurPosition();
                PlayerManager.getInstance(AppApplication.getContext()).playNext();
            } else {
                CommonToast.makeText(AppApplication.getContext(), "已经是最后一首啦");
            }
        }
        if (hasNext) {
            EventBusUtils.postEvent(new TeddyEvent(TeddyEvent.EVENT_TEDDY_KAOLA_PLAY_UPDATElIST, ++curPosition));
        }
        return hasNext;
    }

    public boolean playPre() {
        return playPre(isCurPlayingBroadcast());
    }

    public boolean playPre(boolean isbrocast) {
        boolean hasPre = false;
        int curPosition = -1;
        if (isbrocast) {
            hasPre = BroadcastRadioPlayerManager.getInstance().hasPre();
            if (hasPre) {
                BroadcastRadioPlayerManager.getInstance().playPre();
            } else {
                CommonToast.makeText(AppApplication.getContext(), "已经是最后一首啦");
            }
        } else {
            SitechDevLog.e(this.getClass().getSimpleName(), "========  playPre  was called");
            hasPre = PlayerManager.getInstance(AppApplication.getContext()).hasPre();
            if (hasPre) {
                curPosition = PlayerListManager.getInstance().getCurPosition();
                PlayerManager.getInstance(AppApplication.getContext()).playPre();
//            CommonToast.makeText(AppApplication.getContext(), "上一曲");
            } else {
                CommonToast.makeText(AppApplication.getContext(), "已经是第一首啦");
            }
        }
        if (hasPre) {
            EventBusUtils.postEvent(new TeddyEvent(TeddyEvent.EVENT_TEDDY_KAOLA_PLAY_UPDATElIST, --curPosition));
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
            if (onPlaySourceMusicChangeListeners != null && onPlaySourceMusicChangeListeners.size() > 0) {
                for (int j = 0; j < onPlaySourceMusicChangeListeners.size(); j++) {
                    onPlaySourceMusicChangeListeners.get(j).onMusicPlaying(playItem);
                }
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
            if (onPlaySourceMusicChangeListeners != null && onPlaySourceMusicChangeListeners.size() > 0) {
                for (int j = 0; j < onPlaySourceMusicChangeListeners.size(); j++) {
                    onPlaySourceMusicChangeListeners.get(j).onMusicPlayProgress(s, i, i1, b);
                }
            }
        }

        @Override
        public void onPlayerFailed(PlayItem playItem, int i, int i1) {
            SitechDevLog.e(KaolaPlayManager.class.getSimpleName(), " ============== onPlayerFailed =============");
            if (onPlaySourceMusicChangeListeners != null && onPlaySourceMusicChangeListeners.size() > 0) {
                for (int j = 0; j < onPlaySourceMusicChangeListeners.size(); j++) {
                    onPlaySourceMusicChangeListeners.get(j).onPlayerFailed(playItem);
                }
            }
        }

        @Override
        public void onPlayerEnd(PlayItem playItem) {
            SitechDevLog.e(KaolaPlayManager.class.getSimpleName(), " ============== onPlayerEnd =============");
            if (onPlaySourceMusicChangeListeners != null && onPlaySourceMusicChangeListeners.size() > 0) {
                for (int j = 0; j < onPlaySourceMusicChangeListeners.size(); j++) {
                    onPlaySourceMusicChangeListeners.get(j).onMusicPlayEnd(playItem);
                }
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

    public interface Callback {
        void onSuccess(int index, String textContent);

        void onDataGot(List<Column> data);
    }

    List<Callback> callbacks = new ArrayList<>();

    public void setCallback(Callback callback) {
        callbacks.add(callback);
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

    private List<VoiceSourceManager.onPlaySourceMusicChangeListener> onPlaySourceMusicChangeListeners = new ArrayList<>();

    public void addPlayVoiceSourceManagerListener(VoiceSourceManager.onPlaySourceMusicChangeListener playSourceMusicChangeListener) {
        onPlaySourceMusicChangeListeners.add(playSourceMusicChangeListener);
    }

    public void clearPlayVoiceSourceManagerListener(VoiceSourceManager.onPlaySourceMusicChangeListener l) {
        onPlaySourceMusicChangeListeners.remove(l);
    }

    public static String getShowTimeString(int timeInMs) {
        DecimalFormat df = new DecimalFormat("00");
        long minute = 60 * 1000;// 1分钟
        int min = (int) (timeInMs / minute);
        int sec = (int) (timeInMs % minute) / 1000;
        return new StringBuffer().append(df.format(min)).append(":").append(df.format(sec)).toString();
    }

    public void playBroadcast(long id, GeneralCallback<Boolean> callback) {
        BroadcastRadioPlayerManager.getInstance().playBroadcast(id, callback);
        setCurPlayingBroadcast(true);
    }

    public void playAlbum(Context context, long id) {
        PlayerManager.getInstance(context).playAlbum(id);
        setCurPlayingBroadcast(false);
    }

    public void playPgc(Context context, long id) {
        PlayerManager.getInstance(context).playPgc(id);
        setCurPlayingBroadcast(false);
    }

}
