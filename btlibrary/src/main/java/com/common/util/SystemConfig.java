//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.util;

import android.content.Context;
import android.os.Build.VERSION;
import android.provider.Settings.Global;
import android.provider.Settings.System;

public class SystemConfig {
    public static final String AUTO_PLAY_MUSIC_DEVICES_MOUNTED = "AUTO_PLAY_MUSIC_DEVICES_MOUNTED";
    public static final String CANBOX_DOOR_VOICE = "canbox_door_voice";
    public static final String CANBOX_EQ_VOLUME = "CANBOX_EQ_VOLUME";
    public static final String CANBOX_FRONT_RADAR_OPEN_CAMERA = "canbox_front_radar_open_camera";
    public static final String CANBOX_MILEAGE_UNIT = "CANBOX_MILEAGE_UNIT";
    public static final String CANBOX_TEMP_UNIT = "CANBOX_TEMP_UNIT";
    public static final String GPS_AUTO_UPDATE_TIME = "gps_auto_update_time";
    public static final String GPS_BRAKE = "gps_brake";
    public static final String KEY_ACC_DELAY_OFF = "key_acc_delay_off";
    public static final String KEY_BT_CELL = "key_btcell";
    public static final String KEY_CANBOX_EQ = "key_canbox_eq";
    public static final String KEY_CAR_CELL = "key_carcell";
    public static final String KEY_CE_STYLE = "key_ce_style";
    public static final String KEY_CE_STYLE_WALLPAPER_NAME = "key_ce_style_wallpaper";
    public static final String KEY_CUSTOM_APP = "KEY_CUSTOM_APP";
    public static final String KEY_DARK_MODE_SWITCH = "car_dark_mode_switch";
    public static final String KEY_DATE_FORMAT = "key_date_format";
    public static final String KEY_DEFAULT_RESET_VOLUME_LEVEL = "key_default_reset_volume_level";
    public static final String KEY_DSP = "ak_dsp";
    public static final String KEY_DSP_CUSTOM = "dsp_custom";
    public static final String KEY_DSP_EQ_MODE = "dsp_eq_mode";
    public static final String KEY_DSP_SCREEN_SAVER = "ak_dsp_screen_saver";
    public static final String KEY_DSP_SCREEN_SAVER_TYPE = "ak_dsp_screen_saver_type";
    public static final String KEY_DSP_ZONE = "dsp_zone";
    public static final String KEY_DVR_ACTITUL_RECORDING = "key_dvr_actitul_recording";
    public static final String KEY_DVR_FOLDER = "key_dvr_folder";
    public static final String KEY_DVR_MODE = "key_dvr_mode";
    public static final String KEY_DVR_PATH = "dvr_path";
    public static final String KEY_DVR_RECORDING = "key_dvr_recording";
    public static final String KEY_DVR_TIME = "dvr_time";
    public static final String KEY_ENABLE_AK_VIOCE_ASSISTANT = "key_enable_ak_voice_assistant";
    public static final String KEY_EQ_INDEPEND = "key_eq_independ";
    public static final String KEY_EQ_INDEPEND_SWITCH = "key_eq_independ_swtich";
    public static final String KEY_LAUNCHER_UI_RM10 = "key_launcher_ui_rm10";
    public static final String KEY_LAUNCHER_UI_RM10_WORKSPACE_RELOAD = "key_launcher_ui_workspace_reload";
    public static final String KEY_NAVI_MIX_SOUND = "key_navi_mix_sound";
    public static final String KEY_NISSIAN_360_SYSTEM = "key_nissian_360_system";
    public static final String KEY_NISSIAN_360_SYSTEM_BUTTON = "key_nissian_360_system_button";
    public static final String KEY_REVERSE_BACKLIGHT = "key_reverse_backlight";
    public static final String KEY_REVERSE_CONTRAST = "key_reverse_contrast";
    public static final String KEY_REVERSE_VOLUME = "key_reverse_volume";
    public static final String KEY_SCREEN0_WALLPAPER_NAME = "key_screen0_wallpaper";
    public static final String KEY_SCREEN1_BACKLIGHT = "key_screen1_backlight";
    public static final String KEY_SCREEN1_SCREENSAVER_TYPE = "key_screen1_screensaver_type";
    public static final String KEY_SCREEN1_WALLPAPER_NAME = "key_screen1_wallpaper";
    public static final String KEY_SCREEN_SAVE_STYLE = "key_screen_save_style";
    public static final String MEDIA_INFO_TOAST_BACKGROUND = "media_info_toast_background";
    public static final String MIRROR_PREVIEW = "mirror_preview";
    public static final String PATH_CE_WALLPAPER = "/sdcard/.wallpaper/";
    public static final String PATH_DARK_MODE_WALLPAPER = "dark_mode.jpg";
    public static final String PATH_DEFAULT_CE_WALLPAPER = "default_ce_screen.jpg";
    public static final String PATH_DEFAULT_WALLPAPER0 = "default_screen0.jpg";
    public static final String PATH_DEFAULT_WALLPAPER1 = "default_screen1.jpg";
    public static final String PATH_WALLPAPER = "/mnt/paramter/wallpaper/";
    public static final String REVERSE_DYNC_TRACK = "REVERSE_DYNC_TRACK";
    public static final String REVERSE_RADAR = "REVERSE_RADAR";
    public static final String REVERSE_STATIC_TRACK = "REVERSE_STATIC_TRACK";
    public static final String SHOW_FOCUS_CAR_WARNING_MSG = "SHOW_FOCUS_CAR_WARNING_MSG";

    public SystemConfig() {
    }

    public static int getIntProperty(Context var0, String var1) {
        try {
            int var2;
            if (VERSION.SDK_INT >= 22) {
                var2 = Global.getInt(var0.getContentResolver(), var1);
            } else {
                var2 = System.getInt(var0.getContentResolver(), var1);
            }

            return var2;
        } catch (Exception var3) {
            return 0;
        }
    }

    public static int getIntProperty2(Context var0, String var1) {
        try {
            int var2;
            if (VERSION.SDK_INT >= 22) {
                var2 = Global.getInt(var0.getContentResolver(), var1);
            } else {
                var2 = System.getInt(var0.getContentResolver(), var1);
            }

            return var2;
        } catch (Exception var3) {
            return -1;
        }
    }

    public static String getProperty(Context var0, String var1) {
        try {
            String var3;
            if (VERSION.SDK_INT >= 22) {
                var3 = Global.getString(var0.getContentResolver(), var1);
            } else {
                var3 = System.getString(var0.getContentResolver(), var1);
            }

            return var3;
        } catch (Exception var2) {
            return null;
        }
    }

    public static boolean setIntProperty(Context var0, String var1, int var2) {
        try {
            boolean var3;
            if (VERSION.SDK_INT >= 22) {
                var3 = Global.putInt(var0.getContentResolver(), var1, var2);
            } else {
                var3 = System.putInt(var0.getContentResolver(), var1, var2);
            }

            return var3;
        } catch (Exception var4) {
            return false;
        }
    }

    public static boolean setProperty(Context var0, String var1, String var2) {
        boolean var3;
        try {
            if (VERSION.SDK_INT >= 22) {
                var3 = Global.putString(var0.getContentResolver(), var1, var2);
            } else {
                var3 = System.putString(var0.getContentResolver(), var1, var2);
            }
        } catch (Exception var4) {
            var3 = false;
        }

        Util.sudoExecNoCheck("sync");
        return var3;
    }
}
