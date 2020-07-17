package com.sitechdev.vehicle.pad.util;

import android.app.Activity;

/**
 * 项目名称：SitechVehiclePad
 * 类名称：AppVariants
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2019/08/14 0014 17:16
 * 修改时间：
 * 备注：
 */
public class AppVariants {
    //切到后台为空
    public static Activity currentActivity = null;

    public static volatile boolean initSuccess = false;

    public static volatile boolean activeSuccess = false;
}
