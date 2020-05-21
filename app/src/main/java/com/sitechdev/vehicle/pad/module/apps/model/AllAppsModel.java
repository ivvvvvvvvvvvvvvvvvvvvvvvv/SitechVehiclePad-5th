package com.sitechdev.vehicle.pad.module.apps.model;

import com.google.gson.Gson;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.lib.util.StringUtils;
import com.sitechdev.vehicle.pad.app.AppApplication;
import com.sitechdev.vehicle.pad.bean.AllModuleBean;
import com.sitechdev.vehicle.pad.module.apps.contract.AllAppsContract;

/***
 * 日期： 2019/4/4 13:52
 * 姓名： sitechdev_bjs
 * 说明： 更新内容..
 */
public class AllAppsModel implements AllAppsContract.Model {
    private AllModuleBean bean = null;

    @Override
    public void getAllModuleData(AllAppsContract.AllModuleCallback callback) {
        //获取数据
        if (bean != null) {
            callback.onSuccess(bean);
            return;
        }
        try {
            String allAppJson = AllModuleUtils.getSavedMenuData();
            if (StringUtils.isEmpty(allAppJson)) {
                allAppJson = AllModuleUtils.getAssetsJson2String(AppApplication.getContext());
            }
//            SitechDevLog.w(AppConst.TAG_APP, "读取的菜单数据为=====" + allAppJson);
            bean = new Gson().fromJson(allAppJson, AllModuleBean.class);
            callback.onSuccess(bean);
        } catch (Exception e) {
            SitechDevLog.exception(e);
            callback.onFailed(e.getMessage());
        }
    }
}
