package com.sitechdev.vehicle.pad.manager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;

import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.pad.app.AppApplication;

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

    private NetManagerImpl() {
        networkCallback = new NetworkCallbackImpl();
        builder = new NetworkRequest.Builder();
        request = builder.addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .build();
        connectivityManager = (ConnectivityManager) AppApplication.getContext().
                getSystemService(Context.CONNECTIVITY_SERVICE);
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
        @Override
        public void onAvailable(Network network) {
            super.onAvailable(network);
            SitechDevLog.i(TAG, "NetworkCallbackImpl=====>onAvailable");
        }

        @Override
        public void onLosing(Network network, int maxMsToLive) {
            super.onLosing(network, maxMsToLive);
            SitechDevLog.i(TAG, "NetworkCallbackImpl=====>onLosing==maxMsToLive=>" + maxMsToLive);
        }

        /**
         * 网络断开时，回调
         *
         * @param network network
         */
        @Override
        public void onLost(Network network) {
            super.onLost(network);
            SitechDevLog.i(TAG, "NetworkCallbackImpl=====>onLost");
        }

        @Override
        public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
            super.onCapabilitiesChanged(network, networkCapabilities);
            SitechDevLog.i(TAG, "NetworkCallbackImpl=====>onCapabilitiesChanged=network=>" + network.toString());
            //[ Transports: WIFI Capabilities: NOT_METERED&INTERNET&NOT_RESTRICTED&TRUSTED&NOT_VPN&FOREGROUND LinkUpBandwidth>=1048576Kbps LinkDnBandwidth>=1048576Kbps SignalStrength: -57]
            SitechDevLog.i(TAG, "NetworkCallbackImpl=====>onCapabilitiesChanged2222==" + networkCapabilities.toString());
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                //WIFI链接
                SitechDevLog.i(TAG, "NetworkCallbackImpl=====>onCapabilitiesChanged3333==WIFI");
            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                //蜂窝网络,2G/3G/4G
                SitechDevLog.i(TAG, "NetworkCallbackImpl=====>onCapabilitiesChanged3333==蜂窝网络,2G/3G/4G");
            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                //以太网网络
                SitechDevLog.i(TAG, "NetworkCallbackImpl=====>onCapabilitiesChanged3333==以太网网络");
            }
        }

        @Override
        public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
            super.onLinkPropertiesChanged(network, linkProperties);
            SitechDevLog.i(TAG, "NetworkCallbackImpl=====>onLinkPropertiesChanged==>" + linkProperties.getInterfaceName());
            SitechDevLog.i(TAG, "NetworkCallbackImpl=====>onLinkPropertiesChanged2222==>" + linkProperties.toString());
        }
    }

    public void unregisterCallback() {
        if (connectivityManager != null) {
            connectivityManager.unregisterNetworkCallback(networkCallback);
        }
    }
}
