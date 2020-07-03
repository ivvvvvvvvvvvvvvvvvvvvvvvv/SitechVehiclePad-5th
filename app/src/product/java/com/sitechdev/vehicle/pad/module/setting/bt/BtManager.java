package com.sitechdev.vehicle.pad.module.setting.bt;

import com.my.hw.ATBluetooth;
import com.my.hw.OnBtConnecStatusChangedListener;
import com.my.hw.OnBtPairListChangeListener;
import com.my.hw.SettingConfig;
import com.sitechdev.vehicle.lib.event.EventBusUtils;
import com.sitechdev.vehicle.pad.event.SysEvent;

public class BtManager {
    private static BtManager INSTANCE;
    private OnBtConnecStatusChangedListener onBtConnecStatusChangedListener;
    private OnBtPairListChangeListener mPairListChangeListener;

    public void setOnBtConnecStatusChangedListener(OnBtConnecStatusChangedListener onBtConnecStatusChangedListener) {
        mATBluetooth.setOnBtConnectStatusChangedListener(onBtConnecStatusChangedListener);
    }

    public void setOnPairListChangeListener(OnBtPairListChangeListener mPairListChangeListener) {
        mATBluetooth.setOnBtPairListChangeListener(mPairListChangeListener);
    }

    private BtManager() {
    }

    public void init() {
        mATBluetooth = ATBluetooth.create();
    }

    public static BtManager getInstance() {
        if (null == INSTANCE) {
            synchronized (BtManager.class) {
                if (null == INSTANCE) {
                    INSTANCE = new BtManager();
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
        SettingConfig.getInstance().setBtEnable(true);
        EventBusUtils.postEvent(new SysEvent(SysEvent.EB_SYS_BT_ENABLE,true));
    }

    /**
     * 关闭蓝牙
     */
    public void closeBt() {
        mATBluetooth.write(ATBluetooth.STOP_MODULE);
        SettingConfig.getInstance().setBtEnable(false);
        EventBusUtils.postEvent(new SysEvent(SysEvent.EB_SYS_BT_ENABLE,false));
    }

    /**
     * 蓝牙是否连接
     *
     * @return true=已连接
     */
    public boolean isBtEnable() {
        return SettingConfig.getInstance().isBtEnable();
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
