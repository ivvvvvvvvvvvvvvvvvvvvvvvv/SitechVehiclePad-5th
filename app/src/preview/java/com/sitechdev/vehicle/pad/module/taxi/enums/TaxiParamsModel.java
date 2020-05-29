package com.sitechdev.vehicle.pad.module.taxi.enums;

import com.sitechdev.net.bean.BaseBean;

/**
 * 项目名称：SitechVehiclePad-5th
 * 类名称：TaxiParamsBean
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2020/05/25 0025 15:11
 * 修改时间：
 * 备注：
 */
public class TaxiParamsModel {
    private TaxiParamsModel() {
    }

    private static final class SingleTaxiParamsModel {
        private static final TaxiParamsModel SINGLE = new TaxiParamsModel();
    }

    public static TaxiParamsModel getInstance() {
        return SingleTaxiParamsModel.SINGLE;
    }

    /**
     * 起步价包含的公里数。即{startPrice_km}公里内。。。
     */
    private double startPrice_km = 3;

    /**
     * 起步价的公里数{startPrice_km}公里内的价格。即{startPrice_km}公里内{startPriceInKm}元
     */
    private double startPriceInKm = 5;

    /**
     * 超出起步价的公里后的每公里单价，即超出{startPrice_km}公里后，{singlePriceKm}元/公里
     */
    private double singlePriceKm = 10;

    public double getStartPrice_km() {
        return startPrice_km;
    }

    public void setStartPrice_km(double startPrice_km) {
        this.startPrice_km = startPrice_km;
    }

    public double getStartPriceInKm() {
        return startPriceInKm;
    }

    public void setStartPriceInKm(double startPriceInKm) {
        this.startPriceInKm = startPriceInKm;
    }

    public double getSinglePriceKm() {
        return singlePriceKm;
    }

    public void setSinglePriceKm(double singlePriceKm) {
        this.singlePriceKm = singlePriceKm;
    }
}
