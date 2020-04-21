package com.sitechdev.vehicle.lib.event;

import org.greenrobot.eventbus.EventBus;

/**
 * @author liuhe
 */
public class EventBusUtils {

    /**
     * 绑定 接受者
     */
    public static void register(Object subscriber) {
        if (!EventBus.getDefault().isRegistered(subscriber)) {
            EventBus.getDefault().register(subscriber);
        }
    }

    /**
     * 解绑定
     */
    public static void unregister(Object subscriber) {
        if (EventBus.getDefault().isRegistered(subscriber)) {
            EventBus.getDefault().unregister(subscriber);
        }
    }

    /**
     * 发送消息(事件)
     */
    public static <T extends BaseEvent> void postEvent(T event) {
        EventBus.getDefault().post(event);
    }

    /**
     * 发送 粘性 事件
     * <p>
     * 粘性事件，在注册之前便把事件发生出去，等到注册之后便会收到最近发送的粘性事件（必须匹配）
     * 注意：只会接收到最近发送的一次粘性事件，之前的会接受不到。
     */
    public static <T extends BaseEvent> void postStickyEvent(T event) {
        EventBus.getDefault().postSticky(event);
    }

    /**
     * 注销黏性事件
     */
    public static <T extends BaseEvent> void removeStickyEvent(T event) {
        EventBus.getDefault().removeStickyEvent(event);
    }
}
