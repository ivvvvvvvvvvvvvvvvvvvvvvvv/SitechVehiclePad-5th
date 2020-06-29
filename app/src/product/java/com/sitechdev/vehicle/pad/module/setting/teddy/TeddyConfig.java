package com.sitechdev.vehicle.pad.module.setting.teddy;

import android.text.TextUtils;

import com.sitechdev.vehicle.lib.util.ParamsUtil;
import com.sitechdev.vehicle.lib.util.SPUtils;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.lib.util.StringUtils;
import com.sitechdev.vehicle.pad.app.AppApplication;
import com.sitechdev.vehicle.pad.vui.VoiceConstants;


/**
 * 项目名称：HZ_SitechDOS
 * 类名称：TeddyConfig
 * 类描述：
 * 创建人：shaozhi
 * 创建时间：18-6-22 下午4:02
 * 修改时间：
 * 备注： 提供给设置页面做配置语音选项使用
 */
public class TeddyConfig {
    /**
     * 识别错误最大次数
     */
    public static volatile int maxErrorCount = 0;
    /**
     * 当前识别次数索引
     */
    public static int curErrorCount = 0;

    /**
     * 是否启用自定义语音
     */
    public static boolean isEnableDiyMvw = true;

    /**
     * 当前是否为收音机频道
     */
    public static boolean isRadioChannel = false;
    /**
     * 当前识别是否正在进行中
     */
    public static volatile boolean isTeddyWorking = false;
    /**
     * 当前Activity是否正在最前
     */
    public static boolean isTopActivity = true;
//    /**
//     * 是否开启语音功能。
//     * true==启用语音
//     * false=禁用语音。包括：倒车、打电话、系统更新中
//     */
//    public static boolean enableTeddyVoice = true;
    /**
     * 是否语音资源全部初始化成功
     */
    public static volatile boolean isTeddyInitOver = false;
    /**
     * 是否播放语音欢迎提示
     */
    public static boolean isPlayTtsWelcomeText = true;
    /**
     * 当前carlife music正在进行
     */
    public static boolean isCarlifeMusic = false;
    /**
     * 最终的语音唤醒词
     */
    public static String finalMvwText = "";

    /**
     * 语音功能无法启用的错误标识
     */
    public static TEDDY_ERROR_TYPE teddyErrorType = TEDDY_ERROR_TYPE.NORMAL;

    /**
     * 是否允许语音被声音唤醒。true=允许
     */
    public static boolean isEnableTeddyWakeUp = true;

    /**
     * 语音功能错误类型
     */
    public enum TEDDY_ERROR_TYPE {
        /**
         * 功能正常
         */
        NORMAL,
        /**
         * 初始化未完成
         */
        NO_INIT_OVER,
        /**
         * 屏幕关闭
         */
        POWER_SCREEN_OFF,
        /**
         * acc-off
         */
        ACC_OFF,
        /**
         * 系统更新中
         */
        SYSTEM_UPDATING,
        /**
         * 饭聊语音通话中
         */
        FUNCHATING,
        /**
         * 电话通话中
         */
        CALLING,
        /**
         * 倒车中
         */
        BACKCAM_ON,
        /**
         * 高德提示框显示时，不展示语音
         */
        GAODE_TIP,
        /**
         * 外部设备占用语音，不展示语音
         */
        MIC_BY_OTHER_DEVICE;
    }

    /**
     * 获取当前自动唤醒开关
     *
     * @return true==当前为开启状态
     */
    public static boolean getAutoMVWStatus() {
        String isAutoMvwStatus = ParamsUtil.getStringData(TeddyConstants.TEDDY_SPKEY_MVW_AUTOOPEN);
        SitechDevLog.i(VoiceConstants.TEDDY_TAG, "初始唤醒开关状态==>" + isAutoMvwStatus);
        if (StringUtils.isEmpty(isAutoMvwStatus)) {
            isEnableTeddyWakeUp = true;
        } else {
            isEnableTeddyWakeUp = Boolean.parseBoolean(isAutoMvwStatus);
        }
        SitechDevLog.i(VoiceConstants.TEDDY_TAG, "唤醒开关状态==>" + isEnableTeddyWakeUp);
        return isEnableTeddyWakeUp;
    }

