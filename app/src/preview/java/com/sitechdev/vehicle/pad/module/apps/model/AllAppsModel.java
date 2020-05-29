package com.sitechdev.vehicle.pad.module.apps.model;

import com.google.gson.Gson;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.lib.util.StringUtils;
import com.sitechdev.vehicle.pad.app.AppApplication;
import com.sitechdev.vehicle.pad.bean.AllModuleBean;
import com.sitechdev.vehicle.pad.module.apps.contract.AllAppsContract;

import java.lang.ref.SoftReference;

/***
 * 日期： 2019/4/4 13:52
 * 姓名： sitechdev_bjs
 * 说明： 更新内容..
 */
public class AllAppsModel implements AllAppsContract.Model {
    private static SoftReference<AllModuleBean> moduleBeanSoftReference = null;

    @Override
    public void getAllModuleData(AllAppsContract.AllModuleCallback callback) {
        //获取数据
        try {
            AllModuleBean bean = null;
            //先从软引用中取值
            if (moduleBeanSoftReference != null) {
                bean = moduleBeanSoftReference.get();
            }
            if (bean != null) {
                callback.onSuccess(bean);
                return;
            }
            String allAppJson = "";
            //从本地保存的数据中取值
            if (StringUtils.isEmpty(allAppJson)) {
                allAppJson = AllModuleUtils.getSavedMenuData();
            }
            //再从assets文件中取默认值
            if (StringUtils.isEmpty(allAppJson)) {
                allAppJson = AllModuleUtils.getAssetsJson2String(AppApplication.getContext());
            }
//            SitechDevLog.w(AppConst.TAG_APP, "读取的菜单数据为=====" + allAppJson);
            bean = new Gson().fromJson(allAppJson, AllModuleBean.class);
            //软引用赋最新值
            if (moduleBeanSoftReference == null || moduleBeanSoftReference.get() == null) {
                moduleBeanSoftReference = new SoftReference<>(bean);
            }
            callback.onSuccess(bean);
        } catch (Exception e) {
            SitechDevLog.exception(e);
            callback.onFailed(e.getMessage());
        }
    }

}
