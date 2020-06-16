package com.sitechdev.vehicle.pad.bean;

import java.io.Serializable;

/**
 * poi点实体类
 *
 * @author liuhe
 */
public class PoiBean implements Serializable {
    /**
     * 高德地图需要的一些字段
     */
    public double lon;
    public double lat;
    public String address;
    public String poiName;

    /**
     * 兼容send2car
     */
    public String name;
}
