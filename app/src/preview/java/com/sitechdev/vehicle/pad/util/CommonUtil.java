package com.sitechdev.vehicle.pad.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.util.Log;
import android.view.Gravity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;

import com.blankj.utilcode.util.AdaptScreenUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.ToastUtils;
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
public class CommonUtil {

    /**
     * 在屏幕中间位置弹出Toast
     *
     * @param text
     */
    public static void showToast(String text) {
        ToastUtils.setGravity(Gravity.CENTER, 0, 0);
        ToastUtils.showShort(text);
    }

    /**
     * 在屏幕中间位置弹出Toast
     *
     * @param resId
     */
    public static void showToast(int resId) {
        ToastUtils.setGravity(Gravity.CENTER, 0, 0);
        ToastUtils.showShort(resId);
    }

}
