package com.sitechdev.vehicle.pad.window.manager;

import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.WindowManager;

import com.blankj.utilcode.util.ScreenUtils;
import com.sitechdev.vehicle.lib.event.EventBusUtils;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.lib.util.ThreadUtils;
import com.sitechdev.vehicle.pad.app.AppApplication;
import com.sitechdev.vehicle.pad.app.BaseWindow;
import com.sitechdev.vehicle.pad.event.ScreenEvent;
import com.sitechdev.vehicle.pad.event.SysEvent;
import com.sitechdev.vehicle.pad.event.VoiceEvent;
import com.sitechdev.vehicle.pad.module.setting.teddy.TeddyConfig;
import com.sitechdev.vehicle.pad.vui.VoiceConstants;
import com.sitechdev.vehicle.pad.window.view.FloatTeddyView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @author DELL
 * @date 2016/11/8
 */
public class TeddyWindowManager {

    private static final String TAG = VoiceConstants.TEDDY_TAG;

    private static TeddyWindowManager manager;

    private WindowManager winManager;
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
        EventBusUtils.register(this);
    }

//    public void init(Context context) {
//        SitechDevLog.e(TAG, "-->init");
//        this.context = context;
//        winManager = BaseWindow.getInstance().getWinManager();
//    }

    public void show() {
        SitechDevLog.e(TAG, "-------------show()>");
        displayWidth = BaseWindow.getInstance().getDisplayWidth();
        displayHeight = BaseWindow.getInstance().getDisplayHeight();
        if (isViewShow()) {
            return;
        }
        if (winManager == null) {
            winManager = BaseWindow.getInstance().getWinManager();
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
            floatView = new FloatTeddyView(AppApplication.getContext());
        }
        if (params == null) {
            params = new WindowManager.LayoutParams();
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            params.format = PixelFormat.RGBA_8888;
            params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;

            // 须指定宽度高度信息
            params.width = floatView.mWidth;
            params.height = floatView.mHeight;

            if (ScreenUtils.isLandscape()) {
                //横屏
                params.gravity = Gravity.CENTER;
                params.x = displayWidth - floatView.mWidth - 50;
                params.y = displayHeight - floatView.mHeight;
            } else {
                params.gravity = Gravity.TOP | Gravity.LEFT;
                params.x = 60;
                params.y = displayHeight - floatView.mHeight - 120;
            }
            SitechDevLog.i(TAG, "-------------params.width()>" + params.width);
            SitechDevLog.i(TAG, "-------------params.height()>" + params.height);
            SitechDevLog.i(TAG, "-------------params.x()>" + params.x);
            SitechDevLog.i(TAG, "-------------params.y()>" + params.y);

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

    /**
     * 监听语音事件，变换View
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTeddyVoiceEvent(VoiceEvent event) {
        SitechDevLog.i(TAG, "onTeddyVoiceEvent============" + event.getEventKey());
        if (floatView != null) {
            floatView.refreshTeddyView(event);
        }
        switch (event.getEventKey()) {
            //停止语音
            case VoiceEvent.EVENT_VOICE_STOP_VOICE:
                break;
            //启用语音
            case VoiceEvent.EVENT_VOICE_RESUME_VOICE:
                break;
            case VoiceEvent.EVENT_VOICE_AUTO_MVW_SWITCH:
                boolean autoSwitch = (boolean) event.getEventValue();
                SitechDevLog.i(TAG, "EVENT_VOICE_AUTO_MVW_SWITCH=========autoSwitch===" + autoSwitch);
                TeddyConfig.setAutoMvwStatus(autoSwitch);
                EventBusUtils.postEvent(new SysEvent(SysEvent.EB_SYS_TEDDY_SWITCH_STATE));
                if (floatView != null && !floatView.isTeddyWorking()) {
                    floatView.resetTeddyViewDefault();
                }
                break;
            case VoiceEvent.EVENT_VOICE_SEX_SWITCH:
                boolean isMale = (boolean) event.getEventValue();
                SitechDevLog.i(TAG, "EVENT_VOICE_IS MALE=========autoSwitch===" + isMale);
                TeddyConfig.setSexSwitch(isMale);
                break;
            default:
                break;
        }
    }
}