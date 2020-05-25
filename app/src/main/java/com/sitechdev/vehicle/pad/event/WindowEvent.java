package com.sitechdev.vehicle.pad.event;

import com.sitechdev.vehicle.lib.event.BaseEvent;

/**
 * 项目名称：SitechVehiclePad-5th
 * 类名称：WindowEvent
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2020/04/24 0024 16:00
 * 修改时间：
 * 备注：
 */
public class WindowEvent extends BaseEvent {
    /**
     * 底部主菜单窗口
     */
    public static final String EVENT_WINDOW_MAIN_MENU = "EVENT_WINDOW_MAIN_MENU";
    /**
     * 控制菜单窗口
     */
    public static final String EVENT_WINDOW_CONTROL_MENU = "EVENT_WINDOW_CONTROL_MENU";
    /**
     * 右上角状态窗口
     */
    public static final String EVENT_WINDOW_STATUS_BAR_MENU = "EVENT_WINDOW_STATUS_BAR_MENU";
    /**
     * 隐藏所有窗口--APP切换到后台
     */
    public static final String EVENT_WINDOW_APP_BACKGROUD = "EVENT_WINDOW_APP_BACKGROUD";
    /**
     * 显示必须的所有窗口--APP切换到前台
     */
    public static final String EVENT_WINDOW_APP_FRONT = "EVENT_WINDOW_APP_FRONT";
    /**
     * INPUT_SHOW_STATE 隐藏所有窗口--APP主动隐藏，例如：弹出输入法
     */
    public static final String EVENT_WINDOW_INPUT_SHOW_STATE = "EVENT_WINDOW_INPUT_SHOW_STATE";
    /**
     * INPUT_HIDDEN_STATE 展示窗口--APP主动隐藏
     */
    public static final String EVENT_WINDOW_INPUT_HIDDEN_STATE = "EVENT_WINDOW_INPUT_HIDDEN_STATE";

    private String eventKey = "";
    private Object eventObject = null;

    public WindowEvent(String key) {
        eventKey = key;
    }

    public WindowEvent(String key, Object obj) {
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
