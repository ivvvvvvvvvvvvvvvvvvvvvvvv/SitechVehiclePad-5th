package com.sitechdev.vehicle.pad.module.map.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.poisearch.PoiSearch;
import com.sitechdev.vehicle.lib.util.PackageInfoUtils;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.lib.util.StringUtils;
import com.sitechdev.vehicle.lib.util.ThreadUtils;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.AppApplication;
import com.sitechdev.vehicle.pad.app.AppConst;
import com.sitechdev.vehicle.pad.module.map.MapActivity;
import com.sitechdev.vehicle.pad.module.map.PoiSearchActivity;
import com.sitechdev.vehicle.pad.module.map.constant.AMapConstant;
import com.sitechdev.vehicle.pad.util.AppVariants;
import com.sitechdev.vehicle.pad.view.CommonToast;
import com.sitechdev.vehicle.pad.vui.VUI;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * 项目名称：SitechVehiclePad
 * 类名称：MapUtil
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2019/08/13 0013 13:55
 * 修改时间：
 * 备注：
 */
public class MapUtil {

    private static String[] getMapClientArray() {
        LinkedHashMap<String, String> mapClientMap = new LinkedHashMap<>();
        if (PackageInfoUtils.isInstallApk(AppConst.AMAP_HD_PACKNAME)) {
            //高德地图已安装
            mapClientMap.put("高德地图", AppConst.AMAP_HD_PACKNAME);
        }
//        if (PackageInfoUtils.isInstallApk(AppConst.BAIDU_MAP_HD_PACKNAME)) {
//            //百度地图已安装
//            mapClientMap.put("百度地图", AppConst.BAIDU_MAP_HD_PACKNAME);
//        }
//        if (PackageInfoUtils.isInstallApk(AppConst.TECENT_MAP_PACKNAME)) {
//            //腾讯地图已安装
//            mapClientMap.put("腾讯地图", AppConst.TECENT_MAP_PACKNAME);
//        }
//        if (mapClientMap.size() == 0) {
//            return AppConst.NAVI_MAP_ARRAYS;
//        }
        Iterator<String> it = mapClientMap.keySet().iterator();
        String[] mapClientArray = new String[mapClientMap.size()];
        int index = 0;
        while (it.hasNext()) {
            mapClientArray[index] = it.next();
            index++;
        }
        return mapClientArray;
    }

    /**
     * 展示导航选择窗口
     *
     * @param context      上下文
     * @param markerLatLng 目的地的经纬度对象
     */
    public static void showNaviSelectClientDialog(final Context context, final LatLng markerLatLng, String poiName) {
        if (context == null) {
            SitechDevLog.i(AppConst.TAG, "showNaviSelectClientDialog == 上下文 context =null");
            return;
        }
        String[] mapClientArray = getMapClientArray();
        if (mapClientArray.length == 0) {
            //没有可以调用的的客户端
            ThreadUtils.runOnUIThread(() -> {
                CommonToast.makeText(context, context.getResources().getString(R.string.please_install_amap));
            });
            return;
        }
        if (mapClientArray.length == 1) {
            //只有1个，则直接掉相应的客户端
            startNaviByName(context, mapClientArray[0], markerLatLng, poiName);
            return;
        }
        //添加列表
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setItems(mapClientArray, (dialogInterface, i) -> {
                    startNaviByName(context, mapClientArray[i], markerLatLng, poiName);
                }).create();
        alertDialog.show();
    }

