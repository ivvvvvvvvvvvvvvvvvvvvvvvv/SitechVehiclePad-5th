package com.sitechdev.vehicle.pad.manager;

import android.app.PendingIntent;
import android.content.Intent;

import com.sitechdev.vehicle.lib.event.AppEvent;
import com.sitechdev.vehicle.lib.event.EventBusUtils;
import com.sitechdev.vehicle.lib.util.AppUtils;
import com.sitechdev.vehicle.lib.util.CrashHandler;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.lib.util.XTIDUtil;
import com.sitechdev.vehicle.pad.app.AppApplication;
import com.sitechdev.vehicle.pad.app.AppConst;
import com.sitechdev.vehicle.pad.router.RouterConstants;
import com.sitechdev.vehicle.pad.router.RouterUtils;
import com.sitechdev.vehicle.pad.util.AppUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 项目名称：SitechVehiclePad
 * 类名称：AppManager
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2019/08/16 0016 10:58
 * 修改时间：
 * 备注：
 */
public class AppManager {

    private AppManager() {
        EventBusUtils.register(this);
    }

    private static class Single {
        private static final AppManager singleAppManager = new AppManager();
    }

    public static AppManager getInstance() {
        return Single.singleAppManager;
    }

    public void init(AppApplication appApplication) {
        //
        XTIDUtil.init(appApplication);
        //app
        AppUtils.init(appApplication);

        Intent mIntent = new Intent();
        mIntent.setAction("com.sitechdev.vehicle");
        PendingIntent intent = PendingIntent.getActivity(appApplication, 1000, mIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        CrashHandler.getInstance().init(appApplication, intent);
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEventUtil(AppEvent event) {
        SitechDevLog.i(AppConst.TAG, this + "  onEventUtil====" + event.getEventKey());
        switch (event.getEventKey()) {
            //重新登录
            case AppEvent.EVENT_APP_RELOGIN:
                AppUtil.gotoReLogin();
                break;
            //刷新token
            case AppEvent.EVENT_APP_REFRESH_TOKEN:
                break;
            //个人中心
            case AppEvent.EVENT_APP_OPEN_MEMBER_INFO_PAGE:
                RouterUtils.getInstance().navigation(RouterConstants.SUB_APP_MEMBER);
                break;
            //我的积分
            case AppEvent.EVENT_APP_MY_POINT_PAGE:
                RouterUtils.getInstance().navigation(RouterConstants.SUB_APP_MY_POINTS);
                break;
            //车辆状态
            case AppEvent.EVENT_APP_OPEN_CAR_STATUS_PAGE:
                RouterUtils.getInstance().navigation(RouterConstants.SUB_APP_CAR_STATUS);
                break;
            //模式皮肤
            case AppEvent.EVENT_APP_OPEN_SETTING_SKIN_PAGE:
                RouterUtils.getInstance().navigation(RouterConstants.SETTING_SKIN_PAGE);
                break;
            //出行计价器
            case AppEvent.EVENT_APP_OPEN_TAXI_PAGE:
                RouterUtils.getInstance().navigation(RouterConstants.SUB_APP_TAXI);
                break;
            case AppEvent.EVENT_APP_OPEN_NET_SETTING_PAGE://网络设置
                RouterUtils.getInstance().navigation(RouterConstants.SETTING_NET_PAGE);
                break;
            case AppEvent.EVENT_APP_OPEN_BT_SETTING_PAGE://蓝牙设置
                RouterUtils.getInstance().navigation(RouterConstants.SETTING_BT_PAGE);
                break;
            case AppEvent.EVENT_APP_OPEN_VOICE_SETTING_PAGE://语音设置
                RouterUtils.getInstance().navigation(RouterConstants.SETTING_TEDDY_PAGE);
                break;
            case AppEvent.EVENT_APP_OPEN_SYS_SETTING_PAGE://系统设置
                RouterUtils.getInstance().navigation(RouterConstants.SETTING_SYSTEM_PAGE);
                break;
            case AppEvent.EVENT_APP_OPEN_SETTING_PAGE:// 设置
                RouterUtils.getInstance().navigation(RouterConstants.SETTING_HOME_PAGE);
                break;
            default:
                break;
        }
    }
}
