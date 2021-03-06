package com.sitechdev.vehicle.pad.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.blankj.utilcode.util.ActivityUtils;
import com.sitechdev.vehicle.pad.bean.AllModuleBean;

/**
 * 项目名称：SitechVehiclePad-5th
 * 类名称：JumpUtil
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2020/05/08 0008 15:29
 * 修改时间：
 * 备注：
 */
public class JumpUtils {
    public static String TITLE_NAME = "title_name";

    public static void jumpActivity(Class toActivityClass) {
        jumpActivity(toActivityClass, null);
    }

    public static void jumpActivity(Class toActivityClass, Bundle bundle) {
        Activity currentActivity = ActivityUtils.getTopActivity();
        if (currentActivity != null) {
            Intent mIntent = new Intent(currentActivity, toActivityClass);
            if (bundle != null) {
                mIntent.putExtras(bundle);
            }
            currentActivity.startActivity(mIntent);
        }
    }

    public static void jump(Context context, AllModuleBean.ModuleBean bean) {
//        try {
//            int jumpType = bean.jumpType;

            //对酷我单独做处理，逻辑如下：判断是否登录，若登录直接进入酷我；若未登录，走正常的下面的逻辑。
//            if (RouterConstants.THIRD_APP_KUWO.equals(bean.appRoute) && LoginUtils.isLogin()) {
//                KuwoUtil.startKuwoApp(true);
//                EventBusUtils.postEvent(new TeddyEvent(TeddyEvent.EB_TEDDY_SCENE_EVENT_CHANGE, TeddyEvent.TEDDY_SCENE_KU_WO));
//                return;
//            }

            //判断是否需要网络
//            if (checkNetGroup(bean.appRoute) && !(NetworkUtils.isNetworkAvailable(context))) {
//                Bundle bundle = new Bundle();
//                bundle.putString(TITLE_NAME, bean.appName);
//                SubActivity.start(context, RouterConstants.NO_NET, bundle);
//                return;
//            }

//            // 判断是否需要登录
//            if (checkLoginGroup(bean.appRoute)) {
//                PersonLoginWindow.getInstance().showWnd(() -> toJump(context, jumpType, bean));
//                return;
//            }

//            toJump(context, jumpType, bean);
//        } catch (Exception e) {
//            ToastUtils.showShort(e.getMessage());
//        }
    }

    /**
     * 页面跳转
     */
    private static void toJump(Context context, int jumpType, AllModuleBean.ModuleBean bean) {
//        if (jumpType == 0) {
//            GA10App.fromSetting = StringUtils.isEquals(RouterConstants.FRAGMENT_SETTING, bean.appRoute);
//            if (StringUtils.isEquals(RouterConstants.FRAGMENT_PERSONAL, bean.appRoute)) {
//                Bundle bundle1 = new Bundle();
//                bundle1.putBoolean(AppConstants.KEY_FRAG_LAUNCHER_MODE, true);
//                SubActivity.start(context, RouterConstants.FRAGMENT_PERSONAL, bundle1);
//            } else {
//                SubActivity.start(context, bean.appRoute);
//            }
//        } else if (jumpType == 1) {
//            switch (bean.appRoute) {
//                case RouterConstants.FRAGMENT_CAR_CONTROL:
//                    CarControlWindow.getInstance().showCarSetWnd(false);
//                    break;
//
//                case RouterConstants.THIRD_APP_KUWO:
//                    KuwoUtil.startKuwoApp(true);
//                    EventBusUtils.postEvent(new TeddyEvent(TeddyEvent.EB_TEDDY_SCENE_EVENT_CHANGE, TeddyEvent.TEDDY_SCENE_KU_WO));
//                    break;
//                case RouterConstants.THIRD_APP_CARLIFE:
//                    CarLifeManager.getInstance().showTip(new CarLifeManager.OnDialogListener() {
//                        @Override
//                        public void onPositiveListener() {
//                            TeddyUtil.closeOtherMedia();
//                            EventBusUtils.postEvent(new SysEvent(SysEvent.EB_CAR_LIFE_START));
//                        }
//                    });
//
//                    break;
//                case RouterConstants.THIRD_APP_MAP:
//                    MapDelegate.INSTANCE.startMap();
//                    break;
//                default:
//                    break;
//            }
//        } else if (jumpType == 2) {
//            if (StringUtils.isEquals(RouterConstants.FRAGMENT_AIR_CONTROL, bean.appRoute)) {
//                if (!AirWindow.getInstance().isShow()) {
//                    AirWindow.getInstance().showAirWnd(false);
//                }
//            }
//        }
    }

    private static boolean checkNetGroup(String appRoute) {
        return true;//appRoute.contains("needNet");
    }

    /**
     * 判断登录组是否登录
     */
    public static boolean checkLoginGroup(String appRoute) {
//        boolean shouldLogin = appRoute.startsWith("/needLogin");
//        return shouldLogin && !LoginUtils.isLogin();
            return true;
    }
}
