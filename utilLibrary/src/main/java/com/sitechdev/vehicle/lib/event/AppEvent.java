package com.sitechdev.vehicle.lib.event;

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
    /**
     * 换肤事件
     */
    public static final String EVENT_APP_CHANGE_SKIN = "EVENT_APP_CHANGE_SKIN";

    //=================OPEN APP================================
    /**
     * 跳转个人中心页面
     */
    public static final String EVENT_APP_OPEN_MEMBER_INFO_PAGE = "EVENT_APP_OPEN_MEMBER_INFO_PAGE";
    /**
     * 跳转我的积分页面
     */
    public static final String EVENT_APP_MY_POINT_PAGE = "EVENT_APP_MY_POINT_PAGE";
    /**
     * 跳转车辆状态页面
     */
    public static final String EVENT_APP_OPEN_CAR_STATUS_PAGE = "EVENT_APP_OPEN_CAR_STATUS_PAGE";
    /**
     * 跳转皮肤页面
     */
    public static final String EVENT_APP_OPEN_SETTING_SKIN_PAGE = "EVENT_APP_OPEN_SETTING_SKIN_PAGE";
    /**
     * 跳转设置页面
     */
    public static final String EVENT_APP_OPEN_SETTING_PAGE = "EVENT_APP_OPEN_SETTING_PAGE";
    /**
     * 跳转网络设置页面
     */
    public static final String EVENT_APP_OPEN_NET_SETTING_PAGE = "EVENT_APP_OPEN_NET_SETTING_PAGE";
    /**
     * 跳转蓝牙设置页面
     */
    public static final String EVENT_APP_OPEN_BT_SETTING_PAGE = "EVENT_APP_OPEN_BT_SETTING_PAGE";
    /**
     * 跳转语音设置页面
     */
    public static final String EVENT_APP_OPEN_VOICE_SETTING_PAGE = "EVENT_APP_OPEN_VOICE_SETTING_PAGE";
    /**
     * 跳转系统设置页面
     */
    public static final String EVENT_APP_OPEN_SYS_SETTING_PAGE = "EVENT_APP_OPEN_SYS_SETTING_PAGE";
    /**
     * 跳转出行计价器页面事件
     */
    public static final String EVENT_APP_OPEN_TAXI_PAGE = "EVENT_APP_OPEN_TAXI_PAGE";
    /**
     * 跳转出行计价器--开始计价事件
     */
    public static final String EVENT_APP_TAXI_START_PRICE = "EVENT_APP_TAXI_START_PRICE";
    /**
     * 出行计价器--停止计价事件
     */
    public static final String EVENT_APP_TAXI_STOP_PRICE = "EVENT_APP_TAXI_STOP_PRICE";
    public static final String EVENT_APP_KAOLA_UPDATE = "EVENT_APP_KAOLA_UPDATE";

    /**
     * 个人中心签到
     */
    public static final String EB_MEMBER_SIGN = "EB_MEMBER_SIGN";

    /**
     * 页面切换事件
     */
    public static final String EB_ACTIVITY_START_EVENT = "EB_ACTIVITY_START_EVENT";

    private String eventKey = "";
    private Object eventValue = null;

    public Object getEventValue2() {
        return eventValue2;
    }

    private Object eventValue2 = null;

    public AppEvent(String key) {
        eventKey = key;
    }

    public AppEvent(String key, Object obj) {
        eventKey = key;
        eventValue = obj;
    }

    public AppEvent(String key, Object obj, Object obj2) {
        eventKey = key;
        eventValue = obj;
        eventValue2 = obj2;
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
