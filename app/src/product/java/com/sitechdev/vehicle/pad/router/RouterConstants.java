package com.sitechdev.vehicle.pad.router;

/**
 * Router页面常量
 *
 * @author liuhe
 */
public interface RouterConstants extends IRouterConstants {
    String TAG = "Router";

    /**
     * module页面
     */
    String MODULE_MAIN = "/m_main";

    /**
     * 主页面
     */
    String HOME_MAIN = MODULE_MAIN + LEVEL_HOME + "/mainPage";
    /**
     * 应用列表页面
     */
    String SETTING_APP_LIST = MODULE_MAIN + LEVEL_SUB + "/appList";

    //====================  个人中心组    =============================================
    /**
     * 组名--个人中心
     */
    String GROUP_MEMBER = "/g_member";
    /**
     * 个人中心页面
     */
    String SUB_APP_MEMBER = MODULE_MAIN + LEVEL_THIRD + GROUP_MEMBER + "/member";
    /**
     * 我的积分页面
     */
    String SUB_APP_MY_POINTS = MODULE_MAIN + LEVEL_THIRD + GROUP_MEMBER + "/MyPoints";
    //====================  个人中心组    =============================================

    //====================  车辆状态组    =============================================
    /**
     * 组名--车辆状态
     */
    String GROUP_CAR = "/g_car";
    /**
     * 车辆状态页面
     */
    String SUB_APP_CAR_STATUS = MODULE_MAIN + LEVEL_THIRD + GROUP_CAR + "/carStatus";
    //====================  车辆状态组    =============================================

    //====================  出行计价器组    =============================================
    /**
     * 组名--出行计价器
     */
    String GROUP_TAX = "/g_tax";
    /**
     * 出行计价器页面
     */
    String SUB_APP_TAXI = MODULE_MAIN + LEVEL_THIRD + GROUP_TAX + "/taxi";
    //====================  出行计价器组    =============================================


    //====================  设置组    =============================================
    /**
     * 组名--设置
     */
    String GROUP_SETTING = "/g_setting";
    /**
     * 皮肤设置页面
     */
    String SETTING_SKIN_PAGE = MODULE_MAIN + LEVEL_THIRD + GROUP_SETTING + "/skin";
    /**
     * 网络设置页面
     */
    String SETTING_NET_PAGE = MODULE_MAIN + LEVEL_THIRD + GROUP_SETTING + "/net";
    /**
     * 语音设置页面
     */
    String SETTING_TEDDY_PAGE = MODULE_MAIN + LEVEL_THIRD + GROUP_SETTING + "/teddy";
    /**
     * 系统设置页面
     */
    String SETTING_SYSTEM_PAGE = MODULE_MAIN + LEVEL_THIRD + GROUP_SETTING + "/system";
    /**
     * 设置主页面
     */
    String SETTING_HOME_PAGE = MODULE_MAIN + LEVEL_THIRD + GROUP_SETTING + "/home";
    /**
     * 蓝牙设置页面
     */
    String SETTING_BT_PAGE = MODULE_MAIN + LEVEL_THIRD + GROUP_SETTING + "/bt";
    //====================  设置组    =============================================

    //====================  导航组    =============================================
    /**
     * 组名-- 导航
     */
    String GROUP_NAVI = "/g_navi";
    /**
     * 设置家庭和公司地址页面
     */
    String SET_ADDRESS_PAGE = MODULE_MAIN + LEVEL_THIRD + GROUP_NAVI + "/address";
    //====================  导航组    =============================================

    //====================  本地音乐    =============================================
    /**
     * 组名-- 本地音乐
     */
    String GROUP_LOCALMUSIC = "/g_localMusic";
    /**
     * 在线音乐播放列表页面
     */
    String MUSIC_PLAY_SHOW = MODULE_MAIN + LEVEL_THIRD + GROUP_LOCALMUSIC + "/musicPlay";

