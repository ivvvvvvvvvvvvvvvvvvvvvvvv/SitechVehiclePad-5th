//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class UtilCarKey {
    public static final String PROPERTIESFILE = "/mnt/vendor/.properties_file";
    private static final String TAG = "UtilCarKey";
    public static boolean mBtMusicInBTapk = false;
    private static String mPreActivityBeforeGps;

    static {
        if ("1".equals(MachineConfig.getPropertyReadOnly("bt_music_inside"))) {
            mBtMusicInBTapk = true;
        }

        mPreActivityBeforeGps = null;
    }

    public UtilCarKey() {
    }

    public static void doKeyAudio(Context var0) {
        if (!"com.car.ui/com.my.audio.MusicActivity".equals(AppConfig.getTopActivity())) {
            UtilSystem.doRunActivity(var0, "com.car.ui", "com.my.audio.MusicActivity");
        }

    }

    public static void doKeyAudioEx(Context var0, String var1) {
        try {
            Intent var2 = new Intent();
            var2.setClassName("com.my.audio", "com.my.audio.MusicActivity");
            var2.putExtra("song", var1);
            var2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            var0.startActivity(var2);
        } catch (Exception var3) {
            Log.e("UtilCarKey", var3.getMessage());
        }
    }

    public static void doKeyAuxIn(Context var0) {
        if (!"com.car.ui/com.my.auxplayer.AUXPlayer".equals(AppConfig.getTopActivity())) {
            UtilSystem.doRunActivity(var0, "com.car.ui", "com.my.auxplayer.AUXPlayer");
        }

    }

    public static boolean doKeyBT(Context var0) {
        if (!"com.my.bt/com.my.bt.ATBluetoothActivity".equals(AppConfig.getTopActivity())) {
            UtilSystem.doRunActivity(var0, "com.my.bt", "com.my.bt.ATBluetoothActivity");
            return true;
        } else {
            return false;
        }
    }

    public static void doKeyBTMusic(Context var0) {
        if (!mBtMusicInBTapk) {
            doKeyBTMusicAlone(var0);
        } else {
            doKeyBTMusicInBT(var0);
        }
    }

    public static void doKeyBTMusicAlone(Context var0) {
        if (!"com.car.ui/com.my.btmusic.BTMusicActivity".equals(AppConfig.getTopActivity())) {
            UtilSystem.doRunActivity(var0, "com.car.ui", "com.my.btmusic.BTMusicActivity");
        }

    }

    public static void doKeyBTMusicInBT(Context var0) {
        try {
            Intent var1 = new Intent("android.intent.action.VIEW");
            var1.setClassName("com.my.bt", "com.my.bt.ATBluetoothActivity");
            var1.putExtra("music", 1);
            var1.setFlags(272629760);
            var0.startActivity(var1);
        } catch (Exception var2) {
            Log.e("UtilCarKey", var2.getMessage());
        }
    }

    public static void doKeyCarHome(Context var0) {
        Kernel.doKeyEvent(172);
    }

    public static void doKeyDVD(Context var0) {
        if (!AppConfig.isUSBDvd()) {
            if (!"com.car.ui/com.my.dvd.DVDPlayer".equals(AppConfig.getTopActivity())) {
                UtilSystem.doRunActivity(var0, "com.car.ui", "com.my.dvd.DVDPlayer");
                return;
            }
        } else if (!AppConfig.getTopActivity().contains("com.car.dvdplayer.DVDPlayerActivity")) {
            UtilSystem.doRunActivity(var0, "com.car.dvdplayer", "com.car.dvdplayer.DVDPlayerActivity");
        }

    }

    public static void doKeyDVR(Context var0) {
        if (!"com.my.dvr".equals(AppConfig.getTopActivity())) {
            UtilSystem.doRunActivity(var0, "com.my.dvr", "com.my.dvr.MainActivity");
        }

    }

    public static void doKeyEQ(Context var0) {
        if (!"com.eqset/com.eqset.EQActivity".equals(AppConfig.getTopActivity())) {
            UtilSystem.doRunActivity(var0, "com.eqset", "com.eqset.EQActivity");
        }

    }

    public static boolean doKeyGps(Context var0) {
        return doKeyGpsEx(var0, (String)null);
    }

    public static boolean doKeyGps(Context var0, String var1) {
        return doKeyGpsEx(var0, var1);
    }

    public static boolean doKeyGpsEx(Context var0, String var1) {
        boolean var4;
        String var5;
        label77: {
            var4 = false;
            var5 = SystemConfig.getProperty(var0, "key_gps_package");
            String var6 = SystemConfig.getProperty(var0, "key_gps_class");
            if (var5 != null) {
                var1 = var6;
                if (var6 != null) {
                    break label77;
                }
            }

            var5 = MachineConfig.DEFAULT_GPS_PACKAGE;
            var1 = MachineConfig.DEFAULT_GPS_CLASS;
        }

        Intent var12;
        if (AppConfig.getTopActivity().contains(var5)) {
            boolean var3 = false;
            boolean var2 = var3;
            if (mPreActivityBeforeGps != null) {
                label81: {
                    var12 = new Intent();

                    label57: {
                        Exception var10000;
                        label56: {
                            String[] var14;
                            boolean var10001;
                            label82: {
                                try {
                                    var14 = mPreActivityBeforeGps.split("/");
                                    if (!var14[0].equals("com.anrdoid.launcher") && !var14[0].equals("com.android.launcher3")) {
                                        break label82;
                                    }
                                } catch (Exception var9) {
                                    var10000 = var9;
                                    var10001 = false;
                                    break label56;
                                }

                                try {
                                    var0.startActivity((new Intent("android.intent.action.MAIN")).addFlags(335544320).addCategory("android.intent.category.HOME"));
                                    break label57;
                                } catch (Exception var8) {
                                    var10000 = var8;
                                    var10001 = false;
                                    break label56;
                                }
                            }

                            try {
                                var12.setClassName(var14[0], var14[1]);
                                var12.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                var0.startActivity(var12);
                                break label57;
                            } catch (Exception var7) {
                                var10000 = var7;
                                var10001 = false;
                            }
                        }

                        Exception var13 = var10000;
                        var13.printStackTrace();
                        var2 = var3;
                        break label81;
                    }

                    var2 = true;
                }
            }

            if (!var2) {
                var0.startActivity((new Intent("android.intent.action.MAIN")).addFlags(335544320).addCategory("android.intent.category.HOME"));
            }

            return false;
        } else {
            mPreActivityBeforeGps = AppConfig.getTopActivity();
            Intent var15 = new Intent();

            label69: {
                try {
                    var15.setClassName(var5, var1);
                    var15.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    var0.startActivity(var15);
                } catch (Exception var11) {
                    var11.printStackTrace();
                    break label69;
                }

                var4 = true;
            }

            if (!var4) {
                try {
                    var12 = new Intent();
                    var12.setAction("com.my.factory.intent.action.GeneralSettings");
                    var12.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    var12.putExtra("navi", 1);
                    var0.startActivity(var12);
                    return var4;
                } catch (Exception var10) {
                    var10.printStackTrace();
                }
            }

            return var4;
        }
    }

    public static void doKeyMic(Context var0) {
        String var3 = MachineConfig.getProperty("bt_type");
        byte var2 = 0;
        int var1 = var2;
        if (var3 != null) {
            try {
                var1 = Integer.valueOf(var3);
            } catch (Exception var4) {
                var1 = var2;
            }
        }

        if (var1 != 1) {
            if (!isAKVoiceAssistantEnabled(var0)) {
                UtilSystem.doRunActivity(var0, "com.google.android.googlequicksearchbox", "com.google.android.googlequicksearchbox.VoiceSearchActivity");
                return;
            }

            if (!UtilSystem.doRunActivity(var0, "com.ak.speechrecog", "com.ak.speechrecog.MainActivity")) {
                UtilSystem.doRunActivity(var0, "com.google.android.googlequicksearchbox", "com.google.android.googlequicksearchbox.VoiceSearchActivity");
                return;
            }
        } else {
            UtilSystem.doRunActivity(var0, "com.my.bt", "com.my.bt.VoiceControlActivity");
        }

    }

    public static void doKeyMicEx(Context var0) {
        if (isAKVoiceAssistantEnabled(var0)) {
            if (!UtilSystem.doRunActivity(var0, "com.ak.speechrecog", "com.ak.speechrecog.MainActivity")) {
                UtilSystem.doRunActivity(var0, "com.google.android.googlequicksearchbox", "com.google.android.googlequicksearchbox.VoiceSearchActivity");
                return;
            }
        } else {
            UtilSystem.doRunActivity(var0, "com.google.android.googlequicksearchbox", "com.google.android.googlequicksearchbox.VoiceSearchActivity");
        }

    }

    public static void doKeyPhoneConnect(Context var0) {
        if (!"com.easyconn".equals(AppConfig.getTopActivity())) {
            UtilSystem.doRunActivity(var0, "net.easyconn", "net.easyconn.WelcomeActivity");
        }

    }

    public static void doKeyPic(Context var0) {
        if (!"com.my.gallery".equals(AppConfig.getTopActivity())) {
            UtilSystem.doRunActivity(var0, "com.my.gallery", "com.my.gallery.GalleryActivity");
        }

    }

    public static boolean doKeyRadio(Context var0) {
        if (!"com.car.ui/com.my.radio.RadioActivity".equals(AppConfig.getTopActivity())) {
            UtilSystem.doRunActivity(var0, "com.car.ui", "com.my.radio.RadioActivity");
            return true;
        } else {
            return false;
        }
    }

    public static void doKeySet(Context var0) {
        if (!"com.android.settings".equals(AppConfig.getTopActivity())) {
            UtilSystem.doRunActivity(var0, "com.android.settings", "com.android.settings.Settings");
        }

    }

    public static void doKeyTV(Context var0) {
        if (!"com.car.ui/com.my.tv.TVActivity".equals(AppConfig.getTopActivity())) {
            UtilSystem.doRunActivity(var0, "com.car.ui", "com.my.tv.TVActivity");
        }

    }

    public static void doKeyVideo(Context var0) {
        if (!"com.car.ui/com.my.video.VideoActivity".equals(AppConfig.getTopActivity())) {
            UtilSystem.doRunActivity(var0, "com.car.ui", "com.my.video.VideoActivity");
        }

    }

    public static boolean isAKVoiceAssistantEnabled(Context param0) {
        // $FF: Couldn't be decompiled
        return false;
    }
}
