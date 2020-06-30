package com.sitechdev.vehicle.pad.model.contract;

import com.sitechdev.vehicle.pad.app.BasePresenter;
import com.sitechdev.vehicle.pad.bean.IContract;
import com.sitechdev.vehicle.pad.event.DialEvent;
import com.sitechdev.vehicle.pad.event.SysEvent;

/**
 * 通讯录相关协议层
 *
 * @author liuhe
 */
public interface DialContract {

    abstract class Presenter extends BasePresenter<View> {

        public abstract void passKey(char key);

        public abstract void refreshPhoneNumStr(DialEvent dialEvent);

        public abstract void onSysEvent(SysEvent event);

        public abstract void delete();
    }

    interface View extends IContract.IView {

        void onDialNumber(String number);

        void hideInputWindow();
    }
}