package com.sitechdev.vehicle.pad.util;

import android.os.Environment;

import com.sitechdev.vehicle.pad.BuildConfig;
import com.sitechdev.vehicle.pad.app.AppApplication;
import com.tencent.mars.xlog.Log;
import com.tencent.mars.xlog.Xlog;

/**
 * 项目名称：SitechVehiclePad-5th
 * 类名称：MarsXlogUtil
 * 类描述： 使用腾讯mar组件的xlog工具类，保存日志
 * 创建人：Administrator
 * 创建时间：2020/04/22 0022 10:29
 * 修改时间：
 * 备注：
 */
public class MarsXlogUtil {
    private static final String SDCARD = Environment.getExternalStorageDirectory().getAbsolutePath();
    private static final String logPath = SDCARD + "/SitechPadLog/log";
    // this is necessary, or may crash for SIGBUS
    private static final String cachePath = AppApplication.getContext().getFilesDir() + "/SitechPadCachelog";

    static {
        System.loadLibrary("c++_shared");
        System.loadLibrary("marsxlog");
    }

    public static void initXlog() {
        //init xlog
        if (BuildConfig.DEBUG) {
            Xlog.appenderOpen(Xlog.LEVEL_DEBUG, Xlog.AppednerModeAsync, cachePath, logPath, "SitechPad_debug_log", 0, "");
        } else {
            Xlog.appenderOpen(Xlog.LEVEL_INFO, Xlog.AppednerModeAsync, cachePath, logPath, "SitechPad_log", 0, "");
        }
        Xlog.setConsoleLogOpen(BuildConfig.DEBUG);
        Log.setLogImp(new Xlog());
    }

    public void closeXlog() {
        Log.appenderClose();
    }
}
