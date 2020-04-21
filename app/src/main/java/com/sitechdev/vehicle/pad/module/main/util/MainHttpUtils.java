package com.sitechdev.vehicle.pad.module.main.util;

import android.icu.util.LocaleData;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.sitechdev.net.EnvironmentConfig;
import com.sitechdev.net.HttpCode;
import com.sitechdev.net.JsonCallback;
import com.sitechdev.vehicle.lib.util.NetworkUtils;
import com.sitechdev.vehicle.pad.app.AppApplication;
import com.sitechdev.vehicle.pad.app.AppUrlConst;
import com.sitechdev.vehicle.pad.callback.BaseBribery;
import com.sitechdev.vehicle.pad.module.location.LocationUtil;
import com.sitechdev.vehicle.pad.module.main.bean.WeatherInfoBean;
import com.sitechdev.vehicle.pad.module.map.util.LocationData;
import com.sitechdev.vehicle.pad.module.member.bean.FeedBackTypeBean;
import com.sitechdev.vehicle.pad.net.util.HttpUtil;

import java.util.List;

/**
 * 首页访问网络的工具类
 *
 * @author bijingshuai
 * @date 2019/8/22
 */
public class MainHttpUtils extends HttpUtil {

    /**
     * 请求天气数据的接口
     * @param baseBribery
     */
    public static void getWeatherData(BaseBribery baseBribery) {
        if (!NetworkUtils.isNetworkAvailable(AppApplication.getContext())) {
            if (baseBribery != null) {
                baseBribery.onFailure("网络异常...");
            }
            return;
        }

        if (LocationData.getInstance().getLongitude() == 0d) {
            if (baseBribery != null) {
                baseBribery.onFailure("正在定位中...");
            }
            return;
        }

        //从网络获取数据，并将数据缓存起来
        OkGo.<WeatherInfoBean>get(formatUserFinalRequestUrl(AppUrlConst.GET_WEATHER_DATA))
                .params("lng", LocationData.getInstance().getLongitude())
                .params("lat", LocationData.getInstance().getLatitude())
                .execute(new JsonCallback<WeatherInfoBean>(WeatherInfoBean.class) {
                    @Override
                    public void onSuccess(Response<WeatherInfoBean> response) {
                        if (null == response || null == response.body()) {
                            if (baseBribery != null) {
                                baseBribery.onFailure("数据获取失败");
                            }
                            return;
                        }
                        WeatherInfoBean weatherInfoBeanTwo = response.body();
                        if (weatherInfoBeanTwo.getCode() != null && !"".equals(weatherInfoBeanTwo.getCode())) {
                            if (HttpCode.HTTP_OK.equals(weatherInfoBeanTwo.getCode())) {
                                if (baseBribery != null) {
                                    baseBribery.onSuccess(weatherInfoBeanTwo);
                                }
                            } else {
                                if (baseBribery != null) {
                                    baseBribery.onFailure("数据获取失败");
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Response<WeatherInfoBean> response) {
                        super.onError(response);
                        if (baseBribery != null) {
                            baseBribery.onFailure("数据获取失败");
                        }
                    }
                });
    }
}
