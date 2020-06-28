//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class PublicData {
    public static final String KEY_TOP_ACTIVITY = "key_top_activity";
    private static final String PROJECT_CONFIG = ".public_data";
    private static final String SYSTEM_CONFIG = "/mnt/vendor/.public_data";
    private static Properties mPoperties = new Properties();

    public PublicData() {
    }

    private static void getConfigProperties() {
        File var0 = new File("/mnt/vendor/.public_data");
        if (var0.exists()) {
            try {
                FileInputStream var2 = new FileInputStream(var0);
                mPoperties.load(var2);
                var2.close();
                return;
            } catch (Exception var1) {
                var1.printStackTrace();
            }
        }

    }

    public static String getProperty(String var0) {
        initConfigProperties();
        return mPoperties.getProperty(var0);
    }

    private static void initConfigProperties() {
        getConfigProperties();
    }

    public static Object setProperty(String var0, String var1) {
        initConfigProperties();
        Object var2 = mPoperties.setProperty(var0, var1);
        updateConfigProperties();
        return var2;
    }

    public static void updateConfigProperties() {
        File var0 = new File("/mnt/vendor/.public_data");

        try {
            if (!var0.exists()) {
                var0.createNewFile();
//                FileUtils.setPermissions("/mnt/vendor/.public_data", 511, -1, -1);
            }

//            FileUtils.setPermissions("/mnt/vendor/.public_data", 511, -1, -1);
            FileOutputStream var2 = new FileOutputStream(var0);
            mPoperties.store(var2, "");
            var2.close();
        } catch (Exception var1) {
            var1.printStackTrace();
        }
    }
}
