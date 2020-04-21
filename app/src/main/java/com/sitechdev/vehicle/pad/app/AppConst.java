package com.sitechdev.vehicle.pad.app;

/**
 * 项目名称：SitechVehiclePad
 * 类名称：AppConst
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2019/08/12 0012 14:21
 * 修改时间：
 * 备注：
 */
public interface AppConst {
    String TAG = "SitechVehicleLog";

    /**
     * poi检索关键字key
     */
    String DEVICE_SOURCE = "native_pad";
    /**
     * poi检索关键字key
     */
    String POI_QUERY_KEYWORD = "poiKeyword";
    /**
     * poi检索关键字key
     */
    String POI_DATA = "poiData";
    //地图平台账号
    /**
     * 高德地图key
     */
    String AMAP_APPKEY = "d022b05db57b4566ea7507d12cf5c1d2";
    /**
     * 百度地图key--AK
     */
    String BAIDU_MAP_APPKEY = "TpZ9GVHPNBXgPfLqtnxI1FZElU9GOCI6";
    /**
     * 腾讯地图key
     */
    String TECENT_MAP_APPKEY = "PUKBZ-OWMKW-45ZRG-O3DY6-X6UKK-XSB7O";
    /**
     * 高德地图包名
     */
//    String AMAP_PACKNAME = "com.autonavi.minimap";
    /**
     * 高德地图HD包名
     */
    String AMAP_HD_PACKNAME = "com.autonavi.amapauto";
    /**
     * 百度地图包名
     */
//    String BAIDU_MAP_PACKNAME = "com.baidu.BaiduMap";
    /**
     * 百度地图包名
     */
    String BAIDU_MAP_HD_PACKNAME = "com.baidu.BaiduMap.auto";
    /**
     * 腾讯地图包名
     */
    String TECENT_MAP_PACKNAME = "com.tencent.wecarnavi";
    /**
     * 导航地图选择
     */
    String[] NAVI_MAP_ARRAYS = new String[]{"高德地图", "百度地图", "腾讯地图"};

    /**
     * Activity结果回调
     */
    int REQUEST_RESULT_CODE = 10000;

    //权限申请相关
    /**
     * 权限申请回调Code
     */
    int PERMISSION_REQ_CODE = 102;
//    /**
//     * SD卡读写权限
//     */
//    int PERMISSION_REQ_SD_CODE = 102;
//    /**
//     * 读取手机状态权限
//     */
//    int PERMISSION_PHONE_STATE = 103;
//    /**
//     * 定位权限
//     */
//    int PERMISSION_LOCATION_STATE = 104;


    /**
     * 用户信息保存KEY-对“UserInfo”进行MD5加密
     */
    String KEY_USER = "21D23644BAA10E1EBA75FDD29E179288";

    /**
     * 验证码类型---登录
     */
    String VALID_TYPE_APP_LOGIN = "SID001";

    /**
     * 设置地址 家庭/公司 类型
     */
    String ADDRESS_SET_TYPE = "ADDRESS_SET_TYPE";

    /**
     * 设置地址 家庭/公司 类型
     */
    String MAP_POI_SHOW_TYPE = "MAP_POI_SHOW_TYPE";
    /**
     * 跳转地图页面的需求类型
     */
    String JUMP_MAP_TYPE = "JUMP_MAP_TYPE";
    /**
     * 跳转地图页面的需求类型
     */
    String JUMP_MAP_DATA = "JUMP_MAP_DATA";

    /**
     * 设置地址 家庭 类型
     */
    int HOME_ADDRESS_SET_INDEX = 1;
    /**
     * 设置地址 公司 类型
     */
    int COMPONY_ADDRESS_SET_INDEX = 2;

    /**
     * 跳转地图页面的需求类型--设置地址
     */
    String JUMP_MAP_TYPE_SELECT_MAP_SET_ADDRESS = "JUMP_MAP_TYPE_SELECT_MAP_SET_ADDRESS";
    /**
     * 家庭地址保存的实体key
     */
    String SP_KEY_HOME_ADDRESS = "SP_KEY_HOME_ADDRESS";
    /**
     * 公司地址保存的实体key
     */
    String SP_KEY_COMPONY_ADDRESS = "SP_KEY_COMPONY_ADDRESS";

    String SP_KEY_LOGIN_USER = "SP_KEY_LOGIN_USER";
}
