package com.sitechdev.vehicle.pad.module.setting.presenter;

import android.content.Context;
import android.util.Log;

import com.sitechdev.vehicle.pad.model.contract.SettingBtContract;
import com.sitechdev.vehicle.pad.module.setting.bt.BtDeviceItem;
import com.sitechdev.vehicle.pad.module.setting.bt.BtManager;

import java.util.ArrayList;
import java.util.List;

public class SettingBtPresenter extends SettingBtContract.BtPresenter {
    @Override
    public void openBt(Context context) {
        BtManager.getInstance().openBle(context);
    }

    @Override
    public void closeBt() {
        BtManager.getInstance().closeBle();
    }

    @Override
    public void enableDiscoverable(boolean enable) {
        BtManager.getInstance().setDiscovered(enable);
    }

    @Override
    public void startScan() {
        BtManager.getInstance().scanBtList();
    }

    @Override
    public void connectToDevice(String mac) {

    }

    @Override
    public boolean isBtConnected() {
        return BtManager.getInstance().isEnable();
    }

    @Override
    public void registerReceiver(Context context) {
        BtManager.getInstance().registerReceiver(context, new BtManager.BtScanCallback() {
            @Override
            public void onScan() {
                showBtPairList();
            }
        });
    }

    @Override
    public void unRegisterReceiver(Context context) {
        BtManager.getInstance().unregisterReceiver(context);
    }

    @Override
    public void showBtPairList() {
        List<BtDeviceItem> list = BtManager.getInstance().getBtPairList();
        if (null == list) {
            list = new ArrayList<>();
        }
        getView().showBtList(list);
    }

    @Override
    public String getLocalName() {
        return BtManager.getInstance().getLocalName();
    }
}
