package com.sitechdev.vehicle.pad.manager;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.view.Gravity;

import com.blankj.utilcode.util.ToastUtils;
import com.sitechdev.vehicle.lib.util.AppUtils;
import com.sitechdev.vehicle.lib.util.PackageInfoUtils;
import com.sitechdev.vehicle.lib.util.SerialUtils;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.pad.app.AppApplication;
import com.sitechdev.vehicle.pad.bean.PoiBean;
import com.sitechdev.vehicle.pad.module.map.newmap.IMap;
import com.sitechdev.vehicle.pad.module.map.newmap.MapConstant;
import com.sitechdev.vehicle.pad.module.map.util.LocationData;
import com.sitechdev.vehicle.pad.module.setting.teddy.TeddyConstants;

/**
 * @author 邵志
 * @version 2020/06/16 0016 09:56
 * @name MapManager
 * @description
 */
public class MapManager implements IMap {

    /**
     * 高德地图uid标识
     */
    private static final String KEY_TUID = "navi_tuid";
    /**
     * 需要被安装的高德地图版本
     */
    private static final int AUTO_MAP_PKG_VERSION = 562;

    /**
     * 高德地图包名
     */
    private static final String AUTO_MAP_PKG_NAME = "com.autonavi.amapauto";
    /**
     * 高德地图启动Activity路径
     */
    private static final String MAP_PKG_MAIN_ACTIVITY = "com.autonavi.auto.remote.fill.UsbFillActivity";
    /**
     * 高德地图assets目录文件名称
     */
    private static final String AUTO_MAP_ASSETS_PATH = "";
    /**
     * 高德地图sd存储路径
     */
    private static final String AUTO_MAP_TARGET_PATH = "/storage/sdcard0/Auto.apk";
    /**
     * 高德地图是否激活过
     */
    private static final String AUTO_MAP_ACTIVE_PATH = "/data/automap.ok";

    private static final String TAG = MapManager.class.getSimpleName();

    private Context mContext = null;

    private MapManager() {
        mContext = AppApplication.getContext();
    }

    private static final class SingleMapManager {
        private static final MapManager SINGLE = new MapManager();
    }

    public static MapManager getInstance() {
        return SingleMapManager.SINGLE;
    }

    /**
     * 判断地图是否在前台
     */
    @Override
    public Boolean isForeground() {
        return AppUtils.isRunningForeground(mContext, AUTO_MAP_PKG_NAME);
    }

    /**
     * 初始化操作
     */
    @Override
    public void init() {
        mContext = AppApplication.getContext();
        Settings.System.putString(mContext.getContentResolver(), KEY_TUID, SerialUtils.getMapDeviceId());
        // 模拟uid,可以在线激活
        Settings.System.putString(mContext.getContentResolver(), KEY_TUID, "1E1C51D4EA9A216F0000000000000000");
//        ThreadUtils.runOnUIThread {
//            MapInstalledWindow.getInstance().init(mContext);
//        }
    }

    /**
     * 启动地图
     */
    @Override
    public void startMap() {
        if (!AppUtils.isAppInstalled(mContext, AUTO_MAP_PKG_NAME)) {
            ToastUtils.setGravity(Gravity.CENTER, 0, 0);
            ToastUtils.showLong("正在升级高德地图，请稍后");
            return;
        }
        //酷我界面退出到后台
        if (AppUtils.isRunningForeground(AppApplication.getContext(), TeddyConstants.THIRDAPP_PACKAGENAME_KUWO)) {
//            todo
//            TeddyUtil.startMainHome();
        }

//            todo
//        EventBusUtils.postEvent(MapEvent(MapEvent.MAP_EVENT_HIDE_WINDOW));

        boolean isForeground = AppUtils.isRunningForeground(mContext, AUTO_MAP_PKG_NAME);

        if (isForeground) {
            SitechDevLog.d(TAG, "map is foreground and return!");
//            todo
//            EventBusUtils.postEvent(TeddyEvent(TeddyEvent.EB_TEDDY_SCENE_EVENT_CHANGE, TeddyEvent.TEDDY_SCENE_GAO_DE));
            return;
        }
        SitechDevLog.d(TAG, "map is background and start!");

        Intent launchIntent = new Intent();
        launchIntent.setComponent(new ComponentName(AUTO_MAP_PKG_NAME, MAP_PKG_MAIN_ACTIVITY));
        launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(launchIntent);
        //进度高德地图改变场景
//            todo
//        EventBusUtils.postEvent(TeddyEvent(TeddyEvent.EB_TEDDY_SCENE_EVENT_CHANGE, TeddyEvent.TEDDY_SCENE_GAO_DE))
    }

    /**
     * auto最小化
     */
    @Override
    public void stopMap() {
        Intent intent = new Intent();
        intent.setAction(MapConstant.BROADCAST_ACTION_RECV);
        intent.putExtra(MapConstant.KEY_TYPE, MapConstant.KEY_TYPE_AUTO_MIN);
        mContext.sendBroadcast(intent);
    }

