package com.sitechdev.vehicle.pad.event;

import com.sitechdev.vehicle.lib.event.BaseEvent;

public class KaolaEvent extends BaseEvent<String> {

    public KaolaEvent(String evt) {
        this.eventKey = evt;
    }

    /**
     * 发送Sys模块事件
     *
     * @param evt 事件类型
     * @param obj 事件参数
     */
    public KaolaEvent(String evt, Object obj) {
        this.eventKey = evt;
        this.mObj = obj;
    }

    public String getEvent() {
        return eventKey;
    }

    public Object getObj() {
        return mObj;
    }

    private Object mObj;


    public static final String EB_KAOLA_RESTORE_SEARCH_STATUS = "EB_Kaola_restore_Search_status";
}
