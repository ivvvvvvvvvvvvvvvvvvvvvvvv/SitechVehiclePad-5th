package com.sitechdev.vehicle.pad.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.blankj.utilcode.util.BarUtils;
import com.kaolafm.opensdk.OpenSDK;
import com.kaolafm.opensdk.http.core.HttpCallback;
import com.kaolafm.opensdk.http.error.ApiException;
import com.kaolafm.opensdk.log.Logging;
import com.sitechdev.net.EnvironmentConfig;
import com.sitechdev.net.HttpHelper;
import com.sitechdev.vehicle.lib.util.ProcessUtil;
import com.sitechdev.vehicle.pad.BuildConfig;
import com.sitechdev.vehicle.pad.kaola.KaolaPlayManager;
import com.sitechdev.vehicle.pad.manager.AppManager;
import com.sitechdev.vehicle.pad.manager.CommonTipWindowManager;
import com.sitechdev.vehicle.pad.manager.CommonTopWindowManager;
import com.sitechdev.vehicle.pad.manager.VoiceSourceManager;
import com.sitechdev.vehicle.pad.module.map.util.MapVoiceEventUtil;
import com.sitechdev.vehicle.pad.net.interception.SitechRequestInterceptor;
import com.sitechdev.vehicle.pad.net.interception.SitechResponseInterceptor;
import com.sitechdev.vehicle.pad.util.AppVariants;
import com.sitechdev.vehicle.pad.util.BuglyHelper;
import com.sitechdev.vehicle.pad.util.MarsXlogUtil;
import com.sitechdev.vehicle.pad.util.ParamsUtil;
import com.sitechdev.vehicle.pad.utils.MyEventBusIndex;
import com.sitechdev.vehicle.pad.window.manager.RightTopWindowManager;

import org.greenrobot.eventbus.EventBus;

/**
 * 项目名称：SitechVehiclePad
 * 类名称：AppApplication
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2019/08/09 0009 20:06
 * 修改时间：
 * 备注：
 */
public class AppApplication extends Application {

    private static AppApplication mApplication = null;

    @Override
    public void onCreate() {
        super.onCreate();
        if (!ProcessUtil.isMainProcess(this)) {
            return;
        }
        mApplication = this;
        try {
            EventBus.builder().addIndex(new MyEventBusIndex()).installDefaultEventBus();
        } catch (Exception e) {
            e.printStackTrace();
        }
        AppManager.getInstance().init(this);
        //腾讯相关组件
        initTecentUtil();
        //网络
        initNet();
        //地图事件注册
        MapVoiceEventUtil.getInstance().init();
        //bugly
        BuglyHelper.getInstance().initCrashReport(this);
        //window窗口
        initCustomWindow();
        //Activity 页面管理
        initLifecleActivity();
        //考拉SDK
        initKaolaSdk();
    }

    private void initCustomWindow() {
        //右上角状态window
        RightTopWindowManager.getInstance().init(this);
        //登录、普通Toast弹窗
        CommonTipWindowManager.getInstance().init(this);
    }

    private void initTecentUtil() {
        //日志组件
        MarsXlogUtil.initXlog();
        //数据存储组件
        ParamsUtil.init();
    }

    /**
     * 网络模块初始化
     */
    private void initNet() {
        EnvironmentConfig.init(!BuildConfig.DEBUG);
        //自定义拦截器
        HttpHelper.getInstance().addNetInterceptor(new SitechRequestInterceptor(), new SitechResponseInterceptor());
        //初始化网络组件
        HttpHelper.getInstance().initNet(this, BuildConfig.DEBUG);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static Context getContext() {
        return mApplication.getApplicationContext();
    }

    /**
     * Activity 页面管理
     */
    private void initLifecleActivity() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                AppVariants.currentActivity = activity;
            }

            @Override
            public void onActivityStarted(Activity activity) {
                AppVariants.currentActivity = activity;
            }

            @Override
            public void onActivityResumed(Activity activity) {
                AppVariants.currentActivity = activity;
            }

            @Override
            public void onActivityPaused(Activity activity) {
                if (AppVariants.currentActivity == activity) {
                    AppVariants.currentActivity = null;
                }
            }

            @Override
            public void onActivityStopped(Activity activity) {
                if (AppVariants.currentActivity == activity) {
                    AppVariants.currentActivity = null;
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                if (AppVariants.currentActivity == activity) {
                    AppVariants.currentActivity = null;
                }
            }
        });
    }

    /**
     * 考拉SDK
     */
    private void initKaolaSdk() {
        Logging.setDebug(true);
        OpenSDK.getInstance().initSDK(this, new HttpCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean isSuccess) {
                Log.i("考拉APPDemo", isSuccess ? "初始化SDK成功" : "初始化SDK失败");
                AppVariants.initSuccess = isSuccess;
                if (isSuccess) {
                    KaolaPlayManager.SingletonHolder.INSTANCE.activeKaola();
                }
            }

            @Override
            public void onError(ApiException exception) {
                Log.w("考拉APPDemo", "初始化SDK失败，错误码=" + exception.getCode() + ",错误信息=" + exception.getMessage());
            }
        });
    }


}
