package com.sitechdev.vehicle.pad.window.manager;

import android.content.Context;
import android.graphics.PixelFormat;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.WindowManager;

import com.sitechdev.vehicle.lib.event.EventBusUtils;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.pad.app.BaseWindow;
import com.sitechdev.vehicle.pad.event.AppSignalEvent;
import com.sitechdev.vehicle.pad.event.ScreenEvent;
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
    private Context context;
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

    public void init(Context context) {
        SitechDevLog.e(TAG, "-->init");
        this.context = context;
        winManager = BaseWindow.getInstance().getWinManager();
    }

    /**
     * 显示悬浮框
     */
    public void show() {
        SitechDevLog.e(TAG, "-------------show()>");
        displayWidth = BaseWindow.getInstance().getDisplayWidth();
        displayHeight = BaseWindow.getInstance().getDisplayHeight();
        if (isAppSignalViewShow()) {
            hide();
        }
        if (appSignalView == null) {
            appSignalView = getView();
        }
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
            appSignalView = new AppSignalView(context);
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
        appSignalView.refreshWifiIconView(flag);
    }

    public void bluetoothIconShowOrHide(boolean flag) {
        appSignalView.refreshBtIconView(flag);
    }

    public void tBoxIconChange(int netRssi) {
        appSignalView.refreshTboxIconView(true, netRssi);
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

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventChange(AppSignalEvent event) {
        SitechDevLog.i(TAG, this + "==消息==" + event.getEventKey());
        Object object = event.getEventObject();
        switch (event.getEventKey()) {
            case AppSignalEvent.EVENT_SIGNAL_CHANGE_PHONE_STATE:
                break;
            case AppSignalEvent.EVENT_SIGNAL_CHANGE_WIFI_STATE:
                if (object != null) {
                    if (object instanceof Boolean) {
                        boolean isParams = (boolean) object;
                        wifiIconShowOrHide(isParams);
                    }
                }
                break;
            case AppSignalEvent.EVENT_SIGNAL_CHANGE_BLUETOOTH_STATE:
                break;
            case AppSignalEvent.EVENT_SIGNAL_CHANGE_VOLUME_STATE:
                break;
            case AppSignalEvent.EVENT_SIGNAL_CHANGE_USB_STATE:
                if (object != null) {
                    if (object instanceof Boolean) {
                        boolean isParams = (boolean) object;
                        usbIconShowOrHide(isParams);
                    }
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
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            tm.listen(new PhoneStateListener() {
                @Override
                public void onSignalStrengthsChanged(SignalStrength signalStrength) {
                    super.onSignalStrengthsChanged(signalStrength);
                    //获取网络信号强度
                    //获取0-4的5种信号级别，越大信号越好,但是api23开始才能用
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        int level = signalStrength.getLevel();
                        SitechDevLog.i(TAG, "signalStrength level====" + level);
//                        tBoxIconChange(level);
                    }
                }

                @Override
                public void onDataConnectionStateChanged(int state) {
                    super.onDataConnectionStateChanged(state);
                }
            }, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        } catch (Exception e) {
            e.printStackTrace();
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
