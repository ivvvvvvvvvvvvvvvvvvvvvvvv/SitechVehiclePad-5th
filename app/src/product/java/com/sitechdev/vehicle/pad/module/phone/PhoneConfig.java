package com.sitechdev.vehicle.pad.module.phone;

public class PhoneConfig {
    private PhoneConfig() {
    }

    private static PhoneConfig INSTANCE;

    public static PhoneConfig getInstance() {
        if (null == INSTANCE) {
            synchronized (PhoneConfig.class) {
                if (null == INSTANCE) {
                    INSTANCE = new PhoneConfig();
                }
            }
        }
        return INSTANCE;
    }
}
