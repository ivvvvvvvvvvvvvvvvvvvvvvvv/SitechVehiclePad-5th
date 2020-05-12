package com.sitechdev.vehicle.pad.module.taxi.enums;

import java.nio.DoubleBuffer;

/**
 * 项目名称：SitechVehiclePad-5th
 * 类名称：TaxiDataModel
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2020/05/11 0011 18:40
 * 修改时间：
 * 备注：
 */
public enum TaxiDataModel {
    /**
     * 十位数（起始值（十位数忽略），价格文字大小，总里程文字大小）
     */
    Digit_Ten(99.99d, 220f, 60f),
    /**
     * 百位数（起始值,价格文字大小，总里程文字大小）
     */
    Digit_Hundred(100.00d, 200f, 55f),
    /**
     * 千位数（起始值,价格文字大小，总里程文字大小）
     */
    Digit_Thousand(1000.00d, 160f, 50f);

    private double minValue = 99.99d;
    private float priceSize = 0f, kmSize = 0f;

    private TaxiDataModel(double mValue, float pSize, float kSize) {
        minValue = mValue;
        priceSize = pSize;
        kmSize = kSize;
    }

    public double getMinValue() {
        return minValue;
    }

    public float getPriceSize() {
        return priceSize;
    }

    public float getKmSize() {
        return kmSize;
    }
}
