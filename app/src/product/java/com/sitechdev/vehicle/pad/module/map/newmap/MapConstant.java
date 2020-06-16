package com.sitechdev.vehicle.pad.module.map.newmap;

/**
 * @author 邵志
 * @version 2020/06/16 0016 10:25
 * @name MapConstant
 * @description
 */
public class MapConstant {

    //-------------------以下为高德地图地图相关广播
    public static final String BROADCAST_ACTION_RECV = "AUTONAVI_STANDARD_BROADCAST_RECV";//高德接收的广播
    public static final String BROADCAST_ACTION_SEND = "AUTONAVI_STANDARD_BROADCAST_SEND"; //高德发送的广播

    public static final String KEY_TYPE = "KEY_TYPE";
    public static final String MAP_WEB_KEY = "2a8edc767c852f32cd102ff268847ff0";

    public static final String EXTRA_AUTO_CAN_ZOOM = "EXTRA_CAN_ZOOM"; //(boolean) 比例尺是否能继续缩放
    public static final String EXTRA_AUTO_ZOOM_TYPE = "EXTRA_ZOOM_TYPE"; //(int) 比例尺 0 放大 1 缩小

    // acc on/off消息
    public static final int KEY_TYPE_ACC_ON = 10073;
    public static final int KEY_TYPE_ACC_OFF = 1001;

    //主图功能
    public static final int KEY_TYPE_POSITION = 10008; //查看当前位置
    public static final int KEY_TYPE_POI_ADDRESS = 10011; //传入地址查看POI
    public static final int KEY_TYPE_START = 10034; //进入Auto主图
    public static final int KEY_TYPE_EXIT = 10021; //退出软件
    public static final int KEY_TYPE_AUTO_MIN = 10031; //auto最小化
    public static final int KEY_TYPE_ZOOM = 10074; //比例尺放大缩小通知

    public static final int KEY_TYPE_POI_NAME = 10012; //传入名称查看POI
    public static final int KEY_TYPE_FAVORITES = 10028; //收藏夹
    public static final int KEY_TYPE_LABEL = 10039; //地图标注
    public static final int KEY_TYPE_VOICE_ROLE = 10044; //设置语音播报角色,VOICE_ROLE(0-10)
    public static final int KEY_TYPE_DAY_NIGHT_MODE = 10048; //设置昼夜模式,EXTRA_DAY_NIGHT_MODE(0：自动 1：白天 2：黑夜)
    public static final int KEY_TYPE_OPT_MAP = 10027; //图面操作
    public static final int KEY_TYPE_ROAD_CAMERA = 10064; //电子眼设置
    public static final int KEY_TYPE_RESET = 11001; //导航恢复出厂设置

    //信息透出
    public static final int KEY_TYPE_CURRENT_REGION_REQ = 10029; //当前行政区域信息请求
    public static final int KEY_TYPE_CURRENT_REGION_RES = 10030; //当前行政区域信息请求,反馈
    public static final int KEY_TYPE_GUIDEINFO_REQ = 10062; //引导信息请求
    public static final int KEY_TYPE_GUIDEINFO_RES = 10001; //引导信息透出
    public static final int KEY_TYPE_GUIDEINFO_ROAD = 10056; //获取当前路线信息
    public static final int KEY_TYPE_MAP_INFO = 10041; //地图将软件相关信息通知给三方或系统

    // auto应用内搜索功能
    public static final int KEY_TYPE_INNER_KEYWORDS = 10036; //关键字搜索
    public static final int KEY_TYPE_KEYWORDS_AROUND = 10037; //周边搜索,如周边银行等

    // auto应用外搜索
    public static final int KEY_TYPE_EXTRA_KEYWORDS_REQ = 10023; //关键字搜索
    public static final int KEY_TYPE_EXTRA_KEYWORDS_RES = 10042; //关键字搜索结果返回
    public static final int KEY_TYPE_EXTRA_KEYWORDS_AROUND_REQ = 10024;//周边搜索
    public static final int KEY_TYPE_EXTRA_KEYWORDS_AROUND_RES = 10043; //a周边搜索

    /**
     * (String) 搜索结果JSON字符串，若无数据则为空；
     */
    public static final String KEY_SERACH_EXTRA_RESULT = "EXTRA_RESULT";

