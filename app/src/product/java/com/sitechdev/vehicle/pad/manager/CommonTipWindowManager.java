package com.sitechdev.vehicle.pad.manager;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.pad.view.CommonTipView;

public class CommonTipWindowManager {

    private static final String TAG = CommonTipWindowManager.class.getSimpleName();
    private static CommonTipWindowManager manager;

    private WindowManager winManager;
    private Context context;
    private WindowManager.LayoutParams params;

    /**
     * 显示的浮动窗体
     */
    public CommonTipView commonTipView;

    // 屏幕的尺寸
    private static int displayWidth;
    private static int displayHeight;

    /**
     * @return
     */
    public static CommonTipWindowManager getInstance() {
        if (manager == null) {
            synchronized (CommonTipWindowManager.class) {
                if (manager == null) {
                    manager = new CommonTipWindowManager();
                }
            }
        }
        return manager;
    }

    private CommonTipWindowManager() {
    }

    public void init(Context context) {
        SitechDevLog.e(TAG, "-->init");
        this.context = context;
        winManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        displayWidth = winManager.getDefaultDisplay().getWidth();
        displayHeight = winManager.getDefaultDisplay().getHeight();
        commonTipView = getView();
    }

    /**
     * 显示悬浮框
     */
    public void show() {
        if (commonTipView.getParent() != null) {
            winManager.removeView(commonTipView);
        }
        if (commonTipView != null && commonTipView.getParent() == null && !commonTipView.isShown()) {
            winManager.addView(commonTipView, params);
        }
    }

    public boolean isShow() {
        return winManager != null && commonTipView != null && commonTipView.isShown();
    }

    /**
     * 隐藏悬浮框
     */
    public void hide() {
        if (commonTipView != null && commonTipView.isShown()) {
            winManager.removeView(commonTipView);
        }
    }

    /**
     * 获取悬浮框里面显示的view
     *
     * @return
     */
    public CommonTipView getView() {
        if (commonTipView == null) {
            commonTipView = new CommonTipView(context);
        }
        if (params == null) {
            params = new WindowManager.LayoutParams();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //TYPE_PRESENTATION = FIRST_SYSTEM_WINDOW + 37  在android 8及以上的版本不需要申请权限
                params.type = 2037;
            } else {
                params.type = WindowManager.LayoutParams.TYPE_STATUS_BAR_PANEL;
            }
            params.format = PixelFormat.RGBA_8888;
            params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            params.gravity = Gravity.CENTER;
            params.dimAmount = 0.8f;
            // 须指定宽度高度信息
            params.width = commonTipView.mWidth;
            params.height = commonTipView.mHeight;
//            //居中
//            params.x = displayWidth / 2 - 200;
//            params.y = displayHeight / 2 - 200;

        }
        return commonTipView;
    }

    public void setWindowMessage(String message) {
        if (commonTipView != null) {
            commonTipView.setMessage(message);
        }
    }

    public void setTitle(String title) {
        if (commonTipView != null) {
            commonTipView.setTitle(title);
        }
    }

    public void setWindowOkClickListener(View.OnClickListener clickListener) {
        if (commonTipView != null) {
            commonTipView.setOKClickListener(clickListener);
        }
    }

    public void setWindowCancelListener(View.OnClickListener clickListener) {
        if (commonTipView != null) {
            commonTipView.setCancelClickListener(clickListener);
        }
    }

    public void setCancelBtnTxt(String cancelTxt) {
        if (null != commonTipView) {
            commonTipView.setCancelBtnTxt(cancelTxt);
        }
    }
}
