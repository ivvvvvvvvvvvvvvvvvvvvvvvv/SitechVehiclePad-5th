package com.sitechdev.vehicle.pad.bean;

import java.io.Serializable;

/**
 * 用户默认个性化设置
 *
 * @author bijingshuai
 * @date 2019/6/26
 */
public class PersonalDefaultBean implements Serializable {

    /**
     * code : 200
     * message :
     * data : {"welcome":"欢迎您","sex":"男","ac_time":30,"fault_switch":0}
     */

    public String code;
    public String message;
    public PersonalBean data;

    public static class PersonalBean implements Serializable {
        /**
         * 系统音量：0-30
         */
        public int hu_vol_sys;

        /**
         * teddy音量：0-30
         */
        public int hu_vol_teddy;

        /**
         * 按键音：true-开-1  false-关-0
         */
        public int hu_vol_click;

        /**
         * 速度音量补偿:0-OFF  1-低  2-中  3-高
         */
        public int hu_vol_cpsate;

        /**
         * 音效选择:0-标准 1-流行 2-摇滚 3-古典 4-爵士 5-说唱
         */
        public int hu_vol_type;

        /**
         * 亮度：1-10
         */
        public int hu_luminance;

        /**
         * teddy唤醒词
         */
        public String ted_wake_up_w;

        /**
         * 允许唤醒：false-允许唤醒-0  true-禁止唤醒-1
         */
        public int hu_ted_wake_up;

        /**
         * 开启开机问候语：false-开启开机问候语  true-禁止开机问候语
         */
        public int hu_ted_welcome;

        /**
         * 自定义开机问候语
         */
        public String welcome;

        /**
         * 发音人：false-女-0  true-男-1
         */
        public String sex;

        /**
         * 语音图标是否隐藏：true-隐藏-1  false-不隐藏-0
         */
        public int hu_ted_icon;

        /**
         * 空调时间
         */
        public int ac_time;
        /**
         * 是否开启持续对话：true-开启-1  false-关闭-0
         */
        public int hu_ted_talk;
        /**
         * Teddy 语言播报发音人
         */
        public String hu_ted_speaker;
        /**
         * Teddy 语音播报语速
         */
        public String hu_ted_speed;


        /**
         * 默认设置
         */
        public void reset() {
//            hu_vol_sys = 12;
//            hu_vol_teddy = 12;
//            hu_luminance = 8;
            ted_wake_up_w = "你好Teddy";
            welcome = "NULL";
            hu_ted_wake_up = 0;
            hu_ted_welcome = 0;
            hu_vol_click = 1;
            hu_vol_cpsate = 2;
            hu_vol_type = 0;
            sex = "女";
            hu_ted_icon = 0;
            hu_ted_talk = 0;
            hu_ted_speaker = "jiajia";
            hu_ted_speed = "0";
        }

        @Override
        public String toString() {
            return "PersonalBean{" +
                    "hu_vol_sys=" + hu_vol_sys +
                    ", hu_vol_teddy=" + hu_vol_teddy +
                    ", hu_vol_click=" + hu_vol_click +
                    ", hu_vol_cpsate=" + hu_vol_cpsate +
                    ", hu_vol_type=" + hu_vol_type +
                    ", hu_luminance=" + hu_luminance +
                    ", ted_wake_up_w='" + ted_wake_up_w + '\'' +
                    ", hu_ted_wake_up=" + hu_ted_wake_up +
                    ", hu_ted_welcome=" + hu_ted_welcome +
                    ", welcome='" + welcome + '\'' +
                    ", sex='" + sex + '\'' +
                    ", hu_ted_icon=" + hu_ted_icon +
                    ", ac_time=" + ac_time +
                    ", hu_ted_talk=" + hu_ted_talk +
                    ", hu_ted_speaker='" + hu_ted_speaker + '\'' +
                    ", hu_ted_speed='" + hu_ted_speed + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "PersonalDefaultBean{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", data=" + (data == null ? "NULL" : data.toString()) +
                '}';
    }
}
