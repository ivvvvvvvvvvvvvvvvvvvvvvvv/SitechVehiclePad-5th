package com.sitechdev.vehicle.lib.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuhe
 * @date 2019/03/29
 */
public class PackageInfoUtils {

    private static final String TAG = PackageInfoUtils.class.getSimpleName();
    private static final int SYSTEM_VERSION_LIMIT = 12;

    /**
     * 获取已安装应用列表
     */
    public static ArrayList<String> getAppList() {
        ArrayList<String> pkgs = new ArrayList<>();
        try {
            PackageManager pm = AppUtils.getApp().getPackageManager();
            // Return a List of all packages that are installed on the device.
            List<PackageInfo> packages = pm.getInstalledPackages(0);
            for (PackageInfo packageInfo : packages) {
                // 非系统应用
                if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                    pkgs.add(packageInfo.packageName);
                }
            }
            return pkgs;
        } catch (Exception e) {
            SitechDevLog.e(TAG, e.getMessage());
        }
        return pkgs;
    }

    /**
     * 是否安装应用
     *
     * @param packName
     * @return true=已安装，false=未安装
     */
    public static boolean isInstallApk(String packName) {
        ArrayList<String> pkgs = new ArrayList<>();
        try {
            PackageManager pm = AppUtils.getApp().getPackageManager();
            // Return a List of all packages that are installed on the device.
            List<PackageInfo> packages = pm.getInstalledPackages(0);
            for (PackageInfo packageInfo : packages) {
                pkgs.add(packageInfo.packageName);
            }
        } catch (Exception e) {
            SitechDevLog.e(TAG, e.getMessage());
        }
        return pkgs.contains(packName);
    }

    public static String getAppVersionName() {
        try {
            PackageInfo pi = AppUtils.getApp().getPackageManager().getPackageInfo(AppUtils.getApp().getPackageName(), 0);
            return pi == null ? "1.0.0" : pi.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "1.0.0";
        }
    }

    public static int getAppVersionCode() {
        return getPackageVersionCode(AppUtils.getApp(), AppUtils.getApp().getPackageName());
    }

    /**
     * 根据apk包名获取versionname
     */
    public static String getPackageVersionName(Context context, String packageName) {
        try {
            PackageInfo pi = context.getApplicationContext().getPackageManager().getPackageInfo(packageName, 0);
            return pi == null ? "1.0.0" : pi.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "1.0.0";
        }
    }

    /**
     * 根据apk包名获取apkversionCode
     */
    public static int getPackageVersionCode(Context context, String packageName) {
        try {
            PackageInfo pi = context.getApplicationContext().getPackageManager().getPackageInfo(packageName, 0);
            return pi == null ? -1 : pi.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 获取系统版本号，和系统方协调取此值
     * eg:201809021142,12位版本号
     */
    public static String getSystemVersionCode() {
        try {
            String incremental = Build.VERSION.INCREMENTAL;
            String systemVersion = null;
            if (incremental.length() > SYSTEM_VERSION_LIMIT) {
                systemVersion = incremental.substring(0, 12);
            } else {
                systemVersion = addZeroAfterNum(incremental, SYSTEM_VERSION_LIMIT);
            }
            return systemVersion;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "999999999999";
    }

    private static String addZeroAfterNum(String str, int strLength) {
        if (StringUtils.isEmpty(str)) {
            return "0";
        }
        int strLen = str.length();
        if (strLen < strLength) {
            while (strLen < strLength) {
                StringBuffer sb = new StringBuffer();
                sb.append(str).append("0");
                str = sb.toString();
                strLen = str.length();
            }
        }
        return str;
    }
}
