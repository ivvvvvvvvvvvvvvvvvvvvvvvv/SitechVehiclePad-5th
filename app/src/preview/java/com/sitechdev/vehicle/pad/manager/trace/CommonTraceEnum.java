package com.sitechdev.vehicle.pad.manager.trace;

/**
 * 公共埋点信息
 *
 * @author liuhe
 * @date 2019/06/27
 */
public enum CommonTraceEnum {

    ACC_ON("entity_acc-on", "acc-on"),
    ACC_OFF("entity_acc-off", "acc-off"),
    POWER_ON("entity_power-on", "power-on"),
    POWER_OFF("entity_power-off", "power-off"),
    /**
     * teddy唤醒
     */
    TEDDY_NOTIFY("btn_teddy", "点击Teddy"),
    MENU_TEDDY("btn_hp_teddy", "点击menu侧Teddy"),
    /**
     * 侧边栏
     */
    MENU_HOME("btn_home", "点击返回主页"),
    MENU_ALL("btn_app", "点击全部页面");

    private String point;
    private String des;

    CommonTraceEnum(String point, String des) {
        this.point = point;
        this.des = des;
    }

    public String getPoint() {
        return point;
    }

    public void setName(String name) {
        this.point = name;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}