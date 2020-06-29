package com.sitechdev.vehicle.pad.model.contract;


import com.sitechdev.vehicle.pad.app.BasePresenter;
import com.sitechdev.vehicle.pad.bean.IContract;
import com.sitechdev.vehicle.pad.event.BTEvent;
import com.sitechdev.vehicle.pad.event.SysEvent;
import com.sitechdev.vehicle.pad.model.phone.CallLog;

import java.util.ArrayList;


/**
 * 通话记录相关协议层
 *
 * @author liuhe
 */
public interface CallLogContract {

    abstract class Presenter extends BasePresenter<View> {

        public abstract void start();

        public abstract void reqCallLogs();

        public abstract void dial(String name, String number);

        public abstract void onBTEvent(final BTEvent event);

        public abstract void onFilterCallLogs(SysEvent event);
    }

    interface View extends IContract.IView {

        void showLoadSuccessView();

        void showLoadFailedView();

        void showEmptyView();

        /**
         * 查看未接电话、已接电话等
         *
         * @param callLogs
         *         通话记录列表
         */
        void refreshCallLogs(ArrayList<CallLog> callLogs);

        void changeDisplay(int type);

        void onClCount(int count);

        /**
         * 筛选结果为空，展示
         */
        void showNoFilerCallLogs();
    }
}