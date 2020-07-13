package com.sitechdev.vehicle.pad.module.location;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.AMapLocationQualityReport;
import com.amap.api.location.CoordinateConverter;
import com.amap.api.location.DPoint;
import com.sitechdev.vehicle.lib.event.EventBusUtils;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.lib.util.StringUtils;
import com.sitechdev.vehicle.pad.app.AppApplication;
import com.sitechdev.vehicle.pad.app.AppConst;
import com.sitechdev.vehicle.pad.event.MapEvent;
import com.sitechdev.vehicle.pad.module.map.util.LocationData;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * 项目名称：SitechVehiclePad
 * 类名称：LocationUtil
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2019/08/19 0019 14:54
 * 修改时间：
 * 备注：
 */
public class LocationUtil {
    private static final String TAG = AppConst.TAG + "==>LocationUtil";

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private SimpleDateFormat sdf = null;
    private Context mContext = null;
    /**
     * 定位次数
     */
    private int locCount = 0;

    private volatile static boolean isInited = false;

    private LocationUtil() {
    }

    private static class SingleLocation {
        private static final LocationUtil SINGLE = new LocationUtil();
    }

    public static LocationUtil getInstance() {
        return SingleLocation.SINGLE;
    }

    /**
     * 初始化高德地图设置
     *
     * @param start 是否立即启动定位
     */
    public void init(Context context, boolean start) {
        if (isInited) {
            return;
        }
        //初始化client
        mContext = context.getApplicationContext();
        locationClient = new AMapLocationClient(mContext);
        locationOption = getDefaultOption();
        //设置定位参数
        locationClient.setLocationOption(locationOption);
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
        locationClient.disableBackgroundLocation(true);
        if (start) {
            SitechDevLog.e(TAG, "定位，start======>" + getInstance());
            startLocation();
        }
        isInited = true;
    }

    /**
     * 开始定位
     */
    private void startLocation() {
        // 启动定位
        locationClient.startLocation();
    }

    /**
     * 停止定位
     */
    private void stopLocation() {
        // 停止定位
        locationClient.stopLocation();
    }

    /**
     * 定位监听
     */
    private AMapLocationListener locationListener = aMapLocation -> {
        if (null != aMapLocation) {
            if (locCount > 3) {
                refreshLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            }
            String locationStr = getLocationStr(aMapLocation);
            SitechDevLog.i(TAG, locationStr);
        } else {
            SitechDevLog.e(TAG, "定位失败，loc is null");
        }
    };

