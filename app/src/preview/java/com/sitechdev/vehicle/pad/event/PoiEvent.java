package com.sitechdev.vehicle.pad.event;

import com.sitechdev.vehicle.lib.event.BaseEvent;

/**
 * 项目名称：SitechVehiclePad
 * 类名称：PoiEvent
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2019/08/14 0014 16:47
 * 修改时间：
 * 备注：
 */
public class PoiEvent extends BaseEvent {
    /**
     * 查找指定的POI类型
     */
    public static final String EVENT_QUERY_POI_KEYWORD = "EVENT_QUERY_POI_KEYWORD";
    /**
     * 查找附近的POI类型
     */
    public static final String EVENT_QUERY_NEARBY_POI_KEYWORD = "EVENT_QUERY_NEARBY_POI_KEYWORD";
    /**
     * 查找指定的POI--索引
     */
    public static final String EVENT_QUERY_POI_INDEX = "EVENT_QUERY_POI_INDEX";

    private String eventKey = "";
    private String eventValue = "";
    private String eventValue2 = "";

    public PoiEvent(String tmpKey, String tmpValue) {
        eventKey = tmpKey;
        eventValue = tmpValue;
        eventValue2 = "";
    }

    public PoiEvent(String tmpKey, String tmpValue, String tmpValue2) {
        eventKey = tmpKey;
        eventValue = tmpValue;
        eventValue2 = tmpValue2;
    }

    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    public String getEventValue() {
        return eventValue;
    }

    public String getEventValue2() {
        return eventValue2;
    }

    @Override
    public String toString() {
        StringBuilder sbu = new StringBuilder();
        sbu.append("[ eventKey = ").append(eventKey).append(",");
        sbu.append(" eventValue = ").append(eventValue).append(",");
        sbu.append(" eventValue2 = ").append(eventValue2).append(" ]");
        return sbu.toString();
    }
}
