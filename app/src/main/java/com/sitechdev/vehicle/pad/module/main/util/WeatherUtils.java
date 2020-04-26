package com.sitechdev.vehicle.pad.module.main.util;

import android.text.TextUtils;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.sitechdev.net.EnvironmentConfig;
import com.sitechdev.net.HttpCode;
import com.sitechdev.net.JsonCallback;
import com.sitechdev.vehicle.lib.util.NetworkUtils;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.AppApplication;
import com.sitechdev.vehicle.pad.app.AppUrlConst;
import com.sitechdev.vehicle.pad.bean.WeatherBeanCatch;
import com.sitechdev.vehicle.pad.bean.WeatherInfoBeanTwo;
import com.sitechdev.vehicle.pad.module.map.util.LocationData;
import com.sitechdev.vehicle.pad.util.ParamsUtil;

/**
 * 天气工具类
 *
 * @author bijingshuai
 * @date 2019/8/22
 */
public class WeatherUtils {

    private WeatherUtils() {
    }

    private volatile static WeatherUtils instance;
    public static final int ERROR_TYPE_NET = 1;
    public static final int ERROR_TYPE_OTHER = 2;
    public static final int ERROR_TYPE_TOAST = 3;

    public static final String ERROR_MSG_NET = "网络不稳定，请稍后重试";
    public static final String ERROR_MSG_GPS = "定位获取失败，请稍后重试";
    public static final String ERROR_MSG_ERROR = "数据走丢了，请稍后重试";
    public OnWeatherListener listener;

    public static WeatherUtils getInstance() {
        if (instance == null) {
            synchronized (WeatherUtils.class) {
                if (instance == null) {
                    instance = new WeatherUtils();
                }
            }
        }
        return instance;
    }

    public int getAqiIcon(String imgType) {
        int iconType;
        try {
            iconType = Integer.parseInt(imgType);
        } catch (Exception e) {
            iconType = 0;
        }
        if (iconType < 100 && iconType > 0) {
            return R.drawable.weather_aqigood;
        } else if (iconType > 99 && iconType < 300) {
            return R.drawable.weather_aqimedium;
        } else if (iconType > 299 && iconType < 1000) {
            return R.drawable.weather_aqibad;
        } else {
            return R.drawable.weather_aqigood;
        }
    }

    public int getWeatherIcon(String imgType) {
        int iconType;
        try {
            iconType = Integer.parseInt(imgType);
        } catch (Exception e) {
            iconType = 0;
        }
        switch (iconType) {
            case 1:
                return R.drawable.icon_weather_duoyun_1;
            case 2:
                return R.drawable.icon_weather_yin_2;
            case 3:
                return R.drawable.icon_weather_zhenyu_3;
            case 4:
                return R.drawable.icon_weather_small_leizhenyu_4;
            case 5:
                return R.drawable.icon_weather_leizhenyu_bingbao_5;
            case 6:
                return R.drawable.icon_weather_yujiaxue_6;
            case 7:
                return R.drawable.icon_weather_xiaoyu_7;
            case 8:
            case 21:
            case 301:
                return R.drawable.icon_weather_zhongyu_8;
            case 9:
            case 22:
                return R.drawable.icon_weather_dayu_9;
            case 10:
            case 23:
                return R.drawable.icon_weather_baoyu_10;
            case 11:
            case 24:
                return R.drawable.icon_weather_dabaoyu_11;
            case 12:
            case 25:
                return R.drawable.icon_weather_tedabaoyu_12;
            case 13:
                return R.drawable.icon_weather_zhenxue_13;
            case 14:
                return R.drawable.icon_weather_xiaoxue_14;
            case 15:
            case 26:
            case 302:
                return R.drawable.icon_weather_zhongxue_15;
            case 16:
            case 27:
                return R.drawable.icon_weather_daxue_16;
            case 17:
            case 28:
                return R.drawable.icon_weather_baoxue_17;
            case 18:
            case 32:
            case 49:
            case 57:
            case 59:
                return R.drawable.icon_weather_wu_18;
            case 19:
                return R.drawable.icon_weather_dongyu_19;
            case 20:
                return R.drawable.icon_weather_shachenbao_20;
            case 31:
                return R.drawable.icon_weather_shachenbao_31;
            case 29:
                return R.drawable.icon_weather_fuchen_29;
            case 30:
                return R.drawable.icon_weather_yangsha_30;
            case 53:
                return R.drawable.icon_weather_mai_53;
            case 54:
            case 55:
                return R.drawable.icon_weather_zhongdumai_54;
            case 56:
                return R.drawable.icon_weather_yanzhongmai_56;

            case 0:
            case 99:
            default:
                return R.drawable.icon_weather_qing_0;
        }
    }

