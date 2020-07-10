package com.sitechdev.vehicle.pad.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.Nullable;

import com.sitechdev.vehicle.lib.event.EventBusUtils;
import com.sitechdev.vehicle.lib.util.NetworkUtils;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.lib.util.ThreadUtils;
import com.sitechdev.vehicle.pad.app.AppApplication;
import com.sitechdev.vehicle.pad.event.AppSignalEvent;

import java.util.concurrent.TimeUnit;

/**
 * 项目名称：SitechVehiclePad-5th
 * 类名称：NetReceiver
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2020/05/11 0011 14:56
 * 修改时间：
 * 备注：
 */
public class NetReceiver extends BroadcastReceiver {
    private static final String TAG = "NetReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        // 监听网络连接，包括wifi和移动数据的打开和关闭,以及连接上可用的连接都会接到监听
        SitechDevLog.e(TAG, "net status->" + (null == intent ? "wifi changed" : intent.getAction()));
        SitechDevLog.e(TAG, "net status->" + NetworkUtils.isNetworkAvailable(AppApplication.getContext()));
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            //获取联网状态的NetworkInfo对象
            NetworkInfo info = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            if (info != null) {
                //如果当前的网络连接成功并且网络连接可用
                if (NetworkInfo.State.CONNECTED == info.getState() && info.isAvailable()) {
                    pingNet();
                    SitechDevLog.e(TAG, "网络链接状态->" + NetworkUtils.isNetworkAvailable(AppApplication.getContext()));
                    if (info.getType() == ConnectivityManager.TYPE_WIFI
                            || info.getType() == ConnectivityManager.TYPE_MOBILE) {
                        EventBusUtils.postEvent(new AppSignalEvent(AppSignalEvent.EVENT_SIGNAL_CHANGE_WIFI_STATE, true));
                    }
                } else {
                    EventBusUtils.postEvent(new AppSignalEvent(AppSignalEvent.EVENT_SIGNAL_CHANGE_WIFI_STATE, false));
                }
            }
        } else if (WifiManager.RSSI_CHANGED_ACTION.equals(intent.getAction())) {
            WifiManager wifiManager = NetworkUtils.getWifiService();
            WifiInfo info = wifiManager.getConnectionInfo();
            if (info != null && info.getBSSID() != null) {
//                DataFactory.produceMemData().getDeviceState().wifiRssi = info.getRssi();
            }
        }
    }

    /**
     * 判断网络是否可用
     */
    private void pingNet() {
        ThreadUtils.executeByCachedAtFixRate(new ThreadUtils.SimpleTask<Boolean>() {
            @Nullable
            @Override
            public Boolean doInBackground() {
                return NetworkUtils.isAvailableByPing();
            }

            @Override
            public void onCancel() {
                super.onCancel();
                SitechDevLog.d(TAG, "cancel this task!");
            }

            @Override
            public void onSuccess(@Nullable Boolean netAvailable) {
                super.onSuccess(netAvailable);
                if (netAvailable) {
                    cancel();
                }
            }
        }, 30, TimeUnit.SECONDS);
    }
}
