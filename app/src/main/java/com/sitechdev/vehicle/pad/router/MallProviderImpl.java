package com.sitechdev.vehicle.pad.router;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;

/**
 * 商城组件服务具体实现
 *
 * @author liuhe
 * @date 2019/06/25
 */
@Route(path = RouterConstants.MALL_SERVICE)
public class MallProviderImpl implements IMallProvider {
    @Override
    public void showMallHome() {

    }

    @Override
    public void init(Context context) {

    }
}
