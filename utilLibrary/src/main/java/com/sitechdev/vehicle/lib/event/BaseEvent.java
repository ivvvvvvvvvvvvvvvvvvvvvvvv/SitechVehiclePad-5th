package com.sitechdev.vehicle.lib.event;

/**
 * EventBus事件基类
 *
 * @author liuhe
 * @date 2019/6/27
 */
public abstract class BaseEvent<T> {
    /**
     * 事件key
     */
    public T eventKey;
}
