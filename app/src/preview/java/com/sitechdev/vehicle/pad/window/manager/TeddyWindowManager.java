package com.sitechdev.vehicle.pad.window.manager;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.WindowManager;

import com.blankj.utilcode.util.ScreenUtils;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.lib.util.ThreadUtils;
import com.sitechdev.vehicle.pad.app.BaseWindow;
import com.sitechdev.vehicle.pad.event.ScreenEvent;
import com.sitechdev.vehicle.pad.window.view.FloatTeddyView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @author DELL
 * @date 2016/11/8
 */
public class TeddyWindowManager {

    private static final String TAG = TeddyWindowManager.class.getSimpleName();

    private static TeddyWindowManager manager;

    private WindowManager winManager;
    private Context context;
    private WindowManager.LayoutParams params;

    /**
     * 显示的浮动窗体
     */
    private FloatTeddyView floatView;
    private static int displayWidth;
    private static int displayHeight;

    public static TeddyWindowManager getInstance() {
        if (manager == null) {
            synchronized (TeddyWindowManager.class) {
                if (manager == null) {
                    manager = new TeddyWindowManager();
                }
            }
        }
        return manager;
    }

    private TeddyWindowManager() {
    }

    public void init(Context context) {
        SitechDevLog.e(TAG, "-->init");
        this.context = context;
        winManager = BaseWindow.getInstance().getWinManager();
        displayWidth = BaseWindow.getInstance().getDisplayWidth();
        displayHeight = BaseWindow.getInstance().getDisplayHeight();
    }

    public void show() {
        SitechDevLog.e(TAG, "-------------show()>");
        if (isViewShow()) {
            hide();
        }
        if (floatView == null) {
            getView();
        }
        if (floatView.getParent() != null) {
            winManager.removeViewImmediate(floatView);
        }
        if (floatView != null && floatView.getParent() == null && !floatView.isShown()) {
            winManager.addView(floatView, params);
        }
    }

    public boolean isViewShow() {
        return floatView != null && floatView.isShown();
    }

    /**
     * 获取悬浮框里面显示的view
     */
    public FloatTeddyView getView() {
        if (floatView == null) {
            floatView = new FloatTeddyView(context);
        }
        if (params == null) {
            params = new WindowManager.LayoutParams();
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            params.format = PixelFormat.RGBA_8888;
            params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

            // 须指定宽度高度信息
            params.width = floatView.mWidth;
            params.height = floatView.mHeight;

            if (ScreenUtils.isLandscape()) {
                //横屏
                params.gravity = Gravity.TOP;
                params.x = displayWidth - floatView.mWidth - 50;
                params.y = displayHeight - floatView.mHeight - 30;
            } else {
                params.gravity = Gravity.TOP | Gravity.LEFT;
                params.x = 20;
                params.y = displayHeight - floatView.mHeight - 170;
            }

        }
        return floatView;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onScreenEvent(ScreenEvent event) {
        switch (event.getEventKey()) {
            case ScreenEvent.EVENT_SCREEN_ORIENTATION_CHANGE:
                if (ScreenUtils.isLandscape()) {
                    SitechDevLog.i(TAG, "OrientationReceiver============横屏");
                } else {
                    SitechDevLog.i(TAG, "OrientationReceiver============竖屏");
                }
                hide();
                ThreadUtils.runOnUIThreadDelay(TeddyWindowManager.this::show, 500);
                break;
            default:
                break;
        }
    }

    /**
     * 隐藏悬浮框
     */
    public void hide() {
        SitechDevLog.e(TAG, "-------------hide()>");
        if (floatView != null && floatView.isShown()) {
            winManager.removeViewImmediate(floatView);
            floatView = null;
            params = null;
        }
    }
}