package com.sitechdev.vehicle.lib.util;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * 项目名称：HZ_SitechDOS
 * 类描述：设备vin、iccid等获取
 * 创建人：Administrator
 * 创建时间：2018/08/04 0004 13:36
 * 修改时间：
 * 备注：
 */
public class SerialUtils {

    private static final String TAG = SerialUtils.class.getSimpleName();

    /**
     * IhuId\SN\DeviceId  统一使用同一标识。默认的IHUID。
     */
    private static final String DEFAULT_IHUID = "0";
    public static final String DEFAULT_TBOX_ICCID = "02737181738195578576";
    public static final String DEFAULT_VIN = "URWMPMTFGQ86WKLR1";
    /**
     * 是否绑定过vin与SN
     */
    public static final String KEY_SP_BINDVIN = "BindVinSN";
    private static String tbox_IccId = "";
    private static String snCode = "";
    private static String mapSnCode = "";
    private static String vinCode = "";
    private static String vinTboxCode = "";

    /**
     * 获取车辆的SN
     * IhuId\SN\DeviceId  统一使用同一标识。
     */
    public static String getSnCode() {
        if (TextUtils.isEmpty(snCode)) {
            snCode = getFormatIhuId(Build.SERIAL, 26, "_");
        }
        return snCode;
//        return "1e0751d4ea9a216f_000000000";
    }

    /**
     * 提供给高德使用的SN
     */
    public static String getMapDeviceId() {
        if (TextUtils.isEmpty(mapSnCode)) {
            mapSnCode = getFormatIhuId(Build.SERIAL, 32, "0").toUpperCase();
        }
        return mapSnCode;
    }

    /**
     * 获取车辆TBOX的ICCID
     */
    public static String getTboxIccId() {
        if (TextUtils.isEmpty(tbox_IccId)) {
            Log.e(TAG, "get tbox iccId failed~");
            String savedIccId = (String) SPUtils.getData(AppUtils.getApp(), KEY_SP_BINDVIN, "");
            if (!StringUtils.isEmpty(savedIccId)) {
                return savedIccId;
            } else {
                return DEFAULT_TBOX_ICCID;
            }
        }
        return tbox_IccId;
//        return "89860430111840193622";
    }

    /**
     * 获取车辆的VinCode <br/>
     * 先从MCU中取vin码，如果为空或者默认值；
     * 则从TBox取，如果为空或者默认值，则返回默认值
     */
    public static String getVinCode() {
        if (!isGoodVin(vinCode)) {
            vinCode = getVinTboxCode();
            if (isGoodVin(vinCode)) {
                return vinCode;
            } else {
                return DEFAULT_VIN;
            }
        }
        return vinCode;
//        return "XTEVE101234567032";
    }

    public static boolean isGoodVin(String vinCode) {
        return !TextUtils.isEmpty(vinCode)
                && !"0".equals(vinCode)
                && !SerialUtils.DEFAULT_VIN.equals(vinCode);
    }

    /**
     * 设置车辆TBOX的ICCID
     */
    public static void setTboxIccId(String iccId) {
        tbox_IccId = iccId;
    }


    public static String getVinTboxCode() {
        return vinTboxCode;
    }

    public static void setVinTboxCode(String vinTbox) {
        vinTboxCode = vinTbox;
        if (!StringUtils.isEmpty(vinTboxCode)) {
            SitechDevLog.d(TAG,"tbox vin ->"+ vinTboxCode);
//            TraceClient.setVin(vinTboxCode);
        }
    }

    /**
     * 设置MCU vinCode
     */
    public static void setVinCode(String vin) {
        vinCode = vin;
        if (!StringUtils.isEmpty(vinCode)) {
            SitechDevLog.d(TAG,"mcu vin ->"+ vinCode);
//            TraceClient.setVin(vinCode);
        }
    }

