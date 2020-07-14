package com.sitechdev.vehicle.pad.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.blankj.utilcode.util.ToastUtils;
import com.sitechdev.vehicle.lib.event.EventBusUtils;
import com.sitechdev.vehicle.pad.event.SysEvent;
import com.sitechdev.vehicle.pad.module.music.MusicConfig;

public class UsbReciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_MEDIA_MOUNTED.equals(intent.getAction())) {
            ToastUtils.showShort("U盘已插入！");
            MusicConfig.getInstance().setUdiskMounted(true);
            EventBusUtils.postEvent(new SysEvent(SysEvent.EB_SYS_USB_STATE,true));
        } else if (Intent.ACTION_MEDIA_EJECT.equals(intent.getAction())) {
            ToastUtils.showShort("U盘已拔出！");
            MusicConfig.getInstance().setUdiskMounted(false);
            EventBusUtils.postEvent(new SysEvent(SysEvent.EB_SYS_USB_STATE,false));
        }
    }
}
