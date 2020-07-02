package com.sitechdev.vehicle.pad.module.setting.bt;

import android.util.Log;

import com.my.hw.ATBluetooth;
import com.my.hw.BtDeviceBean;
import com.my.hw.OnBtConnecStatusChangedListener;
import com.my.hw.OnBtPairListChangeListener;
import com.my.hw.SettingConfig;

public class BtManagers {
    private static BtManagers INSTANCE;
    private OnBtConnecStatusChangedListener onBtConnecStatusChangedListener;
    private OnBtPairListChangeListener mPairListChangeListener;

    public void setOnBtConnecStatusChangedListener(OnBtConnecStatusChangedListener onBtConnecStatusChangedListener) {
        mATBluetooth.setOnBtConnectStatusChangedListener(onBtConnecStatusChangedListener);
    }

    public void setOnPairListChangeListener(OnBtPairListChangeListener mPairListChangeListener) {
        mATBluetooth.setOnBtPairListChangeListener(mPairListChangeListener);
    }

    private BtManagers() {
    }

    public void init() {
        mATBluetooth = ATBluetooth.create();
    }

    public static BtManagers getInstance() {
        if (null == INSTANCE) {
            synchronized (BtManagers.class) {
                if (null == INSTANCE) {
                    INSTANCE = new BtManagers();
                }
            }
        }
        return INSTANCE;
    }

    private ATBluetooth mATBluetooth;

    /**
     * 根据地址连接蓝牙设备
     *
     * @param add
     */
    public void connectToDevice(String add) {
        mATBluetooth.write(ATBluetooth.REQUEST_CONNECT_BY_ADDR, add);
    }

    /**
     * 获取蓝牙配对列表
     */
    public void requestPairedList() {
        mATBluetooth.write(ATBluetooth.REQUEST_SEARCH);
    }

    /**
     * 打开蓝牙
     */
    public void openBt() {
        mATBluetooth.write(ATBluetooth.START_MODULE);
    }

    /**
     * 关闭蓝牙
     */
    public void closeBt() {
        mATBluetooth.write(ATBluetooth.STOP_MODULE);
    }

    /**
     * 蓝牙是否连接
     *
     * @return true=已连接
     */
    public boolean isBtEnable() {
        return SettingConfig.getInstance().isBtConnected();
    }

    public void showBtPairList() {
        mATBluetooth.write(ATBluetooth.GET_PAIR_INFO);
        mATBluetooth.write(ATBluetooth.GET_HFP_INFO);
    }

    public void disConnectToDevice() {
        mATBluetooth.write(ATBluetooth.REQUEST_DISCONNECT);
    }

    public void clearPairInfo(String mac) {
        mATBluetooth.write(ATBluetooth.CLEAR_PAIR_INFO, mac);
    }

    public void setDiscovered(boolean enable) {
        mATBluetooth.write(enable ? ATBluetooth.REQUEST_BT_DISCOVERABLE : ATBluetooth.REQUEST_BT_UN_DISCOVERABLE);
    }
}
