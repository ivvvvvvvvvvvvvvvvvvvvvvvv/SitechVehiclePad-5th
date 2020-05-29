package com.sitechdev.vehicle.pad.module.map.util;

import com.amap.api.services.core.PoiItem;

import java.util.ArrayList;

/**
 * 项目名称：SitechVehiclePad
 * 类名称：MapVariants
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2019/08/14 0014 14:51
 * 修改时间：
 * 备注：
 */
public class MapVariants {
    /**
     * true=开始导航，false=结束导航
     */
    public static boolean isMapNavi_Running = false;
    /**
     * true=导航已成功完成，到达目的地
     */
    public static boolean isMapNavi_Over = false;
    /**
     * 具有选择场景
     */
    public static boolean hasSelectListScene = false;

    public static ArrayList<PoiItem> mPoiList = new ArrayList<>();
}
