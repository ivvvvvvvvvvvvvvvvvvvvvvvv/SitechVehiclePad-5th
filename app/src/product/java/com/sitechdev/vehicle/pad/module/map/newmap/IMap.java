package com.sitechdev.vehicle.pad.module.map.newmap;

import com.sitechdev.vehicle.pad.bean.PoiBean;

/**
 * @author 邵志
 * @version 2020/06/16 0016 10:06
 * @name IMap
 * @description
 */
public interface IMap {
    /**
     * 判断地图是否在前台
     */
    Boolean isForeground();

    /**
     * 初始化操作
     */
    void init();

    /**
     * 启动地图
     */
    void startMap();

    /**
     * auto最小化
     */
    void stopMap();

    /**
     * 取消导航
     */
    void stopNavigate();

    /**
     * 请求当前设置的家或公司信息
     *
     * @param extraType 类型 1 家； 2 公司
     */
    void getHomeOrCop(int extraType);


    /**
     * 回家/去公司（特殊点导航）
     *
     * @param destType 0 回家；1 回公司
     * @param autoNav  是否直接开始导航 0 是；1 否
     */
    void navToHomeOrCop(int destType, int autoNav);

    /**
     * 地图缩放
     *
     * @param operaType 0 放大地图； 1缩小地图
     */
    void zoomMap(int operaType);

    /**
     * 设置路线偏好
     *
     * @param routerPrefer 路线偏好
     *                     0（速度快）1（费用少）2（路程短）3 不走高速 4（躲避拥堵）5（不走高速且避免收费）
     *                     6（不走高速且躲避拥堵）7（躲避收费和拥堵）8（不走高速躲避收费和拥堵）
     *                     20（高速优先）24（高速优先且躲避拥堵）
     */
    void routePrefer(int routerPrefer);

    /**
     * 实时路况
     *
     * @param operaType 0 实时路况开；1实时路况关
     */
    void roadCondition(int operaType);

    /**
     * 切换地图视图
     *
     * @param operaType 0切换2d车上； 1切换2d北上；2切换3d车上支持
     */
    void switchViewMode(int operaType);

    /**
     * 设置昼夜模式
     *
     * @param modeType 昼夜模式类型 0:自动; 1：白天; 2：黑夜
     */
    void switchDayNightMode(int modeType);

    /**
     * 设置为家或公司
     *
     * @param extraType int 1：家 2：公司
     * @param copPoi    设置点的地址，address非必填
     */
    void setHomeOrCop(int extraType, PoiBean copPoi);

    /**
     * 收藏当前点
     */
    void collectPoi();

    /**
     * 模拟导航
     */
    void simulateNavi();

    /**
     * 导航到某地
     *
     * @param
     */
    void startNavigate(String name, double lat, double lon);

    /**
     * 查找附近的充电桩
     */
    void findChargingStation();

    /**
     * 查找附近poi
     *
     * @param poi  poi类型
     * @param city 查找城市 ex"北京"
     */
    void searchNearbyExtra(String poi, String city);

    /**
     * 通知导航声道占用
     *
     * @param occupy 占用声道
     */
    void requestMapAudioFocus(boolean occupy);

    /**
     * 点火后，第三方通过该接口发送 acc 消息
     *
     * @param accOn true acc on, false acc off
     */
    void notifyAccOn(boolean accOn);

    /**
     * 通知导航一些参数信息
     */
    void notifyNaviClient(String params);
}
