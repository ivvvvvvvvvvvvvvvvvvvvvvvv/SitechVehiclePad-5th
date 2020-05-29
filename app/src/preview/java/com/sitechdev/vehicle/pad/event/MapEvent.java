package com.sitechdev.vehicle.pad.event;

import com.sitechdev.vehicle.lib.event.BaseEvent;

/**
 * 项目名称：SitechVehiclePad
 * 类名称：MapEvent
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2019/08/15 0014 13:48
 * 修改时间：
 * 备注：
 */
public class MapEvent extends BaseEvent {
    /**
     * 打开地图页面
     */
    public static final String EVENT_OPEN_MAP = "EVENT_OPEN_MAP";
    /**
     * 定位成功，通知各业务刷新
     */
    public static final String EVENT_LOCATION_SUCCESS = "EVENT_LOCATION_SUCCESS";
    /**
     * 地图事件--语音定位marker
     */
    public static final String EVENT_OPEN_MAP_SHOW_POI = "EVENT_OPEN_MAP_SHOW_POI";
    /**
     * 地图设置事件功能--设置为家/公司
     */
    public static final String EVENT_SET_MAP = "EVENT_SET_MAP";
    /**
     * 开始导航
     * new MapEvent(MapEvent.EVENT_MAP_START_NAVI,new LatLng(经度,纬度));
     */
    public static final String EVENT_MAP_START_NAVI = "EVENT_MAP_START_NAVI";
    /**
     * 开始导航
     * new MapEvent(MapEvent.EVENT_MAP_START_NAVI,new LatLng(经度,纬度));
     */
    public static final String EVENT_MAP_CLOSE_NAVI = "EVENT_MAP_CLOSE_NAVI";
    /**
     * 开始导航
     * new MapEvent(MapEvent.EVENT_MAP_START_NAVI,new LatLng(经度,纬度));
     */
    public static final String EVENT_MAP_START_NAVI_HOME = "EVENT_MAP_START_NAVI_HOME";
    /**
     * 开始导航
     * new MapEvent(MapEvent.EVENT_MAP_START_NAVI,locationData.componyaddress);
     */
    public static final String EVENT_MAP_START_NAVI_COMPONY = "EVENT_MAP_START_NAVI_COMPONY";
    /**
     * 设置为家
     */
    public static final String EVENT_MAP_NAVI_SET_HOME_ADDR = "EVENT_MAP_NAVI_SET_HOME_ADDR";
    /**
     * 设置为公司
     */
    public static final String EVENT_MAP_NAVI_SET_WORK_ADDR = "EVENT_MAP_NAVI_SET_WORK_ADDR";

    private String eventKey = "";
    private Object eventValue = null;
    private String eventValue2 = "";

    public MapEvent(String tmpKey) {
        eventKey = tmpKey;
        eventValue = "";
        eventValue2 = "";
    }

    public MapEvent(String tmpKey, Object tmpValue) {
        eventKey = tmpKey;
        eventValue = tmpValue;
        eventValue2 = "";
    }

    public MapEvent(String tmpKey, String tmpValue, String tmpValue2) {
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

    public Object getEventValue() {
        return eventValue;
    }

    public String getEventValue2() {
        return eventValue2;
    }

    @Override
    public String toString() {
        StringBuilder sbu = new StringBuilder();
        sbu.append("[ eventKey = ").append(eventKey).append(",");
        sbu.append(" eventValue = ").append(eventValue).append(" ]");
        return sbu.toString();
    }
}
