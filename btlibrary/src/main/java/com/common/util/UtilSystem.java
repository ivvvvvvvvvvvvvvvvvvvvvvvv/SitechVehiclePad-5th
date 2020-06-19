//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.util.Log;
import android.view.Window;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UtilSystem {
    private static final String TAG = "UtilSystem";

    public UtilSystem() {
    }

    public static void doRunActivity(Context var0, String var1) {
        try {
            Intent var3 = new Intent(var1);
            var3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            var0.startActivity(var3);
        } catch (Exception var2) {
            Log.e("UtilSystem", var2.getMessage());
        }
    }

    public static boolean doRunActivity(Context var0, String var1, String var2) {
        try {
            Intent var3 = new Intent("android.intent.action.VIEW");
            var3.setClassName(var1, var2);
            var3.setFlags(272629760);
            var0.startActivity(var3);
            return true;
        } catch (Exception var4) {
            Log.e("UtilSystem", var4.getMessage());
            return false;
        }
    }

    public static boolean isTopActivity(Context var0, String var1) {
        List var2 = ((ActivityManager)var0.getSystemService("activity")).getRunningTasks(1);
        return var2.size() > 0 && var1.equals(((RunningTaskInfo)var2.get(0)).topActivity.getPackageName());
    }

    public static void killProcess(String var0) {
        boolean var10001;
        String[] var12;
        try {
            Runtime var4 = Runtime.getRuntime();
            StringBuilder var5 = new StringBuilder();
            var5.append("ps | grep ");
            var5.append(var0);
            Process var10 = var4.exec(var5.toString());
            var10.waitFor();
            BufferedReader var11 = new BufferedReader(new InputStreamReader(var10.getInputStream()));
            var11.readLine();
            var12 = var11.readLine().split(" ");
        } catch (Exception var9) {
            var10001 = false;
            return;
        }

        int var2 = 0;
        int var1 = 0;

        while(true) {
            try {
                if (var1 >= var12.length) {
                    break;
                }
            } catch (Exception var7) {
                var10001 = false;
                return;
            }

            int var3 = var2;

            label39: {
                try {
                    if (var12[var1].length() <= 0) {
                        break label39;
                    }
                } catch (Exception var8) {
                    var10001 = false;
                    return;
                }

                var3 = var2 + 1;
            }

            if (var3 == 2) {
                break;
            }

            ++var1;
            var2 = var3;
        }

        var0 = var12[var1];

        try {
            StringBuilder var13 = new StringBuilder();
            var13.append("kill:");
            var13.append(var0);
            Util.sudoExec(var13.toString());
        } catch (Exception var6) {
            var10001 = false;
        }
    }

    public static List<StorageInfo> listAllStorage(Context var0) {
        ArrayList var4 = new ArrayList();
        int var2 = VERSION.SDK_INT;
        byte var1 = 0;
        byte var19;
        if (var2 >= 25) {
            StorageManager var21 = (StorageManager)var0.getSystemService("storage");

            label84: {
                Exception var10000;
                label91: {
                    boolean var10001;
                    List var22;
                    try {
                        Class[] var16 = new Class[0];
                        var22 = (List)StorageManager.class.getMethod("getVolumes", var16).invoke(var21);
                    } catch (Exception var14) {
                        var10000 = var14;
                        var10001 = false;
                        break label91;
                    }

                    if (var22 == null) {
                        break label84;
                    }

                    Iterator var23;
                    try {
                        var23 = var22.iterator();
                    } catch (Exception var13) {
                        var10000 = var13;
                        var10001 = false;
                        break label91;
                    }

                    while(true) {
                        File var6;
                        Object var7;
                        try {
                            if (!var23.hasNext()) {
                                break label84;
                            }

                            var7 = var23.next();
                            var6 = (File)var7.getClass().getMethod("getPath").invoke(var7);
                            var7 = var7.getClass().getMethod("getDisk").invoke(var7);
                        } catch (Exception var11) {
                            var10000 = var11;
                            var10001 = false;
                            break;
                        }

                        if (var7 != null) {
                            Class var8;
                            try {
                                var8 = var7.getClass();
                            } catch (Exception var10) {
                                var10000 = var10;
                                var10001 = false;
                                break;
                            }

                            byte var20 = 0;

                            label73: {
                                label72: {
                                    try {
                                        if ((Boolean)var8.getMethod("isSd").invoke(var7)) {
                                            break label72;
                                        }
                                    } catch (Exception var12) {
                                        var10000 = var12;
                                        var10001 = false;
                                        break;
                                    }

                                    var19 = 2;
                                    break label73;
                                }

                                var19 = 1;
                            }

                            var1 = var20;
                            if (var6 != null) {
                                try {
                                    var4.add(new UtilSystem.StorageInfo(var6.toString(), var19));
                                } catch (Exception var9) {
                                    var10000 = var9;
                                    var10001 = false;
                                    break;
                                }

                                var1 = var20;
                            }
                        }
                    }
                }

                Exception var17 = var10000;
                var17.printStackTrace();
            }

            var4.trimToSize();
            return var4;
        } else {
            int var18 = 0;
            String[] var15 = new String[]{"/storage/usbdisk1/", "/storage/usbdisk2/", "/storage/usbdisk3/", "/storage/usbdisk4/", "/storage/sdcard1/", "/storage/sdcard2/"};

            for(int var3 = var15.length; var18 < var3; ++var18) {
                String var5 = var15[var18];
                if ("mounted".equals(Environment.getExternalStorageState(new File(var5)))) {
                    var19 = 2;
                    if (var5.contains("sdcard")) {
                        var19 = 1;
                    }

                    var4.add(new UtilSystem.StorageInfo(var5, var19));
                }
            }

            return var4;
        }
    }

    public static void setStatusBarTransparent(Activity var0) {
        if (VERSION.SDK_INT >= 21) {
            Window var1 = var0.getWindow();
            var1.clearFlags(201326592);
            var1.getDecorView().setSystemUiVisibility(768);
            var1.addFlags(-2147483648);
            var1.setStatusBarColor(0);
            var1.setNavigationBarColor(0);
        }

    }

    public static class StorageInfo {
        public static final int TYPE_INTERAL = 0;
        public static final int TYPE_SD = 1;
        public static final int TYPE_USB = 2;
        public String mPath;
        public String mState;
        public int mType;

        public StorageInfo(String var1, int var2) {
            this.mPath = var1;
            this.mType = var2;
        }
    }
}
