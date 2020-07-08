package com.sitechdev.vehicle.pad.model.contract;

import com.sitechdev.vehicle.pad.app.BasePresenter;
import com.sitechdev.vehicle.pad.bean.IContract;

public interface PhoneContract {
    interface Model extends IContract.IModel {

    }

    abstract static class Presenter extends BasePresenter<SettingBtContract.View> {
    }

    interface View extends IContract.IView {
    }

    interface CallBack {
    }
}
