package com.sitechdev.vehicle.pad.event;

import com.sitechdev.vehicle.lib.event.BaseEvent;

/**
 * @author 邵志
 * @version 2020/06/15 0015 11:02
 * @name MusicStatusEvent
 * @description 音乐状态变更事件。由各音乐模块改变时发起，提示前端页面改变UI，刷新信息显示
 */
public class MusicStatusEvent extends BaseEvent<String> {

    /**
     * 更新当前播放的Music-歌名信息
     * 参数格式： musicBean.getName() + " -- " + musicBean.getAuthor()
     */
    public static final String EVENT_UPD_MUSIC_TITLE_INFO = "EVENT_UPD_MUSIC_TITLE_INFO";

    /**
     * 更新当前播放的Music-歌手信息
     * 参数格式： musicBean.getName() + " -- " + musicBean.getAuthor()
     */
    public static final String EVENT_UPD_MUSIC_SINGLE_INFO = "EVENT_UPD_MUSIC_SINGLE_INFO";
    /**
     * 更新当前播放的Music icon
     */
    public static final String EVENT_UPD_MUSIC_IMAGE = "EVENT_UPD_MUSIC_IMAGE";
    /**
     * 更新当前播放的Music进度
     */
    public static final String EVENT_UPD_MUSIC_PROGRESS = "EVENT_UPD_MUSIC_PROGRESS";
    /**
     * 更新当前播放的Music状态
     */
    public static final String EVENT_UPD_MUSIC_PLAY_STATUS = "EVENT_UPD_MUSIC_PLAY_STATUS";

    private String key = "";
    private Object bean = null;

    public MusicStatusEvent(String evt, Object musicInfo) {
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
