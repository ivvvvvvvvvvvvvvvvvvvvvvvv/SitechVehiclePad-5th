package com.sitechdev.vehicle.pad.module.music;

import com.my.hw.ATBluetooth;
import com.my.hw.OnBtConnecStatusChangedListener;

public class BtMusicManager {
    private static BtMusicManager INSTANCE;
    private ATBluetooth mATBluetooth;

    private BtMusicManager() {
        mATBluetooth = ATBluetooth.create();
    }

    public static BtMusicManager getInstance() {
        if (null == INSTANCE) {
            synchronized (BtMusicManager.class) {
                if (null == INSTANCE) {
                    INSTANCE = new BtMusicManager();
                }
            }
        }
        return INSTANCE;
    }

    public void btCtrlPre() {
        mATBluetooth.write(ATBluetooth.REQUEST_A2DP_PREV);
    }

    public void btCtrlPlay() {
        mATBluetooth.write(ATBluetooth.REQUEST_A2DP_PLAY);
    }

    public void btCtrlPause() {
        mATBluetooth.write(ATBluetooth.REQUEST_A2DP_PAUSE);
    }

    public void btCtrlNext() {
        mATBluetooth.write(ATBluetooth.REQUEST_A2DP_NEXT);
    }

    public void setOnBtConnecStatusChangedListener(OnBtConnecStatusChangedListener onBtConnecStatusChangedListener) {
        mATBluetooth.setOnBtConnectStatusChangedListener(onBtConnecStatusChangedListener);
    }
}