    // 出行导航
    public static final int KEY_TYPE_NAV_START = 10038; //传入终点直接导航
    public static final int KEY_TYPE_ROUTE_PREFER = 10005; //导航偏好设置,NAVI_ROUTE_PREFER(0-8,20,24)
    public static final int KEY_TYPE_NAV_EXIT = 10010; //结束导航
    public static final int KEY_TYPE_CHANGE_ROAD = 10055; //路线方案选择
    public static final int KEY_TYPE_MOCK_NAV = 10076; //模拟导航

    //用户信息
    public static final int KEY_TYPE_SETTING_HomeOrCop = 10070;//进入家/公司设置界面,EXTRA_TYPE(0：回家 1：回公司)
    public static final int KEY_TYPE_NAVI_HomeOrCop = 10040;  // 回家/去公司（特殊点导航）
    public static final int KEY_TYPE_SETTING_HomeOrCop_REQ = 10058; //设置家/公司,EXTRA_TYPE(0：回家 1：回公司)
    public static final int KEY_TYPE_SETTING_HomeOrCop_RES = 10059; //设置家/公司的结果返回
    public static final int KEY_TYPE_HomeOrCop_REQ = 10045;  // 请求家或者公司信息
    public static final int KEY_TYPE_HomeOrCop_RES = 10046;  // 发送家或者公司信息 CATEGORY:类别(1:表示家,2:表示公司)（int）

    /**
     * 车机系统设置收藏点，auto发送收藏结果
     * {
     * "poiId": "",
     * "name": "思明区望海路59号楼",
     * "addr": "在邦初软件教育学院附近",
     * "latitude": 24.489181862880333,
     * "longitude": 118.18652719259262,
     * "phone": ""
     * }
     */
    public static final int KEY_TYPE_BOOKMARKS_POI = 11003;
    public static final int KEY_TYPE_BOOKMARKS = 10028;  // 打开收藏夹


    // 其他地图发送过来的数据
    public static final int KEY_TYPE_PING = 10019;// 心跳
    public static final int KEY_TYPE_LOCATION_INFO = 10065; // auto将车头方向（从GPS信息获取，按角度透出）信息发送给第三方

    // 地图主图Activity获取到焦点
    public static final int KEY_TYPE_ACTIVITY_GAIN_FOCUS = 50; // activity 获得焦点
    public static final int KEY_TYPE_ACTIVITY_LOSE_FOCUS = 51; // activity 失去焦点


    /**
     * 在导航/巡航/模拟导航中auto主动将变化的引导信息透出给第三方/系统。
     * https://lbs.amap.com/api/amap-auto/guide/android/info#t5
     */
    public class GuideInfoExtraKey {
        //导航类型，对应的值为int类型
        //：GPS导航
        //1：模拟导航
        public static final String TYPE = "TYPE";
        //当前道路名称，对应的值为String类型
        public static final String CUR_ROAD_NAME = "CUR_ROAD_NAME";

        //下一道路名，对应的值为String类型
        public static final String NEXT_ROAD_NAME = "NEXT_ROAD_NAME";

        //距离最近服务区的距离，对应的值为int类型，单位：米
        public static final String SAPA_DIST = "SAPA_DIST";

        //服务区类型，对应的值为int类型
        //0：高速服务区
        //1：其他高速服务设施（收费站等）
        public static final String SAPA_TYPE = "SAPA_TYPE";

        //距离最近的电子眼距离，对应的值为int类型，单位：米
        public static final String CAMERA_DIST = "CAMERA_DIST";

        //电子眼类型，对应的值为int类型
        //0 测速摄像头
        //1为监控摄像头
        //2为闯红灯拍照
        //3为违章拍照
        //4为公交专用道摄像头
        //5为应急车道摄像头
        public static final String CAMERA_TYPE = "CAMERA_TYPE";

        //电子眼限速度，对应的值为int类型，无限速则为0，单位：公里/小时;

        public static final String CAMERA_SPEED = "CAMERA_SPEED";

        //下一个将要路过的电子眼编号，若为-1则对应的道路上没有电子眼，对应的值为int类型
        public static final String CAMERA_INDEX = "CAMERA_INDEX";

        //导航转向图标，对应的值为int类型
        public static final String ICON = "ICON";

        /**
         * 获取当前路线信息
         */
        public static final String EXTRA_ROAD_INFO = "EXTRA_ROAD_INFO";

        //路径剩余距离，对应的值为int类型，单位：米
        public static final String ROUTE_REMAIN_DIS = "ROUTE_REMAIN_DIS";