    /**
     * 调起相应的客户端导航
     *
     * @param context      context
     * @param clientName   clientName
     * @param markerLatLng markerLatLng
     */
    private static void startNaviByName(Context context, String clientName, LatLng markerLatLng, String poiName) {
        ThreadUtils.runOnUIThread(() -> {
            if (MapVariants.isMapNavi_Running) {
                //正在导航中，先结束导航
                sendAMapOverNavi();
            }
            switch (clientName) {
                case "高德地图":
                    if (PackageInfoUtils.isInstallApk(AppConst.AMAP_HD_PACKNAME)) {//传入指定应用包名
                        startAMapNaviHD(context, markerLatLng, poiName);
                    } else {
                        CommonToast.makeText(context, context.getResources().getString(R.string.please_install_amap));
                    }
                    break;
                case "百度地图":
                    if (PackageInfoUtils.isInstallApk(AppConst.BAIDU_MAP_HD_PACKNAME)) {//传入指定应用包名
                        startBaiduNavi(context, "", markerLatLng.latitude, markerLatLng.longitude);
                    } else {
                        CommonToast.makeText(context, context.getResources().getString(R.string.please_install_baidu_map));
                    }
                    break;
                case "腾讯地图":
                    if (PackageInfoUtils.isInstallApk(AppConst.TECENT_MAP_PACKNAME)) {//传入指定应用包名
                        startTecentMapNavi(context, markerLatLng, poiName);
                    } else {
                        CommonToast.makeText(context, context.getResources().getString(R.string.please_install_tecent_map));
                    }
                    break;
                default:
                    break;
            }
        });
    }

    /**
     * 正在导航中，先结束导航
     */
    private static void sendAMapOverNavi() {
        Intent mIntent = getAmapIntent();
        mIntent.putExtra("KEY_TYPE", AMapConstant.BROADCAST_AMAP_TYPE_EXIT_NAVI);
        AppApplication.getContext().sendBroadcast(mIntent);
    }

    /**
     * 调起高德地图导航功能，如果没安装高德地图，会进入异常，可以在异常中处理，调起高德地图app的下载页面
     */
    private static void startAMapNaviHD(Context context, LatLng latLng, String poiName) {
        // 调起高德地图导航
        try {
            Intent intent = new Intent();
            intent.setAction("AUTONAVI_STANDARD_BROADCAST_RECV");
            intent.putExtra("KEY_TYPE", 10038);
            intent.putExtra("POINAME", poiName);
            intent.putExtra("LAT", latLng.latitude);
            intent.putExtra("LON", latLng.longitude);
            intent.putExtra("DEV", 0);
            intent.putExtra("STYLE", 0);
            intent.putExtra("SOURCE_APP", "com.sitechdev.vehicle.pad");
            context.sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
            // 如果没安装会进入异常，调起下载页面
            AMapUtils.getLatestAMapApp(context);
        }
    }

