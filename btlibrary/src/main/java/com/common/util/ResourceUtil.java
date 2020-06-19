//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;

public class ResourceUtil {
    public ResourceUtil() {
    }

    public static int getColorId(Context var0, String var1) {
        return var0.getResources().getIdentifier(var1, "color", var0.getPackageName());
    }

    public static int getDrawableId(Context var0, String var1) {
        return var0.getResources().getIdentifier(var1, "drawable", var0.getPackageName());
    }

    public static int getId(Context var0, String var1) {
        return var0.getResources().getIdentifier(var1, "id", var0.getPackageName());
    }

    public static int getLayoutId(Context var0, String var1) {
        return var0.getResources().getIdentifier(var1, "layout", var0.getPackageName());
    }

    public static int getStringId(Context var0, String var1) {
        return var0.getResources().getIdentifier(var1, "string", var0.getPackageName());
    }

    public static int getStyleId(Context var0, String var1) {
        return var0.getResources().getIdentifier(var1, "style", var0.getPackageName());
    }

    public static void setActivityAnim(Activity var0, int var1) {
        View var2 = ((ViewGroup)var0.findViewById(16908290)).getChildAt(0);
        AnimationSet var3 = (AnimationSet)AnimationUtils.loadAnimation(var0, var1);
        var2.clearAnimation();
        var2.setAnimation(var3);
        var3.start();
    }

    public static void updateAppUi(Context var0) {
        String var5 = MachineConfig.getPropertyReadOnly("system_ui");
        byte var3 = 0;
        byte var4 = 0;
        DisplayMetrics var6 = var0.getResources().getDisplayMetrics();
        short var1;
        byte var2;
        if (var6.widthPixels == 1024 && var6.heightPixels == 600) {
            var2 = 1;
            var1 = 321;
        } else {
            var1 = var3;
            var2 = var4;
            if (var6.widthPixels == 1280) {
                var1 = var3;
                var2 = var4;
                if (var6.heightPixels == 480) {
                    var2 = 2;
                    var1 = var3;
                }
            }
        }

        if ("kld20171125".equals(var5)) {
            if (var2 == 0) {
                var1 = 326;
            } else {
                var1 = 327;
            }
        } else if ("kld6_8702".equals(var5)) {
            if (var2 == 0) {
                var1 = 332;
            } else {
                var1 = 333;
            }
        } else if ("kld10_887".equals(var5)) {
            if (var2 == 0) {
                var1 = 334;
            } else if (var2 == 2) {
                var1 = 335;
            } else {
                var1 = 336;
            }
        }

        Configuration var7 = var0.getResources().getConfiguration();
        if (var1 != 0) {
            var7.smallestScreenWidthDp = var1;
        }

        if (false) {
            var7.screenWidthDp = 0;
        }

        if (false) {
            var7.screenHeightDp = 0;
        }

        var0.getResources().updateConfiguration(var7, (DisplayMetrics)null);
    }

    public static String updateSingleUi(Context var0) {
        String var3 = MachineConfig.getPropertyReadOnly("system_ui");
        byte var2 = 0;
        DisplayMetrics var4 = var0.getResources().getDisplayMetrics();
        short var1 = var2;
        if (var4.widthPixels == 1024) {
            var1 = var2;
            if (var4.heightPixels == 600) {
                var1 = 321;
            }
        }

        Configuration var5 = var0.getResources().getConfiguration();
        if (var1 != 0) {
            var5.smallestScreenWidthDp = var1;
        }

        var0.getResources().updateConfiguration(var5, (DisplayMetrics)null);
        return var3;
    }

    public static String updateUi(Context var0) {
        String var5 = MachineConfig.getPropertyReadOnly("system_ui");
        byte var3 = 0;
        byte var4 = 0;
        DisplayMetrics var6 = var0.getResources().getDisplayMetrics();
        short var1;
        byte var2;
        if (var6.widthPixels == 1024 && var6.heightPixels == 600) {
            var2 = 1;
            var1 = 321;
        } else {
            var1 = var3;
            var2 = var4;
            if (var6.widthPixels == 1280) {
                var1 = var3;
                var2 = var4;
                if (var6.heightPixels == 480) {
                    var2 = 2;
                    var1 = var3;
                }
            }
        }

        if ("kld20170831".equals(var5)) {
            if (var2 == 0) {
                var1 = 322;
            } else {
                var1 = 323;
            }
        } else if ("kld20171124".equals(var5)) {
            if (var2 == 0) {
                var1 = 324;
            } else {
                var1 = 325;
            }
        } else if ("kld20171125".equals(var5)) {
            if (var2 == 0) {
                var1 = 326;
            } else {
                var1 = 327;
            }
        } else if ("kld20180131".equals(var5)) {
            if (var2 == 0) {
                var1 = 328;
            } else {
                var1 = 329;
            }
        } else if ("kld5_1992_20180423".equals(var5)) {
            if (var2 == 0) {
                var1 = 330;
            } else {
                var1 = 331;
            }
        } else if ("kld6_8702".equals(var5)) {
            if (var2 == 0) {
                var1 = 332;
            } else {
                var1 = 333;
            }
        } else if ("kld10_887".equals(var5)) {
            if (var2 == 0) {
                var1 = 334;
            } else if (var2 == 2) {
                var1 = 335;
            } else {
                var1 = 336;
            }
        } else if ("kld12_80".equals(var5)) {
            if (var2 == 0) {
                var1 = 340;
            } else {
                var1 = 341;
            }
        }

        Configuration var7 = var0.getResources().getConfiguration();
        if (var1 != 0) {
            var7.smallestScreenWidthDp = var1;
        }

        if (false) {
            var7.screenWidthDp = 0;
        }

        if (false) {
            var7.screenHeightDp = 0;
        }

        var0.getResources().updateConfiguration(var7, (DisplayMetrics)null);
        return var5;
    }
}
