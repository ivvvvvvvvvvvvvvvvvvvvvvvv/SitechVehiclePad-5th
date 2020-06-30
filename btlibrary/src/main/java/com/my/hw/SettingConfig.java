package com.my.hw;

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

    private boolean isBtConnected = false;
    private boolean isA2dpConnected = false;
    private boolean isHFPConnected = false;
    /**
     * 本地蓝牙模块名称
     */
    private String localBtName = "CAR KIT-K";
    /**
     * 已连接蓝牙设备的MAC地址
     */
    private String connectBtAdd = "";
    /**
     * 已连接蓝牙设备的名称
     */
    private String connectBtName = "";

    public String getLocalBtName() {
        return localBtName;
    }

    public void setLocalBtName(String localBtName) {
        this.localBtName = localBtName;
    }

    public boolean isA2dpConnected() {
        return isA2dpConnected;
    }

    public void setA2dpConnected(boolean a2dpConnected) {
        isA2dpConnected = a2dpConnected;
    }

    public boolean isHFPConnected() {
        return isHFPConnected;
    }

    public void setHFPConnected(boolean hFPConnected) {
        isHFPConnected = hFPConnected;
        if (!hFPConnected) {
            connectBtAdd = "";
        }
    }

    public boolean isBtConnected() {
        return isA2dpConnected || isHFPConnected;
    }

    public void setBtConnected(boolean btConnected) {
        isBtConnected = btConnected;
    }

    public String getConnectBtAdd() {
        return connectBtAdd;
    }

    public void setConnectBtAdd(String connectBtAdd) {
        this.connectBtAdd = connectBtAdd;
    }

    /**
     * 返回当前连接的蓝牙设备名称。
     */
    public String getConnectBtName() {
        return connectBtName;
    }

    public void setConnectBtName(String connectBtName) {
        this.connectBtName = connectBtName;
    }
}
