package com.sitechdev.vehicle.pad.module.main.util;

import android.text.TextUtils;

import com.sitechdev.vehicle.lib.util.SPUtils;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.AppApplication;
import com.sitechdev.vehicle.pad.module.main.MainActivity;
import com.sitechdev.vehicle.pad.module.map.util.LocationData;

/**
 * 天气工具类
 *
 * @author bijingshuai
 * @date 2019/8/22
 */
public class WeatherUtils {

    public static final String SP_LOCATION_CITY = "Location_city";

    /**
     * 根据定位获取城市名称
     * @return
     */
    public static String getCityDataWithLocation() {
        StringBuilder sb = new StringBuilder();
        if (!TextUtils.isEmpty(LocationData.getInstance().getCityName())) {
            sb.append(LocationData.getInstance().getCityName());
        }

        if (!TextUtils.isEmpty(LocationData.getInstance().getDistrictName())) {
            sb.append(" , ");
            sb.append(LocationData.getInstance().getDistrictName());
        }

        if (!TextUtils.isEmpty(sb.toString())) {
            SPUtils.putValue(AppApplication.getContext(), SP_LOCATION_CITY, sb.toString());
            return sb.toString();
        } else {
            return SPUtils.getValue(AppApplication.getContext(), SP_LOCATION_CITY, "一 一，一 一");
        }
    }

    /**
     * 获取文本图片
     * @param imgType
     * @return
     */
    public static int getWeatherIcon(String imgType) {
        int iconType;
        try {
            iconType = Integer.parseInt(imgType);
        } catch (Exception e) {
            iconType = 0;
        }
        switch (iconType) {
            case 1:
                return R.drawable.weather_duoyun_1;
            case 2:
                return R.drawable.weather_yin_2;
            case 3:
                return R.drawable.weather_zhenyu_3;
            case 4:
                return R.drawable.weather_small_leizhenyu_4;
            case 5:
                return R.drawable.weather_leizhenyu_bingbao_5;
            case 6:
                return R.drawable.weather_yujiaxue_6;
            case 7:
                return R.drawable.weather_xiaoyu_7;
            case 8:
            case 21:
            case 301:
                return R.drawable.weather_zhongyu_8;
            case 9:
            case 22:
                return R.drawable.weather_dayu_9;
            case 10:
            case 23:
                return R.drawable.weather_baoyu_10;
            case 11:
            case 24:
                return R.drawable.weather_dabaoyu_11;
            case 12:
            case 25:
                return R.drawable.weather_tedabaoyu_12;
            case 13:
                return R.drawable.weather_zhenxue_13;
            case 14:
                return R.drawable.weather_xiaoxue_14;
            case 15:
            case 26:
            case 302:
                return R.drawable.weather_zhongxue_15;
            case 16:
            case 27:
                return R.drawable.weather_daxue_16;
            case 17:
            case 28:
                return R.drawable.weather_baoxue_17;
            case 18:
            case 32:
            case 49:
            case 57:
            case 59:
                return R.drawable.weather_wu_18;
            case 19:
                return R.drawable.weather_dongyu_19;
            case 20:
                return R.drawable.weather_shachenbao_20;
            case 31:
                return R.drawable.weather_shachenbao_31;
            case 29:
                return R.drawable.weather_fuchen_29;
            case 30:
                return R.drawable.weather_yangsha_30;
            case 53:
                return R.drawable.weather_mai_53;
            case 54:
            case 55:
                return R.drawable.weather_zhongdumai_54;
            case 56:
                return R.drawable.weather_yanzhongmai_56;

            case 0:
            case 99:
            default:
                return R.drawable.weather_qing_0;
        }
    }

}
