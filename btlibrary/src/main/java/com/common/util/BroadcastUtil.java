//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.util;

import android.content.Context;
import android.content.Intent;

public class BroadcastUtil {
    public BroadcastUtil() {
    }

    public static final void sendByCarService(Context var0, int var1, int var2) {
        Intent var3 = new Intent("com.my.car.service.BROADCAST_CAR_SERVICE_SEND");
        var3.putExtra("cmd", var1);
        var3.putExtra("data", var2);
        var0.sendBroadcast(var3);
    }

    public static final void sendByCarService(Context var0, int var1, int var2, int var3) {
        Intent var4 = new Intent("com.my.car.service.BROADCAST_CAR_SERVICE_SEND");
        var4.putExtra("cmd", var1);
        var4.putExtra("data", var2);
        var4.putExtra("data2", var3);
        var0.sendBroadcast(var4);
    }

    public static final void sendByCarService(Context var0, int var1, int var2, Object var3) {
        Intent var4 = new Intent("com.my.car.service.BROADCAST_CAR_SERVICE_SEND");
        var4.putExtra("cmd", var1);
        var4.putExtra("data", var2);
        var0.sendBroadcast(var4);
    }

    public static final void sendByCarService(Context var0, String var1, int var2) {
        Intent var3 = new Intent("com.my.car.service.BROADCAST_CAR_SERVICE_SEND");
        var3.putExtra("cmd", var2);
        if (var1 != null) {
            var3.setPackage(var1);
        }

        var0.sendBroadcast(var3);
    }

    public static final void sendByCarService(Context var0, String var1, int var2, int var3) {
        Intent var4 = new Intent("com.my.car.service.BROADCAST_CAR_SERVICE_SEND");
        var4.putExtra("cmd", var2);
        var4.putExtra("data", var3);
        if (var1 != null) {
            var4.setPackage(var1);
        }

        var0.sendBroadcast(var4);
    }

    public static final void sendByCarService(Context var0, String var1, int var2, String var3) {
        Intent var4 = new Intent("com.my.car.service.BROADCAST_CAR_SERVICE_SEND");
        var4.putExtra("cmd", var2);
        var4.putExtra("data", var3);
        if (var1 != null) {
            var4.setPackage(var1);
        }

        var0.sendBroadcast(var4);
    }

    public static final void sendByCarService(Context var0, String var1, int var2, byte[] var3) {
        Intent var4 = new Intent("com.my.car.service.BROADCAST_CAR_SERVICE_SEND");
        var4.putExtra("cmd", var2);
        var4.putExtra("data", var3);
        if (var1 != null) {
            var4.setPackage(var1);
        }

        var0.sendBroadcast(var4);
    }

    public static final void sendByCarService(Context var0, String[] var1, int var2, byte[] var3) {
        Intent var5 = new Intent("com.my.car.service.BROADCAST_CAR_SERVICE_SEND");
        var5.putExtra("cmd", var2);
        var5.putExtra("data", var3);
        int var4 = var1.length;

        for(var2 = 0; var2 < var4; ++var2) {
            var5.setPackage(var1[var2]);
            var0.sendBroadcast(var5);
        }

    }

    public static final void sendByCarServicePemission(Context var0, String var1, int var2, byte[] var3) {
        Intent var4 = new Intent("com.my.car.service.BROADCAST_CAR_SERVICE_SEND");
        var4.putExtra("cmd", var2);
        var4.putExtra("data", var3);
        var0.sendBroadcast(var4, var1);
    }

    public static final void sendCanboxInfo(Context var0, String var1, int var2, int var3, int var4) {
        Intent var5 = new Intent(var1);
        var5.putExtra("value1", var2);
        var5.putExtra("value2", var3);
        var5.putExtra("value3", var4);
        sendToCarService(var0, var5);
    }

    public static final void sendCanboxInfo(Context var0, byte[] var1) {
        Intent var2 = new Intent("com.my.canbox.BROADCAST_SEND_TO_CAN");
        var2.putExtra("buf", var1);
        sendToCarService(var0, var2);
    }

