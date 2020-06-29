package com.sitechdev.vehicle.pad.model.contract;

import android.content.Context;

import com.my.hw.OnBtConnecStatusChangedListener;
import com.my.hw.OnBtPairListChangeListener;
import com.sitechdev.vehicle.pad.app.BasePresenter;
import com.sitechdev.vehicle.pad.bean.IContract;
import com.sitechdev.vehicle.pad.module.setting.bt.BtDeviceItem;

import java.util.List;

public interface PhoneContract {
    interface Model extends IContract.IModel{

    }

    abstract static class Presenter extends BasePresenter<SettingBtContract.View> {
    }
    interface View extends IContract.IView{
    }

    interface CallBack{
    }
}
