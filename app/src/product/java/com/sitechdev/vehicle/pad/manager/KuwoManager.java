package com.sitechdev.vehicle.pad.manager;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.sitechdev.vehicle.lib.base.BaseApp;
import com.sitechdev.vehicle.lib.event.EventBusUtils;
import com.sitechdev.vehicle.lib.util.NetworkUtils;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.lib.util.StringUtils;
import com.sitechdev.vehicle.lib.util.ThreadUtils;
import com.sitechdev.vehicle.pad.app.AppApplication;
import com.sitechdev.vehicle.pad.callback.BaseBribery;
import com.sitechdev.vehicle.pad.callback.SitechMusicSource;
import com.sitechdev.vehicle.pad.event.KuwoEvent;
import com.sitechdev.vehicle.pad.event.MusicStatusEvent;

import java.util.List;
import java.util.Random;

import cn.kuwo.autosdk.api.KWAPI;
import cn.kuwo.autosdk.api.OnGetBaseOnlineListListener;
import cn.kuwo.autosdk.api.OnGetBaseOnlineListener;
import cn.kuwo.autosdk.api.OnGetSongImgListener;
import cn.kuwo.autosdk.api.OnGetSongListListener;
import cn.kuwo.autosdk.api.OnSearchListener;
import cn.kuwo.autosdk.api.PlayMode;
import cn.kuwo.autosdk.api.PlayState;
import cn.kuwo.autosdk.api.PlayerStatus;
import cn.kuwo.autosdk.bean.notproguard.BaseOnlineSection;
import cn.kuwo.autosdk.bean.notproguard.BaseQukuItem;
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
public class KuwoManager extends BaseMusicManager {
    private static KWAPI mKwapi = null;
    private boolean isServerConnected = false;

    private KuwoManager() {
        mKwapi = KWAPI.createKWAPI(BaseApp.getApp().getApplicationContext(), "auto");
        //注册一些监听
        registerKuwoListener();
    }

    private static final class SingleKuwoManager {
        private static final KuwoManager SINGLE = new KuwoManager();
    }

    public static KuwoManager getInstance() {
        return SingleKuwoManager.SINGLE;
    }

    /**
     * 启动酷我APP
     *
     * @param isAutoplay true==打开后自动播放
     */
    public void startKuwoApp(boolean isAutoplay) {
        ThreadUtils.runOnUIThread(() -> {
            boolean networkAvailable = NetworkUtils.isNetworkAvailable(BaseApp.getApp().getApplicationContext());
            mKwapi.startAPP(networkAvailable);
            registerKuwoService();
            if (isServerConnected) {
                getPlayResource();
            } else {
                isServerConnected = true;
            }
        });
    }

    /**
     * 注册酷我服务
     */
    public void registerKuwoService() {
        //注册
        registerPlayingManager(VoiceSourceManager.KUWO_MUSIC);

        if (mKwapi != null) {
            mKwapi.bindAutoSdkService(BaseApp.getApp().getApplicationContext());
        }
    }

    public void unRegisterKuwoService() {
        registerPlayingManager(-1);
        if (mKwapi != null) {
            mKwapi.unbindAutoSdkService(BaseApp.getApp().getApplicationContext());
        }
    }

    /**
     * 获取当前正在播放的歌曲信息
     */
    private void getPlayResource() {
        if (mKwapi != null) {
            Music music = mKwapi.getNowPlayingMusic();
            if (music == null) {
                return;
            }
            SitechDevLog.e("music", "---------getPlayResource----------");
            pullMusicBitmap(music, new BaseBribery() {
                @Override
                public void onSuccess(Object successObj) {
//                    musicBean.setIconBitmap((Bitmap) successObj);
                    SitechDevLog.e("music", "-----getPlayResource----pullMusicBitmap---------setIconBitmap-" + successObj);
                    EventBusUtils.postEvent(new MusicStatusEvent(MusicStatusEvent.EVENT_UPD_MUSIC_IMAGE, (Bitmap) successObj));
                }
            });

            EventBusUtils.postEvent(new MusicStatusEvent(MusicStatusEvent.EVENT_UPD_MUSIC_TITLE_INFO, music.name + " -- " + music.artist));
            EventBusUtils.postEvent(new MusicStatusEvent(MusicStatusEvent.EVENT_UPD_MUSIC_PLAY_STATUS, getPlayStatus()));
        }
    }

