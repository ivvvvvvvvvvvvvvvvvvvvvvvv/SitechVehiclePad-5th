package com.sitechdev.vehicle.lib.util;

import android.app.ActivityManager;
import android.content.Context;
import android.text.TextUtils;

/**
 * 项目名称：SitechVehiclePad
 * 类名称：ProcessUtil
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2019/08/12 0012 11:47
 * 修改时间：
 * 备注：
 */
public class ProcessUtil {

    /**
     * 不是主APP进程
     *
     * @return
     */
    public static boolean isMainProcess(Context context) {
        String curProcessName = getCurrentProcessName(context);
        return (!TextUtils.isEmpty(curProcessName)) && context.getApplicationContext().getPackageName().equals(curProcessName);
    }

    /**
     * 获取当前进程名
     */
    public static String getCurrentProcessName(Context context) {
        int pid = android.os.Process.myPid();
        String processName = "";
        android.app.ActivityManager manager = (android.app.ActivityManager) context.getApplicationContext().getSystemService
                (Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo process : manager.getRunningAppProcesses()) {
            if (process.pid == pid) {
                processName = process.processName;
            }
        }
        return processName;
    }
}
