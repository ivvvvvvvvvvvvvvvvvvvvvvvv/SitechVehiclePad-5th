package com.sitechdev.vehicle.lib.util;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 *
 * @author dell
 * @date 2017/2/7
 */
public class ServiceUtils {

	/**
	 * 判断服务是否运行
	 *
	 * @param context   上下文
	 * @param className 完整包名的服务类名
	 * @return {@code true}: 是<br>{@code false}: 否
	 */
	public static boolean isServiceRunning(Context context, String className) {
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> infos = activityManager.getRunningServices(0x7FFFFFFF);
		if (infos == null || infos.size() == 0) return false;
		for (ActivityManager.RunningServiceInfo info : infos) {
			if (className.equals(info.service.getClassName())) return true;
		}
		return false;
	}
}
