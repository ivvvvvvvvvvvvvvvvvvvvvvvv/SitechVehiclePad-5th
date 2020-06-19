//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.util;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Iterator;

public class AppConfig {
    public static final String CARUI_BACKGROUND = "/system/etc/carui/background.png";
    public static final String CARUI_PATH = "/system/etc/carui/";
    public static final String CAR_AUXIN = "com.car.ui/com.my.auxplayer.AUXPlayer";
    public static final String CAR_BT = "com.my.bt/com.my.bt.ATBluetoothActivity";
    public static final String CAR_DTV = "com.car.ui/com.my.tv.TVActivity";
    public static final String CAR_EQ = "com.eqset/com.eqset.EQActivity";
    public static final String CAR_UI;
    public static final String CAR_UI_AUDIO = "com.car.ui/com.my.audio.MusicActivity";
    public static final String CAR_UI_AUX_IN = "com.car.ui/com.my.auxplayer.AUXPlayer";
    public static final String CAR_UI_BT_MUSIC = "com.car.ui/com.my.btmusic.BTMusicActivity";
    public static final String CAR_UI_DVD = "com.car.ui/com.my.dvd.DVDPlayer";
    public static final String CAR_UI_FRONT_CAMERA = "com.car.ui/com.my.frontcamera.FrontCameraActivity";
    public static final String CAR_UI_RADIO = "com.car.ui/com.my.radio.RadioActivity";
    public static final String CAR_UI_VIDEO = "com.car.ui/com.my.video.VideoActivity";
    public static final String HIDE_APP_AUX = "AUX";
    public static final String HIDE_APP_DVD = "DVD";
    public static final String HIDE_APP_DVR = "DVR";
    public static final String HIDE_APP_FRONT_CMAERA = "FCAM";
    public static final String HIDE_APP_JOYSTUDY = "JOYSTUDY";
    public static final String HIDE_APP_VIDEO_OUT = "VOUT";
    public static final String HIDE_APP_VIOCE_CONTROL = "VOICE";
    public static final String HIDE_APP_WHEELKEYSTUDY = "WHEELKEYSTUDY";
    public static final String HIDE_BACK_CAMERA = "BACKCAM";
    public static final String HIDE_TPMS = "TPMS";
    public static final String HIDE_USB_DVD = "USBDISC";
    public static final int[] IFLY_SOUND_STEP = new int[]{8, 9, 11, 13};
    public static final int[] MAP_SOUND_STEP = new int[]{1, 2, 3, 4};
    public static final int MEDIA_SOUND_CHANNEL = 3;
    public static final int MEDIA_SOUND_VOLUME = 15;
    public static final String PACKAGE_AUDIO = "com.my.audio";
    public static final String PACKAGE_CAR_SERVICE = "com.my.out";
    public static final String PACKAGE_CAR_UI = "com.car.ui";
    public static final String PACKAGE_EQ = "com.eqset";
    public static final String PACKAGE_LAUNCHER = "com.android.launcher";
    public static final String[] PACKAGE_NAME = new String[]{"com.car.ui", "com.eqset", "com.android.settings", "com.my.audio", "com.my.video", "com.my.setting", "com.my.bt", "net.easyconn", "com.my.out", "com.my.filemanager", "com.canboxsetting", "com.focussync"};
    public static final String[] PACKAGE_NAME_LAUNCHER_BACKGROUND;
    public static final String PACKAGE_RADIO = "com.my.radio";
    public static final String[] PACKAGE_SAVE_DRIVE = new String[]{"com.car.ui", "com.my.filemanager", "com.canboxsetting"};
    public static final String SETTINGS_KEY_DVR_RECORD_SOUND = "key_dvr_record_sound";
    public static final String SETTINGS_KEY_MEDIA_VOLUME = "key_media_volume";
    public static final String SETTINGS_KEY_SOUND_DEFAULT_STEP = "key_gps_volume";
    public static final int SOUND_DEFAULT_STEP = 2;
    private static final String TAG = "AppConfig";
    public static final String USB_DVD = "com.car.dvdplayer.DVDPlayerActivity";
    public static String mLauncherPackage = null;
    private static HashSet<String> mSetHideApp;
    private static int mUsbDvd;
    public static Drawable mWallpaperDrawable;

    static {
        CAR_UI = PACKAGE_NAME[0];
        PACKAGE_NAME_LAUNCHER_BACKGROUND = new String[]{"com.car.ui", "com.eqset", "com.my.bt", "com.my.dvr", "com.SwcApplication"};
        mSetHideApp = new HashSet();
        mUsbDvd = 0;
    }

