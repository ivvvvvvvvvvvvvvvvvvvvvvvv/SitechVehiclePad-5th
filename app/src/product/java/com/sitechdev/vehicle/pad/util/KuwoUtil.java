package com.sitechdev.vehicle.pad.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.ArrayMap;
import android.widget.ImageView;

import com.sitechdev.vehicle.lib.base.BaseApp;
import com.sitechdev.vehicle.lib.event.EventBusUtils;
import com.sitechdev.vehicle.lib.util.NetworkUtils;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.lib.util.ThreadUtils;
import com.sitechdev.vehicle.pad.callback.SitechMusicSource;
import com.sitechdev.vehicle.pad.event.KuwoEvent;
import com.sitechdev.vehicle.pad.manager.SitechMusicNewManager;

import java.util.List;

import cn.kuwo.autosdk.api.KWAPI;
import cn.kuwo.autosdk.api.OnGetSongImgListener;
import cn.kuwo.autosdk.api.OnSearchListener;
import cn.kuwo.autosdk.api.PlayMode;
import cn.kuwo.autosdk.api.PlayState;
import cn.kuwo.autosdk.api.PlayerStatus;
import cn.kuwo.base.bean.Music;

/**
 * 项目名称：HZ_SitechDOS
 * 类名称：KuwoUtil
 * 类描述：
 * 创建人：shaozhi
 * 创建时间：18-8-31 上午9:51
 * 修改时间：
 * 备注：
 */
public class KuwoUtil {
    private static KWAPI mKwapi = null;

    public static void init(Context context) {
        mKwapi = KWAPI.createKWAPI(context, "auto");
        //注册一些监听
        registerKuwoListener();
    }

    public static KWAPI getKuwoInstance(Context context) {
        if (mKwapi == null) {
            init(context);
        }
        return mKwapi;
    }

    /**
     * 启动酷我APP
     *
     * @param isAutoplay true==打开后自动播放
     */
    public static void startKuwoApp(boolean isAutoplay) {
        ThreadUtils.runOnUIThread(() -> {
            if (mKwapi==null){
                init(BaseApp.getApp().getApplicationContext());
            }
            boolean networkAvailable = NetworkUtils.isNetworkAvailable(BaseApp.getApp().getApplicationContext());
            mKwapi.startAPP(networkAvailable);
            mKwapi.bindAutoSdkService(BaseApp.getApp().getApplicationContext());
//            SitechMusicNewManager.getInstance().registerCurrentMusicChannel(SitechMusicSource.MusicChannel.CHANNEL_NET_MUSIC_KUWO);
        });
        //改变音源
//        EventBusUtils.postEvent(new TeddyEvent(TeddyEvent.EB_TEDDY_SCENE_EVENT_CHANGE, TeddyEvent.TEDDY_SCENE_KU_WO));
//        EventBusUtils.postEvent(new SysEvent(SysEvent.EB_SYS_SRC_SWITCTH, DataCenterConstants.CurrentSource.MODE_KUWO));
    }

    /**
     * 在线搜索歌曲并播放,通过歌曲名称
     *
     * @param musicName 歌曲名称
     */
    public static void playMusic(String musicName) {
        ThreadUtils.runOnUIThread(() -> {
            setKuwoLoading(true);
            mKwapi.playClientMusics(musicName, "", "");
        });
    }

    /**
     * 在线搜索歌曲并播放,通过歌曲名称
     *
     * @param musicName   歌曲名称
     * @param musicSinger 歌手
     */
    public static void playMusic(String musicName, String musicSinger) {
        ThreadUtils.runOnUIThread(() -> {
            mKwapi.bindAutoSdkService(BaseApp.getApp().getApplicationContext());
            setKuwoLoading(true);
            mKwapi.playClientMusics(musicName, musicSinger, "");
        });
    }

    /**
     * 在线搜索歌曲并播放,通过歌曲名称
     *
     * @param list   歌曲列表
     * @param index 播放索引
     * @param isEntry 是否打开酷我
     * @param isExit 若打开酷我时，是否3秒内退出酷我音乐界面
     */
    public static void playMusic(List<Music> list, int index, boolean isEntry, boolean isExit) {
        ThreadUtils.runOnUIThread(() -> {
            mKwapi.bindAutoSdkService(BaseApp.getApp().getApplicationContext());
            setKuwoLoading(true);
            mKwapi.playMusic(list,index, isEntry, isExit);
        });
    }

    /**
     * 在线搜索歌曲并播放,通过主题搜索
     *
     * @param theme   主题
     */
    public static void searchThemeMusic(String theme, OnSearchListener searchListener) {
        ThreadUtils.runOnUIThread(() -> {
            mKwapi.bindAutoSdkService(BaseApp.getApp().getApplicationContext());
//            setKuwoLoading(true);
            mKwapi.searchOnlineMusicByTheme(theme,searchListener);
        });
    }


