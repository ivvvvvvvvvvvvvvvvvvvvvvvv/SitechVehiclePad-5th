package com.sitechdev.vehicle.lib.util;

import android.content.Context;
import android.provider.Settings;

import java.util.UUID;

/**
 * 创建人：shaozhi
 */
public class XTIDUtil {

    /**
     * 设备唯一标识信息，保存KEY-对“COMSITECHDEVSITECH_DEVICE_ID”进行MD5加密
     */
    private static final String deviceID_KEY = "D3D9520D58FDE17FE9B84C5C4FDCBDFB";
    /**
     * 保存内容
     */
    private static String deviceID_Content = "";

    public static void init(Context context) {
        deviceID_Content = getDeviceID(context);
    }

    public static void setDeviceID_Content(String deviceID_Content) {
        XTIDUtil.deviceID_Content = deviceID_Content;
    }

    /**
     * 获取 ID
     *
     * @return
     */
    public static String getDeviceID() {
        return deviceID_Content;
    }

    /**
     * 获取deviceID
     *
     * @param context context
     * @return deviceID
     */
    private static String getDeviceID(Context context) {
        if (context == null) {
            return "null";
        }
        String deviceID = readDeviceID(context);
        SitechDevLog.i("UUID", "readDeviceID===>" + deviceID);

        //是否存在，不存在，则重新创建
        if (StringUtils.isEmpty(deviceID)) {
            deviceID = createNewDeviceID(context);
        }
        return deviceID;
    }

    /**
     * 创建新的DeviceID
     *
     * @param context context
     * @return DeviceID
     */
    private static String createNewDeviceID(Context context) {

        String serial = android.os.Build.SERIAL;
        String android_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(android_id.hashCode(), ((long) android_id.hashCode() << 32) | serial.hashCode());
        String deviceId = deviceUuid.toString();
        SitechDevLog.i("splashLogic", "deviceId====>" + deviceId);
        //对生产的deviceId进行md5加密后，保存
        String passwordDeviceID = AppUtils.md5EnCode(deviceId).toUpperCase();
        SitechDevLog.i("splashLogic", "newDeviceID====>" + passwordDeviceID);
        //保存deviceID
        saveDeviceID(context, passwordDeviceID);
        return passwordDeviceID;
    }

    /**
     * 重置 DeviceID
     *
     * @param context context
     */
    public static void resetDeviceID(Context context) {
        saveDeviceID(context, "");
    }

    /**
     * 保存新的deviceID
     *
     * @param context  context
     * @param deviceID deviceID
     */
    private static void saveDeviceID(Context context, String deviceID) {
        SPUtils.putValue(context, deviceID_KEY, deviceID);
    }

    /**
     * 读取存在的 deviceID
     *
     * @param context context
     * @return deviceID
     */
    private static String readDeviceID(Context context) {
        return SPUtils.getValue(context, deviceID_KEY, createNewDeviceID(context));
    }
}
