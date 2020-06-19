package com.sitechdev.vehicle.pad.module.setting.presenter;

import android.content.Context;
import android.util.Log;

import com.my.hw.OnBtConnecStatusChangedListener;
import com.my.hw.OnBtPairListChangeListener;
import com.sitechdev.vehicle.pad.model.contract.SettingBtContract;
import com.sitechdev.vehicle.pad.module.setting.bt.BtDeviceItem;
import com.sitechdev.vehicle.pad.module.setting.bt.BtManager;
import com.sitechdev.vehicle.pad.module.setting.bt.BtManagers;

import java.util.ArrayList;
import java.util.List;

public class SettingBtPresenter extends SettingBtContract.BtPresenter {


    @Override
    public void openBt(Context context) {
        BtManagers.getInstance().openBt();
    }

    @Override
    public void closeBt() {
        BtManagers.getInstance().closeBt();
    }

    @Override
    public void enableDiscoverable(boolean enable) {
        BtManagers.getInstance().setDiscovered(enable);
    }

    @Override
    public void startScan() {
//        BtManager.getInstance().scanBtList();
    }

    @Override
    public void connectToDevice(String mac) {
        BtManagers.getInstance().connectToDevice(mac);
    }

    @Override
    public void disconnectToDevice() {
        BtManagers.getInstance().disConnectToDevice();
    }

    @Override
    public boolean isBtConnected() {
        return BtManagers.getInstance().isBtEnable();
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
        BtManagers.getInstance().showBtPairList();
    }

    @Override
    public String getLocalName() {
        return "";//BtManager.getInstance().getLocalName();
    }

    @Override
    public void init() {
        BtManagers.getInstance().init();
    }

    @Override
    public boolean isBtEnable() {
//        return BtManager.getInstance().isBtEnable();
        return true;
    }

    @Override
    public void registerPaireListCallback(OnBtPairListChangeListener listener) {
        BtManagers.getInstance().setOnPairListChangeListener(listener);
    }

    @Override
    public void registerConnectCallback(OnBtConnecStatusChangedListener listener) {
        BtManagers.getInstance().setOnBtConnecStatusChangedListener(listener);
    }

    @Override
    public void clearPairInfo(String mac) {
        BtManagers.getInstance().clearPairInfo(mac);
    }
}