    public AppConfig() {
    }

    private static void addHiedAppForever() {
        String var2 = MachineConfig.getPropertyReadOnly("key_app_hide_forever");
        if (var2 != null) {
            String[] var4 = var2.split(",");
            int var1 = var4.length;

            for(int var0 = 0; var0 < var1; ++var0) {
                String var3 = var4[var0];
                mSetHideApp.add(var3);
            }
        }

    }

    public static String getCanboxSetting() {
        String var1 = MachineConfig.getPropertyOnce("key_can_box");
        String var0 = var1;
        if (var1 != null) {
            var0 = var1.split(",")[0];
        }

        return var0;
    }

    public static String getLauncherPackage() {
        if (mLauncherPackage == null) {
            String var0 = MachineConfig.getPropertyReadOnly("hide_launcher");
            if (var0 != null && var0.contains("Launcher2")) {
                mLauncherPackage = "com.android.launcher3";
            } else {
                mLauncherPackage = "com.android.launcher";
            }
        }

        return mLauncherPackage;
    }

    public static String getTopActivity() {
        String var1 = "";
        String var0;
        StringBuilder var2;
        if (Util.isAndroidQ()) {
            try {
                var0 = "";//SystemProperties.get("ak.status.public_top_activity");
            } catch (Exception var4) {
                var2 = new StringBuilder();
                var2.append("getTopActivity exctption: ");
                var2.append(var4);
                Log.e("AppConfig", var2.toString());
                var0 = var1;
            }
        } else {
            var0 = var1;

            label72: {
                Exception var10000;
                label66: {
                    FileReader var11;
                    boolean var10001;
                    try {
                        var11 = new FileReader("/sys/class/ak/mcu/public_top_activity");
                    } catch (Exception var9) {
                        var10000 = var9;
                        var10001 = false;
                        break label66;
                    }

                    var0 = var1;

                    BufferedReader var3;
                    try {
                        var3 = new BufferedReader(var11, 256);
                    } catch (Exception var8) {
                        var10000 = var8;
                        var10001 = false;
                        break label66;
                    }

                    var0 = var1;

                    try {
                        var1 = var3.readLine();
                    } catch (Exception var7) {
                        var10000 = var7;
                        var10001 = false;
                        break label66;
                    }

                    var0 = var1;

                    try {
                        var3.close();
                    } catch (Exception var6) {
                        var10000 = var6;
                        var10001 = false;
                        break label66;
                    }

                    var0 = var1;

                    try {
                        var11.close();
                        break label72;
                    } catch (Exception var5) {
                        var10000 = var5;
                        var10001 = false;
                    }
                }

                Exception var10 = var10000;
                var2 = new StringBuilder();
                var2.append("getTopActivity exctption: ");
                var2.append(var10);
                Log.e("AppConfig", var2.toString());
                return var0 == null ? "" : var0;
            }

            var0 = var1;
        }

        return var0 == null ? "" : var0;
    }

    public static boolean isDVRApp(String var0) {
        return var0.startsWith(PACKAGE_NAME[1]);
    }

    public static boolean isGpsApp(Context var0, String var1) {
        String var2 = SystemConfig.getProperty(var0, "key_gps_package");
        String var3 = var2;
        if (var2 == null) {
            var3 = MachineConfig.DEFAULT_GPS_PACKAGE;
        }

        return var1 != null && var1.startsWith(var3);
    }

    public static boolean isHidePackage(String var0) {
        Iterator var1 = mSetHideApp.iterator();

        do {
            if (!var1.hasNext()) {
                return false;
            }
        } while(!var0.equalsIgnoreCase((String)var1.next()));

        return true;
    }

    public static boolean isHomeApp(String var0) {
        return var0.startsWith(PACKAGE_NAME[0]);
    }

    public static boolean isMusicApp(String var0) {
        return var0.startsWith(PACKAGE_NAME[2]);
    }

    public static boolean isNoNeedLauncherBackground(String var0) {
        String[] var3 = PACKAGE_NAME_LAUNCHER_BACKGROUND;
        int var2 = var3.length;

        for(int var1 = 0; var1 < var2; ++var1) {
            if (var0.startsWith(var3[var1])) {
                return true;
            }
        }

        return false;
    }