    /**
     * 取消导航
     */
    @Override
    public void stopNavigate() {
        Intent intent = new Intent();
        intent.setAction(MapConstant.BROADCAST_ACTION_RECV);
        intent.putExtra(MapConstant.KEY_TYPE, MapConstant.KEY_TYPE_NAV_EXIT);
        mContext.sendBroadcast(intent);
    }

    /**
     * 请求当前设置的家或公司信息
     *
     * @param extraType 类型 1 家； 2 公司
     */
    @Override
    public void getHomeOrCop(int extraType) {
        Intent intent = new Intent();
        intent.setAction(MapConstant.BROADCAST_ACTION_RECV);
        intent.putExtra(MapConstant.KEY_TYPE, MapConstant.KEY_TYPE_HomeOrCop_REQ);
        intent.putExtra("EXTRA_TYPE", extraType);
        mContext.sendBroadcast(intent);
    }

    /**
     * 回家/去公司（特殊点导航）
     *
     * @param destType 0 回家；1 回公司
     * @param autoNav  是否直接开始导航 0 是；1 否
     */
    @Override
    public void navToHomeOrCop(int destType, int autoNav) {
        Intent intent = new Intent();
        intent.setAction(MapConstant.BROADCAST_ACTION_RECV);
        intent.putExtra(MapConstant.KEY_TYPE, MapConstant.KEY_TYPE_NAVI_HomeOrCop);
        intent.putExtra("DEST", destType);
        intent.putExtra("IS_START_NAVI", autoNav);
        intent.putExtra("SOURCE_APP", PackageInfoUtils.getAppVersionName());
        mContext.sendBroadcast(intent);
    }

    /**
     * 地图缩放
     *
     * @param operaType 0 放大地图； 1缩小地图
     */
    @Override
    public void zoomMap(int operaType) {
        handleMainMap(1, operaType);
    }

    /**
     * 设置路线偏好
     *
     * @param routerPrefer 路线偏好
     *                     0（速度快）1（费用少）2（路程短）3 不走高速 4（躲避拥堵）5（不走高速且避免收费）
     *                     6（不走高速且躲避拥堵）7（躲避收费和拥堵）8（不走高速躲避收费和拥堵）
     *                     20（高速优先）24（高速优先且躲避拥堵）
     */
    @Override
    public void routePrefer(int routerPrefer) {
        Intent intent = new Intent();
        intent.setAction(MapConstant.BROADCAST_ACTION_RECV);
        intent.putExtra(MapConstant.KEY_TYPE, MapConstant.KEY_TYPE_ROUTE_PREFER);
        intent.putExtra("NAVI_ROUTE_PREFER", routerPrefer);
        mContext.sendBroadcast(intent);
    }

    /**
     * 实时路况
     *
     * @param operaType 0 实时路况开；1实时路况关
     */
    @Override
    public void roadCondition(int operaType) {
        handleMainMap(0, operaType);
    }

    /**
     * 切换地图视图
     *
     * @param operaType 0切换2d车上； 1切换2d北上；2切换3d车上支持
     */
    @Override
    public void switchViewMode(int operaType) {
        handleMainMap(2, operaType);
    }

    /**
     * 设置昼夜模式
     *
     * @param modeType 昼夜模式类型 0:自动; 1：白天; 2：黑夜
     */
    @Override
    public void switchDayNightMode(int modeType) {
        Intent intent = new Intent();
        intent.setAction(MapConstant.BROADCAST_ACTION_RECV);
        intent.putExtra(MapConstant.KEY_TYPE, MapConstant.KEY_TYPE_DAY_NIGHT_MODE);
        intent.putExtra("EXTRA_DAY_NIGHT_MODE", modeType);
        mContext.sendBroadcast(intent);
    }

    /**
     * 设置为家或公司
     *
     * @param extraType int 1：家 2：公司
     * @param copPoi    设置点的地址，address非必填
     */
    @Override
    public void setHomeOrCop(int extraType, PoiBean copPoi) {
        Intent intent = new Intent();
        intent.setAction(MapConstant.BROADCAST_ACTION_RECV);
        intent.putExtra(MapConstant.KEY_TYPE, MapConstant.KEY_TYPE_SETTING_HomeOrCop_REQ);
        intent.putExtra("LAT", copPoi.lat);
        intent.putExtra("LON", copPoi.lon);
        intent.putExtra("POINAME", copPoi.poiName);
        intent.putExtra("ADDRESS", copPoi.address);
        intent.putExtra("EXTRA_TYPE", extraType);
        intent.putExtra("DEV", 0);
        mContext.sendBroadcast(intent);
    }

    /**
     * 收藏当前点
     */
    @Override
    public void collectPoi() {
        Intent intent = new Intent();
        intent.setAction(MapConstant.BROADCAST_ACTION_RECV);
        intent.putExtra(MapConstant.KEY_TYPE, MapConstant.KEY_TYPE_BOOKMARKS_POI);
        mContext.sendBroadcast(intent);
    }