    /**
     * 根据定位结果返回定位信息的字符串
     */
    private String getLocationStr(AMapLocation location) {
        if (null == location) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
        locCount++;
        if (location.getErrorCode() == 0) {
            sb.append("定位成功-->" +
                    "\n定位类型:" + location.getLocationType() +
                    "\n经    度:" + location.getLongitude() +
                    "\n纬    度:" + location.getLatitude() +
                    "\n省      :" + location.getProvince() +
                    "\n市      :" + location.getCity() +
                    "\n区/县   :" + location.getDistrict() +
                    "\n地    址:" + location.getAddress() +
                    "\n兴 趣 点:" + location.getPoiName() +
                    "\n定位时间:" + formatUTC(location.getTime(), "yyyy - MM - dd HH:mm:ss ") + "\n");

            LocationData.getInstance().setaMapLocation(location);

            //设置当前经纬度
            LocationData.getInstance().setLatitude(location.getLatitude());
            LocationData.getInstance().setLongitude(location.getLongitude());
            LocationData.getInstance().setFormatAddress(location.getAddress());

            if (!StringUtils.isEmpty(location.getProvince())) {
                LocationData.getInstance().setProvinceName(location.getProvince());
            }
            if (!StringUtils.isEmpty(location.getCity())) {
                LocationData.getInstance().setCityName(location.getCity());
            }
            if (!StringUtils.isEmpty(location.getDistrict())) {
                LocationData.getInstance().setDistrictName(location.getDistrict());
            }
            //定位成功，直接刷新数据
            EventBusUtils.postEvent(new MapEvent(MapEvent.EVENT_LOCATION_SUCCESS));
        } else {
//            //定位失败
            sb.append("定位失败-->" +
                    "\n错误码:" + location.getErrorCode() +
                    "\n错误信息:" + location.getErrorInfo() +
                    "\n错误描述:" + location.getLocationDetail() + "\n");
        }
        sb.append("***定位质量报告***" +
                "\nWIFI开关：" + (location.getLocationQualityReport().isWifiAble() ? "开启" : "关闭") +
                "\nGPS状态：" + getGPSStatusString(location.getLocationQualityReport().getGPSStatus()) +
                "\nGPS星数：" + location.getLocationQualityReport().getGPSSatellites() +
                "\n网络类型：" + location.getLocationQualityReport().getNetworkType() +
                "\n网络耗时：" + location.getLocationQualityReport().getNetUseTime() +
                "\n****************").append("\n");
//        //定位之后的回调时间
        sb.append("回调时间: " + formatUTC(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss") + "\n");
        return sb.toString();
    }

    /**
     * 默认的定位参数
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(5 * 1000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(true);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        mOption.setGeoLanguage(AMapLocationClientOption.GeoLanguage.DEFAULT);//可选，设置逆地理信息的语言，默认值为默认语言（根据所在地区选择语言）
        return mOption;
    }

    /**
     * AMapLocationMode.Battery_Saving，低功耗定位模式，不会使用GPS和其他传感器，只会使用网络定位（Wi-Fi和基站定位）；
     * AMapLocationMode.Hight_Accuracy，高精度模式,会同时使用网络定位和GPS定位，优先返回最高精度的定位结果，以及对应的地址描述信息。
     * AMapLocationMode.Device_Sensors，仅用设备定位模式，只使用GPS进行定位，仅适合室外环境。
     */
    private void refreshLocationMode(AMapLocationClientOption.AMapLocationMode locationMode) {
        locationOption.setLocationMode(locationMode);
        locationClient.setLocationOption(locationOption);
    }

    /**
     * 获取GPS状态的字符串
     *
     * @param statusCode GPS状态码
     * @return 定位信息
     */
    private String getGPSStatusString(int statusCode) {
        switch (statusCode) {
            case AMapLocationQualityReport.GPS_STATUS_OK:
                return "GPS状态正常";
            case AMapLocationQualityReport.GPS_STATUS_NOGPSPROVIDER:
                return "手机中没有GPS Provider，无法进行GPS定位";
            case AMapLocationQualityReport.GPS_STATUS_OFF:
                return "GPS关闭，建议开启GPS，提高定位质量";
            case AMapLocationQualityReport.GPS_STATUS_MODE_SAVING:
                return "选择的定位模式中不包含GPS定位，建议选择包含GPS定位的模式，提高定位质量";
            case AMapLocationQualityReport.GPS_STATUS_NOGPSPERMISSION:
                return "没有GPS定位权限，建议开启gps定位权限";
            default:
                return "定位失败";
        }
    }

    private DPoint gpsToGaode(DPoint sourceLatLng) {
        CoordinateConverter converter = new CoordinateConverter(AppApplication.getContext());
        try {
            // CoordType.GPS 待转换坐标类型
            converter.from(CoordinateConverter.CoordType.GPS);
            // sourceLatLng待转换坐标点 LatLng类型
            converter.coord(sourceLatLng);
            // 执行转换操作
            return converter.convert();
        } catch (Exception e) {
            SitechDevLog.exception(e);
        }
        return null;
    }

    private String formatUTC(long l, String strPattern) {
        String datePattern = strPattern;
        if (StringUtils.isEmpty(strPattern)) {
            datePattern = "yyyy-MM-dd HH:mm:ss";
        }
        if (sdf == null) {
            try {
                sdf = new SimpleDateFormat(datePattern, Locale.CHINA);
            } catch (Exception e) {
                SitechDevLog.exception(e);
            }
        } else {
            sdf.applyPattern(datePattern);
        }
        if (sdf == null) {
            return "NULL";
        } else {
            return sdf.format(l);
        }
    }
}
