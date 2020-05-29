package com.sitechdev.vehicle.pad.module.weather.presenter;


import android.text.TextUtils;

import com.sitechdev.net.HttpCode;
import com.sitechdev.vehicle.lib.event.EventBusUtils;
import com.sitechdev.vehicle.pad.bean.WeatherInfoBeanTwo;
import com.sitechdev.vehicle.pad.event.WeatherEvent;
import com.sitechdev.vehicle.pad.model.WeatherModel;
import com.sitechdev.vehicle.pad.model.contract.WeatherContract;
import com.sitechdev.vehicle.pad.module.main.util.WeatherUtils;

/***
 * 日期： 2019/7/10 13:54
 * 姓名： sitechdev_bjs
 * 说明： 天气业务逻辑类
 */
public class WeatherPresenter extends WeatherContract.Presenter {

    private WeatherModel mModel = new WeatherModel();

    @Override
    public void loadWeatherData(boolean isUseCatch) {
        getView().showLoading();
        mModel.getWeatherData(isUseCatch, new WeatherContract.WeatherCallback() {
            @Override
            public void onCatchSuccess(WeatherInfoBeanTwo weatherInfoBeanTwo) {
                if (null != getView()) {
                    getView().showCatchSuccessLoading();
                    if (weatherInfoBeanTwo.getCode() != null && !"".equals(weatherInfoBeanTwo.getCode())) {
                        if (HttpCode.HTTP_OK.equals(weatherInfoBeanTwo.getCode())) {
                            EventBusUtils.postEvent(new WeatherEvent(WeatherEvent.UPD_WEATHER, weatherInfoBeanTwo));
                            try {
                                getView().refreshWeatherView(weatherInfoBeanTwo,true);
                            } catch (Exception e) {
                                e.printStackTrace();
                                getView().showError(WeatherUtils.ERROR_TYPE_OTHER, WeatherUtils.ERROR_MSG_ERROR);
                            }
                        } else {
                            getView().showError(WeatherUtils.ERROR_TYPE_OTHER, WeatherUtils.ERROR_MSG_ERROR);
                        }
                    }
                }
            }

            @Override
            public void onSuccess(WeatherInfoBeanTwo weatherInfoBeanTwo) {
                if (null != getView()) {
                    getView().hideLoading();

                    if (weatherInfoBeanTwo != null && !TextUtils.isEmpty(weatherInfoBeanTwo.getCode())) {
                        if (HttpCode.HTTP_OK.equals(weatherInfoBeanTwo.getCode())) {
                            EventBusUtils.postEvent(new WeatherEvent(WeatherEvent.UPD_WEATHER, weatherInfoBeanTwo));
                            try {
                                getView().refreshWeatherView(weatherInfoBeanTwo,false);
                            } catch (Exception e) {
                                e.printStackTrace();
                                getView().showError(WeatherUtils.ERROR_TYPE_OTHER, WeatherUtils.ERROR_MSG_ERROR);
                            }
                        } else {
                            getView().showError(WeatherUtils.ERROR_TYPE_OTHER, WeatherUtils.ERROR_MSG_ERROR);
                        }
                    } else {
                        getView().showError(WeatherUtils.ERROR_TYPE_OTHER, WeatherUtils.ERROR_MSG_ERROR);
                    }
                }
            }

            @Override
            public void onFailed(int type,String msg) {
                if (null != getView()) {
                    getView().hideLoading();
                    getView().showError(type,msg);
                }
            }
        });
    }

    @Override
    public String loadCityDataWithLocation() {
        return mModel.getCityDataWithLocation();
    }
}
