package com.sitechdev.vehicle.pad.router;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.template.IProvider;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ActivityUtils;
import com.sitechdev.vehicle.lib.util.ActivityManager;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.lib.util.StringUtils;

/**
 * router工具类
 *
 * @author liuhe
 * @date 2019/03/23
 */
public class RouterUtils {

    private static final String TAG = RouterUtils.class.getSimpleName();

    private RouterUtils() {
    }

    private static final class Holder {
        private static final RouterUtils INSTANCE = new RouterUtils();
    }

    public static RouterUtils getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * ARouter初始化操作
     */
    public void init(boolean printLogs, Application app) {
        // 这两行必须写在init之前，否则这些配置在init过程中将无效
        if (printLogs) {
            // 打印日志
            ARouter.openLog();
            // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
            ARouter.openDebug();
        }
        ARouter.init(app);
    }

    /**
     * 应用内简单的跳转
     *
     * @param path 目标界面对应的路径
     */
    public void navigation(String path) {
        if (path == null) {
            return;
        }
        ARouter.getInstance()
                .build(path)
                .navigation();
    }

    /**
     * 构建Fragment
     *
     * @param path   fragment路径
     * @param bundle fragment带有值
     * @return
     */
    public Fragment buildFragment(String path, Bundle bundle) {
        try {
            return (Fragment) ARouter.getInstance()
                    .build(path)
                    .with(bundle)
                    .navigation();
        } catch (Exception e) {
            SitechDevLog.e(TAG, "");
        }
        return null;
    }

    /**
     * 携带参数跳转页面
     *
     * @param path   path目标界面对应的路径
     * @param bundle bundle参数
     */
    public void navigation(String path, Bundle bundle) {
        if (path == null || bundle == null) {
            return;
        }
        ARouter.getInstance()
                .build(path)
                .with(bundle)
                .navigation();
    }

    /**
     * 简单的跳转页面
     *
     * @param path     目标界面对应的路径
     * @param callback 监听路由过程
     */
    public void navigation(String path, Context context, SimpleNavigateCallback callback) {
        if (path == null) {
            return;
        }
        ARouter.getInstance()
                .build(path)
                .navigation(context, callback);
    }

    /**
     * 在fragment中注入
     *
     * @param fragment fragment
     */
    public static void injectFragment(Fragment fragment) {
        if (fragment == null) {
            return;
        }
        ARouter.getInstance().inject(fragment);
    }

    /**
     * 组件服务提供
     */
    public <T extends IProvider> T provide(Class<T> clz, String path) {

        if (StringUtils.isEmpty(path)) {
            return null;
        }

        IProvider provider = null;
        try {
            provider = (IProvider) ARouter.getInstance()
                    .build(path)
                    .navigation();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return (T) provider;
    }

    /**
     * 组件服务提供
     */
    public <T extends IProvider> T provide(Class<T> clz) {

        IProvider provider = null;
        try {
            provider = ARouter.getInstance().navigation(clz);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return (T) provider;
    }

    /**
     * 判断要跳转的是否是当前页面
     *
     * @param postcard 路由表
     * @return
     */
    public boolean isCurrentPage(Postcard postcard) {
        try {
            String routerName = postcard.getDestination().getSimpleName();
            String currentPageName = (ActivityUtils.getTopActivity() != null ? (ActivityUtils.getTopActivity().getClass().getSimpleName()) : "");
            SitechDevLog.i(RouterConstants.TAG, "[RouterUtil--isCurrentPage()]==>" + routerName);
            SitechDevLog.i(RouterConstants.TAG, "[RouterUtil--isCurrentPage()]==>当前页面名称==>" + currentPageName);
            if (routerName.equalsIgnoreCase(currentPageName)) {
                return true;
            }
        } catch (Exception e) {
            SitechDevLog.exception(e);
        }
        return false;
    }

    /**
     * 销毁路由资源
     */
    public void destroy() {
        ARouter.getInstance().destroy();
    }
}
