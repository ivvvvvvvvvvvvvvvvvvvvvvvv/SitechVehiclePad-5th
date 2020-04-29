package com.sitechdev.vehicle.pad.window.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.WindowManager;

import com.blankj.utilcode.util.ScreenUtils;
import com.sitechdev.vehicle.lib.event.EventBusUtils;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.pad.app.BaseWindow;
import com.sitechdev.vehicle.pad.event.VoiceEvent;
import com.sitechdev.vehicle.pad.window.view.MainMenuView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class MainMenuWindowManager {

    private static volatile MainMenuWindowManager manager;
    private static final String TAG = MainMenuWindowManager.class.getSimpleName();

    private WindowManager winManager;
    private Context context;
    private WindowManager.LayoutParams params;

    /**
     * 显示的浮动窗体
     */
    public MainMenuView mainMenuView;

    // 屏幕的尺寸
    private static int displayWidth;
    private static int displayHeight;

    private OrientationReceiver receiver = null;

    /**
     * @return
     */
    public static MainMenuWindowManager getInstance() {
        if (manager == null) {
            synchronized (MainMenuWindowManager.class) {
                if (manager == null) {
                    manager = new MainMenuWindowManager();
                }
            }
        }
        return manager;
    }

    private MainMenuWindowManager() {
        EventBusUtils.register(this);
    }

    public void init(Context context) {
        SitechDevLog.e(TAG, "-->init");
        this.context = context;
        winManager = BaseWindow.getInstance().getWinManager();
        displayWidth = BaseWindow.getInstance().getDisplayWidth();
        displayHeight = BaseWindow.getInstance().getDisplayHeight();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.CONFIGURATION_CHANGED");
        receiver = new OrientationReceiver();
        context.registerReceiver(receiver, intentFilter);
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
        if (mainMenuView == null) {
            getView();
        }
        if (mainMenuView.getParent() != null) {
            winManager.removeViewImmediate(mainMenuView);
        }
        if (mainMenuView != null && mainMenuView.getParent() == null && !mainMenuView.isShown()) {
            winManager.addView(mainMenuView, params);
        }
    }

    /**
     * 隐藏悬浮框
     */
    public void hide() {
        SitechDevLog.e(TAG, "-------------hide()>");
        if (mainMenuView != null && mainMenuView.isShown()) {
            winManager.removeViewImmediate(mainMenuView);
            mainMenuView = null;
            params = null;
        }
//        context.unregisterReceiver(reciver);
    }

    public boolean isViewShow() {
        return mainMenuView != null && mainMenuView.isShown();
    }

    /**
     * 获取悬浮框里面显示的view
     *
     * @return
     */
    public void getView() {
        SitechDevLog.i(TAG, "-------------getView()>");
        if (mainMenuView == null) {
            mainMenuView = new MainMenuView(context);
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
            params.width = mainMenuView.mWidth;
            params.height = mainMenuView.mHeight;
            SitechDevLog.i(TAG, "-------------params.width()>" + params.width);
            SitechDevLog.i(TAG, "-------------params.height()>" + params.height);
            //
            params.x = displayHeight - mainMenuView.mHeight;
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

    private class OrientationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.CONFIGURATION_CHANGED".equals(intent.getAction())) {
                if (ScreenUtils.isLandscape()) {
                    SitechDevLog.i(TAG, "OrientationReceiver============横屏");
                    hide();
                    show();
                } else {
                    SitechDevLog.i(TAG, "OrientationReceiver============竖屏");
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
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTeddyVoiceEvent(VoiceEvent event) {
        SitechDevLog.i(TAG, "onTeddyVoiceEvent============" + event.getEventKey());
        mainMenuView.refreshTeddyView(event);
//        switch (event.getEventKey()) {
//            //唤醒成功
//            case VoiceEvent.EVENT_VOICE_MVW_SUCCESS:
//                mainMenuView.refreshTeddyView(event);
//                break;
//            //开始识别
//            case VoiceEvent.EVENT_VOICE_START_SR:
//                mainMenuView.refreshTeddyView(event);
//                break;
//            //结束识别，返回结果
//            case VoiceEvent.EVENT_VOICE_SR_SUCCESS:
//                mainMenuView.refreshTeddyView(event);
//                break;
//            //结束识别
//            case VoiceEvent.EVENT_VOICE_SR_OVER:
//                mainMenuView.refreshTeddyView(event);
//                break;
//            //停止语音
//            case VoiceEvent.EVENT_VOICE_STOP_VOICE:
//                mainMenuView.refreshTeddyView(event);
//                break;
//            //启用语音
//            case VoiceEvent.EVENT_VOICE_RESUME_VOICE:
//                mainMenuView.refreshTeddyView(event);
//                break;
//            default:
//                break;
//        }
    }

    public void changeViewByOri() {
        SitechDevLog.i(TAG, this + "============changeViewByOri");
        hide();
        show();
    }
}
