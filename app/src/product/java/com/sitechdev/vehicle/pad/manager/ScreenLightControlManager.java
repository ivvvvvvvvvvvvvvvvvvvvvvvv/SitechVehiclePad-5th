package com.sitechdev.vehicle.pad.manager;

import android.database.ContentObserver;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.IntRange;

import com.blankj.utilcode.util.BrightnessUtils;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.pad.app.AppApplication;

/**
 * @author 邵志
 * @version 2020/06/24 0024 16:25
 * @name ScreenLightControlManager
 * @description 亮度控制
 */
public class ScreenLightControlManager {
    private final static String TAG = "ScreenLightManager";

    private ScreenLightControlManager() {
    }

    private static final class SingleVolumeControlManager {
        private static final ScreenLightControlManager SINGLE = new ScreenLightControlManager();
    }

    public static ScreenLightControlManager getInstance() {
        return SingleVolumeControlManager.SINGLE;
    }

    /**
     * 初始化亮度控制
     */
    public void init() {
        //注册广播接收器
        registerScreenLightListener();
    }

    /**
     * 注册亮度事件广播接收
     */
    private void registerScreenLightListener() {
        AppApplication.getContext().getContentResolver().registerContentObserver(
                Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS), true,
                mBrightnessObserver);
    }

    /**
     * 亮度返回值的接收器
     */
    private ContentObserver mBrightnessObserver = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            SitechDevLog.i(TAG, "selfChange===" + selfChange);
        }
    };

    /**
     * 设置亮度值
     *
     * @param brightness
     */
    public void setScreenLightValue(@IntRange(from = 0, to = 255) final int brightness) {
        SitechDevLog.i(TAG, "setLightValue===" + brightness);
        try {
//            BrightnessUtils.setWindowBrightness(ActivityUtils.getTopActivity().getWindow(), brightness);
            BrightnessUtils.setBrightness(brightness);
        } catch (Exception e) {
            SitechDevLog.exception(e);
        }
    }

    /**
     * 获取当前亮度值
     *
     * @return
     */
    public int getScreenLightValue() {
//        return BrightnessUtils.getWindowBrightness(ActivityUtils.getTopActivity().getWindow());
        return BrightnessUtils.getBrightness();
    }

}
