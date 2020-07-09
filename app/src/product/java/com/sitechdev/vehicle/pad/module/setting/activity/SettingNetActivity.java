package com.sitechdev.vehicle.pad.module.setting.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.sitechdev.vehicle.lib.util.Assist;
import com.sitechdev.vehicle.lib.util.ForbidClickEnable;
import com.sitechdev.vehicle.lib.util.NetworkUtils;
import com.sitechdev.vehicle.lib.util.SPUtils;
import com.sitechdev.vehicle.lib.util.ThreadManager;
import com.sitechdev.vehicle.lib.util.ToastUtils;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.AppApplication;
import com.sitechdev.vehicle.pad.app.AppConst;
import com.sitechdev.vehicle.pad.app.BaseActivity;
import com.sitechdev.vehicle.pad.manager.trace.TraceManager;
import com.sitechdev.vehicle.pad.module.setting.SettingConstant;
import com.sitechdev.vehicle.pad.module.setting.SettingTraceEnum;
import com.sitechdev.vehicle.pad.module.setting.wifi.WiFiDialog;
import com.sitechdev.vehicle.pad.module.setting.wifi.WiFiEnabler;
import com.sitechdev.vehicle.pad.module.setting.wifi.WiFiListAdapter;
import com.sitechdev.vehicle.pad.util.PermissionHelper;
import com.sitechdev.vehicle.pad.view.CustomSwitchButton;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.net.NetworkInfo.DetailedState.CONNECTED;
import static android.net.NetworkInfo.DetailedState.DISCONNECTED;
import static android.net.NetworkInfo.DetailedState.OBTAINING_IPADDR;


