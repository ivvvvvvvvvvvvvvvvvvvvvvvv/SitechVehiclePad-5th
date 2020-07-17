package com.sitechdev.vehicle.pad.module.map.constant;

/**
 * 项目名称：SitechVehiclePad
 * 类名称：AMapConstant
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2019/08/20 0020 16:36
 * 修改时间：
 * 备注：
 */
public interface AMapConstant {

    //高德地图广播类
    /**
     * 高德地图接收第三方应用的控制广播。第三方应用发出，高德地图接收
     */
    String BROADCAST_TO_AMAP = "AUTONAVI_STANDARD_BROADCAST_RECV";
    /**
     * 高德地图发送给第三方应用的控制广播。高德地图发出,第三方应用接收
     */
    String BROADCAST_FROM_AMAP = "AUTONAVI_STANDARD_BROADCAST_SEND";
    /**
     * KEY_TYPE int 必填
     *      12201
     * EXTRA_CHOICE int
     *       0，1，2, 3 ...(POI结果选择,最多十项，索引从0开始）
     * EXTRA_PAGE_TURNING int
     *       0 : 上一页   1 : 下一页
     * EXTRA_SCREEN_TURNING int
     *      0 : 上翻屛  1 ： 下翻屛
     */
    String BROADCAST_FROM_AMAP_RECV = "AUTONAVI_STANDARD_BROADCAST_RECV";

    /**
     * 第三方系统通知Auto，auto接到通知后退出。
     * <code>
     * Intent intent = new Intent();
     * intent.setAction("AUTONAVI_STANDARD_BROADCAST_RECV");
     * intent.putExtra("KEY_TYPE", 10021);
     * sendBroadcast(intent);
     * </code>
     */
    int BROADCAST_AMAP_TYPE_EXIT_APP = 10021;

    /**
     * auto启动后，第三方发送相关消息将auto切换至后台。
     * <code>
     * Intent intent = new Intent();
     * intent.setAction("AUTONAVI_STANDARD_BROADCAST_RECV");
     * intent.putExtra("KEY_TYPE", 10031);
     * sendBroadcast(intent);
     * </code>
     */
    int BROADCAST_AMAP_TYPE_MINI = 10031;

    /**
     * 当auto启动/未启动时，第三方发送电子眼设置信息（开/关），auto直接响应。
     * <code>
     * Intent intent = new Intent("AUTONAVI_STANDARD_BROADCAST_RECV");
     * intent.putExtra("KEY_TYPE",10064);
     * intent.putExtra("EXTRA_TYPE", 0);// 0 : 所有 ；1: 路况播报；2: 电子眼播报； 3: 警示播报；
     * intent.putExtra("EXTRA_OPERA", 1);//0 : 打开 ；1: 关闭
     * sendBroadcast(intent);
     * </code>
     */
    int BROADCAST_AMAP_TYPE_EYE = 10064;

    /**
     * Auto启动后，第三方发送设置当前播报的语音角色（如标准女音，林志玲等）给auto,auto响应。
     * <code>
     * Intent intent = new Intent();
     * intent.setAction("AUTONAVI_STANDARD_BROADCAST_RECV");
     * intent.putExtra("KEY_TYPE", 10044);
     * intent.putExtra("VOICE_ROLE", 1);//0:国语女声; 1：国语男声; 2：周星星；3：广东话；4：林志玲 ； 5：郭德纲；6:东北话; 7：河南话; 8：湖南话；9：四川话；10：台湾话 ；
     * sendBroadcast(intent);
     * </code>
     */
    int BROADCAST_AMAP_KEY_TYPE_VOICE = 10044;

