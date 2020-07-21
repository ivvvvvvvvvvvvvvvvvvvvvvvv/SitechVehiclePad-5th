package com.sitechdev.vehicle.pad.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.sitechdev.vehicle.lib.event.EventBusUtils;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.pad.app.AppApplication;
import com.sitechdev.vehicle.pad.event.SysEvent;

/**
 * @author 邵志
 * @version 2020/06/24 0024 16:25
 * @name VolumeManager
 * @description
 */
public class VolumeControlManager {
    private final static String TAG = "VolumeControlManager";
    /**
     * 当前音量返回广播key
     * 音量改变会主动发送这个广播。
     */
    private final static int RETURN_CURRENT_VOLUME = 0x78;
    /**
     * 发送给中间件的设置音量的广播key
     */
    private final static int MCU_SET_VOLUME = 0x3004;
    /**
     * 发送给中间件的查询音量的广播key
     */
    private final static int MCU_QUERY_VOLUME = 0x3005;

    /**
     * 中间件发送给APP的广播-action
     */
    private static final String MCU_SERVICE_SEND_TO_APP = "com.my.car.service.BROADCAST_CAR_SERVICE_SEND";
    /**
     * APP发送给中间件的广播-action
     */
    private static final String APP_SEND_TO_MCU_SERVICE = "com.my.car.service.BROADCAST_CMD_TO_CAR_SERVICE";
    /**
     * 中间件的包名
     */
    private static final String MCU_SERVICE_PACKAGE = "com.my.out";

    /**
     * 设备音量的最大值
     */
    private final int MAX_VOLUME_VALUE = 30;
    /**
     * 设备音量的当前值
     */
    private int currentVolumeValue = 0, volumeChangeStepValue = 3;

    private VolumeControlManager() {
    }

    private static final class SingleVolumeControlManager {
        private static final VolumeControlManager SINGLE = new VolumeControlManager();
    }

    public static VolumeControlManager getInstance() {
        return SingleVolumeControlManager.SINGLE;
    }

    /**
     * 初始化音量控制
     */
    public void init() {
        //注册广播接收器
        registerVolumeReceiver();
        //查询当前音量
        queryCurrentVolume();
    }

    /**
     * 注册音量事件广播接收
     */
    private void registerVolumeReceiver() {
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(MCU_SERVICE_SEND_TO_APP);
        AppApplication.getContext().registerReceiver(new VolumeReceiver(), iFilter);
    }

    /**
     * 音量返回值的广播接收器
     */
    private class VolumeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
//            SitechDevLog.i(TAG, "VolumeReceiver ==>" + action);
            if (MCU_SERVICE_SEND_TO_APP.equals(action)) {
                int cmd = intent.getIntExtra("cmd", 0);
//                SitechDevLog.i(TAG, "VolumeReceiver ==>cmd == " + cmd);
                switch (cmd) {
                    case RETURN_CURRENT_VOLUME:
                        currentVolumeValue = intent.getIntExtra("data", -1);
                        SitechDevLog.i(TAG, "VolumeReceiver ==> data==" + currentVolumeValue);
                        if (currentVolumeValue < 0) {
                            currentVolumeValue = 0;
                        }
                        if (currentVolumeValue > 30) {
                            currentVolumeValue = 30;
                        }
                        SitechDevLog.i(TAG, "当前音量==>" + currentVolumeValue);
                        EventBusUtils.postEvent(new SysEvent(SysEvent.EB_SYS_VOLUME_CHANGE_STATE, currentVolumeValue));
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * 返回设备音量的最大值
     *
     * @return MAX_VOLUME_VALUE=30
     */
    public int getMaxVolumeValue() {
        return MAX_VOLUME_VALUE;
    }

    /**
     * 返回设备音量的当前音量值
     *
     * @return currentVolumeValue
     */
    public int getCurrentVolumeValue() {
        return currentVolumeValue;
    }

    /**
     * 查询当前音量。如果设置音量之后，则间隔需要在20ms以上。
     * 音量范围：0-30
     */
    public void queryCurrentVolume() {
        //query at least 20 ms later after volume set
        sendToCarService(MCU_QUERY_VOLUME, 0);
    }

    /**
     * 设置音量。设置完后，会发出 SysEvent.EB_SYS_VOLUME_CHANGE_STATE 通知事件
     *
     * @param currentVolume 设置的音量值。音量范围：0-30
     */
    public void setCurrentVolume(int currentVolume) {
        if (currentVolume > 30) {
            currentVolume = 30;
        }
        if (currentVolume < 0) {
            currentVolume = 0;
        }
        currentVolumeValue = currentVolume;
        //query at least 20 ms later after volume set
        sendToCarService(MCU_SET_VOLUME, currentVolume);
        //发出 SysEvent.EB_SYS_VOLUME_CHANGE_STATE 通知事件
        EventBusUtils.postEvent(new SysEvent(SysEvent.EB_SYS_VOLUME_CHANGE_STATE, currentVolumeValue));
    }

    /**
     * 设置静音或取消
     *
     * @param enable true=静音
     */
    public void setMuteVolume(boolean enable) {
        sendToCarService(MCU_SET_VOLUME, enable ? 0 : currentVolumeValue);
        //发出 SysEvent.EB_SYS_VOLUME_CHANGE_STATE 通知事件
        EventBusUtils.postEvent(new SysEvent(SysEvent.EB_SYS_VOLUME_CHANGE_STATE, enable ? 0 : currentVolumeValue));
    }


    /**
     * 发消息给中间件
     *
     * @param cmd  cmd
     * @param data data
     */
    private void sendToCarService(int cmd, int data) {
        SitechDevLog.i(TAG, "volume==>cmd=" + cmd + ", data=" + data);
        Intent it = new Intent(APP_SEND_TO_MCU_SERVICE);
        it.putExtra("cmd", cmd);
        it.putExtra("data", data);
        it.setPackage(MCU_SERVICE_PACKAGE);
        AppApplication.getContext().sendBroadcast(it);
    }

    /**
     * 改变音量值
     *
     * @param isAddVolumeValue true=增加，false=减小
     */
    public void setCurrentVolumeValue(boolean isAddVolumeValue) {
        if (isAddVolumeValue) {
            currentVolumeValue += volumeChangeStepValue;
        } else {
            currentVolumeValue -= volumeChangeStepValue;
        }
        //设置音量值
        setCurrentVolume(currentVolumeValue);
//        try {
//            Thread.sleep(20);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        //间隔20ms查询音量值
//        queryCurrentVolume();
    }

}
