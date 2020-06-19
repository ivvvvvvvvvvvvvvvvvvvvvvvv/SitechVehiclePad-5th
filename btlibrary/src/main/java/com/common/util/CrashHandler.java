//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import java.lang.Thread.UncaughtExceptionHandler;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@SuppressLint({"SimpleDateFormat"})
public class CrashHandler implements UncaughtExceptionHandler {
    public static String TAG = "CrashHandler";
    private static CrashHandler instance;
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Map<String, String> infos = new HashMap();
    private Context mContext;
    private UncaughtExceptionHandler mDefaultHandler;

    private CrashHandler() {
    }

    public static CrashHandler getInstance(Context var0) {
        if (instance == null) {
            instance = new CrashHandler();
            instance.init(var0);
        }

        return instance;
    }

    private boolean handleException(Throwable var1) {
        String var2 = TAG;
        StringBuilder var3 = new StringBuilder();
        var3.append(this.mContext.getPackageName());
        var3.append(":");
        var3.append(var1);
        Log.e(var2, var3.toString());
        return true;
    }

    public void init(Context var1) {
        this.mContext = var1;
        this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread var1, Throwable var2) {
        this.handleException(var2);
    }
}
