package com.sitechdev.vehicle.pad.window.manager;

import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
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

    private static int minWindowLength = 0, startWindowY = 0, startWindowX = 0;

    private boolean isHiddenView = false;

    /**
     * show view事件
     */
    private static final int PANEL_VIEW_SHOW = 10;
    /**
     * hide view事件
     */
    private static final int PANEL_VIEW_HIDE = 20;
    /**
     * show view 的超时时间。单位毫秒
     */
    private static final int MAX_TIME_OUT = 5000;


    private Handler mControlPanelHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case PANEL_VIEW_SHOW:
                    //移除所有的hide事件
                    removeMessages(PANEL_VIEW_HIDE);
                    //重新发出所有的hide事件,{@link MAX_TIME_OUT}秒后执行
                    SitechDevLog.i(TAG, "sendEmptyMessageDelayed====>PANEL_VIEW_HIDE");
                    mControlPanelHandler.sendEmptyMessageDelayed(PANEL_VIEW_HIDE, MAX_TIME_OUT);
                    break;
                case PANEL_VIEW_HIDE:
                    //移除所有的show事件
                    removeMessages(PANEL_VIEW_SHOW);
                    ThreadUtils.runOnUIThread(() -> {
                        mustHiddenView();
                    });
                    //移除发出所有的hide事件
                    removeMessages(PANEL_VIEW_HIDE);
                    break;
                default:
                    break;
            }
        }
    };

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
            displayHeight = BaseWindow.getInstance().getDisplayHeight();
            displayWidth = BaseWindow.getInstance().getDisplayWidth();
            params.gravity = Gravity.TOP | Gravity.LEFT;
            if (ScreenUtils.isLandscape()) {
                //横屏
                params.width = AdaptScreenUtils.pt2Px(AppApplication.getApp().getResources().getInteger(R.integer.control_panel_view_h_width));
                minWindowLength = AdaptScreenUtils.pt2Px(30);
                startWindowX = minWindowLength - params.width;
                startWindowY = 0;
            } else {
                //竖屏
                params.height = AdaptScreenUtils.pt2Px(AppApplication.getApp().getResources().getInteger(R.integer.control_panel_view_v_height));
                minWindowLength = AdaptScreenUtils.pt2Px(50);
                /**
                 * 依靠左边栏，右滑出现。
                 params.gravity = Gravity.TOP | Gravity.LEFT;
                 startWindowX = minWindowLength - params.width;
                 //在底部出现
                 startWindowY = displayHeight - params.height;
                 */
                /**
                 * 依靠左边栏，右滑出现。
                 startWindowX = minWindowLength - params.width;
                 //在顶部出现
                 startWindowY = 0;
                 */
                /**
                 * 依靠左边栏，右滑出现。
                 startWindowX = minWindowLength - params.width;
                 //在中部出现
                 startWindowY = (displayHeight - params.height) / 2;
                 */
                /**
                 * 依靠顶部栏，下拉出现。
                 */
                startWindowX = 0;
                startWindowY = minWindowLength - params.height;
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
     * 横屏时是从左往右滑,出现View; 竖屏时是从上往下滑,出现View
     *
     * @param deltaX
     * @param deltaY <0代表正在上滑,展示View，>0代表正在下滑,隐藏View
     */
    public void moveView(int deltaX, int deltaY) {
        if (ScreenUtils.isLandscape()) {
            //横屏时的滑动
            moveH(deltaX, deltaY);
        } else {
            //竖屏时的滑动
            move(deltaX, deltaY);
        }
    }

    /**
     * 滑动View的位置，刷新
     *
     * @param delatX
     * @param deltaY <0代表正在上滑,展示View，>0代表正在下滑,隐藏View
     */
    public void move(int delatX, int deltaY) {
        if (deltaY == 0) {
            return;
        }
        SitechDevLog.i(TAG, "move *********************移动后  deltaY==" + deltaY);
        params.y += deltaY;
        if (params.y > 0) {
            //完全滑出
            params.flags = getNewParams(true);
            params.y = 0;
        } else if (params.y <= (minWindowLength - params.height)) {
            //完全收回
            params.flags = getNewParams(false);
            params.y = minWindowLength - params.height;
        } else {
            //滑出过程中
            params.flags = getNewParams(true);
            if (deltaY < 0) {
                //<0代表正在上滑,隐藏View，
                if (params.y <= (-(params.height / 2))) {
                    isHiddenView = true;
                } else {
                    isHiddenView = false;
                }
            } else {
                //>0代表正在下滑,显示view
                if (params.y <= (-(params.height / 2))) {
                    isHiddenView = true;
                } else {
                    isHiddenView = false;
                }
            }
        }
        int delY = params.y;
        int alphaValue = 255 - Math.abs(255 * delY / params.height);
        mainControlPanelView.resetViewAlpha(alphaValue);

        params.dimAmount = alphaValue * 0.8f / 255;
//        if (deltaY < 0) {
//            //上滑过程，背景逐渐不透明
//            //已经滑动过的距离
//            int delY = (displayHeight - minWindowLength) - params.y;
//            int alphaValue = 255 * delY / displayHeight;
//            mainControlPanelView.resetViewAlpha(alphaValue);
//        } else {
//            //下滑过程，背景逐渐透明
//            //已经滑动过的距离
//            int delY = params.y + (displayHeight - params.height);
//            int alphaValue = 255 * delY / displayHeight;
//            mainControlPanelView.resetViewAlpha(alphaValue);
//
//        }
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
        if (ScreenUtils.isLandscape()) {
            //横屏时的滑动
            params.y = startWindowY;
        } else {
            //竖屏时的滑动
            params.y = 0;
        }
        params.flags = getNewParams(true);
        mainControlPanelView.resetViewAlpha(255);
        mainControlPanelView.setFullScreen(true);
        updateWindow();

        //计时开始通知
        sendMessageNotice();
    }

    /**
     * 强制隐藏Window。隐藏到左边，并没有从manager中移除
     */
    public void mustHiddenView() {
        SitechDevLog.i(TAG, "*********************   mustHiddenView==========");
        // 更新window
        if (ScreenUtils.isLandscape()) {
            //横屏时的滑动
            params.x = minWindowLength - params.width;
        } else {
            //竖屏时的滑动
            params.x = 0;
        }
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
    public void resetView(int startX, int endX, int startY, int endY) {
        if (ScreenUtils.isLandscape()) {
            //横屏时的滑动
            resetViewH(startX, endX);
        } else {
            //竖屏时的滑动
            resetViewV(startY, endY);
        }
    }

    /**
     * 窗口归位。
     * 规则：如果滑动距离超过View高度的一半，则上滑展示，下滑隐藏；反之亦然。
     */
    public void resetViewV(int startY, int endY) {
        SitechDevLog.i(TAG, "*********************   resetView=========startY=" + startY + "==endY=" + endY);
//        if (isHiddenView) {
//            mustHiddenView();
//        } else {
//            mustShownView();
//        }
        if ((endY - startY) < 0) {
            //上滑，view消失。
            if (Math.abs(endY - startY) >= 100) {
                //滑动的距离超过了100
                mustHiddenView();
            } else {
                mustShownView();
            }
        } else {
            //下拉，view展示。
            if (Math.abs(endY - startY) >= 100) {
                //滑动的距离超过了100
                mustShownView();
            } else {
                mustHiddenView();
            }
        }
    }

    /**
     * 窗口归位。
     * 规则：如果滑动距离超过View高度的一半，则上滑展示，下滑隐藏；反之亦然。
     */
    public void resetViewH(int startX, int endX) {
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

    /**
     * 发送事件通知，viewshow，执行超时后hide
     */
    public void sendMessageNotice() {
        SitechDevLog.i(TAG, "sendMessageNotice====>PANEL_VIEW_SHOW");
        mControlPanelHandler.sendEmptyMessage(PANEL_VIEW_SHOW);
    }

}
