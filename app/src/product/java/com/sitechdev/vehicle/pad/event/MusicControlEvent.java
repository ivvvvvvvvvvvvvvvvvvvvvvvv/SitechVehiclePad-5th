package com.sitechdev.vehicle.pad.event;

import com.sitechdev.vehicle.lib.event.BaseEvent;

/**
 * @author 邵志
 * @version 2020/06/15 0015 11:02
 * @name MusicControlEvent
 * @description 音乐的控制事件。由页面端发起，控制各音乐模块做出相应的动作
 */
public class MusicControlEvent extends BaseEvent<String> {

    /**
     * 上一首事件
     */
    public static final String EVENT_CONTROL_MUSIC_PRE = "EVENT_CONTROL_MUSIC_PRE";

    /**
     * 下一首事件
     */
    public static final String EVENT_CONTROL_MUSIC_NEXT = "EVENT_CONTROL_MUSIC_NEXT";
    /**
     * 如果UI显示在最上层   执行播放
     */
    public static final String EVENT_CONTROL_MUSIC_PLAY_IF_ON_TOP = "EVENT_CONTROL_MUSIC_PLAY_IF_ON_TOP";

    /**
     * 停止播放
     */
    public static final String EVENT_CONTROL_MUSIC_PLAY_STOP = "EVENT_CONTROL_MUSIC_PLAY_STOP";
    /**
     * 暂停播放
     */
    public static final String EVENT_CONTROL_MUSIC_PLAY_PAUSE = "EVENT_CONTROL_MUSIC_PLAY_PAUSE";
    /**
     * 继续播放
     */
    public static final String EVENT_CONTROL_MUSIC_PLAY_RESUME = "EVENT_CONTROL_MUSIC_PLAY_RESUME";
    /**
     * 随机播放一首歌曲
     */
    public static final String EVENT_CONTROL_MUSIC_PLAY_RANDOM = "EVENT_CONTROL_MUSIC_PLAY_RANDOM";
    /**
     * 更改播放模式--顺序、随机、单曲等
     * 参数：{@link com.sitechdev.vehicle.pad.callback.SitechMusicSource.MusicPlayModels}
     */
    public static final String EVENT_CONTROL_MUSIC_CHANGE_MODE = "EVENT_CONTROL_MUSIC_CHANGE_MODE";
    /**
     * 更改播放模式--顺序、随机、单曲等
     */
    public static final String EVENT_CONTROL_MUSIC_PLAY_BY_INFO = "EVENT_CONTROL_MUSIC_PLAY_BY_INFO";


    private String key = "";
    private Object bean = null;

    public MusicControlEvent(String evt) {
        this.key = evt;
    }

    public MusicControlEvent(String evt, Object musicInfo) {
        this.key = evt;
        bean = musicInfo;
    }

    public String getKey() {
        return key;
    }

    public Object getBean() {
        return bean;
    }
}
