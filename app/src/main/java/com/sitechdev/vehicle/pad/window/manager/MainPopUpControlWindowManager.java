package com.sitechdev.vehicle.pad.window.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.blankj.utilcode.util.ScreenUtils;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.pad.app.BaseWindow;
import com.sitechdev.vehicle.pad.event.VoiceEvent;
import com.sitechdev.vehicle.pad.window.view.MainPopupControlView;
import com.tencent.mars.xlog.Log;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class MainPopUpControlWindowManager {

    private static volatile MainPopUpControlWindowManager manager;
    private static final String TAG = MainPopUpControlWindowManager.class.getSimpleName();

    private WindowManager winManager;
    private Context context;
    private WindowManager.LayoutParams params;

    /**
     * 显示的浮动窗体
     */
    public MainPopupControlView mainControlView;

    // 屏幕的尺寸
    private static int displayWidth;
    private static int displayHeight;

    private OrientationReciver reciver = null;

    /**
     * @return
     */
    public static MainPopUpControlWindowManager getInstance() {
        if (manager == null) {
            synchronized (MainPopUpControlWindowManager.class) {
                if (manager == null) {
                    manager = new MainPopUpControlWindowManager();
                }
            }
        }
        return manager;
    }

    private MainPopUpControlWindowManager() {
    }

    public void init(Context context) {
        Log.e(TAG, "-->init");
        this.context = context;
        winManager = BaseWindow.getInstance().getWinManager();
        displayWidth = BaseWindow.getInstance().getDisplayWidth();
        displayHeight = BaseWindow.getInstance().getDisplayHeight();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.CONFIGURATION_CHANGED");
        reciver = new OrientationReciver();
        context.registerReceiver(reciver, intentFilter);
        getView();
        initData();
    }

    /**
     * 显示悬浮框
     */
    public void show() {
        Log.e(TAG, "-------------show()>");
        if (isViewShow()) {
            return;
        }
        if (mainControlView == null) {
            getView();
        }
        if (mainControlView.getParent() != null) {
            winManager.removeViewImmediate(mainControlView);
        }
        if (mainControlView != null && mainControlView.getParent() == null && !mainControlView.isShown()) {
            winManager.addView(mainControlView, params);
        }
    }

    /**
     * 隐藏悬浮框
     */
    public void hide() {
        Log.e(TAG, "-------------hide()>");
        if (mainControlView != null && mainControlView.isShown()) {
            winManager.removeViewImmediate(mainControlView);
            mainControlView = null;
            params = null;
        }
//        context.unregisterReceiver(reciver);
    }

    public boolean isViewShow() {
        return mainControlView != null && mainControlView.isShown();
    }

    /**
     * 获取悬浮框里面显示的view
     *
     * @return
     */
    public void getView() {
        Log.i(TAG, "-------------getView()>");
        if (mainControlView == null) {
            mainControlView = new MainPopupControlView(context);
        }
        if (params == null) {
            params = new WindowManager.LayoutParams();
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            params.format = PixelFormat.RGBA_8888;
            params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            params.gravity = Gravity.BOTTOM;
//            params.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN;
//            params.format = PixelFormat.TRANSPARENT;
//            Point point = new Point();
//            winManager.getDefaultDisplay().getSize(point);
//            int screenWidth = point.x;
//            int screenHeight = point.y;
//            params.x = screenWidth;
//            params.y = screenHeight;

            // 须指定宽度高度信息
            params.width = mainControlView.mWidth;
            params.height = mainControlView.mHeight;
            SitechDevLog.i(TAG, "-------------params.width()>" + params.width);
            SitechDevLog.i(TAG, "-------------params.height()>" + params.height);
            //
            params.x = displayHeight - mainControlView.mHeight;
            params.y = 0;
            SitechDevLog.i(TAG, "-------------params.x()>" + params.x);
            SitechDevLog.i(TAG, "--displayHeight==" + displayHeight + "-----------params.y()>" + params.y);

        }
    }

    /**
     * 初始化逻辑
     */
    private void initData() {
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class OrientationReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.CONFIGURATION_CHANGED".equals(intent.getAction())) {
                if (ScreenUtils.isLandscape()) {
                    Log.i(TAG, "OrientationReciver============横屏");
                    hide();
                    show();
                } else {
                    Log.i(TAG, "OrientationReciver============竖屏");
                    hide();
                    show();
                }
            }
        }
    }

    /**
     * 监听语音事件，变换View
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onTeddyVoiceEvent(VoiceEvent event) {
        Log.i(TAG, "onTeddyVoiceEvent============" + event.getEventKey());
    }

    public void move(View view, int delatX, int deltaY) {
        if (view == mainControlView) {
            params.x += delatX;
            params.y -= deltaY;
            // 更新floatView
            winManager.updateViewLayout(view, params);
        }
    }

}
