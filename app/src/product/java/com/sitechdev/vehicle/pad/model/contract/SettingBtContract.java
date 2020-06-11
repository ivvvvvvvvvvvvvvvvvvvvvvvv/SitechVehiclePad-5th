package com.sitechdev.vehicle.pad.model.contract;

import android.content.Context;

import com.sitechdev.vehicle.pad.app.BasePresenter;
import com.sitechdev.vehicle.pad.bean.IContract;
import com.sitechdev.vehicle.pad.module.setting.bt.BtDeviceItem;

import java.util.List;

public interface SettingBtContract {
    interface Model extends IContract.IModel{

    }

    abstract static class BtPresenter extends BasePresenter<View> {
        public abstract void openBt(Context context);
        public abstract void closeBt();
        public abstract void enableDiscoverable(boolean enable);
        public abstract void startScan();
        public abstract void connectToDevice(String mac);
        public abstract void registerReceiver(Context context);
        public abstract void unRegisterReceiver(Context context);
        public abstract boolean isBtConnected();
        public abstract void showBtPairList();
        public abstract String getLocalName();
    }
    interface View extends IContract.IView{
        void showBtList(List<BtDeviceItem> list);
    }

    interface CallBack{
        void onDeviceConnected();
        void onDeviceDisConnected();
    }
}
