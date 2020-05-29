package com.sitechdev.vehicle.pad.model.contract;

import com.sitechdev.vehicle.pad.app.BasePresenter;
import com.sitechdev.vehicle.pad.bean.IContract;
import com.sitechdev.vehicle.pad.bean.WeatherInfoBeanTwo;

/***
 * 日期： 2019/4/4 13:48
 * 姓名： sitechdev_bjs
 * 说明： 更新内容..
 */
public interface WeatherContract {
    interface Model extends IContract.IModel {
        void getWeatherData(boolean isUserCatch, WeatherCallback callback);

        String getCityDataWithLocation();
    }

    abstract class Presenter extends BasePresenter<WeatherContract.View> {
        public abstract void loadWeatherData(boolean isUseCatch);

        public abstract String loadCityDataWithLocation();
    }

    interface View extends IContract.IView {
        void refreshWeatherView(WeatherInfoBeanTwo msg,boolean isCatch);
        void showError(int type,String msg);

        void showCatchSuccessLoading();
    }

    interface WeatherCallback {
        void onCatchSuccess(WeatherInfoBeanTwo info);

        void onSuccess(WeatherInfoBeanTwo info);

        void onFailed(int type,String msg);
    }
}
