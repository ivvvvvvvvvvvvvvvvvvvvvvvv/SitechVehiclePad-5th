//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.util;

import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class Util {
    private static final String LOG_CONFIG = "/mnt/vendor/log.txt";
    private static Locale mCurLocale;
    private static Instrumentation mInst;
    private static int mKey = 0;
    private static final String tag = "Util";

    public Util() {
    }

    public static String BToH(String var0) {
        return Integer.toHexString(Integer.valueOf(toD(var0, 2)));
    }

    public static String HToB(String var0) {
        return Integer.toBinaryString(Integer.valueOf(toD(var0, 16)));
    }

    public static int HToO(String var0) {
        return Integer.valueOf(toD(var0.toLowerCase(), 16));
    }

    public static String byte2HexStr(byte[] var0) {
        StringBuilder var3 = new StringBuilder("");

        for(int var1 = 0; var1 < var0.length; ++var1) {
            String var2 = Integer.toHexString(var0[var1] & 255);
            if (var2.length() == 1) {
                StringBuilder var4 = new StringBuilder();
                var4.append("0");
                var4.append(var2);
                var2 = var4.toString();
            }

            var3.append(var2);
            var3.append(" ");
        }

        return var3.toString().toUpperCase().trim();
    }

    public static void byteArrayCopy(byte[] var0, byte[] var1, int var2, int var3, int var4) {
        for(int var5 = 0; var5 < var4; ++var5) {
            var0[var5 + var2] = var1[var3 + var5];
        }

    }

    public static String byteArrayToHex(byte[] var0) {
        String var3 = "";

        for(int var1 = 0; var1 < var0.length; ++var1) {
            String var2 = Integer.toHexString(var0[var1] & 255);
            StringBuilder var4;
            if (var2.length() == 1) {
                var4 = new StringBuilder();
                var4.append(var3);
                var4.append("0");
                var4.append(var2);
                var2 = var4.toString();
            } else {
                var4 = new StringBuilder();
                var4.append(var3);
                var4.append(var2);
                var2 = var4.toString();
            }

            var3 = var2;
            if (var1 < var0.length - 1) {
                StringBuilder var5 = new StringBuilder();
                var5.append(var2);
                var5.append("");
                var3 = var5.toString();
            }
        }

        return var3;
    }

    public static String bytes2HexString(byte[] var0) {
        StringBuilder var5 = new StringBuilder();
        int var2 = var0.length;

        for(int var1 = 0; var1 < var2; ++var1) {
            String var4 = Integer.toHexString(var0[var1] & 255);
            String var3 = var4;
            if (var4.length() == 1) {
                StringBuilder var6 = new StringBuilder();
                var6.append('0');
                var6.append(var4);
                var3 = var6.toString();
            }

            var5.append(var3);
        }

        return var5.toString().toUpperCase();
    }

    public static void checkAKDRunning() {
        doSleep(1L);

        for(int var0 = 0; var0 < 1000 && "running".equals(getProperty("init.svc.akd")); ++var0) {
            doSleep(10L);
        }

        doSleep(1L);
    }

    public static void clearBuf(byte[] var0) {
        if (var0 != null) {
            for(int var1 = 0; var1 < var0.length; ++var1) {
                var0[var1] = 0;
            }
        }

    }

    public static boolean copyFile(String var0, String var1) {
        Exception var10000;
        label48: {
            boolean var10001;
            try {
                StringBuilder var4 = new StringBuilder();
                var4.append(var0);
                var4.append(":::::");
                var4.append(var1);
                Log.d("allen", var4.toString());
            } catch (Exception var9) {
                var10000 = var9;
                var10001 = false;
                break label48;
            }

            int var2 = 0;

            FileInputStream var10;
            FileOutputStream var12;
            byte[] var13;
            try {
                if (!(new File(var0)).exists()) {
                    return false;
                }

                var10 = new FileInputStream(var0);
                var12 = new FileOutputStream(var1);
                var13 = new byte[1444];
            } catch (Exception var7) {
                var10000 = var7;
                var10001 = false;
                break label48;
            }

            while(true) {
                int var3;
                try {
                    var3 = var10.read(var13);
                } catch (Exception var6) {
                    var10000 = var6;
                    var10001 = false;
                    break;
                }

                if (var3 == -1) {
                    try {
                        var10.close();
                        return true;
                    } catch (Exception var5) {
                        var10000 = var5;
                        var10001 = false;
                        break;
                    }
                }

                var2 += var3;

                try {
                    System.out.println(var2);
                    var12.write(var13, 0, var3);
                } catch (Exception var8) {
                    var10000 = var8;
                    var10001 = false;
                    break;
                }
            }
        }

        Exception var11 = var10000;
        System.out.println("复制单个文件操作出错");
        var11.printStackTrace();
        return false;
    }

    public static void copyFolder(String var0, String var1) {
        Exception var10000;
        label78: {
            String[] var5;
            boolean var10001;
            try {
                (new File(var1)).mkdirs();
                var5 = (new File(var0)).list();
            } catch (Exception var16) {
                var10000 = var16;
                var10001 = false;
                break label78;
            }

            int var2 = 0;

            label72:
            while(true) {
                StringBuilder var4;
                File var18;
                label69: {
                    try {
                        if (var2 >= var5.length) {
                            return;
                        }

                        if (var0.endsWith(File.separator)) {
                            var4 = new StringBuilder();
                            var4.append(var0);
                            var4.append(var5[var2]);
                            var18 = new File(var4.toString());
                            break label69;
                        }
                    } catch (Exception var15) {
                        var10000 = var15;
                        var10001 = false;
                        break;
                    }

                    try {
                        var4 = new StringBuilder();
                        var4.append(var0);
                        var4.append(File.separator);
                        var4.append(var5[var2]);
                        var18 = new File(var4.toString());
                    } catch (Exception var12) {
                        var10000 = var12;
                        var10001 = false;
                        break;
                    }
                }

                label60: {
                    FileInputStream var6;
                    byte[] var8;
                    FileOutputStream var21;
                    try {
                        if (!var18.isFile()) {
                            break label60;
                        }

                        var6 = new FileInputStream(var18);
                        StringBuilder var7 = new StringBuilder();
                        var7.append(var1);
                        var7.append("/");
                        var7.append(var18.getName().toString());
                        var21 = new FileOutputStream(var7.toString());
                        var8 = new byte[5120];
                    } catch (Exception var14) {
                        var10000 = var14;
                        var10001 = false;
                        break;
                    }

                    while(true) {
                        int var3;
                        try {
                            var3 = var6.read(var8);
                        } catch (Exception var10) {
                            var10000 = var10;
                            var10001 = false;
                            break label72;
                        }

                        if (var3 == -1) {
                            try {
                                var21.flush();
                                var21.close();
                                var6.close();
                                break;
                            } catch (Exception var9) {
                                var10000 = var9;
                                var10001 = false;
                                break label72;
                            }
                        }

                        try {
                            var21.write(var8, 0, var3);
                        } catch (Exception var11) {
                            var10000 = var11;
                            var10001 = false;
                            break label72;
                        }
                    }
                }

                try {
                    if (var18.isDirectory()) {
                        var4 = new StringBuilder();
                        var4.append(var0);
                        var4.append("/");
                        var4.append(var5[var2]);
                        String var19 = var4.toString();
                        StringBuilder var20 = new StringBuilder();
                        var20.append(var1);
                        var20.append("/");
                        var20.append(var5[var2]);
                        copyFolder(var19, var20.toString());
                    }
                } catch (Exception var13) {
                    var10000 = var13;
                    var10001 = false;
                    break;
                }

                ++var2;
            }
        }

        Exception var17 = var10000;
        System.out.println("复制整个文件夹内容操作出错");
        var17.printStackTrace();
    }

    public static final int daysBetween(Date var0, Date var1) {
        SimpleDateFormat var5 = new SimpleDateFormat("yyyy-MM-dd");
        Date var4 = var0;

        label25: {
            Date var10;
            label24: {
                ParseException var10000;
                label29: {
                    boolean var10001;
                    try {
                        var0 = var5.parse(var5.format(var0));
                    } catch (ParseException var7) {
                        var10000 = var7;
                        var10001 = false;
                        break label29;
                    }

                    var4 = var0;

                    try {
                        var10 = var5.parse(var5.format(var1));
                        break label24;
                    } catch (ParseException var6) {
                        var10000 = var6;
                        var10001 = false;
                    }
                }

                ParseException var8 = var10000;
                var8.printStackTrace();
                var0 = var4;
                break label25;
            }

            var1 = var10;
        }

        Calendar var9 = Calendar.getInstance();
        var9.setTime(var0);
        long var2 = var9.getTimeInMillis();
        var9.setTime(var1);
        return Integer.parseInt(String.valueOf((var9.getTimeInMillis() - var2) / 86400000L));
    }

    public static void doKeyEvent(int var0) {
        mKey = var0;
        (new Thread(new Runnable() {
            public void run() {
                if (Util.mKey != 0) {
                    try {
                        if (Util.mInst == null) {
                            Util.mInst = new Instrumentation();
                        }

                        Util.mInst.sendKeyDownUpSync(Util.mKey);
                    } catch (Exception var2) {
                        Log.e("Exception when sendPointerSync", var2.toString());
                    }

                    Util.mKey = 0;
                }

            }
        })).start();
    }

    public static void doSleep(long var0) {
        try {
            Thread.sleep(var0);
        } catch (InterruptedException var3) {
            var3.printStackTrace();
        }
    }

    public static int do_exec(String var0) {
        try {
            int var1 = Runtime.getRuntime().exec(var0).waitFor();
            StringBuilder var2 = new StringBuilder();
            var2.append("do_exec:");
            var2.append(var0);
            var2.append(":err:");
            var2.append(var1);
            Log.e("Util", var2.toString());
            return var1;
        } catch (Exception var3) {
            Log.e("Util", var3.toString());
            return -1;
        }
    }

    public static String fileMD5(String param0) throws IOException {
        // $FF: Couldn't be decompiled
        return "";
    }

    public static int formatting(String var0) {
        int var2 = 0;

        for(int var1 = 0; var1 < 10; ++var1) {
            if (var0.equals(String.valueOf(var1))) {
                var2 = var1;
            }
        }

        if (var0.equals("a")) {
            var2 = 10;
        }

        if (var0.equals("b")) {
            var2 = 11;
        }

        if (var0.equals("c")) {
            var2 = 12;
        }

        if (var0.equals("d")) {
            var2 = 13;
        }

        if (var0.equals("e")) {
            var2 = 14;
        }

        if (var0.equals("f")) {
            var2 = 15;
        }

        return var2;
    }

    public static String formattingH(int var0) {
        switch(var0) {
            case 10:
                return "a";
            case 11:
                return "b";
            case 12:
                return "c";
            case 13:
                return "d";
            case 14:
                return "e";
            case 15:
                return "f";
            default:
                return String.valueOf(var0);
        }
    }

    public static List<ResolveInfo> getActivityList(Context var0) {
        Intent var1 = new Intent("android.intent.action.MAIN", (Uri)null);
        var1.addCategory("android.intent.category.LAUNCHER");
        List var8 = var0.getPackageManager().queryIntentActivities(var1, 0);
        Iterator var2 = var8.iterator();

        while(var2.hasNext()) {
            ResolveInfo var3 = (ResolveInfo)var2.next();

            Context var4;
            try {
                var4 = var0.createPackageContext(var3.activityInfo.packageName, 2);
            } catch (NameNotFoundException var7) {
                var7.printStackTrace();
                break;
            }

            Resources var10 = var4.getResources();
            if (var10 != null) {
                mCurLocale = var10.getConfiguration().locale;
                var10 = getResourcesByLocale(var10, Locale.SIMPLIFIED_CHINESE);
                String var5 = var10.getResourceName(var3.activityInfo.applicationInfo.labelRes);
                String var9 = var10.getString(var3.activityInfo.applicationInfo.labelRes);
                StringBuilder var6 = new StringBuilder();
                var6.append("zh appName = ");
                var6.append(var9);
                var6.append(", resName = ");
                var6.append(var5);
                var6.append(", locale = ");
                var6.append(var10.getConfiguration().locale);
                Log.d("Util", var6.toString());
                resetLocale(var10);
            }
        }

        return var8;
    }

    public static final String getActivityNameByPkgNameAndClzName(Context var0, String var1, String var2) {
        PackageManager var3 = var0.getPackageManager();
        String var6 = null;

        Exception var10000;
        label27: {
            boolean var10001;
            ActivityInfo var8;
            try {
                var8 = var3.getActivityInfo(new ComponentName(var1, var2), 8388608);
            } catch (Exception var5) {
                var10000 = var5;
                var10001 = false;
                break label27;
            }

            if (var8 == null) {
                return var6;
            }

            try {
                var6 = var8.loadLabel(var3).toString();
                return var6;
            } catch (Exception var4) {
                var10000 = var4;
                var10001 = false;
            }
        }

        Exception var7 = var10000;
        var7.printStackTrace();
        return null;
    }

    public static String getFileString(String var0) {
        Object var2 = null;
        String var1 = (String)var2;

        boolean var10001;
        FileReader var3;
        try {
            var3 = new FileReader(var0);
        } catch (Exception var9) {
            var10001 = false;
            return var1;
        }

        var1 = (String)var2;

        BufferedReader var4;
        try {
            var4 = new BufferedReader(var3, 256);
        } catch (Exception var8) {
            var10001 = false;
            return var1;
        }

        var1 = (String)var2;

        try {
            var0 = var4.readLine();
        } catch (Exception var7) {
            var10001 = false;
            return var1;
        }

        var1 = var0;

        try {
            var4.close();
        } catch (Exception var6) {
            var10001 = false;
            return var1;
        }

        var1 = var0;

        try {
            var3.close();
            return var0;
        } catch (Exception var5) {
            var10001 = false;
            return var1;
        }
    }

    public static int getFileValue(String var0) {
        int var1;
        try {
            if (!(new File(var0)).exists()) {
                return -1;
            }

            FileInputStream var6 = new FileInputStream(var0);
            DataInputStream var2 = new DataInputStream(var6);
            String var3 = var2.readLine();
            var2.close();
            var6.close();

            try {
                if (var3.startsWith("0x")) {
                    var1 = Integer.parseInt(var3.replace("0x", ""), 16);
                } else {
                    var1 = Integer.parseInt(var3, 10);
                }

                return var1;
            } catch (NumberFormatException var4) {
                var4.printStackTrace();
            }
        } catch (IOException var5) {
            var5.printStackTrace();
            return -1;
        }

        var1 = -1;
        return var1;
    }

    public static final ActivityInfo getPackageNameByProgramName(Context var0, String var1) {
        new EditDistance();
        Intent var6 = new Intent("android.intent.action.MAIN", (Uri)null);
        var6.addCategory("android.intent.category.LAUNCHER");
        Iterator var14 = var0.getPackageManager().queryIntentActivities(var6, 0).iterator();

        while(var14.hasNext()) {
            ResolveInfo var7 = (ResolveInfo)var14.next();

            Context var15;
            label44: {
                NameNotFoundException var13;
                label43: {
                    String var8;
                    var8 = var7.activityInfo.packageName;

                    try {
                        var15 = var0.createPackageContext(var8, 2);
                        break label44;
                    } catch (NameNotFoundException var11) {
                        var13 = var11;
                    }
                }

                var13.printStackTrace();
                return null;
            }

            Resources var16 = var15.getResources();
            if (var16 != null) {
                int var5 = var7.labelRes;
                int var4 = var5;
                if (var5 == 0) {
                    var4 = var7.activityInfo.applicationInfo.labelRes;
                }

                if (var4 != 0) {
                    mCurLocale = var16.getConfiguration().locale;
                    Resources var9 = getResourcesByLocale(var16, Locale.SIMPLIFIED_CHINESE);
                    String var10 = var9.getString(var4);
                    if (var10 != null) {
                        double var2 = EditDistance.getSimilarity(var10, var1);
                        if (var2 >= 0.6D && var2 <= 1.0D) {
                            resetLocale(var16);
                            return var7.activityInfo;
                        }

                        resetLocale(var9);
                    }
                }
            }
        }

        return null;
    }

    public static final String getProgramNameByPackageName(Context var0, String var1) {
        PackageManager var3 = var0.getPackageManager();

        try {
            String var4 = var3.getApplicationLabel(var3.getApplicationInfo(var1, 128)).toString();
            return var4;
        } catch (NameNotFoundException var2) {
            var2.printStackTrace();
            return null;
        }
    }

    public static String getProperty(String var0) {
        try {
            Class var1 = Class.forName("android.os.SystemProperties");
            var0 = (String)var1.getMethod("get", String.class).invoke(var1, var0);
            return var0;
        } catch (Exception var2) {
            var2.printStackTrace();
            return null;
        }
    }

    private static final Resources getResourcesByLocale(Resources var0, Locale var1) {
        Configuration var2 = new Configuration(var0.getConfiguration());
        var2.locale = var1;
        return new Resources(var0.getAssets(), var0.getDisplayMetrics(), var2);
    }

    public static String getTimeString(long var0) {
        int var3 = (int)(var0 / 3600L);
        int var2;
        if (var3 > 0) {
            var2 = (int)(var0 % 3600L / 60L);
        } else {
            var2 = (int)(var0 / 60L);
        }

        int var4 = (int)var0 % 60;
        StringBuffer var5 = new StringBuffer();
        if (var3 > 0) {
            var5.append(var3);
            var5.append(":");
        }

        if (var2 < 10) {
            var5.append("0");
        }

        var5.append(var2);
        var5.append(":");
        if (var4 < 10) {
            var5.append("0");
        }

        var5.append(var4);
        return var5.toString();
    }

    public static byte[] hexStr2Bytes(String var0) {
        int var2 = var0.length() / 2;
        System.out.println(var2);
        byte[] var4 = new byte[var2];

        for(int var1 = 0; var1 < var2; ++var1) {
            int var3 = var1 * 2 + 1;
            StringBuilder var5 = new StringBuilder();
            var5.append("0x");
            var5.append(var0.substring(var1 * 2, var3));
            var5.append(var0.substring(var3, var3 + 1));
            var4[var1] = Byte.decode(var5.toString());
        }

        return var4;
    }

    public static String hexStr2Str(String var0) {
        char[] var2 = var0.toCharArray();
        byte[] var3 = new byte[var0.length() / 2];

        for(int var1 = 0; var1 < var3.length; ++var1) {
            var3[var1] = (byte)("0123456789ABCDEF".indexOf(var2[2 * var1]) * 16 + "0123456789ABCDEF".indexOf(var2[2 * var1 + 1]) & 255);
        }

        return new String(var3);
    }

    public static boolean isAndroidO() {
        return VERSION.SDK_INT <= 27;
    }

    public static boolean isAndroidP() {
        return VERSION.SDK_INT == 28;
    }

    public static boolean isAndroidQ() {
        return VERSION.SDK_INT == 29;
    }

    public static boolean isBufEquals(byte[] var0, byte[] var1) {
        if (var0 != null && var1 != null) {
            if (var0.length != var1.length) {
                return false;
            } else {
                for(int var2 = 0; var2 < var0.length; ++var2) {
                    if (var0[var2] != var1[var2]) {
                        return false;
                    }
                }

                return true;
            }
        } else {
            return false;
        }
    }

    public static boolean isGLCamera() {
        return isRKSystem();
    }

    public static boolean isNexellSystem() {
        return Build.DISPLAY.contains("s5p6818") || Build.DISPLAY.contains("aosp_avn_ref");
    }

    public static boolean isNexellSystem60() {
        return Build.DISPLAY.contains("s5p6818") && VERSION.SDK_INT <= 23;
    }

    public static boolean isPX30() {
        return Build.DISPLAY.contains("px30") || Build.DISPLAY.contains("rk3326");
    }

    public static boolean isPX5() {
        return Build.DISPLAY.contains("px5") || Build.DISPLAY.contains("rk3368");
    }

    public static boolean isPX6() {
        return Build.DISPLAY.contains("px6") || Build.DISPLAY.contains("rk3399");
    }

    public static boolean isRKSystem() {
        return isPX5() || isPX6() || isPX30() || isAndroidP() || isAndroidQ();
    }

    public static final boolean isSimilarity(String var0, String var1) {
        return isSimilarity(var0, var1, 0.75F);
    }

    public static final boolean isSimilarity(String var0, String var1, float var2) {
        double var3 = EditDistance.getSimilarity(var0, var1);
        return var3 >= (double)var2 && var3 <= 1.0D;
    }

    public static boolean isSufaceFlashInWallpaperApp() {
        return isRKSystem();
    }

    public static final boolean isZero(byte[] var0) {
        if (var0 != null) {
            for(int var1 = 0; var1 < var0.length; ++var1) {
                if (var0[var1] != 0) {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }

    public static void logAccPowerOff(String var0) {
        String var2 = getFileString("/mnt/vendor/log.txt");
        String var1 = var0;
        if (var2 != null) {
            StringBuilder var3 = new StringBuilder();
            var3.append(var2);
            var3.append("\r\n");
            var3.append(var0);
            var1 = var3.toString();
        }

        setFileValue("/mnt/vendor/log.txt", var1);
    }

    private static final void resetLocale(Resources var0) {
        Configuration var1 = new Configuration(var0.getConfiguration());
        var1.locale = mCurLocale;
        new Resources(var0.getAssets(), var0.getDisplayMetrics(), var1);
    }

    public static final void setBeepToMcu() {
        setFileValue("/sys/class/ak/source/beep", 2);
    }

    public static void setFileValue(String var0, int var1) {
        try {
            File var4 = new File(var0);
            if (var4.exists()) {
                StringBuffer var2 = new StringBuffer();
                var2.append(var1);
                String var6 = var2.toString();
                PrintWriter var5 = new PrintWriter(new FileOutputStream(var4));
                var5.write(var6);
                var5.flush();
                var5.close();
            }
        } catch (IOException var3) {
            var3.printStackTrace();
        }
    }

    public static void setFileValue(String var0, String var1) {
        try {
            FileOutputStream var4 = new FileOutputStream(var0);
            DataOutputStream var2 = new DataOutputStream(var4);
            var2.write(var1.getBytes());
            var2.close();
            var4.close();
        } catch (IOException var3) {
            var3.printStackTrace();
        }
    }

    public static void setFileValue(String var0, byte[] var1) {
        File var5 = new File(var0);
        if (var5.exists()) {
            try {
                FileOutputStream var6 = new FileOutputStream(var5);
                DataOutputStream var2 = new DataOutputStream(var6);
                var2.write(var1);
                var2.close();
                var6.close();
            } catch (FileNotFoundException var3) {
                var3.printStackTrace();
            } catch (IOException var4) {
                var4.printStackTrace();
                return;
            }
        }

    }

    public static void setFileValue(byte[] var0, String var1) {
        File var5 = new File(var1);
        if (var5.exists()) {
            try {
                FileOutputStream var6 = new FileOutputStream(var5);
                DataOutputStream var2 = new DataOutputStream(var6);
                var2.write(var0);
                var2.close();
                var6.close();
            } catch (FileNotFoundException var3) {
                var3.printStackTrace();
            } catch (IOException var4) {
                var4.printStackTrace();
                return;
            }
        }

    }

    public static int setProperty(String var0, String var1) {
        try {
            StringBuilder var2 = new StringBuilder();
            var2.append(var0);
            var2.append("=");
            var2.append(var1);
            Log.d("Util", var2.toString());
            Class var4 = Class.forName("android.os.SystemProperties");
            var4.getMethod("set", String.class, String.class).invoke(var4, var0, var1);
            return 0;
        } catch (Exception var3) {
            var3.printStackTrace();
            return -1;
        }
    }

    public static String str2HexStr(String var0) {
        char[] var2 = "0123456789ABCDEF".toCharArray();
        StringBuilder var3 = new StringBuilder("");
        byte[] var4 = var0.getBytes();

        for(int var1 = 0; var1 < var4.length; ++var1) {
            var3.append(var2[(var4[var1] & 240) >> 4]);
            var3.append(var2[var4[var1] & 15]);
            var3.append(' ');
        }

        return var3.toString().trim();
    }

    public static String strToUnicode(String var0) throws Exception {
        StringBuilder var3 = new StringBuilder();

        for(int var1 = 0; var1 < var0.length(); ++var1) {
            char var2 = var0.charAt(var1);
            String var4 = Integer.toHexString(var2);
            StringBuilder var5;
            if (var2 > 128) {
                var5 = new StringBuilder();
                var5.append("\\u");
                var5.append(var4);
                var3.append(var5.toString());
            } else {
                var5 = new StringBuilder();
                var5.append("\\u00");
                var5.append(var4);
                var3.append(var5.toString());
            }
        }

        return var3.toString();
    }

    public static int sudoExec(String var0) {
        StringBuilder var1 = new StringBuilder();
        var1.append("sudoExec:");
        var1.append(var0);
        Log.e("Util", var1.toString());
        if (VERSION.SDK_INT >= 25) {
            var1 = new StringBuilder();
            var1.append("akd:");
            var1.append(var0);
            setProperty("ctl.start", var1.toString());
            checkAKDRunning();
            return 0;
        } else {
            var1 = new StringBuilder();
            var1.append("start akd:");
            var1.append(var0);
            var1.append("");
            return do_exec(var1.toString());
        }
    }

    public static int sudoExecNoCheck(String var0) {
        StringBuilder var1 = new StringBuilder();
        var1.append("sudoExecNoCheck:");
        var1.append(var0);
        Log.e("Util", var1.toString());
        if (VERSION.SDK_INT >= 25) {
            var1 = new StringBuilder();
            var1.append("akd:");
            var1.append(var0);
            setProperty("ctl.start", var1.toString());
            return 0;
        } else {
            var1 = new StringBuilder();
            var1.append("start akd:");
            var1.append(var0);
            var1.append("");
            return do_exec(var1.toString());
        }
    }

    public static String toD(String var0, int var1) {
        int var3 = 0;

        for(int var2 = 0; var2 < var0.length(); ++var2) {
            var3 = (int)((double)var3 + (double)formatting(var0.substring(var2, var2 + 1)) * Math.pow((double)var1, (double)(var0.length() - var2 - 1)));
        }

        return String.valueOf(var3);
    }

    public static String unicodeToString(String var0) {
        int var2 = var0.length() / 6;
        StringBuilder var3 = new StringBuilder();

        for(int var1 = 0; var1 < var2; ++var1) {
            String var4 = var0.substring(var1 * 6, (var1 + 1) * 6);
            StringBuilder var5 = new StringBuilder();
            var5.append(var4.substring(2, 4));
            var5.append("00");
            String var6 = var5.toString();
            var4 = var4.substring(4);
            var3.append(new String(Character.toChars(Integer.valueOf(var6, 16) + Integer.valueOf(var4, 16))));
        }

        return var3.toString();
    }

    public static final void zeroBuf(byte[] var0) {
        if (var0 != null) {
            for(int var1 = 0; var1 < var0.length; ++var1) {
                var0[var1] = 0;
            }
        }

    }
}
