package com.sitechdev.vehicle.pad.manager;


/**
 * 项目名称：SitechVehiclePad
 * 类名称：RefreshUserTokenManger
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2019/08/16 0016 11:26
 * 修改时间：
 * 备注：
 */
public class RefreshUserTokenManger {

    private volatile int refreshTokenCount = 0;

    private RefreshUserTokenManger() {
        refreshTokenCount = 0;
    }

    private static class SingleToken {
        private static final RefreshUserTokenManger refreshManger = new RefreshUserTokenManger();
    }

    public static RefreshUserTokenManger getInstance() {
        return SingleToken.refreshManger;
    }

    public void addRefreshTokenThread(){

    }
}
