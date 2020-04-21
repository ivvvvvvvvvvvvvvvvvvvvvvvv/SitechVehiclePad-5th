package com.sitechdev.vehicle.lib.util;

import android.os.Handler;

/**
 *
 * @author dell
 * @date 2017/5/19
 */

public class ForbidClickEnable {

	private final static Handler handler = new Handler();
	private static boolean isForbidClick; //是否禁止触摸 true 禁止 false 允许

	public synchronized static boolean isForbidClick(int spaceTime) {
		if (isForbidClick){
			return true;
		}else {
			isForbidClick = true;
			handler.postDelayed(runnable, spaceTime);
			return false;
		}
	}

	private static Runnable runnable = new Runnable() {
		@Override
		public void run() {
			isForbidClick = false;
		}
	};
}
