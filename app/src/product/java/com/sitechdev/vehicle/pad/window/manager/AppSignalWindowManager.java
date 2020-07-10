package com.sitechdev.vehicle.pad.window.manager;

import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.WindowManager;

import com.sitechdev.vehicle.lib.event.EventBusUtils;
import com.sitechdev.vehicle.lib.util.NetworkUtils;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.pad.app.AppApplication;
import com.sitechdev.vehicle.pad.app.BaseAppWindowManager;
import com.sitechdev.vehicle.pad.app.BaseWindow;
import com.sitechdev.vehicle.pad.event.ScreenEvent;
import com.sitechdev.vehicle.pad.event.SysEvent;
import com.sitechdev.vehicle.pad.window.view.AppSignalView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 项目名称：SitechVehiclePad-5th
 * 类名称：AppSignalWindowManager
 * 类描述： 右上角信号windowManager
 * 创建人：Administrator
 * 创建时间：2020/05/11 0011 15:04
 * 修改时间：
 * 备注：
 */
public class AppSignalWindowManager {

    private static volatile AppSignalWindowManager manager;
    private static final String TAG = AppSignalWindowManager.class.getSimpleName();

    private WindowManager winManager;
    private WindowManager.LayoutParams params;

    /**
     * 显示的浮动窗体
     */
    public AppSignalView appSignalView;

    // 屏幕的尺寸
    private static int displayWidth;
    private static int displayHeight;

    /**
     * @return
     */
    public static AppSignalWindowManager getInstance() {
        if (manager == null) {
            synchronized (AppSignalWindowManager.class) {
                if (manager == null) {
                    manager = new AppSignalWindowManager();
                }
            }
        }
        return manager;
    }

    private AppSignalWindowManager() {
        EventBusUtils.register(this);
    }

    /**
     * 显示悬浮框
     * 如果应用处于后台则不执行
     */
    public void show() {
        if (BaseAppWindowManager.isBackground(AppApplication.getContext())) {
            SitechDevLog.e(TAG, "-------------show() fail  now is in isBackground");
            return;
        }
        showForcibly();
    }

    //显示不判断应用是否在前台
    public void showForcibly() {
        SitechDevLog.e(TAG, "-------------show()>");
        displayWidth = BaseWindow.getInstance().getDisplayWidth();
        displayHeight = BaseWindow.getInstance().getDisplayHeight();
        if (isAppSignalViewShow()) {
            return;
        }
        if (winManager == null) {
            winManager = BaseWindow.getInstance().getWinManager();
        }
        if (appSignalView == null) {
            appSignalView = getView();
        }
        initData();
        if (appSignalView.getParent() != null) {
            winManager.removeViewImmediate(appSignalView);
        }
        if (appSignalView != null && appSignalView.getParent() == null && !appSignalView.isShown()) {
            winManager.addView(appSignalView, params);
        }
    }

    /**
     * 隐藏悬浮框
     */
    public void hide() {
        SitechDevLog.e(TAG, "-------------hide()>");
        if (appSignalView != null && appSignalView.isShown()) {
            winManager.removeViewImmediate(appSignalView);
            appSignalView = null;
            params = null;
        }
    }

    public boolean isAppSignalViewShow() {
        return appSignalView != null && appSignalView.isShown();
    }

    /**
     * 获取悬浮框里面显示的view
     *
     * @return
     */
    public AppSignalView getView() {
        SitechDevLog.i(TAG, "-------------getView()>");
        if (appSignalView == null) {
            appSignalView = new AppSignalView(AppApplication.getContext());
        }
        if (params == null) {
            params = new WindowManager.LayoutParams();
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            params.format = PixelFormat.RGBA_8888;
            params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
            params.gravity = Gravity.LEFT | Gravity.TOP;

            // 须指定宽度高度信息
            params.width = appSignalView.mWidth;
            params.height = appSignalView.mHeight;
            //
            params.x = displayWidth - appSignalView.mWidth;
            params.y = 20;
            SitechDevLog.i(TAG, "-------------displayWidth>" + displayWidth);

        }
        return appSignalView;
    }

    public void usbIconShowOrHide(boolean flag) {
        appSignalView.refreshUsbIconView(flag);
    }

    public void wifiIconShowOrHide(boolean flag) {
        if (appSignalView != null) {
            appSignalView.refreshWifiIconView(flag);
        }
    }

    public void bluetoothIconShowOrHide(boolean flag) {
        if (appSignalView != null) {
            appSignalView.refreshBtIconView(flag);
        }
    }

    /**
     * 刷新信号状态。范围[0,4]： 0表示信号强度很差 ,4表示非常强的信号强度。
     *
     * @param isShow  true=显示
     * @param netRssi 范围[0,4]： 0表示信号强度很差 ,4表示非常强的信号强度。
     */
    public void tBoxIconChange(boolean isShow, int netRssi) {
        if (appSignalView != null) {
            appSignalView.refreshTboxIconView(isShow, netRssi);
        }
    }

    public void tBoxIconLevelChange(int level) {
        appSignalView.refreshTboxIconLevel(level);
    }

    public void showMuteView(boolean isMute) {
        appSignalView.refreshVoiceIconView(isMute);
    }

    public void showHotView(boolean isShow) {
        appSignalView.refreshHotIconView(isShow);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSysEventChange(SysEvent event) {
        SitechDevLog.i(TAG, this + "==消息==" + event.getEvent());
        switch (event.getEvent()) {
            case SysEvent.EB_SYS_BT_STATE:
                if (event.getObj() != null) {
                    boolean status = (boolean) event.getObj();
                    bluetoothIconShowOrHide(status);
                }
                break;
            case SysEvent.EB_SYS_MOBILE_NET_SWITCH_STATE:
                if (event.getObj() != null) {
                    boolean status = (boolean) event.getObj();
                    if (status) {
                        tBoxIconChange(true, 4);
                    } else {
                        tBoxIconChange(false, 0);
                    }
                }
                break;
            case SysEvent.EB_SYS_WIFI_STATE:
                if (event.getObj() != null) {
                    boolean status = (boolean) event.getObj();
                    wifiIconShowOrHide(status);
                }
                break;
            default:
                break;
        }
    }


    /**
     * 初始化逻辑
     */
    private void initData() {
        if (NetworkUtils.getMobileDataEnabled()) {
            tBoxIconChange(true, 4);
        } else {
            tBoxIconChange(false, 0);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onScreenEvent(ScreenEvent event) {
        switch (event.getEventKey()) {
            case ScreenEvent.EVENT_SCREEN_ORIENTATION_CHANGE:
                hide();
                show();
                break;
            default:
                break;
        }
    }
}
