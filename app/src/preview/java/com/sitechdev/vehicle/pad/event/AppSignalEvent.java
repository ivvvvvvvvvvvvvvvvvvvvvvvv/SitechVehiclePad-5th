package com.sitechdev.vehicle.pad.event;

import com.sitechdev.vehicle.lib.event.BaseEvent;

/**
 * 项目名称：SitechVehiclePad-5th
 * 类名称：AppSignalEvent
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2020/04/23 0023 15:41
 * 修改时间：
 * 备注：
 */
public class AppSignalEvent extends BaseEvent {

    /**
     * 手机信号状态变化
     */
    public static final String EVENT_SIGNAL_CHANGE_PHONE_STATE = "EVENT_SIGNAL_CHANGE_PHONE_STATE";
    /**
     * WIFI信号状态变化
     */
    public static final String EVENT_SIGNAL_CHANGE_WIFI_STATE = "EVENT_SIGNAL_CHANGE_WIFI_STATE";
    /**
     * 蓝牙信号状态变化
     */
    public static final String EVENT_SIGNAL_CHANGE_BLUETOOTH_STATE = "EVENT_SIGNAL_CHANGE_BLUETOOTH_STATE";
    /**
     * 音量信号状态变化
     */
    public static final String EVENT_SIGNAL_CHANGE_VOLUME_STATE = "EVENT_SIGNAL_CHANGE_VOLUME_STATE";
    /**
     * USB信号状态变化
     */
    public static final String EVENT_SIGNAL_CHANGE_USB_STATE = "EVENT_SIGNAL_CHANGE_USB_STATE";

    private String eventKey = "";
    private Object eventObject = null;

    public AppSignalEvent(String key) {
        eventKey = key;
    }

    public AppSignalEvent(String key, Object obj) {
        eventKey = key;
        eventObject = obj;
    }

    public String getEventKey() {
        return eventKey;
    }

    public Object getEventObject() {
        return eventObject;
    }
}
