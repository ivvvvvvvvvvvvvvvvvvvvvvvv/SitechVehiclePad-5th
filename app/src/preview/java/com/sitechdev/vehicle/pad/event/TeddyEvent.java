package com.sitechdev.vehicle.pad.event;

import com.sitechdev.vehicle.lib.event.BaseEvent;

/**
 * 项目名称：SitechVehiclePad-5th
 * 类名称：TeddyEvent
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2020/04/23 0023 18:52
 * 修改时间：
 * 备注：
 */
public class TeddyEvent extends BaseEvent {

    public static final String EVENT_TEDDY_KAOLA_PLAY_UPDATElIST = "EVENT_TEDDY_KAOLA_PLAY_UPDATElIST";
    public static final String EVENT_TEDDY_AUDIO_STOP = "EVENT_TEDDY_AUDIO_STOP";
    /**
     * 识别流程结束--终止识别流程
     */
    public static final String EB_TEDDY_EVENT_SR_OVER = "EB_EVENT_TEDDY_SR_OVER";
    /**
     * teddy事件--释放音频焦点-结束
     */
    public static final String EB_TEDDY_RELEASE_VOICE_FOCUS_OVER = "EB_TEDDY_RELEASE_VOICE_FOCUS_OVER";
    /**
     * 更新设置-teddy设置-自定义开机问候语
     */
    public static final String EB_TEDDY_EVENT_SETTING_WELCOME = "EB_TEDDY_EVENT_SETTING_WELCOME";

    /**
     * 在Teddy设置界面唤醒成功，启动识别
     */
    public static final String EB_TEDDY_EVENT_SETTING_TEDDY_MVW_SUCCESS = "EB_TEDDY_EVENT_SETTING_TEDDY_MVW_SUCCESS";


    private String eventKey = "";
    private Object eventValue = null;

    public TeddyEvent(String key) {
        eventKey = key;
    }

    public TeddyEvent(String tmpKey, Object tmpValue) {
        eventKey = tmpKey;
        eventValue = tmpValue;
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