    /**
     * 模拟导航
     */
    @Override
    public void simulateNavi() {
        Intent intent = new Intent();
        intent.setAction(MapConstant.BROADCAST_ACTION_RECV);
        intent.putExtra(MapConstant.KEY_TYPE, MapConstant.KEY_TYPE_MOCK_NAV);
        mContext.sendBroadcast(intent);
    }

    /**
     * 导航到某地
     *
     * @param name
     * @param lat
     * @param lon
     */
    @Override
    public void startNavigate(String name, double lat, double lon) {
        Intent intent = new Intent();
        intent.setAction(MapConstant.BROADCAST_ACTION_RECV);
        intent.putExtra(MapConstant.KEY_TYPE, MapConstant.KEY_TYPE_NAV_START);
        intent.putExtra("POINAME", name);
        intent.putExtra("LAT", lat);
        intent.putExtra("LON", lon);
        intent.putExtra("DEV", 0);
        intent.putExtra("STYLE", -1);
        intent.putExtra("SOURCE_APP", PackageInfoUtils.getAppVersionName());
        mContext.sendBroadcast(intent);
    }

    /**
     * 查找附近的充电桩
     */
    @Override
    public void findChargingStation() {
        searchNearby("充电站", LocationData.getInstance().getLatitude(), LocationData.getInstance().getLongitude());
    }

    /**
     * 查找附近poi
     *
     * @param poi  poi类型
     * @param city 查找城市 ex"北京"
     */
    @Override
    public void searchNearbyExtra(String poi, String city) {
        Intent intent = new Intent();
        intent.setAction(MapConstant.BROADCAST_ACTION_RECV);
        intent.putExtra(MapConstant.KEY_TYPE, MapConstant.KEY_TYPE_EXTRA_KEYWORDS_AROUND_REQ);
        intent.putExtra("EXTRA_KEYWORD", poi);
        intent.putExtra("EXTRA_MYLOCLAT", LocationData.getInstance().getLatitude());
        intent.putExtra("EXTRA_MYLOCLON", LocationData.getInstance().getLongitude());
        intent.putExtra("EXTRA_DEV", 0);
        intent.putExtra("EXTRA_SEARCHTYPE", 1);
        intent.putExtra("EXTRA_MAXCOUNT", 5);
        intent.putExtra("EXTRA_SORTORDER ", 1);
        mContext.sendBroadcast(intent);
    }

    /**
     * 通知导航声道占用
     *
     * @param occupy 占用声道
     */
    @Override
    public void requestMapAudioFocus(boolean occupy) {
//        GA10App.getAudioManager().setStreamMute(AudioManager.STREAM_MUSIC, occupy)
    }

    /**
     * 点火后，第三方通过该接口发送 acc 消息
     *
     * @param accOn true acc on, false acc off
     */
    @Override
    public void notifyAccOn(boolean accOn) {
        Intent intent = new Intent();
        intent.setAction(MapConstant.BROADCAST_ACTION_RECV);
        intent.putExtra(MapConstant.KEY_TYPE, accOn ? MapConstant.KEY_TYPE_ACC_ON : MapConstant.KEY_TYPE_ACC_OFF);
        mContext.sendBroadcast(intent);
    }

    /**
     * 通知导航一些参数信息
     *
     * @param params
     */
    @Override
    public void notifyNaviClient(String params) {
        stopNavigate();
    }

    /**
     * 地图图画操作
     *
     * @param extraType 操作类型 0 实时路况; 1 缩放地图; 2 视图模式（必填）
     * @param operaType extraType为0：0 实时路况开；1实时路况关
     *                  extraType为1：0 放大地图； 1缩小地图
     *                  extraType为2：0切换2d车上； 1切换2d北上；2切换3d车上
     */
    private void handleMainMap(int extraType, int operaType) {
        Intent intent = new Intent();
        intent.setAction(MapConstant.BROADCAST_ACTION_RECV);
        intent.putExtra(MapConstant.KEY_TYPE, MapConstant.KEY_TYPE_OPT_MAP);
        intent.putExtra("EXTRA_TYPE", extraType);
        intent.putExtra("EXTRA_OPERA", operaType);
        mContext.sendBroadcast(intent);
    }

    /**
     * 应用内查找数据，会自动打开地图，但是没有响应结果回传；
     * 比如：附近的银行、超市等
     */
    private void searchNearby(String keywords, double lat, double lon) {
        Intent intent = new Intent();
        intent.setAction(MapConstant.BROADCAST_ACTION_RECV);
        intent.putExtra(MapConstant.KEY_TYPE, MapConstant.KEY_TYPE_KEYWORDS_AROUND);
        intent.putExtra("KEYWORDS", keywords);
        intent.putExtra("LAT", lat);
        intent.putExtra("LON", lon);
        intent.putExtra("DEV", 0);
        intent.putExtra("SOURCE_APP", PackageInfoUtils.getAppVersionName());
        mContext.sendBroadcast(intent);

    }


}
