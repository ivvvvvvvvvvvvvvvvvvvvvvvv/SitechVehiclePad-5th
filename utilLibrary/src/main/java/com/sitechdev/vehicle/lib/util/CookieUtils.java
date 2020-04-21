package com.sitechdev.vehicle.lib.util;

import android.content.Context;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

/**
 * WebView cookie设置
 * @date 2019/03/23
 * @author liuhe
 */
public class CookieUtils {

    public static void syncCookie(Context context, String url, String cookie) {
        try {
            CookieSyncManager.createInstance(context.getApplicationContext());
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.removeSessionCookie();// 移除以前的cookie
            cookieManager.removeAllCookie();
            //为url设置cookie
            cookieManager.setCookie(getDomain(url), cookie);
            CookieSyncManager.getInstance().sync();//同步cookie 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getDomain(String url) {
        url = url.replace("http://", "").replace("https://", "");
        if (url.contains("/")) {
            url = url.substring(0, url.indexOf('/'));
        }
        return url;
    }
}
