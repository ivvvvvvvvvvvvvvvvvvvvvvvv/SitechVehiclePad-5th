package com.my.hw;

import com.sitechdev.vehicle.lib.util.ParamsUtil;
import com.sitechdev.vehicle.lib.util.StringUtils;

public class SettingConfig {
    private static SettingConfig INSTANCE;
    private static final String SP_KEY_SETTING_BT_ENABLE = "SP_KEY_SETTING_BT_ENABLE";

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
     * 蓝牙开关
     */
    private boolean isBtEnable = true;
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

    /**
     * 正在连接的蓝牙设备名称
     * 用于连接失败时弹窗使用
     *
     */
    private String connecttingBtName = "";

    public String getConnecttingBtName() {
        return connecttingBtName;
    }

    public void setConnecttingBtName(String connecttingBtName) {
        this.connecttingBtName = connecttingBtName;
    }

    public boolean isBtEnable() {
        String enable = ParamsUtil.getStringData(SP_KEY_SETTING_BT_ENABLE);
        if (StringUtils.isEmpty(enable)) {
            isBtEnable = true;
        } else {
            isBtEnable = Boolean.parseBoolean(enable);
        }
        return isBtEnable;
    }

    public void setBtEnable(boolean btEnable) {
        isBtEnable = btEnable;
        ParamsUtil.setData(SP_KEY_SETTING_BT_ENABLE, String.valueOf(btEnable));
    }

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
