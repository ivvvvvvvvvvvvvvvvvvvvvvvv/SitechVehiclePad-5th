package com.sitechdev.vehicle.pad.module.phone;

/**
 * 蓝牙相关常量
 *
 * @author liuhe
 * @date 2019/06/24
 */
public class BtConstants {
    /**
     * DialPad
     **/
    public final static char DP_ONE = '1';
    public final static char DP_TWO = '2';
    public final static char DP_THREE = '3';
    public final static char DP_FOUR = '4';
    public final static char DP_FIVE = '5';
    public final static char DP_SIX = '6';
    public final static char DP_SEVEN = '7';
    public final static char DP_EIGHT = '8';
    public final static char DP_NINE = '9';
    public final static char DP_STAR = '*';
    public final static char DP_ZERO = '0';
    public final static char DP_PLUS = '+';
    public final static char DP_HASH = '#';

    /**
     * Bluetooth Init Result
     **/
    public final static int INIT_FALED = 0;
    public final static int INIT_SUCCEED = 1;

    /**
     * Bluetooth Info
     **/
    public final static int BTINFO_VER = 0;
    public final static int BTINFO_NAME = 1;
    public final static int BTINFO_ADDR = 2;
    public final static int BTINFO_PIN = 3;

    /**
     * Current Device Info
     */
    public final static int CUR_DEVICE_ADDR = 0;
    public final static int CUR_DEVICE_NAME = 1;

    /**
     * PhoneBook Type
     **/
    public static final int TYPE_PHONEBOOK = 0;
    public static final int TYPE_ALLRECORD = 2;
    public static final int TYPE_DIALED = 4;
    public static final int TYPE_RECEIVED = 5;
    public static final int TYPE_MISSED = 6;

    /**
     * Music Play State
     **/
    public final static int MUSIC_ERROR = -1;
    public final static int MUSIC_STOPPED = 0;
    public final static int MUSIC_PLAYING = 1;
    public final static int MUSIC_PAUSED = 2;

    /**
     * Pair Mode
     **/
    public final static int PAIRMODE_IN = 0;
    public final static int PAIRMODE_OUT = 1;

    /**
     * 通话传递号码类型
     */
    public final static int TYPE_ACTIVE = 0;
    public final static int TYPE_WAITING = 1;
    public final static int TYPE_HELD = 2;

    /**
     * Music Info Type (Def seq for feasy)
     */
    public final static int MINFO_FINISH = 0;
    public final static int MINFO_TITLE = 1;
    public final static int MINFO_ARTIST = 2;
    public final static int MINFO_ALBUM = 3;
    public final static int MINFO_ORDINAL = 4;
    public final static int MINFO_AMOUNT = 5;
    public final static int MINFO_GENRE = 6;
    public final static int MINFO_DURATION = 7;

    /**
     * Music player attr value definition
     */
    public final static int ATTR_EQ = 0;
    public final static int ATTR_REPEAT = 1;
    public final static int ATTR_SHUFFLE = 2;
    public final static int ATTR_SCAN = 3;
    public final static int FUNC_OFF = 1;
    public final static int EQ_ON = 2;
    public final static int REPEAT_SINGLE = 2;
    public final static int REPEAT_ALL = 3;
    public final static int REPEAT_GROUP = 4;
    public final static int SHUFFLE_ALL = 2;
    public final static int SHUFFLE_GROUP = 3;
    public final static int SCAN_ALL = 2;
    public final static int SCAN_GROUP = 3;

    public final static String KEY_BLUETOOTH_NAME = "hazens.bluetooth.name";
    /**
     * 电话页面类型
     */
    public static final String KEY_PHONE_TYPE = "key_phone_type";
    public final static int A2DP_ON = 2;

    public static volatile boolean REMOTE_CONTROL_STATE = false;
}