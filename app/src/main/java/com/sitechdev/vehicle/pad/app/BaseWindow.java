package com.sitechdev.vehicle.pad.app;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.WindowManager;

/**
 * 项目名称：SitechVehiclePad-5th
 * 类名称：BaseWindow
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2020/04/23 0023 13:46
 * 修改时间：
 * 备注：
 */
public class BaseWindow {
    private WindowManager winManager;
    private Context context;
    private WindowManager.LayoutParams params;
    private int displayWidth;
    private int displayHeight;
    private Handler uiHandler;

    private BaseWindow() {
        initFunc();
    }

    private static class SingleBaseWindow {
        private static final BaseWindow mBaseWindow = new BaseWindow();
    }

    public static BaseWindow getInstance() {
        return SingleBaseWindow.mBaseWindow;
    }

    private void initFunc() {
        context = AppApplication.getContext();
        winManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        displayWidth = winManager.getDefaultDisplay().getWidth();
        displayHeight = winManager.getDefaultDisplay().getHeight();
        uiHandler = new Handler(Looper.getMainLooper());
    }

    public WindowManager getWinManager() {
        return winManager;
    }

    public Context getContext() {
        return context;
    }

    public WindowManager.LayoutParams getParams() {
        return params;
    }

    public int getDisplayWidth() {
        return displayWidth;
    }

    public int getDisplayHeight() {
        return displayHeight;
    }

    public Handler getUiHandler() {
        return uiHandler;
    }
}