    /**
     * 在线搜索歌曲
     *
     * @param musicName   歌曲名称
     * @param musicSinger 歌手
     * @param album 专辑
     * @param searchListener 回调
     */
    public static void searchOnlineMusic(String musicName, String musicSinger, String album, OnSearchListener searchListener) {
        ThreadUtils.runOnUIThread(() -> {
            mKwapi.bindAutoSdkService(BaseApp.getApp().getApplicationContext());
            mKwapi.searchOnlineMusic(musicSinger,musicName,album,searchListener);
        });
    }

    /**
     * 在线搜索歌曲并播放,通过歌曲名称
     *
     * @param musicName   歌曲名称
     * @param musicSinger 歌手
     * @param musicAlbum  专辑名
     */
    public static void playMusic(String musicName, String musicSinger, String musicAlbum) {
        ThreadUtils.runOnUIThread(() -> {
            setKuwoLoading(true);
            mKwapi.playClientMusics(musicName, musicSinger, musicAlbum);
        });
    }

    /**
     * 在线搜索歌曲并播放,通过歌词
     *
     * @param lrc 歌词
     */
    public static void playMusicByLrc(String lrc) {
        ThreadUtils.runOnUIThread(() -> {
            setKuwoLoading(true);
            mKwapi.playClientMusicsByLrc(lrc);
        });
    }

    /**
     * 播放控制
     */
    public static void playControl(KuwoEvent.PlayControl playControl) {
        EventBusUtils.postEvent(new KuwoEvent(KuwoEvent.EB_KUWO_PLAY_CONTROL, playControl));
        ThreadUtils.runOnUIThread(() -> {
            switch (playControl) {
                case PLAY_PLAY:
                    setKuwoLoading(true);
                    mKwapi.setPlayState(PlayState.STATE_PLAY);
                    break;
                case PLAY_PAUSE:
                    setKuwoLoading(false);
                    mKwapi.setPlayState(PlayState.STATE_PAUSE);
                    break;
                case PLAY_NEXT:
                    setKuwoLoading(true);
                    mKwapi.setPlayState(PlayState.STATE_NEXT);
                    break;
                case PLAY_PRE:
                    setKuwoLoading(true);
                    mKwapi.setPlayState(PlayState.STATE_PRE);
                    break;
                case PLAY_MODE_ALL_CIRCLE:
                    mKwapi.setPlayMode(PlayMode.MODE_ALL_CIRCLE);
                    break;
                case PLAY_MODE_ALL_ORDER:
                    mKwapi.setPlayMode(PlayMode.MODE_ALL_ORDER);
                    break;
                case PLAY_MODE_SINGLE:
                    mKwapi.setPlayMode(PlayMode.MODE_SINGLE_CIRCLE);
                    break;
                case PLAY_MODE_RANDOM:
                    mKwapi.setPlayMode(PlayMode.MODE_ALL_RANDOM);
                    break;
            }
        });
    }

    /**
     * 获取当前酷我音乐的播放状态
     *
     * @return true=正在播放
     */
    public static boolean getPlayStatus() {
        return mKwapi.getPlayerStatus() == PlayerStatus.PLAYING;
    }

    /**
     * 获取当前酷我音乐的正在播放的音乐信息
     *
     * @return musicInfo
     */
    public static Music getNowPlayingMusicInfo() {
        return mKwapi.getNowPlayingMusic();
    }

    /**
     * 进入酷我随机播放一首歌曲
     */
    public static void startRandomPlayMusic() {
        ThreadUtils.runOnUIThread(() -> {
            setKuwoLoading(true);
            mKwapi.randomPlayMusic();
        });
    }

    /**
     * 对ImageView设置当前播放的音乐图片
     *
     * @param music        当前播放的音乐
     * @param imageView    ImageView组件
     * @param defaultResID 占位图
     */
    public static void setMusicByImageView(Music music, ImageView imageView, int defaultResID) {
        mKwapi.displayImage(music, imageView, defaultResID);
    }

    /**
     * 获取当前播放的音乐的音乐小图。
     * 结果使用new KuwoEvent(KuwoEvent.EB_KUWO_MUSIC_BITMAP_RESULT, bitmap)事件传递。
     * 如果值为null，则代表获取该音乐小图片失败。
     *
     * @param music 当前播放的音乐
     */
    public static void pullMusicBitmap(Music music) {
        mKwapi.setMusicImg(music, new OnGetSongImgListener() {
            @Override
            public void sendSyncNotice_HeadPicStart(Music music) {
                //开始下载图片
            }

            @Override
            public void sendSyncNotice_HeadPicFinished(Music music, Bitmap bitmap) {
                //下载完成
                EventBusUtils.postEvent(new KuwoEvent(KuwoEvent.EB_KUWO_MUSIC_BITMAP_RESULT, bitmap));
            }

            @Override
            public void sendSyncNotice_HeadPicFailed(Music music) {
                //下载失败
                EventBusUtils.postEvent(new KuwoEvent(KuwoEvent.EB_KUWO_MUSIC_BITMAP_RESULT, null));
            }

            @Override
            public void sendSyncNotice_HeadPicNone(Music music) {
                //没有图片，无法下载
                EventBusUtils.postEvent(new KuwoEvent(KuwoEvent.EB_KUWO_MUSIC_BITMAP_RESULT, null));
            }
        });
    }

