//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.util;

public class Kernel {
    private static final String KEY = "sys/class/ak/mcu/key";
    public static final int KEY_BACK = 158;
    public static final int KEY_DOWN = 108;
    public static final int KEY_ENTER = 28;
    private static final String KEY_EX = "sys/class/ak/tkey/key_ex";
    public static final int KEY_F11 = 87;
    public static final int KEY_F12 = 88;
    public static final int KEY_F7 = 65;
    public static final int KEY_F8 = 66;
    public static final int KEY_HOMEPAGE = 172;
    public static final int KEY_LEFT = 105;
    public static final int KEY_LEFTSHIFT = 42;
    public static final int KEY_MAX = 255;
    public static final int KEY_MENU = 139;
    public static final int KEY_NEXTSONG = 163;
    public static final int KEY_PLAYPAUSE = 164;
    public static final int KEY_PREVIOUSSONG = 165;
    public static final int KEY_RIGHT = 106;
    public static final int KEY_TAB = 15;
    public static final int KEY_UP = 103;

    public Kernel() {
    }

    public static void doKeyEvent(int var0) {
        Util.setFileValue("sys/class/ak/mcu/key", var0);
    }

    public static void doKeyEvent(int var0, int var1) {
        Util.setFileValue("sys/class/ak/tkey/key_ex", new byte[]{(byte)var0, (byte)var1});
    }

    public static void doKeyEventEx(int var0, int var1) {
        Util.setFileValue("sys/class/ak/tkey/key_ex", new byte[]{(byte)var0, 1});
        Util.doSleep((long)var1);
        Util.setFileValue("sys/class/ak/tkey/key_ex", new byte[]{(byte)var0, 0});
    }
}
