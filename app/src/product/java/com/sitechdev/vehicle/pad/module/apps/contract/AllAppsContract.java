package com.sitechdev.vehicle.pad.module.apps.contract;

import com.sitechdev.vehicle.pad.app.BasePresenter;
import com.sitechdev.vehicle.pad.bean.AllModuleBean;
import com.sitechdev.vehicle.pad.bean.IContract;

/***
 * 日期： 2019/4/4 13:48
 * 姓名： sitechdev_bjs
 * 说明： 更新内容..
 */
public interface AllAppsContract {
    interface Model extends IContract.IModel {
        void getAllModuleData(AllModuleCallback callback);
    }

    abstract class Presenter extends BasePresenter<View> {
        public abstract void loadAllModuleData();
    }

    interface View extends IContract.IView {
        void refreshAllModuleView(AllModuleBean bean);
    }

    interface AllModuleCallback {
        void onSuccess(AllModuleBean bean);

        void onFailed(String msg);
    }

}
