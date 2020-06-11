package com.sitechdev.vehicle.pad.model;

import android.bluetooth.BluetoothDevice;

import java.io.Serializable;

public class BtConnectItemBean implements Serializable {
    private BluetoothDevice device;
    private boolean hasConnected;

    public boolean hasConnected() {
        return hasConnected;
    }

    public void setHasConnected(boolean hasConnected) {
        this.hasConnected = hasConnected;
    }

    public BluetoothDevice getDevice() {
        return device;
    }

    public void setDevice(BluetoothDevice device) {
        this.device = device;
    }
}
