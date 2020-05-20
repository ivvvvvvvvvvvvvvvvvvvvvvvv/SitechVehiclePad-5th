package com.sitechdev.vehicle.pad.event;

import com.sitechdev.vehicle.lib.event.BaseEvent;

/**
 * 项目名称：SitechVehiclePad
 * 类名称：ScreenEvent
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2019/08/16 0016 11:01
 * 修改时间：
 * 备注：
 */
public class ScreenEvent extends BaseEvent {

    /**
     * 屏幕方向切换事件
     * true=横屏，false=竖屏
     */
    public static final String EVENT_SCREEN_ORIENTATION_CHANGE = "EVENT_SCREEN_ORIENTATION_CHANGE";

    private String eventKey = "";
    private Object eventValue = null;


    public ScreenEvent(String key) {
        eventKey = key;
    }

    public ScreenEvent(String key, Object obj) {
        eventKey = key;
        eventValue = obj;
    }

    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    public Object getEventValue() {
        return eventValue;
    }

    public void setEventValue(Object eventValue) {
        this.eventValue = eventValue;
    }
}
