//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.util.log;

import android.util.Log;

public class JLog {
    public static final int TYPE_DEBUG = 3;
    public static final int TYPE_ERROR = 2;
    public static final int TYPE_INFO = 1;
    public static final int TYPE_VERBOSE = 0;
    private String iTag = "JLog";
    boolean isPrint;
    int mDefaultType = 3;

    public JLog() {
    }

    public JLog(String var1, boolean var2, int var3) {
        this.setPrint(var2);
        this.setTag(var1);
    }

    private String byteArray2String(byte[] var1, int var2, int var3) {
        StringBuffer var6;
        for(var6 = new StringBuffer(); var2 < var3; ++var2) {
            String var5 = Integer.toHexString(var1[var2] & 255);
            String var4 = var5;
            if (var5.length() == 1) {
                StringBuilder var7 = new StringBuilder();
                var7.append('0');
                var7.append(var5);
                var4 = var7.toString();
            }

            var6.append(var4.toUpperCase());
        }

        return var6.toString();
    }

    public void print() {
        this.print("");
    }

    public void print(int var1) {
        this.print(String.valueOf(var1));
    }

    public void print(long var1) {
        this.print(String.valueOf(var1));
    }

    public void print(Object var1) {
        this.print(var1.toString());
    }

    public void print(String var1) {
        this.print(var1, this.mDefaultType);
    }

    public void print(String var1, int var2) {
        if (this.isPrint) {
            switch(var2) {
                case 0:
                    Log.v(this.iTag, var1);
                    break;
                case 1:
                    Log.i(this.iTag, var1);
                    return;
                case 2:
                    Log.e(this.iTag, var1);
                    return;
                case 3:
                    Log.d(this.iTag, var1);
                    return;
                default:
                    return;
            }
        }

    }

    public void print(String var1, byte[] var2) {
        StringBuilder var3 = new StringBuilder();
        var3.append(var1);
        var3.append(this.byteArray2String(var2, 0, var2.length));
        this.print(var3.toString());
    }

    public void print(boolean var1) {
        String var2;
        if (var1) {
            var2 = "true";
        } else {
            var2 = "false";
        }

        this.print(var2);
    }

    public void print(byte[] var1) {
        this.print(var1, 0, var1.length);
    }

    public void print(byte[] var1, int var2, int var3) {
        this.print(this.byteArray2String(var1, var2, var3));
    }

    public void setDefaultType(int var1) {
        this.mDefaultType = var1;
    }

    public void setPrint(boolean var1) {
        this.isPrint = var1;
    }

    public void setTag(String var1) {
        this.iTag = var1;
    }
}
