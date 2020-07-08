package com.sitechdev.vehicle.pad.module.music;

import com.my.hw.ATBluetooth;

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

    public void getInfo() {
        mATBluetooth.write(ATBluetooth.RETURN_A2DP_AUDIO_INFO);
    }

    public void btCtrlNext() {
        mATBluetooth.write(ATBluetooth.REQUEST_A2DP_NEXT);
    }

    public void btCtrlRequestStatus() {
        mATBluetooth.write(ATBluetooth.REQUEST_A2DP_CONNECT_STATUS);
    }

    public void btCtrlPlayPause() {
        mATBluetooth.write(ATBluetooth.REQUEST__A2DP_AUDIO_PLAYPAUSE);
    }


}
