package com.sitechdev.vehicle.pad.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;

import com.blankj.utilcode.util.AdaptScreenUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.lzy.okgo.OkGo;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.pad.app.AppConst;
import com.sitechdev.vehicle.pad.manager.UserManager;
import com.sitechdev.vehicle.pad.module.login.LoginActivity;
import com.sitechdev.vehicle.pad.view.CommonToast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * 项目名称：SitechVehiclePad
 * 类名称：AppUtil
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2019/08/14 0014 16:35
 * 修改时间：
 * 备注：
 */
public class AppUtil {

    public static AnimationSet getAnimationSet() {
        AnimationSet animationSet = new AnimationSet(true);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0.5f, 0f);
        alphaAnimation.setDuration(2000);
        // 设置不断重复
        alphaAnimation.setRepeatCount(Animation.INFINITE);

        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 3.0f, 1.0f, 3.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        );
        scaleAnimation.setDuration(2000);
        // 设置不断重复
        scaleAnimation.setRepeatCount(Animation.INFINITE);

        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.setInterpolator(new LinearInterpolator());

        return animationSet;
    }

    /**
     * 前去重新登录
     */
    public static void gotoReLogin() {
        try {
            if (isActivity(LoginActivity.class)) {
                //如果当前已经在登录相关页面，则不做处理
                return;
            }
            //跳转登录页面
            AppUtil.startReLoginView();

            //做清除上一次登录缓存的操作 、退出登录的操作
            cleanLoginUserData();

            //终止所有的请求
            OkGo.getInstance().cancelAll();

        } catch (Exception e) {
            SitechDevLog.exception(e);
        }
    }

    /**
     * 清除用户缓存
     */
    private static void cleanLoginUserData() {
        //用户退出
        UserManager.getInstance().logoutUser();
    }

    /**
     * 是否是登录界面
     *
     * @return
     */
    public static boolean isActivity(Class classObject) {
        if (AppVariants.currentActivity == null) {
            return false;
        }
        return (AppVariants.currentActivity.getClass() == classObject);
    }

    /**
     * 跳往登录页面
     */
    public static void startReLoginView() {
        if (AppVariants.currentActivity == null) {
            SitechDevLog.i("tmpActivity", "AppVariants.currentActivity===>null");
            return;
        }
        Intent mIntent = new Intent();
        mIntent.setClass(AppVariants.currentActivity, LoginActivity.class);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        AppVariants.currentActivity.startActivity(mIntent);
    }

    public static void goOtherActivity(Context context, String appName, String pckName, String clsName) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        ComponentName componentName = new ComponentName(
                pckName, clsName);
        intent.setComponent(componentName);
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(intent, PackageManager.GET_RESOLVED_FILTER);
        if (null != resolveInfos && resolveInfos.size() > 0) {
            context.startActivity(intent);
        } else {
            CommonToast.showToast("您还未安装" + appName + ",请先安装");
        }
    }

    /**
     * 复制
     *
     * @param old
     * @return
     */
    public static Object copyObject(Object old) {
        Object clazz = null;
        try {
            // 写入字节流
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(old);
            // 读取字节流
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            clazz = (Object) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return clazz;
    }

    /**
     * 获取屏幕适配的宽高
     *
     * @param currentResources
     * @return
     */
    public static Resources getCurrentResource(Resources currentResources) {
        Log.i("AppUtil", "getResources=======" + ScreenUtils.isLandscape() + "=====>" + currentResources);
        if (ScreenUtils.isLandscape()) {
//            Log.i(this.getClass().getSimpleName(), "横屏");
            return AdaptScreenUtils.adaptWidth(currentResources, AppConst.LAND_SCAPE_DESIGN_WIDTH);
        } else {
//            Log.i(this.getClass().getSimpleName(), "竖屏");
//            return AdaptScreenUtils.adaptWidth(super.getResources(), 800);
            return AdaptScreenUtils.adaptWidth(currentResources, AppConst.ORI_SCAPE_DESIGN_WIDTH);
        }
    }
}