    /**
     * Auto启动后，第三方发送设置地图是否播报即是否静音（永久静音/临时静音），auto响应。
     * <code>
     * Intent intent = new Intent();
     * intent.setAction("AUTONAVI_STANDARD_BROADCAST_RECV");
     * intent.putExtra("KEY_TYPE", 10047);
     * intent.putExtra("EXTRA_MUTE", 0);//永久静音：静音后，需要取消静音或者auto内部设置操作取消静音才能恢复。0:取消静音；1；静音，未传默认0
     * sendBroadcast(intent);
     * Intent intent = new Intent();
     * intent.setAction("AUTONAVI_STANDARD_BROADCAST_RECV");
     * intent.putExtra("KEY_TYPE", 10047);
     * intent.putExtra("EXTRA_CASUAL_MUTE", 0);//临时静音：设置的单次有效，如退出地图后再重启，地图声音就会恢复。0:取消临时静音；1:临时禁音， 未传默认0
     * sendBroadcast(intent);
     * </code>
     * 注：1.x临时静音与永久静音同时发送；2.x需单独分开发送
     */
    int BROADCAST_AMAP_TYPE_MUTE = 10047;

    /**
     * auto启动后，车机系统发送收藏当前点信息给auto【任何场景下都支持（包含导航，巡航，以及非地图界面）】
     * <code>
     * Intent intent = new Intent();
     * intent.setAction("AUTONAVI_STANDARD_BROADCAST_RECV");
     * intent.putExtra("KEY_TYPE", 11003);
     * sendBroadcast(intent);
     * </code>
     */
    int BROADCAST_AMAP_TYPE_FAVORITE_POINT = 11003;


    /**
     * auto启动/未启动时，第三方传入终点，auto以车标为起点规划路径并直接进入导航界面。
     * <code>
     * Intent intent = new Intent();
     * intent.setAction("AUTONAVI_STANDARD_BROADCAST_RECV");
     * intent.putExtra("KEY_TYPE", 10038);
     * intent.putExtra("POINAME","厦门大学");
     * intent.putExtra("LAT",24.444593);//（必填）(double)纬度
     * intent.putExtra("LON",118.101011);//（必填）(double)经度
     * intent.putExtra("DEV",0);//（必填）(int)是否偏移(0:lat 和 lon 是已经加密后的,不需要国测加密; 1:需要国测加密)
     * intent.putExtra("STYLE",0);//（必填）(int)导航方式
     * intent.putExtra("SOURCE_APP","Third App");//第三方应用名称(String)
     * sendBroadcast(intent);
     * </code>
     */
    int BROADCAST_AMAP_TYPE_START_NAVI = 10038;

    /**
     * auto启动/未启动时，第三方传入终点，auto以车标为起点规划路径并直接进入导航界面。
     * <code>
     * Intent intent = new Intent();
     * intent.setAction("AUTONAVI_STANDARD_BROADCAST_RECV");
     * intent.putExtra("KEY_TYPE", 10040);
     * intent.putExtra("DEST", 0);//（必填）0 回家；1 回公司(int)
     * intent.putExtra("IS_START_NAVI", 0);//（必填）是否直接开始导航 0 是；1 否(int)
     * intent.putExtra("SOURCE_APP","Third App");//第三方应用名称(String)
     * sendBroadcast(intent);
     * </code>
     */
    int BROADCAST_AMAP_TYPE_GO_HOME_WORK = 10040;

    /**
     * 第三方通知auto结束引导，退出导航状态，回到主图界面。
     * <code>
     * Intent intent = new Intent();
     * intent.setAction("AUTONAVI_STANDARD_BROADCAST_RECV");
     * intent.putExtra("KEY_TYPE", 10010);
     * sendBroadcast(intent);
     * </code>
     */
    int BROADCAST_AMAP_TYPE_EXIT_NAVI = 10010;

    /**
     * Auto处于全览状态时，接收第三方发送的信息判断是否从全览回到导航界面。
     * <code>
     * Intent intent = new Intent();
     * intent.setAction("AUTONAVI_STANDARD_BROADCAST_RECV");
     * intent.putExtra("KEY_TYPE", 10053);
     * intent.putExtra("EXTRA_AUTO_BACK_NAVI_DATA", true);//true(回车位继续导航);false(暂停在全览界面)
     * sendBroadcast(intent);
     * </code>
     */
    int BROADCAST_AMAP_TYPE_BACK_NAVI = 10053;

