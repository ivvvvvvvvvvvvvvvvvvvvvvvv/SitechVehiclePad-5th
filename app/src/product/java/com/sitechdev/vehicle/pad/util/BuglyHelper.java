package com.sitechdev.vehicle.pad.util;

import android.content.Context;
import android.text.TextUtils;

import com.sitechdev.vehicle.lib.event.BaseEvent;
import com.sitechdev.vehicle.pad.BuildConfig;
import com.sitechdev.vehicle.pad.app.AppApplication;
import com.tencent.bugly.crashreport.CrashReport;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Bugly 工具类
 *
 * @author liuhe
 * @date 18-7-17
 */
public class BuglyHelper {
    private BuglyHelper() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    private static class SingleBuglyHelper {
        private static final BuglyHelper BUGLYHELPER = new BuglyHelper();
    }

    public static BuglyHelper getInstance() {
        return SingleBuglyHelper.BUGLYHELPER;
    }

    public void initCrashReport(Context ctx) {
        if (null == ctx) {
            return;
        }
        Context context = ctx.getApplicationContext();
        // 获取当前包名
        String packageName = context.getPackageName();
        // 获取当前进程名
        String processName = getProcessName(android.os.Process.myPid());
        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        // 初始化Bugly
        CrashReport.initCrashReport(context, "f2ead9f793", BuildConfig.DEBUG, strategy);
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventListener(BaseEvent event) {
//            try {
//                //传递vin码
//                String vinCode = SerialUtils.getVinCode();
//                if (!SerialUtils.isGoodVin(vinCode)) {
//                    vinCode = SerialUtils.getVinTboxCode();
//                    if (!SerialUtils.isGoodVin(vinCode)) {
//                        return;
//                    }
//                }
//                setCrashParams("carVinCode", vinCode);
//                setCrashParams("carIccId", SerialUtils.getTboxIccId());
//                setCrashParams("carSN", SerialUtils.getSnCode());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
    }

    /**
     * 设置参数
     */
    private void setCrashParams(String key, String value) {
        try {
            CrashReport.putUserData(AppApplication.getContext(), key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
