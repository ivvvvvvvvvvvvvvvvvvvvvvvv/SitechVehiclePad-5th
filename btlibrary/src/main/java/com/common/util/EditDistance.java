//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.util;

import java.lang.reflect.Array;

public class EditDistance {
    public EditDistance() {
    }

    public static int calculteEditDistance(String var0, String var1) {
        int var5 = var0.length() + 1;
        int var6 = var1.length() + 1;
        int[][] var10 = (int[][])Array.newInstance(Integer.TYPE, new int[]{var6, var5});

        int var2;
        for(var2 = 1; var2 < var5; var10[0][var2] = var2++) {
        }

        for(var2 = 1; var2 < var6; var10[var2][0] = var2++) {
        }

        for(var2 = 1; var2 < var5; ++var2) {
            for(int var3 = 1; var3 < var6; ++var3) {
                int var7 = var10[var3 - 1][var2];
                int var8 = var10[var3][var2 - 1];
                int var9 = var10[var3 - 1][var2 - 1];
                byte var4;
                if (var0.charAt(var2 - 1) == var1.charAt(var3 - 1)) {
                    var4 = 0;
                } else {
                    var4 = 1;
                }

                var10[var3][var2] = min(var7 + 1, var8 + 1, var9 + var4);
            }
        }

        return var10[var6 - 1][var5 - 1];
    }

    public static double getSimilarity(String var0, String var1) {
        if (var0 != null && var1 != null) {
            int var3 = calculteEditDistance(var0, var1);
            int var2;
            if (var0.length() > var1.length()) {
                var2 = var0.length();
            } else {
                var2 = var1.length();
            }

            return (double)(var2 - var3) / (double)var2;
        } else {
            return 1.7976931348623157E308D;
        }
    }

    public static int min(int var0, int var1, int var2) {
        int var3 = var0;
        var0 = var0;
        if (var3 > var1) {
            var0 = var1;
        }

        var1 = var0;
        if (var0 > var2) {
            var1 = var2;
        }

        return var1;
    }
}
