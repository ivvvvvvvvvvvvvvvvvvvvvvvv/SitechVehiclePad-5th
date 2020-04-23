package com.sitechdev.vehicle.pad.window.manager;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.WindowManager;

import com.sitechdev.vehicle.pad.app.BaseWindow;
import com.sitechdev.vehicle.pad.window.view.RightTopView;
import com.tencent.mars.xlog.Log;


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
        Log.e("RightTop", "-------------getView()>");
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

    public void showMuteView(boolean isMute) {
        rightTopView.refreshVoiceIconView(isMute);
    }

    public void showHotView(boolean isShow) {
        rightTopView.refreshHotIconView(isShow);
    }
}