    public static boolean isNoNeedLauncherBackground(String var0, String var1) {
        String[] var4 = PACKAGE_NAME_LAUNCHER_BACKGROUND;
        int var3 = var4.length;

        for(int var2 = 0; var2 < var3; ++var2) {
            if (var0.startsWith(var4[var2])) {
                if (var0.startsWith("com.my.bt")) {
                    if (var1.startsWith("com.my.bt.VoiceControlActivity")) {
                        return false;
                    }

                    return true;
                }

                return true;
            }
        }

        return false;
    }

    public static boolean isPackageSaveSpecApp(String var0) {
        String[] var3 = PACKAGE_SAVE_DRIVE;
        int var2 = var3.length;

        for(int var1 = 0; var1 < var2; ++var1) {
            if (var0.startsWith(var3[var1])) {
                return true;
            }
        }

        return false;
    }

    public static boolean isSystemApp(String var0) {
        String[] var3 = PACKAGE_NAME;
        int var2 = var3.length;

        for(int var1 = 0; var1 < var2; ++var1) {
            if (var0.startsWith(var3[var1])) {
                return true;
            }
        }

        return false;
    }

    public static boolean isUSBDvd() {
        boolean var2 = false;
        mUsbDvd = MachineConfig.getPropertyInt("key_usb_dvd");
        boolean var1 = var2;
        if (mUsbDvd == 1) {
            boolean var0 = false;
            Iterator var3 = mSetHideApp.iterator();

            while(var3.hasNext()) {
                if ("com.car.ui/com.my.dvd.DVDPlayer".equalsIgnoreCase((String)var3.next())) {
                    var0 = true;
                }
            }

            var1 = var2;
            if (!var0) {
                var1 = true;
            }
        }

        return var1;
    }

    public static boolean isVideoApp(String var0) {
        return var0.startsWith(PACKAGE_NAME[3]);
    }