    /**
     * 关闭导航
     */
    public static void closeNavi() {
        // 退出高德地图导航
        try {
            Intent intent = new Intent();
            intent.setAction("AUTONAVI_STANDARD_BROADCAST_RECV");
            intent.putExtra("KEY_TYPE", AMapConstant.BROADCAST_AMAP_TYPE_EXIT_NAVI);
            AppApplication.getContext().sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 切换地图应用到后台
     */
    public static void hideNaviClient() {
        // 退出高德地图导航
        try {
            Intent intent = new Intent();
            intent.setAction("AUTONAVI_STANDARD_BROADCAST_RECV");
            intent.putExtra("KEY_TYPE", AMapConstant.BROADCAST_AMAP_TYPE_MINI);
            AppApplication.getContext().sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 启动百度App进行导航
     *
     * @param address 目的地
     * @param lat     必填 纬度
     * @param lon     必填 经度
     */
    private static void startBaiduNavi(Context context, String address, double lat, double lon) {
        double[] doubles = {lat, lon};
        //启动路径规划页面
        Intent naviIntent = new Intent("android.intent.action.VIEW",
                android.net.Uri.parse("baidumap://map/direction?destination=" + doubles[0] + "," + doubles[1] + "&mode=driving&coord_type=gcj02"));
        context.startActivity(naviIntent);
    }

    /**
     * 启动腾讯地图App进行导航
     * 调起腾讯地图APP，显示由清华大学到怡和世家小区的驾车路线
     * qqmap://map/routeplan?type=drive&from=清华&fromcoord=39.994745,116.247282&to=怡和世家&tocoord=39.867192,116.493187&referer=OB4BZ-D4W3U-B7VVO-4PJWW-6TKDJ-WPB77
     *
     * @param context 目的地
     * @param toCoord 必填 终点经纬度
     */
    private static void startTecentMapNavi(Context context, LatLng toCoord, String poiName) {
        StringBuilder mapSbu = new StringBuilder();
        mapSbu.append("Tnavi://routePlan?")
                //起点名称,非必填
                // .append("&from=").append("起点地址")
                //起点坐标，格式：lat,lng （纬度在前，经度在后，逗号分隔）
                .append("start=").append(LocationData.getInstance().getLatitude()).append(",").append(LocationData.getInstance().getLongitude()).append(",").append(LocationData.getInstance().getFormatAddress())
                //终点名称,非必填
                //.append("&to=").append("终点地址")
                //终点坐标	tocoord=40.010024,116.392239
                .append("&dest=").append(toCoord.latitude).append(",").append(toCoord.longitude).append(",").append(poiName);
//        //启动路径规划页面
//        Intent naviIntent = new Intent("android.intent.action.VIEW", android.net.Uri.parse(mapSbu.toString()));
//        context.startActivity(naviIntent);
        Intent intent = new Intent("com.tencent.wecar.navi.intentapi");
        intent.setData(Uri.parse(mapSbu.toString()));
        context.startActivity(intent);
    }

    /**
     * 返回地点的描述信息
     */
    public static String getPoiAddressInfo(PoiItem item) {
        StringBuilder poiSbu = new StringBuilder();
        poiSbu.append(item.getProvinceName()).append("-").append(item.getCityName()).append("-").append(item.getAdName()).append("-")
                .append(item.getSnippet());
        return poiSbu.toString();
    }

    /**
     * 得到两点间的距离,米
     *
     * @param mLat
     * @param mLng
     * @param desLat
     * @param desLng
     * @return
     */
    public static String getDistance(double mLat, double mLng, double desLat, double desLng) {
        LatLng mlatLng = new LatLng(mLat, mLng);
        LatLng deslatLng = new LatLng(desLat, desLng);
        //单位：米
        String restult = "";
        float resutltF = (int) AMapUtils.calculateLineDistance(mlatLng, deslatLng);
        if (resutltF < 1000) {
            restult = (int) resutltF + "m";
        } else {
            DecimalFormat decimalFormat = new DecimalFormat(".0");//构造方法的字符格式这里如果小数不足1位,会以0补足.
            restult = decimalFormat.format(resutltF / 1000) + "km";
        }
        return restult;
    }

    /**
     * POI搜索
     *
     * @param context           上下文
     * @param keyword           关键字
     * @param maxPerPageCount   每页最大返回的数量
     * @param pageIndex         当前页码
     * @param poiSearchListener 结果回调
     */
    public static void startQueryPoiInfo(Context context, String keyword, int maxPerPageCount, int pageIndex, PoiSearch.OnPoiSearchListener poiSearchListener) {
        if (StringUtils.isEmpty(keyword)) {
            return;
        }
        PoiSearch.Query query = new PoiSearch.Query(keyword, "", "");
//keyWord表示搜索字符串，
//第二个参数表示POI搜索类型，二者选填其一，选用POI搜索类型时建议填写类型代码，码表可以参考下方（而非文字）
//cityCode表示POI搜索区域，可以是城市编码也可以是城市名称，也可以传空字符串，空字符串代表全国在全国范围内进行搜索
        query.setPageSize(maxPerPageCount);// 设置每页最多返回多少条poiitem
        query.setPageNum(pageIndex);//设置查询页码

        PoiSearch poiSearch = new PoiSearch(context, query);
//        poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(locationMarker.getPosition().latitude,
//                locationMarker.getPosition().longitude), 1000));//设置周边搜索的中心点以及半径
        poiSearch.setOnPoiSearchListener(poiSearchListener);

        //发送请求。结果通过回调接口 onPoiSearched 解析返回的结果，将查询到的 POI 以绘制点的方式显示在地图上
        poiSearch.searchPOIAsyn();
    }

    /**
     * 当前是否是POI搜索页面
     *
     * @return
     */
    public static boolean isMapActivityFront() {
        return isActivityFront(MapActivity.class);
    }

    /**
     * 当前是否是POI搜索页面
     *
     * @return
     */
    public static boolean isPoiActivityFront() {
        return isActivityFront(PoiSearchActivity.class);
    }

    /**
     * 当前是否是指定页面
     *
     * @param activityClass activity
     */
    public static boolean isActivityFront(Class activityClass) {
        if (AppVariants.currentActivity == null) {
            return false;
        }
        return AppVariants.currentActivity.getClass() == activityClass;
    }

    /**
     * 根据经纬度查询地址详情
     *
     * @param context               context
     * @param latLng                latLng
     * @param geocodeSearchListener geocodeSearchListener
     */
    public static void regeoAddressQuery(Context context, LatLng latLng, GeocodeSearch.OnGeocodeSearchListener geocodeSearchListener) {
        GeocodeSearch geocoderSearch = new GeocodeSearch(context);
        geocoderSearch.setOnGeocodeSearchListener(geocodeSearchListener);
        // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(latLng.latitude, latLng.longitude), 100, GeocodeSearch.AMAP);

        geocoderSearch.getFromLocationAsyn(query);
    }

    private static Intent getAmapIntent() {
        Intent mIntent = new Intent();
        mIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        mIntent.addCategory("com.sitechdev.vehicle.pad");
        mIntent.setAction(AMapConstant.BROADCAST_TO_AMAP);
        mIntent.setPackage("com.autonavi.amapauto");
        mIntent.addFlags(0x01000000);
        mIntent.setClassName("com.autonavi.amapauto", "com.autonavi.amapauto.adapter.internal.AmapAutoBroadcastReceiver");
        return mIntent;
    }

    /**
     * 高德初始化广播，查询家庭/公司地址
     */
    public static void sendAMapInitBroadcast() {
        SitechDevLog.i(AppConst.TAG, "发送广播给高德查询家庭地址===========>");
        Intent mIntent = getAmapIntent();
        //发给高德地图
        mIntent.putExtra("KEY_TYPE", AMapConstant.BROADCAST_AMAP_TYPE_GET_HOME_WORK_ADDRESS);
        mIntent.putExtra("EXTRA_TYPE", 1);//1:表示搜索家,2:表示搜索公司
        AppApplication.getContext().sendBroadcast(mIntent);
        SitechDevLog.i(AppConst.TAG, "发送广播给高德查询家庭地址===========>已发送==" + mIntent.toString());

        SitechDevLog.i(AppConst.TAG, "发送广播给高德查询公司地址===========>");
        //再发送一个广播
        Intent mIntent2 = getAmapIntent();
        //发给高德地图
        mIntent2.putExtra("KEY_TYPE", AMapConstant.BROADCAST_AMAP_TYPE_GET_HOME_WORK_ADDRESS);
        mIntent2.putExtra("EXTRA_TYPE", 2);//1:表示搜索家,2:表示搜索公司
        AppApplication.getContext().sendBroadcast(mIntent2);
        SitechDevLog.i(AppConst.TAG, "发送广播给高德查询公司地址===========>已发送==" + mIntent2.toString());
    }

    /**
     * 发送给高德保存家和公司地址
     *
     * @param EXTRA_TYPE 1:表示搜索家,2:表示搜索公司
     */
    public static void sendAMapAddressSave(int EXTRA_TYPE) {
        SitechDevLog.i(AppConst.TAG, "发送广播给高德保存家和公司地址===========>EXTRA_TYPE==" + EXTRA_TYPE);
        Intent mIntent = getAmapIntent();
        mIntent.putExtra("KEY_TYPE", AMapConstant.BROADCAST_AMAP_TYPE_SET_HOME_WORK_ADDRESS);
        mIntent.putExtra("LAT", (EXTRA_TYPE == 1) ? LocationData.getInstance().getHomeLatitude() : LocationData.getInstance().getWorkLatitude());
        mIntent.putExtra("LON", (EXTRA_TYPE == 1) ? LocationData.getInstance().getHomeLongitude() : LocationData.getInstance().getWorkLongitude());
        mIntent.putExtra("POINAME", (EXTRA_TYPE == 1) ? LocationData.getInstance().getHomePoiName() : LocationData.getInstance().getWorkPoiName());
        mIntent.putExtra("ADDRESS", (EXTRA_TYPE == 1) ? LocationData.getInstance().getHomeAddressName() : LocationData.getInstance().getWorkAddressName());
        mIntent.putExtra("EXTRA_TYPE", EXTRA_TYPE);
        mIntent.putExtra("DEV", 0);
        AppApplication.getContext().sendBroadcast(mIntent);
        SitechDevLog.i(AppConst.TAG, "发送广播给高德保存家和公司地址===========>已发送==" + mIntent.toString());
    }

    /**
     * 发送给高德保存家和公司地址
     *
     * @param EXTRA_TYPE 0:表示 家,1:表示 公司
     */
    public static void sendAMapAddressView(int EXTRA_TYPE) {
        SitechDevLog.i(AppConst.TAG, "发送广播给高德展示家和公司地址页面===========>EXTRA_TYPE==" + EXTRA_TYPE);
        Intent mIntent = getAmapIntent();
        mIntent.putExtra("KEY_TYPE", AMapConstant.BROADCAST_AMAP_TYPE_SET_HOME_WORK_ADDRESS_VIEW);
        mIntent.putExtra("EXTRA_TYPE", EXTRA_TYPE);
        AppApplication.getContext().sendBroadcast(mIntent);
        SitechDevLog.i(AppConst.TAG, "发送广播给高德展示家和公司地址页面===========>已发送==" + mIntent.toString());
    }

    public static void initAmapReceiver(Context context) {
//        /高德地图车镜版本（后视镜）使用该包名/
//                String pkgName = "com.autonavi.amapautolite";
//        /高德地图车机版本 使用该包名/
        String pkgName = "com.autonavi.amapauto";
        Intent launchIntent = new Intent();
        launchIntent.setComponent(new ComponentName(pkgName, "com.autonavi.auto.remote.fill.UsbFillActivity"));
        context.startActivity(launchIntent);
    }

    /**
     * 是否是地图选择场景
     *
     * @return true=是Map-Poi的选择场景
     */
    public static boolean isSelectPoiScene() {
        return MapVariants.hasSelectListScene;// && MapVariants.mPoiList.isEmpty();
    }

    public static void startPoiNaviByIndex(int poiIndex) {
        if (MapVariants.mPoiList.isEmpty() || !MapVariants.hasSelectListScene) {
            //没有可供选择的列表
            return;
        }
        if (poiIndex == 0) {
            poiIndex = 1;
        }
        if (poiIndex <= MapVariants.mPoiList.size()) {
            ThreadUtils.runOnUIThread(() -> {
                VUI.getInstance().shut();
                if (AppVariants.currentActivity != null) {
                    AppVariants.currentActivity.finish();
                }
            });
            PoiItem poiItem = MapVariants.mPoiList.get(poiIndex - 1);
            startAMapNaviHD(AppApplication.getContext(),
                    new LatLng(poiItem.getLatLonPoint().getLatitude(), poiItem.getLatLonPoint().getLongitude()),
                    poiItem.getTitle()
            );
        } else {
            ThreadUtils.runOnUIThread(() -> {
                VUI.getInstance().shutAndTTS(false, "选择错误。我仅有" + MapVariants.mPoiList.size() + "条信息，请重新选择");
            });
        }
    }
}