    /**
     * 获取当前播放的歌曲的时长
     */
    public static int getCurrentMusicDuration() {
        return mKwapi.getCurrentMusicDuration();
    }

    /**
     * 获取当前播放的歌曲的进度
     */
    public static int getCurrentMusicPos() {
        return mKwapi.getCurrentPos();
    }

    /**
     * 注册酷我信息监听
     */
    private static void registerKuwoListener() {
        //酷我进入
        mKwapi.registerEnterListener(BaseApp.getApp().getApplicationContext(),
                () -> {
                    SitechDevLog.e("KuwoUtils", "---------registerEnterListener----------");
//                    EventBusUtils.postEvent(new KuwoEvent(KuwoEvent.EB_KUWO_LANUCHER));
                    //解除注册
                    mKwapi.unRegisterEnterListener(BaseApp.getApp().getApplicationContext());
                });
        //酷我退出
        mKwapi.registerExitListener(BaseApp.getApp().getApplicationContext(),
                () -> {
                    SitechDevLog.e("KuwoUtils", "---------registerExitListener----------");
                    setKuwoLoading(false);
//                    EventBusUtils.postEvent(new KuwoEvent(KuwoEvent.EB_KUWO_EXIT));
                    //解除注册
                    mKwapi.unRegisterExitListener(BaseApp.getApp().getApplicationContext());
                });
        //播放状态的改变
        mKwapi.registerPlayerStatusListener(BaseApp.getApp().getApplicationContext(),
                (playerStatus, music) -> {

                    ArrayMap<String, Object> kuwoMap = new ArrayMap<>();
                    kuwoMap.put("music", music);
                    kuwoMap.put("playStatus", playerStatus);
//                    EventBusUtils.postEvent(new KuwoEvent(KuwoEvent.EB_KUWO_PLAY_STATUS_CHANNGE, kuwoMap));
                    switch (playerStatus) {
                        case INIT:
                        case BUFFERING:
                            setKuwoLoading(true);
                            break;
                        case PLAYING:
                        case STOP:
                        case PAUSE:
                            setKuwoLoading(false);
                            break;
                    }
                });

        mKwapi.registerPlayEndListener(BaseApp.getApp().getApplicationContext(), playEndType -> {
            switch (playEndType) {
                case END_COMPLETE:
                    setKuwoLoading(true);
                    break;
               /* case END_USER:
                    //若是酷我界面，则需要通过USER_END来更改状态：正在加载中。。
                    //若不是酷我界面，通过控制方法来更改状态：正在加载中。。
                    //暂时有异常，当初次进入酷我的时候，走完play后会再次执行user_end的回掉。暂时注掉。
                    if (TeddyEvent.TEDDY_SCENE_KU_WO.equals(TeddyMain.getInstance().getCurrentPageScene())) {
                        SitechDevLog.e("KuwoUtils","---------registerPlayEndListener---END_USER----- " + true);
                        setKuwoLoading(true);
                    }
                    break;*/

                case END_ERROR:
                    setKuwoLoading(false);
                    break;
            }
        });

    }

    /**
     * 获取酷我APP的运行状态
     *
     * @return true==酷我APP正在运行
     */
    public static boolean isRunning() {
        return mKwapi.isKuwoRunning();
    }

    /**
     * 退出酷我APP
     */
    public static void exitKuwoApp() {
        SitechDevLog.e("KuwoUtils", "---------exitKuwoApp----------");
        ThreadUtils.runOnUIThread(() -> {
            if (isRunning()) {
                setKuwoLoading(false);
//                EventBusUtils.postEvent(new SysEvent(SysEvent.EB_SYS_SRC_SWITCTH, DataCenterConstants.CurrentSource.MODE_UNKNOWN));
                mKwapi.unbindAutoSdkService(BaseApp.getApp().getApplicationContext());
                mKwapi.exitAPP();
            }
        });
    }

    /**
     * 记录当前是否正在加载中。。。
     */
    private static boolean isKuwoLoading;

    private static void setKuwoLoading(boolean isLoading) {
        isKuwoLoading = isLoading;
    }

    public static boolean getKuwoLoading() {
        SitechDevLog.e("KuwoUtils", "---------getKuwoLoading----------isKuwoLoading = " + isKuwoLoading);
        return isKuwoLoading;
    }


}
