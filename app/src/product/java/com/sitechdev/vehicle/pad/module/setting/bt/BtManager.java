package com.sitechdev.vehicle.pad.module.setting.bt;

import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.ToastUtils;
import com.sitechdev.vehicle.pad.app.AppApplication;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BtManager {
    private static BtManager INSTANCE;
    private BluetoothAdapter mBtAdapter;
    private BluetoothManager mBtManager;
    private boolean isScanning = false;
    private List<BluetoothDevice> mDevices;
    private List<String> mDeviceMacs;
    private Scan5_0 mScan5_0;
    private Scan4_3 mScan4_3;
    private BtScanCallback mCallBack;

    private BtManager() {
        mBtManager =
                (BluetoothManager) AppApplication.getContext().getSystemService(Context.BLUETOOTH_SERVICE);
        mBtAdapter = mBtManager.getAdapter();
        mDevices = new ArrayList<>();
        mDeviceMacs = new ArrayList<>();
        if (Build.VERSION.SDK_INT < 21) {
            mScan4_3 = new Scan4_3(this.mBtAdapter);
        } else {
            mScan5_0 = new Scan5_0(this.mBtAdapter);
        }
    }

    public static BtManager getInstance() {
        if (null == INSTANCE) {
            synchronized (BtManager.class) {
                if (null == INSTANCE) {
                    INSTANCE = new BtManager();
                }
            }
        }
        return INSTANCE;
    }

    public void openBle(Context context) {
        logTest("openBle");
        if (null == mBtAdapter) {
            return;
        }
        logTest("openBle 1");
//        mBtAdapter.enable();
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        ((Activity) context).startActivityForResult(intent, 0);
    }

    public void closeBle() {
        if (null == mBtAdapter) {
            return;
        }
        mBtAdapter.disable();
    }

    public void scanBtList() {
        logTest("scan Ble LIST");
        if (null == mBtAdapter) {
            return;
        }
        if (!mBtAdapter.isEnabled()) {
            logTest("bt is disable");
            return;
        }
        if (isScanning) {
            stopScan();
        }
        if (mBtAdapter.isEnabled()) {
            mBtAdapter.enable();
        }
        logTest("start discovery");
        mDevices.clear();
        mDeviceMacs.clear();
        mBtAdapter.startDiscovery();
        if (Build.VERSION.SDK_INT < 21) {
//            mScan4_3.startScan(scanCallback);
        } else {
//            mScan5_0.startScan(scanCallback);
        }
    }

    public boolean isBtEnable(){
        if(null == mBtAdapter){
            return false;
        }
        return mBtAdapter.isEnabled();
    }

    public void registerReceiver(Context context, BtScanCallback callback) {
        mCallBack = callback;
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        context.registerReceiver(mReceiver, filter);
    }

    public void unregisterReceiver(Context context) {
        context.unregisterReceiver(mReceiver);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction() + "";
            logTest("onReceive-----action:" + action);
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device == null) {
                    return;
                }
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    String address = device.getAddress();
                    if (!TextUtils.isEmpty(address) && !mDeviceMacs.contains(address)) {
                        mDeviceMacs.add(address);
                        mDevices.add(device);
                    }
                }
            } else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
            } else if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
                mCallBack.onScan();
            }
        }
    };

    public void stopScan() {
    }

    public String getLocalName() {
        return TextUtils.isEmpty(mBtAdapter.getName()) ? "" : mBtAdapter.getName();
    }

    private Handler handl = new Handler();

    public boolean isEnable() {
        return mBtAdapter.isEnabled();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private class Scan5_0 {
        private BluetoothAdapter mBluetoothAdapter;
        private BtScanCallback scanCallback;

        public Scan5_0(BluetoothAdapter mBluetoothAdapter) {
            this.mBluetoothAdapter = mBluetoothAdapter;
        }

        // 5.0+系统扫描回调方法
        private android.bluetooth.le.ScanCallback mScanCallback =
                new android.bluetooth.le.ScanCallback() {

                    @Override
                    public void onScanResult(final int callbackType, final ScanResult result) {
                        super.onScanResult(callbackType, result);
                        if (result == null) {
                            return;
                        }
                        handl.post((Runnable) () -> {
                            BtDeviceItem deviceItem = new BtDeviceItem();
                            deviceItem.setDevice(result.getDevice());
                            scanCallback.onScan();
                        });
                    }

                    @Override
                    public void onBatchScanResults(List<ScanResult> results) {
                        super.onBatchScanResults(results);
                    }

                    @Override
                    public void onScanFailed(int errorCode) {
                        super.onScanFailed(errorCode);
                    }

                };

        public boolean startScan(BtScanCallback scanCallback) {
            System.out.println("5.0 scan");

            BluetoothLeScanner scaner = mBluetoothAdapter.getBluetoothLeScanner();
            if (scaner == null) {
                return false;
            }

            this.scanCallback = scanCallback;

            scaner.startScan(mScanCallback);

            return true;
        }

        public boolean stopScan() {

            if (!mBluetoothAdapter.isEnabled()) {
                return true;
            }

            BluetoothLeScanner scaner = mBluetoothAdapter.getBluetoothLeScanner();

            if (scaner == null) {
                return true;
            }

            scaner.stopScan(mScanCallback);

            return true;
        }
    }

    // 安卓4.3+系统扫描方式
    private class Scan4_3 {
        private BluetoothAdapter mBluetoothAdapter;
        private BtScanCallback scanCallback;

        // 4.3-5.0系统扫描回调方法
        private BluetoothAdapter.LeScanCallback lescancallback =
                new BluetoothAdapter.LeScanCallback() {

                    @Override
                    public void onLeScan(final BluetoothDevice device, final int rssi,
                                         final byte[] scanRecord) {
                        handl.post(new Runnable() {

                            @Override
                            public void run() {
                                BtDeviceItem deviceItem = new BtDeviceItem();
                                deviceItem.setDevice(device);
                                scanCallback.onScan();
                            }
                        });

                        return;
                    }
                };

        public Scan4_3(BluetoothAdapter mBluetoothAdapter) {
            this.mBluetoothAdapter = mBluetoothAdapter;
        }

        public boolean startScan(BtScanCallback scanCallback) {
            System.out.println("4.3 scan");
            this.scanCallback = scanCallback;

            this.mBluetoothAdapter.startLeScan(lescancallback);
            return true;
        }

        public boolean stopScan() {
//				this.scanCallback = null;
            this.mBluetoothAdapter.stopLeScan(lescancallback);
            return true;
        }
    }

    public List<BtDeviceItem> getBtPairList() {
        Set<BluetoothDevice> devices = mBtAdapter.getBondedDevices();
        List<BtDeviceItem> list = new ArrayList<>();
        BtDeviceItem item;
        for (BluetoothDevice device : devices) {
            item = new BtDeviceItem();
            item.setDevice(device);
            list.add(item);
        }
        return list;
    }

    public void setDiscovered(boolean discovered) {
        Class[] setArgArray = new Class[]{int.class};
        try {
            Method mSetMethod = mBtAdapter.getClass().getMethod("setScanMode", setArgArray);
            if (!discovered) {
                mSetMethod.invoke(mBtAdapter, BluetoothAdapter.SCAN_MODE_NONE);
            } else {
                mSetMethod.invoke(mBtAdapter, BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface BtScanCallback {
        void onScan(/*BtDeviceItem deviceItem*/);
    }

    private void logTest(String msg) {
        Log.e("TEST_BtManager", "-----" + msg);
        ToastUtils.showShort(msg);
    }

}
