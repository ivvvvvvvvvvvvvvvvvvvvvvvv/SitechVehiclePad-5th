package com.sitechdev.vehicle.pad.module.setting;

/**
 * 设置  埋点名称
 *
 * @author bijingshuai
 * @date 2019/7/3
 */
public enum SettingTraceEnum {
    //声音
    SLIDE_SETV_VOLUME("slide_setv_volume","音量调节"),
    SLIDE_SETV_VOICE_VOLUME("slide_setv_voice_volume","语音音量调节"),
    BTN_SETV_BTN_SOUND("btn_setv_btn_sound","按键音效"),
    BTN_SETV_SPEED_SOUND("btn_setv_speed_sound","速度声音补偿"),
    PAGE_SETV_OUT(" page_setv_out","离开声音设置页面"),

    //显示
    PAGE_SETS_OUT("page_sets_out","离开显示设置界面"),
    SLIDE_SETS_BRIGHT("slide_sets_bright","显示亮度调节"),

    //WIFI
    BTN_SETW_HOT_ON("btn_setw_hot_on","热点开启"),
    BTN_SETW_HOT_OFF("btn_setw_hot_off","热点关闭"),
    BTN_SETW_WIFI_ON("btn_setw_wifi_on","wifi开启"),
    BTN_SETW_WIFI_TRY_CONNECT("btn_setw_wifi_try_connect","尝试连接wifi"),
    BTN_SETW_WIFI_LINK("btn_setw_wifi_link","连接wifi"),
    PAGE_SETW_OUT("page_setw_out","离开wifi界面"),

    //Teddy
    BTN_SETT_TEDDY("btn_sett_teddy","唤醒词"),
    BTN_SETT_TEDDY_YES("btn_sett_teddy_yes","修改唤醒词"),
    BTN_SETT_TALK_ON("btn_sett_talk_on","持续对话"),
    BTN_SETT_HELLO_ON("btn_sett_hello_on","开机问候语"),
    BTN_SETT_HELLO("btn_sett_hello","自定义开机问候语"),
    BTN_SETT_HELLO_YES("btn_sett_hello_yes","修改开机问候语"),
    BTN_SETT_WAKE_UP_ON("btn_sett_wake_up_on","允许语音唤醒"),
    BTN_SETT_VOICE("btn_sett_voice","发音人"),
    BTN_SETT_ICON("btn_sett_icon","语音图标隐藏"),
    BTN_SETT_MATE("btn_sett_mate","恢复语音设置"),
    PAGE_SETT_OUT("page_sett_out","离开Teddy界面"),

    //系统
    BTN_SETS_MATE_YES("btn_sets_mate_yes","确认恢复出厂设置"),
    BTN_SETS_CHECK("btn_sets_check","检查更新");

    private String point;
    private String des;

    SettingTraceEnum(String point, String des) {
        this.point = point;
        this.des = des;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}