package com.sitechdev.vehicle.pad.module.map.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.sitechdev.vehicle.lib.event.EventBusUtils;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.lib.util.StringUtils;
import com.sitechdev.vehicle.pad.app.AppConst;
import com.sitechdev.vehicle.pad.event.MapEvent;
import com.sitechdev.vehicle.pad.manager.MapManager;
import com.sitechdev.vehicle.pad.module.map.constant.AMapConstant;
import com.sitechdev.vehicle.pad.module.map.util.LocationData;
import com.sitechdev.vehicle.pad.module.map.util.MapUtil;
import com.sitechdev.vehicle.pad.module.map.util.MapVariants;
import com.sitechdev.vehicle.pad.vui.VUI;
import com.sitechdev.vehicle.pad.window.manager.TeddyWindowManager;

/**
 * 项目名称：SitechVehiclePad
 * 类名称：MapReceiver
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2019/08/20 0020 16:58
 * 修改时间：
 * 备注：
 */
public class MapReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            return;
        }

        String action = intent.getAction();
        SitechDevLog.i(AppConst.TAG, "接收到广播===>action=" + action);
        switch (action) {
            case AMapConstant.BROADCAST_FROM_AMAP:
                parseAmapReceInfo(intent);
                break;
            default:
                break;
        }
    }
    /**
     * 解析广播数据
     *
     * @param intent intent
     */
    private void parseAmapReceInfo(Intent intent) {
        int keyType = intent.getIntExtra("KEY_TYPE", 0);
//        SitechDevLog.i("zyf", "广播数据解析===>keyType=" + keyType);
        switch (keyType) {
            case AMapConstant.BROADCAST_AMAP_TYPE_GET_HOME_WORK_ADDRESS_RESULT:
                //在auto启动/未启动的情况下，第三方可调用该接口设置家/公司的地址。获取结果
                /**
                 * POINAME:poi名称 (String)
                 * LON:经度参数（double）
                 * LAT:纬度参数（double）
                 * DISTANCE:距离 (int)
                 * CATEGORY:类别(1:表示家,2:表示公司)（int）
                 * ADDRESS:地址(String)
                 */
                int cateoryCode = intent.getIntExtra("CATEGORY", -1);
                String poiName = intent.getStringExtra("POINAME");
                String address = intent.getStringExtra("ADDRESS");
                double addressLat = intent.getDoubleExtra("LAT", 0d);
                double addressLong = intent.getDoubleExtra("LON", 0d);
                boolean hasAddressOk = (!StringUtils.isEmpty(poiName) || !StringUtils.isEmpty(address)) && (addressLat != 0) && (addressLong != 0);
                SitechDevLog.i(AppConst.TAG, "广播数据解析===>cateoryCode=" + cateoryCode
                        + ", hasAddressOk=" + hasAddressOk
                        + ", POINAME=" + poiName
                        + ", ADDRESS=" + address
                        + ", LAT=" + addressLat
                        + ", LON=" + addressLong);
                if (cateoryCode == 1) {
                    //家庭地址
                    LocationData.getInstance().setHasHomeAddress(hasAddressOk);
                    LocationData.getInstance().setHomePoiName(poiName);
                    LocationData.getInstance().setHomeAddressName(address);
                    LocationData.getInstance().setHomeLatitude(addressLat);
                    LocationData.getInstance().setHomeLongitude(addressLong);
                } else if (cateoryCode == 2) {
                    //公司地址
                    LocationData.getInstance().setHasWorkAddress(hasAddressOk);
                    LocationData.getInstance().setWorkPoiName(poiName);
                    LocationData.getInstance().setWorkAddressName(address);
                    LocationData.getInstance().setWorkLatitude(addressLat);
                    LocationData.getInstance().setWorkLongitude(addressLong);
                }
                EventBusUtils.postEvent(new MapEvent(MapEvent.EVENT_MAP_HOME_WORK_ADDRESS_RESULT));
                break;
            case AMapConstant.BROADCAST_AMAP_TYPE_SET_HOME_WORK_ADDRESS_RESULT:
                //在auto启动/未启动的情况下，第三方可调用该接口设置家/公司的地址。
                /**
                 * CATEGORY:类别(1:表示家,2:表示公司) （int）
                 * EXTRA_RESPONSE_CODE （0：成功， 1：失败）（int）
                 */
                int cateoryCode1 = intent.getIntExtra("CATEGORY", -1);
                int responseCode = intent.getIntExtra("EXTRA_RESPONSE_CODE", 1);
                SitechDevLog.i(AppConst.TAG, "广播数据解析===>cateoryCode1=" + cateoryCode1);
                SitechDevLog.i(AppConst.TAG, "广播数据解析===>responseCode=" + responseCode);
                if (cateoryCode1 == 1) {
                    //家庭地址
                    LocationData.getInstance().setHasHomeAddress(responseCode == 0);
                } else if (cateoryCode1 == 2) {
                    //公司地址
                    LocationData.getInstance().setHasWorkAddress(responseCode == 0);
                }
                break;
            case AMapConstant.BROADCAST_AMAP_TYPE_NAVI_STATE:
                //当导航发生状态变更时，将相应的状态通知给系统。
                /**
                 * EXTRA_STATE：运行状态（int）
                 */
                int EXTRA_STATE = intent.getIntExtra("EXTRA_STATE", -1);
                SitechDevLog.i(AppConst.TAG, "广播数据解析===>EXTRA_STATE=" + EXTRA_STATE);
                switch (EXTRA_STATE) {
                    case 8:
                    case 10:
                        //导航已成功完成中
                        MapVariants.isMapNavi_Over = false;
                        //开始导航中
                        MapVariants.isMapNavi_Running = true;
                        break;
                    case 9:
                    case 12:
                        //结束导航中
                        MapVariants.isMapNavi_Running = false;
                        break;
                    case 39:
                        //导航已成功完成中
                        MapVariants.isMapNavi_Over = true;
                        break;
                    case 26:
                        //收藏夹家信息变更通知
                    case 27:
                        //收藏夹公司信息变更通知
                        MapUtil.sendAMapInitBroadcast();
                        break;
                    default:
                        break;
                }
                break;
            /**
             * EXTRA_POINUM
             *
             * 10 (列表POI个数)
             *
             * int
             *
             * EXTRA_IS_FIRST_PAGE
             *
             * true 或 false （是否第一页）
             *
             * boolean
             *
             * EXTRA_IS_LAST_PAGE
             *
             * true 或 false （是否最后一页）
             *
             * boolean
             *
             * EXTRA_CHOICE
             *
             * -1,0，1，2, 3 ...(用户手动POI结果选择,最多十项，索引从0开始,-1为没有手动选择操作）
             *
             * int
             *
             * EXTRA_PLAN_ROUTE
             *
             * true 或 false （ 是否点击‘去这里’规划路线操作）
             *
             * boolean
             *
             * EXTRA_BACK
             *
             * true 或 false （是否点击返回按键）
             *
             * boolean
             *
             * EXTRA_IS_LIST_TOP
             *
             * true 或 false （是否处于列表顶部）
             *
             * boolean
             *
             * EXTRA_IS_LIST_BOTTOM
             *
             * true 或 false （是否处于列表底部）
             *
             * boolean
             */
            case AMapConstant.BROADCAST_AMAP_TYPE_SEARCH_RESULT:
//                SitechDevLog.i("zyf", "接收到广播===> extra_choice  =  " +  intent.getIntExtra("EXTRA_CHOICE", -1));
                int extra_poinum = intent.getIntExtra("EXTRA_POINUM", -1);
                int extra_choice = intent.getIntExtra("EXTRA_CHOICE", -1);
                boolean extra_is_first_page = intent.getBooleanExtra("EXTRA_IS_FIRST_PAGE", false);
                boolean extra_is_last_page = intent.getBooleanExtra("EXTRA_IS_LAST_PAGE", false);
                boolean extra_plan_route = intent.getBooleanExtra("EXTRA_PLAN_ROUTE", false);
                boolean extra_back = intent.getBooleanExtra("EXTRA_BACK", false);
                boolean extra_is_list_top = intent.getBooleanExtra("EXTRA_IS_LIST_TOP", false);
                boolean extra_is_list_bottom = intent.getBooleanExtra("EXTRA_IS_LIST_BOTTOM", false);
                break;
            case AMapConstant.BROADCAST_AMAP_TYPE_SEARCH_INFO_RESULT:
//                SitechDevLog.i("zyf", "接收到广播===10042> =");
                /**
                 * 0：搜索成功
                 * 1：搜索无结果
                 * 2：未知错误
                 * 3：无网络，且无地图数据
                 * 4：当前无路线，无法进行沿途搜
                 * 5：当前城市无结果，但有城市建议
                 */
                int result = intent.getIntExtra("SEARCH_RESULT_CODE", -1);
                if (result == 0) {
                    String json = intent.getStringExtra("EXTRA_RESULT");
                    int size = MapManager.getInstance().setSearchResult(json);
                    TeddyWindowManager.getInstance().show();
                    VUI.getInstance().shutAndTTS(false, "为您找到" + size + "个结果，请对我说第几个");
//                    SitechDevLog.i("zyf", "接收到广播===> ="+json);
                }

                break;
            default:
                break;
        }
        SitechDevLog.i(AppConst.TAG, "接收到广播===>OVER=================================");
    }
}
