package com.sitechdev.vehicle.lib.event;

import com.blankj.utilcode.util.BusUtils;

/**
 * 项目名称：SitechVehiclePad-5th
 * 类名称：XtBusUtil
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2020/05/21 0021 17:37
 * 修改时间：
 * 备注：
 */
public class XtBusUtil {
    /**
     * 绑定 接受者
     *
     * @param subscriber subscriber
     */
    public static void register(Object subscriber) {
        BusUtils.register(subscriber);
    }

    /**
     * 解绑定
     *
     * @param subscriber subscriber
     */
    public static void unregister(Object subscriber) {
        BusUtils.unregister(subscriber);
    }

    /**
     * 发送消息(事件)
     *
     * @param tag tag
     */
    public static void postEvent(String tag) {
        postEvent(tag, null, false);
    }

    /**
     * 发送消息(粘性事件),不带参数
     *
     * @param tag tag
     */
    public static void postStickEvent(String tag) {
        postEvent(tag, null, true);
    }

    /**
     * 发送消息(事件)
     *
     * @param tag    tag
     * @param params params
     */
    public static void postEvent(String tag, Object params) {
        postEvent(tag, params, false);
    }

    /**
     * 发送消息(粘性事件),不带参数
     *
     * @param tag    tag
     * @param params params
     */
    public static void postStickEvent(String tag, Object params) {
        postEvent(tag, params, true);
    }

    /**
     * 最终的事件发送
     *
     * @param tag      tag
     * @param params   params
     * @param isSticky true=粘性事件
     */
    private static void postEvent(String tag, Object params, boolean isSticky) {
        if (isSticky) {
            BusUtils.postSticky(tag, params);
        } else {
            BusUtils.post(tag, params);
        }
    }

    /**
     * 移除粘性事件
     *
     * @param tag tag
     */
    public void removeStickyEvent(String tag) {
        BusUtils.removeSticky(tag);
    }
}
