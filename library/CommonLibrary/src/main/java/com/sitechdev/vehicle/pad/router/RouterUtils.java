package com.sitechdev.vehicle.pad.router;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.template.IProvider;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ActivityUtils;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.lib.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * router工具类
 *
 * @author liuhe
 * @date 2019/03/23
 */
public class RouterUtils {
    private static final String TAG = "Router";
    private List<String> classOutArray = null;

    private RouterUtils() {
        classOutArray = new ArrayList<>();
    }

    private static final class Holder {
        private static final RouterUtils INSTANCE = new RouterUtils();
    }

    public static RouterUtils getInstance() {
        return Holder.INSTANCE;
    }

    public void addOutClassName(String... outClassName) {
        if (outClassName != null && outClassName.length > 0) {
            for (int i = 0; i < outClassName.length; i++) {
                classOutArray.add(outClassName[i]);
            }
        }
    }

    public void clearOutClassList() {
        if (classOutArray != null) {
            classOutArray.clear();
        }

    }

    /**
     * ARouter初始化操作
     *
     * @param printLogs true=打印log，开启debug模式。false反之
     * @param app       当前application对象
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
    public void navigationNoFlag(String path) {
        if (path == null) {
            return;
        }
        ARouter.getInstance()
                .build(path)
                .navigation(ActivityUtils.getTopActivity());
    }

    /**
     * 应用内简单的跳转
     *
     * @param pathUri 目标界面对应的路径
     */
    public void navigationNoFlag(Uri pathUri) {
        if (pathUri == null) {
            return;
        }
        ARouter.getInstance()
                .build(pathUri)
                .navigation(ActivityUtils.getTopActivity());
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
                .withFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                .navigation(ActivityUtils.getTopActivity());
    }

    /**
     * 应用内简单的跳转,待finish参数。标识无需finish last page
     *
     * @param path 目标界面对应的路径
     */
    public void navigationNoFinish(String path) {
        if (path == null) {
            return;
        }
        ARouter.getInstance()
                .build(path)
                .withBoolean("isFinish", false)
                .withFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                .navigation(ActivityUtils.getTopActivity());
    }


    /**
     * 应用内简单的跳转
     *
     * @param pathUri 目标界面对应的路径
     */
    public void navigation(Uri pathUri) {
        if (pathUri == null) {
            return;
        }
        ARouter.getInstance()
                .build(pathUri)
                .withFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                .navigation(ActivityUtils.getTopActivity());
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
                    .navigation(ActivityUtils.getTopActivity());
        } catch (Exception e) {
            SitechDevLog.e(TAG, "");
        }
        return null;
    }

    /**
     * 构建Fragment
     *
     * @param path  fragment路径
     * @param flags 跳转所使用的N个flags
     */
    public void navigationWithFlags(String path, int... flags) {
        navigationWithFlags(path, null, flags);
    }

    /**
     * 跳往首页的单独封装
     *
     * @param homePath homePath
     */
    public void navigationHomePage(String homePath) {
        navigationWithFlags(homePath,
                Intent.FLAG_ACTIVITY_NEW_TASK, Intent.FLAG_ACTIVITY_CLEAR_TASK, Intent.FLAG_ACTIVITY_CLEAR_TOP
        );
    }

    /**
     * 构建Fragment
     *
     * @param path   fragment路径
     * @param bundle 跳转传递的bundle参数
     * @param flags  跳转所使用的N个flags
     */
    public void navigationWithFlags(String path, Bundle bundle, int... flags) {
        if (path == null) {
            return;
        }
        if (flags == null || flags.length == 0) {
            navigation(path, bundle);
            return;
        }
        Postcard postcard = ARouter.getInstance().build(path);
        for (int i = 0; i < flags.length; i++) {
            postcard.addFlags(flags[i]);
        }
        postcard.navigation();
    }

    /**
     * @param path fragment路径
     */
    public Postcard getPostcard(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        Postcard postcard = ARouter.getInstance().build(path);
        return postcard;
    }

    /**
     * 携带参数跳转页面
     *
     * @param path   path目标界面对应的路径
     * @param bundle bundle参数
     */
    public void navigation(String path, Bundle bundle) {
        if (path == null && bundle == null) {
            return;
        }
        ARouter.getInstance()
                .build(path, path.split("/")[3])
                .with(bundle)
                .navigation(ActivityUtils.getTopActivity());
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
                    .navigation(ActivityUtils.getTopActivity());
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
            SitechDevLog.i(TAG, "[RouterUtil--isCurrentPage()]==>" + routerName);
            SitechDevLog.i(TAG, "[RouterUtil--getflag()]==>" + postcard.getFlags());
            SitechDevLog.i(TAG, "[RouterUtil--isCurrentPage()]==>当前页面名称==>" + currentPageName);
            SitechDevLog.i(TAG, "[RouterUtil--currentPage()是否在例外]==>" + classOutArray.contains(routerName));
            if (routerName.equalsIgnoreCase(currentPageName) && !this.classOutArray.contains(routerName)) {
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
