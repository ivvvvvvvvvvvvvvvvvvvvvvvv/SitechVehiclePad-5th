package com.sitechdev.vehicle.pad.window.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.Utils;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.pad.app.BaseWindow;
import com.sitechdev.vehicle.pad.event.VoiceEvent;
import com.sitechdev.vehicle.pad.window.view.MainPopupControlView;

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
    public View mainControlContentView;

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
        SitechDevLog.e(TAG, "-->init");
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
        SitechDevLog.e(TAG, "-------------show()>");
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
        SitechDevLog.e(TAG, "-------------hide()>");
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
        SitechDevLog.i(TAG, "-------------getView()>");
        if (mainControlView == null) {
            mainControlView = new MainPopupControlView(context);
        }
        if (params == null) {
//            params = new WindowManager.LayoutParams();
//            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
//            params.format = PixelFormat.RGBA_8888;
//            params.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN;
//            params.gravity = Gravity.BOTTOM;
//            params.format = PixelFormat.TRANSPARENT;
//
//            // 须指定宽度高度信息
//            params.width = mainControlView.mWidth;
//            params.height = mainControlView.mHeight;
//            SitechDevLog.i(TAG, "-------------params.width()>" + params.width);
//            SitechDevLog.i(TAG, "-------------params.height()>" + params.height);
//            //
//            params.x = displayHeight - mainControlView.mHeight;
//            params.y = 0;
//            SitechDevLog.i(TAG, "-------------params.x()>" + params.x);
//            SitechDevLog.i(TAG, "--displayHeight==" + displayHeight + "-----------params.y()>" + params.y);


            params = new WindowManager.LayoutParams();
            Point point = new Point();
            winManager.getDefaultDisplay().getSize(point);
            int screenWidth = point.x;
            int screenHeight = point.y;
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                params.type = WindowManager.LayoutParams.TYPE_PHONE;
            }
            params.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            params.format = PixelFormat.TRANSLUCENT;
            params.dimAmount = 0.5f;
//            params.gravity = Gravity.BOTTOM;
            params.screenOrientation = Utils.getApp().getResources().getConfiguration().orientation;
            params.x = 0;
            params.y = screenHeight;
            SitechDevLog.i("popUpWindowInfo", "params==[params.width=" + params.width +
                    ", params.height=" + params.height +
                    ", params.x=" + params.x +
                    ", params.y=" + params.y);
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
                    SitechDevLog.i(TAG, "OrientationReciver============横屏");
                } else {
                    SitechDevLog.i(TAG, "OrientationReciver============竖屏");
                }
                if (!isViewShow()) {
                    return;
                }
                hide();
                show();
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
        SitechDevLog.i(TAG, "onTeddyVoiceEvent============" + event.getEventKey());
    }

    public void move(int delatX, int deltaY) {
//        if (view == mainControlContentView) {
        SitechDevLog.i(TAG, "*********************移动前  move (params.x )==" + params.x + "， (params.Y)==" + params.y);
        SitechDevLog.i(TAG, "*********************移动前  move (x - preX)==" + mainControlView + "， (delatX)==" + delatX + "， (deltaY)==" + deltaY);
        params.x -= 10;
        params.y = 0;
        SitechDevLog.i(TAG, "*********************移动后  move (params.x )==" + params.x + "， (params.Y)==" + params.y);
        // 更新window
        winManager.updateViewLayout(mainControlView, params);
//        }
    }

}
