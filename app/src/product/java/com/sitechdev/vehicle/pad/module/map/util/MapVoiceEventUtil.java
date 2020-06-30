package com.sitechdev.vehicle.pad.module.map.util;

import android.content.Intent;
import android.content.IntentFilter;

import com.amap.api.maps.model.LatLng;
import com.sitechdev.vehicle.lib.event.EventBusUtils;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.lib.util.ThreadUtils;
import com.sitechdev.vehicle.pad.app.AppApplication;
import com.sitechdev.vehicle.pad.app.AppConst;
import com.sitechdev.vehicle.pad.event.MapEvent;
import com.sitechdev.vehicle.pad.event.PoiEvent;
import com.sitechdev.vehicle.pad.event.VoiceEvent;
import com.sitechdev.vehicle.pad.module.main.MainActivity;
import com.sitechdev.vehicle.pad.module.map.MapActivity;
import com.sitechdev.vehicle.pad.module.map.SetAddressActivity;
import com.sitechdev.vehicle.pad.module.map.constant.AMapConstant;
import com.sitechdev.vehicle.pad.module.map.receiver.MapReceiver;
import com.sitechdev.vehicle.pad.module.splash.SplashActivity;
import com.sitechdev.vehicle.pad.util.AppVariants;
import com.sitechdev.vehicle.pad.view.CommonToast;
import com.sitechdev.vehicle.pad.vui.VUI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;

/**
 * 项目名称：SitechVehiclePad
 * 类名称：MapVoiceEventUtil
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2019/08/14 0014 17:06
 * 修改时间：
 * 备注：
 */
public class MapVoiceEventUtil {

    private MapVoiceEventUtil() {
        EventBusUtils.register(this);
        registerMapReceiver();
    }

    private static class SingleMapVoiceUtil {
        private static final MapVoiceEventUtil SINGLE = new MapVoiceEventUtil();
    }

    public static MapVoiceEventUtil getInstance() {
        return SingleMapVoiceUtil.SINGLE;
    }

    public void init() {
        SitechDevLog.i(AppConst.TAG, this + "==========地图语音事件注册==========");
    }

