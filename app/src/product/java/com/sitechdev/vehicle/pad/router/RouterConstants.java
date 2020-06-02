package com.sitechdev.vehicle.pad.router;

/**
 * Router页面常量
 *
 * @author liuhe
 */
public interface RouterConstants {
    String TAG = "Router";

    String HOME_MAIN = "/home/main";

    String HOME_SECONDARY = "/home/secondary";

    /**
     * 个人中心页面
     */
    String SUB_APP_MEMBER = "/subApp/member";
    /**
     * 我的积分页面
     */
    String SUB_APP_MY_POINTS = "/subApp/MyPoints";
    /**
     * 车辆状态页面
     */
    String SUB_APP_CAR_STATUS = "/subApp/carstatus";
    /**
     * 出行计价器页面
     */
    String SUB_APP_TAXI = "/subApp/taxi";
    /**
     * 模式设置页面
     */
    String SETTING_SKIN_PAGE = "/setting/skin";
    /**
     * 网络设置页面
     */
    String SETTING_NET_PAGE = "/setting/net";
    /**
     * 系统设置页面
     */
    String SETTING_SYSTEM_PAGE = "/setting/system";
    /**
     * 设置主页面
     */
    String SETTING_HOME_PAGE = "/setting/home";
    /**
     * 蓝牙设置页面
     */
    String SETTING_BT_PAGE = "/setting/bt";


    /**
     * 应用列表页面
     */
    String SETTING_APP_LIST = "/subApp/appList";
    /**
     * 在线音乐播放列表页面
     */
    String MUSIC_PLAY_SHOW = "/subApp/musicPlay";
    String MUSIC_PLAY_ONLINE = "/subApp/musicPlayOnline";
    String MUSIC_PLAY_ONLINE_BROADCAST = "/subApp/musicPlayBroadcastOnline";
    /**
     * 设置
     */
    String FRAGMENT_SETTING = "/main/setting";

    /**
     * 本地音乐
     */
    String FRAGMENT_LOCAL_MUSIC = "/main/localMusic";

    /**
     * 视频
     */
    String FRAGMENT_VIDEO = "/main/video";

    /**
     * 电话
     */
    String FRAGMENT_PHONE = "/bt/phone";
    /**
     * 蓝牙配对
     */
    String FRAGMENT_BT_PAIR = "/bt/pair";

    /**
     * 车辆控制
     */
    String FRAGMENT_CAR_CONTROL = "/main/carControl";

    /**
     * 天气
     */
    String SUB_APP_WEATHER = "/subApp/weather";

    /**
     * 空调控制
     */
    String FRAGMENT_AIR_CONTROL = "/main/airControl";
    /**
     * 图片
     */
    String FRAGMENT_PICTURE = "/main/picture";

    /**
     * 三方app跳转
     */
    String THIRD_APP_MAP = "/main/map";
    String THIRD_APP_CARLIFE = "/main/carLife";

    /**
     * 登录组
     */
    String LOGIN_GROUP = "needLogin";
    /**
     * 消息盒子
     */
    String FRAGMENT_MESSAGE_BOX = "/needLogin/needNet/messageBox";
    /**
     * 个人中心
     */
    String FRAGMENT_PERSONAL = "/needLogin/needNet/personal";
    /**
     * 饭聊
     */
    String FRAGMENT_FUNCHAT = "/needLogin/needNet/funchat";

    /**
     * 用户反馈
     */
    String FRAGMENT_FEEDBACK = "/needLogin/needNet/feedback";

    /**
     * 商城
     */
    String FRAGMENT_MALL = "/needLogin/needNet/mall";
    /**
     * 商城搜索
     */
    String FRAGMENT_MALL_SEARCH = "/needLogin/needNet/mallSearch";

    /**
     * 考拉 FM
     */
    String FRAGMENT_KAOLA_RADIO = "/needLogin/needNet/kaolaRadio";
    String KAOLA_RADIO_LIST = "/needLogin/needNet/kaolaRadioList";
    String KAOLA_RADIO_PLAY = "/needLogin/needNet/kaolaRadioPlay";

    /**
     * 酷我
     */
    String THIRD_APP_KUWO = "/needLogin/needNet/kuwo";

    /**
     * 无网络
     */
    String NO_NET = "/main/noNet";


    String MALL_SERVICE = "/mall_service/mall";

}
