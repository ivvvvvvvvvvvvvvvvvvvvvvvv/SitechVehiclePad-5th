package com.sitechdev.vehicle.pad.model;

import com.sitechdev.vehicle.pad.bean.WeatherInfoBeanTwo;
import com.sitechdev.vehicle.pad.model.contract.WeatherContract;
import com.sitechdev.vehicle.pad.module.main.util.WeatherUtils;
import com.sitechdev.vehicle.pad.module.map.util.LocationData;

/***
 * 日期： 2019/4/4 13:52
 * 姓名： sitechdev_bjs
 * 说明： 更新内容..
 */
public class WeatherModel implements WeatherContract.Model {

    @Override
    public void getWeatherData(boolean isUserCatch, WeatherContract.WeatherCallback callback) {
        WeatherUtils.getInstance().setOnWeatherListener(new WeatherUtils.OnWeatherListener() {
            @Override
            public void onCatchSuccess(WeatherInfoBeanTwo infoBeanTwo) {
                callback.onCatchSuccess(infoBeanTwo);
            }

            @Override
            public void onSuccess(WeatherInfoBeanTwo infoBeanTwo) {
                callback.onSuccess(infoBeanTwo);
            }

            @Override
            public void onError(int type, String msg) {
                callback.onFailed(type, msg);
            }
        });
        WeatherUtils.getInstance().getWeatherData(LocationData.getInstance().getLongitude(), LocationData.getInstance().getLatitude(), isUserCatch);
    }

    @Override
    public String getCityDataWithLocation() {
        return WeatherUtils.getCityDataWithLocation();
    }
}