    private void registerMapReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(AMapConstant.BROADCAST_FROM_AMAP);
        AppApplication.getContext().registerReceiver(new MapReceiver(), filter);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onVoiceEvent(PoiEvent poiEvent) {
        SitechDevLog.i(AppConst.TAG, this + "==地图语音消息==" + poiEvent.toString());
        switch (poiEvent.getEventKey()) {
            case PoiEvent.EVENT_QUERY_POI_KEYWORD:
            case PoiEvent.EVENT_QUERY_NEARBY_POI_KEYWORD:
                if (MapUtil.isMapActivityFront() || MapUtil.isPoiActivityFront()) {
                    //是POI页面或地图页面，不做处理，交由相应页面的事件响应处理
                    SitechDevLog.i(AppConst.TAG, this + " == " + poiEvent.toString() + " ==不做处理，交由POI页面的事件响应处理 ");
                    return;
                }
                Intent mIntent = new Intent();
                //展示poi类型
                mIntent.putExtra(AppConst.JUMP_MAP_TYPE, AppConst.MAP_POI_SHOW_TYPE);
                //poi点内容--天安门
                mIntent.putExtra(AppConst.JUMP_MAP_DATA, (String) poiEvent.getEventValue());
                //因POI的返回规则,先打开地图页面，再通过地图跳转POI搜索
                mIntent.setClass(AppVariants.currentActivity, MapActivity.class);
                //先StartActivity poiActivity，再通过Intent传值
                ThreadUtils.runOnUIThread(() -> {
                    if (AppVariants.currentActivity != null) {
                        AppVariants.currentActivity.startActivity(mIntent);
                    }
                });
                break;
            //poi列表选择
            case PoiEvent.EVENT_QUERY_POI_INDEX:
                MapUtil.startPoiNaviByIndex(Integer.parseInt(poiEvent.getEventValue()));
                break;
            default:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onVoiceEvent(MapEvent event) {
        SitechDevLog.i(AppConst.TAG, this + "==地图语音消息==" + event.toString());
        //先StartActivity poiActivity，再通过Intent传值
        switch (event.getEventKey()) {
            //打开地图
            case MapEvent.EVENT_OPEN_MAP:
                if (MapUtil.isMapActivityFront() || MapUtil.isPoiActivityFront()) {
                    //是POI页面或地图页面，不做处理，交由相应页面的事件响应处理
                    SitechDevLog.i(AppConst.TAG, this + " == " + event.toString() + " ==不做处理，交由POI页面的事件响应处理 ");
                    return;
                }
                Intent mIntent = new Intent();
                //因POI的返回规则,先打开地图页面，再通过地图跳转POI搜索
                mIntent.setClass(AppVariants.currentActivity, MapActivity.class);
                ThreadUtils.runOnUIThread(() -> {
                    if (AppVariants.currentActivity != null) {
                        AppVariants.currentActivity.startActivity(mIntent);
                    }
                });
                break;
            case MapEvent.EVENT_MAP_START_NAVI:
                LatLng mLatlng = (LatLng) event.getEventValue();
                ThreadUtils.runOnUIThread(() -> {
                    MapUtil.showNaviSelectClientDialog(AppVariants.currentActivity, mLatlng, "test");
                });
                break;
            //设置为公司
            case MapEvent.EVENT_MAP_NAVI_SET_HOME_ADDR:
                EventBusUtils.postEvent(new VoiceEvent(VoiceEvent.EVENT_VOICE_TTS_PLAY_TEXT, "请设置您的家庭地址"));
                Intent mIntent3 = new Intent(AppVariants.currentActivity, SetAddressActivity.class);
                mIntent3.putExtra(AppConst.ADDRESS_SET_TYPE, AppConst.HOME_ADDRESS_SET_INDEX);
                if (AppVariants.currentActivity != null) {
                    AppVariants.currentActivity.startActivity(mIntent3);
                }
                break;
            //设置为家
            case MapEvent.EVENT_MAP_NAVI_SET_WORK_ADDR:
                EventBusUtils.postEvent(new VoiceEvent(VoiceEvent.EVENT_VOICE_TTS_PLAY_TEXT, "请设置您的公司地址"));
                Intent mIntent2 = new Intent(AppVariants.currentActivity, SetAddressActivity.class);
                mIntent2.putExtra(AppConst.ADDRESS_SET_TYPE, AppConst.COMPONY_ADDRESS_SET_INDEX);
                if (AppVariants.currentActivity != null) {
                    AppVariants.currentActivity.startActivity(mIntent2);
                }
                break;
            //关闭导航
            case MapEvent.EVENT_MAP_CLOSE_NAVI:
                if (MapUtil.isMapActivityFront() || MapUtil.isPoiActivityFront()) {
                    //是POI页面或地图页面，不做处理，交由相应页面的事件响应处理
                    SitechDevLog.i(AppConst.TAG, this + " == " + event.toString() + " ==不做处理，交由POI页面的事件响应处理 ");
                    Intent intent = new Intent(AppVariants.currentActivity, MainActivity.class);
                    startActivity(intent);
                }
                MapUtil.closeNavi();
                MapUtil.hideNaviClient();
                ThreadUtils.runOnUIThread(() -> VUI.getInstance().shutAndTTS("已为您关闭导航"));
                break;
            case MapEvent.EVENT_MAP_START_NAVI_HOME:
                //导航回家
                if (!LocationData.getInstance().isHasHomeAddress()) {
                    ThreadUtils.runOnUIThread(() -> {
                        CommonToast.makeText(AppApplication.getContext(), "您还未设置家庭地址");
                        Intent mIntent1 = new Intent(AppVariants.currentActivity, SetAddressActivity.class);
                        mIntent1.putExtra(AppConst.ADDRESS_SET_TYPE, AppConst.HOME_ADDRESS_SET_INDEX);
                        if (AppVariants.currentActivity != null) {
                            AppVariants.currentActivity.startActivity(mIntent1);
                        }
                    });
                    return;
                }
                ThreadUtils.runOnUIThread(() -> {
                    MapUtil.showNaviSelectClientDialog(AppVariants.currentActivity,
                            new LatLng(LocationData.getInstance().getHomeLatitude(),
                                    LocationData.getInstance().getHomeLongitude()),
                            LocationData.getInstance().getHomeAddressName());
                });
                break;
            case MapEvent.EVENT_MAP_START_NAVI_COMPONY:
                //导航回公司
                if (!LocationData.getInstance().isHasWorkAddress()) {
                    ThreadUtils.runOnUIThread(() -> {
                        CommonToast.makeText(AppApplication.getContext(), "您还未设置公司地址");
                        Intent mIntent4 = new Intent(AppVariants.currentActivity, SetAddressActivity.class);
                        mIntent4.putExtra(AppConst.ADDRESS_SET_TYPE, AppConst.COMPONY_ADDRESS_SET_INDEX);
                        if (AppVariants.currentActivity != null) {
                            AppVariants.currentActivity.startActivity(mIntent4);
                        }
                    });
                    return;
                }
                ThreadUtils.runOnUIThread(() -> {
                    MapUtil.showNaviSelectClientDialog(AppVariants.currentActivity,
                            new LatLng(LocationData.getInstance().getWorkLatitude(),
                                    LocationData.getInstance().getWorkLongitude()),
                            LocationData.getInstance().getWorkAddressName());
                });
                break;
            //在地图页面展现POI
            case MapEvent.EVENT_OPEN_MAP_SHOW_POI:
                if (MapUtil.isMapActivityFront()) {
                    //是POI页面或地图页面，不做处理，交由相应页面的事件响应处理
                    SitechDevLog.i(AppConst.TAG, this + " == " + event.toString() + " ==不做处理，交由POI页面的事件响应处理 ");
                    return;
                }
                if (MapUtil.isPoiActivityFront()) {
                    if (AppVariants.currentActivity != null) {
                        AppVariants.currentActivity.finish();
                    }
                }
                //打开地图页面，描绘5个marker
                Intent mIntent5 = new Intent();
                //因POI的返回规则,先打开地图页面，再通过地图跳转POI搜索
                mIntent5.setClass(AppVariants.currentActivity, MapActivity.class);
                //展示poi类型
                mIntent5.putExtra(AppConst.JUMP_MAP_TYPE, AppConst.MAP_POI_SHOW_TYPE);
                //poi点内容--天安门
                mIntent5.putExtra(AppConst.JUMP_MAP_DATA, (String) event.getEventValue());

                ThreadUtils.runOnUIThread(() -> {
                    if (AppVariants.currentActivity != null) {
                        AppVariants.currentActivity.startActivity(mIntent5);
                    }
                });
                break;
            default:
                break;
        }
    }
}
