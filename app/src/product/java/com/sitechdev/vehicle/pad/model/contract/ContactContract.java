package com.sitechdev.vehicle.pad.model.contract;


import com.sitechdev.vehicle.pad.app.BasePresenter;
import com.sitechdev.vehicle.pad.bean.IContract;
import com.sitechdev.vehicle.pad.event.BTEvent;

/**
 * 通讯录相关协议层
 *
 * @author liuhe
 */
public interface ContactContract {

    abstract class Presenter extends BasePresenter<View> {

        public abstract void start();

        public abstract void reqPhoneBook();

        public abstract void dial(String dialPadPhoneNum);

        public abstract void onBTEvent(final BTEvent event);

        public abstract void onPbDataRecvd(String name, String number);

    }

    interface View extends IContract.IView {

        void showLoadSuccessView();

        void showLoadFailedView();

        void showEmptyView(boolean isFail);

        void onPbCount(int count);
    }
}