    /**
     * 返回标准的26位的IHUID。 如果不够26位，后面补"_0"
     * <p>
     * IhuId\SN\DeviceId  统一使用同一标识。
     *
     * @param length 需要设置的sn长度
     */
    private static String getFormatIhuId(String ihuid, int length, String splitSymbol) {
        if (TextUtils.isEmpty(ihuid)) {
            return DEFAULT_IHUID;
        }
        int oldIhuIdLen = ihuid.length();
        if (oldIhuIdLen == length) {
            return ihuid;
        } else if (oldIhuIdLen > length) {
            return ihuid.substring(0, length);
        } else {
            int count = length - oldIhuIdLen - 1;
            StringBuilder sbu = new StringBuilder();
            sbu.append(ihuid).append(splitSymbol);
            for (int i = 0; i < count; i++) {
                sbu.append("0");
            }
            return sbu.toString();
        }
    }

    /**
     * 检验VIN格式
     *
     * @param vin
     * @return
     */
    public static boolean checkVIN(String vin) {
        Map<Integer, Integer> vinMapWeighting = new HashMap<>();
        Map<Character, Integer> vinMapValue = new HashMap<>();
        vinMapWeighting.put(1, 8);
        vinMapWeighting.put(2, 7);
        vinMapWeighting.put(3, 6);
        vinMapWeighting.put(4, 5);
        vinMapWeighting.put(5, 4);
        vinMapWeighting.put(6, 3);
        vinMapWeighting.put(7, 2);
        vinMapWeighting.put(8, 10);
        vinMapWeighting.put(9, 0);
        vinMapWeighting.put(10, 9);
        vinMapWeighting.put(11, 8);
        vinMapWeighting.put(12, 7);
        vinMapWeighting.put(13, 6);
        vinMapWeighting.put(14, 5);
        vinMapWeighting.put(15, 4);
        vinMapWeighting.put(16, 3);
        vinMapWeighting.put(17, 2);
        vinMapValue.put('0', 0);
        vinMapValue.put('1', 1);
        vinMapValue.put('2', 2);
        vinMapValue.put('3', 3);
        vinMapValue.put('4', 4);
        vinMapValue.put('5', 5);
        vinMapValue.put('6', 6);
        vinMapValue.put('7', 7);
        vinMapValue.put('8', 8);
        vinMapValue.put('9', 9);
        vinMapValue.put('A', 1);
        vinMapValue.put('B', 2);
        vinMapValue.put('C', 3);
        vinMapValue.put('D', 4);
        vinMapValue.put('E', 5);
        vinMapValue.put('F', 6);
        vinMapValue.put('G', 7);
        vinMapValue.put('H', 8);
        vinMapValue.put('J', 1);
        vinMapValue.put('K', 2);
        vinMapValue.put('M', 4);
        vinMapValue.put('L', 3);
        vinMapValue.put('N', 5);
        vinMapValue.put('P', 7);
        vinMapValue.put('R', 9);
        vinMapValue.put('S', 2);
        vinMapValue.put('T', 3);
        vinMapValue.put('U', 4);
        vinMapValue.put('V', 5);
        vinMapValue.put('W', 6);
        vinMapValue.put('X', 7);
        vinMapValue.put('Y', 8);
        vinMapValue.put('Z', 9);
        boolean resultFlag = false;
        String upperVin = vin.toUpperCase();
        //排除字母O、I
        if (TextUtils.isEmpty(vin) || upperVin.indexOf("O") >= 0 || upperVin.indexOf("I") >= 0) {
            resultFlag = false;
        } else {
            try {
                if (vin.length() == 17) {
                    char[] vinArr = upperVin.toCharArray();
                    int amount = 0;
                    for (int i = 0; i < vinArr.length; i++) {
                        //VIN码从从第一位开始，码数字的对应值×该位的加权值，计算全部17位的乘积值相加
                        if (null != vinMapValue.get(vinArr[i])) {
                            amount += vinMapValue.get(vinArr[i]) * vinMapWeighting.get(i + 1);
                        }
                    }
                    //乘积值相加除以11、若余数为10，即为字母Ｘ
                    if (amount % 11 == 10) {
                        resultFlag = vinArr[8] == 'X';
                    } else {
                        //VIN码从从第一位开始，码数字的对应值×该位的加权值，
                        //计算全部17位的乘积值相加除以11，所得的余数，即为第九位校验值
                        resultFlag = amount % 11 == vinMapValue.get(vinArr[8]);
                    }
                } else {
                    resultFlag = false;
                }
            } catch (Exception e) {
                Log.e(TAG, "vin is invalid!");
            }
        }
        return resultFlag;
    }
}
