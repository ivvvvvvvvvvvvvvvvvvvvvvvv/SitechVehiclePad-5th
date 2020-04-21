package com.sitechdev.vehicle.pad.event;

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
public class VoiceEvent extends BaseEvent {

    /**
     * 唤醒成功
     */
    public static final String EVENT_VOICE_MVW_SUCCESS = "EVENT_VOICE_MVW_SUCCESS";
    /**
     * 合成TTS音频播报
     */
    public static final String EVENT_VOICE_TTS_PLAY_TEXT = "EVENT_VOICE_TTS_PLAY_TEXT";
    /**
     * 上传词典事件
     */
    public static final String EVENT_VOICE_UPLOAD = "EVENT_VOICE_UPLOAD";

    private String eventKey = "";
    private Object eventValue = null;

    public VoiceEvent(String key) {
        eventKey = key;
    }

    public VoiceEvent(String tmpKey, Object tmpValue) {
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