    /**
     * 本地音乐
     */
    String FRAGMENT_LOCAL_MUSIC = MODULE_MAIN + LEVEL_THIRD + GROUP_LOCALMUSIC + "/localMusic";
    //====================  本地音乐    =============================================

    //====================  听伴    =============================================
    /**
     * 组名-- 听伴
     */
    String GROUP_KAOLAFM = "/g_kaolaFM";
    /**
     * 听伴首页
     */
    String MUSIC_PLAY_ONLINE_MAIN = MODULE_MAIN + LEVEL_THIRD + GROUP_KAOLAFM + "/musicPlayOnline";
    /**
     * 听伴专辑播放页
     */
    String MUSIC_PLAY_ONLINE = MODULE_MAIN + LEVEL_THIRD + GROUP_KAOLAFM + "/musicPlayOnlinePlaypage";
    /**
     * 听伴广播播放页
     */
    String MUSIC_PLAY_ONLINE_BROADCAST = MODULE_MAIN + LEVEL_THIRD + GROUP_KAOLAFM + "/musicPlayBroadcastOnline";
    //====================  听伴    =============================================

    //====================  视频    =============================================
    /**
     * 组名-- 视频
     */
    String GROUP_VIDEO = "/g_video";
    /**
     * 视频列表页
     */
    String VIDEO_LIST = MODULE_MAIN + LEVEL_THIRD + GROUP_VIDEO + "/videoList";
    //====================  听伴    =============================================

    //====================  天气    =============================================
    /**
     * 组名-- 天气
     */
    String GROUP_WEATHER = "/g_weather";
    /**
     * 天气
     */
    String SUB_APP_WEATHER = MODULE_MAIN + LEVEL_THIRD + GROUP_WEATHER + "/weather";
    //====================  天气    =============================================

    //====================  三方app跳转    =============================================
    /**
     * 组名-- 三方app
     */
    String GROUP_THIRD_APP = "/g_third_app";
    /**
     * 三方app跳转--地图
     */
    String THIRD_APP_MAP = MODULE_MAIN + LEVEL_THIRD + GROUP_THIRD_APP + "/naviApp";
    /**
     * 三方app跳转--酷我
     */
    String THIRD_APP_KUWO = MODULE_MAIN + LEVEL_THIRD + GROUP_THIRD_APP + "/netMusic";
    /**
     * 三方app跳转--百度carlife
     */
    String THIRD_APP_CARLIFE = MODULE_MAIN + LEVEL_THIRD + GROUP_THIRD_APP + "/carLife";
    //====================  三方app跳转    =============================================

    /**
     * 无网络
     */
    String NO_NET = "/main/noNet";

    //====================  商城    =============================================
    /**
     * 组名-- 电话界面
     */
    String GROUP_MALL = "/g_mall";
    /**
     * 商城
     */
    String MALL_SERVICE = MODULE_MAIN + LEVEL_THIRD + GROUP_MALL + "/mall";
    //====================  商城    =============================================

    //====================  电话界面    =============================================
    /**
     * 组名-- 电话界面
     */
    String GROUP_PHONE = "/g_phone";
    /**
     * 电话界面
     */
    String PHONE_MAIN_PAGE = MODULE_MAIN + LEVEL_THIRD + GROUP_PHONE + "/phone";
    //====================  电话界面    =============================================

    //====================  车辆控制    =============================================
    /**
     * 组名-- 车辆控制
     */
    String GROUP_CAR_CONTROL = "/g_car_control";
    /**
     * 车辆控制
     */
    String FRAGMENT_CAR_CONTROL = MODULE_MAIN + LEVEL_THIRD + GROUP_CAR_CONTROL + "/carControl";
    //====================  车辆控制    =============================================

    /**
     * 组名-- led
     */
    String GROUP_LED_MANAGER = "/_led_manager";
    /**
     * led管理界面
     */
    String SETTING_LED_MANAGER = MODULE_MAIN + LEVEL_THIRD + GROUP_LED_MANAGER + "/ledManager";
}
