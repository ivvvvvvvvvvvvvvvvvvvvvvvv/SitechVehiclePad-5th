package com.sitechdev.vehicle.pad.bean;

import com.sitechdev.vehicle.lib.util.ParamsUtil;

/**
 * 用于天气数据的缓存
 *
 * @author bijingshuai
 * @date 2019/5/15
 */
public class WeatherBeanCatch {
    private static final String SP_WEATHER_CATCH_DATA = "sp_weather_catch_data";
    private WeatherInfoBeanTwo infoBeanTwo;     //天气数据
    private Long dataTimeMillis = 0L;           //数据的存储时间
    private int validTime = 30 * 60 * 1000;     //数据有效的时间

    private static volatile WeatherBeanCatch instance;

    private WeatherBeanCatch() {
    }

    public static WeatherBeanCatch getInstance() {
        if (instance == null) {
            synchronized (WeatherBeanCatch.class) {
                if (instance == null) {
                    instance = new WeatherBeanCatch();
                }
            }
        }
        return instance;
    }

    /**
     * 设置天气数据
     *
     * @param infoBeanTwo
     * @param dataTimeMillis
     */
    public void setData(WeatherInfoBeanTwo infoBeanTwo, Long dataTimeMillis) {
        this.infoBeanTwo = infoBeanTwo;
        this.dataTimeMillis = dataTimeMillis;

        ParamsUtil.setData(SP_WEATHER_CATCH_DATA, infoBeanTwo);
    }

    /**
     * 获取天气数据
     *
     * @return
     */
    public WeatherInfoBeanTwo getWeatherInfoBeanTwo() {
        if (infoBeanTwo == null) {
            infoBeanTwo = (WeatherInfoBeanTwo) ParamsUtil.getBeanData(SP_WEATHER_CATCH_DATA);
        }
        return infoBeanTwo;
    }

    /**
     * 判断数据是否有效
     *
     * @return
     */
    public boolean isValid() {
        return (System.currentTimeMillis() - dataTimeMillis) < validTime;
    }

    /**
     * 设置数据有效的时间
     *
     * @param validTime 单位毫秒
     */
    public void setValidTime(int validTime) {
        this.validTime = validTime;
    }
}
