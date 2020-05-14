package com.sitechdev.vehicle.pad.event;

import com.sitechdev.vehicle.lib.event.BaseEvent;

/**
 * 项目名称：GA10-C
 * 类名称：KuwoEvent
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2019/04/18 0018 19:50
 * 修改时间：
 * 备注：
 */
public class KuwoEvent extends BaseEvent<String> {

    /**
     * 酷我启动
     */
    public static final String EB_KUWO_LANUCHER = "EB_KUWO_LANUCHER";
    /**
     * 酷我退出
     */
    public static final String EB_KUWO_EXIT = "EB_KUWO_EXIT";
    /**
     * 酷我-播放状态改变
     * 使用：
     * <code><br/>
     * ArrayMap<String, Object> kuwoMap = new ArrayMap<>();<br/>
     * kuwoMap.put("music", music);//Music music<br/>
     * kuwoMap.put("playStatus", playerStatus);//PlayerStatus playerStatus<br/>
     * EventBusUtils.postEvent(new KuwoEvent(KuwoEvent.EB_KUWO_PLAY_STATUS_CHANNGE, kuwoMap));<br/>
     * </code>
     */
    public static final String EB_KUWO_PLAY_STATUS_CHANNGE = "EB_KUWO_PLAY_STATUS_CHANNGE";

    /**
     * 酷我播放控制
     */
    public static final String EB_KUWO_PLAY_CONTROL = "EB_KUWO_PLAY_CONTROL";

    /**
     * 酷我播放控制--盘控控制
     */
    public static final String EB_KUWO_PLAY_KEY_CONTROL = "EB_KUWO_PLAY_KEY_CONTROL";

    /**
     * 酷我-播放进度通知
     */
    public static final String EB_KUWO_PLAY_MUSIC_POS = "EB_KUWO_PLAY_MUSIC_POS";
    /**
     * 酷我-获取到的小图片结果
     */
    public static final String EB_KUWO_MUSIC_BITMAP_RESULT = "EB_KUWO_MUSIC_BITMAP_RESULT";

    public Object kuwoBean;

    public KuwoEvent(String evt) {
        this.eventKey = evt;
        this.kuwoBean = null;
    }

    public KuwoEvent(String evt, Object kuwoBean) {
        this.eventKey = evt;
        this.kuwoBean = kuwoBean;
    }
    public enum PlayControl {
        /**
         * 播放
         */
        PLAY_PLAY,
        /**
         * 播放
         */
        PLAY_PAUSE,
        /**
         * 下一首
         */
        PLAY_NEXT,
        /**
         * 上一首
         */
        PLAY_PRE,
        /**
         * 循环播放
         */
        PLAY_MODE_ALL_CIRCLE,
        /**
         * 单曲循环
         */
        PLAY_MODE_SINGLE,
        /**
         * 顺序播放
         */
        PLAY_MODE_ALL_ORDER,
        /**
         * 随机播放
         */
        PLAY_MODE_RANDOM;

        PlayControl() {
        }
    }
}
