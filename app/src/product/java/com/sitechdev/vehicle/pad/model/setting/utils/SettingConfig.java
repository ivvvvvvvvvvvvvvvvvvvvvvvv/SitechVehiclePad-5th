package com.sitechdev.vehicle.pad.model.setting.utils;


public class SettingConfig {
    private static SettingConfig INSTANCE;

    private SettingConfig() {
    }

    public static SettingConfig getInstance() {
        if (null == INSTANCE) {
            synchronized (SettingConfig.class) {
                if (null == INSTANCE) {
                    INSTANCE = new SettingConfig();
                }
            }
        }
        return INSTANCE;
    }

    private String connectBtAdd = "";
    private String connectBtName = "";

    public String getConnectBtAdd() {
        return connectBtAdd;
    }

    public void setConnectBtAdd(String connectBtAdd) {
        this.connectBtAdd = connectBtAdd;
    }

    public String getConnectBtName() {
        return connectBtName;
    }

    public void setConnectBtName(String connectBtName) {
        this.connectBtName = connectBtName;
    }
}
