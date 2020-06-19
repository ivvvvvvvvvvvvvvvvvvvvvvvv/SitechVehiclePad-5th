//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.util;

public class ProtocolAk47 {
    public static final byte RECEVE_AUDIO_EQ_INFO = 3;
    public static final byte RECEVE_AUDIO_MUTE_INFO = 6;
    public static final byte RECEVE_AUDIO_VOLUME_INFO = 5;
    public static final byte RECEVE_COMMON_KEY_INFO = 1;
    public static final byte RECEVE_RADIO_SUB_CURRENT_RADIO_REGION_INFO = 4;
    public static final byte RECEVE_RADIO_SUB_PRESET_LIST_FREQUENCY_INFO = 3;
    public static final byte RECEVE_RADIO_SUB_RADIO_CURRENT_INFO = 2;
    public static final byte RECEVE_RDS_SUB_PRESET_LIST_PS_INFO = 6;
    public static final byte RECEVE_RDS_SUB_PS_INFO = 4;
    public static final byte RECEVE_RDS_SUB_PTY_INFO = 3;
    public static final byte RECEVE_RDS_SUB_RDS_FLAGS = 2;
    public static final byte RECEVE_RDS_SUB_RT_INFO = 5;
    public static final byte SEND_AUDIO_CMD_SET = 2;
    public static final byte SEND_AUDIO_CMD_SET2 = 3;
    public static final byte SEND_AUDIO_SUB_QUERYAUDIO_INFO = 6;
    public static final byte SEND_COMMON_SUB_BEEP_CONTROL = 8;
    public static final byte SEND_COMMON_SUB_BLUETOOTH = 6;
    public static final byte SEND_COMMON_SUB_BRIGHTNESS = 4;
    public static final byte SEND_COMMON_SUB_CAR_STATUS_QUERY = 18;
    public static final byte SEND_COMMON_SUB_CURRENT_SOURCE = 5;
    public static final byte SEND_COMMON_SUB_CURRENT_SOURCE_QUERY = 16;
    public static final byte SEND_COMMON_SUB_DVD_LED = 0;
    public static final byte SEND_COMMON_SUB_DVD_POWER = 14;
    public static final byte SEND_COMMON_SUB_FACTORY_RESET = 11;
    public static final byte SEND_COMMON_SUB_FRONT_SOURCE = 2;
    public static final byte SEND_COMMON_SUB_POWER = 1;
    public static final byte SEND_COMMON_SUB_REAL_L_SOURCE = 3;
    public static final byte SEND_COMMON_SUB_SCREEN0 = 23;
    public static final byte SEND_COMMON_SUB_SETTINGS_QUERY = 9;
    public static final byte SEND_COMMON_SUB_VERSION_QUERY = 10;
    public static final byte SEND_DVD_SUB_DVD_POWER = 1;
    public static final byte SEND_RADIO_SUB_QUERY_RADIO_INFO = 4;
    public static final byte SEND_RADIO_SUB_RADIO_OPERATION = 1;
    public static final byte SEND_RADIO_SUB_SET_CURRENT_FREQUENCY = 2;
    public static final byte SEND_RDS_SUB_OPT = 1;
    public static final byte SEND_RDS_SUB_QUERY = 3;
    public static final byte SEND_VOLUME_SUB_DATA1_SET_VOLUME = 0;
    public static final byte SEND_VOLUME_SUB_VOLUME_CONTROL = 4;
    public static final byte TYPE_AUDIO_RECEIVE = 4;
    public static final byte TYPE_AUDIO_SEND = 4;
    public static final byte TYPE_CAN_RECEIVE = 5;
    public static final byte TYPE_CAN_SEND = 5;
    public static final byte TYPE_COMMON_RECEIVE = 1;
    public static final byte TYPE_COMMON_SEND = 1;
    public static final byte TYPE_DVD_RECEIVE = 13;
    public static final byte TYPE_DVD_SEND = 13;
    public static final byte TYPE_IAP_RECEIVE = 7;
    public static final byte TYPE_IAP_SEND = 6;
    public static final byte TYPE_PANEL_KEY_RECEIVE = 11;
    public static final byte TYPE_PANEL_KEY_SEND = 11;
    public static final byte TYPE_RADIO_RECEIVE = 2;
    public static final byte TYPE_RADIO_SEND = 2;
    public static final byte TYPE_RDS_RECEIVE = 3;
    public static final byte TYPE_RDS_SEND = 3;
    public static final byte TYPE_SECRET_RECEIVE = 12;
    public static final byte TYPE_SECRET_SEND = 12;
    public static final byte TYPE_SETTINGS_RECEIVE = 6;
    public static final byte TYPE_SETTINGS_SEND = 10;
    public static final byte TYPE_SWC_RECEIVE = 9;
    public static final byte TYPE_SWC_SEND = 7;
    public static final byte TYPE_TV_SEND = 9;
    private byte resultType = 0;

    public ProtocolAk47() {
    }

    public static byte[] generateNullProtocol(byte var0, byte var1) {
        return new byte[]{var0, var1};
    }

    public static byte[] generateProtocol1(byte var0, byte var1, byte var2) {
        return new byte[]{var0, var1, var2};
    }

    public static byte[] generateProtocol2(byte var0, byte var1, byte var2, byte var3) {
        return new byte[]{var0, var1, var2, var3};
    }

    public static int getDataLength(byte[] var0) {
        return getProtocolLength(var0) - 2;
    }

    public static int getDataOffset() {
        return 2;
    }

    public static final byte getGroupId(byte[] var0) {
        return var0[0];
    }

    public static int getProtocolLength(byte[] var0) {
        return var0.length;
    }

    public static final byte getSubId(byte[] var0) {
        return var0[1];
    }
}
