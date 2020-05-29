package com.sitechdev.vehicle.pad.manager.trace;

/**
 * 埋点上传参数的key
 *
 * @author bijingshuai
 * @date 2019/7/8
 */
public class TraceConstants {

    //多媒体
    public static final String USB_STATE_CHANGED = "usbStateChanged";       //埋点USB是否改变的状态
    public static final String USB_STATE = "usbState";                      //埋点USB状态  true连接 false未连接
    public static final String BLUE_STATE_CHANGED = "blueStateChaneged";    // 蓝牙状态是否改变
    public static final String VIDEO_CAR_STATE = "videoCarState";           // 车辆状态

    //首页
    public static final String BLUE_STATE = "blueState";                    //蓝牙是否连接  true连接 false未连接
    public static final String TRACE_CURRENT_SOURCE = "CurrentSource";      //当前音源
    public static final String TRACE_SOURCE_USB = "USB";                    //音源-USB
    public static final String TRACE_SOURCE_BLUETOOTH = "Bluetooth";        //音源-蓝牙
    public static final String TRACE_SOURCE_RADIO = "Radio";                //音源-收音机
    public static final String TRACE_SOURCE_KRADIO = "kradio";              //音源-考拉
    public static final String TRACE_SOURCE_KUWO = "Kuwo";                  //音源-酷我

    //空调控制
    public static final String DAY_TEMP = "day_temp";                       //当前天气温度
    public static final String TEMP = "temp";                               //空调-温度
    public static final String WINDSPEED = "windspeed";                     //空调-风速
    public static final String AC = "AC";                                   //空调-AC
    public static final String ACMAX = "ACmax";                             //空调-ACMax
    public static final String WIND = "wind";                               //空调-角度
    public static final String FROST = "frost";                             //空调-前除霜
    public static final String REAR = "rear";                               //空调-后除霜
    public static final String CYCLE = "cycle";                             //空调-循环模式

    //车辆控制
    public static final String BATTERY = "battery";                         //动力电池模式
    public static final String AIR = "air";                                 //空调功率模式
    public static final String KERS = "kers";                               //动能回收
    public static final String SPEED_LIMIT = "speed_limit";                 //车速限制
    public static final String LOCK = "lock";                               //锁车反应
    public static final String CAR_VIDEO = "car_video";                     //行车视频限制

    // 电话
    public static final String LOAD_CONTACT_STATUS = "loadContactSuccess"; // 通讯录加载

    //天气
    public static final String WEATHER_WEATHER = "weather";                 // 当前天气信息
    public static final String WEATHER_SUCCESS = "success";                 // 是否刷新成功
    public static final String WEATHER_CITY = "city";                       // 当前城市
    public static final String WEATHER_LONGITUDE = "longitude";             // 经度
    public static final String WEATHER_LATITUDE = "latitude";               // 纬度

    //个人中心
    public static final String PERSON_SIGN_IN = "sign_in";                  // 签到
    public static final String PERSON_INTEGRAL = "integral";                // 积分数量
    public static final String PERSON_CAR = "car";                          // 是否有车

    //商城
    public static final String MALL_NAME = "name";                          // 商品名称
    public static final String MALL_TYPE = "type";                          // 商品分类
    public static final String MALL_INTPARKING = "intParking";              // 车辆当前的行车状态
    public static final String MALL_NETRSSI = "netRssi";                    // Tbox的网络状态

    //蓝牙
    public static final String SETTING_OPENBT = "openBt";                    // 蓝牙开启状态
    public static final String SETTING_ISFIND = "isFind";                    // 蓝牙可被发现
    public static final String SETTING_ISCONNECT = "isConnect";              // 蓝牙是否配对成功
    public static final String SETTING_CLICKLIST = "clickList";              // 点击了设备列表

    //设置-系统
    public static final String SETTING_ISCHECK = "isCheck";                  // 是否有更新

    //设置-wifi
    public static final String SETTING_WIFI_SUCCESS = "wifi_success";        // wifi是否连接成功
    public static final String SETTING_WIFI_HOT = "hot";                     // 热点是否开启
    public static final String SETTING_WIFI_WIFI = "wifi";                   // WiFi是否开启

    //设置-显示
    public static final String SETTING_BRIGHT = "bright";                    // 显示-调节后的亮度

    //设置-声音
    public static final String SETTING_VOL_VOLUME = "volume";                // 音量
    public static final String SETTING_VOL_VOICE_VOLUME = "voice_volume";    // 语音音量
    public static final String SETTING_VOL_BTN_SOUND = "btn_sound";          // 按键音效
    public static final String SETTING_VOL_SPEED_SOUND = "speed_sound";      // 速度音量补偿
    public static final String SETTING_VOL_SOUND = "sound";                  // 音效

    //设置-teddy
    public static final String SETTING_TED_TALK = "talk";                    // 持续对话开关
    public static final String SETTING_TED_HELLO_ON = "hello_on";            // 开机问候语
    public static final String SETTING_TED_WAKE_UP = "wake_up";              // 是否允许语音唤醒
    public static final String SETTING_TED_VOICE = "voice";                  // 发音人
    public static final String SETTING_TED_ICON = "icon";                    // 图标隐藏


}
