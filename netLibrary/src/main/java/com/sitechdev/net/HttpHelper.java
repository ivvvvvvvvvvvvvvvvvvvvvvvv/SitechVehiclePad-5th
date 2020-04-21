package com.sitechdev.net;

import android.app.Application;
import android.util.Log;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.lzy.okgo.model.HttpHeaders;
import com.sitechdev.vehicle.lib.util.DeviceUtils;
import com.sitechdev.vehicle.lib.util.NetworkUtils;
import com.sitechdev.vehicle.lib.util.PackageInfoUtils;
import com.sitechdev.vehicle.lib.util.SerialUtils;
import com.sitechdev.vehicle.lib.util.XTIDUtil;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 * @author liuhe
 */
public class HttpHelper {

    private static final String TAG = HttpHelper.class.getSimpleName();

    private ArrayList<Interceptor> appInterceptorList = new ArrayList<>();

    private ArrayList<Interceptor> appNetInterceptorList = new ArrayList<>();

    private static class Holder {
        private static final HttpHelper instance = new HttpHelper();
    }

    public static HttpHelper getInstance() {
        return Holder.instance;
    }

    /**
     * 网络初始化。
     * 网络主要用在poi相关功能
     */
    public void initNet(Application application, boolean showLogs) {
        DeviceUtils.init(application.getApplicationContext());
        try {
            OkGo.getInstance().init(application)
                    .setOkHttpClient(getOKHttpClient(showLogs))
                    .setCacheMode(CacheMode.NO_CACHE)
                    .addCommonHeaders(buildCommonHeaders())
                    .setRetryCount(3);
        } catch (Throwable e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public OkHttpClient getOKHttpClient(boolean showLogs) {
        return getOkHttpClientBuilder(showLogs).build();
    }

    private OkHttpClient.Builder getOkHttpClientBuilder(boolean showLogs) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        if (!appNetInterceptorList.isEmpty()) {
            for (Interceptor interceptor : appNetInterceptorList) {
                builder.addNetworkInterceptor(interceptor);
            }
        }

        if (!appInterceptorList.isEmpty()) {
            for (Interceptor interceptor : appInterceptorList) {
                builder.addInterceptor(interceptor);
            }
        }

        if (showLogs) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("SitechVehicleLog-Net");
            loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
            loggingInterceptor.setColorLevel(Level.INFO);
            builder.addInterceptor(loggingInterceptor);
        }

        //超时时间设置，默认60秒
        builder.readTimeout(60 * 1000, TimeUnit.MILLISECONDS);
        builder.writeTimeout(60 * 1000, TimeUnit.MILLISECONDS);
        builder.connectTimeout(60 * 1000, TimeUnit.MILLISECONDS);
        builder.retryOnConnectionFailure(true);
        builder.followRedirects(true);
        builder.followSslRedirects(true);
        return builder;
    }

    /**
     * 增加自定义拦截器。一定要在初始化OkGo实例之前调用
     *
     * @param interceptors interceptors
     */
    public void addAppInterceptor(Interceptor... interceptors) {
        if (interceptors == null) {
            return;
        }
        for (Interceptor interceptor : interceptors) {
            appInterceptorList.add(interceptor);
        }
    }

    /**
     * 增加自定义网络拦截器。一定要在初始化OkGo实例之前调用
     *
     * @param interceptors interceptors
     */
    public void addNetInterceptor(Interceptor... interceptors) {
        if (interceptors == null) {
            return;
        }
        for (Interceptor interceptor : interceptors) {
            appNetInterceptorList.add(interceptor);
        }
    }

    public HttpHeaders buildCommonHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.put("User-Agent", "SitechDev_VehiclePad;android;v" + PackageInfoUtils.getAppVersionName() + ";" + DeviceUtils.getScreenInfo());
        headers.put("Accept", "application/json; charset=UTF-8");
        //iOS/android... - 设备类型
        headers.put("Device-Type", DeviceUtils.getPhoneModel());
        //设备惟一识别编码
        headers.put("Device-ID", XTIDUtil.getDeviceID());
        //设备名称
        headers.put("Device-Name", DeviceUtils.getPhoneBrand() + " " + DeviceUtils.getPhoneModel());
        headers.put("Device-DevOsName", DeviceUtils.getBuildInfo());
        headers.put("Device-DevOsVersion", "" + PackageInfoUtils.getSystemVersionCode());
        headers.put("Device-DevOsType", "Android");
        headers.put("AppVersion", "" + PackageInfoUtils.getAppVersionCode());
        headers.put("Device-Network", NetworkUtils.getNetworkType().toString());
        //来源，如：d_os：新特中控操作系统，native_app：原生app，wx_mapp：微信小程序，wx_sub：微信公众号，web_m：M站，web_pc：pc站，web_pad：平板电脑等,native_pad:车机应用版
        headers.put("source", "native_pad");
        headers.put("Connection", "close");
        return headers;
    }
}