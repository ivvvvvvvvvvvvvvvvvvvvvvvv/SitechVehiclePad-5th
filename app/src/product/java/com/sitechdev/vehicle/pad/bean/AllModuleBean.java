package com.sitechdev.vehicle.pad.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 全部应用中的实体
 *
 * @author bijingshuai
 * @date 2019/4/12
 */
public class AllModuleBean implements Serializable {

    private static final long serialVersionUID = -15L;
    public List<ModuleBean> apps;
    /**
     * menu_version : 1
     * apps : [{"index":0,"appName":"天气","appIcon":1,"appRoute":"/subApp/needNet/weather","jumpType":0,"eventId":"btn_app_weather"},{"index":1,"appName":"出行计价器","appIcon":2,"appRoute":"/subApp/taxi","jumpType":0,"eventId":"btn_app_taxi"},{"index":2,"appName":"听伴","appIcon":3,"appRoute":"/subApp/musicPlayOnline?deepIndex=100","jumpType":0,"eventId":"btn_app_kaolaFM"},{"index":3,"appName":"本地音乐","appIcon":4,"appRoute":"/subApp/forShow/localMusic","jumpType":0,"eventId":"btn_app_local_music"},{"index":4,"appName":"个人中心","appIcon":5,"appRoute":"/needLogin/subApp/member","jumpType":0,"eventId":"btn_app_member"},{"index":5,"appName":"车辆状态","appIcon":6,"appRoute":"/subApp/carstatus","jumpType":0,"eventId":"btn_app_car_status"},{"index":6,"appName":"皮肤设置","appIcon":7,"appRoute":"/setting/skin","jumpType":0,"eventId":"btn_app_setting_skin"},{"index":7,"appName":"导航","appIcon":8,"appRoute":"/subApp/developing/navi","jumpType":1,"eventId":"btn_app_navi"},{"index":8,"appName":"网络音乐","appIcon":9,"appRoute":"/subApp/developing/needLogin/needNet/netMusic","jumpType":0,"eventId":"btn_app_net_music"},{"index":9,"appName":"LED表情管理","appIcon":10,"appRoute":"/subApp/developing/led","jumpType":0,"eventId":"btn_app_led"},{"index":10,"appName":"视频","appIcon":11,"appRoute":"/subApp/developing/video","jumpType":0,"eventId":"btn_app_video"},{"index":11,"appName":"电话","appIcon":12,"appRoute":"/subApp/developing/phone","jumpType":0,"eventId":"btn_app_phone"},{"index":12,"appName":"饭聊","appIcon":13,"appRoute":"/subApp/developing/funChat","jumpType":0,"eventId":"btn_app_funChat"},{"index":13,"appName":"商城","appIcon":14,"appRoute":"/needLogin/developing/needNet/mall","jumpType":0,"eventId":"btn_app_mall"}]
     */
    public int menu_version = -1;

    public class ModuleBean {
        /**
         * 应用名称
         */
        public String appId;
        /**
         * 应用名称
         */
        public int appIndex;
        /**
         * 应用名称
         */
        public String appName;
        /**
         * 应用icon
         */
        public int appIcon;
        /**
         * 跳转路径
         */
        public String appRoute;
        /**
         * 跳转标识
         * 0->跳转本地页面
         * 1->跳转三方应用，具体跳转页面根据appRoute判断
         * 2->window展示，eg:空调
         */
        public int jumpType;

        /**
         * 埋点id
         */
        public String eventId;
    }
}