    /**
     * 在auto启动/未启动的情况下，第三方可调用该接口设置家/公司的地址。
     * <code>
     * Intent intent = new Intent();
     * intent.setAction("AUTONAVI_STANDARD_BROADCAST_RECV");
     * intent.putExtra("KEY_TYPE", 10053);
     * intent.putExtra("POINAME", "朝阳大厦");//POI名称
     * intent.putExtra("LON", "经度");//double
     * intent.putExtra("LAN", "纬度");//double
     * intent.putExtra("ADDRESS", "地址");//非必须
     * intent.putExtra("EXTRA_TYPE", "类别 家/公司");//int  1：家 2：公司
     * intent.putExtra("DEV", 0);//int  	 经纬度是否偏移 0：不需要国测加密 1：需要国测加密
     * sendBroadcast(intent);
     * </code>
     */
    int BROADCAST_AMAP_TYPE_SET_HOME_WORK_ADDRESS = 10058;

    /**
     * 在auto启动/未启动的情况下，第三方可调用该接口设置家/公司的地址。
     * <code>
     * AUTONAVI_STANDARD_BROADCAST_SEND
     * CATEGORY:类别(1:表示家,2:表示公司) （int）
     * EXTRA_RESPONSE_CODE （0：成功， 1：失败）（int）
     * </code>
     */
    int BROADCAST_AMAP_TYPE_SET_HOME_WORK_ADDRESS_RESULT = 10059;

    /**
     * auto启动/未启动时，允许第三方调用跳转到设置回家/公司界面。
     * <code>
     * Action："AUTONAVI_STANDARD_BROADCAST_RECV"
     * KEY_TYPE:10070
     * EXTRA_TYPE：附加参数类型 0 回家 1 回公司
     * </code>
     */
    int BROADCAST_AMAP_TYPE_SET_HOME_WORK_ADDRESS_VIEW = 10070;

    /**
     * 在未启动/启动auto的情况下，第三方可调用接口查看家和公司的信息。
     * <code>
     * Intent intent = new Intent();
     * intent.setAction("AUTONAVI_STANDARD_BROADCAST_RECV");
     * intent.putExtra("KEY_TYPE", 10045);
     * intent.putExtra("EXTRA_TYPE", "类别 家/公司");//int  1：家 2：公司
     * sendBroadcast(intent);
     * </code>
     */
    int BROADCAST_AMAP_TYPE_GET_HOME_WORK_ADDRESS = 10045;

    /**
     * 在auto启动/未启动的情况下，第三方可调用该接口设置家/公司的地址。获取结果
     * <code>
     * POINAME:poi名称 (String)
     * LON:经度参数（double）
     * LAT:纬度参数（double）
     * DISTANCE:距离 (int)
     * CATEGORY:类别(1:表示家,2:表示公司)（int）
     * ADDRESS:地址(String)
     * </code>
     */
    int BROADCAST_AMAP_TYPE_GET_HOME_WORK_ADDRESS_RESULT = 10046;
    /**
     * 关键字搜索结果返回
     */
    int BROADCAST_AMAP_TYPE_SEARCH_INFO_RESULT = 10042;
    int BROADCAST_AMAP_TYPE_SEARCH_RESULT = 12201;

    /**
     * 当导航发生状态变更时，将相应的状态通知给系统。
     * <code>
     * EXTRA_STATE：运行状态（int）
     * </code>
     * 参考：https://lbs.amap.com/api/amapauto/guide/auto_naviinfo_protocal  1.1
     */
    int BROADCAST_AMAP_TYPE_NAVI_STATE = 10019;

}
