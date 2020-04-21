
package com.sitechdev.vehicle.lib.util;

import android.os.RemoteException;
import android.util.Log;

/**
 * 日志打印类
 */
public class SitechDevLog {
    private static boolean TRACE_LOG = true;
    private static boolean SAVE_LOGS = false;
    private static final String TAG = "SitechDevLog";

    private SitechDevLog() {
        throw new AssertionError();
    }

    public static void openDebug(boolean isOpen) {
        TRACE_LOG = isOpen;
    }

    public static void saveLogs(boolean saveLogs) {
        SAVE_LOGS = saveLogs;
    }

    /**
     * 写入轨迹数据，记忆轨迹（非错误数据），非实时保存
     *
     * @param msg
     */
    public static void TRACE(String msg) {
        if (!TRACE_LOG) {
            return;
        }
        try {
            // TODO why track log?
//            TraceImpl.getInstance().setTrackLog(msg);
            d("TRACE", msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 写入程序ERROR数据，实时保存到文件
     */
    public static void ERROR(String tag, String msg) {
        try {
            TraceImpl.getInstance().setErrLog(tag, msg);
            e(tag, msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void SAVE() {
        TraceImpl.getInstance().saveTrack();
    }

    public static void i(String msg) {
        if (TRACE_LOG) {
            Log.i(TAG, msg);
            saveLogs2Files(TAG, msg);
        }
    }

    public static void d(String msg) {
        if (TRACE_LOG) {
            Log.d(TAG, msg);
            saveLogs2Files(TAG, msg);
        }
    }

    public static void w(String msg) {
        if (TRACE_LOG) {
            Log.w(TAG, msg);
            saveLogs2Files(TAG, msg);
        }
    }

    public static void e(String msg) {
        if (TRACE_LOG) {
            Log.e(TAG, msg);
            saveLogs2Files(TAG, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (TRACE_LOG) {
            Log.i(tag, msg);
            saveLogs2Files(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (TRACE_LOG) {
            Log.d(tag, msg);
            saveLogs2Files(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (TRACE_LOG) {
            Log.w(tag, msg);
            saveLogs2Files(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (TRACE_LOG) {
            Log.e(tag, "msg:" + msg);
            saveLogs2Files(tag, msg);
        }
    }

    public static void exception(Exception e) {
        if (TRACE_LOG && e != null) {
            e.printStackTrace();
            saveLogs2Files("exception", e.getMessage());
        }
    }

    private static void saveLogs2Files(String tag, String msg) {
        if (!SAVE_LOGS) {
            return;
        }
        try {
            TraceImpl.getInstance().setErrLog(tag, msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static String getTraceFilePath() {
        return TraceImpl.getInstance().traceFilePath();
    }

}
