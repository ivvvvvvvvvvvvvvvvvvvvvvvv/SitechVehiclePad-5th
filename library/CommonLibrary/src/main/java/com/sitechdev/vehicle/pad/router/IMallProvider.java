package com.sitechdev.vehicle.pad.router;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * 组件路由服务类
 * （1）在frmework中定义商城组件provider,定义跳转商城首页的方法
 * void showMallHome();
 * (2)商城组件中配置具体服务路径，并实现以下方法，如MallProviderImpl
 * （3）在其它组件中调用
 * IMallProvider mallProvider = RouterUtils.getInstance().provide(IMallProvider.class, RouterConstants.MALL_SERVICE);
 * mallProvider.showMallHome();
 *
 * @author liuhe
 * @date 2019/06/25
 */
public interface IMallProvider extends IProvider {

    /**
     * 路由到商城首页
     */
    void showMallHome();
}
