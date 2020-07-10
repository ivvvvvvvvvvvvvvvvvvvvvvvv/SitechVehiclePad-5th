package com.sitechdev.vehicle.pad.manager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;

import com.sitechdev.vehicle.lib.event.EventBusUtils;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.lib.util.StringUtils;
import com.sitechdev.vehicle.pad.app.AppApplication;
import com.sitechdev.vehicle.pad.event.SysEvent;

import java.util.HashMap;

/**
 * 项目名称：SitechVehiclePad-5th
 * 类名称：NetManagerImpl
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2020/05/11 0011 15:04
 * 修改时间：
 * 备注：
 */
public class NetManagerImpl {
    private static final String TAG = "NetManagerImpl";
    private NetworkCallbackImpl networkCallback = null;
    private NetworkRequest.Builder builder = null;
    private NetworkRequest request = null;
    private ConnectivityManager connectivityManager = null;

    private HashMap<Network, String> mNetTypeConnectMap = null;

    private NetManagerImpl() {
        networkCallback = new NetworkCallbackImpl();
        builder = new NetworkRequest.Builder();
        request = builder.addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .build();
        connectivityManager = (ConnectivityManager) AppApplication.getContext().
                getSystemService(Context.CONNECTIVITY_SERVICE);
        mNetTypeConnectMap = new HashMap<>();
    }

    private static final class Single {
        private static final NetManagerImpl SINGLE = new NetManagerImpl();
    }

    public static NetManagerImpl getInstance() {
        return Single.SINGLE;
    }

    /**
     * 初始化网络回调
     */
    public void initNetCallback() {
        connectivityManager.registerNetworkCallback(request, networkCallback);
    }

    private class NetworkCallbackImpl extends ConnectivityManager.NetworkCallback {
        private Network currentNetWork = null;

        @Override
        public void onAvailable(Network network) {
            super.onAvailable(network);
            currentNetWork = network;
            SitechDevLog.i(TAG, "NetworkCallbackImpl=== 网络已连接==>onAvailable==" + currentNetWork);
//            mNetTypeConnectMap.put(currentNetWork.toString(), true);
        }

        /**
         * 网络断开时，回调
         *
         * @param network network
         */
        @Override
        public void onLost(Network network) {
            super.onLost(network);
            SitechDevLog.i(TAG, "NetworkCallbackImpl=====>onLost==>" + network);
            String type = mNetTypeConnectMap.get(network);
            if (StringUtils.isEmpty(type)) {
                return;
            }
            switch (type) {
                case "TRANSPORT_WIFI":
                    //WIFI网络断开连接
                    EventBusUtils.postEvent(new SysEvent(SysEvent.EB_SYS_WIFI_STATE, false));
                    break;
                case "TRANSPORT_CELLULAR":
                    //蜂窝网络,2G/3G/4G--断开连接
                    EventBusUtils.postEvent(new SysEvent(SysEvent.EB_SYS_MOBILE_NET_SWITCH_STATE, false));
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
            super.onCapabilitiesChanged(network, networkCapabilities);
            if (!network.toString().equalsIgnoreCase(currentNetWork.toString())) {
                return;
            }
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                mNetTypeConnectMap.put(currentNetWork, "TRANSPORT_WIFI");
                //WIFI网络已连接
                EventBusUtils.postEvent(new SysEvent(SysEvent.EB_SYS_WIFI_STATE, true));
            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                mNetTypeConnectMap.put(currentNetWork, "TRANSPORT_CELLULAR");
                //蜂窝网络,2G/3G/4G=已连接
                EventBusUtils.postEvent(new SysEvent(SysEvent.EB_SYS_MOBILE_NET_SWITCH_STATE, true));
            }
        }

        @Override
        public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
            super.onLinkPropertiesChanged(network, linkProperties);
//            SitechDevLog.i(TAG, "NetworkCallbackImpl=====>onLinkPropertiesChanged==>" + linkProperties.getInterfaceName());
//            SitechDevLog.i(TAG, "NetworkCallbackImpl=====>onLinkPropertiesChanged2222==>" + linkProperties.toString());
        }
    }

    public void unregisterCallback() {
        if (connectivityManager != null) {
            connectivityManager.unregisterNetworkCallback(networkCallback);
        }
    }
}
