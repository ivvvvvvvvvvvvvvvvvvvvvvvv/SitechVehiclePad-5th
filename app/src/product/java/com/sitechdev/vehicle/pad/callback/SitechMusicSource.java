package com.sitechdev.vehicle.pad.callback;

import com.sitechdev.vehicle.pad.bean.SitechMusicBean;

/**
 * @author 邵志
 * @version 2020/06/11 0011 16:00
 * @name SitechMusicListener
 * @description
 */
public class SitechMusicSource {
    /**
     * 音源渠道
     */
    public enum MusicChannel {
        /**
         * 未知音源。刚建立还没有音频播放时
         */
        CHANNEL_UNKOWN,
        /**
         * 本地音乐
         */
        CHANNEL_LOCAL_MUSIC,
        /**
         * 考拉听伴
         */
        CHANNEL_KAOLA,
        /**
         * 网络音乐-酷我音乐
         */
        CHANNEL_NET_MUSIC_KUWO;
    }

    /**
     * 音源播放模式
     */
    public enum MusicPlayModels {
        /**
         * 顺序播放--默认模式
         */
        MODEL_ALL_PLAY,
        /**
         * 单曲循环
         */
        MODEL_SINGLE_PLAY,
        /**
         * 随机播放
         */
        MODEL_RANDOM_PLAY;
    }

    /**
     * 界面UI控制播放逻辑的接口。UI触发逻辑
     */
    public interface onMusicControlListener {
        /**
         * 下一首
         */
        void onMusicPlayNext();

        /**
         * 上一首
         */
        void onMusicPlayPre();

        /**
         * 暂停播放
         */
        void onMusicPlayPause();

        /**
         * 继续播放
         */
        void onMusicPlayResume();

        /**
         * 切换播放模式: 顺序、单曲、随机
         */
        void onMusicChangePlayModel(MusicPlayModels playModel);

        /**
         * 随机播放一首歌曲
         */
        void onMusicRandomPlay();

        /**
         * 当前是否有音频正在播放
         */
        boolean isMusicPlaying();

        /**
         * 清除音频资源
         */
        void releaseMusicSource();
    }

    /**
     * 为了界面展示用的回调接口。改变上层UI
     */
    public interface onMusicInfoChangeUIListener {

        /**
         * 当前播放的音乐信息
         *
         * @param musicBean 音乐Bean
         */
        void onMusicInfo(SitechMusicBean musicBean);

        /**
         * 当前播放的音乐图片
         *
         * @param musicBean 音乐Bean
         */
        void onMusicImage(SitechMusicBean musicBean);

        /**
         * 当前播放的状态
         *
         * @param musicBean 音乐Bean
         */
        void onMusicPlayStatus(SitechMusicBean musicBean);

        /**
         * 当前播放的音乐进度
         *
         * @param musicBean 音乐Bean
         */
        void onMusicProgress(SitechMusicBean musicBean);

        /**
         * 播放失败的回调
         *
         * @param musicBean 音乐Bean
         */
        void onMusicPlayerFailed(SitechMusicBean musicBean);

        /**
         * 播放完成的回调
         *
         * @param musicBean 音乐Bean
         */
        void onMusicPlayerFinish(SitechMusicBean musicBean);
    }
}
