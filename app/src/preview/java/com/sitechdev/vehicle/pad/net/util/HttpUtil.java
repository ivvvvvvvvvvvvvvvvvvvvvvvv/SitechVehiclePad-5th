package com.sitechdev.vehicle.pad.net.util;

import com.sitechdev.net.EnvironmentConfig;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.pad.app.AppUrlConst;

import org.json.JSONObject;

/**
 * 项目名称：Sitech
 * 类名称：HttpUtil
 * 类描述：
 * 创建人：shaozhi
 * 创建时间：2018/03/12 10:12
 * 修改时间：
 * 备注：
 */
public class HttpUtil {

    public static String getResponseBodyString(String responseString) {
        try {
            JSONObject responseJsonString = new JSONObject(responseString);
            if (responseJsonString.has("data")) {
                return responseJsonString.getString("data");
            }
        } catch (Exception e) {
            SitechDevLog.exception(e);
        }
        return "";
    }

    /**
     * 返回增加host的请求地址
     *
     * @param pathUrl path
     * @return EX： http://12.3.4.5/pathurl
     */
    public static String getFormatRequestUrl(String host, String pathUrl) {
        return String.format("%s%s", host, pathUrl);
    }

    /**
     * 返回用户体系的相关的最终请求地址
     *
     * @param pathUrl path
     * @return EX： http://12.3.4.5/pathurl
     */
    public static String formatUserFinalRequestUrl(String pathUrl) {
        return getFormatRequestUrl(EnvironmentConfig.URL_ROOT_HOST, pathUrl);
    }


    /**
     * 返回商城相关的最终请求地址
     *
     * @param pathUrl path
     * @return EX： http://12.3.4.5/pathurl
     */
    public static String formatMallUrlRequestUrl(String pathUrl) {
        return getFormatRequestUrl(EnvironmentConfig.URL_MALL_HOST, pathUrl);
    }

    public static String formatBBSUrlRequestUrl(String pathUrl) {
        return getFormatRequestUrl(EnvironmentConfig.BBS_HOST, pathUrl);
    }

}
