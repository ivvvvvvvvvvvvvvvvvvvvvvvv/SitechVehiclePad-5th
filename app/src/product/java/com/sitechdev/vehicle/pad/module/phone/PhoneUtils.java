package com.sitechdev.vehicle.pad.module.phone;

import android.content.Context;
import android.content.Intent;

import com.my.hw.SettingConfig;

public class PhoneUtils {
    private static PhoneUtils INSTANCE;

    private PhoneUtils() {
    }

    public static PhoneUtils getInstance() {
        if (null == INSTANCE) {
            synchronized (PhoneUtils.class) {
                if (null == INSTANCE) {
                    INSTANCE = new PhoneUtils();
                }
            }
        }
        return INSTANCE;
    }

    public void startPhoneActivity(Context context) {
        if (null != context) {
            if (SettingConfig.getInstance().isBtConnected()) {
                context.startActivity(new Intent(context, PhoneActivity.class));
            } else {
                //TODO 打开蓝牙未连接界面
            }
        }
    }

}
