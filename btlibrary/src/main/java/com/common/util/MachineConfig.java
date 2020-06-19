//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.util;

import android.os.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class MachineConfig {
    public static final String ANDROID_VERSION_SHOW = "android_version_show";
    public static String DEFAULT_GPS_CLASS = "none";
    public static String DEFAULT_GPS_PACKAGE = "none";
    public static final String DRIVING_SET_PASSWD = "driving_set_passwd";
    public static final String FACTROY_PASSWD = "factroy_passwd";
    public static final String KEY_1992_LIST_COLOR = "1992_list_color";
    public static final String KEY_ACC_DELAY_OFF = "key_acc_delay_off";
    public static final String KEY_APP_HIDE = "key_app_hide";
    public static final String KEY_APP_HIDE_FOREVER = "key_app_hide_forever";
    public static final String KEY_BOOTANIM_PATH = "bootanim_path";
    public static final String KEY_BT_FAVORITE = "bt_favorite";
    public static final String KEY_BT_MUSIC_INSIDE = "bt_music_inside";
    public static final String KEY_BT_NAME = "bt_name";
    public static final String KEY_BT_PASSWD = "bt_passwd";
    public static final String KEY_BT_SUPPORT_MACTALK = "bt_support_mactalk";
    public static final String KEY_BT_TYPE = "bt_type";
    public static final String KEY_BT_VOICE_BUTTON = "bt_voice_button";
    public static final String KEY_CALIBRATION = "key_calibration";
    public static final String KEY_CAMERA_TYPE = "key_camera_type";
    public static final String KEY_CAN_BOX = "key_can_box";
    public static final String KEY_CAN_BOX_EX = "key_can_box_ex";
    public static final String KEY_CAR_SETTING_TYPE = "key_car_setting_type";
    public static final String KEY_CAR_TYPE = "key_car_type";
    public static final String KEY_CUSTOMER_NAME = "key_customer_name";
    public static final String KEY_DEFAULT_GPS = "default_gps";
    public static final String KEY_DVR_FOLDER = "key_dvr_folder";
    public static final String KEY_DVR_MIRROR = "key_dvr_mirror";
    public static final String KEY_DVR_MODE = "key_dvr_mode";
    public static final String KEY_DVR_PATH = "dvr_path";
    public static final String KEY_DVR_RECORDING = "key_dvr_recording";
    public static final String KEY_DVR_TIME = "dvr_time";
    public static final String KEY_FACTORY_AUDIO_GAIN = "key_factory_audio_gain";
    public static final String KEY_FCAMERA_SHOW_USBCMAERA = "fcamera_show_usbcamera";
    public static final String KEY_FTJ_TYPE = "key_ftj";
    public static final String KEY_GPS_CLASS = "key_gps_class";
    public static final String KEY_GPS_PACKAGE = "key_gps_package";
    public static final String KEY_GPS_UID = "key_gps_uid";
    public static final String KEY_GPS_VOLUME_CHANNEL = "key_gps_volume_channel";
    public static final String KEY_HIDE_ADD_LANGUAGE = "key_hide_add_language";
    public static final String KEY_HIDE_LAUNCHER = "hide_launcher";
    public static final String KEY_HIDE_LAUNCHER_CE_BUTTON = "hide_ce_button";
    public static final String KEY_HIDE_SCREEN_SHOT = "hide_screen_shot";
    public static final String KEY_IFLY_CHANNEL = "key_ifly_channel";
    public static final String KEY_IFLY_PACKAGE = "ifly_package";
    public static final String KEY_KEYBOARD = "keyboard";
    public static final String KEY_LANGUAGE = "lang";
    public static final String KEY_LAUNCHER_CONFIG = "launcher_config";
    public static final String KEY_LAUNCHER_TRANSITION = "key_launcher_transition";
    public static final String KEY_LAUNCHER_UI = "launcher_ui";
    public static final String KEY_LED_TYPE = "key_led_type";
    public static final String KEY_LOCALES = "key_locales";
    public static final String KEY_LOGO_PATH = "logo_path";
    public static final String KEY_LOGO_PATH_K = "logo_path_kernel";
    public static final String KEY_LOG_TEST = "key_log_test";
    public static final String KEY_MCU_BEEP = "mcu_beep";
    public static final String KEY_MCU_PREFIX = "key_mcu_prefix";
    public static final String KEY_MEDIA_PATH1 = "media_path1";
    public static final String KEY_MEDIA_PATH2 = "media_path2";
    public static final String KEY_MODEL = "key_model";
    public static final String KEY_MODE_KEY_STYLE = "key_mode_key_style";
    public static final String KEY_NO_REVERSE = "key_no_reverse";
    public static final String KEY_OBD_SHOW_IN_SCREEN1 = "obd_show_in_screen1";
    public static final String KEY_OTG_TEST = "key_otg_test";
    public static final String KEY_PANEL_KEY_DEF_CONFIG = "panel_key_defcfg";
    public static final String KEY_PARAMTER_PATH = "paramter_path";
    public static final String KEY_RADIO_BUTTON_TYPE = "key_radio_button_type";
    public static final String KEY_RADIO_FREQ = "radio_freq";
    public static final String KEY_RADIO_REGION = "key_radio_region";
    public static final String KEY_RADIO_SWITCH = "radio_switch";
    public static final String KEY_RDS = "key_rds";
    public static final String KEY_RUDDER = "rudder";
    public static final String KEY_SAVE_DRIVER = "save_driver";
    public static final String KEY_SAVE_DRIVER_PACKAGE = "save_driver_package";
    public static final String KEY_SCREEN1_BT = "screen1_bt";
    public static final String KEY_SCREEN1_CANBOX_AIR = "screen1_canbox_air";
    public static final String KEY_SCREEN1_H = "aux_disp_h";
    public static final String KEY_SCREEN1_REVERSE = "screen1_reverse";
    public static final String KEY_SCREEN1_VIEW = "screen1_view";
    public static final String KEY_SCREEN1_VOLUME = "screen1_volume";
    public static final String KEY_SCREEN1_W = "aux_disp_w";
    public static final String KEY_SCREEN_H = "disp_h";
    public static final String KEY_SCREEN_W = "disp_w";
    public static final String KEY_SETTINGS_SHOW_EXT = "settings_show_ext";
    public static final String KEY_SHOW_BACK_CAMERA = "show_back_camera";
    public static final String KEY_SHOW_DARK_MODE = "show_dark_mode";
    public static final String KEY_SUB_CANBOX_AIR_CONDITION = "a";
    public static final String KEY_SUB_CANBOX_CAR_TYPE = "t";
    public static final String KEY_SUB_CANBOX_CAR_TYPE2 = "p";
    public static final String KEY_SUB_CANBOX_CHANGE_KEY = "c";
    public static final String KEY_SUB_CANBOX_EQ = "e";
    public static final String KEY_SUB_CANBOX_FRONT_DOOR = "f";
    public static final String KEY_SUB_CANBOX_KEY_TYPE = "k";
    public static final String KEY_SUB_CANBOX_OTHER = "o";
    public static final String KEY_SUB_CANBOX_REAR_DOOR = "r";
    public static final String KEY_SWC_KEY_DEF_CONFIG = "swc_key_defcfg";
    public static final String KEY_SWITCH_TO_FRONT_CAMER = "switch_to_front_camera";
    public static final String KEY_SYSTEM_UI = "system_ui";
    public static final String KEY_THIRD_APP_SOUND_FIRST = "key_3rd_app_snd_first";
    public static final String KEY_TIME12 = "time12";
    public static final String KEY_TIMEZONE = "timezone";
    public static final String KEY_TOUCH3_IDENTIFY = "key_touch3_identify";
    public static final String KEY_TOUCH_KEY_TYPE = "key_touch_key_type";
    public static final String KEY_TPMS_TYPE = "tpms_type";
    public static final String KEY_TP_CALIB = "tp_calib";
    public static final String KEY_TP_X_INV = "tp_x_inv";
    public static final String KEY_TP_Y_INV = "tp_y_inv";
    public static final String KEY_TV_TYPE = "tv_type";
    public static final String KEY_UPDATE_NAME = "key_update_name";
    public static final String KEY_USB_DVD = "key_usb_dvd";
    public static final String KEY_USER_AUDIO_GAIN = "key_user_audio_gain";
    public static final String KEY_VIDEO_ON_DRIVING = "mcu_video_on_driving";
    public static final String MCU_ILLUM_ACC_NODE = "MCU_ILLUM_ACC_NODE";
    private static final String PROJECT_CONFIG = ".config_properties";
    private static final String SYSTEM_CONFIG = "/mnt/vendor/.config_properties";
    public static final String VALUE_BOOTANIM_AUDI = "audi.zip";
    public static final String VALUE_BOOTANIM_AUDI1024 = "audi1024.zip";
    public static final String VALUE_BOOTANIM_BENZ = "benz.zip";
    public static final String VALUE_BOOTANIM_BMW = "bmw.zip";
    public static final String VALUE_BOOTANIM_CADILLAC = "cadillac.zip";
    public static final String VALUE_BOOTANIM_CITROEN = "citroen.zip";
    public static final String VALUE_BOOTANIM_CZL = "czl";
    public static final String VALUE_BOOTANIM_DONGFENG = "dongfeng.zip";
    public static final String VALUE_BOOTANIM_GM = "gm.zip";
    public static final String VALUE_BOOTANIM_INFINITI = "infiniti.zip";
    public static final String VALUE_BOOTANIM_JAGUAR = "jaguar.zip";
    public static final String VALUE_BOOTANIM_LANDLOVER = "landlover.zip";
    public static final String VALUE_BOOTANIM_OTHER = "other.zip";
    public static final String VALUE_BOOTANIM_OTHER1024 = "other1024.zip";
    public static final String VALUE_BOOTANIM_PATH = "/mnt/paramter/";
    public static final String VALUE_BOOTANIM_PEUGEOT = "peugeot.zip";
    public static final String VALUE_BOOTANIM_PORSCHE = "porsche.zip";
    public static final String VALUE_BOOTANIM_VOLVO = "volvo.zip";
    public static final String VALUE_CANBOX_ACCORD2013 = "accord2013";
    public static final String VALUE_CANBOX_ACCORD7_CHANGYUANTONG = "accord_cyt";
    public static final String VALUE_CANBOX_ACCORD_BINARYTEK = "accord_binarytek";
    public static final String VALUE_CANBOX_AUDI_SIMPLE = "audi_simple";
    public static final String VALUE_CANBOX_BENZ_B200_UNION = "benz_b200_union";
    public static final String VALUE_CANBOX_BENZ_BAGOO = "benz_bagoo";
    public static final String VALUE_CANBOX_BENZ_VITO_SIMPLE = "benz_vito_simple";
    public static final String VALUE_CANBOX_BESTURN_X80 = "besturn_x80";
    public static final String VALUE_CANBOX_BMW_E90X1_UNION = "bmw_e90x1_union";
    public static final String VALUE_CANBOX_BRAVO_UNION = "bravo_union";
    public static final String VALUE_CANBOX_CHERY_OD = "chery_od";
    public static final String VALUE_CANBOX_CHRYSLER_SIMPLE = "chrysler_simple";
    public static final String VALUE_CANBOX_DACIA_SIMPLE = "dacia_simple";
    public static final String VALUE_CANBOX_EX_HY = "ford_simple";
    public static final String VALUE_CANBOX_FIAT = "fiat_simple";
    public static final String VALUE_CANBOX_FIAT_EGEA_RAISE = "fiat_egea_raise";
    public static final String VALUE_CANBOX_FORD_EXPLORER_SIMPLE = "for_explorer_simple";
    public static final String VALUE_CANBOX_FORD_MONDEO = "ford_mondeo_simple";
    public static final String VALUE_CANBOX_FORD_RAISE = "ford_raise";
    public static final String VALUE_CANBOX_FORD_SIMPLE = "ford_simple";
    public static final String VALUE_CANBOX_GMC_SIMPLE = "gmc_simple";
    public static final String VALUE_CANBOX_GM_SIMPLE = "gm_simple";
    public static final String VALUE_CANBOX_HAFER_H2 = "hafer_h2";
    public static final String VALUE_CANBOX_HONDA_DA_SIMPLE = "honda_da_simple";
    public static final String VALUE_CANBOX_HONDA_RAISE = "honda_da_raise";
    public static final String VALUE_CANBOX_HY = "hy";
    public static final String VALUE_CANBOX_HY_RAISE = "hy_raise";
    public static final String VALUE_CANBOX_JEEP_SIMPLE = "jeep_simple";
    public static final String VALUE_CANBOX_JEEP_XINBAS = "jeep_xinbas";
    public static final String VALUE_CANBOX_KADJAR_RAISE = "kadjar_raise";
    public static final String VALUE_CANBOX_LANDROVER_HAOZHENG = "landrover_haozheng";
    public static final String VALUE_CANBOX_MAZDA = "mazda";
    public static final String VALUE_CANBOX_MAZDA3_BINARYTEK = "mazda_binarytek";
    public static final String VALUE_CANBOX_MAZDA3_SIMPLE = "mszda3_simple";
    public static final String VALUE_CANBOX_MAZDA_BT50_SIMPLE = "mazda_bt50";
    public static final String VALUE_CANBOX_MAZDA_CX5_SIMPLE = "mazda_cx5_simple";
    public static final String VALUE_CANBOX_MAZDA_XINBAS = "mazda_xinbas";
    public static final String VALUE_CANBOX_MINI_HIWORD = "mini_hiword";
    public static final String VALUE_CANBOX_MITSUBISHI_OUTLANDER_SIMPLE = "mitsubishi_outlander";
    public static final String VALUE_CANBOX_MONDEO_DAOJUN = "mondeo_daojun";
    public static final String VALUE_CANBOX_NISSAN2013 = "nissan2013";
    public static final String VALUE_CANBOX_NISSAN_BINARYTEK = "nissan_binarytek";
    public static final String VALUE_CANBOX_NISSAN_RAISE = "nissan_raise";
    public static final String VALUE_CANBOX_NONE = "none";
    public static final String VALUE_CANBOX_OBD_BINARUI = "obd_binarytek";
    public static final String VALUE_CANBOX_OPEL = "opel";
    public static final int VALUE_CANBOX_OTHER_RADAR_BEEP = 2;
    public static final int VALUE_CANBOX_OTHER_RADAR_UI = 4;
    public static final String VALUE_CANBOX_OUSHANG_RAISE = "oushang_raise";
    public static final String VALUE_CANBOX_PETGEO_RAISE = "petgeo_raise";
    public static final String VALUE_CANBOX_PETGEO_SCREEN_RAISE = "petgeo_screen_raise";
    public static final String VALUE_CANBOX_PEUGEOT206 = "peugeot206";
    public static final String VALUE_CANBOX_PEUGEOT307_UNION = "peugeot307_union";
    public static final String VALUE_CANBOX_PORSCHE_UNION = "porsche_union";
    public static final String VALUE_CANBOX_PSA = "psa_simple";
    public static final String VALUE_CANBOX_PSA206_SIMPLE = "psa_206_simple";
    public static final String VALUE_CANBOX_PSA_BAGOO = "psa_bagoo";
    public static final String VALUE_CANBOX_RAM_FIAT = "ram_fiat_simple";
    public static final String VALUE_CANBOX_RENAULT_MEGANE_FLUENCE_SMPLE = "renault_megane_fluence_imple";
    public static final String VALUE_CANBOX_RX330_HAOZHENG = "rx33_haozheng";
    public static final String VALUE_CANBOX_SMART_HAOZHENG = "smart_haozheng";
    public static final String VALUE_CANBOX_SUBARU_ODS = "subaru_ods";
    public static final String VALUE_CANBOX_TEANA_2013 = "teana_2013";
    public static final String VALUE_CANBOX_TOUAREG_HIWORLD = "touareg_hiworld";
    public static final String VALUE_CANBOX_TOYOTA = "toyota";
    public static final String VALUE_CANBOX_TOYOTA_BINARYTEK = "toyota_binarytek";
    public static final String VALUE_CANBOX_TOYOTA_LOW = "toyota_low";
    public static final String VALUE_CANBOX_VW = "vw";
    public static final String VALUE_CANBOX_VW_GOLF_SIMPLE = "golf_simple";
    public static final String VALUE_CANBOX_VW_MQB_RAISE = "vw_mqb_raise";
    public static final String VALUE_CANBOX_X30_RAISE = "x30_raise";
    public static final String VALUE_CAR_TYPE_AUDIA4L = "audiA4L";
    public static final String VALUE_CAR_TYPE_AUDIA4L1024 = "audiA4L1024";
    public static final String VALUE_CAR_TYPE_AUDIQ5 = "audiQ5";
    public static final String VALUE_CAR_TYPE_AUDIQ51024 = "audiQ51024";
    public static final String VALUE_CAR_TYPE_BENZ = "benz";
    public static final String VALUE_CAR_TYPE_BMW = "bmw";
    public static final String VALUE_CAR_TYPE_GM = "gm";
    public static final String VALUE_CUSTOMER_YX = "yx";
    public static final String VALUE_CUSTOM_NAME_CZL = "czl";
    public static final String VALUE_CUSTOM_NAME_YX = "yx";
    public static final String VALUE_LOGO_PATH = "/mnt/paramter/logo/";
    public static final String VALUE_OFF = "0";
    public static final String VALUE_ON = "1";
    public static final String VALUE_SCREEN1_VIEW_BT = "bt";
    public static final String VALUE_SCREEN1_VIEW_REVERSE = "reverse";
    public static final String VALUE_SYSTEM_UI16_7099 = "7099";
    public static final String VALUE_SYSTEM_UI19_KLD1 = "kld1";
    public static final String VALUE_SYSTEM_UI20_RM10_1 = "kld20_rm10_1";
    public static final String VALUE_SYSTEM_UI21_RM10_2 = "kld21_rm10_2";
    public static final String VALUE_SYSTEM_UI21_RM12 = "rm12";
    public static final String VALUE_SYSTEM_UI22_1050 = "kld22_1050";
    public static final String VALUE_SYSTEM_UI24_616 = "616";
    public static final String VALUE_SYSTEM_UI28_7451 = "7451";
    public static final String VALUE_SYSTEM_UI31_KLD7 = "kld7";
    public static final String VALUE_SYSTEM_UI32_KLD8 = "kld8";
    public static final String VALUE_SYSTEM_UI33_IXB = "ixb";
    public static final String VALUE_SYSTEM_UI34_KLD9 = "kld9";
    public static final String VALUE_SYSTEM_UI35_KLD813 = "kld813";
    public static final String VALUE_SYSTEM_UI35_KLD813_2 = "kld813_2";
    public static final String VALUE_SYSTEM_UI36_664 = "664";
    public static final String VALUE_SYSTEM_UI37_KLD10 = "kld10";
    public static final String VALUE_SYSTEM_UI40_KLD90 = "kld90";
    public static final String VALUE_SYSTEM_UI41_2007 = "2007";
    public static final String VALUE_SYSTEM_UI42_13 = "13";
    public static final String VALUE_SYSTEM_UI42_913 = "913";
    public static final String VALUE_SYSTEM_UI43_3300 = "3300";
    public static final String VALUE_SYSTEM_UI43_3300_1 = "3300_1";
    public static final String VALUE_SYSTEM_UI44_KLD007 = "kld007";
    public static final String VALUE_SYSTEM_UI45_8702_2 = "8702_2";
    public static final String VALUE_SYSTEM_UI_1050_2 = "1050_2";
    public static final String VALUE_SYSTEM_UI_710 = "710";
    public static final String VALUE_SYSTEM_UI_887_90 = "887_90";
    public static final String VALUE_SYSTEM_UI_927_1 = "927_1";
    public static final String VALUE_SYSTEM_UI_KLD1 = "kld20170831";
    public static final String VALUE_SYSTEM_UI_KLD10_887 = "kld10_887";
    public static final String VALUE_SYSTEM_UI_KLD11_200 = "kld11_200";
    public static final String VALUE_SYSTEM_UI_KLD12_80 = "kld12_80";
    public static final String VALUE_SYSTEM_UI_KLD15_6413 = "kld15_6413";
    public static final String VALUE_SYSTEM_UI_KLD2 = "kld20171124";
    public static final String VALUE_SYSTEM_UI_KLD3 = "kld20171125";
    public static final String VALUE_SYSTEM_UI_KLD3_8702 = "kld6_8702";
    public static final String VALUE_SYSTEM_UI_KLD5 = "kld20180131";
    public static final String VALUE_SYSTEM_UI_KLD7_1992 = "kld5_1992_20180423";
    public static final String VALUE_SYSTEM_UI_PX30_1 = "px30_1";
    public static final int VAULE_BT_TYPE_GOC = 2;
    public static final int VAULE_BT_TYPE_GOC_8761 = 3;
    public static final int VAULE_BT_TYPE_GOC_RF210 = 4;
    public static final int VAULE_BT_TYPE_IVT = 0;
    public static final int VAULE_BT_TYPE_PARROT = 1;
    public static final int VAULE_BT_TYPE_SD_816 = 5;
    public static final int VAULE_CAMERA4 = 1;
    public static final int VAULE_CAMERA_FRONT = 0;
    private static String mParamterPath = "/mnt/paramter/";
    private static Properties mPoperties = new Properties();
    private static Properties mPopertiesReadOnly;

    static {
        File var0 = new File("/mnt/paramter/+ PROJECT_CONFIG");
        String var5;
        if (var0.exists()) {
            label30: {
                Exception var10000;
                label34: {
                    boolean var10001;
                    try {
                        FileInputStream var4 = new FileInputStream(var0);
                        Properties var1 = new Properties();
                        var1.load(var4);
                        var4.close();
                        var5 = var1.getProperty("paramter_path");
                    } catch (Exception var3) {
                        var10000 = var3;
                        var10001 = false;
                        break label34;
                    }

                    if (var5 == null) {
                        break label30;
                    }

                    try {
                        mParamterPath = var5;
                        break label30;
                    } catch (Exception var2) {
                        var10000 = var2;
                        var10001 = false;
                    }
                }

                Exception var6 = var10000;
                var6.printStackTrace();
            }
        }

        var5 = getPropertyReadOnly("default_gps");
        if (var5 != null) {
            String[] var7 = var5.split("/");
            if (var7.length > 1) {
                DEFAULT_GPS_PACKAGE = var7[0];
                DEFAULT_GPS_CLASS = var7[1];
            }
        }

    }

    public MachineConfig() {
    }

    public static Properties exportProperties() {
        return (Properties)mPoperties.clone();
    }

    private static void getConfigProperties() {
        File var0 = new File("/mnt/vendor/.config_properties");
        if (var0.exists()) {
            try {
                FileInputStream var2 = new FileInputStream(var0);
                mPoperties.clear();
                mPoperties.load(var2);
                var2.close();
                return;
            } catch (Exception var1) {
                var1.printStackTrace();
            }
        }

    }

    public static int getIntProperty2(String var0) {
        try {
            int var1 = Integer.valueOf(getProperty(var0));
            return var1;
        } catch (Exception var2) {
            return -1;
        }
    }

    public static String getParamterPath() {
        return mParamterPath;
    }

    public static String getProperty(String var0) {
        initConfigProperties();
        return mPoperties.getProperty(var0);
    }

    public static int getPropertyInt(String var0) {
        try {
            int var1 = Integer.valueOf(getProperty(var0));
            return var1;
        } catch (Exception var2) {
            return 0;
        }
    }

    public static int getPropertyIntOnce(String var0) {
        try {
            int var1 = Integer.valueOf(getPropertyOnce(var0));
            return var1;
        } catch (Exception var2) {
            return 0;
        }
    }

    public static int getPropertyIntReadOnly(String var0) {
        try {
            int var1 = Integer.valueOf(getPropertyReadOnly(var0));
            return var1;
        } catch (Exception var2) {
            return 0;
        }
    }

    public static String getPropertyOnce(String var0) {
        getConfigProperties();
        return mPoperties.getProperty(var0);
    }

    public static String getPropertyReadOnly(String var0) {
        if (mPopertiesReadOnly == null) {
            StringBuilder var1 = new StringBuilder();
            var1.append(getParamterPath());
            var1.append(".config_properties");
            File var3 = new File(var1.toString());
            if (var3.exists()) {
                try {
                    FileInputStream var4 = new FileInputStream(var3);
                    if (mPopertiesReadOnly == null) {
                        mPopertiesReadOnly = new Properties();
                        mPopertiesReadOnly.clear();
                        mPopertiesReadOnly.load(var4);
                    }

                    var4.close();
                } catch (Exception var2) {
                    var2.printStackTrace();
                }
            }
        }

        return mPopertiesReadOnly != null ? mPopertiesReadOnly.getProperty(var0) : null;
    }

    public static void importProperties(Properties var0) {
        mPoperties.clear();
        mPoperties = (Properties)var0.clone();
        updateConfigProperties();
    }

    private static void initConfigProperties() {
        if (mPoperties.size() == 0) {
            getConfigProperties();
        }

    }

    public static void setIntProperty(String var0, int var1) {
        StringBuilder var2 = new StringBuilder();
        var2.append("");
        var2.append(var1);
        setProperty(var0, var2.toString());
    }

    public static Object setProperty(String var0, String var1) {
        initConfigProperties();
        Object var2;
        if (var1 == null) {
            var2 = mPoperties.remove(var0);
        } else {
            var2 = mPoperties.setProperty(var0, var1);
        }

        updateConfigProperties();
        return var2;
    }

    public static void updateConfigProperties() {
        File var0 = new File("/mnt/vendor/.config_properties");

        try {
            if (!var0.exists()) {
                var0.createNewFile();
//                FileUtils.setPermissions("/mnt/vendor/.config_properties", 511, -1, -1);
            }

//            FileUtils.setPermissions("/mnt/vendor/.config_properties", 511, -1, -1);
            Util.sudoExec("chmod:666:/mnt/vendor/.config_properties");
            FileOutputStream var2 = new FileOutputStream(var0);
            mPoperties.store(var2, "");
            var2.flush();
            var2.close();
            Util.sudoExec("sync");
        } catch (Exception var1) {
            var1.printStackTrace();
        }
    }
}
