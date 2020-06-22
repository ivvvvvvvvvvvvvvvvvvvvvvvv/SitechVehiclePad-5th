package com.sitechdev.vehicle.pad.module.map.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.sitechdev.vehicle.lib.event.EventBusUtils;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.lib.util.StringUtils;
import com.sitechdev.vehicle.pad.app.AppConst;
import com.sitechdev.vehicle.pad.event.MapEvent;
import com.sitechdev.vehicle.pad.module.map.constant.AMapConstant;
import com.sitechdev.vehicle.pad.module.map.util.LocationData;
import com.sitechdev.vehicle.pad.module.map.util.MapUtil;
import com.sitechdev.vehicle.pad.module.map.util.MapVariants;

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
//        SitechDevLog.i(AppConst.TAG, "广播数据解析===>keyType=" + keyType);
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
            default:
                break;
        }
        SitechDevLog.i(AppConst.TAG, "接收到广播===>OVER=================================");
    }
}
