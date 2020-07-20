package com.sitechdev.vehicle.pad.window.manager;

import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.WindowManager;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AdaptScreenUtils;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.sitechdev.vehicle.lib.event.EventBusUtils;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.lib.util.ThreadUtils;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.AppApplication;
import com.sitechdev.vehicle.pad.app.BaseAppWindowManager;
import com.sitechdev.vehicle.pad.app.BaseWindow;
import com.sitechdev.vehicle.pad.event.ScreenEvent;
import com.sitechdev.vehicle.pad.event.SysEvent;
import com.sitechdev.vehicle.pad.event.VoiceEvent;
import com.sitechdev.vehicle.pad.window.view.MainControlPanelView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class MainControlPanelWindowManager {

    private static volatile MainControlPanelWindowManager manager;
    private static final String TAG = MainControlPanelWindowManager.class.getSimpleName();

    private WindowManager winManager;
    private WindowManager.LayoutParams params;

    /**
     * 显示的浮动窗体
     */
    public MainControlPanelView mainControlPanelView;

    // 屏幕的尺寸
    private static int displayWidth;
    private static int displayHeight;

    private static int minWindowLength = 0, maxWindowLength = 0, minHeight = 0, startWindowY = 0, startWindowX = 0;

    private boolean isHiddenView = false;

    /**
     * @return
     */
    public static MainControlPanelWindowManager getInstance() {
        if (manager == null) {
            synchronized (MainControlPanelWindowManager.class) {
                if (manager == null) {
                    manager = new MainControlPanelWindowManager();
                }
            }
        }
        return manager;
    }

    private MainControlPanelWindowManager() {
        EventBusUtils.register(this);
    }

    public boolean isViewShow() {
        return mainControlPanelView != null && mainControlPanelView.isShown();
    }

    /**
     * 获取悬浮框里面显示的view
     *
     * @return
     */
    public void getView() {
        SitechDevLog.i(TAG, "-------------getView()===========" + (ScreenUtils.isLandscape() ? "横屏" : "竖屏"));
        if (mainControlPanelView == null) {
            mainControlPanelView = new MainControlPanelView(AppApplication.getContext());
            SitechDevLog.i(TAG, "-------------getView()===mainControlPanelView==" + mainControlPanelView);
        }
        if (params == null) {
            params = new WindowManager.LayoutParams();
            params.height = WindowManager.LayoutParams.MATCH_PARENT;
            params.gravity = Gravity.TOP | Gravity.LEFT;
            displayHeight = BaseWindow.getInstance().getDisplayHeight();
            displayWidth = BaseWindow.getInstance().getDisplayWidth();
            if (ScreenUtils.isLandscape()) {
                //横屏
                maxWindowLength = AdaptScreenUtils.pt2Px(AppApplication.getApp().getResources().getInteger(R.integer.control_panel_view_h_width));
                params.width = AdaptScreenUtils.pt2Px(AppApplication.getApp().getResources().getInteger(R.integer.control_panel_view_h_width));
                minWindowLength = AdaptScreenUtils.pt2Px(30);
                startWindowX = minWindowLength - params.width;
                startWindowY = 0;
            } else {
                //竖屏
                maxWindowLength = AdaptScreenUtils.pt2Px(1080);
                params.width = AdaptScreenUtils.pt2Px(1080);
                params.height = AdaptScreenUtils.pt2Px(500);
                minWindowLength = AdaptScreenUtils.pt2Px(70);
                startWindowX = minWindowLength - params.width;
                //底部出现
                startWindowY = displayHeight - params.height;
//                //顶部出现
//                startWindowY = 0;
                //中部出现
//                startWindowY = (displayHeight - params.height) / 2;
            }
            params.x = startWindowX;
            params.y = startWindowY;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                params.type = WindowManager.LayoutParams.TYPE_PHONE;
            }
            params.flags = getNewParams(false);
            params.format = PixelFormat.RGBA_8888;
            params.dimAmount = 0.8f;
            SitechDevLog.i(TAG, "params==[params.width=" + params.width +
                    ", params.height=" + params.height +
                    ", params.x=" + params.x +
                    ", params.y=" + params.y +
                    ", displayHeight=" + displayHeight +
                    ", displayWidth=" + displayWidth);
            ;
        }
    }

    /**
     * 重置高度
     *
     * @param isShowFull 是否正在显示中
     */
    private int getNewParams(boolean isShowFull) {
        SitechDevLog.e(TAG, "getNewParams *********************isShowFull====" + isShowFull);
        return isShowFull ? (WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_DIM_BEHIND | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
                : (WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSysEventChange(SysEvent event) {
        SitechDevLog.i(TAG, this + "==SysEvent 消息==" + event.getEvent());
        switch (event.getEvent()) {
            case SysEvent.EB_SYS_BT_ENABLE:
                if (mainControlPanelView != null) {
                    //蓝牙变化消息
                    mainControlPanelView.refreshBtSwitchView();
                }
                break;
            case SysEvent.EB_SYS_WIFI_STATE:
                if (mainControlPanelView != null) {
                    if (event.getObj() != null) {
                        boolean status = (boolean) event.getObj();
                        //wifi变化消息
                        mainControlPanelView.refreshWifiSwitchView(status);
                    }
                }
                break;
            case SysEvent.EB_SYS_WIFI_SWITCH_STATE:
                if (mainControlPanelView != null) {
                    if (event.getObj() != null) {
                        boolean status = (boolean) event.getObj();
                        //wifi变化消息
                        mainControlPanelView.refreshWifiSwitchView(status);
                    }
                }
                break;
            case SysEvent.EB_SYS_TEDDY_SWITCH_STATE:
                if (mainControlPanelView != null) {
                    //Teddy变化消息
                    mainControlPanelView.refreshTeddySwitchView();
                }
                break;
            case SysEvent.EB_SYS_MOBILE_NET_SWITCH_STATE:
                if (mainControlPanelView != null) {
                    //移动网络变化消息
                    mainControlPanelView.refreshMobileNetSwitchView();
                }
                break;
            case SysEvent.EB_SYS_VOLUME_CHANGE_STATE:
                if (mainControlPanelView != null) {
                    if (event.getDataCtrlMask() != -1) {
                        int volumeValue = (int) event.getDataCtrlMask();
                        //音量的变化
                        mainControlPanelView.refreshVolumeView(volumeValue);
                    }
                }
                break;
            case SysEvent.EB_SYS_LIGHT_CHANGE_STATE:
                if (mainControlPanelView != null) {
                    if (event.getDataCtrlMask() != -1) {
                        int value = (int) event.getDataCtrlMask();
                        //音量的变化
                        mainControlPanelView.refreshScreenLightView(value);
                    }
                }
                break;
            default:
                break;
        }
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
                ThreadUtils.runOnUIThreadDelay(MainControlPanelWindowManager.this::show, 500);
                break;
            default:
                break;
        }
    }

    /**
     * 滑动View的位置，刷新
     *
     * @param delatX
     * @param deltaY <0代表正在上滑,展示View，>0代表正在下滑,隐藏View
     */
    public void move(int delatX, int deltaY) {
//        if (view == mainControlContentView) {
        params.x = 0;
        params.y += deltaY;
//        SitechDevLog.i(TAG, "*********************移动后  move (params.x )==" + params.x + "， (params.Y)==" + params.y + "， (displayHeight - params.height / 2)==" + (displayHeight - params.height / 2));
        if (params.y <= displayHeight - params.height) {
            params.flags = getNewParams(true);
            params.y = displayHeight - params.height;
        } else if (params.y >= displayHeight - minWindowLength) {
            params.flags = getNewParams(false);
            params.y = displayHeight - minWindowLength;
        } else {
            if (params.y >= (displayHeight - params.height / 2)) {
                isHiddenView = true;
            } else if (params.y < (displayHeight - params.height / 2)) {
                isHiddenView = false;
            }
        }
        if (deltaY < 0) {
            //上滑过程，背景逐渐不透明
            //已经滑动过的距离
            int delY = (displayHeight - minWindowLength) - params.y;
            int alphaValue = 255 * delY / displayHeight;
            mainControlPanelView.resetViewAlpha(alphaValue);
        } else {
            //下滑过程，背景逐渐透明
            //已经滑动过的距离
            int delY = params.y + (displayHeight - params.height);
            int alphaValue = 255 * delY / displayHeight;
            mainControlPanelView.resetViewAlpha(alphaValue);

        }
//        SitechDevLog.i(TAG, "*********************移动后  move (deltaY > 0 )==" + (deltaY > 0)
//                + "params.y >= (displayHeight - params.height / 2)===" + (params.y >= (displayHeight - params.height / 2))
//                + "(isHiddenView) == " + isHiddenView);
        updateWindow();
    }

    /**
     * 从左往右滑动View的位置。左滑代表展示view，右滑代表关闭view
     *
     * @param delatX <0代表正在左滑,隐藏View，>0代表正在右滑,展示View
     * @param deltaY deltaY
     */
    public void moveH(int delatX, int deltaY) {
        if (delatX == 0) {
            return;
        }
        params.x += delatX;
        if (params.x > 0) {
            params.flags = getNewParams(true);
            params.x = 0;
        } else if (params.x <= (minWindowLength - params.width)) {
            params.flags = getNewParams(false);
            params.x = minWindowLength - params.width;
        } else {
            params.flags = getNewParams(true);
            if (delatX < 0) {
                //<0代表正在左滑,隐藏View
                if (params.x <= (-(params.width / 2))) {
                    isHiddenView = true;
                } else {
                    isHiddenView = false;
                }
            } else {
                //>0代表正在右滑,展示View
                if (params.x <= (-(params.width / 2))) {
                    isHiddenView = true;
                } else {
                    isHiddenView = false;
                }
            }
        }
        int delX = params.x;
        int alphaValue = 255 - Math.abs(255 * delX / params.width);
        mainControlPanelView.resetViewAlpha(alphaValue);

        params.dimAmount = alphaValue * 0.8f / 255;

//        SitechDevLog.i(TAG, "*********************移动后  move (params.x )==" + params.x
//                + "，【 (minWindowLength - params.width)=="
//                + (minWindowLength - params.width)
//                + "】， (params.width / 2)== -"
//                + (params.width / 2)
//                + "】， delX== "
//                + delX
//                + "】， (alphaValue)== -"
//                + (alphaValue));
        updateWindow();
    }

    private void updateWindow() {
        // 更新window
        winManager.updateViewLayout(mainControlPanelView, params);
        BarUtils.setNavBarVisibility(ActivityUtils.getTopActivity(), false);
    }

    /**
     * 强制显示Window。被拖动展示出来
     */
    public void mustShownView() {
        SitechDevLog.i(TAG, "*********************   mustShownView==========");
        // 更新window
        params.x = 0;
        params.y = startWindowY;
        params.flags = getNewParams(true);
        mainControlPanelView.resetViewAlpha(255);
        mainControlPanelView.setFullScreen(true);
        updateWindow();
    }

    /**
     * 强制隐藏Window。隐藏到左边，并没有从manager中移除
     */
    public void mustHiddenView() {
        SitechDevLog.i(TAG, "*********************   mustHiddenView==========");
        // 更新window
        params.x = minWindowLength - params.width;
        params.y = startWindowY;
        params.flags = getNewParams(false);
        mainControlPanelView.resetViewAlpha(0);
        mainControlPanelView.setFullScreen(false);
        updateWindow();
    }

    /**
     * 显示悬浮框。默认形态，展示到底部，不会全部出现。
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
        SitechDevLog.e(TAG, "-------------show()=====" + mainControlPanelView);
        displayWidth = BaseWindow.getInstance().getDisplayWidth();
        displayHeight = BaseWindow.getInstance().getDisplayHeight();
        if (isViewShow()) {
            return;
        }
        if (winManager == null) {
            winManager = BaseWindow.getInstance().getWinManager();
        }
        if (mainControlPanelView == null) {
            SitechDevLog.e(TAG, "-------------show()  new view ");
            getView();
        }
        if (mainControlPanelView.getParent() != null) {
            winManager.removeViewImmediate(mainControlPanelView);
        }
        if (mainControlPanelView != null && mainControlPanelView.getParent() == null && !mainControlPanelView.isShown()) {
            mainControlPanelView.initVolumeAndLightData();
            winManager.addView(mainControlPanelView, params);
        }
    }

    /**
     * 隐藏悬浮框。从winmanager中移除。
     */
    public void hide() {
        SitechDevLog.e(TAG, "-------------hide()=====" + mainControlPanelView);
        if (mainControlPanelView != null && mainControlPanelView.isShown()) {
            SitechDevLog.e(TAG, "-------------hide()  remove=====" + mainControlPanelView);
            winManager.removeViewImmediate(mainControlPanelView);
            mainControlPanelView = null;
            params = null;
        }
    }

    /**
     * 窗口归位。
     * 规则：如果滑动距离超过View高度的一半，则上滑展示，下滑隐藏；反之亦然。
     */
    public void resetView() {
        SitechDevLog.i(TAG, "*********************   resetView=========isHiddenView=" + isHiddenView);
        if (isHiddenView) {
            mustHiddenView();
        } else {
            mustShownView();
        }
    }

    /**
     * 窗口归位。
     * 规则：如果滑动距离超过View高度的一半，则上滑展示，下滑隐藏；反之亦然。
     */
    public void resetView(int startX, int endX) {
        SitechDevLog.i(TAG, "*********************   resetView=========startX=" + startX + "==endX=" + endX);
//        if (isHiddenView) {
//            mustHiddenView();
//        } else {
//            mustShownView();
//        }
        if ((endX - startX) < 0) {
            //右->左，view消失。
            if (Math.abs(endX - startX) >= 100) {
                //滑动的距离超过了100
                mustHiddenView();
            } else {
                mustShownView();
            }
        } else {
            //左->右，view展示。
            if (Math.abs(endX - startX) >= 100) {
                //滑动的距离超过了100
                mustShownView();
            } else {
                mustHiddenView();
            }
        }
    }

}
