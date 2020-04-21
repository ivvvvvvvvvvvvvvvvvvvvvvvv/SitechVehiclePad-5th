package com.sitechdev.vehicle.pad.vui;

/**
 * 项目名称：SitechVehiclePad
 * 类名称：VoiceConstants
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2019/08/26 0026 18:14
 * 修改时间：
 * 备注：
 */
public interface VoiceConstants {
    /**
     * TTS默认应答语
     */
    String TTS_RESPONSE_DEFAULT_TEXT = "我在呢";
    /**
     * TTS默认应答语
     */
    String TTS_RESPONSE_NAVI_TEXT = "请问您想去哪";

    /**
     * 导航相关的语音service定义
     */
    String VOICE_CUSTOM_SERVICE_NAVI="SITECHAI.sitechMapNavi";
    /**
     * 导航相关的语音intent--设置为家庭
     */
    String VOICE_CUSTOM_INTENT_NAVI_SETHOME="setHomeAddr";
    /**
     * 导航相关的语音intent--设置为公司
     */
    String VOICE_CUSTOM_INTENT_NAVI_SETWORK="setWorkAddr";
    /**
     * 导航相关的语音intent--开始导航
     */
    String VOICE_CUSTOM_INTENT_START_NAVI="sitechMap_Navi";
    /**
     * 导航相关的语音intent--关闭导航
     */
    String VOICE_CUSTOM_INTENT_CLOSE_NAVI="closeNavi";
}
