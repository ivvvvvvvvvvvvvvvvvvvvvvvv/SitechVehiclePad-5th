package com.my.hw;

import com.sitechdev.vehicle.lib.event.BaseEvent;

/**
 * <pre>
 *      author : zyf
 *      time   : 2020/6/22
 * </pre>
 */
public class BluetoothEvent extends BaseEvent {
    /**
     * 当前播放歌曲名城
     */
    public static String BT_EVENT_RECEIVE_TITLE = "bt_event_receive_title";
    /**
     * 当前播放歌手名称
     */
    public static String BT_EVENT_RECEIVE_ART = "bt_event_receive_art";
    /**
     * 当前播放状态--正在播放
     */
    public static String BT_EVENT_RECEIVE_PLAY_ON = "bt_event_receive_play_on";
    /**
     * 当前播放状态-暂停播放
     */
    public static String BT_EVENT_RECEIVE_PLAY_OFF = "bt_event_receive_play_off";
    private String tag;
    private Object object;

    public BluetoothEvent(String tag, Object o) {
        this.tag = tag;
        this.object = o;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
