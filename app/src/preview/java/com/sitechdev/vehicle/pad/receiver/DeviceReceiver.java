package com.sitechdev.vehicle.pad.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.sitechdev.vehicle.lib.event.EventBusUtils;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.pad.event.AppSignalEvent;

import java.io.File;

/**
 * 项目名称：SitechVehiclePad-5th
 * 类名称：DeviceReceiver
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2020/05/11 0011 16:41
 * 修改时间：
 * 备注：
 */
public class DeviceReceiver extends BroadcastReceiver {
    private static final String TAG = "DeviceReceiver";
    private boolean isUsbRemove; //代表U盘刚插入，在检查的过程中已经remove掉了

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        final String path = intent.getData().getPath();
        boolean value = intent.getBooleanExtra("read-only", true);
        SitechDevLog.d(TAG, "onReceive: action=" + action + "; path=" + path + ";is read only=" + value);
        if (action.equals(Intent.ACTION_MEDIA_MOUNTED)) {
            Log.d(TAG, "onReceive: isUsbRemove==" + isUsbRemove);
            if (!isUsbRemove) {
                return;
            }
//            Log.d(TAG, "onReceive: -------1--------");
//            if (isScanDevice(path)) {
//                Log.d(TAG, "onReceive: -------2--------");
//                ThreadManager.getInstance().addTask(new Runnable() {
//                    @Override
//                    public void run() {
//                        Looper.prepare();
//                        isExistUpdateFile(path);
//                        Looper.loop();
//                    }
//                });
//                Log.d(TAG, "onReceive: -------3--------");
            if (isUsbScan(path)) {
//                    Log.d(TAG, "onReceive: post-->SysEvent.EB_SYS_USB_STATE_CHANGE 1 path=" + path);
                EventBusUtils.postEvent(new AppSignalEvent(AppSignalEvent.EVENT_SIGNAL_CHANGE_USB_STATE, true));
            }
//            }
//            Log.d(TAG, "onReceive: -------7-------");
//            //通知语音业务，上传歌曲词典
//            EventBus.getDefault().postSticky(new TeddyEvent(TeddyEvent.EB_TEDDY_EVENT_SR_MUSIC_DICT));
        } else if (action.equals(Intent.ACTION_MEDIA_SCANNER_FINISHED)) {

        } else if (action.equals(Intent.ACTION_MEDIA_EJECT)) {
            //Log.d("usbremoveusbremove", "usbremove1");
//            if (isScanDevice(path)) {
//                //Log.d("usbremoveusbremove", "usbremove11111111");
//                UsbDisconnect(context, path);
//            }
        } else if (action.equals(Intent.ACTION_MEDIA_UNMOUNTED)) {
            // 设备没有mount在文件系统上面,不可以说明其已经从卡槽移除
            //Log.d("usbremoveusbremove", "usbremove2");
//            if (isScanDevice(path)) {
//                //Log.d("usbremoveusbremove", "usbremove22222222");
//                UsbDisconnect(context, path);
//            }
        } else if (action.equals(Intent.ACTION_MEDIA_REMOVED)) {
            // 设备已经从卡槽移除
            //Log.d("usbremoveusbremove", "usbremove3");
//            if (isScanDevice(path)) {
//                //Log.d("usbremoveusbremove", "usbremove33333333");
//                UsbDisconnect(context, path);
//            }
            isUsbRemove = false;
        } else if (action.equals(Intent.ACTION_MEDIA_CHECKING)) {
            isUsbRemove = true;
        }
    }

    /**
     * 系统未收到USB设备插拔广播需进行主动扫描
     * 情况一：USB连接的情况下，应用报错重启
     * 情况一：USB连接的情况下，系统启动
     */
    private boolean isUsbScan(String strPath) {

        boolean isConnect;
        // 检测USB
        File usbCard = new File(strPath);
        File[] files = usbCard.listFiles();
        if (usbCard.exists() && (files != null) && files.length > 0) {
            isConnect = true;
        } else {
            // 非法设备插入广播
            return false;
        }
        return isConnect;
    }

    private void UsbDisconnect(Context context, String path) {
        EventBusUtils.postEvent(new AppSignalEvent(AppSignalEvent.EVENT_SIGNAL_CHANGE_USB_STATE, false));
    }
}
