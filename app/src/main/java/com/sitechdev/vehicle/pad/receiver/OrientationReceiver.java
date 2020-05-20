package com.sitechdev.vehicle.pad.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.blankj.utilcode.util.ScreenUtils;
import com.sitechdev.vehicle.lib.event.EventBusUtils;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.pad.event.ScreenEvent;

/**
 * 项目名称：SitechVehiclePad-5th
 * 类名称：OrientationReceiver
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2020/05/20 0020 16:20
 * 修改时间：
 * 备注：
 */
public class OrientationReceiver extends BroadcastReceiver {
    private static final String TAG = OrientationReceiver.class.getSimpleName();

    /**
     * @param context The Context in which the receiver is running.
     * @param intent  The Intent being received.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.CONFIGURATION_CHANGED".equals(intent.getAction())) {
            if (ScreenUtils.isLandscape()) {
                SitechDevLog.i(TAG, "OrientationReceiver============横屏");
                EventBusUtils.postEvent(new ScreenEvent(ScreenEvent.EVENT_SCREEN_ORIENTATION_CHANGE, true));
            } else {
                SitechDevLog.i(TAG, "OrientationReceiver============竖屏");
                EventBusUtils.postEvent(new ScreenEvent(ScreenEvent.EVENT_SCREEN_ORIENTATION_CHANGE, false));
            }
        }
    }
}
