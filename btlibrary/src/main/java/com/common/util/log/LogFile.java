//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.util.log;

import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LogFile {
    private static boolean mIsLog = false;
    private static String mLogFile = "/sdcard/car_logfile.txt";

    public LogFile() {
    }

    public static void d(String var0, String var1) {
        if (mIsLog) {
            String var2 = mLogFile;
            StringBuilder var3 = new StringBuilder();
            var3.append(var0);
            var3.append("   ");
            var3.append(var1);
            var3.append("\n");
            setFileValueEx(var2, var3.toString());
        }

        Log.d(var0, var1);
    }

    public static void init(String var0) {
        mLogFile = var0;
        File var2 = new File(var0);
        if (!var2.exists()) {
            try {
                var2.createNewFile();
            } catch (Exception var1) {
                Log.d("", var1.toString());
            }
        }

        mIsLog = true;
    }

    public static void setFileValueEx(String var0, String var1) {
        try {
            FileWriter var3 = new FileWriter(var0, true);
            var3.write(var1);
            var3.close();
        } catch (IOException var2) {
            var2.printStackTrace();
        }
    }
}
