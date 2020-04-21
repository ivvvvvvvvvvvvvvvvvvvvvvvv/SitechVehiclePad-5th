package com.sitechdev.vehicle.lib.base;

import android.app.Application;

/**
 * Application基类，每个组件作为单独app时继承，保证每个组件使用同一个方法获取context
 *
 * @author liuhe
 * @date 2019/06/20
 */
public class BaseApp extends Application {

    private static Application sApp;

    public static Application getApp() {
        return sApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApp = this;
    }
}
