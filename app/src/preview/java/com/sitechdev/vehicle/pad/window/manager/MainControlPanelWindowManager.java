package com.sitechdev.vehicle.pad.window.manager;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.sitechdev.vehicle.lib.event.EventBusUtils;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.lib.util.ThreadUtils;
import com.sitechdev.vehicle.pad.app.BaseWindow;
import com.sitechdev.vehicle.pad.event.ScreenEvent;
import com.sitechdev.vehicle.pad.event.VoiceEvent;
import com.sitechdev.vehicle.pad.window.view.MainControlPanelView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class
MainControlPanelWindowManager {

    private static volatile MainControlPanelWindowManager manager;
    private static final String TAG = MainControlPanelWindowManager.class.getSimpleName();

    private WindowManager winManager;
    private Context context;
    private WindowManager.LayoutParams params;

    /**
     * 显示的浮动窗体
     */
    public MainControlPanelView mainControlPanelView;
    public View mainControlContentView;

    // 屏幕的尺寸
    private static int displayWidth;
    private static int displayHeight;

    private static int minWindowY = 0, maxWindowY = 0, minHeight = 0;

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

    public void init(Context context) {
        SitechDevLog.e(TAG, "-->init");
        this.context = context;
        winManager = BaseWindow.getInstance().getWinManager();
        displayWidth = BaseWindow.getInstance().getDisplayWidth();
        displayHeight = BaseWindow.getInstance().getDisplayHeight();
        getView();
        initData();
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
            mainControlPanelView = new MainControlPanelView(context);
            SitechDevLog.i(TAG, "-------------getView()===mainControlPanelView==" + mainControlPanelView);
        }
        if (params == null) {
//            params = new WindowManager.LayoutParams();
//            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
//            params.format = PixelFormat.RGBA_8888;
//            params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
//                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
//            params.gravity = Gravity.TOP;
//            params.format = PixelFormat.TRANSPARENT;
//
//            // 须指定宽度高度信息
//            params.width = WindowManager.LayoutParams.MATCH_PARENT;
//            params.height = 519;
//            SitechDevLog.i(TAG, "-------------params.width()>" + params.width);
//            SitechDevLog.i(TAG, "-------------params.height()>" + params.height);
//            //
//            params.x = 0;
//            params.y = displayHeight - 50;
//            SitechDevLog.i(TAG, "-------------params.x()>" + params.x);
//            SitechDevLog.i(TAG, "--displayHeight==" + displayHeight + "-----------params.y()>" + params.y);

            params = new WindowManager.LayoutParams();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            if (ScreenUtils.isLandscape()) {
                //横屏
                maxWindowY = 519;
                params.height = 519;
            } else {
                maxWindowY = 1000;
                params.height = 1000;
            }
            displayHeight = BaseWindow.getInstance().getDisplayHeight();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                params.type = WindowManager.LayoutParams.TYPE_PHONE;
            }
            params.flags = getNewParams(false);
            params.format = PixelFormat.RGBA_8888;
            params.dimAmount = 0.8f;
            params.gravity = Gravity.TOP;
            params.x = 0;
            params.y = displayHeight - minWindowY;
            SitechDevLog.i(TAG, "params==[params.width=" + params.width +
                    ", params.height=" + params.height +
                    ", params.x=" + params.x +
                    ", params.y=" + params.y +
                    ", displayHeight=" + displayHeight +
                    ", displayWidth=" + displayWidth);
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
     * 初始化逻辑
     */
    private void initData() {
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
        } else if (params.y >= displayHeight - minWindowY) {
            params.flags = getNewParams(false);
            params.y = displayHeight - minWindowY;
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
            int delY = (displayHeight - minWindowY) - params.y;
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
        params.y = displayHeight - maxWindowY;
        params.flags = getNewParams(true);
        mainControlPanelView.resetViewAlpha(255);
        mainControlPanelView.setFullScreen(true);
        updateWindow();
    }

    /**
     * 强制隐藏Window。隐藏到底部，并没有从manager中移除
     */
    public void mustHiddenView() {
        SitechDevLog.i(TAG, "*********************   mustHiddenView==========");
        // 更新window
        params.y = displayHeight - minWindowY;
        params.flags = getNewParams(false);
        mainControlPanelView.resetViewAlpha(0);
        mainControlPanelView.setFullScreen(false);
        updateWindow();
    }

    /**
     * 显示悬浮框。默认形态，展示到底部，不会全部出现。
     */
    public void show() {
        SitechDevLog.e(TAG, "-------------show()=====" + mainControlPanelView);
        if (isViewShow()) {
            return;
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

}