public class SettingNetActivity extends BaseActivity implements View.OnClickListener,
        WiFiEnabler.CallBack,
        WiFiListAdapter.OnRecyclerViewItemClickListener,
        CustomSwitchButton.OnSwitchCheckChangeListener {
    public static final String TAG = SettingNetActivity.class.getName();
    private TextView wifiHnit;
    private boolean isHotdotOpen;
    private NetworkInfo.DetailedState state;
    private CustomSwitchButton wifiSwitch, gprsSwitch;
    private RecyclerView wifiRecyclerView;
    private WiFiListAdapter wiFiListAdapter;
    private WifiManager mWifiManager;
    private WiFiEnabler wiFiEnabler;
    private Scanner mScanner;
    private IntentFilter mFilter;
    private WiFiDialog wiFiDialog;
    private RefreshLayout refreshLayout;
    private RelativeLayout recyclerRLayout;

    private Handler mHandler = new Handler();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting_net;
    }

    @Override
    protected void initData() {
        mScanner = new Scanner(this);
        wiFiEnabler = new WiFiEnabler(this, wifiSwitch);
        mWifiManager = NetworkUtils.getWifiService();
        List<ScanResult> wifiResultList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        wiFiListAdapter = new WiFiListAdapter(this, wifiResultList, mWifiManager);
        wifiRecyclerView.setLayoutManager(linearLayoutManager);
        wifiRecyclerView.setDrawingCacheEnabled(true);
        wifiRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        wifiRecyclerView.setAdapter(wiFiListAdapter);
        boolean hotSwitch = false;//TboxHotSpotBean.getInstance().isHotSpotStatus();
        if (!hotSwitch) {
            wifiSwitch.setChecked(mWifiManager.isWifiEnabled());
        }
        Log.d(TAG, "hotSwitch：" + hotSwitch);

        isHotdotOpen = hotSwitch;

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        initView();
    }

    private void initView() {
        wifiSwitch = findViewById(R.id.setting_net_wifi_swith);
        gprsSwitch = findViewById(R.id.setting_net_gprs_swith);
        wifiRecyclerView = findViewById(R.id.wifi_recyclerView);
        wifiHnit = wifiSwitch.getTipTextView();
        refreshLayout = findViewById(R.id.smartrefresh);
        recyclerRLayout = findViewById(R.id.rl_recycler);
        ((TextView) findViewById(R.id.tv_sub_title)).setText("网络设置");
    }

    @Override
    protected void initListener() {
        super.initListener();
        gprsSwitch.setOnSwitchChangeListener(this);
        wiFiListAdapter.setOnItemClickListener(this);
        wiFiEnabler.setCallBack(this);

        findViewById(R.id.iv_sub_back).setOnClickListener(this);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                mWifiManager.startScan();
            }
        });


        wiFiEnabler.resume();

        mFilter = new IntentFilter();
        //wifi状态改变的action
        mFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        //WiFi扫描到附近可用WiFi时的广播
        mFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        mFilter.addAction(WifiManager.NETWORK_IDS_CHANGED_ACTION);
        mFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
        mFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        mFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);
        registerReceiver(mReceiver, mFilter);

        mHandler.postDelayed(() -> {
            if (mWifiManager.isWifiEnabled()) {
                Log.d(TAG, "onResume start getScanResults..");
                getScanResults();
            }
        }, 1000);
    }

    @Override
    public void onSwithChecked(int viewId, boolean isChecked) {
        if (viewId == R.id.setting_net_gprs_swith) {
            NetworkUtils.setMobileDataEnabled(isChecked);
        }
    }

    class HotdotChangeListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            //1s不能点击
            Assist.with().limits(mHandler, 1000, compoundButton);

            if (b) {
                if (mWifiManager.isWifiEnabled()) {
                    mWifiManager.setWifiEnabled(false);
                }
                isHotdotOpen = true;
//                TboxHotSpotBean.getInstance().controlTboxHotspot(true, tv_Hotspot_Name.getText
//                ().toString());
            } else {
                isHotdotOpen = false;
//                TboxHotSpotBean.getInstance().controlTboxHotspot(false);
            }

            String point = isHotdotOpen ? SettingTraceEnum.BTN_SETW_HOT_ON.getPoint() :
                    SettingTraceEnum.BTN_SETW_HOT_OFF.getPoint();
            TraceManager.getInstance().traceClick(SettingNetActivity.class, point);
        }
    }

    //**********************

    @Override
    public void onClick(View v) {
        //添加
        switch (v.getId()) {
            case R.id.iv_sub_back:
                finish();
                break;
            default:
                break;
        }
        //******************************
    }

    @Override
    public void refresh(String SSID, NetworkInfo.DetailedState state) {
        Log.d(TAG, "refresh " + SSID + " " + state);
        if (wiFiListAdapter != null) {
            wiFiListAdapter.refresh(SSID, state);
        }
        //添加
        if (state == OBTAINING_IPADDR || state == CONNECTED) {
        }
    }

    @Override
    public void switchCheckedListener(boolean isCheck) {


        SPUtils.putValue(AppApplication.getContext(), SettingConstant.key_wifi_switch, isCheck);

        if (!isCheck) {
            SPUtils.putValue(this, SettingConstant.KEY_HOT_SWITCH, false);
        }
        Log.e("ling", "wifi is open: " + isCheck);
    }

    @Override
    public void switchEnabledListener(boolean isEnabled) {
        wifiSwitch.setAlpha(isEnabled ? 1 : (float) 0.5);
    }

    @Override
    public void onItemClick(View view, int position, NetworkInfo.DetailedState indexState) {
        //添加
        if (indexState == CONNECTED) {
//            tv_connect.setVisibility(View.VISIBLE);
        } else {
//            tv_connect.setVisibility(View.GONE);
        }
//        iv_replace.setImageResource(R.drawable.wifi_on_big);
//        tv_hot_dot.setVisibility(View.GONE);
        //************
        if (ForbidClickEnable.isForbidClick(700)) {
            return;
        }
        if (view == null) {
            Log.e(TAG, "WiFiFragment onItemClick: view is null");
        }

//        TraceManager.getInstance().traceClick(SettingNetActivity.class,SettingTraceEnum
//        .BTN_SETW_WIFI_TRY_CONNECT.getPoint());

        final ScanResult scanResult = wiFiListAdapter.getData().get(position);
        final int configuredNetworkPosition = isClickSaveItem(scanResult);
        if (configuredNetworkPosition != -1) {//点击的是已保存的wifi
            if (isClickConnectItem(scanResult)) {//点击已保存已经连接的wifi
                Log.d(TAG, "onItemClick:点击已保存已经连接的wifi ");
                wiFiDialog = new WiFiDialog(this, scanResult.SSID, "已连接", getLevel(scanResult),
                        true, 1);
                wiFiDialog.show();
                wiFiDialog.setCallBack(new WiFiDialog.CallBack() {
                    @Override
                    public void onSureListener() {
                    }

                    @Override
                    public void onCancelListener() {
                        wiFiDialog.dismiss();
                    }

                    @Override
                    public void onCancelSave() {
                        removeNetwork(mWifiManager.getConfiguredNetworks().get(configuredNetworkPosition).networkId);
                        wiFiListAdapter.refresh(scanResult.SSID, DISCONNECTED);
                        wiFiDialog.dismiss();

                        //添加
//                        tv_connect.setVisibility(View.GONE);
                        state = DISCONNECTED;
                        //****************
                    }

                    @Override
                    public void onInterruptOrConnectListener() {
                        WifiInfo connectionInfo = mWifiManager.getConnectionInfo();
                        if (connectionInfo != null) {
                            int networkId = connectionInfo.getNetworkId();
                            disconnectWifi(networkId);
                        }
                        wiFiDialog.dismiss();
                        //添加
//                        tv_connect.setVisibility(View.GONE);
                        state = DISCONNECTED;
                        //****************
                    }
                });
            } else {//点击已保存未连接的wifi
                Log.d(TAG, "onItemClick:点击已保存未连接的wifi ");
                wiFiDialog = new WiFiDialog(this, scanResult.SSID, "未连接", getLevel(scanResult),
                        false, 1);
                wiFiDialog.show();
                wiFiDialog.setCallBack(new WiFiDialog.CallBack() {
                    @Override
                    public void onSureListener() {
                    }

                    @Override
                    public void onCancelListener() {
                        wiFiDialog.dismiss();
                    }

                    @Override
                    public void onCancelSave() {
                        if (mWifiManager != null && mWifiManager.getConfiguredNetworks() != null &&
                                configuredNetworkPosition <= mWifiManager.getConfiguredNetworks().size() - 1 &&
                                0 <= configuredNetworkPosition) {
                            removeNetwork(mWifiManager.getConfiguredNetworks().get(configuredNetworkPosition).networkId);
                            wiFiListAdapter.refresh(scanResult.SSID, DISCONNECTED);
                        }
                        wiFiDialog.dismiss();
                    }

                    @Override
                    public void onInterruptOrConnectListener() {
                        connectConfiguration(configuredNetworkPosition);
                        wiFiDialog.dismiss();
                        TraceManager.getInstance().traceClick(SettingNetActivity.class,
                                SettingTraceEnum.BTN_SETW_WIFI_TRY_CONNECT.getPoint());
                    }
                });

            }
        } else {//点击的是未保存的wifi
            Log.d(TAG, "onItemClick:点击的是未保存的wifi ");
            if (scanResult.capabilities.contains("WPA") || scanResult.capabilities.contains("WEP")) {//已加密要输入密码
                wiFiDialog = new WiFiDialog(this, scanResult.SSID, 3);
                wiFiDialog.show();
                final int type;
                if (scanResult.capabilities.contains("WPA")) {
                    type = 3;
                } else if (scanResult.capabilities.contains("WEP")) {
                    type = 2;
                } else {
                    type = 1;
                }
                final String ssid = scanResult.SSID;
                wiFiDialog.setCallBack(new WiFiDialog.CallBack() {
                    @Override
                    public void onSureListener() {
                        final String passWord = wiFiDialog.getPassWord();
                        ThreadManager.getInstance().addTask(new Runnable() {
                            @Override
                            public void run() {
                                addNetwork(CreateWifiInfo(ssid, passWord, type));//wifi名，密码，加密类型
                            }
                        });
                        wiFiDialog.dismiss();
                        TraceManager.getInstance().traceClick(SettingNetActivity.class,
                                SettingTraceEnum.BTN_SETW_WIFI_TRY_CONNECT.getPoint());
                    }

                    @Override
                    public void onCancelListener() {
                        wiFiDialog.dismiss();
                    }

                    @Override
                    public void onCancelSave() {
                    }

                    @Override
                    public void onInterruptOrConnectListener() {
                    }
                });
            } else {//未加密直接连接
                Log.d(TAG, "onItemClick:未加密直接连接 ");
                wiFiDialog = new WiFiDialog(this, scanResult.SSID, "", getLevel(scanResult),
                        false, 2);
                wiFiDialog.show();
                wiFiDialog.setCallBack(new WiFiDialog.CallBack() {
                    @Override
                    public void onSureListener() {
                    }

                    @Override
                    public void onCancelListener() {
                        wiFiDialog.dismiss();
                    }

                    @Override
                    public void onCancelSave() {
                    }

                    @Override
                    public void onInterruptOrConnectListener() {
                        ThreadManager.getInstance().addTask(new Runnable() {
                            @Override
                            public void run() {
                                addNetwork(CreateWifiInfo(scanResult.SSID, "", 1));
                            }
                        });
                        wiFiDialog.dismiss();
                        TraceManager.getInstance().traceClick(SettingNetActivity.class,
                                SettingTraceEnum.BTN_SETW_WIFI_TRY_CONNECT.getPoint());
                    }
                });
            }
        }
    }

    //添加
    @Override
    public void transData(String wifiName, NetworkInfo.DetailedState indexState) {
        if (indexState == CONNECTED) {
//            tv_connect.setText(wifiName);
        }
        state = indexState;
    }
    //****************

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)) {
                Log.d(TAG, "WIFI_STATE_CHANGED_ACTION: ");
                updateWifiState(intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,
                        WifiManager.WIFI_STATE_UNKNOWN));


            } else if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(action)) {
                Log.d(TAG, "SCAN_RESULTS_AVAILABLE_ACTION: ");
                refreshLayout.finishRefresh();
                getScanResults();
            } else if (WifiManager.SUPPLICANT_STATE_CHANGED_ACTION.equals(action)) {
                int linkWifiResult = intent.getIntExtra(WifiManager.EXTRA_SUPPLICANT_ERROR, 123);
                if (linkWifiResult == WifiManager.ERROR_AUTHENTICATING) {
                    ToastUtils.showShort("wifi密码错误!");
                }
            }
        }
    };

    public void getScanResults() {
        List<ScanResult> scanResults = mWifiManager.getScanResults();
        if (scanResults == null) {
            Log.d(TAG, "scanResults is null!");
            return;
        }
        Log.d(TAG, "scanResults:" + scanResults.size());
        //去除重复名字的wifi
        for (int i = 0; i < scanResults.size(); i++) {
            for (int j = scanResults.size() - 1; j > i; j--) {
                if (scanResults.get(i).SSID.equals(scanResults.get(j).SSID)) {
                    scanResults.remove(j);
                }
            }
        }
        //去除隐藏名字的
        for (int i = scanResults.size() - 1; i >= 0; i--) {
            if (TextUtils.isEmpty(scanResults.get(i).SSID)) {
                scanResults.remove(i);
            }
        }
        wiFiListAdapter.getData().clear();
        sortByLevel(scanResults);
        wiFiListAdapter.getData().addAll(scanResults);
        wiFiListAdapter.onTopNotifyDataSetChanged();
        Log.d(TAG, "setAdapter：" + scanResults.size());
//        superSwipeRefreshLayout.setVisibility(View.VISIBLE);
        if (scanResults.size() > 0) {
            wifiRecyclerView.setVisibility(View.VISIBLE);
            recyclerRLayout.setVisibility(View.VISIBLE);
            wifiHnit.setVisibility(View.GONE);
        } else {
            wifiRecyclerView.setVisibility(View.GONE);
            recyclerRLayout.setVisibility(View.GONE);
            wifiHnit.setVisibility(View.VISIBLE);
            wifiHnit.setText("附近无可用WiFi");
        }

    }

    private void updateWifiState(int state) {
        Log.d(TAG, "state: " + state);
        if (wiFiListAdapter != null) {
            List<ScanResult> adapterData = wiFiListAdapter.getData();
            if (adapterData != null && adapterData.size() > 0) {
                wifiHnit.setVisibility(View.GONE);
                wifiRecyclerView.setVisibility(View.VISIBLE);
                recyclerRLayout.setVisibility(View.VISIBLE);
            } else {
                wifiHnit.setVisibility(View.VISIBLE);
                wifiRecyclerView.setVisibility(View.GONE);
                recyclerRLayout.setVisibility(View.GONE);
            }
        } else {
            wifiHnit.setVisibility(View.VISIBLE);
        }
        switch (state) {
            case WifiManager.WIFI_STATE_ENABLED://打开WiFi
                wifiHnit.setText(this.getResources().getString(R.string.wifi_hint_3));
                mScanner.resume();//从下面的方法中可以看到，该方法是用于开启WiFi的扫描，并记录扫描次数
                return; // not break, to avoid the call to pause() below

            case WifiManager.WIFI_STATE_ENABLING://正在打开WiFi
                //addMessagePreference(R.string.wifi_starting);
                wifiHnit.setText(this.getResources().getString(R.string.wifi_hint_2));
                break;

            case WifiManager.WIFI_STATE_DISABLED://关闭WiFi


//                //打开TBOX热点
//                hot_dot_switch.setChecked(true);
                wiFiListAdapter.getData().clear();
                wiFiListAdapter.notifyDataSetChanged();
                wifiHnit.setText(this.getResources().getString(R.string.wifi_hint_1));

                wifiHnit.setVisibility(View.VISIBLE);
//                superSwipeRefreshLayout.setVisibility(View.GONE);
                wifiRecyclerView.setVisibility(View.GONE);
                recyclerRLayout.setVisibility(View.GONE);
                break;
        }
        mScanner.pause();//移除message通知
    }

    /**
     * 按信号强度排列
     *
     * @param list
     */
    private void sortByLevel(List<ScanResult> list) {
        Collections.sort(list, new Comparator<ScanResult>() {
            @Override
            public int compare(ScanResult o1, ScanResult o2) {
                return o2.level - o1.level;
            }
        });
    }

    private static class Scanner extends Handler {
        private int mRetry = 0;
        private WeakReference<SettingNetActivity> reference;

        private Scanner(SettingNetActivity activity) {
            reference = new WeakReference<>(activity);
        }

        void resume() {
            if (!hasMessages(0)) {
                Log.d(TAG, "resume()");
                sendEmptyMessage(0);
            }
        }

        void forceScan() {
            removeMessages(0);
            sendEmptyMessage(0);
            Log.d(TAG, "forceScan()");
        }

        void pause() {
            Log.d(TAG, "pause()");
            mRetry = 0;
            removeMessages(0);
        }

        @Override
        public void handleMessage(Message message) {
            Log.d(TAG, "message.what " + message.what);
            SettingNetActivity fragment = reference.get();
            Log.d(TAG, "fragment: " + fragment + " ");
            if (fragment != null && fragment.mWifiManager.startScan()) {
                Log.d(TAG, "startScan: ");
                mRetry = 0;
            } else if (++mRetry >= 3) {
                mRetry = 0;
                return;
            }
            sendEmptyMessageDelayed(0, 10 * 1000);//10s后再次发送message
        }
    }

    private String getLevel(ScanResult scanResult) {
        String level;
        if (scanResult.level <= 0 && scanResult.level >= -50) {//1 强
            level = "强";
        } else if (scanResult.level < -50 && scanResult.level >= -70) {//2较好
            level = "较好";
        } else if (scanResult.level < -70 && scanResult.level >= -80) {//3一般
            level = "一般";
        } else if (scanResult.level < -80 && scanResult.level >= -100) {//4较差
            level = "较差";
        } else {
            level = "差";
        }
        return level;
    }

    private int isClickSaveItem(ScanResult scanResult) {
        List<WifiConfiguration> configuredNetworks = mWifiManager.getConfiguredNetworks();
        if (configuredNetworks != null) {
            for (int i = 0; i < configuredNetworks.size(); i++) {
                int length = configuredNetworks.get(i).SSID.length();
                if (length > 1 && scanResult.SSID.equals(configuredNetworks.get(i).SSID.substring(1, length - 1))) {
                    return i;
                }
            }
            return -1;
        } else {
            return -1;
        }
    }

    private boolean isClickConnectItem(ScanResult scanResult) {
        WifiInfo connectionInfo = mWifiManager.getConnectionInfo();
        if (connectionInfo != null) {
            int length = connectionInfo.getSSID().length();
            if (length > 1 && scanResult.SSID.equals(connectionInfo.getSSID().substring(1,
                    length - 1))) {
                return true;
            }
            return false;
        } else {
            return false;
        }
    }

    // 连接wifi热点
    private WifiConfiguration CreateWifiInfo(String SSID, String Password, int Type) {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";

        WifiConfiguration tempConfig = this.IsExsits(SSID);
        if (tempConfig != null) {
            mWifiManager.removeNetwork(tempConfig.networkId);
        }
        if (Type == 1) { //WIFICIPHER_NOPASS
            // config.wepKeys[0] = "";
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            // config.wepTxKeyIndex = 0;
        }
        if (Type == 2) { //WIFICIPHER_WEP
            config.hiddenSSID = true;
            config.wepKeys[0] = "\"" + Password + "\"";
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        if (Type == 3) { //WIFICIPHER_WPA
            config.preSharedKey = "\"" + Password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            //config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;
        }
        return config;
    }

    private WifiConfiguration IsExsits(String SSID) {
        List<WifiConfiguration> existingConfigs = mWifiManager.getConfiguredNetworks();
        if (existingConfigs != null) {
            for (WifiConfiguration existingConfig : existingConfigs) {
                if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
                    return existingConfig;
                }
            }
        }
        return null;
    }

    // 添加一个网络并连接
    private boolean addNetwork(WifiConfiguration wcg) {
        int wcgID = mWifiManager.addNetwork(wcg);
        boolean b = mWifiManager.enableNetwork(wcgID, true);
        return b;
    }

    // 断开指定ID的网络
    private void disconnectWifi(int netId) {
        mWifiManager.disableNetwork(netId);
        mWifiManager.disconnect();
    }

    // 遗忘指定ID的网络
    private void removeNetwork(int netId) {
        mWifiManager.removeNetwork(netId);
        mWifiManager.saveConfiguration();
    }

    // 指定配置好的网络进行连接
    public boolean connectConfiguration(int index) {
        // 索引大于配置好的网络索引返回
        if (mWifiManager.getConfiguredNetworks() != null) {
            if (index > mWifiManager.getConfiguredNetworks().size()) {
                return false;
            }
            // 连接配置好的指定ID的网络
            return mWifiManager.enableNetwork(mWifiManager.getConfiguredNetworks().get(index).networkId, true);
        } else {
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == AppConst.PERMISSION_REQ_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "wifi 权限已开启", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "人家不给权限看到没", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private List<String> rPermissionsList = null;
    String[] permissions = {
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.CHANGE_NETWORK_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE};

    @Override
    protected boolean checkPermission() {
        rPermissionsList = PermissionHelper.getNeedRequestPermissionNameList(this, permissions);
        if (rPermissionsList == null || rPermissionsList.isEmpty()) {
            return false;
        } else {
            //请求权限
            PermissionHelper.requestPermissons(this, rPermissionsList,
                    AppConst.PERMISSION_REQ_CODE);
            return true;
        }
    }


}
