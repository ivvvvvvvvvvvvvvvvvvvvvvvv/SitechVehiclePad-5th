package com.sitechdev.vehicle.pad.event;

import com.sitechdev.vehicle.lib.event.BaseEvent;

/**
 * 项目名称：SitechVehiclePad-5th
 * 类名称：TeddyEvent
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2020/04/23 0023 18:52
 * 修改时间：
 * 备注：
 */
public class TeddyEvent extends BaseEvent {

    public static final String EVENT_TEDDY_KAOLA_PLAY_UPDATElIST = "EVENT_TEDDY_KAOLA_PLAY_UPDATElIST";

    private String eventKey = "";
    private Object eventValue = null;

    public TeddyEvent(String key) {
        eventKey = key;
    }

    public TeddyEvent(String tmpKey, Object tmpValue) {
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
