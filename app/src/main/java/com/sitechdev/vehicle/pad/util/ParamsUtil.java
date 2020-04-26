package com.sitechdev.vehicle.pad.util;

import android.util.Base64;

import com.sitechdev.vehicle.lib.util.StringUtils;
import com.sitechdev.vehicle.pad.app.AppApplication;
import com.tencent.mars.xlog.Log;
import com.tencent.mmkv.MMKV;
import com.tencent.mmkv.MMKVHandler;
import com.tencent.mmkv.MMKVLogLevel;
import com.tencent.mmkv.MMKVRecoverStrategic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 项目名称：SitechVehiclePad-5th
 * 类名称：ParamsUtil
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2020/04/22 0022 11:59
 * 修改时间：
 * 备注：
 */
public class ParamsUtil implements MMKVHandler {

    private static MMKV kvInstance = null;
    private static final String LOGTAG = "SitechMMKV";

    /**
     * MMKV的初始化。需要在application的onCreate中执行
     */
    public static void init() {
        String rootDir = MMKV.initialize(AppApplication.getContext());
        Log.i(LOGTAG, "mmkv root: " + rootDir);
        kvInstance = MMKV.defaultMMKV();
    }

    /**
     * 获取mmkv的实例
     *
     * @return
     */
    public static MMKV getKvInstance() {
        if (kvInstance == null) {
            kvInstance = MMKV.defaultMMKV();
        }
        return kvInstance;
    }

    /**
     * 保存数据
     *
     * @param key
     * @param value
     */
    public static void setData(String key, Object value) {
        if (kvInstance == null) {
            kvInstance = MMKV.defaultMMKV();
        }
        if (value instanceof String) {
            kvInstance.encode(key, (String) value);
        } else if (value instanceof Integer) {
            kvInstance.encode(key, (int) value);
        } else if (value instanceof Long) {
            kvInstance.encode(key, (long) value);
        } else if (value instanceof Double) {
            kvInstance.encode(key, (double) value);
        } else if (value instanceof Float) {
            kvInstance.encode(key, (Float) value);
        } else if (value instanceof Boolean) {
            kvInstance.encode(key, (boolean) value);
        }
    }

    public static void setBeanData(String key, Object value) {
        String valueStr = object2String(value);
        kvInstance.encode(key, valueStr);
    }

    public static Object getBeanData(String key) {
        return string2Object(kvInstance.decodeString(key));
    }

    @Override
    public MMKVRecoverStrategic onMMKVCRCCheckFail(String s) {
        return MMKVRecoverStrategic.OnErrorRecover;
    }

    @Override
    public MMKVRecoverStrategic onMMKVFileLengthError(String s) {
        return MMKVRecoverStrategic.OnErrorRecover;
    }

    /**
     * 返回true,代表log输出交由app组件管理
     *
     * @return
     */
    @Override
    public boolean wantLogRedirecting() {
        return true;
    }

    /**
     * MMKV 默认将日志打印到 logcat，对线上问题的解决很不便。你可以在 App 启动时接收转发 MMKV 的日志。
     */
    @Override
    public void mmkvLog(MMKVLogLevel level, String file, int line, String func, String message) {
        String log = "<" + file + ":" + line + "::" + func + "> " + message;
        switch (level) {
            case LevelDebug:
                Log.d(LOGTAG, log);
                break;
            case LevelInfo:
                Log.i(LOGTAG, log);
                break;
            case LevelWarning:
                Log.w(LOGTAG, log);
                break;
            case LevelError:
                Log.e(LOGTAG, log);
                break;
            case LevelNone:
            default:
                break;
        }
    }

    /**
     * writeObject 方法负责写入特定类的对象的状态，以便相应的 readObject 方法可以还原它
     * 最后，用Base64.encode将字节文件转换成Base64编码保存在String中
     *
     * @param object 待加密的转换为String的对象
     * @return String   加密后的String
     */
    private static String object2String(Object object) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = null;
        try {
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);
            String string = new String(Base64.encode(byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
            objectOutputStream.close();
            return string;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 使用Base64解密String，返回Object对象
     *
     * @param objectString 待解密的String
     * @return object      解密后的object
     */
    private static Object string2Object(String objectString) {
        if (StringUtils.isEmpty(objectString)) {
            return null;
        }
        byte[] mobileBytes = Base64.decode(objectString.getBytes(), Base64.DEFAULT);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(mobileBytes);
        ObjectInputStream objectInputStream = null;
        try {
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Object object = objectInputStream.readObject();
            objectInputStream.close();
            return object;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
