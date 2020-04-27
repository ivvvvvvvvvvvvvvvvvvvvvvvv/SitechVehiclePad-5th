package com.sitechdev.vehicle.pad.window.manager;

import android.content.Context;
import android.graphics.PixelFormat;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.WindowManager;

import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.pad.app.AppConst;
import com.sitechdev.vehicle.pad.app.BaseWindow;
import com.sitechdev.vehicle.pad.event.RightTopEvent;
import com.sitechdev.vehicle.pad.window.view.RightTopView;
import com.tencent.mars.xlog.Log;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class RightTopWindowManager {

    private static volatile RightTopWindowManager manager;
    private static final String TAG = RightTopWindowManager.class.getSimpleName();

    private WindowManager winManager;
    private Context context;
    private WindowManager.LayoutParams params;

    /**
     * 显示的浮动窗体
     */
    public RightTopView rightTopView;

    // 屏幕的尺寸
    private static int displayWidth;
    private static int displayHeight;

    /**
     * @return
     */
    public static RightTopWindowManager getInstance() {
        if (manager == null) {
            synchronized (RightTopWindowManager.class) {
                if (manager == null) {
                    manager = new RightTopWindowManager();
                }
            }
        }
        return manager;
    }

    private RightTopWindowManager() {
    }

    public void init(Context context) {
        Log.e("RightTop", "-------------init()>");
        Log.e(TAG, "-->init");
        this.context = context;
//        winManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        displayWidth = winManager.getDefaultDisplay().getWidth();
//        displayHeight = winManager.getDefaultDisplay().getHeight();
        winManager = BaseWindow.getInstance().getWinManager();
        displayWidth = BaseWindow.getInstance().getDisplayWidth();
        displayHeight = BaseWindow.getInstance().getDisplayHeight();
        rightTopView = getView();
        //        DL.i(TeddyConstants.TAG_TEDDY, "宽度====>" + displayWidth + "，高度======>" + displayHeight);
        initData();
    }

    /**
     * 显示悬浮框
     */
    public void show() {
        Log.e("RightTop", "-------------show()>");
        if (isRightTopViewShow()) {
            return;
        }
        if (rightTopView == null) {
            rightTopView = getView();
        }
        if (rightTopView.getParent() != null) {
            winManager.removeViewImmediate(rightTopView);
        }
        if (rightTopView != null && rightTopView.getParent() == null && !rightTopView.isShown()) {
            winManager.addView(rightTopView, params);
        }
    }

    /**
     * 隐藏悬浮框
     */
    public void hide() {
        Log.e("RightTop", "-------------hide()>");
        if (rightTopView != null && rightTopView.isShown()) {
            winManager.removeViewImmediate(rightTopView);
        }
    }

    public boolean isRightTopViewShow() {
        return rightTopView != null && rightTopView.isShown();
    }

    /**
     * 获取悬浮框里面显示的view
     *
     * @return
     */
    public RightTopView getView() {
        SitechDevLog.i("RightTop", "-------------getView()>");
        if (rightTopView == null) {
            rightTopView = new RightTopView(context);
        }
        if (params == null) {
            params = new WindowManager.LayoutParams();
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            params.format = PixelFormat.RGBA_8888;
            params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
            params.gravity = Gravity.LEFT | Gravity.TOP;

            // 须指定宽度高度信息
            params.width = rightTopView.mWidth;
            params.height = rightTopView.mHeight;
            //
            params.x = displayWidth - rightTopView.mWidth;
            params.y = 0;

        }
        return rightTopView;
    }

    public void usbIconShowOrHide(boolean flag) {
        rightTopView.refreshUsbIconView(flag);
    }

    public void wifiIconShowOrHide(boolean flag) {
        rightTopView.refreshWifiIconView(flag);
    }

    public void bluetoothIconShowOrHide(boolean flag) {
        rightTopView.refreshBtIconView(flag);
    }

    public void tBoxIconChange(int netRssi) {
        rightTopView.refreshTboxIconView(true, netRssi);
    }

    public void tBoxIconLevelChange(int level) {
        rightTopView.refreshTboxIconLevel(level);
    }

    public void showMuteView(boolean isMute) {
        rightTopView.refreshVoiceIconView(isMute);
    }

    public void showHotView(boolean isShow) {
        rightTopView.refreshHotIconView(isShow);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventChange(RightTopEvent event) {
        SitechDevLog.i(AppConst.TAG, this + "==消息==" + event.getEventKey());
        switch (event.getEventKey()) {
            case RightTopEvent.EVENT_RIGHT_TOP_CHANGE_PHONE_STATE:
                break;
            case RightTopEvent.EVENT_RIGHT_TOP_CHANGE_WIFI_STATE:
                break;
            case RightTopEvent.EVENT_RIGHT_TOP_CHANGE_BLUETOOTH_STATE:
                break;
            case RightTopEvent.EVENT_RIGHT_TOP_CHANGE_VOLUME_STATE:
                break;
            case RightTopEvent.EVENT_RIGHT_TOP_CHANGE_USB_STATE:
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
                        SitechDevLog.i("PhoneStateChange", "signalStrength level====" + level);
                        tBoxIconChange(level);
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
}