    /**
     * 保存当前自动唤醒开关状态
     *
     * @return true==当前为开启状态
     */
    public static void setAutoMvwStatus(boolean autoMvw) {
        isEnableTeddyWakeUp = autoMvw;
        SitechDevLog.i(VoiceConstants.TEDDY_TAG, "重置唤醒开关状态==>" + isEnableTeddyWakeUp);
        ParamsUtil.setData(TeddyConstants.TEDDY_SPKEY_MVW_AUTOOPEN, String.valueOf(autoMvw));
    }

    /**
     * 重设唤醒词
     *
     * @param keyWords 新唤醒词
     */
    public static void saveResetMVWKeywords(String keyWords) {
        saveTeddyConfig(TeddyConstants.TEDDY_SPKEY_MVW_KEYWORDS, keyWords);
    }

    /**
     * 获取默认唤醒词的字符串表达式
     *
     * @return key1#key2#key3#
     */
    public static String getDefaultMVWKeywordStr(String selfKeyword) {
        StringBuilder sbu = new StringBuilder();
        for (String key : TeddyConstants.MVW_DEFAULT_KEYWORDS_LIST) {
            if (TextUtils.isEmpty(selfKeyword) || !selfKeyword.equals(key)) {
                sbu.append(key).append("#");
            }
        }
        String keywords = sbu.toString();
        if (!TextUtils.isEmpty(keywords) && keywords.endsWith("#")) {
            keywords = keywords.substring(0, keywords.length() - 1);
        }
        return keywords;
    }

    /**
     * 保存发音人--男声/女声
     *
     * @param isMan true=男声；false=女声
     */
    public static void saveTtsSpeaker(boolean isMan) {
        //0=男声，1=女声
        saveTeddyConfig(TeddyConstants.TEDDY_SPKEY_TTS_SPEAKER, isMan ? "0" : "1");
    }

    /**
     * 获取发音人--男声/女声
     *
     * @return true=男声；false=女声
     */
    public static boolean getTTsSpeaker() {
        //0=男声，1=女声
        String isManSpeakerValue = getTeddyConfig(TeddyConstants.TEDDY_SPKEY_TTS_SPEAKER, "1");
        return "0".equals(isManSpeakerValue);
    }

    public static void saveTTSSpeaker(String speaker) {
        saveTeddyConfig(TeddyConstants.TEDDY_CONFIG_TTS_SPEAKER, speaker);
    }

    public static String getTTSSpeaker() {
        return getTeddyConfig(TeddyConstants.TEDDY_CONFIG_TTS_SPEAKER, "jiajia");
    }

    public static void saveTTSSpeed(String speed) {
        saveTeddyConfig(TeddyConstants.TEDDY_CONFIG_TTS_SPEED, speed);
    }

    public static String getTTSSpeed() {
        return getTeddyConfig(TeddyConstants.TEDDY_CONFIG_TTS_SPEED, "0");
    }


    /**
     * 获取当前唤醒词
     */
    public static String getTeddyConfig(String key, String defaultValue) {
        return (String) SPUtils.getData(AppApplication.getContext(), key, defaultValue);
    }

    /**
     * 保存当前唤醒词
     */
    public static void saveTeddyConfig(String key, String keyWords) {
        SPUtils.setData(AppApplication.getContext(), key, keyWords);
    }

    /**
     * Teddy图标隐藏开关是否ON/OFF
     *
     * @return 0-true=开关关闭-OFF，展示Teddy图标；1-false=开关打开-ON，隐藏Teddy图标
     */
    public static boolean isShowTeddyIcon() {
        return SPUtils.getValue(AppApplication.getContext(), TeddyConstants.TEDDY_ICON_SHOW, true);
    }

    /**
     * 设置Teddy图标隐藏开关是否ON/OFF
     *
     * @param isShow 0-true=开关关闭-OFF，展示Teddy图标；1-false=开关打开-ON，隐藏Teddy图标
     */
    public static void setTeddyIconVisible(boolean isShow) {
        SPUtils.putValue(AppApplication.getContext(), TeddyConstants.TEDDY_ICON_SHOW, isShow);
    }
}