        //路径剩余时间，对应的值为int类型，单位：秒
        public static final String ROUTE_REMAIN_TIME = "ROUTE_REMAIN_TIME";

        //当前导航段剩余距离，对应的值为int类型，单位：米
        public static final String SEG_REMAIN_DIS = "SEG_REMAIN_DIS";

        //当前导航段剩余时间，对应的值为int类型，单位：秒
        public static final String SEG_REMAIN_TIME = "SEG_REMAIN_TIME";

        //当前位置的前一个形状点号，对应的值为int类型，从0开始
        public static final String CUR_POINT_NUM = "CUR_POINT_NUM";

        //环岛出口序号，对应的值为int类型，从0开始.
        //1.x版本：只有在icon为11和12时有效，其余为无效值0
        //2.x版本：只有在icon为11、12、17、18时有效，其余为无效值0
        public static final String ROUND_ABOUT_NUM = "ROUNG_ABOUT_NUM";

        //路径总距离，对应的值为int类型，单位：米
        public static final String ROUTE_ALL_DIS = "ROUTE_ALL_DIS";

        //路径总时间，对应的值为int类型，单位：秒
        public static final String ROUTE_ALL_TIME = "ROUTE_ALL_TIME";

        //当前车速，对应的值为int类型，单位：公里/小时
        public static final String CUR_SPEED = "CUR_SPEED";

        //红绿灯个数，对应的值为int类型
        public static final String TRAFFIC_LIGHT_NUM = "TRAFFIC_LIGHT_NUM";

        //服务区个数，对应的值为int类型
        public static final String SAPA_NUM = "SAPA_NUM";

        //下一个服务区名称，对应的值为String类型
        public static final String SAPA_NAME = "SAPA_NAME";

        //当前道路类型，对应的值为int类型
        //0：高速公路
        //1：国道
        //2：省道
        //3：县道
        //4：乡公路
        //5：县乡村内部道路
        //6：主要大街、城市快速道
        //7：主要道路
        //8：次要道路
        //9：普通道路
        //10：非导航道路
        public static final String ROAD_TYPE = "ROAD_TYPE";
    }


    //--------------------道道通相关广播
    public static final String TITLE_CAR_SPEED = "com.hazens.navi.car_speed";
    public static final String TITLE_CAR_SPEED_KEY = "speed";
    public static final String TITLE_CAR_MILEAGE = "com.hazens.navi.recharge_mileage";
    public static final String TITLE_CAR_MILEAGE_KEY = "mileage";
    public static final String TITLE_CAR_RECEIVER = "com.hazens.openapp.receiver";
    public static final String TITLE_CAR_VIN = "com.hz.common.device.vin";
    public static final String TITLE_CAR_VIN_KEY = "vinCode";
    public static final String NAVIMAP_VOICE_START = "navimap.voice.start";
    public static final String NAVIMAP_VOICE_END = "navimap.voice.end";

    /**
     * 道道通-发送广播
     */
    public static final String TEDDY_NAVI_BROADCAST_SEND = "com.hz.common.send";
    /**
     * 道道通--接收广播
     */
    public static final String TEDDY_NAVI_BROADCAST_RECV = "com.hz.common.recv";

    /**
     * 道道通导航过程中
     * intent.putExtra("returndata", "0");
     * 0代表非导航  1代表导航中
     */
    public static final String NAV_GUIDE_STATUS = "com.guide.status";

    /**
     * 导航播报开始结束广播
     * "navimap.voice.start" 播报开始
     * "navimap.voice.end" 播报结束
     */
    public static final String NAV_GUIDE_VOICE = "hazens.navimap.voice";

    /**
     * 通知导航切换声道，SysManage存在同样的常量
     */
    public static final String TITLE_CAR_CHANNEL = "com.hazens.intent.action.CHANNEL_SWITCH_TO_BT";
    public static final String TITLE_CAR_CHANNEL_KEY = "enable";
    public static final String TITLE_CAR_CLOSE = "com.system.close";

    //道道通导航信息透出
    public static final int PIF_GUIDECODE_CODE_NOGUIDE = (0x0000); /* [CN] 无方向引导  [CN]*/
    public static final int PIF_GUIDECODE_CODE_INVALID = (0xFFFF); /* [CN] 无效 [CN]*/

