package com.my.hw;

import java.io.Serializable;

public class BtDeviceBean implements Serializable {
    private String btName;
    private String btAddress;

    public String getBtName() {
        return btName;
    }

    public void setBtName(String btName) {
        this.btName = btName;
    }

    public String getBtAddress() {
        return btAddress;
    }

    public void setBtAddress(String btAddress) {
        this.btAddress = btAddress;
    }

    @Override
    public String toString() {
        return "BtDeviceBean{" +
                "btName='" + btName + '\'' +
                ", btAddress='" + btAddress + '\'' +
                '}';
    }
}
