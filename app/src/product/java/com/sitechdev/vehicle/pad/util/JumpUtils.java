package com.sitechdev.vehicle.pad.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.blankj.utilcode.util.ActivityUtils;
import com.sitechdev.vehicle.lib.util.NetworkUtils;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.lib.util.StringUtils;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.bean.AllModuleBean;
import com.sitechdev.vehicle.pad.manager.KuwoManager;
import com.sitechdev.vehicle.pad.manager.MapManager;
import com.sitechdev.vehicle.pad.module.apps.util.AppsMenuConfig;
import com.sitechdev.vehicle.pad.module.login.util.LoginUtils;
import com.sitechdev.vehicle.pad.router.RouterConstants;
import com.sitechdev.vehicle.pad.router.RouterUtils;
import com.sitechdev.vehicle.pad.window.view.PersonLoginWindow;

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

    /**
     * 指定path跳转
     *
     * @param context
     * @param path
     */
    public static void jumpFromActivity(Context context, String path) {
        if (StringUtils.isEmpty(path) || AppsMenuConfig.mAllMenuBean == null || AppsMenuConfig.mAllMenuBean.apps == null || AppsMenuConfig.mAllMenuBean.apps.isEmpty()) {
            return;
        }

        for (int i = 0; i < AppsMenuConfig.mAllMenuBean.apps.size(); i++) {
            AllModuleBean.ModuleBean bean = AppsMenuConfig.mAllMenuBean.apps.get(i);
            if (path.equalsIgnoreCase(bean.appRoute)) {
                jump(context, bean);
                break;
            }
        }
    }

    public static void jump(Context context, AllModuleBean.ModuleBean bean) {
        try {
            int jumpType = bean.jumpType;
            Uri pathUri = Uri.parse(bean.appRoute);

            //对酷我单独做处理，逻辑如下：判断是否登录，若登录直接进入酷我；若未登录，走正常的下面的逻辑。
//            if (RouterConstants.THIRD_APP_KUWO.equals(bean.appRoute) && LoginUtils.isLogin()) {
//                KuwoUtil.startKuwoApp(true);
//                EventBusUtils.postEvent(new TeddyEvent(TeddyEvent.EB_TEDDY_SCENE_EVENT_CHANGE, TeddyEvent.TEDDY_SCENE_KU_WO));
//                return;
//            }

            if (checkIsDeveloping(pathUri)) {
                CommonUtil.showToast(R.string.app_developing);
                return;
            }

//            判断是否需要网络
            if (checkNetGroup(pathUri) && !(NetworkUtils.isNetworkAvailable(context))) {
//                Bundle bundle = new Bundle();
//                bundle.putString(TITLE_NAME, bean.appName);
//                SubActivity.start(context, RouterConstants.NO_NET, bundle);
                CommonUtil.showToast(R.string.tip_no_net);
                return;
            }

            // 判断是否需要登录
            if (checkLoginGroup(pathUri)) {
                PersonLoginWindow.getInstance().showWnd(() -> toJump(context, jumpType, bean));
                return;
            }

            toJump(context, jumpType, bean);
        } catch (Exception e) {
//            ToastUtils.showShort(e.getMessage());
            SitechDevLog.exception(e);
        }
    }

    /**
     * 页面跳转
     *
     * @param context  context
     * @param jumpType 跳转标识
     *                 * 0->跳转本地页面
     *                 * 1->跳转三方应用，具体跳转页面根据appRoute判断
     *                 * 2->window展示，eg:空调
     * @param bean     实体类
     */
    private static void toJump(Context context, int jumpType, AllModuleBean.ModuleBean bean) {
        if (jumpType == 0) {
            RouterUtils.getInstance().navigation(Uri.parse(bean.appRoute));
        } else if (jumpType == 1) {
            switch (bean.appRoute) {
                case RouterConstants.FRAGMENT_CAR_CONTROL:
//                    CarControlWindow.getInstance().showCarSetWnd(false);
                    break;

                case RouterConstants.THIRD_APP_KUWO:
                    KuwoManager.getInstance().startKuwoApp(true);
//                    EventBusUtils.postEvent(new TeddyEvent(TeddyEvent.EB_TEDDY_SCENE_EVENT_CHANGE, TeddyEvent.TEDDY_SCENE_KU_WO));
                    break;
                case RouterConstants.THIRD_APP_CARLIFE:
//                    CarLifeManager.getInstance().showTip(new CarLifeManager.OnDialogListener() {
//                        @Override
//                        public void onPositiveListener() {
//                            TeddyUtil.closeOtherMedia();
//                            EventBusUtils.postEvent(new SysEvent(SysEvent.EB_CAR_LIFE_START));
//                        }
//                    });

                    break;
                case RouterConstants.THIRD_APP_MAP:
                    MapManager.getInstance().startMap();
                    break;
                default:
                    break;
            }
        } else if (jumpType == 2) {
//            if (StringUtils.isEquals(RouterConstants.FRAGMENT_AIR_CONTROL, bean.appRoute)) {
//                if (!AirWindow.getInstance().isShow()) {
//                    AirWindow.getInstance().showAirWnd(false);
//                }
//            }
        }
    }

    private static boolean checkNetGroup(Uri appRoute) {
        String isNetParam = appRoute.getQueryParameter("needNet");
        return !StringUtils.isEmpty(isNetParam) && Boolean.parseBoolean(isNetParam);
    }

    /**
     * 判断登录组是否登录
     */
    public static boolean checkLoginGroup(Uri appRoute) {
        String isLoginParam = appRoute.getQueryParameter("needLogin");
        return !StringUtils.isEmpty(isLoginParam) && Boolean.parseBoolean(isLoginParam) && !LoginUtils.isLogin();
    }

    /**
     * 是否还在开发中
     *
     * @param appRoute
     * @return
     */
    public static boolean checkIsDeveloping(Uri appRoute) {
        String isDeveloppingParam = appRoute.getQueryParameter("developing");
        return !StringUtils.isEmpty(isDeveloppingParam) && Boolean.parseBoolean(isDeveloppingParam);
    }

    /**
     * 跳往AndroidLauncher3
     */
    public static void jumpAndroidLauncher() {
        String pkg = "com.android.launcher3";
        String cls = "com.android.launcher3.Launcher";
        ComponentName componet = new ComponentName(pkg, cls);
        Intent intent = new Intent();
        intent.setComponent(componet);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        ActivityUtils.startActivity(intent);
        ActivityUtils.getTopActivity().startActivity(intent);
    }
}