    public static final int PIF_GUIDECODE_CODE_RIGHT_SIDE = (0x1); /* [CN] 靠左/沿左侧道路 			    [CN]*/
    public static final int PIF_GUIDECODE_CODE_LEFT_SIDE = (0x1009); /* [CN] 靠左/沿左侧道路 			    [CN]*/

    public static final int PIF_GUIDECODE_CODE_RIGHT_TURNBACK = (0x1); /* [CN] 右掉头[CN]*/
    public static final int PIF_GUIDECODE_CODE_RIGHT_BACK = (0x2); /* [CN] 右后斜 [CN]*/
    public static final int PIF_GUIDECODE_CODE_RIGHT = (0x3); /* [CN] 右 [CN]*/
    public static final int PIF_GUIDECODE_CODE_RIGHT_FRONT = (0x4); /* [CN] 右斜[CN]*/
    public static final int PIF_GUIDECODE_CODE_RIGHT_FRONT_1 = (0xA); /* [CN] 右转专用道 [CN]*/
    public static final int PIF_GUIDECODE_CODE_RIGHT_FRONT_2 = (0xB); /* [CN] 右侧道路 [CN]*/
    public static final int PIF_GUIDECODE_CODE_STRAIGHT = (0x0005); /* [CN] 直行 [CN]*/
    public static final int PIF_GUIDECODE_CODE_LEFT_FRONT = (0x6); /* [CN] 左斜 [CN]*/
    public static final int PIF_GUIDECODE_CODE_LEFT_FRONT_1 = (0xC); /* [CN] 左侧道路 [CN]*/
    public static final int PIF_GUIDECODE_CODE_LEFT = (0x7); /* [CN] 左 [CN]*/
    public static final int PIF_GUIDECODE_CODE_LEFT_BACK = (0x8);/* [CN] 左后斜 [CN]*/
    public static final int PIF_GUIDECODE_CODE_LEFT_TURNBACK = (0x9);/* [CN] 左掉头[CN]*/

    public static final int PIF_GUIDECODE_CODE_TEMP_POINT = (0x1F); /* [CN] 途经点 			    [CN]*/
    public static final int PIF_GUIDECODE_CODE_DEST = (0x1E);/* [CN] 目的地 			    [CN]*/
    public static final int PIF_GUIDECODE_CODE_TUNNEL = (0x1D);/* [CN] 隧道入口 			    [CN]*/
    public static final int PIF_GUIDECODE_CODE_SERVICE = (0x1C);/* [CN] 服务区 			    [CN]*/
    public static final int PIF_GUIDECODE_CODE_GATE = (0x1B);/* [CN] 收费站 			    [CN]*/

    public static final int PIF_GUIDECODE_CODE_ROUNDABOUT = (0x11);/* [CN] 环岛 			    [CN]*/
    public static final int PIF_GUIDECODE_CODE_ROUNDABOUT_1 = (0x12); /* [CN] 环岛右后转 			    [CN]*/
    public static final int PIF_GUIDECODE_CODE_ROUNDABOUT_2 = (0x13); /* [CN] 环岛右转 			    [CN]*/
    public static final int PIF_GUIDECODE_CODE_ROUNDABOUT_3 = (0x14); /* [CN] 环岛右前转 			    [CN]*/
    public static final int PIF_GUIDECODE_CODE_ROUNDABOUT_4 = (0x16); /* [CN] 环岛左前转 			    [CN]*/
    public static final int PIF_GUIDECODE_CODE_ROUNDABOUT_5 = (0x17); /* [CN] 环岛左前转 			    [CN]*/
    public static final int PIF_GUIDECODE_CODE_ROUNDABOUT_6 = (0x18); /* [CN] 环岛左前转 			    [CN]*/
    public static final int PIF_GUIDECODE_CODE_ROUNDABOUT_7 = (0x19); /* [CN] 环岛左前转 			    [CN]*/
    public static final int PIF_GUIDECODE_CODE_ROUNDABOUT_STRAIGHT = (0x15); /* [CN] 环岛直行 			    [CN]*/

    public static final int PIF_GUIDECODE_CODE_FERRY = (0x1A); /* [CN] 渡口 			    [CN]*/
    public static final int PIF_GUIDECODE_CODE_ROUNDABOUT_BACK = (0x19); /* [CN] 环岛掉头 			    [CN]*/
    public static final int PIF_GUIDECODE_CODE_ROUNDABOUT_LEFT_BACK = (0x18); /* [CN] 环岛左后转 			    [CN]*/
}