    public static void updateHideAppConfig() {
        mSetHideApp.clear();
        boolean var5 = false;
        boolean var0 = false;
        boolean var6 = false;
        boolean var1 = false;
        mUsbDvd = MachineConfig.getPropertyIntOnce("key_usb_dvd");
        if (mUsbDvd != 1) {
            mSetHideApp.add("com.car.dvdplayer.DVDPlayerActivity");
        }

        String var18 = MachineConfig.getPropertyOnce("key_app_hide");
        if (var18 != null) {
            if (var18.contains("DTV")) {
                mSetHideApp.add("com.my.tv.TVActivity");
            }

            if (var18.contains("AUX")) {
                var0 = true;
            }

            if (var18.contains("BT")) {
                mSetHideApp.add("com.my.bt.ATBluetoothActivity");
            }

            if (var18.contains("DVD")) {
                mSetHideApp.add("com.my.dvd.DVDPlayer");
            }

            if (var18.contains("FCAM")) {
                var1 = true;
            }

            if (var18.contains("VOUT")) {
                mSetHideApp.add("com.my.videoout.VideoOutActivity");
            }

            if (var18.contains("DVR")) {
                mSetHideApp.add("com.my.dvr.MainActivity");
            }

            if (var18.contains("JOYSTUDY")) {
                mSetHideApp.add("com.SwcApplication.JoyActivity");
            }

            if (var18.contains("WHEELKEYSTUDY")) {
                mSetHideApp.add("com.SwcApplication.SwcActivity");
            }

            var5 = var0;
            var6 = var1;
            if (var18.contains("TPMS")) {
                mSetHideApp.add("com.ak.tpms.ActivityMain");
                var5 = var0;
                var6 = var1;
            }
        } else {
            mSetHideApp.add("com.my.tv.TVActivity");
            mSetHideApp.add("com.SwcApplication.JoyActivity");
        }

        String var19 = MachineConfig.getProperty("bt_type");
        byte var24 = 0;
        int var23 = var24;
        if (var19 != null) {
            try {
                var23 = Integer.valueOf(var19);
            } catch (Exception var22) {
                var23 = var24;
            }
        }

        if (var23 != 1 || var18 != null && var18.contains("VOICE")) {
            mSetHideApp.add("com.my.bt.VoiceControlActivity");
        }

        boolean var11 = true;
        boolean var7 = true;
        boolean var14 = true;
        boolean var16 = true;
        boolean var12 = true;
        boolean var17 = true;
        boolean var13 = true;
        boolean var15 = true;
        var19 = MachineConfig.getPropertyOnce("key_can_box");
        int var10 = 0;
        var18 = var19;
        if (var19 != null) {
            String[] var27 = var19.split(",");
            var18 = var27[0];
            var23 = 0;

            int var2;
            for(int var25 = 1; var25 < var27.length; var23 = var2) {
                var2 = var23;
                if (var27[var25].startsWith("t")) {
                    try {
                        var2 = Integer.valueOf(var27[var25].substring(1));
                    } catch (Exception var21) {
                        var2 = var23;
                    }
                }

                ++var25;
            }

            var10 = var23;
        }

        boolean var4 = var5;
        boolean var8 = var6;
        boolean var9 = var11;
        var0 = var7;
        var1 = var14;
        boolean var3 = var12;
        boolean var26 = var13;
        if (var18 != null) {
            if (!var18.equals("ford_simple") && !var18.equals("ford_raise") && !var18.equals("for_explorer_simple")) {
                if (!var18.equals("gm_simple") && !var18.equals("honda_da_simple") && !var18.equals("honda_da_raise") && !var18.equals("psa_bagoo") && !var18.equals("bmw_e90x1_union") && !var18.equals("psa_simple") && !var18.equals("kadjar_raise") && !var18.equals("toyota") && !var18.equals("toyota_binarytek") && !var18.equals("accord_binarytek") && !var18.equals("accord2013") && !var18.equals("touareg_hiworld") && !var18.equals("petgeo_raise") && !var18.equals("petgeo_screen_raise") && !var18.equals("landrover_haozheng") && !var18.equals("oushang_raise")) {
                    if (!var18.equals("golf_simple") && !var18.equals("vw_mqb_raise")) {
                        if (!var18.equals("vw") && !var18.equals("peugeot206") && !var18.equals("porsche_union") && !var18.equals("obd_binarytek") && !var18.equals("psa_206_simple") && !var18.equals("mondeo_daojun") && !var18.equals("fiat_egea_raise")) {
                            if (!var18.equals("opel") && !var18.equals("ram_fiat_simple") && !var18.equals("mitsubishi_outlander") && !var18.equals("chery_od") && !var18.equals("nissan2013") && !var18.equals("hafer_h2") && !var18.equals("smart_haozheng") && !var18.equals("mazda_cx5_simple")) {
                                if (var18.equals("jeep_simple")) {
                                    label226: {
                                        if (var10 != 0 && var10 != 1 && var10 != 4 && var10 != 6) {
                                            var3 = var17;
                                            if (var10 != 7) {
                                                break label226;
                                            }
                                        }

                                        var3 = false;
                                    }

                                    var1 = false;
                                    var0 = false;
                                    var26 = false;
                                    var4 = var5;
                                    var8 = var6;
                                    var9 = var11;
                                } else if (var18.equals("mazda_binarytek")) {
                                    var1 = false;
                                    var0 = false;
                                    var26 = false;
                                    var4 = var5;
                                    var8 = var6;
                                    var9 = var11;
                                    var3 = var12;
                                } else if (var18.equals("mazda_xinbas")) {
                                    var7 = false;
                                    var14 = false;
                                    var4 = var5;
                                    var8 = var6;
                                    var9 = var11;
                                    var0 = var14;
                                    var1 = var7;
                                    var3 = var12;
                                    var26 = var13;
                                    if (var10 != 1) {
                                        var4 = true;
                                        var26 = false;
                                        var8 = var6;
                                        var9 = var11;
                                        var0 = var14;
                                        var1 = var7;
                                        var3 = var12;
                                    }
                                } else if (var18.equals("mszda3_simple")) {
                                    var4 = var5;
                                    var26 = var15;
                                    if (var10 != 1) {
                                        var4 = true;
                                        var26 = false;
                                    }

                                    var0 = false;
                                    var1 = false;
                                    var8 = var6;
                                    var9 = var11;
                                    var3 = var12;
                                } else if (var18.equals("hafer_h2")) {
                                    var8 = true;
                                    var4 = var5;
                                    var9 = var11;
                                    var0 = var7;
                                    var1 = var14;
                                    var3 = var12;
                                    var26 = var13;
                                } else if (var18.equals("rx33_haozheng")) {
                                    var1 = false;
                                    var26 = false;
                                    var4 = true;
                                    var3 = false;
                                    var8 = var6;
                                    var9 = var11;
                                    var0 = var7;
                                } else if (var18.equals("x30_raise")) {
                                    label213: {
                                        if (var10 != 1) {
                                            var4 = var5;
                                            var8 = var6;
                                            var9 = var11;
                                            var0 = var7;
                                            var1 = var14;
                                            var3 = var12;
                                            var26 = var13;
                                            if (var10 != 2) {
                                                break label213;
                                            }
                                        }

                                        var3 = false;
                                        var4 = var5;
                                        var8 = var6;
                                        var9 = var11;
                                        var0 = var7;
                                        var1 = var14;
                                        var26 = var13;
                                    }
                                } else {
                                    var4 = var5;
                                    var8 = var6;
                                    var9 = var11;
                                    var0 = var7;
                                    var1 = var14;
                                    var3 = var12;
                                    var26 = var13;
                                    if (var18.equals("jeep_xinbas")) {
                                        var0 = false;
                                        var26 = false;
                                        var3 = false;
                                        var4 = var5;
                                        var8 = var6;
                                        var9 = var11;
                                        var1 = var14;
                                    }
                                }
                            } else {
                                var0 = false;
                                var4 = var5;
                                var8 = var6;
                                var9 = var11;
                                var1 = var14;
                                var3 = var12;
                                var26 = var13;
                            }
                        } else {
                            var1 = false;
                            var4 = var5;
                            var8 = var6;
                            var9 = var11;
                            var0 = var7;
                            var3 = var12;
                            var26 = var13;
                        }
                    } else {
                        var0 = false;
                        var1 = false;
                        var3 = false;
                        var4 = var5;
                        var8 = var6;
                        var9 = var11;
                        var26 = var13;
                    }
                } else {
                    var0 = false;
                    var1 = false;
                    var4 = var5;
                    var8 = var6;
                    var9 = var11;
                    var3 = var12;
                    var26 = var13;
                }
            } else {
                label208: {
                    if (!var18.equals("ford_simple")) {
                        var7 = var16;
                        if (!var18.equals("ford_raise")) {
                            break label208;
                        }
                    }

                    var7 = false;
                }

                var14 = false;
                var4 = var5;
                var8 = var6;
                var9 = var11;
                var0 = var14;
                var1 = var7;
                var3 = var12;
                var26 = var13;
                if (1 != var10) {
                    label203: {
                        var11 = false;
                        if (var10 != 2) {
                            var4 = var5;
                            var8 = var6;
                            var9 = var11;
                            var0 = var14;
                            var1 = var7;
                            var3 = var12;
                            var26 = var13;
                            if (var10 != 3) {
                                break label203;
                            }
                        }

                        var4 = true;
                        var26 = var13;
                        var3 = var12;
                        var1 = var7;
                        var0 = var14;
                        var9 = var11;
                        var8 = var6;
                    }
                }
            }
        }

        if (var4) {
            mSetHideApp.add("com.my.auxplayer.AUXPlayer");
        }

        if (var9) {
            mSetHideApp.add("com.focussync.MainActivity");
        }

        if (var0) {
            mSetHideApp.add("com.canboxsetting.MainActivity");
        }

        if (var1) {
            mSetHideApp.add("com.canboxsetting.CarInfoActivity");
        }

        if (var3) {
            mSetHideApp.add("com.canboxsetting.CanAirControlActivity");
        }

        if (var26) {
            mSetHideApp.add("com.canboxsetting.JeepCarCDPlayerActivity");
        }

        if (!var8) {
            var18 = MachineConfig.getPropertyOnce("key_camera_type");
            if (var18 != null) {
                if (var18.equals("0")) {
                    mSetHideApp.add("com.my.frontcamera.FrontCameraActivity4");
                } else if (var18.equals("1")) {
                    mSetHideApp.add("com.my.frontcamera.FrontCameraActivity");
                }
            } else {
                mSetHideApp.add("com.my.frontcamera.FrontCameraActivity4");
            }
        } else {
            mSetHideApp.add("com.my.frontcamera.FrontCameraActivity");
            mSetHideApp.add("com.my.frontcamera.FrontCameraActivity4");
        }

        if (MachineConfig.getPropertyIntReadOnly("show_back_camera") != 1) {
            mSetHideApp.add("com.my.frontcamera.BackCameraActivity");
        }

        addHiedAppForever();
    }

    public static void updateSystemBackground(Context var0, View var1) {
        Drawable var2 = WallpaperManager.getInstance(var0).getDrawable();
        if (var2 != null && mWallpaperDrawable != var2) {
            mWallpaperDrawable = var2;
            var1.setBackground(var2);
        }

    }
}
