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
     * 开始识别
     */
    public static final String EVENT_VOICE_START_SR = "EVENT_VOICE_START_SR";
    /**
     * 识别过程中的音量变化
     */
    public static final String EVENT_VOICE_SR_ING_VOLUME = "EVENT_VOICE_SR_ING_VOLUME";
    /**
     * 识别成功，TTS播报结果中
     */
    public static final String EVENT_VOICE_TTS_PLAYIING = "EVENT_VOICE_TTS_PLAYIING";
    /**
     * 识别成功，展示结果
     */
    public static final String EVENT_VOICE_SR_SUCCESS = "EVENT_VOICE_SR_SUCCESS";
    /**
     * 合成TTS音频播报
     */
    public static final String EVENT_VOICE_TTS_PLAY_TEXT = "EVENT_VOICE_TTS_PLAY_TEXT";
    /**
     * 结束识别
     */
    public static final String EVENT_VOICE_SR_OVER = "EVENT_VOICE_SR_OVER";
    /**
     * 停止/暂停语音
     */
    public static final String EVENT_VOICE_STOP_VOICE = "EVENT_VOICE_STOP_VOICE";
    /**
     * 启用语音
     */
    public static final String EVENT_VOICE_RESUME_VOICE = "EVENT_VOICE_RESUME_VOICE";
    /**
     * 上传词典事件
     */
    public static final String EVENT_VOICE_UPLOAD = "EVENT_VOICE_UPLOAD";
    /**
     * 开启/关闭语音唤醒
     */
    public static final String EVENT_VOICE_AUTO_MVW_SWITCH = "EVENT_VOICE_AUTO_MVW_SWITCH";

    /**
     * 语音发音人
     * true 男 ；false 女
     */
    public static final String EVENT_VOICE_SEX_SWITCH = "EVENT_VOICE_SEX_SWITCH";

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
