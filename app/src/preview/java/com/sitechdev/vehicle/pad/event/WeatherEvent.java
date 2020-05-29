package com.sitechdev.vehicle.pad.event;

import com.sitechdev.vehicle.lib.event.BaseEvent;

/**
 * update weather
 *
 * @author EugeneTuladhar
 * @date 2019/04/16
 */
public class WeatherEvent extends BaseEvent<String> {

    public static final String UPD_WEATHER = "update_weather";

    public Object mWeatherbean;

    public WeatherEvent(String evt, Object mWeatherbean) {
        this.eventKey = evt;
        this.mWeatherbean = mWeatherbean;
    }
}
