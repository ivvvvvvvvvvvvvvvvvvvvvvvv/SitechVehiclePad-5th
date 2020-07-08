package com.sitechdev.vehicle.pad.module.setting.presenter;

import android.content.Context;

import com.my.hw.OnBtPairListChangeListener;
import com.my.hw.SettingConfig;
import com.sitechdev.vehicle.pad.model.contract.SettingBtContract;
import com.sitechdev.vehicle.pad.module.setting.bt.BtManager;

public class SettingBtPresenter extends SettingBtContract.BtPresenter {


    @Override
    public void openBt(Context context) {
        BtManager.getInstance().openBt();
    }

    @Override
    public void closeBt() {
        BtManager.getInstance().closeBt();
    }

    @Override
    public void enableDiscoverable(boolean enable) {
        BtManager.getInstance().setDiscovered(enable);
    }

    @Override
    public void startScan() {
//        BtManager.getInstance().scanBtList();
    }

    @Override
    public void connectToDevice(String mac) {
        BtManager.getInstance().connectToDevice(mac);
    }

    @Override
    public void disconnectToDevice() {
        BtManager.getInstance().disConnectToDevice();
    }

    @Override
    public boolean isBtConnected() {
        return SettingConfig.getInstance().isBtConnected();
    }

    @Override
    public void registerReceiver(Context context) {
//        BtManager.getInstance().registerReceiver(context, new BtManager.BtScanCallback() {
//            @Override
//            public void onScan() {
//                showBtPairList();
//            }
//        });
    }

    @Override
    public void unRegisterReceiver(Context context) {
//        BtManager.getInstance().unregisterReceiver(context);
    }

    @Override
    public void showBtPairList() {
        BtManager.getInstance().showBtPairList();
    }

    @Override
    public String getLocalName() {
        return "";//BtManager.getInstance().getLocalName();
    }

    @Override
    public void init() {
        BtManager.getInstance().init();
    }

    @Override
    public boolean isBtEnable() {
        return BtManager.getInstance().isBtEnable();
    }

    @Override
    public void registerPaireListCallback(OnBtPairListChangeListener listener) {
        BtManager.getInstance().setOnPairListChangeListener(listener);
    }

    @Override
    public void clearPairInfo(String mac) {
        BtManager.getInstance().clearPairInfo(mac);
    }
}
