package com.sitechdev.vehicle.pad.app;

import android.app.ActivityManager;
import android.content.Context;
import android.content.IntentFilter;

import com.sitechdev.vehicle.lib.event.EventBusUtils;
import com.sitechdev.vehicle.pad.event.WindowEvent;
import com.sitechdev.vehicle.pad.manager.CommonTipWindowManager;
import com.sitechdev.vehicle.pad.receiver.OrientationReceiver;
import com.sitechdev.vehicle.pad.window.manager.AppSignalWindowManager;
import com.sitechdev.vehicle.pad.window.manager.MainControlPanelWindowManager;
import com.sitechdev.vehicle.pad.window.manager.MainMenuWindowManager;
import com.sitechdev.vehicle.pad.window.manager.TeddyWindowManager;
import com.sitechdev.vehicle.pad.window.view.PersonLoginWindow;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * 项目名称：SitechVehiclePad-5th
 * 类名称：BaseWindowManager
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2020/04/24 0024 15:57
 * 修改时间：
 * 备注：AppWindowManager 的基类，负责管理所有window的显示与隐藏
 */
public class BaseAppWindowManager {

    private BaseAppWindowManager() {
        EventBusUtils.register(this);
    }

    private static final class SingleBaseWindowManager {
        private static final BaseAppWindowManager SINGLE = new BaseAppWindowManager();
    }

    public static BaseAppWindowManager getInstance() {
        return SingleBaseWindowManager.SINGLE;
    }

    public void init(AppApplication appApplication) {
        //右上角状态window
        AppSignalWindowManager.getInstance().init(appApplication);
        //底部主菜单
        MainMenuWindowManager.getInstance().init(appApplication);
        //底部主菜单--Teddy图标
        TeddyWindowManager.getInstance().init(appApplication);
        //底部控制菜单
        MainControlPanelWindowManager.getInstance().init(appApplication);
        //登录、普通Toast弹窗
        CommonTipWindowManager.getInstance().init(appApplication);
        PersonLoginWindow.getInstance().init(appApplication);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.CONFIGURATION_CHANGED");
        OrientationReceiver receiver = new OrientationReceiver();
        AppApplication.getContext().registerReceiver(receiver, intentFilter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWindowEvent(WindowEvent event) {
        Object object = event.getEventObject();
        switch (event.getEventKey()) {
            case WindowEvent.EVENT_WINDOW_MAIN_MENU:
                MainMenuWindowManager.getInstance().show();
                TeddyWindowManager.getInstance().show();
                break;
            case WindowEvent.EVENT_WINDOW_STATUS_BAR_MENU:
                AppSignalWindowManager.getInstance().show();
                break;
            case WindowEvent.EVENT_WINDOW_CONTROL_MENU:
                if (object != null) {
                    if (object instanceof Boolean) {
                        boolean isParams = (boolean) object;
                        if (isParams) {
                            MainControlPanelWindowManager.getInstance().mustShownView();
                        } else {
                            MainControlPanelWindowManager.getInstance().mustHiddenView();
                        }
                    }
                }
                break;
            case WindowEvent.EVENT_WINDOW_APP_FRONT:
                //显示必须的所有窗口--APP切换到前台
                //showForcibly = event.getEventObject()
                if (event.getEventObject() instanceof Boolean) {
                    boolean showForcibly = (boolean) event.getEventObject();
                    if (showForcibly) {
                        AppSignalWindowManager.getInstance().showForcibly();
                        MainMenuWindowManager.getInstance().showForcibly();
                        MainControlPanelWindowManager.getInstance().showForcibly();
                        TeddyWindowManager.getInstance().show();
                        return;
                    }
                }
                AppSignalWindowManager.getInstance().show();
                MainMenuWindowManager.getInstance().show();
                TeddyWindowManager.getInstance().show();
                MainControlPanelWindowManager.getInstance().show();
                break;
            case WindowEvent.EVENT_WINDOW_APP_BACKGROUD:
                //隐藏必须的所有窗口--APP切换到后台
                AppSignalWindowManager.getInstance().hide();
                MainMenuWindowManager.getInstance().hide();
                TeddyWindowManager.getInstance().hide();
                MainControlPanelWindowManager.getInstance().hide();
                break;
            //输入法弹出时
            case WindowEvent.EVENT_WINDOW_INPUT_SHOW_STATE:
                MainMenuWindowManager.getInstance().hide();
                TeddyWindowManager.getInstance().hide();
                MainControlPanelWindowManager.getInstance().mustHiddenView();
                break;
            //输入法隐藏时
            case WindowEvent.EVENT_WINDOW_INPUT_HIDDEN_STATE:
                MainMenuWindowManager.getInstance().show();
                TeddyWindowManager.getInstance().show();
                MainControlPanelWindowManager.getInstance().show();
                break;
            default:
                break;
        }
    }

    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                /*
                BACKGROUND=400 EMPTY=500 FOREGROUND=100
                GONE=1000 PERCEPTIBLE=130 SERVICE=300 ISIBLE=200
                 */
                if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

}
