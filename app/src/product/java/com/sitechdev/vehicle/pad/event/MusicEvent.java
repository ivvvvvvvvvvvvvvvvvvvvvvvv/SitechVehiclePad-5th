package com.sitechdev.vehicle.pad.event;

import com.sitechdev.vehicle.lib.event.BaseEvent;
import com.sitechdev.vehicle.pad.bean.SitechMusicBean;

/**
 * @author 邵志
 * @version 2020/06/15 0015 11:02
 * @name MusicEvent
 * @description
 */
public class MusicEvent extends BaseEvent<String> {

    /**
     * 更新当前播放的Music信息
     */
    public static final String EVENT_UPD_MUSIC_INFO = "EVENT_UPD_MUSIC_INFO";
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
    private SitechMusicBean bean = null;

    public MusicEvent(String evt, SitechMusicBean musicBean) {
        this.key = evt;
        bean = musicBean;
    }

    public String getKey() {
        return key;
    }

    public SitechMusicBean getBean() {
        return bean;
    }
}
