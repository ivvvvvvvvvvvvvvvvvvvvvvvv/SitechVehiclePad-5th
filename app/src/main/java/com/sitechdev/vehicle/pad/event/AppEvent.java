package com.sitechdev.vehicle.pad.event;

import android.os.IBinder;

import com.sitechdev.vehicle.lib.event.BaseEvent;

/**
 * 项目名称：SitechVehiclePad
 * 类名称：AppEvent
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2019/08/16 0016 11:01
 * 修改时间：
 * 备注：
 */
public class AppEvent extends BaseEvent {

    /**
     * 重新登录
     */
    public static final String EVENT_APP_RELOGIN = "EVENT_APP_RELOGIN";
    /**
     * token过期，刷新token
     */
    public static final String EVENT_APP_REFRESH_TOKEN = "EVENT_APP_REFRESH_TOKEN";
    /**
     * 登录成功
     */
    public static final String EVENT_APP_LOGIN_SUCCESS = "EVENT_APP_LOGIN_LOGIN";
    /**
     * 登出、退出登录
     */
    public static final String EVENT_APP_LOGIN_LOGOUT = "EVENT_APP_LOGIN_LOGOUT";
    /**
     * 滑动事件
     */
    public static final String EVENT_APP_WINDOW_MOVE = "EVENT_APP_WINDOW_MOVE";

    private String eventKey = "";
    private Object eventValue = null;

    public AppEvent(String key) {
        eventKey = key;
    }

    public AppEvent(String key, Object obj) {
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
