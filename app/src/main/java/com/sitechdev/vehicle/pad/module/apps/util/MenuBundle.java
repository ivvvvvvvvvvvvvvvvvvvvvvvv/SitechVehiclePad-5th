package com.sitechdev.vehicle.pad.module.apps.util;

/**
 * 项目名称：SitechVehiclePad-5th
 * 类名称：MenuBundle
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2020/05/18 0018 14:44
 * 修改时间：
 * 备注：
 */
public class MenuBundle {
    /**
     * 菜单变换事件的回调接口
     */
    private MainViewMenuListener mainViewMenuListener = null;

    private MenuBundle() {
    }

    private static final class SingleMenuBundle {
        private static final MenuBundle SINGLE = new MenuBundle();
    }

    public static MenuBundle getInstance() {
        return SingleMenuBundle.SINGLE;
    }

    /**
     * 菜单变换事件的回调接口
     */
    public MainViewMenuListener getMainViewMenuListener() {
        return mainViewMenuListener;
    }

    /**
     * 菜单变换事件的回调接口
     */
    public void setMainViewMenuListener(MainViewMenuListener mainViewMenuListener) {
        this.mainViewMenuListener = mainViewMenuListener;
    }
}
