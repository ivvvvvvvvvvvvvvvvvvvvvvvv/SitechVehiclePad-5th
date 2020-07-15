package com.sitechdev.vehicle.pad.module.setting.wifi;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.sitechdev.vehicle.lib.event.EventBusUtils;
import com.sitechdev.vehicle.lib.util.NetworkUtils;
import com.sitechdev.vehicle.pad.event.SysEvent;
import com.sitechdev.vehicle.pad.view.CustomSwitchButton;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by DELL on 2017/7/27.
 */

public class WiFiEnabler implements CustomSwitchButton.OnSwitchCheckChangeListener {

    public static final String TAG = WiFiEnabler.class.getSimpleName();

    private final Context mContext;
    private CustomSwitchButton mSwitch;
    private AtomicBoolean mConnected = new AtomicBoolean(false);

    private final WifiManager mWifiManager;
    private boolean mStateMachineEvent;
    private final IntentFilter mIntentFilter;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)) {
                handleWifiStateChanged(intent.getIntExtra(
                        WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN));
            } else if (WifiManager.SUPPLICANT_STATE_CHANGED_ACTION.equals(action)) {
                if (!mConnected.get()) {
                    handleStateChanged(WifiInfo.getDetailedStateOf((SupplicantState)
                            intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE)));
                }
            } else if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(action)) {
                NetworkInfo info = (NetworkInfo) intent.getParcelableExtra(
                        WifiManager.EXTRA_NETWORK_INFO);
                mConnected.set(info.isConnected());
                handleStateChanged(info.getDetailedState());
            }
        }
    };

    public WiFiEnabler(Context context, CustomSwitchButton switch_) {
        mContext = context;
        mSwitch = switch_;

        mWifiManager = NetworkUtils.getWifiService();
        mIntentFilter = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
        // The order matters! We really should not depend on this. :(
        mIntentFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
    }

    public void resume() {
        // Wi-Fi state is sticky, so just let the receiver update UI
        mContext.registerReceiver(mReceiver, mIntentFilter);
        mSwitch.setOnSwitchChangeListener(this);
    }

    public void pause() {
        try {
            mContext.unregisterReceiver(mReceiver);
            mSwitch.setOnSwitchChangeListener(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSwitch(CustomSwitchButton switch_) {
        if (mSwitch == switch_) {
            return;
        }
        mSwitch.setOnSwitchChangeListener(null);
        mSwitch = switch_;
        mSwitch.setOnSwitchChangeListener(this);

        final int wifiState = mWifiManager.getWifiState();
        boolean isEnabled = wifiState == WifiManager.WIFI_STATE_ENABLED;
        boolean isDisabled = wifiState == WifiManager.WIFI_STATE_DISABLED;
//        mSwitch.setChecked(isEnabled);
        mSwitchSetChecked(isEnabled);
//        mSwitch.setEnabled(isEnabled || isDisabled);
        mSwitchSetEnabled(isEnabled || isDisabled);
    }

    private void handleWifiStateChanged(int state) {
        switch (state) {
            case WifiManager.WIFI_STATE_ENABLING:
                mSwitchSetEnabled(false);
                break;
            case WifiManager.WIFI_STATE_ENABLED:
                setSwitchChecked(true);
                mSwitchSetEnabled(true);
                break;
            case WifiManager.WIFI_STATE_DISABLING:
                mSwitchSetEnabled(false);
                break;
            case WifiManager.WIFI_STATE_DISABLED:
                setSwitchChecked(false);
                mSwitchSetEnabled(true);
                break;
            default:
                setSwitchChecked(false);
                mSwitchSetEnabled(true);
                break;
        }
    }

    private void setSwitchChecked(boolean checked) {
        if (checked != mSwitch.isChecked()) {
            mStateMachineEvent = true;
//            mSwitch.setChecked(checked);
            mSwitchSetChecked(checked);
            mStateMachineEvent = false;
        }
    }

    private void handleStateChanged(@SuppressWarnings("unused") NetworkInfo.DetailedState state) {
         /*After the refactoring from a CheckBoxPreference to a Switch, this method is useless since
         there is nowhere to display a summary.
         This code is kept in case a future change re-introduces an associated text.
         WifiInfo is valid if and only if Wi-Fi is enabled.
         Here we use the state of the switch as an optimization.*/
        if (state != null && mSwitch.isChecked()) {
            WifiInfo info = mWifiManager.getConnectionInfo();
            if (info != null) {
                if (callBack != null) {
                    callBack.refresh(info.getSSID(), state);
                }
            }
        }
    }


    private void mSwitchSetEnabled(boolean isEnabled) {
        mSwitch.setEnabled(isEnabled);
        if (callBack != null) {
            callBack.switchEnabledListener(isEnabled);
        }
    }

    private void mSwitchSetChecked(boolean isChecked) {
        mSwitch.setChecked(isChecked);
        if (callBack != null) {
            callBack.switchCheckedListener(isChecked);
        }
    }


    private CallBack callBack;

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public void onSwithChecked(int viewId, boolean isChecked) {
        //Do nothing if called as a result of a state machine event
        if (mStateMachineEvent) {
            return;
        }
        mSwitchSetChecked(isChecked);

        /*Show toast message if Wi-Fi is not allowed in airplane mode
        判断是否在飞行模式中WiFi是不允许的
        if (isChecked && !WirelessSettings.isRadioAllowed(mContext, Settings.Global.RADIO_WIFI)) {
            Toast.makeText(mContext, R.string.wifi_in_airplane_mode, Toast.LENGTH_SHORT).show();
            // Reset switch to off. No infinite check/listenenr loop.
            buttonView.setChecked(false);
            return;
        }

        Disable tethering if enabling Wifi
        如果启用无线禁用拘束
        int wifiApState = mWifiManager.getWifiApState();
        if (isChecked && ((wifiApState == WifiManager.WIFI_AP_STATE_ENABLING) ||
                (wifiApState == WifiManager.WIFI_AP_STATE_ENABLED))) {
            mWifiManager.setWifiApEnabled(null, false);
        }*/

        mSwitchSetEnabled(false);


//        mWifiManager.setWifiEnabled(isChecked);
        Log.e("wifiwifi", "WiFiEnabler open wifi + isChecked==" + isChecked);
        if (!mWifiManager.setWifiEnabled(isChecked)) {

            // Error
            mSwitchSetEnabled(true);
//            Toast.makeText(mContext, "R.string.wifi_error", Toast.LENGTH_SHORT).show();
        }
        EventBusUtils.postEvent(new SysEvent(SysEvent.EB_SYS_WIFI_SWITCH_STATE, isChecked));
    }

    public interface CallBack {
        void refresh(String SSID, NetworkInfo.DetailedState state);

        void switchCheckedListener(boolean isCheck);

        void switchEnabledListener(boolean isEnabled);
    }
}
