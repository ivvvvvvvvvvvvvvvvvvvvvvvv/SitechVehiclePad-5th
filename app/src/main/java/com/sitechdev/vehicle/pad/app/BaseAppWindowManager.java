package com.sitechdev.vehicle.pad.app;

import com.sitechdev.vehicle.lib.event.EventBusUtils;
import com.sitechdev.vehicle.pad.event.WindowEvent;
import com.sitechdev.vehicle.pad.window.manager.AppSignalWindowManager;
import com.sitechdev.vehicle.pad.window.manager.MainControlPanelWindowManager;
import com.sitechdev.vehicle.pad.window.manager.MainMenuWindowManager;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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

    public void init() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWindowEvent(WindowEvent event) {
        Object object = event.getEventObject();
        switch (event.getEventKey()) {
            case WindowEvent.EVENT_WINDOW_MAIN_MENU:
                MainMenuWindowManager.getInstance().show();
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
                AppSignalWindowManager.getInstance().show();
                MainMenuWindowManager.getInstance().show();
                break;
            case WindowEvent.EVENT_WINDOW_APP_BACKGROUD:
                AppSignalWindowManager.getInstance().hide();
                MainMenuWindowManager.getInstance().hide();
                MainControlPanelWindowManager.getInstance().hide();
                break;
            default:
                break;
        }
    }

}
