package com.sitechdev.vehicle.pad.util;

import android.view.Gravity;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;

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

    public static String formatNumber(String phone) {
        if (StringUtils.isEmpty(phone)) {
            return phone;
        }

        if (phone.length() < 11) {
            return phone;
        }

        return phone.substring(0, 3) + "****" + phone.substring(7, phone.length());
    }

}