    public static final void sendKey(Context var0, byte var1, int var2, boolean var3) {
        Intent var4 = new Intent("com.seanovo.android.ACTION_KEY_PRESS");
        var4.putExtra("extras_key_press_type", var1);
        var4.putExtra("extras_key_code", var2);
        var4.putExtra("extras_is_need_delay", var3);
        var0.sendBroadcast(var4);
    }

    public static final void sendKey(Context var0, int var1) {
        Intent var2 = new Intent("com.seanovo.android.ACTION_KEY_PRESS");
        var2.putExtra("extras_key_press_type", (byte)1);
        var2.putExtra("extras_key_code", var1);
        var0.sendBroadcast(var2);
    }

    public static final void sendKey(Context var0, int var1, byte var2) {
        Intent var3 = new Intent("com.seanovo.android.ACTION_KEY_PRESS");
        var3.putExtra("data", var2);
        var3.putExtra("extras_key_code", var1);
        var0.sendBroadcast(var3);
    }

    public static final void sendKey(Context var0, String var1, int var2) {
        Intent var3 = new Intent("com.seanovo.android.ACTION_KEY_PRESS");
        var3.putExtra("extras_key_press_type", (byte)1);
        var3.putExtra("extras_key_code", var2);
        if (var1 != null) {
            var3.setPackage(var1);
        }

        var0.sendBroadcast(var3);
    }

    public static final void sendToCarService(Context var0, int cmd, int data) {
        if (data == MyCmd.PhoneStatus.PHONE_ON){
            Util.setProperty("ak.af.btphone.on", "1");
        } else if (data == MyCmd.PhoneStatus.PHONE_OFF){
            Util.setProperty("ak.af.btphone.on", "0");
        }
        Intent var3 = new Intent("com.my.car.service.BROADCAST_CMD_TO_CAR_SERVICE");
        var3.putExtra("cmd", cmd);
        var3.putExtra("data", data);
        var3.setPackage("com.my.out");
        sendToCarService(var0, var3);
    }

    public static final void sendToCarService(Context var0, int var1, int var2, int var3) {
        Intent var4 = new Intent("com.my.car.service.BROADCAST_CMD_TO_CAR_SERVICE");
        var4.putExtra("cmd", var1);
        var4.putExtra("data", var2);
        var4.putExtra("data2", var3);
        var4.setPackage("com.my.out");
        sendToCarService(var0, var4);
    }

    public static final void sendToCarService(Context var0, int var1, int var2, int var3, int var4) {
        Intent var5 = new Intent("com.my.car.service.BROADCAST_CMD_TO_CAR_SERVICE");
        var5.putExtra("cmd", var1);
        var5.putExtra("data", var2);
        var5.putExtra("data2", var3);
        var5.putExtra("data3", var4);
        var5.setPackage("com.my.out");
        sendToCarService(var0, var5);
    }

    public static final void sendToCarService(Context var0, Intent var1) {
        var1.setPackage("com.my.out");
        var0.sendBroadcast(var1);
    }

    public static final void sendToCarServiceMcuDVD(Context var0, int var1, int var2) {
        sendToCarService(var0, 4102, var1, var2);
    }

    public static final void sendToCarServiceMcuDVD(Context var0, int var1, int var2, int var3) {
        sendToCarService(var0, 4102, var1, var2, var3);
    }

    public static final void sendToCarServiceMcuEQ(Context var0, int var1, int var2) {
        sendToCarService(var0, 4100, var1, var2);
    }

    public static final void sendToCarServiceMcuEQ(Context var0, int var1, int var2, int var3) {
        sendToCarService(var0, 4100, var1, var2, var3);
    }

    public static final void sendToCarServiceMcuRadio(Context var0, int var1, int var2) {
        sendToCarService(var0, 4096, var1, var2);
    }

    public static final void sendToCarServiceMcuRadio(Context var0, int var1, int var2, int var3) {
        sendToCarService(var0, 4096, var1, var2, var3);
    }

    public static final void sendToCarServiceMcuRds(Context var0, int var1, int var2) {
        sendToCarService(var0, 4097, var1, var2);
    }

    public static final void sendToCarServiceMcuRds(Context var0, int var1, int var2, int var3) {
        sendToCarService(var0, 4097, var1, var2, var3);
    }

    public static final void sendToCarServiceSetSource(Context var0, int var1) {
        sendToCarService(var0, 1, var1);
    }
}
