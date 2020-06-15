package com.sitechdev.vehicle.pad.manager;

import android.content.Context;

import com.sitechdev.vehicle.pad.callback.SitechMusicSource;

/**
 * @author zhubaoqiang
 * @date 2019/8/26
 */
public class SitechMusicNewManager {
    private Context context;
    private static SitechMusicNewManager INSTANCE;
    private BaseMusicManager currentMusicChannel = null;
    private SitechMusicSource.onMusicInfoChangeUIListener onMusicUiListener = null;

    private SitechMusicNewManager() {
    }

    private static final class SingleSitechMusicNewManager {
        private static final SitechMusicNewManager SINGLE = new SitechMusicNewManager();
    }

    public static SitechMusicNewManager getInstance() {
        return SingleSitechMusicNewManager.SINGLE;
    }

    /**
     * 注册当前播放的Music渠道
     *
     * @param channel {@link SitechMusicSource.MusicChannel}
     */
    public void registerCurrentMusicChannel(BaseMusicManager channel) {
        VoiceSourceManager.getInstance().setMusicSource(-1);
        currentMusicChannel = channel;
    }

    /**
     * 注册当前播放的Music渠道
     */
    public void resetCurrentMusicChannel() {
        currentMusicChannel = null;
    }

    public BaseMusicManager getCurrentMusicChannel() {
        return currentMusicChannel;
    }

    public SitechMusicSource.onMusicInfoChangeUIListener getOnMusicUiListener() {
        return onMusicUiListener;
    }

    public void setOnMusicUiListener(SitechMusicSource.onMusicInfoChangeUIListener onMusicUiListener) {
        this.onMusicUiListener = onMusicUiListener;
    }

    /**
     * 下一首
     */
    public void onPlayNext() {
//        switch (currentMusicChannel) {
//            case CHANNEL_UNKOWN:
//                break;
//            case CHANNEL_LOCAL_MUSIC:
//                break;
//            case CHANNEL_KAOLA:
//                break;
//            case CHANNEL_NET_MUSIC_KUWO:
//                KuwoManager.playControl(KuwoEvent.PlayControl.PLAY_NEXT);
//                break;
//            default:
//                break;
//        }
        if (currentMusicChannel != null) {
            currentMusicChannel.onMusicPlayNext();
        }
    }

    /**
     * 上一首
     */
    public void onPlayPre() {
        if (currentMusicChannel != null) {
            currentMusicChannel.onMusicPlayPre();
        }
//        switch (currentMusicChannel) {
//            case CHANNEL_UNKOWN:
//                break;
//            case CHANNEL_LOCAL_MUSIC:
//                break;
//            case CHANNEL_KAOLA:
//                break;
//            case CHANNEL_NET_MUSIC_KUWO:
//                KuwoManager.playControl(KuwoEvent.PlayControl.PLAY_PRE);
//                break;
//            default:
//                break;
//        }
    }

    /**
     * 暂停播放
     */
    public void onPlayPause() {
        if (currentMusicChannel != null) {
            currentMusicChannel.onMusicPlayPause();
        }
//        switch (currentMusicChannel) {
//            case CHANNEL_UNKOWN:
//                break;
//            case CHANNEL_LOCAL_MUSIC:
//                break;
//            case CHANNEL_KAOLA:
//                break;
//            case CHANNEL_NET_MUSIC_KUWO:
//                KuwoManager.playControl(KuwoManager.getPlayStatus() ? KuwoEvent.PlayControl.PLAY_PAUSE : KuwoEvent.PlayControl.PLAY_PLAY);
//                break;
//            default:
//                break;
//        }
    }

    /**
     * 继续播放
     */
    public void onPlayResume() {
        if (currentMusicChannel != null) {
            currentMusicChannel.onMusicPlayResume();
        }
//        switch (currentMusicChannel) {
//            case CHANNEL_UNKOWN:
//                break;
//            case CHANNEL_LOCAL_MUSIC:
//                break;
//            case CHANNEL_KAOLA:
//                break;
//            case CHANNEL_NET_MUSIC_KUWO:
//                KuwoManager.playControl(KuwoManager.getPlayStatus() ? KuwoEvent.PlayControl.PLAY_PAUSE : KuwoEvent.PlayControl.PLAY_PLAY);
//                break;
//            default:
//                break;
//        }
    }

    /**
     * 切换播放模式: 顺序、单曲、随机
     *
     * @param playModel 播放模式
     */
    public void onChangePlayModel(SitechMusicSource.MusicPlayModels playModel) {
        if (currentMusicChannel != null) {
            currentMusicChannel.onMusicChangePlayModel(playModel);
        }
//        switch (currentMusicChannel) {
//            case CHANNEL_UNKOWN:
//                break;
//            case CHANNEL_LOCAL_MUSIC:
//                break;
//            case CHANNEL_KAOLA:
//                break;
//            case CHANNEL_NET_MUSIC_KUWO:
//                switch (playModel) {
//                    default:
//                    case MODEL_ALL_PLAY:
//                        KuwoManager.playControl(KuwoEvent.PlayControl.PLAY_MODE_ALL_CIRCLE);
//                        break;
//                    case MODEL_SINGLE_PLAY:
//                        KuwoManager.playControl(KuwoEvent.PlayControl.PLAY_MODE_SINGLE);
//                        break;
//                    case MODEL_RANDOM_PLAY:
//                        KuwoManager.playControl(KuwoEvent.PlayControl.PLAY_MODE_RANDOM);
//                        break;
//                }
//                break;
//            default:
//                break;
//        }
    }

    /**
     * 随机播放歌曲
     */
    public void onPlayRandomMusic() {
        if (currentMusicChannel != null) {
            currentMusicChannel.onMusicRandomPlay();
        }
    }

    /**
     * 当前是否有音频正在播放
     *
     * @return true=当前有音频正在播放，false=无音频正在播放
     */
    public boolean hasMusicPlaying() {
        if (currentMusicChannel != null) {
            return currentMusicChannel.isMusicPlaying();
        }
        return false;
    }
}
