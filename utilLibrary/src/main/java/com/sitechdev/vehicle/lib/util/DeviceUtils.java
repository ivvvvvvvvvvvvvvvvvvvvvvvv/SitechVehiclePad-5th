package com.sitechdev.vehicle.lib.util;

import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * 创建人：shaozhi
 */
public class DeviceUtils {

    public static int screenWidth = 0, screenHeight = 0, densityDpi = 0;
    private static float density = 0.0f;

    public static void init(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;         // 屏幕宽度（像素）
        screenHeight = dm.heightPixels;       // 屏幕高度（像素）
        density = dm.density;         // 屏幕密度（0.75 / 1.0 / 1.5）
        densityDpi = dm.densityDpi;     // 屏幕密度dpi（120 / 160 / 240）
    }

    /**
     * 获取手机品牌
     */
    public static String getPhoneBrand() {
        return Build.BRAND;
    }

    /**
     * 获取手机型号
     */
    public static String getPhoneModel() {
        return Build.MODEL;
    }

    /**
     * 获取手机Android 版本（4.4、5.0、5.1 ...）
     */
    public static String getBuildVersion() {
        return Build.VERSION.RELEASE;
    }

    public static String getScreenInfo() {
        return String.format("%sx%s,%s,%s", screenWidth, screenHeight, density, densityDpi);
    }

    public static String getBuildInfo() {
        return Build.FINGERPRINT;
    }

}