    /**
     * 在线搜索歌曲并播放,通过歌曲名称
     *
     * @param musicName 歌曲名称
     */
    public void playMusic(String musicName) {
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
    public void playMusic(String musicName, String musicSinger) {
        ThreadUtils.runOnUIThread(() -> {
            registerKuwoService();
            setKuwoLoading(true);
            mKwapi.playClientMusics(musicName, musicSinger, "");
            isServerConnected = true;
        });
    }

    /**
     * 在线搜索歌曲并播放,通过歌曲名称
     *
     * @param list    歌曲列表
     * @param index   播放索引
     * @param isEntry 是否打开酷我
     * @param isExit  若打开酷我时，是否3秒内退出酷我音乐界面
     */
    public void playMusic(List<Music> list, int index, boolean isEntry, boolean isExit) {
        ThreadUtils.runOnUIThread(() -> {
            setKuwoLoading(true);
            mKwapi.playMusic(list, index, isEntry, isExit);
            isServerConnected = true;
        });
    }

    /**
     * 在线搜索歌曲并播放,通过主题搜索
     *
     * @param theme 主题
     */
    public void searchThemeMusic(String theme, OnSearchListener searchListener) {
        ThreadUtils.runOnUIThread(() -> {
            isServerConnected = true;
//            setKuwoLoading(true);
            mKwapi.searchOnlineMusicByTheme(theme, searchListener);
        });
    }


    /**
     * 在线搜索歌曲
     *
     * @param musicName      歌曲名称
     * @param musicSinger    歌手
     * @param album          专辑
     * @param searchListener 回调
     */
    public void searchOnlineMusic(String musicName, String musicSinger, String album, OnSearchListener searchListener) {
        ThreadUtils.runOnUIThread(() -> {
            isServerConnected = true;
            mKwapi.searchOnlineMusic(musicSinger, musicName, album, searchListener);
        });
    }

    /**
     * 在线搜索歌曲并播放,通过歌曲名称
     *
     * @param musicName   歌曲名称
     * @param musicSinger 歌手
     * @param musicAlbum  专辑名
     */
    public void playMusic(String musicName, String musicSinger, String musicAlbum) {
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
    public void playMusicByLrc(String lrc) {
        ThreadUtils.runOnUIThread(() -> {
            setKuwoLoading(true);
            mKwapi.playClientMusicsByLrc(lrc);
        });
    }

    /**
     * 播放控制
     */
    public void playControl(KuwoEvent.PlayControl playControl) {
//        EventBusUtils.postEvent(new KuwoEvent(KuwoEvent.EB_KUWO_PLAY_CONTROL, playControl));
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
    public boolean getPlayStatus() {
        if (!isServerConnected || mKwapi == null || !mKwapi.isKuwoRunning()) {
            return false;
        }
        return mKwapi.getPlayerStatus() == PlayerStatus.PLAYING;
    }

    /**
     * 获取当前酷我音乐的正在播放的音乐信息
     *
     * @return musicInfo
     */
    public Music getNowPlayingMusicInfo() {
        if (mKwapi == null) {
            return null;
        }
        return mKwapi.getNowPlayingMusic();
    }

    /**
     * 进入酷我随机播放一首歌曲
     */
    public void startRandomPlayMusic() {
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
    public void setMusicByImageView(Music music, ImageView imageView, int defaultResID) {
        if (mKwapi == null) {
            return;
        }
        mKwapi.displayImage(music, imageView, defaultResID);
    }

    /**
     * 获取当前播放的音乐的音乐小图。
     * 结果使用new KuwoEvent(KuwoEvent.EB_KUWO_MUSIC_BITMAP_RESULT, bitmap)事件传递。
     * 如果值为null，则代表获取该音乐小图片失败。
     *
     * @param music 当前播放的音乐
     */
    public void pullMusicBitmap(Music music, BaseBribery bribery) {
        if (mKwapi == null) {
            return;
        }
        mKwapi.setMusicImg(music, new OnGetSongImgListener() {
            @Override
            public void sendSyncNotice_HeadPicStart(Music music) {
                //开始下载图片
                SitechDevLog.e("music", "---------开始下载图片----------");
            }

            @Override
            public void sendSyncNotice_HeadPicFinished(Music music, Bitmap bitmap) {
                //下载完成
//                EventBusUtils.postEvent(new KuwoEvent(KuwoEvent.EB_KUWO_MUSIC_BITMAP_RESULT, bitmap));
                SitechDevLog.e("music", "---------图片下载完成----------");
                if (bribery != null) {
                    bribery.onSuccess(bitmap);
                }
            }

            @Override
            public void sendSyncNotice_HeadPicFailed(Music music) {
                //下载失败
                EventBusUtils.postEvent(new KuwoEvent(KuwoEvent.EB_KUWO_MUSIC_BITMAP_RESULT, null));
                SitechDevLog.e("music", "---------图片下载失败----------");
            }

            @Override
            public void sendSyncNotice_HeadPicNone(Music music) {
                //没有图片，无法下载
                EventBusUtils.postEvent(new KuwoEvent(KuwoEvent.EB_KUWO_MUSIC_BITMAP_RESULT, null));
                SitechDevLog.e("music", "---------没有图片，无法下载----------");
            }
        });
    }

    /**
     * 获取当前播放的歌曲的时长
     */
    public int getCurrentMusicDuration() {
        return mKwapi.getCurrentMusicDuration();
    }

    /**
     * 获取当前播放的歌曲的进度
     */
    public int getCurrentMusicPos() {
        return mKwapi.getCurrentPos();
    }

    /**
     * 注册酷我信息监听
     */
    private void registerKuwoListener() {
        //酷我进入
        mKwapi.registerEnterListener(AppApplication.getContext(),
                () -> {
                    SitechDevLog.e("music", "---------registerEnterListener----------");
                    //EventBusUtils.postEvent(new KuwoEvent(KuwoEvent.EB_KUWO_LANUCHER));
                    //解除注册
                    mKwapi.unRegisterEnterListener(AppApplication.getContext());
                });
        //酷我退出
        mKwapi.registerExitListener(AppApplication.getContext(),
                () -> {
                    SitechDevLog.e("music", "---------registerExitListener----------");
                    setKuwoLoading(false);
                    //EventBusUtils.postEvent(new KuwoEvent(KuwoEvent.EB_KUWO_EXIT));
                    //解除注册
                    mKwapi.unRegisterExitListener(AppApplication.getContext());
                    isServerConnected = false;
                });
        //播放状态的改变
        mKwapi.registerPlayerStatusListener(AppApplication.getContext(),
                (playerStatus, music) -> {
                    if (!isServerConnected) {
                        return;
                    }
                    SitechDevLog.e("music", "---------registerPlayerStatusListener---------music-" + music.name);
                    SitechDevLog.e("music", "---------registerPlayerStatusListener---------playerStatus-" + playerStatus);

                    switch (playerStatus) {
                        case INIT:
                            pullMusicBitmap(music, new BaseBribery() {
                                @Override
                                public void onSuccess(Object successObj) {
                                    SitechDevLog.e("music", "---------pullMusicBitmap---------setIconBitmap-" + successObj);
                                    EventBusUtils.postEvent(new MusicStatusEvent(MusicStatusEvent.EVENT_UPD_MUSIC_IMAGE, (Bitmap) successObj));
                                }
                            });
                            EventBusUtils.postEvent(new MusicStatusEvent(MusicStatusEvent.EVENT_UPD_MUSIC_TITLE_INFO, music.name + " -- " + music.artist));
                        case BUFFERING:
                            setKuwoLoading(true);
                            break;
                        case PLAYING:
                        case STOP:
                        case PAUSE:
                            setKuwoLoading(false);
                            EventBusUtils.postEvent(new MusicStatusEvent(MusicStatusEvent.EVENT_UPD_MUSIC_PLAY_STATUS, playerStatus == PlayerStatus.PLAYING));
                            break;
                        default:
                            break;
                    }
                });

        mKwapi.registerPlayEndListener(AppApplication.getContext(), playEndType -> {
            SitechDevLog.e("music", "---------registerPlayerStatusListener---------playEndType-" + playEndType);
            switch (playEndType) {
                case END_COMPLETE:
                    setKuwoLoading(true);
                    break;
               /* case END_USER:
                    //若是酷我界面，则需要通过USER_END来更改状态：正在加载中。。
                    //若不是酷我界面，通过控制方法来更改状态：正在加载中。。
                    //暂时有异常，当初次进入酷我的时候，走完play后会再次执行user_end的回掉。暂时注掉。
                    if (TeddyEvent.TEDDY_SCENE_KU_WO.equals(TeddyMain.getInstance().getCurrentPageScene())) {
                        SitechDevLog.e("music","---------registerPlayEndListener---END_USER----- " + true);
                        setKuwoLoading(true);
                    }
                    break;*/

                case END_ERROR:
                    setKuwoLoading(false);
                    break;
                default:
                    break;
            }
        });

    }

    /**
     * 获取当前正在播放的酷我音乐
     */
    public void getCurrentPlayMusic() {

    }

    /**
     * 获取酷我APP的运行状态
     *
     * @return true==酷我APP正在运行
     */
    public boolean isRunning() {
        return mKwapi.isKuwoRunning();
    }

    /**
     * 退出酷我APP
     */
    public void exitKuwoApp() {
        SitechDevLog.e("music", "---------exitKuwoApp----------");
        ThreadUtils.runOnUIThread(() -> {
            if (isRunning()) {
                setKuwoLoading(false);
                unRegisterKuwoService();
                isServerConnected = false;
                mKwapi.exitAPP();
                EventBusUtils.postEvent(new MusicStatusEvent(MusicStatusEvent.EVENT_UPD_MUSIC_IMAGE, null));
                EventBusUtils.postEvent(new MusicStatusEvent(MusicStatusEvent.EVENT_UPD_MUSIC_TITLE_INFO, null));
                EventBusUtils.postEvent(new MusicStatusEvent(MusicStatusEvent.EVENT_UPD_MUSIC_PLAY_STATUS, false));
            }
        });
    }

    /**
     * 记录当前是否正在加载中。。。
     */
    private boolean isKuwoLoading;

    private void setKuwoLoading(boolean isLoading) {
        isKuwoLoading = isLoading;
    }

    public boolean getKuwoLoading() {
        SitechDevLog.e("music", "---------getKuwoLoading----------isKuwoLoading = " + isKuwoLoading);
        return isKuwoLoading;
    }

    /**
     * 下一首
     */
    public void onMusicPlayNext() {
        playControl(KuwoEvent.PlayControl.PLAY_NEXT);
    }

    /**
     * 上一首
     */
    public void onMusicPlayPre() {
        playControl(KuwoEvent.PlayControl.PLAY_PRE);
    }

    /**
     * 暂停/继续播放
     */
    public void onMusicPlayPause() {
        if (getPlayStatus()) {
            playControl(KuwoEvent.PlayControl.PLAY_PAUSE);
        }
    }

    /**
     * 继续播放
     */
    public void onMusicPlayResume() {
        try {
            if (!getPlayStatus()) {
                playControl(KuwoEvent.PlayControl.PLAY_PLAY);
            }
        } catch (Exception e) {
            SitechDevLog.exception(e);
        }
    }

    /**
     * 切换播放模式: 顺序、单曲、随机
     *
     * @param playModel
     */
    public void onMusicChangePlayModel(SitechMusicSource.MusicPlayModels playModel) {
        switch (playModel) {
            default:
            case MODEL_ALL_PLAY:
                playControl(KuwoEvent.PlayControl.PLAY_MODE_ALL_CIRCLE);
                break;
            case MODEL_SINGLE_PLAY:
                playControl(KuwoEvent.PlayControl.PLAY_MODE_SINGLE);
                break;
            case MODEL_RANDOM_PLAY:
                playControl(KuwoEvent.PlayControl.PLAY_MODE_RANDOM);
                break;
        }
    }

    /**
     * 随机播放一首歌曲
     */
    public void onMusicRandomPlay() {
        if (mKwapi != null) {
            mKwapi.randomPlayMusic();
        }
    }

    /**
     * 当前是否有音频正在播放
     */
    public boolean isMusicPlaying() {
        if (mKwapi == null) {
            return false;
        }
        return getPlayStatus();
    }

    /**
     * 清除音频资源
     */
    public void releaseMusicSource() {
        SitechDevLog.e("Music", this.getClass().getSimpleName() + "---------releaseMusicSource----------= 退出酷我");
        //界面展示为空
        exitKuwoApp();
    }

    /**
     * 返回可以播放的歌单--主要为语音使用
     *
     * @param songKeyList 热词关键字列表
     * @param bribery     回调函数==>回调onSuccess为成功，回调onFailure为失败
     */
    public void playSongList(List<String> songKeyList, BaseBribery bribery) {
        if (songKeyList == null || songKeyList.isEmpty()) {
            if (bribery != null) {
                bribery.onFailure("");
            }
            return;
        }

        //匹配“热歌或新歌”等排行榜音乐
        String finalSongKey = "";
        for (String songKey : songKeyList) {
            if ("最新".equalsIgnoreCase(songKey)
                    || "新歌".equalsIgnoreCase(songKey)) {
                finalSongKey = "新歌";
            }
            if ("最热".equalsIgnoreCase(songKey)
                    || "热歌".equalsIgnoreCase(songKey)) {
                finalSongKey = "热歌";
            }
            if (!StringUtils.isEmpty(finalSongKey)) {
                SitechDevLog.i("Music", "--------匹配到的“热歌或新歌”Name为：----------finalSongKey= " + finalSongKey);
                playTopSongList(finalSongKey, bribery);
                return;
            }
        }

        SitechDevLog.i("Music", "--------匹配分类音乐：----------= ");
        //匹配分类音乐
        mKwapi.showClassifyList(0, new OnGetBaseOnlineListListener() {
            @Override
            public void onSuccess(List<BaseOnlineSection> onlineSections) {
                //主题---影视/抖音/古风、心情-温柔/年轻
                for (int i = 0; i < onlineSections.size(); i++) {
                    BaseOnlineSection baseOnlineSection = onlineSections.get(i);
                    for (int i1 = 0; i1 < baseOnlineSection.getOnlineInfos().size(); i1++) {
                        BaseQukuItem qukuItem = baseOnlineSection.getOnlineInfos().get(i1);
                        for (int t = 0; t < songKeyList.size(); t++) {
                            String songKey = songKeyList.get(t);
                            if (songKey.equalsIgnoreCase(qukuItem.getName())) {
                                SitechDevLog.i("Music", "--------匹配到的分类Name为：----------songKey= " + songKey);
                                SitechDevLog.i("Music", "--------匹配到的分类Name为：----------qukuItem.getName()= " + qukuItem.getName());
                                mKwapi.showCategoryList(0, qukuItem, new OnGetBaseOnlineListener() {
                                            @Override
                                            public void onSuccess(BaseOnlineSection onlineSection) {
                                                if (onlineSection != null) {
                                                    List<BaseQukuItem> items = onlineSection.getOnlineInfos();
                                                    if (onlineSection.getOnlineInfos().size() > 0) {
                                                        int randomIndex = new Random().nextInt(onlineSection.getOnlineInfos().size());
                                                        BaseQukuItem finalQukuItem = onlineSection.getOnlineInfos().get(randomIndex);
                                                        SitechDevLog.i("Music", "--------最终播放的歌单名称：----------= " + finalQukuItem.getName());
                                                        playMusicList(finalQukuItem, bribery);
                                                    } else {
                                                        if (bribery != null) {
                                                            bribery.onFailure("");
                                                        }
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onError(String str) {
                                                SitechDevLog.i("Music", "--------获取歌单信息失败：----------错误信息==> " + str);
                                                if (bribery != null) {
                                                    bribery.onFailure(str);
                                                }
                                            }
                                        }
                                );
                                return;
                            }
                        }
                    }
                }
                if (bribery != null) {
                    bribery.onFailure("");
                }
            }

            @Override
            public void onError(String str) {
                SitechDevLog.i("Music", "--out------获取歌单信息失败：----------错误信息==> " + str);
                if (bribery != null) {
                    bribery.onFailure(str);
                }
            }
        });
    }

    /**
     * 返回可以播放的歌单
     *
     * @param songKey 热词关键字
     * @param bribery 回调函数==>回调onSuccess为成功，回调onFailure为失败
     */
    public void playTopSongList(String songKey, BaseBribery bribery) {
        SitechDevLog.i("Music", "--------排行榜 需要匹配的songKey名称：----------= " + songKey);
        mKwapi.showTopList(0, new OnGetBaseOnlineListener() {
                    @Override
                    public void onSuccess(BaseOnlineSection onlineSection) {
                        if (onlineSection != null) {
                            final List<BaseQukuItem> items = onlineSection.getOnlineInfos();
                            if (items == null || items.isEmpty()) {
                                if (bribery != null) {
                                    bribery.onFailure("");
                                }
                                return;
                            }

                            for (int i = 0; i < items.size(); i++) {
                                BaseQukuItem baseQukuItem = items.get(i);
                                String songName = baseQukuItem.getName();
                                SitechDevLog.i("Music", "--------排行榜 songName名称：----------= " + songName);
                                if (songName.contains(songKey)) {
                                    SitechDevLog.i("Music", "--------排行榜 最终播放的歌单名称：----------= " + baseQukuItem.getName());
                                    playMusicList(baseQukuItem, bribery);
                                    break;
                                }
                            }
                        }
                        if (bribery != null) {
                            bribery.onFailure("");
                        }
                    }

                    @Override
                    public void onError(String str) {
                        SitechDevLog.i("Music", "--------排行榜 获取歌单信息失败：----------错误信息==> " + str);
                        if (bribery != null) {
                            bribery.onFailure(str);
                        }
                    }
                }
        );
    }

    /**
     * 播放最终的歌单，替换当前歌单
     *
     * @param baseQukuItem baseQukuItem
     * @param bribery      bribery
     */
    private void playMusicList(BaseQukuItem baseQukuItem, BaseBribery bribery) {
        mKwapi.showSongList(0, baseQukuItem, new OnGetSongListListener() {
            @Override
            public void onSuccess(final List<Music> musics) {
                if (musics == null || musics.isEmpty()) {
                    if (bribery != null) {
                        bribery.onFailure("");
                    }
                    return;
                }
                mKwapi.playMusicReplacePlayList(musics, 0, true, false);
                if (bribery != null) {
                    bribery.onSuccess(true);
                }
            }

            @Override
            public void onError(String str) {
                if (bribery != null) {
                    bribery.onFailure(str);
                }
            }
        });
    }
}