    /**
     * 获取天气数据：
     * 整体策略是，获取缓存--》
     * 1 若缓存存在并且有效--》展示缓存
     * 2 缓存不存在--》获取网络数据--》成功后展示网络数据并将数据缓存，失败展示数据为空的界面。
     * 3 缓存存在但失效--》先展示缓存--》获取网络数据--》成功后展示网络数据并将数据缓存，失败Toast提示。
     *
     * @param lng
     * @param lat
     * @param isUseCatch 是否使用缓存数据 true 使用
     */
    public void getWeatherData(double lng, double lat, boolean isUseCatch) {
        //使用缓存，并且数据有效
        if (WeatherBeanCatch.getInstance().getWeatherInfoBeanTwo() != null) {
            if (listener != null) {
                listener.onCatchSuccess(WeatherBeanCatch.getInstance().getWeatherInfoBeanTwo());
            }
            if (isUseCatch && WeatherBeanCatch.getInstance().isValid()) {
                return;
            }
        }

        if (!NetworkUtils.isNetworkAvailable(AppApplication.getContext())) {
            if (listener != null) {
                listener.onError(WeatherBeanCatch.getInstance().getWeatherInfoBeanTwo() != null ? ERROR_TYPE_TOAST : ERROR_TYPE_NET, ERROR_MSG_NET);
            }
            return;
        }

        if (LocationData.getInstance().getLongitude() == 0d) {
            if (listener != null) {
                listener.onError(WeatherBeanCatch.getInstance().getWeatherInfoBeanTwo() != null ? ERROR_TYPE_TOAST : ERROR_TYPE_OTHER, ERROR_MSG_GPS);
            }
            return;
        }

        //从网络获取数据，并将数据缓存起来
        OkGo.<WeatherInfoBeanTwo>get(EnvironmentConfig.URL_ROOT_HOST + AppUrlConst.WEATHER_INFO_BY_COORDINATE)
                .params("lng", lng)
                .params("lat", lat)
                .execute(new JsonCallback<WeatherInfoBeanTwo>(WeatherInfoBeanTwo.class) {
                    @Override
                    public void onSuccess(Response<WeatherInfoBeanTwo> response) {
                        if (null == response || null == response.body()) {
                            if (listener != null) {
                                listener.onError(WeatherBeanCatch.getInstance().getWeatherInfoBeanTwo() != null ? ERROR_TYPE_TOAST : ERROR_TYPE_OTHER, ERROR_MSG_ERROR);
                            }
                            return;
                        }
                        WeatherInfoBeanTwo weatherInfoBeanTwo = response.body();
                        if (weatherInfoBeanTwo.getCode() != null && !"".equals(weatherInfoBeanTwo.getCode())) {
                            if (HttpCode.HTTP_OK.equals(weatherInfoBeanTwo.getCode())) {
                                //将数据缓存起来
                                WeatherBeanCatch.getInstance().setData(weatherInfoBeanTwo, System.currentTimeMillis());
                                if (listener != null) {
                                    listener.onSuccess(weatherInfoBeanTwo);
                                }
                            } else {
                                if (listener != null) {
                                    listener.onError(WeatherBeanCatch.getInstance().getWeatherInfoBeanTwo() != null ? ERROR_TYPE_TOAST : ERROR_TYPE_OTHER, ERROR_MSG_ERROR);
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Response<WeatherInfoBeanTwo> response) {
                        super.onError(response);
                        if (listener != null) {
                            listener.onError(WeatherBeanCatch.getInstance().getWeatherInfoBeanTwo() != null ? ERROR_TYPE_TOAST : ERROR_TYPE_OTHER, ERROR_MSG_ERROR);
                        }
                    }
                });
    }

    /**
     * 通过城市名称获取天气
     */
    public void getWeatherData(String cityName) {
        if (!NetworkUtils.isNetworkAvailable(AppApplication.getContext())) {
            if (listener != null) {
                listener.onError(WeatherBeanCatch.getInstance().getWeatherInfoBeanTwo() != null ? ERROR_TYPE_TOAST : ERROR_TYPE_NET, ERROR_MSG_NET);
            }
            return;
        }

        //从网络获取数据，并将数据缓存起来
        OkGo.<WeatherInfoBeanTwo>get(EnvironmentConfig.URL_ROOT_HOST + AppUrlConst.WEATHER_INFO_BY_NAME)
                .params("city", cityName)
                .execute(new JsonCallback<WeatherInfoBeanTwo>(WeatherInfoBeanTwo.class) {
                    @Override
                    public void onSuccess(Response<WeatherInfoBeanTwo> response) {
                        if (null == response || null == response.body()) {
                            if (listener != null) {
                                listener.onError(WeatherBeanCatch.getInstance().getWeatherInfoBeanTwo() != null ? ERROR_TYPE_TOAST : ERROR_TYPE_OTHER, ERROR_MSG_ERROR);
                            }
                            return;
                        }
                        WeatherInfoBeanTwo weatherInfoBeanTwo = response.body();
                        if (weatherInfoBeanTwo.getCode() != null && !"".equals(weatherInfoBeanTwo.getCode())) {
                            if (HttpCode.HTTP_OK.equals(weatherInfoBeanTwo.getCode())) {
                                if (listener != null) {
                                    listener.onSuccess(weatherInfoBeanTwo);
                                }
                            } else {
                                if (listener != null) {
                                    listener.onError(ERROR_TYPE_OTHER, ERROR_MSG_ERROR);
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Response<WeatherInfoBeanTwo> response) {
                        super.onError(response);
                        if (listener != null) {
                            listener.onError(ERROR_TYPE_OTHER, ERROR_MSG_ERROR);
                        }
                    }
                });
    }

    public interface OnWeatherListener {
        void onCatchSuccess(WeatherInfoBeanTwo infoBeanTwo);

        void onSuccess(WeatherInfoBeanTwo infoBeanTwo);

        void onError(int type, String msg);
    }

    public void setOnWeatherListener(OnWeatherListener listener) {
        this.listener = listener;
    }

    /**
     * 根据定位获取城市名称
     *
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
            ParamsUtil.setData("Location_city", sb.toString());
            return sb.toString();
        } else {
            return ParamsUtil.getKvInstance().decodeString("Location_city", "一 一，一 一");
        }
    }
}
